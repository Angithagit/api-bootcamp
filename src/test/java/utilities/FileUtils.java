package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readJsonFromFile(String fileName) {
        String path = "src/test/resources/RequestBody/" + fileName;
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }
}
