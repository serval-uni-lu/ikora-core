package org.ikora.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JsonUtils {
    public static String convertToJsonArray(List<?> list) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, list);

        return new String(out.toByteArray());
    }

    public static String convertToJsonArray(Set<?> list) throws IOException {
        return convertToJsonArray(new ArrayList<>(list));
    }
}
