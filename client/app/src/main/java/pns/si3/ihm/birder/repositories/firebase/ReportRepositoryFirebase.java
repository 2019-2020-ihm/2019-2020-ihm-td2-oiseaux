package pns.si3.ihm.birder.repositories.firebase;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
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
	 * The firebase storage instance.
	 */
	private FirebaseStorage firebaseStorage;

	/**
	 * The list of reports (updated in real time).
	 */
	private MutableLiveData<List<Report>> reportsLiveData;

	/**
	 * The report errors (updated in real time).
	 */
	private MutableLiveData<Exception> errorLiveData;

	/**
	 * Constructs a report repository.
	 */
	public ReportRepositoryFirebase() {
		firebaseFirestore = FirebaseFirestore.getInstance();
		firebaseStorage = FirebaseStorage.getInstance();
		errorLiveData = new MutableLiveData<>();
		reportsLiveData = new MutableLiveData<>();
		loadReports();
	}

	/**
	 * Loads the reports (updated in real time).
	 */
	private void loadReports() {
		firebaseFirestore
			.collection("reports")
			.orderBy("date", Query.Direction.DESCENDING)
			.addSnapshotListener(
				(reportsSnapshot, error) -> {
					if (error == null) {
						// Query succeeded.
						if (reportsSnapshot != null) {
							// Reports found.
							List<Report> reports = new ArrayList<>();
							for (QueryDocumentSnapshot reportSnapshot : reportsSnapshot) {
								Report report = reportSnapshot.toObject(Report.class);
								report.setId(reportSnapshot.getId());
								reports.add(report);
							}

							// Update the reports live data.
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
	 * Returns the list of reports (updated in real time).
	 * @return The list of reports (updated in real time).
	 */
	public LiveData<List<Report>> getReports() {
		return reportsLiveData;
	}

	/**
	 * Returns a report (updated in real time).
	 * @param id The id of the report.
	 * @return The report (updated in real time).
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
						if (reportSnapshot != null) {
							// Report found.
							Report report = reportSnapshot.toObject(Report.class);
							if (report != null) {
								report.setId(id);
								reportLiveData.setValue(report);
							}
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
	 * Creates a report.
	 * @param report The bird report.
	 * @return The created report.
	 */
	@Override
	public LiveData<Report> createReport(Report report) {
		Uri pictureUri = report.getPictureUri();
		if (pictureUri != null) {
			// Generate the picture name.
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
			String timestamp = dateFormat.format(now);
			String pictureName = report.getUserId() + "_" + timestamp;

			// Store the picture.
			return Transformations.switchMap(
				storePicture(pictureName, pictureUri),
				picturePath -> {
					// Store the report.
					report.setPicturePath(picturePath);
					return storeReport(report);
				}
			);
		} else {
			// Store the report.
			return storeReport(report);
		}
	}

	/**
	 * Stores a report.
	 * @param report The report to be created.
	 * @return The created report.
	 */
	private LiveData<Report> storeReport(Report report) {
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
							report.setId(reference.getId());
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
	 * Get the path of the report picture.
	 * @param name The name of the report picture.
	 * @return The path of the report picture.
	 */
	private String getPicturePath(String name) {
		return "reports/images/" + name;
	}

	/**
	 * Stores a report picture.
	 * @param name The name of the report picture.
	 * @param uri The URI of the report picture.
	 * @return The path of the report picture.
	 */
	private LiveData<String> storePicture(String name, Uri uri) {
		MutableLiveData<String> picturePathLiveData = new MutableLiveData<>();

		// Get the picture path.
		String picturePath = getPicturePath(name);

		// Store the picture.
		StorageReference reference = firebaseStorage.getReference(picturePath);
		reference
			.putFile(uri)
			.addOnCompleteListener(
				uploadTask -> {
					if (uploadTask.isSuccessful()) {
						// Storage succeeded.
						picturePathLiveData.setValue(picturePath);
					} else {
						// Storage failed.
						errorLiveData.setValue(uploadTask.getException());
					}
				}
			);

		return picturePathLiveData;
	}

	/**
	 * Updates a report.
	 * @param report The report to be updated.
	 * @return The updated report.
	 */
	public LiveData<Report> updateReport(Report report) {
		MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

		// Update the report.
		firebaseFirestore
			.collection("reports")
			.document(report.getId())
			.set(report)
			.addOnCompleteListener(
				reportTask -> {
					if (reportTask.isSuccessful()) {
						// Query succeeded.
						reportLiveData.setValue(report);
					} else {
						// Query failed.
						errorLiveData.setValue(reportTask.getException());
					}
				}
			);

		return reportLiveData;
	}

	/**
	 * Deletes a report.
	 * @param report The report to be delete.
	 * @return The deleted report.
	 */
	public LiveData<Report> deleteReport(Report report) {
		MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

		// Delete the report.
		firebaseFirestore
			.collection("reports")
			.document(report.getId())
			.delete()
			.addOnCompleteListener(
				reportTask -> {
					if (reportTask.isSuccessful()) {
						// Query succeeded.
						reportLiveData.setValue(report);
					} else {
						// Query failed.
						errorLiveData.setValue(reportTask.getException());
					}
				}
			);

		return reportLiveData;
	}

	/**
	 * Returns the report errors (updated in real time).
	 * @return The report errors (updated in real time).
	 */
	public LiveData<Exception> getErrors() {
		return errorLiveData;
	}

	/**
	 * Clears the report errors.
	 * This avoids receiving the same error multiple times.
	 */
	public void clearErrors() {
		errorLiveData.setValue(null);
	}
}
