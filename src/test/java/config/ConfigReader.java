package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.http.Header;

public class ConfigReader {

	private static Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return properties.getProperty(key);
	}

    
}
