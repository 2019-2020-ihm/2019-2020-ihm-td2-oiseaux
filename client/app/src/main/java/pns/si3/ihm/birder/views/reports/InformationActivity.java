package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;

public class InformationActivity extends AppCompatActivity {

	/**
	 * The tag of the log messages.
	 */
	static final String TAG = "InformationActivity";

	/**
	 * The activity buttons.
	 */
    private Button buttonInfoRetour;
    private Button shareSignal;


	/**
	 * The activity fields.
	 */
	private ImageView imageInfo;
	private TextView textInfoEspece;
    private TextView textInfoName;
    private TextView textInfoNumber;
    private TextView textInfoDate;
    private TextView textInfoAuteur;

	/**
	 * The report view model.
	 */
	private ReportViewModel reportViewModel;

	/**
	 * The user view model.
	 */
    private UserViewModel userViewModel;

	/**
	 * The species view model.
	 */
	private SpeciesViewModel speciesViewModel;

	/**
	 * The selected report.
	 */
	private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
		initViewModels();
        initButtons();
        initFields();
        loadReport();
    }

	/**
	 * Initializes the view models that hold the data.
	 */
	private void initViewModels() {
		userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
		speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
	}

	/**
	 * Initializes the activity buttons.
	 */
	private void initButtons(){
        buttonInfoRetour = findViewById(R.id.buttonInfoRetour);
        buttonInfoRetour.setOnClickListener(v -> {
			Intent intent = new Intent(InformationActivity.this, MainActivity.class);
			startActivity(intent);
		});

		shareSignal = findViewById(R.id.buttonSignalShare);
		shareSignal.setOnClickListener(v -> {
			if (report != null) {
				String shareBody = "L'oiseau d'espèce \"" + report.getSpecies() + "\" a été observé!";
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "oiseau");
				sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Partager via"));
			}
		});
    }

	/**
	 * Initializes the activity fields.
	 */
	private void initFields(){
        imageInfo = findViewById(R.id.imageInfo);
        textInfoEspece = findViewById(R.id.textInfoEspece);
        textInfoNumber = findViewById(R.id.textInfoNombre);
        textInfoDate = findViewById(R.id.textInfoDate);
        textInfoAuteur = findViewById(R.id.textInfoAuteur);
        textInfoName = findViewById(R.id.textInfoNom);
    }

	/**
	 * Loads the report.
	 */
	private void loadReport() {
		// Get the report id.
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");

		// Get the report.
    	reportViewModel.getReport(id);
		reportViewModel
			.getSelectedReportLiveData()
			.observe(
				this,
				selectedReport -> {
					if (selectedReport != null) {
						// Update the report.
						report = selectedReport;
						updateReport();
						loadPicture();
						loadUser();
					}
				}
			);
	}

	/**
	 * Update the report values.
	 */
	private void updateReport() {
		textInfoEspece.setText("Espèce : " + report.getSpecies());
		textInfoNumber.setText("Nombre : " + report.getNumber());
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd-MM-yyyy ");
		String date = formatter.format(report.getDate());
		textInfoDate.setText("Date : " + date);

		// Search the species on the database (based on user input).
		speciesViewModel.searchSpecies(report.getSpecies());

		// Query succeeded.
		speciesViewModel
			.getSearchedSpeciesLiveData()
			.observe(
				this,
				foundSpecies -> {
					// Species found.
					if (foundSpecies != null && foundSpecies.size() > 0) {
						Species bestMatch = foundSpecies.get(0);
						textInfoName.setText("Nom scientifique : " + bestMatch.getName());
					}
				}
			);

		// Query failed.
		speciesViewModel
			.getSpeciesErrorsLiveData()
			.observe(
				this,
				error -> {
					if (error != null) {
						// Log the error.
						Log.e(TAG, error.getMessage());
					}
				}
			);
	}

	/**
	 * Loads the report user.
	 */
	private void loadUser() {
		userViewModel.getUser(report.getUserId());
		userViewModel
			.getSelectedUserLiveData()
			.observe(
				this,
				user -> {
					if (user != null) {
						textInfoAuteur.setText("Par : " + user.getFirstName() + " " + user.getLastName());
					}
				}
			);
	}

	/**
	 * Loads the report picture.
	 */
    private void loadPicture() {
		String picturePath = report.getPicturePath();
		if (picturePath != null) {
			FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
			StorageReference pictureReference = firebaseStorage.getReference(picturePath);
			Glide
				.with(this)
				.load(pictureReference)
				.into(imageInfo);
		}
	}



}
