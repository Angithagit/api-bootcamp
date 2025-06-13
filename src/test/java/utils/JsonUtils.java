package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static List<Map<String, Object>> readJsonArray(String path) throws IOException {
        InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("File not found: " + path);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
    }

    public static Object[][] convertToDataProvider(String filePath) throws Exception {
        List<Map<String, Object>> dataList = readJsonArray(filePath);
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}
