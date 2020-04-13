package com.macaron.vra.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonUtils {
    public static Map<String, Object> jsonStrToMap(String json) throws JsonParseException, JsonMappingException, IOException{
        return jsonStrToObject(json, new TypeReference<Map<String, Object>>(){});
    }

    public static <T> T jsonStrToObject(String json, TypeReference<T> typeReference) throws JsonParseException, JsonMappingException, IOException {
        return new ObjectMapper().readValue(json, typeReference);
    }

    public static String objectToJsonStr(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    public static List<String> jsonStrToList(String jsonData) throws IOException {
        return jsonStrToObject(jsonData,new TypeReference<List<String>>(){});
    }
}
