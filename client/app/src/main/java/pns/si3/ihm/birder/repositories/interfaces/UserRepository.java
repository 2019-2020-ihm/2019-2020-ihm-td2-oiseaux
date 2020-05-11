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
	MutableLiveData<User> getUser(String id);

	/**
	 * Creates a user in the database.
	 * @param user The user to be created.
	 * @return The live data of the created user.
	 */
	MutableLiveData<User> createUser(User user);

	/**
	 * Returns the live data of the user request errors.
	 * @return The live data of the user request errors.
	 */
	MutableLiveData<Exception> getErrors();
}