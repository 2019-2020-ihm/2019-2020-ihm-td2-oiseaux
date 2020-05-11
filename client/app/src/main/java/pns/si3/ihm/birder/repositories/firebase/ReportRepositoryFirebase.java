package pns.si3.ihm.birder.repositories.firebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import pns.si3.ihm.birder.exceptions.DocumentNotCreatedException;
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
	 * The live data of the report request errors.
	 */
	private MutableLiveData<Exception> exceptionLiveData;

	/**
	 * Constructs a report repository.
	 */
	public ReportRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		exceptionLiveData = new MutableLiveData<>();
	}

	/**
	 * Gets a bird report.
	 * @param id The id of the report.
	 * @return The live data of the created report.
	 */
	public MutableLiveData<Report> getReport(String id) {
		MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

		// Get the report.
		firebaseFirestore
			.collection("report")
			.document(id)
			.get()
			.addOnCompleteListener(
				reportTask -> {
					if (reportTask.isSuccessful()) {
						// Query succeeded.
						Report report = reportTask.getResult().toObject(Report.class);
						if (report != null) {
							// Report found.
							reportLiveData.setValue(report);
						} else {
							// Report not found.
							exceptionLiveData.setValue(reportTask.getException());
						}

					} else {
						// Query failed.
						exceptionLiveData.setValue(reportTask.getException());
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
	public MutableLiveData<Report> createReport(Report report) {
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
							exceptionLiveData.setValue(new DocumentNotCreatedException());
						}
					} else {
						// Query failed.
						exceptionLiveData.setValue(reportTask.getException());
					}
				}
			);

		return reportLiveData;
	}

	/**
	 * Returns the live data of the report request errors.
	 * @return The live data of the report request errors.
	 */
	public MutableLiveData<Exception> getErrors() {
		return exceptionLiveData;
	}
}
