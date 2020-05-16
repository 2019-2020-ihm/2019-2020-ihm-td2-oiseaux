package pns.si3.ihm.birder.exceptions;

public class UserNotAuthenticatedException extends Exception {
	public UserNotAuthenticatedException() {
		super("The user was not authenticated.");
	}
}
