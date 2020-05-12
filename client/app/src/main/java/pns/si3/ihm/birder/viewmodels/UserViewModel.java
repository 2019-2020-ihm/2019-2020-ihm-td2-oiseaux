package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.repositories.interfaces.UserRepository;
import pns.si3.ihm.birder.repositories.firebase.UserRepositoryFirebase;

/**
 * User view model.
 *
 * Holds the data for the views that use the user data.
 */
public class UserViewModel extends ViewModel {
	/**
	 * The user repository.
	 */
	private UserRepository userRepository;

	/**
	 * The live data of the user.
	 */
	private LiveData<User> userLiveData;

	/**
	 * The live data of the user errors.
	 */
	private LiveData<Exception> userErrorsLiveData;

	/**
	 * Constructs a user view model.
	 */
	public UserViewModel() {
		super();

		// Initialize the repositories.
		userRepository = new UserRepositoryFirebase();

		// Initialize the live data.
		userLiveData = new MutableLiveData<>();
		userErrorsLiveData = userRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the live data of the user.
	 * @return The live data of the user.
	 */
	public LiveData<User> getUserLiveData() {
		return userLiveData;
	}

	/**
	 * Returns the live data of the user errors.
	 * @return The live data of the user errors.
	 */
	public LiveData<Exception> getUserErrorsLiveData() {
		return userErrorsLiveData;
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Gets a user from the database.
	 * @param id The id of the user.
	 */
	public void getUser(String id) {
		userLiveData = userRepository.getUser(id);
	}

	/**
	 * Sets a user in the database.
	 * @param user The user to be created.
	 */
	public void setUser(User user) {
		userLiveData = userRepository.setUser(user);
	}

	/**
	 * Clears the live data of the user errors.
	 * This avoid receiving the same error twice.
	 */
	public void clearUserErrors() {
		userRepository.clearErrors();
	}
}
