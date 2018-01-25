package lu.uni.serval;

import lu.uni.serval.robotframework.TestCaseFileFactory;
import lu.uni.serval.utils.TreeNode;

public class RFTestGenerator {
    public static void main(String[] args) {
        TestCaseFileFactory factory = new TestCaseFileFactory();
        factory.create();
        /*
        TreeNode<String> root = new TreeNode<String>("root");
        TreeNode<String> child1 = root.addChild("child 1");
        root.addChild("child 2");
        child1.addChild("child 3");

        for (TreeNode<String> node: root) {
            System.out.println(node.toString() + " is:" );
            System.out.println("\troot:" + (node.isRoot() ? "YES": "NO"));
            System.out.println("\tleaf:" + (node.isLeaf() ? "YES": "NO"));
            System.out.println("\tlevel: " + node.getLevel());
        }
        */
    }
}
