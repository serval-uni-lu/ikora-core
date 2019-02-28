package org.ukwikora.compiler;

import org.ukwikora.model.Library;
import org.ukwikora.model.Resources;
import org.ukwikora.model.Settings;
import org.ukwikora.model.TestCaseFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class SettingsTableParser {
    private SettingsTableParser(){ }

    public static Settings parse(LineReader reader, TestCaseFile testCaseFile) throws IOException {
        Settings settings = new Settings();
        settings.setFile(testCaseFile);

        Line line = reader.readLine();

        while(line.isValid() && !LexerUtils.isBlock(line.getText())){
            if(line.ignore()){
                line = reader.readLine();
                continue;
            }

            String[] tokens = LexerUtils.tokenize(line.getText());

            if(tokens.length == 0){
                line = reader.readLine();
                continue;
            }

            String label = tokens[0];

            if(LexerUtils.compareNoCase(label, "documentation")){
                parseDocumentation(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "resource")){
                parseResource(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "library")){
                parseLibrary(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "variables")) {
                parseVariable(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "metadata")) {
                parseMetadata(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "suite setup")) {
                parseSuiteSetup(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "suite teardown")) {
                parseSuiteTeardown(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "force tags")) {
                parseForceTags(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "default tags")){
                parseDefaultTags(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "test setup")){
                parseTestSetup(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "test teardown")){
                parseTestTeardown(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "test template")){
                parseTestTemplate(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "test timeout")){
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
        LexerUtils.parseDocumentation(reader, builder);

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
        tokens = LexerUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }

        reader.readLine();
    }
}
