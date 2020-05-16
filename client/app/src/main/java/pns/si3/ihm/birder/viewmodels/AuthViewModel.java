package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.repositories.firebase.AuthRepositoryFirebase;
import pns.si3.ihm.birder.repositories.interfaces.AuthRepository;

/**
 * Authentication view model.
 *
 * Holds the data for the views that use the authentication.
 */
public class AuthViewModel extends ViewModel {
	/**
	 * The authentication repository.
	 */
	private AuthRepository authRepository;

	/**
	 * The authenticated user.
	 */
	private LiveData<User> authenticatedUserLiveData;

	/**
	 * Whether the password of the user has been updated, or not.
	 */
	private LiveData<Boolean> passwordUpdatedLiveData;

	/**
	 * Whether the user has been deleted, or not.
	 */
	private LiveData<Boolean> userDeletedLiveData;

	/**
	 * The authentication errors (updated in real time).
	 */
	private LiveData<Exception> authenticationErrorsLiveData;

	/**
	 * Constructs an authentication view model.
	 */
	public AuthViewModel() {
		super();

		// Initialize the repositories.
		authRepository = new AuthRepositoryFirebase();

		// Initialize the live data.
		authenticatedUserLiveData = new MutableLiveData<>();
		passwordUpdatedLiveData = new MutableLiveData<>();
		userDeletedLiveData = new MutableLiveData<>();
		authenticationErrorsLiveData = authRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the authenticated user.
	 * @return The authenticated user.
	 */
	public LiveData<User> getAuthenticatedUserLiveData() {
		return authenticatedUserLiveData;
	}

	/**
	 * Returns whether the password of the user has been updated, or not.
	 * @return Whether the password of the user has been updated, or not.
	 */
	public LiveData<Boolean> getPasswordUpdatedLiveData() {
		return passwordUpdatedLiveData;
	}

	/**
	 * Returns whether the user has been deleted, or not.
	 * @return Whether the user has been deleted, or not.
	 */
	public LiveData<Boolean> getUserDeletedLiveData() {
		return userDeletedLiveData;
	}

	/**
	 * Returns the authentication errors (updated in real time).
	 * @return The authentication errors (updated in real time).
	 */
	public LiveData<Exception> getAuthenticationErrorsLiveData() {
		return authenticationErrorsLiveData;
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Checks if the user is authenticated.
	 * @return Whether the user is authenticated, or not.
	 */
	public boolean isAuthenticated() {
		return authRepository.isAuthenticated();
	}

	/**
	 * Returns the id of the authenticated user.
	 * @return The id of the user, if he's authenticated, null otherwise.
	 */
	public String getAuthenticationId() {
		return authRepository.getAuthenticationId();
	}

	/**
	 * Request the authentication of a user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	public void signInWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.signInWithEmailAndPassword(email, password);
	}

	/**
	 * Request the creation of a user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	public void createUserWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.createUserWithEmailAndPassword(email, password);
	}

	/**
	 * Requests the password update of the authenticated user.
	 * @param newPassword The new password of the user.
	 */
	public void updatePassword(String newPassword) {
		passwordUpdatedLiveData = authRepository.updatePassword(newPassword);
	}

	/**
	 * Requests the deletion of the authenticated user.
	 */
	public void deleteUser() {
		userDeletedLiveData = authRepository.deleteUser();
	}

	/**
	 * Signs out the user.
	 */
	public void signOut() {
		authRepository.signOut();

		// Clear the live data.
		authenticatedUserLiveData = new MutableLiveData<>();
	}

	/**
	 * Clears the authentication errors.
	 * This avoids receiving the same error twice.
	 */
	public void clearAuthenticationError() {
		authRepository.clearErrors();
	}
}
