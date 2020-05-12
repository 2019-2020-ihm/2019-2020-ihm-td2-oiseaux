package pns.si3.ihm.birder.repositories.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
	private MutableLiveData<Exception> errorLiveData;

	/**
	 * Constructs a user repository.
	 */
	public UserRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		errorLiveData = new MutableLiveData<>();
	}

	/**
	 * Gets a user from the database in real time.
	 * @param id The id of the user.
	 * @return The live data of the user.
	 */
	public LiveData<User> getUser(String id) {
		MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Get the user.
		firebaseFirestore
			.collection("users")
			.document(id)
			.addSnapshotListener(
				(userSnapshot, error) -> {
					if (error == null) {
						// Query succeeded.
						if (userSnapshot != null) {
							// User found.
							User user = userSnapshot.toObject(User.class);
							if (user != null) {
								user.setId(id);
								userLiveData.setValue(user);
							}
						} else {
							// User not found.
							errorLiveData.setValue(new DocumentNotFoundException());
						}
					} else {
						// Query failed.
						errorLiveData.setValue(error);
					}
				}
			);

		return userLiveData;
	}

	/**
	 * Sets a user in the database.
	 * @param user The user to be created.
	 * @return The created user.
	 */
	public LiveData<User> insertUser(User user) {
		MutableLiveData<User> userLiveData = new MutableLiveData<>();

		// Create the user.
		firebaseFirestore
			.collection("users")
			.document(user.getId())
			.set(user)
			.addOnCompleteListener(
				userTask -> {
					if (userTask.isSuccessful()) {
						// Query succeeded.
						userLiveData.setValue(user);
					} else {
						// Query failed.
						errorLiveData.setValue(userTask.getException());
					}
				}
			);

		return userLiveData;
	}

	/**
	 * Returns the user request errors.
	 * @return The user request errors.
	 */
	public LiveData<Exception> getErrors() {
		return errorLiveData;
	}

	/**
	 * Clears the user request errors.
	 * This avoid receiving the same error multiple times.
	 */
	public void clearErrors() {
		errorLiveData.setValue(null);
	};
}
