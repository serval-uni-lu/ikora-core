package tech.ikora.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SourceNode implements Node, Differentiable {
    private SourceFile sourceFile;
    private SourceNode astParent;
    private final List<SourceNode> astChildren;
    private final Set<Node> dependencies;
    private final Tokens tokens;

    SourceNode(){
        astParent = null;
        astChildren = new ArrayList<>();
        dependencies = new HashSet<>();
        tokens = new Tokens();
    }

    public String getName(){
        return getNameToken().getText();
    }

    public final void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;

        for(SourceNode astChild: this.astChildren){
            astChild.setSourceFile(sourceFile);
        }
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public void setAstParent(SourceNode astParent){
        if(astParent == null){
            return;
        }

        this.astParent = astParent;
        this.setSourceFile(astParent.getSourceFile());
    }

    public void addAstChild(SourceNode astChild){
        if(astChild == null || astChildren.contains(astChild)){
            return;
        }

        astChild.setAstParent(this);
        astChildren.add(astChild);
    }

    protected void addAstChild(int index, SourceNode astChild) {
        if(astChild == null || astChildren.contains(astChild)){
            return;
        }

        astChild.setAstParent(this);
        astChildren.add(index, astChild);
    }

    protected void clearAstChildren() {
        this.astChildren.clear();
    }

    public File getFile(){
        if(sourceFile == null){
            return null;
        }

        return sourceFile.getFile();
    }

    @Override
    public String getLibraryName() {
        if(this.sourceFile == null){
            return "<NONE>";
        }

        return this.sourceFile.getName();
    }

    public long getEpoch() {
        return sourceFile.getEpoch();
    }

    public Project getProject(){
        if(getSourceFile() == null){
            return null;
        }

        return getSourceFile().getProject();
    }

    public SourceNode getAstParent() {
        return astParent;
    }

    public List<SourceNode> getAstChildren() {
        return astChildren;
    }

    @Override
    public void addDependency(Node sourceNode) {
        if(sourceNode == null || this.dependencies.contains(sourceNode)) {
            return;
        }

        this.dependencies.add(sourceNode);
    }

    @Override
    public Set<Node> getDependencies() {
        return dependencies;
    }

    public boolean isDeadCode(){
        return getDependencies().size() == 0;
    }

    public void addToken(Token token){
        if(token == null || token.isEmpty()){
            return;
        }

        this.tokens.add(token);
    }

    public void addTokens(Tokens tokens){
        if(tokens == null){
            return;
        }

        this.tokens.addAll(tokens);
    }

    public Tokens getTokens(){
        return tokens;
    }

    public Range getRange(){
        return Range.fromTokens(tokens, null);
    }

    public int getLoc() {
        int startLine = getRange().getStart().getLine();
        int endLine = getRange().getEnd().getLine();

        return this.sourceFile.getLinesOfCode(startLine, endLine);
    }

    public abstract Token getNameToken();
}
