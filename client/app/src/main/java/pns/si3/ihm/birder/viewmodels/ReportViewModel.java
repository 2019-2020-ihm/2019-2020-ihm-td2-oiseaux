package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

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
	 * The live data of the reports.
	 */
	private LiveData<List<Report>> reportsLiveData;

	/**
	 * The live data of the report request errors.
	 */
	private LiveData<Exception> reportErrorsLiveData;

	/**
	 * Constructs a report view model.
	 */
	public ReportViewModel() {
		super();

		// Initialize the repositories.
		reportRepository = new ReportRepositoryFirebase();

		// Initialize the live data.
		reportsLiveData = reportRepository.getReports();
		reportErrorsLiveData = reportRepository.getErrors();
	}

	/**
	 * Returns the list of bird reports from the database in real time.
	 * @return The live data of the reports.
	 */
	public LiveData<List<Report>> getReports() {
		return reportsLiveData;
	}

	/**
	 *  Returns a bird report from the database in real time.
	 * @param id The id of the bird report.
	 * @return The live data of the report.
	 */
	public LiveData<Report> getReport(String id) {
		return reportRepository.getReport(id);
	}

	/**
	 * Creates a bird report.
	 * @param report The bird report to be create.
	 * @return The live data of the created report.
	 */
	public LiveData<Report> createReport(Report report) {
		return reportRepository.createReport(report);
	}

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	public LiveData<Exception> getErrors() {
		return reportErrorsLiveData;
	}
}
