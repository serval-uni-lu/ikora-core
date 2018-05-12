package lu.uni.serval.utils.tree;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EditActionSerializer.class)
public class EditAction {
    public EditOperation operation;
    private LabelTreeNode node1;
    private LabelTreeNode node2;

    EditAction(EditOperation operation, LabelTreeNode node1, LabelTreeNode node2){
        this.operation = operation;
        this.node1 = node1;
        this.node2 = node2;
    }

    public LabelTreeNode getNode1(){
        return node1;
    }

    public LabelTreeNode getNode2(){
        return node2;
    }

    public String getNodeLabel1() {
        return getSafeLabel(node1);
    }

    public String getNodeLabel2() {
        return getSafeLabel(node2);
    }

    private String getSafeLabel(LabelTreeNode node){
        return node == null ? "[NULL]" : node.getLabel();
    }

    public String getRootLabel() {
        if(node1 != null){
            return node1.getRoot().getLabel();
        }

        if(node2 != null){
            return node2.getRoot().getLabel();
        }

        return "[NULL]";
    }
}
