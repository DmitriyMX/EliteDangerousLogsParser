/**
 * 
 */
package components.workers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import commons.api.EliteDangerousParserState;
import commons.api.EliteDangerousParserState.ParserState;
import components.EliteDangerousLogsParser;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsDirectoryReader extends SwingWorker<File, EliteDangerousParserState> {

	EliteDangerousLogsParser parser;
	
	private static String extension = ".log";
    private static class LogFileFilter implements FilenameFilter{
        
        private String extension;
        
        public LogFileFilter(String extension) {
            this.extension = extension.toLowerCase();
        }
        
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(extension);
        }
    }	
	
	public EliteDangerousLogsDirectoryReader(EliteDangerousLogsParser parser) {
		this.parser = parser;
	}

	@Override
	protected File doInBackground() throws Exception {
		parser.start();
		File directory = new File(parser.getDirectoryInputText());
		if (directory.isDirectory()==false) {
			publish(new EliteDangerousParserState(ParserState.DIRECTORY_DOES_NOT_EXISTS, "Directory does not exists : " + parser.getDirectoryInputText()));
			return directory;
		}
		File[] directoryListing = directory.listFiles(new LogFileFilter(extension));
		if (directoryListing != null) {
			if (directoryListing.length == 0) {
				publish(new EliteDangerousParserState(ParserState.NO_FILES_WITH_EXTENSION, directory + " doesn't have any file with extension " + extension));
			} else
				for (File file : directoryListing) {
					if (file.isFile() && file.canRead()) {
						if (file.length() == 0) {
							publish(new EliteDangerousParserState(ParserState.INCREMENT_EMPTY_FILES, directoryListing.length));
							continue;
						}
						CountDownLatch doneSignal = new CountDownLatch(1);
						new EliteDangerousLogsFileParser(file, parser, doneSignal).execute();
						doneSignal.await();
						publish(new EliteDangerousParserState(ParserState.INCREMENT_USED_FILES, directoryListing.length));
					} else
						publish(new EliteDangerousParserState(ParserState.INCREMENT_ERROR_FILES, file + " is not a file or is not readable", directoryListing.length));
				}
		} else {
			// Handle the case where directory is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
		return directory;
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
			parser.printTextLine(this.getClass() + " canceled");
		} catch (InterruptedException e) {
			parser.printTextLine(this.getClass() + " interrupted");
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			parser.stop();
		}
	}
	
	/*/
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#process(java.util.List)
	 */
	@Override
    protected void process(List<EliteDangerousParserState> parserStates) {
        for (EliteDangerousParserState parserState : parserStates) {
        	switch (parserState.getState()) {
			case DIRECTORY_DOES_NOT_EXISTS:
			case NO_FILES_WITH_EXTENSION:
				parser.printTextLine(parserState.getMessage());
				break;
			case INCREMENT_EMPTY_FILES:
				parser.incrementEmptyFiles(parserState.getValue());
				break;
			case INCREMENT_USED_FILES:
				parser.incrementUsedFiles(parserState.getValue());
				parser.cleanDialogsTree();
				break;
			case INCREMENT_ERROR_FILES:
				parser.incrementErrorFiles(parserState.getValue());
				parser.printTextLine(parserState.getMessage());
				break;
        	}
        }
    }
}
