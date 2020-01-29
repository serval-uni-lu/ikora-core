package org.ikora.builder;

import org.ikora.error.ErrorManager;
import org.ikora.error.ErrorMessages;
import org.ikora.exception.InvalidTypeException;
import org.ikora.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            Tokens tokens = LexerUtils.tokenize(line);

            if(tokens.size() == 0){
                line = reader.readLine();
                continue;
            }

            String label = ParserUtils.getLabel(reader, tokens, errors).getValue();

            if(LexerUtils.compareNoCase(label, "documentation")){
                parseDocumentation(reader, settings);
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
                parseSuiteSetup(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "suite teardown")) {
                parseSuiteTeardown(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "force tags")) {
                parseForceTags(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "default tags")){
                parseDefaultTags(reader, tokens, settings);
            }
            else if(LexerUtils.compareNoCase(label, "test setup")){
                parseTestSetup(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "test teardown")){
                parseTestTeardown(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "test template")){
                parseTestTemplate(reader, tokens, settings, errors);
            }
            else if(LexerUtils.compareNoCase(label, "test timeout")){
                ParserUtils.parseTimeOut("test timeout", reader, tokens, settings, errors);
            }
            else {
                reader.readLine();
            }

            line = reader.getCurrent();
        }

        return settings;
    }

    private static void parseTestTemplate(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "test template", false, errors);

        try {
            settings.setTemplate(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEMPLATE, e.getMessage()),
                    step.getPosition()
            );
        }
        reader.readLine();
    }

    private static void parseTestTeardown(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "test teardown", false, errors);

        try {
            settings.setTestTeardown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }

        reader.readLine();
    }

    private static void parseTestSetup(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "test setup", false, errors);

        try {
            settings.setTestSetup(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getPosition()
            );
        }

        reader.readLine();
    }

    private static void parseForceTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        for(Token token: tokens.withoutIndent()){
            settings.addForceTag(token.getValue());
        }

        reader.readLine();
    }

    private static void parseDefaultTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        for(Token token: tokens.withoutIndent()){
            settings.addDefaultTag(token.getValue());
        }

        reader.readLine();
    }

    private static void parseSuiteTeardown(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "suite teardown", false, errors);

        try {
            settings.setSuiteTeardown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }

        reader.readLine();
    }

    private static void parseSuiteSetup(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, "suite setup", false, errors);

        try {
            settings.setSuiteSetup(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getPosition()
            );
        }

        reader.readLine();
    }

    private static void parseMetadata(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        Tokens metadataTokens = tokens.withoutTag("metadata");

        if(metadataTokens.get(0).isPresent()){
            String key =  metadataTokens.get(0).get().getValue();

            Value value = Value.empty();
            if(metadataTokens.get(1).isPresent()){
                value = new Value(metadataTokens.get(1).get());
            }

            settings.addMetadata(key, value);
        }

        reader.readLine();
    }

    private static void parseVariable(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        Tokens variableTokens = tokens.withoutTag("variables");

        if(variableTokens.get(0).isPresent()){
            String file =  variableTokens.get(0).get().getValue();

            List<String> parameters = new ArrayList<>();
            for(Token token: tokens.withoutFirst()){
                if(token.isText()){
                    parameters.add(token.getValue());
                }
            }

            settings.addVariableFile(new VariableFile(file, parameters));
        }

        reader.readLine();
    }

    private static void parseDocumentation(LineReader reader, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        LexerUtils.parseDocumentation(reader, builder);

        settings.setDocumentation(builder.toString());
    }

    private static void parseLibrary(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        tokens = tokens.withoutFirst();
        Token label = ParserUtils.getLabel(reader, tokens, errors);
        Library library = new Library(label, new ArrayList<>(), Token.empty());
        settings.addLibrary(library);

        reader.readLine();
    }

    private static void parseResource(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        tokens = tokens.withoutFirst();
        Token label = ParserUtils.getLabel(reader, tokens, errors);
        File filePath = new File(label.getValue());
        if(!filePath.isAbsolute()) {
            filePath = new File(reader.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(label, filePath, new ArrayList<>(), Token.empty());
        settings.addResources(resources);

        reader.readLine();
    }
}
