/**
 * 
 */
package components.trees.renderers;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import commons.api.EliteDangerousLogsTreeNode;
import commons.i18n.EliteDangerousFormats;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousDialogsTreeNodeRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8192891908925386235L;

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof String) {
			setText(node.getUserObject().toString());
		} else {
			EliteDangerousLogsTreeNode nodeData = (EliteDangerousLogsTreeNode) (node.getUserObject());
			switch (nodeData.getTreeDataType()) {
			case HEADER:
				setText(EliteDangerousFormats.longDateFormat.format(nodeData.getLogsFile().getDateLastModified()));
				break;
			case LINE:
				setText(nodeData.getLogsLine().getOriginalText());
				break;
			case TIMESTAMP:
				setText("");
				break;
			}
			
		}
		return this;
	}
	
}
