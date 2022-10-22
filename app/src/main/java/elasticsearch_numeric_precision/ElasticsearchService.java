package elasticsearch_numeric_precision;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateRequest;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ObjectBuilder;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ElasticsearchService {

    private final ElasticsearchClient client;

    public ElasticsearchService() {
        this.client = createClient();
    }

    public void putIndexTemplate() throws Exception {
        String templateName = "test";
        URL url = Resources.getResource("mapping.json");

        try (InputStream inputStream = Resources.asByteSource(url).openStream()) {
            PutIndexTemplateRequest putIndexTemplateRequest = PutIndexTemplateRequest.of(builder -> builder
                .name(templateName)
                .indexPatterns("test-*")
                .template(templateBuilder -> templateBuilder.withJson(inputStream)));

            PutIndexTemplateResponse putIndexTemplateResponse = client.indices().putIndexTemplate(putIndexTemplateRequest);
            log.info("putIndexTemplate {} {}", templateName, putIndexTemplateResponse.acknowledged());
        }
    }

    public void deleteIndices() throws Exception {
        DeleteIndexRequest deleteIndexRequest = DeleteIndexRequest.of(new Function<DeleteIndexRequest.Builder, ObjectBuilder<DeleteIndexRequest>>() {
            @Override
            public ObjectBuilder<DeleteIndexRequest> apply(DeleteIndexRequest.Builder builder) {
                return builder.index("test-*");
            }
        });

        DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest);
        log.info("deleteIndexResponse {}", deleteIndexResponse.acknowledged());

    }

    public void put(List<Model> models) throws Exception {
        final String indexName = "test-1";
        BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

        for (Model model : models) {
            bulkRequestBuilder.operations(builder -> builder
                .index((Function<IndexOperation.Builder<Model>, ObjectBuilder<IndexOperation<Model>>>) builder1 -> builder1
                    .index(indexName)
                    .document(model)));
        }

        BulkResponse bulkResponse = client.bulk(bulkRequestBuilder.build());
        log.info("bulkResponse {} errors {}", bulkResponse.items().size(), bulkResponse.errors());
    }

    public SearchResponse<Void> query() throws Exception {
        final String indexName = "test-1";
        return client.search(b -> b
                .index(indexName)
                .size(0)
                .query(MatchAllQuery.of(builder -> builder)._toQuery())
                .aggregations("sum_amount_float", builder -> builder
                    .sum(builder1 -> builder1.field("amount.float"))
                )
                .aggregations("avg_amount_float", builder -> builder
                    .avg(builder1 -> builder1.field("amount.float"))
                )
                .aggregations("sum_amount_double", builder -> builder
                    .sum(builder1 -> builder1.field("amount.double"))
                )
                .aggregations("avg_amount_double", builder -> builder
                    .avg(builder1 -> builder1.field("amount.double"))
                )
                .aggregations("sum_amount_scaled_float", builder -> builder
                    .sum(builder1 -> builder1.field("amount.scaled_float"))
                )
                .aggregations("avg_amount_scaled_float", builder -> builder
                    .avg(builder1 -> builder1.field("amount.scaled_float"))
                )
                .aggregations("sum_amount_integral", builder -> builder
                    .sum(builder1 -> builder1.field("amount.integral"))
                )
                .aggregations("sum_amount_fractional", builder -> builder
                    .sum(builder1 -> builder1.field("amount.fractional"))
                ),
            Void.class
        );
    }

    private ElasticsearchClient createClient() {
        RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

        ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }


}
