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
	 * The selected user (updated in real time).
	 */
	private LiveData<User> selectedUserLiveData;

	/**
	 * The inserted user.
	 */
	private LiveData<User> insertedUserLiveData;

	/**
	 * The deleted user.
	 */
	private LiveData<User> deletedUserLiveData;

	/**
	 * The user errors.
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
		selectedUserLiveData = new MutableLiveData<>();
		insertedUserLiveData = new MutableLiveData<>();
		deletedUserLiveData = new MutableLiveData<>();
		userErrorsLiveData = userRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the selected user (updated in real time).
	 * @return The selected user (updated in real time).
	 */
	public LiveData<User> getSelectedUserLiveData() {
		return selectedUserLiveData;
	}

	/**
	 * Returns the inserted user.
	 * @return The inserted user.
	 */
	public LiveData<User> getInsertedUserLiveData() {
		return insertedUserLiveData;
	}

	/**
	 * Returns the deleted user.
	 * @return The deleted user.
	 */
	public LiveData<User> getDeletedUserLiveData() {
		return deletedUserLiveData;
	}

	/**
	 * Returns the user errors.
	 * @return The user errors.
	 */
	public LiveData<Exception> getUserErrorsLiveData() {
		return userErrorsLiveData;
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Requests a user.
	 * @param id The id of the user.
	 */
	public void getUser(String id) {
		selectedUserLiveData = userRepository.getUser(id);
	}

	/**
	 * Requests the insertion of a user.
	 * @param user The user to be created.
	 */
	public void insertUser(User user) {
		insertedUserLiveData = userRepository.insertUser(user);
	}

	/**
	 * Requests the deletion of a user.
	 * @param user The user to be deleted.
	 */
	public void deleteUser(User user) {
		deletedUserLiveData = userRepository.deleteUser(user);
	}

	/**
	 * Clears the user errors.
	 * This avoids receiving the same error twice.
	 */
	public void clearUserErrors() {
		userRepository.clearErrors();
	}
}
