package cn.iocoder.yudao.module.star.service.edu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static JsonNode parsePreloadedState(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
