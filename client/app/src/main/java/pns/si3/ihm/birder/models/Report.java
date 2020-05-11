package pns.si3.ihm.birder.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Bird report.
 *
 * Represents a bird report created by a user.
 */
public class Report {
	/**
	 * The unique id of the report.
	 */
	@Exclude
	public String id;

	/**
	 * The id of the user.
	 */
	public String userId;

	/**
	 * The species of the report.
	 */
	public String species;

	/**
	 * The number of birds.
	 */
	public int number;

//	/**
//	 * The location of the report.
//	 */
//	public GeoPoint location;
//
//	/**
//	 * The date of the report.
//	 */
//	@ServerTimestamp
//	public Date date;

	public Report() {}

	public Report(String id, String userId, String species, int number) {
		this.id = id;
		this.userId = userId;
		this.species = species;
		this.number = number;
	}
}
