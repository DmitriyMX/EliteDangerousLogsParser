/**
 * 
 */
package components.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import components.EliteDangerousLogsParser;

/**
 * @author Naeregwen
 *
 */
public class UnfoldDialogsTreeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3855240668758124553L;

	private EliteDangerousLogsParser parser;
	
	public UnfoldDialogsTreeAction(EliteDangerousLogsParser parser) {
		this.parser = parser;
		putValue(NAME, "Unfold");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		parser.expandAllDialogsTree();
	}

}
