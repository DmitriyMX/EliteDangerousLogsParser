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
public class StopEliteDangerousLogsDirectoryReaderAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 116686659502342832L;

	private EliteDangerousLogsParser parser;
	
	public StopEliteDangerousLogsDirectoryReaderAction(EliteDangerousLogsParser parser) {
		this.parser = parser;
		putValue(NAME, "Stop");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		parser.stopReader();
	}

}
