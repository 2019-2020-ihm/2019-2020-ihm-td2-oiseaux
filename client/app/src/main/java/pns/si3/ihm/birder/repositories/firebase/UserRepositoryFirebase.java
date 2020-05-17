package pns.si3.ihm.birder.repositories.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import pns.si3.ihm.birder.exceptions.DocumentNotFoundException;
import pns.si3.ihm.birder.exceptions.UserNotAuthenticatedException;
import pns.si3.ihm.birder.models.StateData;
import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.repositories.interfaces.UserRepository;

/**
 * User repository using Firebase.
 *
 * Implementation of the user repository using Firebase.
 */
public class UserRepositoryFirebase implements UserRepository {
	/**
	 * The tag of the log messages.
	 */
	private static final String TAG = "UserRepository";

	/**
	 * The firebase authentication instance.
	 */
	private FirebaseAuth firebaseAuth;

	/**
	 * The firebase firestore instance.
	 */
	private FirebaseFirestore firebaseFirestore;

	/**
	 * Constructs a user repository.
	 */
	public UserRepositoryFirebase() {
		firebaseAuth = FirebaseAuth.getInstance();
		firebaseFirestore = FirebaseFirestore.getInstance();
	}

	/*====================================================================*/
	/*                         AUTHENTICATION ONLY                        */
	/*====================================================================*/

	/**
	 * Returns whether the user is authenticated, or not.
	 * @return Whether the user is authenticated, or not.
	 */
	@Override
	public boolean isAuthenticated() {
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		return firebaseUser != null;
	}

	/**
	 * Returns the id of the authenticated user.
	 * @return The id of the user, if the user is authenticated;
	 * <code>null</code> otherwise.
	 */
	public String getAuthenticationId() {
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		return (firebaseUser != null) ? firebaseUser.getUid() : null;
	}

	/**
	 * Signs in the user (authentication only).
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return The id of the authenticated user.
	 */
	private LiveData<StateData<String>> signInAuthenticationOnly(String email, String password) {
		MutableLiveData<StateData<String>> userIdLiveData = new MutableLiveData<>();

		// Sign in the user.
		firebaseAuth
			.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(
				task -> {
					// Sign in succeeded.
					if (task.isSuccessful()) {
						String userId = getAuthenticationId();
						StateData<String> stateData = StateData.success(userId);
						userIdLiveData.setValue(stateData);
					}

					// Sign in failed.
					else {
						StateData<String> stateData = StateData.error(task.getException());
						userIdLiveData.setValue(stateData);
					}
				}
			);

		return userIdLiveData;
	}

	/**
	 * Creates a user (authentication only).
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return The id of the created user.
	 */
	private LiveData<StateData<String>> createUserAuthenticationOnly(String email, String password) {
		MutableLiveData<StateData<String>> userIdLiveData = new MutableLiveData<>();

		// Create the user.
		firebaseAuth
			.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(
				task -> {
					if (task.isSuccessful()) {
						// Creation succeeded.
						String userId = getAuthenticationId();
						StateData<String> stateData = StateData.success(userId);
						userIdLiveData.setValue(stateData);
					} else {
						// Creation failed.
						StateData<String> stateData = StateData.error(task.getException());
						userIdLiveData.setValue(stateData);
					}
				}
			);

		return userIdLiveData;
	}

	/**
	 * Updates the email of the authenticated user.
	 * @param email The email of the user.
	 * @return Whether the email has been updated, or not.
	 */
	private LiveData<StateData<Void>> updateEmail(String email) {
		MutableLiveData<StateData<Void>> emailUpdatedLiveData = new MutableLiveData<>();

		// The user is authenticated.
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		if (firebaseUser != null) {
			// Update the email.
			firebaseUser
				.updateEmail(email)
				.addOnCompleteListener(
					task -> {
						if (task.isSuccessful()) {
							// Email updated.
							StateData<Void> stateData = StateData.success();
							emailUpdatedLiveData.setValue(stateData);
						} else {
							// Email not updated.
							StateData<Void> stateData = StateData.error(task.getException());
							emailUpdatedLiveData.setValue(stateData);
						}
					}
				);
		} else {
			// User not authenticated.
			StateData<Void> stateData = StateData.error(new UserNotAuthenticatedException());
			emailUpdatedLiveData.setValue(stateData);
		}

		return emailUpdatedLiveData;
	}

