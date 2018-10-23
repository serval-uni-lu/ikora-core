package lu.uni.serval.robotframework.utils;

public class StringUtils {
    public static String lineBreak = "\r\n|\r|\n";

    public static int countLines(String block){
        String[] lines = block.split(lineBreak);
        return  lines.length;
    }
}
