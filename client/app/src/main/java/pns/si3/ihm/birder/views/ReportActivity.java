package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;

/**
 * Report activity.
 *
 * Allows the user to create bird reports.
 */
public class ReportActivity extends AppCompatActivity {
	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "ReportActivity";

	/**
	 * The report view model.
	 */
	private ReportViewModel reportViewModel;

	/**
	 * The auth view model.
	 */
	private AuthViewModel authViewModel;

	/**
	 * The activity fields.
	 */
	Uri imageUri;
    ImageView image;
	boolean imageUpload;
	EditText editSpecies;
	EditText editNumber;
    EditText editTime;
    EditText editDate;
	EditText editLocation;

	/**
	 * The activity buttons.
	 */
	Button returnButton;
	Button editImageButton;
	Button deleteImageButton;
	TextView currentLocationButton;
	TextView chooseLocationButton;
	Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
		initViewModel();
		initFields();
		initButtons();
    }

	@Override
	public void onStart() {
		super.onStart();
		if (!authViewModel.isAuthenticated()) {
			finish();
		}
	}

	/**
	 * Initializes the report view model that holds the data.
	 */
	private void initViewModel() {
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
		authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
	}

	/**
	 * Initializes the activity fields.
	 */
    private void initFields() {
    	// Get the fields.
		image = findViewById(R.id.image_bird);
		editSpecies = findViewById(R.id.edit_species);
		editNumber = findViewById(R.id.edit_number);
		editLocation = findViewById(R.id.edit_location);
		editTime = findViewById(R.id.edit_time);
		editDate = findViewById(R.id.edit_date);
	}

	/**
	 * Initializes the activity buttons.
	 */
    private void initButtons() {
    	// Return button.
		returnButton = findViewById(R.id.button_return);
		returnButton.setOnClickListener(v -> {
			finish();
		});

		// Edit image button.
		editImageButton = findViewById(R.id.edit_image_button);
		editImageButton.setOnClickListener(v -> {
			Intent intent = new Intent(ReportActivity.this, CameraActivity.class);
			startActivity(intent);
		});

		// Delete image button.
		deleteImageButton = findViewById(R.id.delete_image_button);
		deleteImageButton.setOnClickListener(v -> {
			image.setImageResource(R.drawable.gallery);
			imageUpload = false;
		});

		// Current location button.
		currentLocationButton = findViewById(R.id.text_current_location);
		currentLocationButton.setOnClickListener(v -> {
			// TO-DO
		});

		// Choose location button.
		chooseLocationButton = findViewById(R.id.text_choose_location);
		chooseLocationButton.setOnClickListener(v -> {
			Intent intent = new Intent(ReportActivity.this, GPSActivity.class);
			startActivity(intent);
		});

		// Submit button.
		submitButton = findViewById(R.id.button_submit);
		submitButton.setOnClickListener(v -> {
			submit();
		});
	}

	/**
	 * Creates a report.
	 */
	public void submit() {
    	if (isFormValid()) {
    		createReport();
		}
	}

	public void createReport() {
		// Get values.
		String species = editSpecies.getText().toString();
		int number = Integer.parseInt(editNumber.getText().toString());

		// Report created.
		Log.i(TAG, "Create a report.");
		String userId = authViewModel.getAuthenticationId();
		Report report = new Report(null, userId, species, number);
		reportViewModel
			.createReport(report)
			.observe(
				this,
				createdReport -> {
					if (createdReport != null) {
						Log.i(TAG, "Report created.");
					}
				}
			);

		// Report failed.
		reportViewModel
			.getErrors()
			.observe(
				this,
				reportError -> {
					if (reportError != null) {
						Log.e(TAG, "Report failed.");
						Log.e(TAG, reportError.getMessage());
					}
				}
			);
	}

	/**
	 * Checks if the form is valid.
	 * @return Whether the form is valid, or not.
	 */
	public boolean isFormValid() {
		// Get values.
		String species = editSpecies.getText().toString();
		String number = editNumber.getText().toString();

		// Species is empty.
		if (species.isEmpty()) {
			editSpecies.setError("Veuillez saisir une espèce.");
			editSpecies.requestFocus();
			return false;
		}

		// Number is empty.
		if (number.isEmpty()) {
			editNumber.setError("Veuillez saisir un nombre d'individus.");
			editNumber.requestFocus();
			return false;
		}

		// Number is invalid.
		if (!isNumeric(number)) {
			editNumber.setError("Veuillez saisir un nombre valide d'individus.");
			editNumber.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Checks if a string is numeric.
	 * @param string The string to be checked.
	 * @return Whether the string is numeric, or not.
	 */
	public static boolean isNumeric(String string) {
		return string.matches("\\d+");
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IPictureActivity.PICK_IMAGE){
            imageUri = data.getData();
            image.setImageURI(imageUri);
            imageUpload = true;
        }
    }
}
