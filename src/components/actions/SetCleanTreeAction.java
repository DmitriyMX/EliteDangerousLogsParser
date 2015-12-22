/**
 * 
 */
package components.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

import components.EliteDangerousLogsParser;

/**
 * @author Naeregwen
 *
 */
public class SetCleanTreeAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5905678555465830832L;

	private EliteDangerousLogsParser parser;
	
	public SetCleanTreeAction(EliteDangerousLogsParser parser) {
		this.parser = parser;
		putValue(NAME, "Clean tree");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		parser.setCleanDialogsTree(((JCheckBox) e.getSource()).isSelected());
	}

}
