package org.ikora.utils;

import org.apache.commons.text.WordUtils;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static final Pattern lineBreak;

    static {
        lineBreak = Pattern.compile("\r\n|\r|\n");
    }

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

    public static String toBeautifulName(String raw){
        String name = org.apache.commons.lang.StringUtils.join(org.apache.commons.lang.StringUtils.splitByCharacterTypeCamelCase(raw.toLowerCase()), ' ');
        name = WordUtils.capitalize(name.replaceAll("_|-+", " "));

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

    public static String removeStart(@Nonnull String str, @Nonnull String remove) {
        if (str.isEmpty() || remove.isEmpty()) {
            return str;
        }
        if (str.startsWith(remove)){
            return str.substring(remove.length());
        }
        return str;
    }

    public static String trimLeft(String string, String remove) {
        return org.apache.commons.lang3.StringUtils.stripEnd(string, remove);
    }
}
