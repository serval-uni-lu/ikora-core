package org.ukwikora.report;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Utils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");

    static LocalDateTime toLocalDateTime(String dateTime){
        return LocalDateTime.parse(dateTime, dateFormatter);
    }

    public static boolean toBoolean(String text) throws IOException {
        if(text.equalsIgnoreCase("yes")
        || text.equalsIgnoreCase("true")){
            return true;
        }
        else if(text.equalsIgnoreCase("no")
                || text.equalsIgnoreCase("false")){
            return false;
        }

        throw new IOException(String.format("Could not convert %s to boolean value", text));
    }
}
