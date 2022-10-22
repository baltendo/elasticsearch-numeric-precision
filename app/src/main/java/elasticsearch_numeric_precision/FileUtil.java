package elasticsearch_numeric_precision;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.Files;

import java.io.File;
import java.util.List;

public final class FileUtil {

    private FileUtil() {

    }

    public static void writeLines(String path, List<String> lines) throws Exception {
        File file = new File(path);
        CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
        sink.writeLines(lines, "\n");
    }

}
