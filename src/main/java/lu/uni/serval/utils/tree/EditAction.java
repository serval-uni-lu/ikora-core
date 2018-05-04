package lu.uni.serval.utils.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EditActionSerializer.class)
public class EditAction {
    public EditOperation operation;
    private TreeNode node1;
    private TreeNode node2;

    EditAction(EditOperation operation, TreeNode node1, TreeNode node2){
        this.operation = operation;
        this.node1 = node1;
        this.node2 = node2;
    }

    public TreeNode getNode1(){
        return node1;
    }

    public TreeNode getNode2(){
        return node2;
    }

    public String getNodeLabel1() {
        return getSafeLabel(node1);
    }

    public String getNodeLabel2() {
        return getSafeLabel(node2);
    }

    private String getSafeLabel(TreeNode node){
        return node == null ? "[NULL]" : node.getLabel();
    }

}
