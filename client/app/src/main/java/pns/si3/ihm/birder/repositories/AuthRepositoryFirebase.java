package pns.si3.ihm.birder.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pns.si3.ihm.birder.models.User;

/**
 * Authentication repository using Firebase.
 *
 * Implementation of the auth repository using Firebase.
 */
public class AuthRepositoryFirebase implements AuthRepository {
	/**
	 * The tag of the log messages.
	 */
	static final String TAG = "AuthRepository";

	/**
	 * The firebase authentication instance.
	 */
	private FirebaseAuth firebaseAuth;

	/**
	 * The live data of the authentication errors.
	 */
	private MutableLiveData<Exception> exceptionLiveData;

	/**
	 * Constructs an authentication repository.
	 */
	public AuthRepositoryFirebase() {
		firebaseAuth = FirebaseAuth.getInstance();
		exceptionLiveData = new MutableLiveData<>();
	}

	/**
	 * Checks if the user is authenticated.
	 * @return Whether the user is authenticated, or not.
	 */
	public boolean isAuthenticated() {
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		return firebaseUser != null;
	}

	/**
	 * Signs in a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the authenticated user.
	 */
	public MutableLiveData<User> signInWithEmailAndPassword(String email, String password) {
		final MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Sign in with Firebase Auth.
		firebaseAuth
			.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(
				authTask -> {
					if (authTask.isSuccessful()) {
						// Sign in succeeded.
						FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
						if (firebaseUser != null) {
							User user = new User(
								firebaseUser.getUid(),
								null,
								null,
								firebaseUser.getEmail()
							);
							userLiveData.setValue(user);
						}
					} else {
						// Sign in failed.
						exceptionLiveData.setValue(authTask.getException());
					}
				}
			);

		return userLiveData;
	}

	/**
	 * Signs up a user with an email and password.
	 * @param email The email of the user.
	 * @param password The password of the user.
	 * @return A live data of the created user.
	 */
	public MutableLiveData<User> createUserWithEmailAndPassword(String email, String password) {
		final MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Sign up with firebase.
		firebaseAuth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(
				task -> {
					if (task.isSuccessful()) {
						// Sign up succeeded.
						FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
						if (firebaseUser != null) {
							User user = new User(
								firebaseUser.getUid(),
								null,
								null,
								firebaseUser.getEmail()
							);
							userLiveData.setValue(user);
						}
					} else {
						// Sign up failed.
						exceptionLiveData.setValue(task.getException());
					}
				})
			;

		return userLiveData;
	}

	/**
	 * Returns the live data of the authentication errors.
	 * @return The live data of the authentication errors.
	 */
	public MutableLiveData<Exception> getErrors() {
		return exceptionLiveData;
	};
}
