/*
 *
 *     Copyright © 2019 - 2022 University of Luxembourg
 *
 *     Licensed under the Apache License, Version 2.0 (the "License")
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package lu.uni.serval.ikora.core.utils;

import lu.uni.serval.ikora.core.model.Token;
import org.apache.commons.lang3.tuple.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final Pattern lineBreak = Pattern.compile("\r\n|\r|\n");
    public static final Pattern equalsPattern = Pattern.compile("(^=)|(\\s*[^\\\\]=\\s*)");

    private StringUtils() {}

    public static int countLines(String block){
        String begin = String.format("^(%s)", lineBreak);
        String end = String.format("(%s)$", lineBreak);
        String str = block.trim().replaceAll(begin, "").replaceAll(end, "");

        if(str.isEmpty()){
            return 0;
        }

        Matcher matcher = lineBreak.matcher(str);

        int count = 0;
        while (matcher.find()) ++count;

        return ++count;
    }

    public static String capitalize(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;

        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }

        return String.valueOf(chars);
    }

    public static String toBeautifulName(String raw){
        String name = String.join(" ", org.apache.commons.lang3.StringUtils.splitByCharacterTypeCamelCase(raw.toLowerCase()));
        name = capitalize(name.replaceAll("_|-+", " "));

        return name.replaceAll("\\s+", " ").trim();
    }

    public static String toBeautifulUrl(String raw, String extension) throws UnsupportedEncodingException {
        String url = toBeautifulName(raw).toLowerCase();
        url = url.replaceAll("\\s+", "-");
        url = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());

        if(extension.isEmpty()){
            return url;
        }

        return String.format("%s.%s", url, extension);
    }

    public static String lineTruncate(String line, int maxSize){
        if(maxSize >= line.length() || maxSize < 0){
            return line;
        }

        if(maxSize - 3 < 0){
            return new String(new char[maxSize]).replace("\0", ".");
        }

        return String.format("%s...", line.substring(0, maxSize - 3));
    }

    public static String trimLeft(String string, String removeChars) {
        return org.apache.commons.lang3.StringUtils.stripStart(string, removeChars);
    }

    public static String trimRight(String string, String removeChars) {
        return org.apache.commons.lang3.StringUtils.stripEnd(string, removeChars);
    }

    public static boolean matchesIgnoreCase(Token token, String regex){
        return matchesIgnoreCase(token.getText(), regex);
    }

    public static boolean matchesIgnoreCase(String text, String regex){
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    public static Pair<String, String> splitEqual(String text){
        final Matcher matcher = equalsPattern.matcher(text);
        final Pair<String, String> split;

         if(matcher.find()){
             final int start = matcher.start() > 0 ? matcher.start() + 1 : 0;
             final int end = matcher.end();

            split = Pair.of(text.substring(0, start), text.substring(end));
        }
        else{
            split = Pair.of("", text);
        }

        return split;
    }
}
