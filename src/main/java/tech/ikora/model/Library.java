package tech.ikora.model;

import java.util.List;

public class Library {
    private Token name;
    private List<Token> arguments;
    private Token comment;

    public Library(Token name, List<Token> arguments, Token comment) {
        this.name = name;
        this.arguments = arguments;
        this.comment = comment;
    }

    public Token getName() {
        return this.name;
    }
}
