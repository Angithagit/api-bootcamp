package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

	private static Properties properties;
	private final static String propertyFilePath = "src/test/resources/Configurations/config.properties";

	// To get the path of config properties file
	public static void loadConfig() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath));
			properties = new Properties();
			try {
				properties.load(reader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("config.properties not found at " + propertyFilePath);
		}

	}
	// Get the url details from config properties file

	public static String getConfig(String key) {
		if (properties == null) {
	        loadConfig();
	    }
		String value = properties.getProperty(key);
		if (value != null)
			return value;
		else
			throw new RuntimeException("Key '" + key + "' not found in config.properties file.");
	}


}