package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class SettingsTableParser {
    private SettingsTableParser(){ }

    public static Settings parse(LineReader reader, SourceFile sourceFile, ErrorManager errors) throws IOException {
        Settings settings = new Settings();
        settings.setFile(sourceFile);

        Line line = reader.readLine();

        while(line.isValid() && !LexerUtils.isBlock(line.getText())){
            if(line.ignore()){
                line = reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(line.getText());

            if(tokens.size() == 0){
                line = reader.readLine();
                continue;
            }

            String label = ParserUtils.getLabel(reader, tokens, errors);

            if(LexerUtils.compareNoCase(label, "documentation")){
                parseDocumentation(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "resource")){
                parseResource(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "library")){
                parseLibrary(reader, tokens, settings, errors);
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

    private static void parseTestTimeout(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestTemplate(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestTeardown(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseTestSetup(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseForceTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseSuiteTeardown(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseSuiteSetup(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseMetadata(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseVariable(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        LexerUtils.parseDocumentation(reader, builder);

        settings.setDocumentation(builder.toString());
    }

    private static void parseLibrary(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        tokens = tokens.withoutFirst();
        String label = ParserUtils.getLabel(reader, tokens, errors);
        Library library = new Library(label, new ArrayList<>(), "");
        settings.addLibrary(library);

        reader.readLine();
    }

    private static void parseResource(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        tokens = tokens.withoutFirst();
        String label = ParserUtils.getLabel(reader, tokens, errors);
        File filePath = new File(label);
        if(!filePath.isAbsolute()) {
            filePath = new File(reader.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(label, filePath, new ArrayList<>(), "");
        settings.addResources(resources);

        reader.readLine();
    }

    private static void parseDefaultTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        for(Token token: tokens.withoutIndent()){
            settings.addDefaultTag(token.getValue());
        }

        reader.readLine();
    }
}
