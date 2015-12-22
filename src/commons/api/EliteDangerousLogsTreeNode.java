/**
 * 
 */
package commons.api;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsTreeNode {
	
	public enum TreeDataType {
		HEADER,
		LINE,
		TIMESTAMP
	}
	
	EliteDangerousLogsFile logsFile;
	TreeDataType treeDataType;
	EliteDangerousLogsLine logsLine;
	
	public EliteDangerousLogsTreeNode(EliteDangerousLogsFile logsFile, TreeDataType treeDataType, EliteDangerousLogsLine logsLine) {
		this.logsFile = logsFile;
		this.treeDataType = treeDataType;
		this.logsLine = logsLine;
	}

	public EliteDangerousLogsFile getLogsFile() {
		return logsFile;
	}

	public TreeDataType getTreeDataType() {
		return treeDataType;
	}

	public EliteDangerousLogsLine getLogsLine() {
		return logsLine;
	}
	
}
