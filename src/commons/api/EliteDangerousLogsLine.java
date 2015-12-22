/**
 * 
 */
package commons.api;

import java.util.Date;

/**
 * @author Naeregwen
 *
 */
public class EliteDangerousLogsLine {

	Date dateLogged;
	String text;
	String sender;
	String receiver;
	String originalText;
	
	public EliteDangerousLogsLine(Date dateLogged, String originalText) {
		this.dateLogged = dateLogged;
		this.originalText = originalText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Date getDateLogged() {
		return dateLogged;
	}

	public String getOriginalText() {
		return originalText;
	}
	
}
