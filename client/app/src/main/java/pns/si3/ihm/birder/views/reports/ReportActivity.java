package pns.si3.ihm.birder.views.reports;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.views.GPSActivity;
import pns.si3.ihm.birder.views.GpsActivity;
import pns.si3.ihm.birder.views.pictures.PictureActivity;

/**
 * Report activity.
 *
 * Allows the user to create bird reports.
 */
public class ReportActivity
	extends AppCompatActivity
	implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
	public static final int REQUEST_PICTURE = 1;

	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "ReportActivity";

	/**
	 * The report view model.
	 */
	private ReportViewModel reportViewModel;

	/**
	 * The authentication view model.
	 */
	private AuthViewModel authViewModel;

	/**
	 * The activity fields.
	 */
    ImageView picture;
	EditText editSpecies;
	EditText editNumber;
	EditText editDate;
    EditText editTime;
	EditText editLocation;

	/**
	 * The date and time values.
	 */
	int selectedDay;
	int selectedMonth;
	int selectedYear;
	int selectedHour;
	int selectedMinute;

	/**
	 * The image values.
	 */
	Uri pictureUri;
	boolean pictureCreated;

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
		initViewModels();
		initFields();
		initValues();
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
	 * Initializes the view models that hold the data.
	 */
	private void initViewModels() {
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
		authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
	}

	/**
	 * Initializes the field values.
	 */
	private void initValues() {
		// Date and time values.
		Calendar calendar = Calendar.getInstance();
		selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
		selectedMonth = calendar.get(Calendar.MONTH);
		selectedYear = calendar.get(Calendar.YEAR);
		selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
		selectedMinute = calendar.get(Calendar.MINUTE);
		updateDateField();
		updateTimeField();

		// Picture values.
		pictureUri = null;
		pictureCreated = false;
	}

	/**
	 * Initializes the activity fields.
	 */
    private void initFields() {
    	// Get the fields.
		picture = findViewById(R.id.image_picture);
		editSpecies = findViewById(R.id.edit_species);
		editNumber = findViewById(R.id.edit_number);
		editLocation = findViewById(R.id.edit_location);

		// Edit date field.
		editDate = findViewById(R.id.edit_date);
		editDate.setOnClickListener(
			v -> {
				DatePickerDialog datePickerDialog = createDatePickerDialog();
				datePickerDialog.show();
			}
		);

		// Edit time field.
		editTime = findViewById(R.id.edit_time);
		editTime.setOnClickListener(
			v -> {
				TimePickerDialog timePickerDialog = createTimePickerDialog();
				timePickerDialog.show();
			}
		);
	}

	/**
	 * Initializes the activity buttons.
	 */
    private void initButtons() {
    	// Return button.
		returnButton = findViewById(R.id.button_return);
		returnButton.setOnClickListener(v -> {
			deleteCreatedPicture();
			finish();
		});

		// Edit image button.
		editImageButton = findViewById(R.id.edit_image_button);
		editImageButton.setOnClickListener(v -> {
			Intent intent = new Intent(ReportActivity.this, PictureActivity.class);
			startActivityForResult(intent, REQUEST_PICTURE);
		});

		// Delete image button.
		deleteImageButton = findViewById(R.id.delete_image_button);
		deleteImageButton.setOnClickListener(v -> {
			resetPicture();
		});

		// Current location button.
		currentLocationButton = findViewById(R.id.text_current_location);
		currentLocationButton.setOnClickListener(v -> {
			// TO-DO
		});

		// Choose location button.
		chooseLocationButton = findViewById(R.id.text_choose_location);
		chooseLocationButton.setOnClickListener(v -> {
			Intent intent = new Intent(ReportActivity.this, GpsActivity.class);
			startActivity(intent);
		});

		// Submit button.
		submitButton = findViewById(R.id.button_submit);
		submitButton.setOnClickListener(v -> {
			submit();
		});
	}

	/**
	 * Submits the report, if the form is valid.
	 */
	public void submit() {
    	if (isFormValid()) {
    		createReport();
		}
	}

	/**
	 * Creates the report.
	 */
	public void createReport() {
		// Initialize the report.
		Report report = getReport();

		// Request the report creation.
		reportViewModel.createReport(report);

		// Query succeeded.
		reportViewModel
			.getCreatedReportLiveData()
			.observe(
				this,
				createdReport -> {
					if (createdReport != null) {
						// Success toast.
						Toast.makeText(
							ReportActivity.this,
							"Votre signalement a été envoyé.",
							Toast.LENGTH_SHORT
						).show();

						// Close the activity.
						finish();
					}
				}
			);

		// Query failed.
		reportViewModel
			.getReportErrorsLiveData()
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
	 * Returns the report created from the field values.
	 * @return The report created from the field values.
	 */
	private Report getReport() {
		// Get the values.
		String species = editSpecies.getText().toString();
		int number = Integer.parseInt(editNumber.getText().toString());
		String userId = authViewModel.getAuthenticationId();
		Date date = getSelectedDate();

		// Init the report.
		return new Report(
			null,
			userId,
			species,
			number,
			pictureUri,
			date
		);
	}

	/**
	 * Checks if the form is valid.
	 * @return Whether the form is valid, or not.
	 */
	private boolean isFormValid() {
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

	/**
	 * Deletes a picture file by URI.
	 */
	public void deletePictureFile(Uri uri) {
		ContentResolver contentResolver = getContentResolver();
		contentResolver.delete(uri, null, null);
	}

	/**
	 * Deletes the picture if it has been created.
	 */
	private void deleteCreatedPicture() {
		if (pictureUri != null && pictureCreated) {
			deletePictureFile(pictureUri);
		}
	}

	/**
	 * Sets the picture.
	 * @param uri The new picture URI.
	 * @param created Whether the image has been created, or not.
	 */
	private void setPicture(Uri uri, boolean created) {
		pictureUri = uri;
		pictureCreated = created;
		picture.setImageURI(pictureUri);
	}

	/**
	 * Resets the picture.
	 */
	private void resetPicture() {
		// Delete the selected picture if created.
		deleteCreatedPicture();

		// Reset attributes.
		pictureUri = null;
		pictureCreated = false;
		picture.setImageResource(R.drawable.gallery);
	}

	/**
	 * Method triggered when the activity receives a result.
	 * @param requestCode The request code.
	 * @param resultCode The result code.
	 * @param data The result data.
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE && resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null) {
				// Reset the picture.
				resetPicture();

				// Set the new picture.
				setPicture(
					(Uri) bundle.get("pictureURI"),
					(boolean) bundle.get("pictureCreated")
				);
			}
        }
    }

	/*====================================================================*/
	/*                            DATE AND TIME                           */
	/*====================================================================*/

	/**
	 * Creates a date picker dialog.
	 * @return A date picker dialog.
	 */
    private DatePickerDialog createDatePickerDialog() {
    	// Create date picker dialog.
		DatePickerDialog datePickerDialog = new DatePickerDialog(
			this,
			this,
			selectedYear,
			selectedMonth,
			selectedDay
		);

		// Get date interval.
		Date minDate = getMinDate();
		Date now = new Date();

		// Set date interval.
		DatePicker datePicker = datePickerDialog.getDatePicker();
		datePicker.setMinDate(minDate.getTime());
		datePicker.setMaxDate(now.getTime());

		return datePickerDialog;
	}

	/**
	 * Creates a time picker dialog.
	 * @return A time picker dialog.
	 */
	private TimePickerDialog createTimePickerDialog() {
		return new TimePickerDialog(
			this,
			this,
			selectedHour,
			selectedMinute,
			true
		);
	}

	/**
	 * Returns the date created from the field values.
	 * @return The date created from the field values.
	 */
	private Date getSelectedDate() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(
			selectedYear,
			selectedMonth,
			selectedDay,
			selectedHour,
			selectedMinute
		);
		return calendar.getTime();

	}

	/**
	 * Returns the minimal date a user can select.
	 * @return The minimal date a user can select.
	 */
	private Date getMinDate() {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, -2);
		return calendar.getTime();
	}

	/**
	 * Updates the date field with the current date values.
	 */
	private void updateDateField() {
		editDate.setText(
			String.format(
				Locale.getDefault(),
				"%02d/%02d/%02d",
				selectedDay,
				selectedMonth + 1,
				selectedYear
			)
		);
	}

	/**
	 * Updates the time field with the current time values.
	 */
	private void updateTimeField() {
		editTime.setText(
			String.format(
				Locale.getDefault(),
				"%02d:%02d",
				selectedHour,
				selectedMinute
			)
		);
	}

	/**
	 * Method triggered when the user selects a date on the date picker dialog.
	 * @param view The current view.
	 * @param year The selected year.
	 * @param month The selected month (starting at zero).
	 * @param dayOfMonth The selected day of the month.
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
		selectedDay = dayOfMonth;
		selectedMonth = month;
		selectedYear = year;
		updateDateField();
	}

	/**
	 * Method triggered when the user selects a time on the time picker dialog.
	 * @param view The current view.
	 * @param hourOfDay The selected hour.
	 * @param minute The selected minute.
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		selectedHour = hourOfDay;
		selectedMinute = minute;
		updateTimeField();
	}
}
