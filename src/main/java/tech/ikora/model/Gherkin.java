package tech.ikora.model;

import java.util.regex.Pattern;

public class Gherkin {
    public enum Type{
        GIVEN, WHEN, THEN, AND, BUT, NONE
    }

    private static final Pattern gherkinPattern =  Pattern.compile("^(\\s*(Given|When|Then|And|But)\\s*)", Pattern.CASE_INSENSITIVE);

    private final Type type;
    private final Token token;

    private Gherkin(Type type){
        this.type = type;
        this.token = Token.empty();
    }

    public Gherkin(Token raw){
        this.token = raw.extract(gherkinPattern);
        type = getType(this.token);
    }

    public Type getType() {
        return type;
    }

    public Token getToken() {
        return token;
    }

    private Type getType(Token token){
        String prefix = token.getText().trim().toLowerCase();

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
