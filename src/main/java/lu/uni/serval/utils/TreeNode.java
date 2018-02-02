package lu.uni.serval.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> implements Iterable<TreeNode<T>> {

    public T data;
    public TreeNode<T> parent;
    public List<TreeNode<T>> children;

    private List<TreeNode<T>> elementIndexes;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
        this.elementIndexes = new LinkedList<TreeNode<T>>();
        this.elementIndexes.add(this);
    }

    public boolean isRoot(){
        return parent == null;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getLevel() {
        if (this.isRoot()) {
            return 0;
        }
        else {
            return this.parent.getLevel() + 1;
        }
    }

    public List<TreeNode<T>> getLeaves(){
        List<TreeNode<T>> leaves = new ArrayList<TreeNode<T>>();
        getLeaves(leaves);

        return leaves;
    }

    public TreeNode<T> addChild(T child){
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);

        return childNode;
    }

    public TreeNode<T> findTreeNode(Comparable<T> comparable) {
        for (TreeNode<T> element : this.elementIndexes) {
            T elementData = element.data;
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

    private StringBuilder buildString(StringBuilder stringBuilder, int level) {
        String indent = "";

        if(level > 0) {
            indent  = new String(new char[level]).replace("\0", "\t");
            stringBuilder.append("\n");
        }

        stringBuilder.append(indent);
        stringBuilder.append(data != null ? data.toString() : "[null]");

        for(TreeNode<T> child: this.children) {
            child.buildString(stringBuilder, level + 1);
        }

        return stringBuilder;
    }

    @Nonnull
    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<T>(this);
    }

    private void registerChildForSearch(TreeNode<T> node) {
        elementIndexes.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }

    private void getLeaves(List<TreeNode<T>> leaves) {
        if(this.isLeaf()) {
            leaves.add(this);
        }
        else {
            for(TreeNode<T> child : children) {
                child.getLeaves(leaves);
            }
        }
    }
}
