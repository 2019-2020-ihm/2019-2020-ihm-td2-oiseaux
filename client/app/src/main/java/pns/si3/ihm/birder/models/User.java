package pns.si3.ihm.birder.models;

import com.google.firebase.firestore.Exclude;

/**
 * User.
 */
public class User {
	/**
	 * The unique id of the user.
	 */
	private String id;

	/**
	 * The first name of the user.
	 */
	private String firstName;

	/**
	 * The last name of the user.
	 */
	private String lastName;

	/**
	 * The email of the user.
	 */
	private String email;

	public User() {}

	public User(String id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	@Exclude
	public String getId() {
		return id;
	}

	@Exclude
	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
