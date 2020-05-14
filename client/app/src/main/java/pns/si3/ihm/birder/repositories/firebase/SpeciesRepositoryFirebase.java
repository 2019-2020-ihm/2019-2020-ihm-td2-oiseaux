package pns.si3.ihm.birder.repositories.firebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
	 * The firebase authentication instance.
	 */
	private FirebaseAuth firebaseAuth;

	/**
	 * The algolia client instance.
	 */
	private Client algoliaClient;

	/**
	 * The algolia species index.
	 * This is used to make searches.
	 */
	private Index algoliaIndex;

	/**
	 * The species errors (updated in real time).
	 */
	private MutableLiveData<Exception> errorLiveData;

	/**
	 * Constructs a species repository.
	 */
	public SpeciesRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		firebaseAuth = FirebaseAuth.getInstance();
		algoliaClient = new Client("AH93SYAOZE", "5b67feb8945d27389980251e9c6f4d65");
		algoliaIndex = algoliaClient.getIndex("species");
		errorLiveData = new MutableLiveData<>();
	}

	/**
	 * Returns the id of the authenticated user.
	 * @return The id of the user, if he's authenticated, "anonymous" otherwise.
	 */
	private String getAuthenticationId() {
		FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
		if (firebaseUser != null) {
			return firebaseUser.getUid();
		}
		return "anonymous";
	}

	/**
	 * Searches for species ids based on a text query.
	 * @param text The text query.
	 * @return The list of found species ids.
	 */
	private LiveData<List<String>> searchSpeciesIds(String text) {
		MutableLiveData<List<String>> foundSpeciesLiveData = new MutableLiveData<>();

		// Set the request options for analytics.
		String userId = getAuthenticationId();
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setHeader("X-Algolia-UserToken", userId);

		// Create the query.
		Query query = new Query(text)
			.setAttributesToRetrieve("objectID")
			.setHitsPerPage(5);

		// Search the species.
		algoliaIndex.searchAsync(
			query,
			requestOptions,
			(content, error) -> {
				if (error == null) {
					// Search succeeded.
					try {
						if (content != null) {
							// Species found.
							List<String> foundSpecies = new ArrayList<>();
							JSONArray hits = content.getJSONArray("hits");
							for (int i = 0; i < hits.length(); i++) {
								JSONObject hit = hits.getJSONObject(i);
								String id = (String) hit.get("objectID");
								foundSpecies.add(id);
							}
							foundSpeciesLiveData.setValue(foundSpecies);
						}
					} catch (Exception otherError) {
						// JSON deserialize failed.
						errorLiveData.setValue(otherError);
					}
				} else {
					// Search failed.
					errorLiveData.setValue(error);
				}
			}
		);

		return foundSpeciesLiveData;
	}

	public LiveData<List<Species>> searchSpecies(String text) {
		// Search species ids.
		return Transformations.switchMap(
			searchSpeciesIds(text),
			speciesIds -> {
				MutableLiveData<List<Species>> speciesLiveData = new MutableLiveData<>();

				// Get species from list of ids.
				firebaseFirestore
					.collection("species")
					.whereIn(FieldPath.documentId(), speciesIds)
					.get()
					.addOnCompleteListener(
						speciesTask -> {
							if (speciesTask.isSuccessful()) {
								// Query succeeded.
								QuerySnapshot foundSpeciesSnapshot = speciesTask.getResult();
								if (foundSpeciesSnapshot != null) {
									// Species found.
									List<Species> foundSpecies = new ArrayList<>();
									for (QueryDocumentSnapshot speciesSnapshot : foundSpeciesSnapshot) {
										Species species = speciesSnapshot.toObject(Species.class);
										species.setId(speciesSnapshot.getId());
										foundSpecies.add(species);
									}

									// Update the species live data.
									speciesLiveData.setValue(foundSpecies);
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
		);
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
