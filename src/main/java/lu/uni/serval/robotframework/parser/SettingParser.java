package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Resources;
import lu.uni.serval.robotframework.model.Resources.Type;
import lu.uni.serval.robotframework.model.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class SettingParser {
    private SettingParser(){ }

    static public Settings parse(BufferedReader bufferRead) throws IOException {
        Settings settings = new Settings();

        String line;
        while((line = bufferRead.readLine()) != null){
            String[] tokens = ParsingUtils.tokenizeLine(line);

            if(tokens.length == 0){
                continue;
            }

            String label = tokens[0];

            if(ParsingUtils.compareNoCase(label, "default tags")){
                parseDefaultTags(tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "documentation")){
                parseDocument(tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "resource")){
                parseResource(tokens, settings, Resources.Type.Resource);
            }
            else if(ParsingUtils.compareNoCase(label, "library")){
                parseResource(tokens, settings, Resources.Type.Library);
            }
        }

        return settings;
    }

    private static void parseResource(String[] tokens, Settings settings, Resources.Type type) {
        Resources resources = new Resources(type, tokens[1], new ArrayList<>(), "");
        settings.addResources(resources);
    }

    private static void parseDocument(String[] tokens, Settings settings) {
        settings.setDocumentation(tokens[1]);
    }

    private static void parseDefaultTags(String[] tokens, Settings settings) {
        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }
    }
}
