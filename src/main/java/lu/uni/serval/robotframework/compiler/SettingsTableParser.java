package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Library;
import lu.uni.serval.robotframework.model.Resources;
import lu.uni.serval.robotframework.model.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsTableParser {
    private SettingsTableParser(){ }

    static public Settings parse(LineReader reader) throws IOException {
        Settings settings = new Settings();

        Line line = reader.readLine();

        while(line.isValid() && !Utils.isBlock(line.getText())){
            if(Utils.ignore(line)){
                line = reader.readLine();
                continue;
            }

            String[] tokens = line.tokenize();

            if(tokens.length == 0){
                line = reader.readLine();
                continue;
            }

            String label = tokens[0];

            if(Utils.compareNoCase(label, "documentation")){
                parseDocumentation(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "resource")){
                parseResource(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "library")){
                parseLibrary(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "variables")) {
                parseVariable(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "metadata")) {
                parseMetadata(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "suite setup")) {
                parseSuiteSetup(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "suite teardown")) {
                parseSuiteTeardown(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "force tags")) {
                parseForceTags(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "default tags")){
                parseDefaultTags(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "test setup")){
                parseTestSetup(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "test teardown")){
                parseTestTeardown(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "test template")){
                parseTestTemplate(reader, tokens, settings);
            }
            else if(Utils.compareNoCase(label, "test timeout")){
                parseTestTimeout(reader, tokens, settings);
            }
            else {
                reader.readLine();
            }

            line = reader.getCurrent();
        }

        return settings;
    }

    private static void parseTestTimeout(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestTemplate(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestTeardown(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestSetup(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseForceTags(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseSuiteTeardown(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseSuiteSetup(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseMetadata(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseVariable(LineReader reader, String[] tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, String[] tokens, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        Utils.parseDocumentation(reader, tokens, builder);

        settings.setDocumentation(builder.toString());
    }

    private static void parseLibrary(LineReader reader, String[] tokens, Settings settings) throws IOException {
        Library library = new Library(tokens[1], new ArrayList<>(), "");
        settings.addLibrary(library);

        reader.readLine();
    }

    private static void parseResource(LineReader reader, String[] tokens, Settings settings) throws IOException {
        File filePath = new File(tokens[1]);
        if(!filePath.isAbsolute()) {
            filePath = new File(reader.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(tokens[1], filePath, new ArrayList<>(), "");
        settings.addResources(resources);

        reader.readLine();
    }

    private static void parseDefaultTags(LineReader reader, String[] tokens, Settings settings) throws IOException {
        tokens = Utils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }

        reader.readLine();
    }
}
