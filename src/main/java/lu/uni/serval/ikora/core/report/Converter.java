package lu.uni.serval.ikora.core.report;

/*-
 * #%L
 * Ikora Core
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.types.BooleanType;

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
        for(Map.Entry<Pattern, DateTimeFormatter> entry: patternToFormatter.entrySet()){
            if(entry.getKey().matcher(date).matches()){
                return toDate(date, entry.getValue());
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
        || text.equalsIgnoreCase(BooleanType.TRUE)){
            return true;
        }
        else if(text.equalsIgnoreCase("no")
                || text.equalsIgnoreCase(BooleanType.FALSE)){
            return false;
        }

        throw new ParseException(String.format("Could not convert '%s' to boolean value", text), 0);
    }
}
