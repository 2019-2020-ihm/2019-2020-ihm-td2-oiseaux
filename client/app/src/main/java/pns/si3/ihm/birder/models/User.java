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

	/**
	 * True if the user want to be notify of all of the report
	 * False otherwise
	 */
	private Boolean allNotificationActivate;

	private ArrayList<String> speciesNotifications;

	public User() {}

	public User(String firstName, String lastName, String email) {
		this.id = null;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.allNotificationActivate = true;
		this.speciesNotifications = new ArrayList<>();
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

	public Boolean getAllNotificationActivate(){
	    return allNotificationActivate;
    }


    public void setAllNotificationActivate(Boolean allNotificationActivate){
	    this.allNotificationActivate = allNotificationActivate;
    }

	public ArrayList<String> getSpeciesNotifications() {
		return speciesNotifications;
	}

	public void setSpeciesNotifications(ArrayList<String> speciesNotifications) {
		this.speciesNotifications = speciesNotifications;
	}

	public void deleteItemToSpeciesNotifications(String speciesName){
		this.speciesNotifications.remove(speciesName);
	}
}
