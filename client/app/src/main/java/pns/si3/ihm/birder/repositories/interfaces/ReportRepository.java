package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.LiveData;

import java.util.List;

import pns.si3.ihm.birder.models.Report;

/**
 * Report repository.
 *
 * Manages the bird reports in the database.
 */
public interface ReportRepository {
	/**
	 * Returns the list of reports (updated in real time).
	 * @return The list of reports (updated in real time).
	 */
	LiveData<List<Report>> getReports();

	/**
	 * Returns a report (updated in real time).
	 * @param id The id of the report.
	 * @return The report (updated in real time).
	 */
	LiveData<Report> getReport(String id);

	/**
	 * Creates a bird report.
	 * @param report The bird report.
	 * @return The created report.
	 */
	LiveData<Report> createReport(Report report);

	/**
	 * Returns the report errors.
	 * @return The report errors.
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the report errors (updated in real time).
	 * This avoids receiving the same error multiple times.
	 */
	void clearErrors();
}
