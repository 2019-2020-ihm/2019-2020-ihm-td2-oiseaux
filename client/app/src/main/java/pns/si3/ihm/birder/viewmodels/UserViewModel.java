package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.StateData;
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
	 * Constructs a user view model.
	 */
	public UserViewModel() {
		super();

		// Initialize the repositories.
		userRepository = new UserRepositoryFirebase();
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Returns whether the user is authenticated, or not.
	 * @return Whether the user is authenticated, or not.
	 */
	public boolean isAuthenticated() {
		return userRepository.isAuthenticated();
	}

	/**
	 * Returns the id of the authenticated user.
	 * @return The id of the user, if the user is authenticated;
	 * <code>null</code> otherwise.
	 */
	public String getAuthenticationId() {
		return userRepository.getAuthenticationId();
	}

	/**
	 * Requests the sign in of a user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	public LiveData<StateData<User>> signIn(String email, String password) {
		return userRepository.signIn(email, password);
	}

	/**
	 * Requests the sign out of the user.
	 */
	public void signOut() {
		userRepository.signOut();
	}

	/**
	 * Requests a user.
	 * @param id The id of the user.
	 */
	public LiveData<StateData<User>> getUser(String id) {
		return userRepository.getUser(id);
	}

	/**
	 * Requests the authenticated user.
	 */
	public LiveData<StateData<User>> getUser() {
		return userRepository.getUser();
	}

	/**
	 * Requests the creation of a user.
	 * @param user The user to be created.
	 */
	public LiveData<StateData<User>> createUser(User user, String password) {
		return userRepository.createUser(user, password);
	}

	/**
	 * Requests the update of a user.
	 * @param user The user to be updated.
	 */
	public LiveData<StateData<User>> updateUser(User user) {
		return userRepository.updateUser(user);
	}

	/**
	 * Updates the password of the authenticated user.
	 * @param newPassword The new password of the user.
	 * @return Whether the password has been updated, or not.
	 */
	public LiveData<StateData<Void>> updatePassword(String newPassword) {
		return userRepository.updatePassword(newPassword);
	}

	/**
	 * Deletes the authenticated user.
	 * @return Whether the user has been deleted, or not.
	 */
	public LiveData<StateData<Void>> deleteUser() {
		return userRepository.deleteUser();
	}
}
