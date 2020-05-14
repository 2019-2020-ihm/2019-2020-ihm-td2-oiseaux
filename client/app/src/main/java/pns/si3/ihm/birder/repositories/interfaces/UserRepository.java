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
	 * Returns a user (updated in real time).
	 * @param id The id of the user.
	 * @return The user (updated in real time).
	 */
	LiveData<User> getUser(String id);

	/**
	 * Inserts a user.
	 * @param user The user to be inserted.
	 * @return The inserted user.
	 */
	LiveData<User> insertUser(User user);

	/**
	 * Returns the user errors (updated in real time).
	 * @return The user errors (updated in real time).
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the user errors.
	 * This avoids receiving the same error multiple times.
	 */
	void clearErrors();
}
