package lu.uni.serval.ikora.core.report;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class Converter {
    private static final Map<Pattern, DateTimeFormatter> patternToFormatter = new HashMap<>(6);

    static {
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d$"), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS"));
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d$"), DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"));
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d\\d\\d\\d\\d$"), DateTimeFormatter.ofPattern("yyyyMMdd"));
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d.\\d\\d\\d$"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d$"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        patternToFormatter.put(Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d$"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private Converter() {}

    static Instant toDate(String date) throws DateTimeParseException {
        for(Pattern pattern: patternToFormatter.keySet()){
            if(pattern.matcher(date).matches()){
                return toDate(date, patternToFormatter.get(pattern));
            }
        }

        return toDate(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    static Instant toDate(String date, DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(date, dateTimeFormatter).toInstant(ZoneOffset.UTC);
    }

    static String fromDate(Instant date){
        return DateTimeFormatter.ISO_DATE.format(date);
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
