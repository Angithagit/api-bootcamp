package utilities;


import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties prop;

    static {
        try {
            prop = new Properties();
            FileInputStream input = new FileInputStream("src/test/resources/config/config.properties");
            prop.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
