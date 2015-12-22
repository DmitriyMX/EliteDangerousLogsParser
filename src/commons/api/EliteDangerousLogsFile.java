/**
 * 
 */
package commons.api;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsFile {

	String filename;
	Date dateLastModified;
	ArrayList<EliteDangerousLogsLine> lines;
	
	public EliteDangerousLogsFile(String filename, Date dateLastModified) {
		this.filename = filename;
		this.dateLastModified = dateLastModified;
		lines = new ArrayList<EliteDangerousLogsLine>();
	}
	
	public String getFilename() {
		return filename;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void addLogsLine(EliteDangerousLogsLine logsLine) {
		lines.add(logsLine);
	}

	public boolean hasLogsLines() {
		return lines.size() != 0;
	}
	
	public EliteDangerousLogsLine getLastLogsLine()  {
		return lines.get(lines.size()-1);
	}
}
