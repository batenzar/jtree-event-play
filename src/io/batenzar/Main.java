package io.batenzar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Simple JTree for testing tree.
 * 
 * @author batenzar
 *
 */
public class Main {

	private static JTree jtree;

	public static void main(String[] args) {
		JDialog dialog = new JDialog();
		dialog.setPreferredSize(new Dimension(400,800));
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setLayout(new FlowLayout());

		jtree = new JTree(); // initial JTree with sample data.
		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(Main::addChild);

		JButton delBtn = new JButton("Delete");
		delBtn.addActionListener(Main::deleteNode);

		JButton sortBtn = new JButton("Sort child");
		sortBtn.addActionListener(Main::sortChild);

		registerExpandListner();

		dialog.add(jtree);
		dialog.add(addBtn);
		dialog.add(delBtn);
		dialog.add(sortBtn);
		dialog.pack();
		dialog.setVisible(true);

	}

	/**
	 * Register listener for debugging.
	 */
	private static void registerExpandListner() {
		jtree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				System.out.println("TreeExpansionListener - TreeExpanded was called.");
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				System.out.println("TreeExpansionListener - TreeCollapsed was called.");
			}
		});

		jtree.addTreeWillExpandListener(new TreeWillExpandListener() {

			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				System.out.println("TreeWillExpandListener - TreeExpanded was called.");
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				System.out.println("TreeWillExpandListener - TreeCollapsed was called.");
			}
		});
	}

	/**
	 * Sort the children of selected tree node.
	 * Keep expanding state.
	 * 
	 * @param e
	 */
	private static void sortChild(ActionEvent e) {
		TreePath selectionPath = jtree.getSelectionPath();
		System.out.println(selectionPath);

		DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();

		// keep expanding state
		Enumeration<TreePath> expandedDescendants = jtree.getExpandedDescendants(selectionPath);

		List<DefaultMutableTreeNode> children = new ArrayList<>();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
		for (int i = 0; i < parent.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
			children.add(child);

			model.nodesWereRemoved(parent, new int[] { 0 }, new TreeNode[] { child });
		}

		Collections.sort(children, new Comparator<DefaultMutableTreeNode>() {
			@Override
			public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
				return o2.hashCode() - o1.hashCode();
			}
		});

		for (int i = 0; i < children.size(); i++) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode) children.get(i);
			model.insertNodeInto(tn, parent, i);
		}
		
		// The method below will collapse tree nodes after calling
//		int[] a = IntStream.rangeClosed(0, children.size()-1).toArray();
//		model.nodesWereInserted(lastPathComponent, a);

		// re-apply expanding state of children nodes
		while (expandedDescendants.hasMoreElements()) {
			jtree.expandPath(expandedDescendants.nextElement());
		}
		

	}

	private static void addChild(ActionEvent e) {
		TreePath selectionPath = jtree.getSelectionPath();
		System.out.println(selectionPath);

		// TODO Auto-generated method stub
	}

	private static void deleteNode(ActionEvent e) {
		TreePath selectionPath = jtree.getSelectionPath();
		System.out.println(selectionPath);

		// TODO Auto-generated method stub
	}

}
