package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.repositories.firebase.AuthRepositoryFirebase;
import pns.si3.ihm.birder.repositories.firebase.ReportRepositoryFirebase;
import pns.si3.ihm.birder.repositories.firebase.UserRepositoryFirebase;
import pns.si3.ihm.birder.repositories.interfaces.ReportRepository;

/**
 * Report view model.
 *
 * Holds the data for report views.
 */
public class ReportViewModel extends ViewModel {
	/**
	 * The report repository.
	 */
	private ReportRepository reportRepository;

	/**
	 * Constructs a report view model.
	 */
	public ReportViewModel() {
		super();

		// Initialize the repositories.
		reportRepository = new ReportRepositoryFirebase();
	}

	/**
	 * Creates a bird report.
	 * @param report The bird report to be create.
	 * @return The live data of the create report.
	 */
	public LiveData<Report> createReport(Report report) {
		return reportRepository.createReport(report);
	}

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	public LiveData<Exception> getErrors() {
		return reportRepository.getErrors();
	}
}
