/**
 * 
 */
package commons.api;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousParserState {
	
	public enum ParserState {
		DIRECTORY_DOES_NOT_EXISTS,
		NO_FILES_WITH_EXTENSION,
		INCREMENT_EMPTY_FILES,
		INCREMENT_USED_FILES,
		INCREMENT_ERROR_FILES
	}

	ParserState state;
	String message;
	int value;
	
	public EliteDangerousParserState(ParserState state, String message) {
		this.state = state;
		this.message = message;
	}
	
	public EliteDangerousParserState(ParserState state, int value) {
		this.state = state;
		this.value = value;
	}

	public EliteDangerousParserState(ParserState state, String message, int value) {
		super();
		this.state = state;
		this.message = message;
		this.value = value;
	}

	public ParserState getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public int getValue() {
		return value;
	}

	
}
