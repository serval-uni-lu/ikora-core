package lu.uni.serval.utils.tree;

import lu.uni.serval.utils.exception.DuplicateNodeException;

import javax.annotation.Nonnull;
import java.util.*;

public class TreeNode implements Iterable<TreeNode> {

    public TreeNodeData data;
    public TreeNode parent;
    public List<TreeNode> children;
    public boolean isDataUnique;

    private List<TreeNode> elementIndexes;

    public TreeNode(TreeNodeData data, boolean isDataUnique) {
        this.parent = null;
        this.data = data;
        this.children = new LinkedList<>();
        this.elementIndexes = new LinkedList<>();
        this.elementIndexes.add(this);
        this.isDataUnique = isDataUnique;
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

        return this.parent.getLevel() + 1;
    }

    public int getDepth(){
        if(this.isLeaf()){
            return 0;
        }

        int depth = 0;
        for(TreeNode child: children){
            depth = Math.max(depth, child.getDepth());
        }

        return depth + 1;
    }

    public List<TreeNode> getLeaves(){
        List<TreeNode> leaves = new ArrayList<>();
        getLeaves(leaves);

        return leaves;
    }

    public int getSize() {
        int size = 1;

        for(TreeNode child: children) {
            size += child.getSize();
        }

        return size;
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

    public TreeNode addChild(TreeNodeData child) throws DuplicateNodeException {
        if(isDataUnique && child.isValid() && isAncestor(child)){
            throw new DuplicateNodeException(child.toString());
        }

        TreeNode childNode = new TreeNode(child, isDataUnique);
        childNode.parent = this;

        this.children.add(childNode);
        this.registerChildForSearch(childNode);

        return childNode;
    }

    public void addChild(TreeNode child){
        this.children.add(child);
        child.parent = this;
    }

    public boolean isAncestor(TreeNodeData data){
        TreeNode ancestor = parent;

        while(ancestor != null){
            if(ancestor.data.isSame(data)){
                return true;
            }

            ancestor = ancestor.parent;
        }

        return false;
    }

    public boolean isAncestor(TreeNode node) {
        return isAncestor(node.data);
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
