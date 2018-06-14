package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Resources;
import lu.uni.serval.robotframework.model.Resources.Type;
import lu.uni.serval.robotframework.model.Settings;
import org.eclipse.jetty.util.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class SettingParser {
    private SettingParser(){ }

    static public String parse(BufferedReader bufferRead, Settings settings) throws IOException {
        String line = bufferRead.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            String[] tokens = ParsingUtils.tokenizeLine(line);

            if(tokens.length == 0){
                continue;
            }

            String label = tokens[0];

            if(ParsingUtils.compareNoCase(label, "default tags")){
                line = parseDefaultTags(bufferRead, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "documentation")){
                line = parseDocumentation(bufferRead, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "resource")){
                line = parseResource(bufferRead, tokens, settings, Resources.Type.Resource);
            }
            else if(ParsingUtils.compareNoCase(label, "library")){
                line = parseResource(bufferRead, tokens, settings, Resources.Type.Library);
            }
        }

        return line;
    }

    private static String parseDocumentation(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(tokens[1]);

        String line = ParsingUtils.appendMultiline(bufferedReader, builder);

        settings.setDocumentation(builder.toString());

        return line;
    }

    private static String parseResource(BufferedReader bufferedReader, String[] tokens, Settings settings, Resources.Type type) throws IOException {
        Resources resources = new Resources(type, tokens[1], new ArrayList<>(), "");
        settings.addResources(resources);

        return bufferedReader.readLine();
    }

    private static String parseDefaultTags(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }

        return bufferedReader.readLine();
    }
}
