package pns.si3.ihm.birder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
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
	 * The updated report.
	 */
	private LiveData<Report> updatedReportLiveData;

	/**
	 * The deleted report.
	 */
	private LiveData<Report> deletedReportLiveData;

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
		updatedReportLiveData = new MutableLiveData<>();
		deletedReportLiveData = new MutableLiveData<>();
		reportErrorsLiveData = reportRepository.getErrors();
	}

	/*====================================================================*/
	/*                              LIVE DATA                             */
	/*====================================================================*/

	/**
	 * Returns the list of reports (updated in real time).
	 * @return The list of reports (updated in real time).
	 */
	public LiveData<List<Report>> getReportsLiveData() {
		return reportsLiveData;
	}

	/**
	 * Returns the selected report (updated in real time).
	 * @return The selected report (updated in real time).
	 */
	public LiveData<Report> getSelectedReportLiveData() {
		return selectedReportLiveData;
	}

	/**
	 * Returns the created report.
	 * @return The created report.
	 */
	public LiveData<Report> getCreatedReportLiveData() {
		return createdReportLiveData;
	}

	/**
	 * Returns the updated report.
	 * @return The updated report.
	 */
	public LiveData<Report> getUpdatedReportLiveData() {
		return updatedReportLiveData;
	}

	/**
	 * Returns the deleted report.
	 * @return The deleted report.
	 */
	public LiveData<Report> getDeletedReportLiveData() {
		return deletedReportLiveData;
	}

	/**
	 * Returns the report errors (updated in real time).
	 * @return The report errors (updated in real time).
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
	 * @param report The report to be created.
	 */
	public void createReport(Report report) {
		createdReportLiveData = reportRepository.createReport(report);
	}

	/**
	 * Requests the update of a report.
	 * @param report The report to be updated.
	 */
	public void updateReport(Report report) {
		updatedReportLiveData = reportRepository.updateReport(report);
	}

	/**
	 * Requests the deletion of a report.
	 * @param report The report to be deleted.
	 */
	public void deleteReport(Report report) {
		deletedReportLiveData = reportRepository.deleteReport(report);
	}

	/**
	 * Clears the report errors.
	 * This avoids receiving the same error twice.
	 */
	public void clearReportErrors() {
		reportRepository.clearErrors();
	}
}
