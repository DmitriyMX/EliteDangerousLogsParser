/**
 * 
 */
package components.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import components.EliteDangerousLogsParser;
import components.dialogs.CenteredFileChooser;

/**
 * @author Naeregwen
 *
 */
public class AskForEliteDangerousLogsDirectoryAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1444754712044834173L;
	
	private EliteDangerousLogsParser parser;
	
	private String inputDirectoryPath = "";
	
	private String defaultDirectory = System.getProperty("user.dir");

	public AskForEliteDangerousLogsDirectoryAction(EliteDangerousLogsParser parser) {
		this.parser = parser;
		putValue(NAME, "Select");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		this.defaultDirectory = parser.getDefaultEliteDangerousLogsPath();
		
		CenteredFileChooser fileChooser = new CenteredFileChooser(parser.getRootPane());
		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Activate "Only directory" filter
		fileChooser.setAcceptAllFileFilterUsed(false); // disable the "All files" option.
	    
		File directory = new File(defaultDirectory);
		fileChooser.setCurrentDirectory(directory.isDirectory() ? new File(defaultDirectory) : new File(System.getProperty("user.dir")));
		
		if (parser.getDirectoryInputText() != null && !parser.getDirectoryInputText().trim().equals("")) {
			File file = new File(parser.getDirectoryInputText());			
			if (!file.isDirectory()) {
				file = file.getParentFile();
				if (file != null && file.exists())
					setDirectory(fileChooser, file);
			} else
				setDirectory(fileChooser, file);
		}
		
		int userAction = fileChooser.showOpenDialog(parser.getFrame());
		if (userAction == JFileChooser.APPROVE_OPTION) {
			inputDirectoryPath = fileChooser.getSelectedFile().getAbsoluteFile().toString();
			defaultDirectory = fileChooser.getSelectedFile().getParent();
			parser.setDirectoryInputText(inputDirectoryPath);
		}
		if (userAction == JFileChooser.CANCEL_OPTION) {
			inputDirectoryPath = "";
		}
		if (userAction == JFileChooser.ERROR_OPTION) {
			inputDirectoryPath = "";
		}
	}

	/**
	 * Try to set a fileChooser defaultDirectory with file passed
	 * 
	 * @param fileChooser the fileChosser to set
	 * @param file the file to help determine new default directory
	 * 
	 * @return true if fileChooser has been set
	 */
	private boolean setDirectory(CenteredFileChooser fileChooser, File file) {
		if (file.exists() && file.canRead()) {
			if (file.isDirectory())
				fileChooser.setCurrentDirectory(file);
			else 
				fileChooser.setCurrentDirectory(new File(file.getPath()));
			return true;
		}
		return false;
	}
	
}
