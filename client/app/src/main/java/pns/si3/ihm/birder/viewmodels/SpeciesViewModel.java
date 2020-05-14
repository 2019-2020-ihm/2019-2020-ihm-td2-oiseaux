package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.repositories.firebase.SpeciesRepositoryFirebase;
import pns.si3.ihm.birder.repositories.interfaces.SpeciesRepository;

/**
 * Species view model.
 *
 * Holds the data for species views.
 */
public class SpeciesViewModel extends ViewModel {
	/**
	 * The species repository.
	 */
	private SpeciesRepository speciesRepository;

	/**
	 * The selected species.
	 */
	private LiveData<Species> selectedReportLiveData;

	/**
	 * The species errors (updated in real time).
	 */
	private LiveData<Exception> speciesErrorsLiveData;

	/**
	 * Constructs a species view model.
	 */
	public SpeciesViewModel() {
		super();

		// Initialize the repositories.
		speciesRepository = new SpeciesRepositoryFirebase();

		// Initialize the live data.
		selectedReportLiveData = new MutableLiveData<>();
		speciesErrorsLiveData = speciesRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the selected species.
	 * @return The selected species.
	 */
	public LiveData<Species> getSelectedSpeciesLiveData() {
		return selectedReportLiveData;
	}

	/**
	 * Returns the species errors (updated in real time).
	 * @return The species errors (updated in real time).
	 */
	public LiveData<Exception> getSpeciesErrorsLiveData() {
		return speciesErrorsLiveData;
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Requests a species.
	 * @param id The id of the species.
	 */
	public void getSpecies(String id) {
		selectedReportLiveData = speciesRepository.getSpecies(id);
	}

	/**
	 * Clears the species errors.
	 * This avoids receiving the same error multiple times.
	 */
	public void clearReportErrors() {
		speciesRepository.clearErrors();
	}
}
