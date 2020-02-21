package tech.ikora.model;

import tech.ikora.analytics.visitor.NodeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.runner.Runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node implements Differentiable {
    private SourceFile sourceFile;
    private Node astParent;
    private final List<Node> astChildren;
    private final Set<Node> dependencies;
    private final Tokens tokens;

    Node(){
        astParent = null;
        astChildren = new ArrayList<>();
        dependencies = new HashSet<>();
        tokens = new Tokens();
    }

    public final void setSourceFile(SourceFile sourceFile) {
        this.sourceFile = sourceFile;

        for(Node astChild: this.astChildren){
            astChild.setSourceFile(sourceFile);
        }
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public void setAstParent(Node astParent){
        if(astParent == null){
            return;
        }

        this.astParent = astParent;
        this.setSourceFile(astParent.getSourceFile());
    }

    public void addAstChild(Node astChild){
        if(astChild == null || astChildren.contains(astChild)){
            return;
        }

        astChild.setAstParent(this);
        astChildren.add(astChild);
    }

    protected void addAstChild(int index, Node astChild) {
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

    public String getFileName() {
        if(this.sourceFile == null){
            return "<NONE>";
        }

        return this.sourceFile.getName();
    }

    public String getLibraryName(){
        if(this.sourceFile == null){
            return "";
        }

        return this.sourceFile.getLibraryName();
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

    public Node getAstParent() {
        return astParent;
    }

    public List<Node> getAstChildren() {
        return astChildren;
    }

    public void addDependency(Node node) {
        if(node == null || this.dependencies.contains(node)) {
            return;
        }

        this.dependencies.add(node);
    }

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

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }

        if (!(other instanceof Node)){
            return false;
        }

        Node node = (Node)other;

        boolean same = this.getName().equalsIgnorePosition(node.getName());
        same &= this.astChildren.size() == node.astChildren.size();

        for(int i = 0; same && i < this.astChildren.size(); ++i) {
            same = this.astChildren.get(i).equals(node.astChildren.get(i));
        }

        return same;
    }

    public abstract boolean matches(Token name);
    public abstract void accept(NodeVisitor visitor, VisitorMemory memory);
    public abstract void execute(Runtime runtime) throws Exception;
    public abstract Token getName();
}
