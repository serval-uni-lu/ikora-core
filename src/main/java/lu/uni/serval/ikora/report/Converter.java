package lu.uni.serval.ikora.report;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

class Converter {
    private static final Pattern datePattern1 = Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d$");
    private static final Pattern datePattern2 = Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d$");
    private static final Pattern datePattern3 = Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d$");

    private static final Pattern datePattern4 = Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d$");
    private static final Pattern datePattern5 = Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d$");
    private static final Pattern datePattern6 = Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d$");

    private static final DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
    private static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter dateTimeFormatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter dateTimeFormatter5 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatter6 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static Instant toDate(String date) throws DateTimeParseException {
        if(datePattern1.matcher(date).matches()){
            return toDate(date, dateTimeFormatter1);
        }

        if(datePattern2.matcher(date).matches()){
            return toDate(date, dateTimeFormatter2);
        }

        if(datePattern3.matcher(date).matches()){
            return toDate(date, dateTimeFormatter3);
        }

        if(datePattern4.matcher(date).matches()){
            return toDate(date, dateTimeFormatter4);
        }

        if(datePattern5.matcher(date).matches()){
            return toDate(date, dateTimeFormatter5);
        }

        if(datePattern6.matcher(date).matches()){
            return toDate(date, dateTimeFormatter6);
        }

        return toDate(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    static Instant toDate(String date, DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(date, dateTimeFormatter).toInstant(ZoneOffset.UTC);
    }

    static String fromDate(Instant date){
        return dateTimeFormatter4.format(date);
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
