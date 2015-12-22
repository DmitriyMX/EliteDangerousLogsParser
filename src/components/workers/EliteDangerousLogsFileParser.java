/**
 * 
 */
package components.workers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import commons.api.EliteDangerousLogsFile;
import commons.api.EliteDangerousLogsLine;
import commons.api.EliteDangerousLogsTreeNode;
import commons.api.EliteDangerousLogsTreeNode.TreeDataType;
import components.EliteDangerousLogsParser;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsFileParser extends SwingWorker<File, EliteDangerousLogsTreeNode> {

	File file;
	EliteDangerousLogsParser parser;
	CountDownLatch doneSignal;
	
	EliteDangerousLogsFile currentLogsFile;
	private Date startLogsDate;
	private Date lastLogsDate;
	
	enum MessageType {
		SEND,
		RECEIVED,
		TIMESTAMP;
	}
	
	private static String timeHeaderPattern = "^\\{(([\\d]*):([\\d]*):([\\d]*))\\}";
	private static Pattern timeLoggedPattern = Pattern.compile(timeHeaderPattern+"(.*)");
	private static Pattern messageSendedPattern = Pattern.compile(timeHeaderPattern+"\\s*TalkChannel::SendTextMessageTo\\[([\\d]*)\\]\\s*-(.*)");
	private static Pattern messageReceivedPattern = Pattern.compile(timeHeaderPattern+"\\s*TalkChannel:SendTextMessage\\s*\\(received\\)\\s*([^:]*):(.*)");
	
	public EliteDangerousLogsFileParser(File file, EliteDangerousLogsParser parser, CountDownLatch doneSignal) {
		this.file = file;
		this.parser = parser;
		this.doneSignal = doneSignal;
	}

	@Override
	protected File doInBackground() throws Exception {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
			currentLogsFile = new EliteDangerousLogsFile(file.getName(), new Date(file.lastModified()));
			publish(new EliteDangerousLogsTreeNode(currentLogsFile, TreeDataType.HEADER, null));
			String line;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					Matcher timeLoggedMatcher = timeLoggedPattern.matcher(line);
					if (timeLoggedMatcher.matches()) {
						EliteDangerousLogsLine logsLine = newDialogsLine(MessageType.TIMESTAMP, line, timeLoggedMatcher);
						publish(new EliteDangerousLogsTreeNode(currentLogsFile, TreeDataType.TIMESTAMP, logsLine));
					}
					Matcher messageSendedMatcher = messageSendedPattern.matcher(line);
					if (messageSendedMatcher.matches()) {
						EliteDangerousLogsLine logsLine = newDialogsLine(MessageType.SEND, line, messageSendedMatcher);
				        currentLogsFile.addLogsLine(logsLine);
						publish(new EliteDangerousLogsTreeNode(currentLogsFile, TreeDataType.LINE, logsLine));
					}
					Matcher messageReceivedMatcher = messageReceivedPattern.matcher(line);
					if (messageReceivedMatcher.matches()) {
						EliteDangerousLogsLine logsLine = newDialogsLine(MessageType.RECEIVED, line, messageReceivedMatcher);
						currentLogsFile.addLogsLine(logsLine);
						publish(new EliteDangerousLogsTreeNode(currentLogsFile, TreeDataType.LINE, logsLine));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private EliteDangerousLogsLine newDialogsLine(MessageType messageType, String originalText, Matcher messageMatcher) {
		
        Integer hoursLogged = Integer.parseInt(messageMatcher.group(2));
        Integer minutesLogged = Integer.parseInt(messageMatcher.group(3));
        Integer secondsLogged = Integer.parseInt(messageMatcher.group(4));
		
		Calendar dateLogged = Calendar.getInstance();
		dateLogged.setTime(currentLogsFile.getDateLastModified());
        dateLogged.set(Calendar.HOUR, hoursLogged);
        dateLogged.set(Calendar.MINUTE, minutesLogged);
        dateLogged.set(Calendar.SECOND, secondsLogged);
        
		// Check overturn
		if (dateLogged.before(currentLogsFile.getDateLastModified())) {
			dateLogged.setTime(currentLogsFile.getDateLastModified());
			dateLogged.add(Calendar.DATE, 1);
	        dateLogged.set(Calendar.HOUR, hoursLogged);
	        dateLogged.set(Calendar.MINUTE, minutesLogged);
	        dateLogged.set(Calendar.SECOND, secondsLogged);
		}
			
		EliteDangerousLogsLine logsLine = new EliteDangerousLogsLine(dateLogged.getTime(), originalText);
		
        switch (messageType) {
        case SEND:
        	logsLine.setSender("Me");
        	logsLine.setReceiver(messageMatcher.group(5));
        	logsLine.setText(messageMatcher.group(6));
        	break;
        case RECEIVED:
        	logsLine.setSender(messageMatcher.group(5));
        	logsLine.setReceiver("Me");
        	logsLine.setText(messageMatcher.group(6));
        	break;
		case TIMESTAMP:
	    	if (startLogsDate == null)
	    		startLogsDate = dateLogged.getTime();
	    	lastLogsDate = dateLogged.getTime();
 			break;
        }
   	
        return logsLine;
	}
	
	/*/
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		try {
			get();
		} catch (CancellationException e) {
			parser.printTextLine(this.getClass() + " (" + this.file.getName() + ") canceled");
		} catch (InterruptedException e) {
			parser.printTextLine(this.getClass() + " (" + this.file.getName() + ") interrupted");
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			parser.setTimePlayed(startLogsDate, lastLogsDate);
			doneSignal.countDown();
		}
	}
	
	/*/
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
    protected void process(List<EliteDangerousLogsTreeNode> logsTreeNodes) {
        for (EliteDangerousLogsTreeNode logsTreeNode : logsTreeNodes) {
        	switch (logsTreeNode.getTreeDataType()) {
        	case HEADER:
        		parser.addLogsFile(logsTreeNode);
        		break;
        	case LINE:
        		parser.addLogsLine(logsTreeNode);
        		break;
			case TIMESTAMP:
//				parser.updateTimePlayed(startLogsDate, logsTreeNode.getLogsLine().getDateLogged());
				break;
        	}
        }
    }

}
