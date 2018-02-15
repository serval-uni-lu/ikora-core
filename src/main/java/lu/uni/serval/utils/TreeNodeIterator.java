package lu.uni.serval.utils;

import java.util.Iterator;

public class TreeNodeIterator implements Iterator<TreeNode> {
    enum ProcessStages {
        ProcessParents, ProcessChildCurNode, ProcessChildSubNode
    }

    private TreeNode treeNode;
    private TreeNode next;
    private Iterator<TreeNode> childrenCurNodeIterator;
    private Iterator<TreeNode> childrenSubNodeIterator;

    private ProcessStages doNext;

    public TreeNodeIterator(TreeNode treeNode) {
        this.treeNode = treeNode;
        this.doNext = ProcessStages.ProcessParents;
        this.childrenCurNodeIterator = treeNode.children.iterator();
    }

    public boolean hasNext() {
        if (this.doNext == ProcessStages.ProcessParents) {
            this.next = this.treeNode;
            this.doNext = ProcessStages.ProcessChildCurNode;

            return true;
        }

        if (this.doNext == ProcessStages.ProcessChildCurNode) {
            if(childrenCurNodeIterator.hasNext()){
                TreeNode childDirect = childrenCurNodeIterator.next();
                childrenSubNodeIterator = childDirect.iterator();
                this.doNext = ProcessStages.ProcessChildSubNode;

                return hasNext();
            }
            else {
                this.doNext = null;

                return false;
            }
        }

        if (this.doNext == ProcessStages.ProcessChildSubNode) {
            if (childrenSubNodeIterator.hasNext()) {
                this.next = childrenSubNodeIterator.next();

                return true;
            }
            else {
                this.next = null;
                this.doNext = ProcessStages.ProcessChildCurNode;

                return hasNext();
            }
        }

        return false;
    }

    public TreeNode next(){
        return this.next;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
