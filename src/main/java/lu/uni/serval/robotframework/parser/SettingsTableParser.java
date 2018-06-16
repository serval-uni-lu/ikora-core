package lu.uni.serval.robotframework.parser;

import lu.uni.serval.robotframework.model.Library;
import lu.uni.serval.robotframework.model.Resources;
import lu.uni.serval.robotframework.model.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsTableParser {
    private SettingsTableParser(){ }

    static public String parse(BufferedReader bufferedReader, Settings settings) throws IOException {
        String line = bufferedReader.readLine();

        while(line != null){
            if(ParsingUtils.isBlock(line)){
                break;
            }

            String[] tokens = ParsingUtils.tokenizeLine(line);

            if(tokens.length == 0){
                continue;
            }

            String label = tokens[0];


            if(ParsingUtils.compareNoCase(label, "documentation")){
                line = parseDocumentation(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "resource")){
                line = parseResource(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "library")){
                line = parseLibrary(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "variables")) {
                line = parseVariable(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "metadata")) {
                line = parseMetadata(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "suite setup")) {
                line = parseSuiteSetup(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "suite teardown")) {
                line = parseSuiteTeardown(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "force tags")) {
                line = parseForceTags(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "default tags")){
                line = parseDefaultTags(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test setup")){
                line = parseTestSetup(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test teardown")){
                line = parseTestTeardown(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test template")){
                line = parseTestTemplate(bufferedReader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test timeout")){
                line = parseTestTimeout(bufferedReader, tokens, settings);
            }
            else {
                line = bufferedReader.readLine();
            }
        }

        return line;
    }

    private static String parseTestTimeout(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTestTemplate(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTestTeardown(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseTestSetup(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseForceTags(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseSuiteTeardown(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseSuiteSetup(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseMetadata(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseVariable(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        return bufferedReader.readLine();
    }

    private static String parseDocumentation(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = ParsingUtils.parseDocumentation(bufferedReader, tokens, builder);

        settings.setDocumentation(builder.toString());

        return line;
    }

    private static String parseLibrary(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        Library library = new Library(tokens[1], new ArrayList<>(), "");
        settings.addLibrary(library);

        return bufferedReader.readLine();
    }

    private static String parseResource(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        File filePath = new File(tokens[1]);
        if(!filePath.isAbsolute()) {
            filePath = new File(settings.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(tokens[1], filePath, new ArrayList<>(), "");
        settings.addResources(resources);

        return bufferedReader.readLine();
    }

    private static String parseDefaultTags(BufferedReader bufferedReader, String[] tokens, Settings settings) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }

        return bufferedReader.readLine();
    }
}
