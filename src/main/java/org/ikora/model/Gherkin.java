package org.ikora.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gherkin {
    public enum Type{
        GIVEN, WHEN, THEN, AND, BUT, NONE
    }

    private static final Pattern gherkinPattern =  Pattern.compile("^(\\s*(Given|When|Then|And|But)\\s*)", Pattern.CASE_INSENSITIVE);

    private final Type type;

    public Gherkin(Type type){
        this.type = type;
    }

    public Gherkin(Token raw){
        type = extractType(raw);
    }

    public Type getType() {
        return type;
    }

    private String extractPrefix(String raw){
        Matcher matcher = gherkinPattern.matcher(raw);
        String prefix = "";

        if(matcher.find()){
            prefix = matcher.group().toLowerCase();
        }

        return prefix.trim();
    }

    private Type extractType(Token raw){
        String prefix = extractPrefix(raw.getValue());

        switch (prefix){
            case "given": return Type.GIVEN;
            case "when": return Type.WHEN;
            case "then": return Type.THEN;
            case "and": return Type.AND;
            case "but": return Type.BUT;
            default: return Type.NONE;
        }
    }

    public static Gherkin none() {
        return new Gherkin(Type.NONE);
    }

    public static Token getCleanName(Token rawName){
        return rawName.trim(gherkinPattern);
    }
}
