package lu.uni.serval.utils.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.awt.*;

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
}
