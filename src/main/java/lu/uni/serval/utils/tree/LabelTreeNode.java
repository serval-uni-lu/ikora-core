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
        
	/**
	 * no node id
	 */
	public final static int NO_NODE = -1;
	
	/**
	 * no tree id is defined
	 */
	public final int NO_TREE_ID = -1;
	
	private int treeID = NO_TREE_ID;
	private int nodeID = NO_NODE;
	private TreeNodeData data = null;
	
	/**
	 * Use only this constructor!
	 */
	public LabelTreeNode(TreeNodeData data) {
		super();

		setData(data);
	}

	public String getLabel() {
		return data.getLabel();
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
	}

	public TreeNodeData getData() {
		return data;
	}
	
	public int getNodeCount() {
		int sum = 1;
		for (Enumeration e = children(); e.hasMoreElements();) {
			sum += ((LabelTreeNode)e.nextElement()).getNodeCount();
		}
		return sum;
	}

	public int getLeavesSize() {
	    return getLeaves().size();
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

