package components.trees;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import commons.api.EliteDangerousLogsTreeNode;
import components.trees.renderers.EliteDangerousDialogsTreeNodeRenderer;

public class EliteDangerousDialogsTree extends JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4311817736907822227L;

	public EliteDangerousDialogsTree(DefaultMutableTreeNode root) {
		super(root);
		setCellRenderer(new EliteDangerousDialogsTreeNodeRenderer());
	}
	
	@Override
	public String getToolTipText(MouseEvent evt) {
		if (getRowForLocation(evt.getX(), evt.getY()) == -1) return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
		if (node.getUserObject() instanceof EliteDangerousLogsTreeNode) {
			EliteDangerousLogsTreeNode nodeData = (EliteDangerousLogsTreeNode) node.getUserObject();
			switch (nodeData.getTreeDataType()) {
			case HEADER:
				return nodeData.getLogsFile().getFilename();
			case LINE:
				return nodeData.getLogsLine().getOriginalText();
			case TIMESTAMP:
				break;
			}
		}
		return super.getToolTipText();
	}
}
