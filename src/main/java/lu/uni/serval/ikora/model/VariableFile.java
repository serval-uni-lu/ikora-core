package lu.uni.serval.ikora.model;

import java.util.List;

public class VariableFile {
    private final Token path;
    private final List<Token> parameters;

    public VariableFile(Token path, List<Token> parameters) {
        this.path = path;
        this.parameters = parameters;
    }

    public String getPath() {
        return this.path.getText();
    }
}
