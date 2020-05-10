package pns.si3.ihm.birder.exceptions;

/**
 * User not found exception.
 */
public class UserNotFoundException extends Exception {
	public UserNotFoundException() {
		super("The requested user was not found in the database.");
	}
}
