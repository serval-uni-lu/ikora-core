package lu.uni.serval.utils.tree;

public class TreeView {
    private final TreeNode head;
    private final TreeNode root;

    public TreeView(TreeNode tree){
        if(tree == null){
            throw new NullPointerException();
        }

        this.head = tree.getRoot();
        this.root = tree.getRoot();
    }

    public TreeView(TreeNode head, TreeNode root){
        this.head = head;
        this.root = root;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof TreeView){
            return this.equals((TreeView)other);
        }

        return false;
    }

    public boolean equals(TreeView other){
        return  this.head == other.head
                && this.root == other.root;
    }

    public TreeNode head(){
        return this.head;
    }

    public TreeNode root(){
        return this.root;
    }

    public TreeView getInside() {
        TreeNode child = head.getFirstChild();

        if(child != null){
            return new TreeView(child, this.head);
        }

        return null;
    }

    public TreeView getOutside() {
        TreeNode node = head;

        while(node != root){
            TreeNode sibling = node.getNextSibling();

            if(sibling != null) {
                return new TreeView(sibling, root);
            }

            node = node.getParent();
        }

        return null;
    }

    public TreeView deleteHead() {
        TreeNode child = head.getFirstChild();

        if(child == null){
            return getOutside();
        }

        return new TreeView(child, this.head);
    }
}
