package lu.uni.serval.utils;

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
        return toString("",0);
    }

    public String toString(String current, int level) {
        String indent = new String();

        if(level > 0) {
            indent  = new String(new char[level]).replace("\0", "\t");
            current += "\n";
        }

        current += indent;
        current += data != null ? data.toString() : "[null]";

        for(TreeNode<T> child: this.children) {
            current = child.toString(current, level + 1);
        }

        return current;
    }

    public Iterator<TreeNode<T>> iterator() {
        return new TreeNodeIterator<T>(this);
    }

    private void registerChildForSearch(TreeNode<T> node) {
        elementIndexes.add(node);
        if (parent != null) {
            parent.registerChildForSearch(node);
        }
    }
}
