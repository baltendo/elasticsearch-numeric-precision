package elasticsearch_numeric_precision;

import com.google.common.base.Strings;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public final class ModelUtil {

    private ModelUtil() {

    }

    public static List<Model> readModels(int scale, RoundingMode roundingMode) throws Exception {
        List<Model> models = new LinkedList<>();

        URL url = Resources.getResource("data.txt");

        long lineCount = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Resources.asByteSource(url).openStream()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                lineCount++;

                BigDecimal bigDecimal = new BigDecimal(line).setScale(scale, roundingMode);

                String[] parts = line.split("\\.", 2);
                String integralString = parts[0];
                BigInteger integral = new BigInteger(integralString);

                String fractionalString = Strings.padEnd(parts[1], scale, '0').substring(0, scale);
                BigInteger fractional = new BigInteger(fractionalString);

                log.info("reading lineCount {} line {} integralString {} integral {} fractionalString {} fractional {} to bigDecimal {}",
                    lineCount, line, integralString, integral, fractionalString, fractional, bigDecimal);

                Model model = Model.builder()
                    .line(lineCount)
                    .bigDecimal(bigDecimal)
                    .amountFloat(bigDecimal.doubleValue())
                    .amountDouble(bigDecimal.doubleValue())
                    .amountScaledFloat(bigDecimal.doubleValue())
                    .amountIntegral(integral.longValue())
                    .amountFractional(fractional.longValue())
                    .build();

                models.add(model);

                line = bufferedReader.readLine();
            }
        }

        return models;
    }
}
