/**
 * 
 */
package components;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import commons.api.EliteDangerousLogsFile;
import commons.api.EliteDangerousLogsTreeNode;
import commons.i18n.EliteDangerousFormats;
import commons.windows.WinRegistry;
import components.workers.EliteDangerousLogsDirectoryReader;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsParser {

	EliteDangerousLogsParserLauncher launcher;
	ArrayList<EliteDangerousLogsFile> logs;
	String steamExecutableLocationPath;
	boolean cleanDialogsTree;
	
	private EliteDangerousLogsDirectoryReader reader;
	private DefaultMutableTreeNode currentLogsHeadLine;
	
	private int usedFiles;
	private int emptyFiles;
	private int errorFiles;
	
	private long timePlayedBefore;
	
	public EliteDangerousLogsParser(EliteDangerousLogsParserLauncher launcher) {
		
		this.launcher = launcher;
		logs = new ArrayList<EliteDangerousLogsFile>();
		
		// Read Steam Executable Location (SteamExe in registry)
		try {
			steamExecutableLocationPath = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER, "SOFTWARE\\Valve\\Steam", "SteamExe");
			if (steamExecutableLocationPath != null) {
//				if (librarian.getParameters().getOs().getPrefix() == OS.Prefix.Win) 
				steamExecutableLocationPath = 
						(steamExecutableLocationPath.substring(0, 1).toUpperCase() /* Camel case is nice */
						+ steamExecutableLocationPath.substring(1).replaceAll("/", File.separator + File.separator)).replace("steam.exe", "");
			} else {
				
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			steamExecutableLocationPath = System.getProperty("user.dir");
			e.printStackTrace();
		}
		
		cleanDialogsTree = false;
	}
	
	public String getSteamExecutableLocationPath() {
		return steamExecutableLocationPath;
	}
	
	public String getDefaultEliteDangerousLogsPath() {
		return steamExecutableLocationPath.equals(System.getProperty("user.dir")) ? steamExecutableLocationPath : 
			steamExecutableLocationPath 
				+ "SteamApps" + File.separator 
				+ "common" + File.separator 
				+ "Elite Dangerous" + File.separator 
				+ "Products" + File.separator 
				+ "FORC-FDEV-D-1010" + File.separator 
				+ "Logs";
	}
	
	public String getDirectoryInputText() {
		return launcher.directoryInput.getText();
	}
	
	public void setDirectoryInputText(String text) {
		launcher.directoryInput.setText(text);
	}
	
	public void setReader(EliteDangerousLogsDirectoryReader reader) {
		this.reader = reader;
	}

	public JRootPane getRootPane() {
		return launcher.frame.getRootPane();
	}
	
	public JFrame getFrame() {
		return launcher.frame;
	}
	
	public void start() {
		launcher.selectButton.setEnabled(false);
		launcher.directoryInput.setEnabled(false);
		launcher.parseButton.setEnabled(false);
		launcher.cleanDialogsTreeOption.setEnabled(false);
		launcher.foldDialogsButton.setEnabled(false);
		launcher.unfoldDialogsButton.setEnabled(false);
		launcher.dialogsTree.setEnabled(false);
		
		launcher.stopButton.setEnabled(true);
		
		clearStatus();
		clearConsole();
		clearDialogsTree();
	}
	
	public void stop() {
		launcher.selectButton.setEnabled(true);
		launcher.directoryInput.setEnabled(true);
		launcher.parseButton.setEnabled(true);
		launcher.cleanDialogsTreeOption.setEnabled(true);
		launcher.foldDialogsButton.setEnabled(true);
		launcher.unfoldDialogsButton.setEnabled(true);
		launcher.dialogsTree.setEnabled(true);
		
		launcher.stopButton.setEnabled(false);
		
		deployDialogsTree();
	}
	
	public void stopReader() {
		try {
			reader.cancel(true);
			reader.get();
		} catch (CancellationException e) {
			printTextLine(this.getClass() + " canceled");
		} catch (InterruptedException e) {
			printTextLine(this.getClass() + " interrupted");
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			stop();
		}
	}
	
	/**
	 * Add a logFile to tree
	 * 
	 * @param logsTreeNode
	 */
	public void addLogsFile(EliteDangerousLogsTreeNode logsTreeNode) {
		logs.add(logsTreeNode.getLogsFile());
		launcher.dialogsTreeRoot.add(currentLogsHeadLine = new DefaultMutableTreeNode(logsTreeNode));
		printTextLine(logsTreeNode.getLogsFile().getFilename() + " - " + EliteDangerousFormats.longDateFormat.format(logsTreeNode.getLogsFile().getDateLastModified()));
	}

	/**
     * Add a logLine to current tree node
     * 
	 * @param logsTreeNode
	 */
	public void addLogsLine(EliteDangerousLogsTreeNode logsTreeNode) {
		currentLogsHeadLine.add(new DefaultMutableTreeNode(logsTreeNode));
		printTextLine(logsTreeNode.getLogsLine().getOriginalText());
	}

	private static SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
	
	/**
	 * Add text to end of document output pane
	 * Update caret position
	 * 
	 * @param text text to display
	 */
	public void printTextLine(String text) {
		Document document = launcher.consolePane.getDocument();
		try {
			document.insertString(document.getLength(), (document.getLength() > 0 ? "\n" : "") + text, simpleAttributeSet);
			launcher.consolePane.setCaretPosition(document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void clearConsole() {
		launcher.consolePane.setText("");
	}
	
	public void setCleanDialogsTree(boolean cleanDialogsTree) {
		launcher.cleanDialogsTreeOption.setSelected(this.cleanDialogsTree = cleanDialogsTree);
	}
	
	public void cleanDialogsTree() {
		if (cleanDialogsTree && currentLogsHeadLine.getChildCount() == 0)
			((DefaultTreeModel) launcher.dialogsTree.getModel()).removeNodeFromParent(currentLogsHeadLine);
	}
	
	private void clearDialogsTree() {
		launcher.dialogsTreeRoot.removeAllChildren();
		((DefaultTreeModel) launcher.dialogsTree.getModel()).reload(launcher.dialogsTreeRoot);
	}

	public void expandFirstRow() {
		launcher.dialogsTree.expandRow(0);
	}
	
	public void expandAllDialogsTree() {
	    int row = 0;
	    while (row < launcher.dialogsTree.getRowCount())
	    	launcher.dialogsTree.expandRow(row++);
	}
	
	public void collapseAllDialogsTree() {
		int row = launcher.dialogsTree.getRowCount() - 1;
		while (row >= 1)
			launcher.dialogsTree.collapseRow(row--);
	}
	
	public void deployDialogsTree() {
		collapseAllDialogsTree();
		expandFirstRow();
	}
	
	private void clearStatus() {
		usedFiles = 0;
		emptyFiles = 0;
		errorFiles = 0;
		timePlayedBefore = 0;
	}
	
	public void incrementUsedFiles(int filesCount) {
		launcher.usedFilesValue.setText(++usedFiles + "/" + filesCount);
	}
	
	public void incrementEmptyFiles(int filesCount) {
		launcher.emptyFilesValue.setText(++emptyFiles + "/" + filesCount);
	}
	
	public void incrementErrorFiles(int filesCount) {
		launcher.errorFilesValue.setText(++errorFiles + "/" + filesCount);
	}
	
	private long updateTimePlayed(Date start, Date stop) {
		long timePlayed = stop.getTime() - start.getTime();
		printTextLine("timePlayedBefore = " + timePlayedBefore + ", timePlayed = " + timePlayed + ", start = " + EliteDangerousFormats.longDateFormat.format(start) + ", stop = " + EliteDangerousFormats.longDateFormat.format(stop));
		launcher.timePlayedValue.setText(parseTimePlayed(timePlayedBefore + timePlayed));
		return timePlayed;
	}
	
	public void setTimePlayed(Date start, Date stop) {
		if (timePlayedBefore == 0)
			timePlayedBefore = updateTimePlayed(start, stop);
		else
			timePlayedBefore += updateTimePlayed(start, stop);
	}
	
	private static final String TIME_PLAYED_FORMAT = "%d:%02d:%02d";
	
	public static String parseTimePlayed(long timePlayed) {
		return String.format(TIME_PLAYED_FORMAT,
				TimeUnit.MILLISECONDS.toHours(timePlayed),
				TimeUnit.MILLISECONDS.toMinutes(timePlayed) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timePlayed)),
				TimeUnit.MILLISECONDS.toSeconds(timePlayed) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timePlayed)));
	}
}
