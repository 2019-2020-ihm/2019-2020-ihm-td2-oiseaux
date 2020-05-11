package pns.si3.ihm.birder.repositories.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import pns.si3.ihm.birder.exceptions.DocumentNotCreatedException;
import pns.si3.ihm.birder.exceptions.DocumentNotFoundException;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.repositories.interfaces.ReportRepository;

public class ReportRepositoryFirebase implements ReportRepository {
	/**
	 * The tag of the log messages.
	 */
	static final String TAG = "ReportRepository";

	/**
	 * The firebase firestore instance.
	 */
	private FirebaseFirestore firebaseFirestore;

	/**
	 * The live data of the reports.
	 */
	private MutableLiveData<List<Report>> reportsLiveData;

	/**
	 * The live data of the report request errors.
	 */
	private MutableLiveData<Exception> errorLiveData;

	/**
	 * Constructs a report repository.
	 */
	public ReportRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		errorLiveData = new MutableLiveData<>();
		reportsLiveData = new MutableLiveData<>();
		loadReports();
	}

	/**
	 * Loads the reports from the database in real time.
	 */
	private void loadReports() {
		firebaseFirestore
			.collection("reports")
			.addSnapshotListener(
				(reportsSnapshot, error) -> {
					if (error == null) {
						// Query succeeded.
						if (reportsSnapshot != null) {
							// Reports found.
							List<Report> reports = new ArrayList<>();
							for (QueryDocumentSnapshot reportSnapshot : reportsSnapshot) {
								Report report = reportSnapshot.toObject(Report.class);
								reports.add(report);
							}

							// Update the reports live data.
							Log.e(TAG, "UPDATED!!!!");
							reportsLiveData.setValue(reports);
						} else {
							// Reports not found.
							errorLiveData.setValue(new DocumentNotFoundException());
						}
					} else {
						// Query failed.
						errorLiveData.setValue(error);
					}
				}
			);
	}

	/**
	 * Returns the list of bird reports from the database in real time.
	 * @return The live data of the reports.
	 */
	public LiveData<List<Report>> getReports() {
		return reportsLiveData;
	}

	/**
	 * Returns a bird report from the database in real time.
	 * @param id The id of the report.
	 * @return The live data of the report.
	 */
	public LiveData<Report> getReport(String id) {
		MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

		// Get the report.
		firebaseFirestore
			.collection("reports")
			.document(id)
			.addSnapshotListener(
				(reportSnapshot, error) -> {
					if (error == null) {
						// Query succeeded.
						Report report = reportSnapshot.toObject(Report.class);
						if (report != null) {
							// Report found.
							reportLiveData.setValue(report);
						} else {
							// Report not found.
							errorLiveData.setValue(new DocumentNotFoundException());
						}
					} else {
						// Query failed.
						errorLiveData.setValue(error);
					}
				}
			);

		return reportLiveData;
	}

	/**
	 * Creates a bird report.
	 * @param report The bird report.
	 * @return The live data of the created report.
	 */
	public LiveData<Report> createReport(Report report) {
		MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

		// Create the report.
		firebaseFirestore
			.collection("reports")
			.add(report)
			.addOnCompleteListener(
				reportTask -> {
					if (reportTask.isSuccessful()) {
						// Query succeeded.
						DocumentReference reference = reportTask.getResult();
						if (reference != null) {
							// Report created.
							report.id = reference.getId();
							reportLiveData.setValue(report);
						} else {
							// Report not created.
							errorLiveData.setValue(new DocumentNotCreatedException());
						}
					} else {
						// Query failed.
						errorLiveData.setValue(reportTask.getException());
					}
				}
			);

		return reportLiveData;
	}

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	public LiveData<Exception> getErrors() {
		return errorLiveData;
	}

	/**
	 * Clears the live data of the report request errors.
	 * This avoid receiving the same error multiple times.
	 */
	public void clearErrors() {
		errorLiveData.setValue(null);
	};
}
