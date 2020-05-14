package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.LiveData;

import java.util.List;

import pns.si3.ihm.birder.models.Species;

/**
 * Species repository.
 *
 * Manages the bird species in the database.
 */
public interface SpeciesRepository {
	/**
	 * Returns a species.
	 * @param id The id of the species.
	 * @return The selected species.
	 */
	LiveData<Species> getSpecies(String id);

	/**
	 * Returns the species errors (updated in real time).
	 * @return The species errors (updated in real time).
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the species errors.
	 * This avoids receiving the same error multiple times.
	 */
	void clearErrors();
}
