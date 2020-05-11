package pns.si3.ihm.birder.repositories.interfaces;

import androidx.lifecycle.MutableLiveData;

import pns.si3.ihm.birder.models.Report;

/**
 * Report repository.
 *
 * Manages the bird reports in the database.
 */
public interface ReportRepository {
	/**
	 * Creates a bird report.
	 * @param report The bird report.
	 * @return The live data of the created report.
	 */
	MutableLiveData<Report> createReport(Report report);

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	MutableLiveData<Exception> getErrors();
}
