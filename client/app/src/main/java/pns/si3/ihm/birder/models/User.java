package pns.si3.ihm.birder.models;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * User.
 */
public class User {
	/**
	 * The unique id of the user.
	 */
	public String id;

	/**
	 * The first name of the user.
	 */
	public String firstName;

	/**
	 * The last name of the user.
	 */
	public String lastName;

	/**
	 * The email of the user.
	 */
	public String email;

	/**
	 * True if the user want to be notify of all of the report
	 * False otherwise
	 */
	public Boolean allNotificationActivate;

	public User() {}

	public User(String id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.allNotificationActivate = true;
	}

}
