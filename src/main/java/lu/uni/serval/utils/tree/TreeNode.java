package lu.uni.serval.utils.tree;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode implements Iterable<TreeNode> {

    public TreeNodeData data;
    public TreeNode parent;
    public List<TreeNode> children;

    private List<TreeNode> elementIndexes;

    public TreeNode(TreeNodeData data) {
        this.parent = null;
        this.data = data;
        this.children = new LinkedList<TreeNode>();
        this.elementIndexes = new LinkedList<TreeNode>();
        this.elementIndexes.add(this);
    }

    public boolean isRoot(){
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode getRoot() {
        if(parent == null) {
            return this;
        }

        return parent.getRoot();
    }

    public int getLevel() {
        if (this.isRoot()) {
            return 0;
        }
        else {
            return this.parent.getLevel() + 1;
        }
    }

    public List<TreeNode> getLeaves(){
        List<TreeNode> leaves = new ArrayList<TreeNode>();
        getLeaves(leaves);

        return leaves;
    }

    public TreeNode getFirstChild() {
        if(children.size() > 0){
            return children.get(0);
        }

        return null;
    }

    public TreeNode getNextSibling(){
        if(this.isRoot()){
            return null;
        }

        return parent.getChildNextSibling(this);
    }

    public TreeNode addChild(TreeNodeData child){
        TreeNode childNode = new TreeNode(child);
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);

        return childNode;
    }

    public TreeNode findTreeNode(Comparable comparable) {
        for (TreeNode element : this.elementIndexes) {
            TreeNodeData elementData = element.data;
            if (comparable.compareTo(elementData) == 0) {
                return element;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return buildString(new StringBuilder(),0).toString();
    }

    public String getLabel() {
        return data.getLabel();
    }

    private StringBuilder buildString(StringBuilder stringBuilder, int level) {
        String indent = "";

        if(level > 0) {
            indent  = new String(new char[level]).replace("\0", "\t");
            stringBuilder.append("\n");
        }

        stringBuilder.append(indent);
        stringBuilder.append(data != null ? data.toString() : "[null]");

        for(TreeNode child: this.children) {
            child.buildString(stringBuilder, level + 1);
        }

        return stringBuilder;
    }

    @Nonnull
    public Iterator<TreeNode> iterator() {
        return new TreeNodeIterator(this);
    }

    private void registerChildForSearch(TreeNode node) {
        elementIndexes.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }

    private void getLeaves(List<TreeNode> leaves) {
        if(this.isLeaf()) {
            leaves.add(this);
        }
        else {
            for(TreeNode child : children) {
                child.getLeaves(leaves);
            }
        }
    }

    private TreeNode getChildNextSibling(TreeNode treeNode) {
        int index = children.indexOf(treeNode);

        if(index == -1 || index == children.size() - 1){
            return null;
        }

        return children.get(index + 1);
    }
}
