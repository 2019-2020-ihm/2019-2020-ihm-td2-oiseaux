package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pns.si3.ihm.birder.models.User;

/**
 * User repository.
 *
 * Manages the users in the database.
 */
public interface UserRepository {
	/**
	 * Get a user from the database.
	 * @param id The id of the user.
	 * @return The live data of the user.
	 */
	LiveData<User> getUser(String id);

	/**
	 * Sets a user in the database.
	 * @param user The user to be set.
	 * @return The live data of the created user.
	 */
	LiveData<User> setUser(User user);

	/**
	 * Returns the live data of the user request errors.
	 * @return The live data of the user request errors.
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the live data of the user request errors.
	 * This avoid receiving the same error multiple times.
	 */
	void clearErrors();
}
