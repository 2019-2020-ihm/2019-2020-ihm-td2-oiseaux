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
	 * Returns the list of bird reports from the database in real time.
	 * @return The live data of the reports.
	 */
	LiveData<List<Report>> getReports();

	/**
	 * Returns a bird report from the database in real time.
	 * @param id The id of the report.
	 * @return The live data of the report.
	 */
	LiveData<Report> getReport(String id);

	/**
	 * Creates a bird report.
	 * @param report The bird report.
	 * @return The live data of the created report.
	 */
	LiveData<Report> createReport(Report report);

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	LiveData<Exception> getErrors();

	/**
	 * Clears the live data of the report request errors.
	 * This avoid receiving the same error multiple times.
	 */
	void clearErrors();
}
