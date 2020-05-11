package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pns.si3.ihm.birder.models.User;

/**
 * Authentication repository.
 *
 * Manages all the authentication tasks.
 */
public interface AuthRepository {
	/**
	 * Checks if the user is authenticated.
	 * @return Whether the user is authenticated, or not.
	 */
	boolean isAuthenticated();

	/**
	 * Returns the id of the authenticated user.
	 * @return The id of the user, if he's authenticated, null otherwise.
	 */
	String getAuthenticationId();

	/**
	 * Signs in a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the authenticated user.
	 */
	LiveData<User> signInWithEmailAndPassword(String email, String password);

	/**
	 * Signs up a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the authenticated user.
	 */
	LiveData<User> createUserWithEmailAndPassword(String email, String password);

	/**
	 * Signs out a user.
	 */
	void signOut();

	/**
	 * Returns the live data of the authentication errors.
	 * @return The live data of the authentication errors.
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the live data of the authentication errors.
	 * This avoid receiving the same error multiple times.
	 */
	void clearErrors();
}
