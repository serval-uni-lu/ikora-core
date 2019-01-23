package org.ukwikora.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static String convertToJsonArray(List<?> list){
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(out, list);

            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "[]";
    }
}
