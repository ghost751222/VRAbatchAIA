package com.macaron.vra.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJsonString(T cls) throws JsonProcessingException {
        return objectMapper.writeValueAsString(cls);
    }


    public static JsonNode toJsonNode(String jsonData) throws JsonProcessingException, IOException {
        return objectMapper.readTree(jsonData);
    }


    public static <T> T toClass(String jsonData, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(jsonData, type);
    }


    public static List<Map<String, Object>> getMapList(Map<String, Object> map, String field) {
        Object o = map.get(field);
        return o == null ? new ArrayList<Map<String, Object>>() : (List<Map<String, Object>>) o;
    }

}
