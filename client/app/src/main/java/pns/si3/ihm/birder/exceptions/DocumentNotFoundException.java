package pns.si3.ihm.birder.exceptions;

/**
 * User not found exception.
 */
public class DocumentNotFoundException extends Exception {
	public DocumentNotFoundException() {
		super("The requested document was not found in the database.");
	}
}
