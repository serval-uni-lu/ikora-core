package lu.uni.serval.robotframework.compiler;

import lu.uni.serval.robotframework.model.Library;
import lu.uni.serval.robotframework.model.Resources;
import lu.uni.serval.robotframework.model.Settings;

import java.io.LineNumberReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsTableParser {
    private SettingsTableParser(){ }

    static public Line parse(LineNumberReader reader, Settings settings) throws IOException {
        Line line = Line.getNextLine(reader);

        while(line.isValid()){
            if(ParsingUtils.isBlock(line.getText())){
                break;
            }

            String[] tokens = line.tokenize();

            if(tokens.length == 0){
                continue;
            }

            String label = tokens[0];


            if(ParsingUtils.compareNoCase(label, "documentation")){
                line = parseDocumentation(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "resource")){
                line = parseResource(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "library")){
                line = parseLibrary(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "variables")) {
                line = parseVariable(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "metadata")) {
                line = parseMetadata(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "suite setup")) {
                line = parseSuiteSetup(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "suite teardown")) {
                line = parseSuiteTeardown(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "force tags")) {
                line = parseForceTags(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "default tags")){
                line = parseDefaultTags(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test setup")){
                line = parseTestSetup(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test teardown")){
                line = parseTestTeardown(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test template")){
                line = parseTestTemplate(reader, tokens, settings);
            }
            else if(ParsingUtils.compareNoCase(label, "test timeout")){
                line = parseTestTimeout(reader, tokens, settings);
            }
            else {
                line = Line.getNextLine(reader);
            }
        }

        return line;
    }

    private static Line parseTestTimeout(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTestTemplate(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTestTeardown(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseTestSetup(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseForceTags(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseSuiteTeardown(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseSuiteSetup(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseMetadata(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseVariable(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        return Line.getNextLine(reader);
    }

    private static Line parseDocumentation(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        StringBuilder builder = new StringBuilder();
        Line line = ParsingUtils.parseDocumentation(reader, tokens, builder);

        settings.setDocumentation(builder.toString());

        return line;
    }

    private static Line parseLibrary(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        Library library = new Library(tokens[1], new ArrayList<>(), "");
        settings.addLibrary(library);

        return Line.getNextLine(reader);
    }

    private static Line parseResource(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        File filePath = new File(tokens[1]);
        if(!filePath.isAbsolute()) {
            filePath = new File(settings.getFile().getParentFile(), filePath.getPath());
        }

        Resources resources = new Resources(tokens[1], filePath, new ArrayList<>(), "");
        settings.addResources(resources);

        return Line.getNextLine(reader);
    }

    private static Line parseDefaultTags(LineNumberReader reader, String[] tokens, Settings settings) throws IOException {
        tokens = ParsingUtils.removeIndent(tokens);

        for(int i = 1; i < tokens.length; ++i){
            settings.addDefaultTag(tokens[i]);
        }

        return Line.getNextLine(reader);
    }
}
