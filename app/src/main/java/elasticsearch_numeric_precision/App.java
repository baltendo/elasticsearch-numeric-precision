package elasticsearch_numeric_precision;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static elasticsearch_numeric_precision.BigDecimalUtil.combine;
import static elasticsearch_numeric_precision.BigDecimalUtil.sum;
import static elasticsearch_numeric_precision.BigDecimalUtil.withScale;
import static elasticsearch_numeric_precision.DataUtil.generate;
import static elasticsearch_numeric_precision.FileUtil.writeLines;
import static elasticsearch_numeric_precision.ModelUtil.readModels;
import static elasticsearch_numeric_precision.ResultUtil.difference;

@Slf4j
public class App {

    public static void main(String[] args) throws Exception {
        int number = 9973;
        int maxDigitsIntegral = 2;
        int maxDigitsFractional, scale;
        maxDigitsFractional = scale = 15;

        RoundingMode roundingMode = RoundingMode.HALF_EVEN;

        //List<String> lines = generate(number, maxDigitsIntegral, maxDigitsFractional);
        //writeLines("app/src/main/resources/data.txt", lines);

        ElasticsearchService elasticsearchService = new ElasticsearchService();
        elasticsearchService.putIndexTemplate();

        elasticsearchService.deleteIndices();

        List<Model> models = readModels(scale, roundingMode);
        elasticsearchService.put(models);
        Thread.sleep(3_000);

        // Java

        // BigDecimal

        BigDecimal size = BigDecimal.valueOf(models.size());
        log.info("Number of models: {}", size);

        List<BigDecimal> bigDecimals = models.stream()
            .map(Model::getBigDecimal)
            .toList();

        BigDecimal sumBigDecimal = sum(bigDecimals).setScale(scale, roundingMode);

        Result javaBigDecimal = Result.builder()
            .sum(sumBigDecimal)
            .avg(sumBigDecimal.divide(size, roundingMode).setScale(scale, roundingMode))
            .build();

        log.info("javaBigDecimal: {}", javaBigDecimal);

        // Elasticsearch

        SearchResponse<Void> searchResponse = elasticsearchService.query();
        Map<String, Aggregate> aggregations = searchResponse.aggregations();

        // float

        Result elasticsearchFloat = Result.builder()
            .sum(withScale(aggregations.get("sum_amount_float").sum().value(), scale, roundingMode))
            .avg(withScale(aggregations.get("avg_amount_float").avg().value(), scale, roundingMode))
            .build();

        log.info("elasticsearchFloat: {}", elasticsearchFloat);

        Result javaBigDecimalVsElasticsearchFloat = difference(javaBigDecimal, elasticsearchFloat);
        log.info("javaBigDecimal vs. elasticsearchFloat: {}", javaBigDecimalVsElasticsearchFloat);

        // double

        Result elasticsearchDouble = Result.builder()
            .sum(withScale(aggregations.get("sum_amount_double").sum().value(), scale, roundingMode))
            .avg(withScale(aggregations.get("avg_amount_double").avg().value(), scale, roundingMode))
            .build();

        log.info("elasticsearchDouble: {}", elasticsearchDouble);

        Result javaBigDecimalVsElasticsearchDouble = difference(javaBigDecimal, elasticsearchDouble);
        log.info("javaBigDecimal vs. elasticsearchDouble: {}", javaBigDecimalVsElasticsearchDouble);

        // scaled_float

        Result elasticsearchScaledFloat = Result.builder()
            .sum(withScale(aggregations.get("sum_amount_scaled_float").sum().value(), scale, roundingMode))
            .avg(withScale(aggregations.get("avg_amount_scaled_float").avg().value(), scale, roundingMode))
            .build();

        log.info("elasticsearchScaledFloat: {}", elasticsearchScaledFloat);

        Result javaBigDecimalVsElasticsearchScaledFloat = difference(javaBigDecimal, elasticsearchScaledFloat);
        log.info("javaBigDecimal vs. elasticsearchScaledFloat: {}", javaBigDecimalVsElasticsearchScaledFloat);

        // integral & fractional

        BigDecimal sumAmountIntegral = withScale(aggregations.get("sum_amount_integral").sum().value(), scale, roundingMode);
        BigDecimal sumAmountFractional = withScale(aggregations.get("sum_amount_fractional").sum().value(), scale, roundingMode);
        BigDecimal sumAmountIntegralFractional = combine(sumAmountIntegral, sumAmountFractional, scale, roundingMode);
        BigDecimal totalIntegralFractional = withScale(searchResponse.hits().total().value(), scale, roundingMode);

        Result elasticsearchScaledIntegralFractional = Result.builder()
            .sum(sumAmountIntegralFractional)
            .avg(sumAmountIntegralFractional.divide(totalIntegralFractional, roundingMode))
            .build();

        log.info("elasticsearchScaledIntegralFractional: {}", elasticsearchScaledIntegralFractional);

        Result javaBigDecimalVsElasticsearchScaledIntegralFractional = difference(javaBigDecimal, elasticsearchScaledIntegralFractional);
        log.info("javaBigDecimal vs. elasticsearchScaledIntegralFractional: {}", javaBigDecimalVsElasticsearchScaledIntegralFractional);

        log.info("---");

        log.info("Number of numbers: {}", size);

        log.info("---");

        log.info("javaBigDecimal:                        {}", javaBigDecimal);
        log.info("elasticsearchFloat:                    {}", elasticsearchFloat);
        log.info("elasticsearchDouble:                   {}", elasticsearchDouble);
        log.info("elasticsearchScaledIntegralFractional: {}", elasticsearchScaledIntegralFractional);
        log.info("elasticsearchScaledFloat:              {}", elasticsearchScaledFloat);

        log.info("---");

        log.info("javaBigDecimal vs. elasticsearchFloat:                    {}", javaBigDecimalVsElasticsearchFloat);
        log.info("javaBigDecimal vs. elasticsearchDouble:                   {}", javaBigDecimalVsElasticsearchDouble);
        log.info("javaBigDecimal vs. elasticsearchScaledIntegralFractional: {}", javaBigDecimalVsElasticsearchScaledIntegralFractional);
        log.info("javaBigDecimal vs. elasticsearchScaledFloat:              {}", javaBigDecimalVsElasticsearchScaledFloat);

        System.exit(0);
    }

}
