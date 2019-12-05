package org.ikora.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Converter {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");

    static Date toDate(String date) throws ParseException {
        return dateFormatter.parse(date);
    }

    static String fromDate(Date date){
        return dateFormatter.format(date);
    }

    static boolean toBoolean(String text) throws ParseException {
        if(text.equalsIgnoreCase("yes")
        || text.equalsIgnoreCase("true")){
            return true;
        }
        else if(text.equalsIgnoreCase("no")
                || text.equalsIgnoreCase("false")){
            return false;
        }

        throw new ParseException(String.format("Could not convert '%s' to boolean value", text), 0);
    }
}
