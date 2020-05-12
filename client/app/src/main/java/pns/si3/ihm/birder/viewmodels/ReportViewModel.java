package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.repositories.firebase.ReportRepositoryFirebase;
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
	 * The list of reports (updated in real time).
	 */
	private LiveData<List<Report>> reportsLiveData;


	/**
	 * The selected report (updated in real time).
	 */
	private LiveData<Report> selectedReportLiveData;

	/**
	 * The created report.
	 */
	private LiveData<Report> createdReportLiveData;


	/**
	 * The report errors (updated in real time).
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
		selectedReportLiveData = new MutableLiveData<>();
		createdReportLiveData = new MutableLiveData<>();
		reportErrorsLiveData = reportRepository.getErrors();

	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the list of reports.
	 * @return The list of reports.
	 */
	public LiveData<List<Report>> getReportsLiveData() {
		return reportsLiveData;
	}

	/**
	 * Returns the selected report.
	 * @return The selected report.
	 */
	public LiveData<Report> getSelectedReportLiveData() {
		return createdReportLiveData;
	}

	/**
	 * Returns the created report.
	 * @return The created report.
	 */
	public LiveData<Report> getCreatedReportLiveData() {
		return createdReportLiveData;
	}

	/**
	 * Returns the report errors.
	 * @return The report errors.
	 */
	public LiveData<Exception> getReportErrorsLiveData() {
		return reportErrorsLiveData;
	}

	/*====================================================================*/
	/*                               REQUESTS                             */
	/*====================================================================*/

	/**
	 * Requests a report.
	 * @param id The id of the report.
	 */
	public void getReport(String id) {
		selectedReportLiveData = reportRepository.getReport(id);
	}

	/**
	 * Requests the creation of a report.
	 */
	public void createReport(Report report) {
		createdReportLiveData = reportRepository.createReport(report);
	}

	/**
	 * Clears the report errors.
	 * This avoid receiving the same error twice.
	 */
	public void clearReportErrors() {
		reportRepository.clearErrors();
	}
}
