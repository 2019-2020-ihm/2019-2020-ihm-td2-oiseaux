package pns.si3.ihm.birder.exceptions;

/**
 * User not found exception.
 */
public class DocumentNotCreatedException extends Exception {
	public DocumentNotCreatedException() {
		super("The document was not created in the database.");
	}
}
