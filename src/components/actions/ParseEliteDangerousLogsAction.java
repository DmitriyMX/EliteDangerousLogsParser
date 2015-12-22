/**
 * 
 */
package components.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import components.EliteDangerousLogsParser;
import components.workers.EliteDangerousLogsDirectoryReader;

/**
 * @author Naeregwen
 *
 */
public class ParseEliteDangerousLogsAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 465598346843941938L;
	
	EliteDangerousLogsParser parser;
	JTextField directoryInput;
	JTextPane textPane;
	
	public ParseEliteDangerousLogsAction(EliteDangerousLogsParser parser) {
		this.parser = parser;
		putValue(NAME, "Parse");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		EliteDangerousLogsDirectoryReader reader = new EliteDangerousLogsDirectoryReader(parser);
		parser.setReader(reader);
		reader.execute();
	}

}
