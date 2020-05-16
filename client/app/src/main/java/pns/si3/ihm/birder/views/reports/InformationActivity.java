package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.models.User;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.GiveSpeciesActivity;

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
	 * The request code
	 */
	public static final int REQUEST_INFORM_SPECIES = 0;


	/**
	 * The activity fields.
	 */
	private ImageView imageInfo;
	private ImageView imageQuestion;
	private TextView textInfoEspece;
    private TextView textInfoName;
    private TextView textInfoNumber;
    private TextView textInfoDate;
    private TextView textInfoAuteur;
    private TextView textGender;
    private TextView textAge;
    private Species species;

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

		imageQuestion = findViewById(R.id.imageView_question);
		imageQuestion.setOnClickListener(v -> {
			if(textInfoEspece.getText().toString().equals("Espèce non renseignée")) {
				Intent intent = new Intent(InformationActivity.this, GiveSpeciesActivity.class);
				intent.putExtra("picturePath", report.getPicturePath());
				intent.putExtra("reportId", report.getId());
				startActivityForResult(intent, REQUEST_INFORM_SPECIES);
			}
			else {
				Intent intent = new Intent(InformationActivity.this, InformationOneSpeciesActivity.class);
				intent.putExtra("speciesId", species.getId());
				startActivity(intent);
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
        textGender = findViewById(R.id.textInfoGender);
        textAge = findViewById(R.id.textInfoAge);
        imageQuestion = findViewById(R.id.imageView_question);
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
		if(!report.getSpecies().equals("Inconnue")){
			textInfoEspece.setText("Espèce : " + report.getSpecies());
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
									species = bestMatch;
									textInfoName.setText("Nom scientifique : " + bestMatch.getName());
								}
							}
					);
		}else {
			textInfoEspece.setText("Espèce non renseignée");
			textInfoName.setVisibility(View.GONE);
			imageQuestion.setVisibility(View.VISIBLE);
		}

		textInfoNumber.setText("Nombre : " + report.getNumber());
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd-MM-yyyy ");
		String date = formatter.format(report.getDate());
		textInfoDate.setText("Date : " + date);


		if(report.getAge() != null && !report.getAge().equals("")) {
			if (Integer.valueOf(report.getAge()) > 1) {
				textAge.setText("Age : environ " + report.getAge() + " ans");
			} else textAge.setText("Age : environ " + report.getAge() + " an");
		}
		else textAge.setVisibility(View.GONE);
		if(report.getGender() != null) textGender.setText("Genre : " + report.getGender());
		else textGender.setVisibility(View.GONE);

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
		// Get the user.
		userViewModel
			.getUser(report.getUserId())
			.observe(
				this,
				task -> {
					// User found.
					if (task.isSuccessful()) {
						// Get the user.
						User user = task.getData();
						String userDisplayName = "Par : " + user.getFirstName() + " " + user.getLastName();
						textInfoAuteur.setText(userDisplayName);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_INFORM_SPECIES && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				String speciesChoosed = (String) bundle.get("name");

			}
		}
	}

}
