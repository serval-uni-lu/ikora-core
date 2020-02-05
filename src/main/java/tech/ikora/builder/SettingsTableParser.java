package tech.ikora.builder;

import tech.ikora.error.ErrorManager;
import tech.ikora.error.ErrorMessages;
import tech.ikora.exception.InvalidTypeException;
import tech.ikora.utils.StringUtils;
import tech.ikora.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SettingsTableParser {
    private SettingsTableParser(){ }

    public static Settings parse(LineReader reader, Tokens blockTokens, SourceFile sourceFile, ErrorManager errors) throws IOException {
        Settings settings = new Settings();
        settings.setFile(sourceFile);
        settings.setHeader(ParserUtils.parseHeaderName(reader, blockTokens, errors));

        while(reader.getCurrent().isValid() && !LexerUtils.isBlock(reader.getCurrent().getText())){
            if(reader.getCurrent().ignore()){
                reader.readLine();
                continue;
            }

            Tokens tokens = LexerUtils.tokenize(reader);

            String label = ParserUtils.getLabel(reader, tokens, errors).getText();

            if(StringUtils.compareNoCase(label, "documentation")){
                parseDocumentation(reader, tokens.withoutTag("documentation"), settings);
            }
            else if(StringUtils.compareNoCase(label, "resource")){
                parseResource(reader, tokens.withoutTag("resource"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "library")){
                parseLibrary(reader, tokens.withoutTag("library"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "variables")) {
                parseVariable(reader, tokens.withoutTag("variables"), settings);
            }
            else if(StringUtils.compareNoCase(label, "metadata")) {
                parseMetadata(reader, tokens.withoutTag("metadata"), settings);
            }
            else if(StringUtils.compareNoCase(label, "suite setup")) {
                parseSuiteSetup(reader, tokens.withoutTag("suite setup"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "suite teardown")) {
                parseSuiteTeardown(reader, tokens.withoutTag("suite teardown"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "force tags")) {
                parseForceTags(reader, tokens.withoutTag("force tags"), settings);
            }
            else if(StringUtils.compareNoCase(label, "default tags")){
                parseDefaultTags(reader, tokens.withoutTag("default tags"), settings);
            }
            else if(StringUtils.compareNoCase(label, "test setup")){
                parseTestSetup(reader, tokens.withoutTag("test setup"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "test teardown")){
                parseTestTeardown(reader, tokens.withoutTag("test teardown"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "test template")){
                parseTestTemplate(reader, tokens.withoutTag("test template"), settings, errors);
            }
            else if(StringUtils.compareNoCase(label, "test timeout")){
                ParserUtils.parseTimeOut(reader, tokens, settings, errors);
            }
        }

        return settings;
    }

    private static void parseTestTemplate(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            settings.setTemplate(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    ErrorMessages.FAILED_TO_PARSE_TEMPLATE,
                    step.getPosition()
            );
        }
    }

    private static void parseTestTeardown(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            settings.setTestTeardown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }
    }

    private static void parseTestSetup(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            settings.setTestSetup(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getPosition()
            );
        }
    }

    private static void parseForceTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        for(Token token: tokens.withoutIndent()){
            settings.addForceTag(token.getText());
        }
    }

    private static void parseDefaultTags(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        for(Token token: tokens.withoutIndent()){
            settings.addDefaultTag(token.getText());
        }
    }

    private static void parseSuiteTeardown(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            settings.setSuiteTeardown(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_TEARDOWN, e.getMessage()),
                    step.getPosition()
            );
        }
    }

    private static void parseSuiteSetup(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Step step = StepParser.parse(reader, tokens, false, errors);

        try {
            settings.setSuiteSetup(step);
        } catch (InvalidTypeException e) {
            errors.registerSyntaxError(
                    step.getFile(),
                    String.format("%s: %s", ErrorMessages.FAILED_TO_PARSE_SETUP, e.getMessage()),
                    step.getPosition()
            );
        }
    }

    private static void parseMetadata(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        settings.addMetadata(tokens.first().getText(), tokens.get(1));
    }

    private static void parseVariable(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        String file = tokens.first().getText();

        List<String> parameters = new ArrayList<>();
        for(Token token: tokens.withoutFirst()){
            if(token.isText()){
                parameters.add(token.getText());
            }
        }

        settings.addVariableFile(new VariableFile(file, parameters));
    }

    private static void parseDocumentation(LineReader reader, Tokens tokens, Settings settings) throws IOException {
        settings.setDocumentation(tokens.toString());
    }

    private static void parseLibrary(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Token name = ParserUtils.getLabel(reader, tokens, errors);
        Library library = new Library(name, new ArrayList<>(), Token.empty());
        settings.addLibrary(library);
    }

    private static void parseResource(LineReader reader, Tokens tokens, Settings settings, ErrorManager errors) throws IOException {
        Token label = ParserUtils.getLabel(reader, tokens, errors);
        File filePath = new File(label.getText());
        if(!filePath.isAbsolute()) {
            filePath = new File(reader.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(label, filePath, new ArrayList<>(), Token.empty());
        settings.addResources(resources);
    }
}