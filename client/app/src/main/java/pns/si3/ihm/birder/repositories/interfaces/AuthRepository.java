package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.LiveData;

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
	 * @return The id of the user, if the user is authenticated;
	 * <code>null</code> otherwise.
	 */
	String getAuthenticationId();

	/**
	 * Signs in a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return The authenticated user.
	 */
	LiveData<User> signInWithEmailAndPassword(String email, String password);

	/**
	 * Signs up a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return The created user.
	 */
	LiveData<User> createUserWithEmailAndPassword(String email, String password);

	/**
	 * Updates the password of the authenticated user.
	 * @param newPassword The new password of the user.
	 * @return Whether the password has been updated, or not.
	 */
	LiveData<Boolean> updatePassword(String newPassword);

	/**
	 * Deletes the authenticated user.
	 * @return Whether the user has been deleted, or not.
	 */
	LiveData<Boolean> deleteUser();

	/**
	 * Signs out a user.
	 */
	void signOut();

	/**
	 * Returns the authentication errors (updated in real time).
	 * @return The authentication errors (updated in real time).
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the authentication errors.
	 * This avoids receiving the same error multiple times.
	 */
	void clearErrors();
}
