package pns.si3.ihm.birder.models;

/**
 * User.
 *
 * The model of the user.
 */
public class User {
	public String firstName;
	public String lastName;
	public String email;

	public User() {}

	public User(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
}
