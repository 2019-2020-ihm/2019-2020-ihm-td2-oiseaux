package pns.si3.ihm.birder.repositories;

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
	 * Signs in a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the authenticated user.
	 */
	MutableLiveData<User> signInWithEmailAndPassword(String email, String password);

	/**
	 * Signs up a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the authenticated user.
	 */
	MutableLiveData<User> createUserWithEmailAndPassword(String email, String password);

	/**
	 * Returns the live data of the authentication errors.
	 * @return The live data of the authentication errors.
	 */
	MutableLiveData<Exception> getErrors();
}