	/**
	 * Updates the password of the authenticated user.
	 * @param newPassword The new password of the user.
	 * @return Whether the password has been updated, or not.
	 */
	public LiveData<StateData<Void>> updatePassword(String newPassword) {
		MutableLiveData<StateData<Void>> passwordUpdatedLiveData = new MutableLiveData<>();

		// The user is authenticated.
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		if (firebaseUser != null) {
			// Update the password.
			firebaseUser
				.updatePassword(newPassword)
				.addOnCompleteListener(
					task -> {
						if (task.isSuccessful()) {
							// Password updated.
							StateData<Void> stateData = StateData.success();
							passwordUpdatedLiveData.setValue(stateData);
						} else {
							// Password not updated.
							StateData<Void> stateData = StateData.error(task.getException());
							passwordUpdatedLiveData.setValue(stateData);
						}
					}
				);
		} else {
			// User not authenticated.
			StateData<Void> stateData = StateData.error(new UserNotAuthenticatedException());
			passwordUpdatedLiveData.setValue(stateData);
		}

		return passwordUpdatedLiveData;
	}

	/**
	 * Deletes the authenticated user.
	 * @return Whether the user has been deleted, or not.
	 */
	private LiveData<StateData<Void>> deleteUserAuthenticationOnly() {
		MutableLiveData<StateData<Void>> deletedUserLiveData = new MutableLiveData<>();

		// The user is authenticated.
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		if (firebaseUser != null) {
			// Update the password.
			firebaseUser
				.delete()
				.addOnCompleteListener(
					task -> {
						if (task.isSuccessful()) {
							// User deleted.
							StateData<Void> stateData = StateData.success();
							deletedUserLiveData.setValue(stateData);
						} else {
							// User not deleted.
							StateData<Void> stateData = StateData.error(task.getException());
							deletedUserLiveData.setValue(stateData);
						}
					}
				);

		} else {
			// User not authenticated.
			StateData<Void> stateData = StateData.error(new UserNotAuthenticatedException());
			deletedUserLiveData.setValue(stateData);
		}

		return deletedUserLiveData;
	}

	/*====================================================================*/
	/*                            DATABASE ONLY                           */
	/*====================================================================*/

	/**
	 * Returns a user by id (updated in real time).
	 * @param id The id of the user.
	 * @return The selected user (updated in real time).
	 */
	public LiveData<StateData<User>> getUser(String id) {
		MutableLiveData<StateData<User>> userLiveData = new MutableLiveData<>();

		// Get the user.
		firebaseFirestore
			.collection("users")
			.document(id)
			.addSnapshotListener(
				(snapshot, error) -> {
					if (error == null) {
						// Query succeeded.
						if (snapshot != null && snapshot.exists()) {
							// User found.
							User user = snapshot.toObject(User.class);
							if (user != null) {
								user.setId(id);
								StateData<User> stateData = StateData.success(user);
								userLiveData.setValue(stateData);
							}
						} else {
							// User not found.
							StateData<User> stateData = StateData.error(new DocumentNotFoundException());
							userLiveData.setValue(stateData);
						}
					} else {
						// Query failed.
						StateData<User> stateData = StateData.error(error);
						userLiveData.setValue(stateData);
					}
				}
			);

		return userLiveData;
	}

	/**
	 * Creates or updates a user (database only).
	 * @param user The user to be created.
	 * @return The created user.
	 */
	private LiveData<StateData<User>> insertUserDatabaseOnly(User user) {
		MutableLiveData<StateData<User>> createdUserLiveData = new MutableLiveData<>();

		firebaseFirestore
			.collection("users")
			.document(user.getId())
			.set(user)
			.addOnCompleteListener(
				task -> {
					if (task.isSuccessful()) {
						// Creation succeeded.
						StateData<User> stateData = StateData.success(user);
						createdUserLiveData.setValue(stateData);
					} else {
						// Creation failed.
						StateData<User> stateData = StateData.error(task.getException());
						createdUserLiveData.setValue(stateData);
					}
				}
			);

		return createdUserLiveData;
	}

