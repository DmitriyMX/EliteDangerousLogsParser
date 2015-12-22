package components;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;

import components.actions.AskForEliteDangerousLogsDirectoryAction;
import components.actions.FoldDialogsTreeAction;
import components.actions.ParseEliteDangerousLogsAction;
import components.actions.SetCleanTreeAction;
import components.actions.StopEliteDangerousLogsDirectoryReaderAction;
import components.actions.UnfoldDialogsTreeAction;
import components.trees.EliteDangerousDialogsTree;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsParserLauncher {

	JFrame frame;
	
	JButton selectButton;
	JTextField directoryInput;
	JCheckBox cleanDialogsTreeOption;
	JButton parseButton;
	JButton stopButton;
	
	JTextPane consolePane;
	
	private JToolBar dialogsToolBar;
	JButton foldDialogsButton;
	JButton unfoldDialogsButton;
	
	JTree dialogsTree;
	DefaultMutableTreeNode dialogsTreeRoot;
	
	private JToolBar statusBar;
	private JLabel usedFilesLabel;
	JTextField usedFilesValue;
	private JLabel emptyFilesLabel;
	JTextField emptyFilesValue;
	private JLabel errorFilesLabel;
	JTextField errorFilesValue;
	private JLabel timePlayedLabel;
	JTextField timePlayedValue;
	
	private EliteDangerousLogsParser parser;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EliteDangerousLogsParserLauncher window = new EliteDangerousLogsParserLauncher();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EliteDangerousLogsParserLauncher() {
		
		parser = new EliteDangerousLogsParser(this);
		
		initialize();
		
		// Hook Actions
		selectButton.setAction(new AskForEliteDangerousLogsDirectoryAction(parser));
		cleanDialogsTreeOption.setAction(new SetCleanTreeAction(parser));
		parseButton.setAction(new ParseEliteDangerousLogsAction(parser));
		stopButton.setAction(new StopEliteDangerousLogsDirectoryReaderAction(parser));
		
		foldDialogsButton.setAction(new FoldDialogsTreeAction(parser));
		unfoldDialogsButton.setAction(new UnfoldDialogsTreeAction(parser));
		
		// Set application initial states
		directoryInput.setText(parser.getDefaultEliteDangerousLogsPath());
		parser.setCleanDialogsTree(true);
		stopButton.setEnabled(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// Initialize Frame
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Initialize control bar
		JPanel controlsPanel = new JPanel();
		frame.getContentPane().add(controlsPanel, BorderLayout.NORTH);
		controlsPanel.setLayout(new BorderLayout(0, 0));
		
		// Initialize control bar components
		JToolBar toolBar = new JToolBar();
		controlsPanel.add(toolBar, BorderLayout.NORTH);
		
		selectButton = new JButton();
		toolBar.add(selectButton);
		
		directoryInput = new JTextField();
		toolBar.add(directoryInput);
		
		cleanDialogsTreeOption = new JCheckBox();
		toolBar.add(cleanDialogsTreeOption);
		
		parseButton = new JButton();
		toolBar.add(parseButton);
		
		stopButton = new JButton();
		toolBar.add(stopButton);
		
		// Initialize TabbedPane
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Initialize first tab
		consolePane = new JTextPane();
		consolePane.setEditable(false);
		JScrollPane consoleScrollPane = new JScrollPane(consolePane);
		tabbedPane.addTab("Console", null, consoleScrollPane, null);

		// Initialize second tab
		JPanel dialogsPanel = new JPanel();
		tabbedPane.addTab("Dialogs Tree", null, dialogsPanel, null);
		dialogsPanel.setLayout(new BorderLayout(0, 0));
		
		// Initialize results tool bar
		dialogsToolBar = new JToolBar();
		dialogsPanel.add(dialogsToolBar, BorderLayout.NORTH);
		
		foldDialogsButton = new JButton();
		dialogsToolBar.add(foldDialogsButton);
		
		unfoldDialogsButton = new JButton();
		dialogsToolBar.add(unfoldDialogsButton);
		
		// Initialize dialogs tree
		dialogsTreeRoot = new DefaultMutableTreeNode("Dialogs");
		dialogsTree = new EliteDangerousDialogsTree(dialogsTreeRoot);
		ToolTipManager.sharedInstance().registerComponent(dialogsTree);
		
		JScrollPane dialogsScrollPane = new JScrollPane(dialogsTree);
		dialogsPanel.add(dialogsScrollPane);
		
		// Initialize status bar
		statusBar = new JToolBar();
		FlowLayout statusBarFlowLayout = new FlowLayout();
		statusBarFlowLayout.setAlignment(FlowLayout.LEFT);
		statusBar.setLayout(statusBarFlowLayout);
		frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		usedFilesLabel = new JLabel("Used files :");
		statusBar.add(usedFilesLabel);
		
		usedFilesValue = new JTextField();
		usedFilesValue.setEditable(false);
		statusBar.add(usedFilesValue);
		usedFilesValue.setColumns(6);
		
		emptyFilesLabel = new JLabel("Empty files :");
		statusBar.add(emptyFilesLabel);
		
		emptyFilesValue = new JTextField();
		emptyFilesValue.setEditable(false);
		statusBar.add(emptyFilesValue);
		emptyFilesValue.setColumns(6);
		
		errorFilesLabel = new JLabel("Error files :");
		statusBar.add(errorFilesLabel);
		
		errorFilesValue = new JTextField();
		errorFilesValue.setEditable(false);
		statusBar.add(errorFilesValue);
		errorFilesValue.setColumns(6);
		
		timePlayedLabel = new JLabel("Time played :");
		statusBar.add(timePlayedLabel);
		
		timePlayedValue = new JTextField();
		timePlayedValue.setEditable(false);
		statusBar.add(timePlayedValue);
		timePlayedValue.setColumns(6);
	}

}
