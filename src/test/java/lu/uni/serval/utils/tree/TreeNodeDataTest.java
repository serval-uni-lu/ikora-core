package lu.uni.serval.utils.tree;

public class TreeNodeDataTest implements TreeNodeData{
    private String label;

    public TreeNodeDataTest(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}