	/**
	 * Deletes the authenticated user (database only).
	 * @param id The id of the user.
	 * @return Whether the user has been deleted, or not.
	 */
	private LiveData<StateData<Void>> deleteUserDatabaseOnly(String id) {
		MutableLiveData<StateData<Void>> userDeletedLiveData = new MutableLiveData<>();

		// Delete the user.
		firebaseFirestore
			.collection("users")
			.document(id)
			.delete()
			.addOnCompleteListener(
				task -> {
					if (task.isSuccessful()) {
						// User deleted.
						StateData<Void> stateData = StateData.success();
						userDeletedLiveData.setValue(stateData);
					} else {
						// User not deleted.
						StateData<Void> stateData = StateData.error(task.getException());
						userDeletedLiveData.setValue(stateData);
					}
				}
			);

		return userDeletedLiveData;
	}

	/*====================================================================*/
	/*                   BOTH AUTHENTICATION AND DATABASE                 */
	/*====================================================================*/


	/**
	 * Returns the authenticated user.
	 * @return The authenticated user.
	 */
	@Override
	public LiveData<StateData<User>> getUser() {
		String userId = getAuthenticationId();
		if (userId != null) {
			// Get the authenticated user.
			return getUser(userId);
		}

		// User not authenticated.
		StateData<User> stateData = StateData.error(new UserNotAuthenticatedException());
		return new MutableLiveData<>(stateData);
	}

	/**
	 * Signs in the user.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return The authenticated user (updated in real time).
	 */
	@Override
	public LiveData<StateData<User>> signIn(String email, String password) {
		return Transformations.switchMap(
			// Sign in the user.
			signInAuthenticationOnly(email, password),
			signInUser -> {
				if (signInUser.isSuccessful()) {
					// Get the user.
					String userId = signInUser.getData();
					return getUser(userId);
				}

				// User not authenticated.
				StateData<User> stateData = StateData.error(signInUser.getError());
				return new MutableLiveData<>(stateData);
			}
		);
	}

	/**
	 * Signs out the user.
	 */
	@Override
	public void signOut() {
		firebaseAuth.signOut();
	}

	/**
	 * Creates a user.
	 * @param user The user to be created.
	 * @return The created user.
	 */
	@Override
	public LiveData<StateData<User>> createUser(User user, String password) {
		return Transformations.switchMap(
			// Create the authentication user.
			createUserAuthenticationOnly(user.getEmail(), password),
			createUser -> {
				if (createUser.isSuccessful()) {
					// Create the database user.
					String userId = createUser.getData();
					user.setId(userId);
					return insertUserDatabaseOnly(user);
				}

				// Creation failed.
				StateData<User> stateData = StateData.error(createUser.getError());
				return new MutableLiveData<>(stateData);
			}
		);
	}

	/**
	 * Returns whether the email of the user has changed, or not.
	 * @param user The user to be checked.
	 * @return Whether the email of the user has changed, or not.
	 */
	private boolean emailChanged(User user) {
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		if (firebaseUser != null) {
			// User authenticated.
			String authEmail = firebaseUser.getEmail();
			String userEmail = user.getEmail();
			return (authEmail != null && !authEmail.equals(userEmail));
		}
		return false;
	}

	/**
	 * Updates the authenticated user.
	 * @param user The user to be updated.
	 * @return The updated user.
	 */
	@Override
	public LiveData<StateData<User>> updateUser(User user) {
		if (emailChanged(user)) {
			return Transformations.switchMap(
				// Update the authentication email.
				updateEmail(user.getEmail()),
				updateEmail -> {
					if (updateEmail.isSuccessful()) {
						// Update the database user.
						return insertUserDatabaseOnly(user);
					}

					// Email not updated.
					StateData<User> stateData = StateData.error(updateEmail.getError());
					return new MutableLiveData<>(stateData);
				}
			);
		} else {
			// Update the database user.
			return insertUserDatabaseOnly(user);
		}
	}

	/**
	 * Deletes the authenticated user.
	 * @return The deleted user.
	 */
	@Override
	public LiveData<StateData<Void>> deleteUser() {
		// Get the id of the authenticated user.
		String userId = getAuthenticationId();

		return Transformations.switchMap(
			// Delete the authentication user.
			deleteUserAuthenticationOnly(),
			deleteUser -> {
				if (deleteUser.isSuccessful()) {
					// Delete the database user.
					return deleteUserDatabaseOnly(userId);
				}

				// Deletion failed.
				return new MutableLiveData<>(deleteUser);
			}
		);
	}
}
