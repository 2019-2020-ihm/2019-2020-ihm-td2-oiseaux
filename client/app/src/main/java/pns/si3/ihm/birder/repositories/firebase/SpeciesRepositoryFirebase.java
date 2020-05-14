package pns.si3.ihm.birder.repositories.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import pns.si3.ihm.birder.exceptions.DocumentNotFoundException;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.repositories.interfaces.SpeciesRepository;

public class SpeciesRepositoryFirebase implements SpeciesRepository {
	/**
	 * The tag of the log messages.
	 */
	static final String TAG = "SpeciesRepository";

	/**
	 * The firebase firestore instance.
	 */
	private FirebaseFirestore firebaseFirestore;

	/**
	 * The species errors (updated in real time).
	 */
	private MutableLiveData<Exception> errorLiveData;

	/**
	 * Constructs a species repository.
	 */
	public SpeciesRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		errorLiveData = new MutableLiveData<>();
	}

	/**
	 * Returns a species.
	 * @param id The id of the species.
	 * @return The selected species.
	 */
	@Override
	public LiveData<Species> getSpecies(String id) {
		MutableLiveData<Species> speciesLiveData = new MutableLiveData<>();

		firebaseFirestore
			.collection("species")
			.document(id)
			.get()
			.addOnCompleteListener(
				speciesTask -> {
					if (speciesTask.isSuccessful()) {
						// Query succeeded.
						DocumentSnapshot speciesSnapshot = speciesTask.getResult();
						if (speciesSnapshot != null && speciesSnapshot.exists()) {
							// Species found.
							Species species = speciesSnapshot.toObject(Species.class);
							if (species != null) {
								species.setId(id);
								speciesLiveData.setValue(species);
							}
						} else {
							// Species not found.
							errorLiveData.setValue(new DocumentNotFoundException());
						}
					} else {
						// Query failed.
						errorLiveData.setValue(speciesTask.getException());
					}
				}
			);

		return speciesLiveData;
	}

	/**
	 * Returns the species errors (updated in real time).
	 * @return The species errors (updated in real time).
	 */
	@Override
	public LiveData<Exception> getErrors() {
		return errorLiveData;
	}

	/**
	 * Clears the species errors.
	 * This avoids receiving the same error multiple times.
	 */
	public void clearErrors() {
		errorLiveData.setValue(null);
	}
}
