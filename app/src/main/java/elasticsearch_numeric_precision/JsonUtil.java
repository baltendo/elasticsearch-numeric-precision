package elasticsearch_numeric_precision;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public final class JsonUtil {

    public static final ObjectMapper JSON;

    static {
        SimpleModule module = new SimpleModule();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(ZonedDateTime.class,
            new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")));

        JSON = new ObjectMapper();
        //JSON.registerModules(module, new Jdk8Module());
        JSON.registerModules(module, javaTimeModule);

        JSON.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JSON.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);

        JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JSON.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    private JsonUtil() {

    }

    public static Map<String, Object> toMap(Object object) {
        try {
            return JSON.convertValue(object, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static <T> T fromMap(Map<String, Object> map, Class<T> type) {
        try {
            return JSON.convertValue(map, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static String convertToJsonString(Object object) {
        try {
            return JSON.writeValueAsString(object).replaceAll("\"", "");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static String print(Object object) {
        try {
            return JSON.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static Map<String, Object> readFileToMap(Path path) throws IOException {
        String json = Files.readString(path);
        return JsonUtil.JSON.readValue(json, new TypeReference<>() {
        });
    }

}
