package lu.uni.serval.utils.tree;

public class TreeNodeDataTest implements TreeNodeData{
    private String label;

    public TreeNodeDataTest(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean isSame(TreeNodeData other) {
        if(! this.getClass().equals(other.getClass())){
            return false;
        }

        return this.label.compareTo(((TreeNodeDataTest)other).label) == 0;
    }
}