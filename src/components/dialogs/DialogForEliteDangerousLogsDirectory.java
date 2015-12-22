/**
 * 
 */
package components.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Naeregwen
 *
 */
public class DialogForEliteDangerousLogsDirectory extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6529681808591245829L;
	
	private JButton selectButton;
	private JTextField input;
	
	private JButton okButton;
	private JButton cancelButton;
	
	private String inputFilename = "";
	private String inputDirectory = "";
	
	private String defaultDirectory = System.getProperty("user.dir");
	private String defaultFilename = "netLog.xml";

	public DialogForEliteDangerousLogsDirectory(JFrame frame) {
		super(frame);
		initComponents();
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	private void initComponents() {

		selectButton = new JButton("Select");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectPerformed(event);
			}
		});
		
		input = new JTextField();
		input.setText(defaultFilename);

		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				okPerformed(event);
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelPerformed(event);
			}
		});
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		getContentPane().add(input, BorderLayout.NORTH);
		getContentPane().add(selectButton, BorderLayout.WEST);
		getContentPane().add(okButton, BorderLayout.CENTER);
		getContentPane().add(cancelButton, BorderLayout.EAST);
		
		// Hook VK_ENTER/VK_ESCAPE listeners to rootPane
		rootPane.setDefaultButton(okButton);
		setEscapeAction();
	}
	
	/**
	 * Do user confirmation of file choosing
	 */
	private void ok() {
		inputFilename = input.getText();
		if (inputDirectory.equals(""))
			inputDirectory = defaultDirectory.endsWith(System.getProperty("file.separator")) ? defaultDirectory : defaultDirectory + System.getProperty("file.separator");
		this.dispose();
	}
	
	/**
	 * Do user cancellation of file choosing
	 */
	private void cancel() {
		inputFilename = "";
		inputDirectory = "";
		this.dispose();
	}
	
	/**
	 * Hook VK_ESCAPE listener
	 */
	private void setEscapeAction() {
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escPressed");
		rootPane.getActionMap().put("escPressed", new AbstractAction("escPressed") {
			private static final long serialVersionUID = 1282000323553661543L;
			public void actionPerformed(ActionEvent actionEvent) {
				cancel();
			}
		});
	}

	
	/**
	 * Try to set a fileChooser defaultDirectory with file passed
	 * 
	 * @param fileChooser the fileChosser to set
	 * @param file the file to help determine new default directory
	 * 
	 * @return true if fileChooser has been set
	 *  
	 * FIXME: Target directory difference between load (canRead) & save (canWrite) ?
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
	
	/**
	 * User event on start file browsing from application base directory
	 * 
	 * @param event the source event
	 */
	private void selectPerformed(ActionEvent event) {
		defaultDirectory = System.getProperty("user.dir");
		actionPerformed(event);
	}

	public void actionPerformed(ActionEvent arg0) {
		CenteredFileChooser fileChooser = new CenteredFileChooser(this.getRootPane());
		
		File directory = new File(defaultDirectory);
		fileChooser.setCurrentDirectory(directory.isDirectory() ? new File(defaultDirectory) : new File(System.getProperty("user.dir")));
		
		if (input.getText() != null && !input.getText().trim().equals("")) {
			File file = new File(input.getText());			
			if (!file.isDirectory()) {
				file = file.getParentFile();
				if (file != null && file.exists())
					setDirectory(fileChooser, file);
			} else
				setDirectory(fileChooser, file);
		}
		FileNameExtensionFilter XMLFileFilter = new FileNameExtensionFilter("Fichiers XML", "xml");
		fileChooser.addChoosableFileFilter(XMLFileFilter);
		fileChooser.setFileFilter(XMLFileFilter);
		
		int userAction = fileChooser.showOpenDialog(this);
		if (userAction == JFileChooser.APPROVE_OPTION) {
			inputFilename = fileChooser.getSelectedFile().getName();
			defaultDirectory = inputDirectory = fileChooser.getSelectedFile().getParent();
			input.setText(inputFilename);
		}
		if (userAction == JFileChooser.CANCEL_OPTION) {
			inputFilename = "";
			inputDirectory = "";
		}
		if (userAction == JFileChooser.ERROR_OPTION) {
			inputFilename = "";
			inputDirectory = "";
		}
	}
	
	/**
	 * User event on confirmation of file choosing
	 * 
	 * @param event the source event
	 */
	private void okPerformed(ActionEvent event) {
		ok();
	}

	/**
	 * User event on cancellation of file choosing
	 * 
	 * @param event the source event
	 */
	private void cancelPerformed(ActionEvent event) {
		cancel();
	}

}
