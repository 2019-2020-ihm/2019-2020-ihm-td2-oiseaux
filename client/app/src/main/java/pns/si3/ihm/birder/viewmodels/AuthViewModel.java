package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.repositories.interfaces.AuthRepository;
import pns.si3.ihm.birder.repositories.firebase.AuthRepositoryFirebase;
import pns.si3.ihm.birder.repositories.interfaces.UserRepository;
import pns.si3.ihm.birder.repositories.firebase.UserRepositoryFirebase;

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
	private LiveData<Exception> authenticationErrorsLiveData;

	/**
	 * The live data of the database user.
	 */
	private LiveData<User> databaseUserLiveData;

	/**
	 * The live data of the database errors.
	 */
	private LiveData<Exception> databaseErrorsLiveData;

	/**
	 * Constructs an authentication view model.
	 */
	public AuthViewModel() {
		super();

		// Initialize the repositories.
		authRepository = new AuthRepositoryFirebase();
		userRepository = new UserRepositoryFirebase();

		// Initialize the live data.
		authenticatedUserLiveData = new MutableLiveData<>();
		databaseUserLiveData = new MutableLiveData<>();
		authenticationErrorsLiveData = authRepository.getErrors();
		databaseErrorsLiveData = userRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the live data of the authenticated user.
	 * @return The live data of the authenticated user.
	 */
	public LiveData<User> getAuthenticatedUser() {
		return authenticatedUserLiveData;
	}

	/**
	 * Returns the live data of the authentication errors.
	 * @return The live data of the authentication errors.
	 */
	public LiveData<Exception> getAuthenticationErrors() {
		return authenticationErrorsLiveData;
	}

	/**
	 * Returns the live data of the user from the database.
	 * @return The live data of the user from the database.
	 */
	public LiveData<User> getDatabaseUser() {
		return databaseUserLiveData;
	}

	/**
	 * Returns the live data of the database errors.
	 * @return The live data of the database errors.
	 */
	public LiveData<Exception> getDatabaseErrors() {
		return databaseErrorsLiveData;
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
	 * Signs in the user using an email and a password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	public void signInWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.signInWithEmailAndPassword(email, password);
	}

	/**
	 * Creates a user using an email and a password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 */
	public void createUserWithEmailAndPassword(String email, String password) {
		authenticatedUserLiveData = authRepository.createUserWithEmailAndPassword(email, password);
	}

	/**
	 * Signs out the user.
	 */
	public void signOut() {
		authRepository.signOut();

		// Clear the live data.
		authenticatedUserLiveData = new MutableLiveData<>();
		databaseUserLiveData = new MutableLiveData<>();
	}

	/**
	 * Clears the authentication errors.
	 * This avoid receiving the same error twice.
	 */
	public void clearAuthenticationError() {
		authRepository.clearErrors();
	}

	/**
	 * Returns a user from the database in real time.
	 * @param id The id of the user.
	 */
	public void getUser(String id) {
		databaseUserLiveData = userRepository.getUser(id);
	}

	/**
	 * Creates a user in the database.
	 * @param user The user to be created.
	 */
	public void createUser(User user) {
		databaseUserLiveData = userRepository.createUser(user);
	}

	/**
	 * Clears the live data of the user errors.
	 * This avoid receiving the same error twice.
	 */
	public void clearDatabaseErrors() {
		userRepository.clearErrors();
	}
}
