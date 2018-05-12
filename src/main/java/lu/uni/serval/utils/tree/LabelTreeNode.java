//    Copyright (C) 2012  Mateusz Pawlik and Nikolaus Augsten
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as
//    published by the Free Software Foundation, either version 3 of the
//    License, or (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package lu.uni.serval.utils.tree;

import org.w3c.dom.Node;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * A node of a tree. Each tree has an ID.
 * The label can be empty, but can not contain trailing spaces (nor consist only of spaces).
 * 
 * Two nodes are equal, if there labels are equal, and n1 < n2 if label(n1) < label(n2).
 * 
 * @author Nikolaus Augsten from approxlib, available at http://www.inf.unibz.it/~augsten/src/ modified by Mateusz Pawlik
 */
public class LabelTreeNode extends DefaultMutableTreeNode implements Comparable {
	
	public static final String TAB_STRING = "    ";
	public static final String ROOT_STRING =   "*---+";
	public static final String BRANCH_STRING = "+---+";
	
	public static final String OPEN_BRACKET = "{";
	public static final String CLOSE_BRACKET = "}";
	public static final String ID_SEPARATOR = ":";
	
	public static final int HIDE_NOTHING = 0;
	public static final int HIDE_ROOT_LABEL = 1;
	public static final int RENAME_LABELS_TO_LEVEL = 2;
	public static final int HIDE_ALL_LABELS = 3;
	public static final int RANDOM_ROOT_LABEL = 4;
        
	/**
	 * no node id
	 */
	public final static int NO_NODE = -1;
	
	/**
	 * no tree id is defined
	 */
	public final int NO_TREE_ID = -1;
	
	int treeID = NO_TREE_ID;
	String label = null;
	TreeNodeData data = null;
	Object tmpData = null;
	int nodeID = NO_NODE;
	
	/**
	 * Use only this constructor!
	 */
	public LabelTreeNode(TreeNodeData data) {
		super();

		setData(data);
	}
		
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
		
	public int getTreeID() {
		if (isRoot()) {
			return treeID;
		} else {
			return getRoot().getTreeID();
		}
	}
	
	public void setTreeID(int treeID) {
		if (isRoot()) {
			this.treeID = treeID;
		} else {
			getRoot().setTreeID(treeID);
		}
	}
	
	/**
	 * tmpData: Hook for any data that a method must attach to a tree.
	 * Methods can assume, that this date is null and should return it
	 * to be null!
	 */
	public void setData(TreeNodeData data) {
		this.data = data;
		this.label = data.getLabel();
	}

	public void setTmpData(Object tmpData){
	    this.tmpData = tmpData;
    }
	
	public TreeNodeData getData() {
		return data;
	}

	public Object getTmpData(){
	    return tmpData;
    }
		
	public void prettyPrint() {
		prettyPrint(false);
	}
	
	
	public void prettyPrint(boolean printTmpData) {
		for (int i = 0; i < getLevel(); i++) {
			System.out.print(TAB_STRING);
		}
		if (!isRoot()) {
			System.out.print(BRANCH_STRING);
		} else {
			if (getTreeID() != NO_TREE_ID) {
				System.out.println("treeID: " + getTreeID());
			}
			System.out.print(ROOT_STRING);
		}
		System.out.print(" '" + this.getLabel() + "' ");
		if (printTmpData) {
			System.out.println(getData());
		} else {
			System.out.println();
		}
		for (Enumeration e = children(); e.hasMoreElements();) {
			((LabelTreeNode)e.nextElement()).prettyPrint(printTmpData);
		}
		
	}
	
	public int getNodeCount() {
		int sum = 1;
		for (Enumeration e = children(); e.hasMoreElements();) {
			sum += ((LabelTreeNode)e.nextElement()).getNodeCount();
		}
		return sum;
	}

	@Override
	public LabelTreeNode getParent() {
		return (LabelTreeNode)super.getParent();
	}

    @Override
    public LabelTreeNode getRoot() {
        return (LabelTreeNode)super.getRoot();
    }

    public List<LabelTreeNode> getLeaves(){
        List<LabelTreeNode> leaves = new ArrayList<>();
        getLeaves(leaves);

        return leaves;
    }

    public boolean isAncestor(TreeNodeData data){
        LabelTreeNode ancestor = getParent();

        while(ancestor != null){
            if(ancestor.getData().isSame(data)){
                return true;
            }

            ancestor = ancestor.getParent();
        }

        return false;
    }

    public boolean isAncestor(LabelTreeNode node) {
        return isAncestor(node.getData());
    }

    private static String getNodeName (Node n) {
		if( n.getNodeType() == Node.TEXT_NODE ){
			return n.getTextContent().trim();
		}
		return n.getNodeName();
	}

    @Override
    public String toString() {
        return buildString(new StringBuilder(),0).toString();
    }

    private void getLeaves(List<LabelTreeNode> leaves) {
        if(this.isLeaf()) {
            leaves.add(this);
        }
        else {
            for(Object child : children) {
                ((LabelTreeNode)child).getLeaves(leaves);
            }
        }
    }

    private StringBuilder buildString(StringBuilder stringBuilder, int level) {
        String indent = "";

        if(level > 0) {
            indent  = new String(new char[level]).replace("\0", "\t");
            stringBuilder.append("\n");
        }

        stringBuilder.append(indent);
        stringBuilder.append(data != null ? data.toString() : "[null]");

        for(int i = 0; i < getChildCount(); ++i) {
            ((LabelTreeNode)getChildAt(i)).buildString(stringBuilder, level + 1);
        }

        return stringBuilder;
    }

    public LabelTreeNode add(TreeNodeData data){
	    LabelTreeNode child = new LabelTreeNode(data);
	    add(child);

	    return child;
    }

	/**
	 * Compares the labels.
	 */
	public int compareTo(Object o) {		
		return getLabel().compareTo(((LabelTreeNode)o).getLabel());
	}
}

