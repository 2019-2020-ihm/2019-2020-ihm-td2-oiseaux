package pns.si3.ihm.birder.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.repositories.AuthRepository;
import pns.si3.ihm.birder.repositories.AuthRepositoryFirebase;
import pns.si3.ihm.birder.repositories.UserRepository;
import pns.si3.ihm.birder.repositories.UserRepositoryFirebase;

/**
 * Authentication view model.
 *
 * Holds the data for the authentication views.
 */
public class AuthViewModel extends ViewModel {
	/**
	 * The authentication repository.
	 */
	private AuthRepository authRepository;

	/**
	 * The user repository.
	 */
	private UserRepository userRepository;

	/**
	 * The live data of the authenticated user.
	 */
	private LiveData<User> authenticatedUserLiveData;

	/**
	 * The live data of the authentication errors.
	 */
	private LiveData<Exception> authenticationExceptionLiveData;

	/**
	 * The live data of the database user.
	 */
	private LiveData<User> databaseUserLiveData;

	public AuthViewModel() {
		super();

		// Initialize the repositories.
		authRepository = new AuthRepositoryFirebase();
		userRepository = new UserRepositoryFirebase();
	}

	public boolean isAuthenticated() {
		return authRepository.isAuthenticated();
	}

	public LiveData<User> getAuthenticatedUser() {
		return authenticatedUserLiveData;
	}

	public LiveData<Exception> getAuthenticationErrors() {
		return authRepository.getErrors();
	}

	public void clearAuthenticationError() {
		authRepository.getErrors().setValue(null);
	}

	public void signInWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.signInWithEmailAndPassword(email, password);
	}

	public void createUserWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.createUserWithEmailAndPassword(email, password);
	}

	public LiveData<User> getDatabaseUser() {
		return databaseUserLiveData;
	}

	public LiveData<Exception> getDatabaseErrors() {
		return userRepository.getErrors();
	}

	public void clearDatabaseError() {
		userRepository.getErrors().setValue(null);
	}

	public void getUserFromDatabase(String id) {
		databaseUserLiveData = userRepository.getUser(id);
	}

	public void createUserInDatabase(User user) {
		databaseUserLiveData = userRepository.createUser(user);
	}
}
