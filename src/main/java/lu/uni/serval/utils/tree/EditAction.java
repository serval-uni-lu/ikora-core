package lu.uni.serval.utils.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EditActionSerializer.class)
public class EditAction {
    public EditOperation operation;
    private LabelTreeNode root1;
    private LabelTreeNode node1;
    private LabelTreeNode root2;
    private LabelTreeNode node2;

    EditAction(EditOperation operation, LabelTreeNode root1, LabelTreeNode node1, LabelTreeNode root2, LabelTreeNode node2){
        this.operation = operation;
        this.root1 = root1;
        this.node1 = node1;
        this.root2 = root2;
        this.node2 = node2;
    }

    public LabelTreeNode getNode1(){
        return node1;
    }

    public LabelTreeNode getNode2(){
        return node2;
    }

    public LabelTreeNode getRoot1(){
        return root1;
    }

    public LabelTreeNode getRoot2(){
        return root2;
    }

    public String getNodeLabel1() {
        return getSafeLabel(node1);
    }

    public String getNodeLabel2() {
        return getSafeLabel(node2);
    }

    public String getRootLabel1() {
        return root1.getLabel();
    }

    public String getRootLabel2() {
        return root2.getLabel();
    }

    private String getSafeLabel(LabelTreeNode node){
        return node == null ? "[NULL]" : node.getLabel();
    }

    @Override
    public boolean equals(Object other) {
        if(this == other){
            return true;
        }

        if(other == null){
            return false;
        }

        if(this.getClass() != other.getClass()){
            return false;
        }

        EditAction action = (EditAction)other;

        return this.operation == action.operation
                && this.root1 == action.root1
                && this.root2 == action.root2
                && this.node1 == action.node1
                && this.node2 == action.node2;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + operation.hashCode();
        hash = 31 * hash + (node1 == null ? 0 : node1.hashCode());
        hash = 31 * hash + (node2 == null ? 0 : node2.hashCode());

        return hash;
    }
}
