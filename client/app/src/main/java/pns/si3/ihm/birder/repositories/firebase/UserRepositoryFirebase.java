package pns.si3.ihm.birder.repositories.firebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import pns.si3.ihm.birder.exceptions.DocumentNotFoundException;
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
	 * The firebase firestore instance.
	 */
	private FirebaseFirestore firebaseFirestore;

	/**
	 * The live data of the user request errors.
	 */
	private MutableLiveData<Exception> exceptionLiveData;

	/**
	 * Constructs a user repository.
	 */
	public UserRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		exceptionLiveData = new MutableLiveData<>();
	}

	/**
	 * Gets a user from the database.
	 * @param id The id of the user.
	 * @return The live data of the user.
	 */
	public MutableLiveData<User> getUser(String id) {
		MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Get the user.
		firebaseFirestore
			.collection("users")
			.document(id)
			.get()
			.addOnCompleteListener(
				userTask -> {
					if (userTask.isSuccessful()) {
						// Query succeeded.
						User user = userTask.getResult().toObject(User.class);
						if (user != null) {
							userLiveData.setValue(user);
						} else {
							// User not found.
							exceptionLiveData.setValue(new DocumentNotFoundException());
						}
					} else {
						// Query failed.
						exceptionLiveData.setValue(userTask.getException());
					}
				}
			);

		return userLiveData;
	}

	/**
	 * Creates a user in the database.
	 * @param user The user to be created.
	 * @return The live data of the created user.
	 */
	public MutableLiveData<User> createUser(User user) {
		MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Create the user.
		firebaseFirestore
			.collection("users")
			.document(user.id)
			.set(user)
			.addOnCompleteListener(
				aVoid -> {
					// Query succeeded.
					userLiveData.setValue(user);
				}
			);

		return userLiveData;
	}

	/**
	 * Returns the live data of the user request errors.
	 * @return The live data of the user request errors.
	 */
	public MutableLiveData<Exception> getErrors() {
		return exceptionLiveData;
	}
}
