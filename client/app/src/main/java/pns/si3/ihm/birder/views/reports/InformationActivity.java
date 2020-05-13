package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.os.Bundle;
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
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;

public class InformationActivity extends AppCompatActivity {
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

		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
        loadReport(id);

		shareSignal = findViewById(R.id.buttonSignalShare);
		shareSignal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String shareBody = "L'oiseau d'espèce \"" + report.getSpecies() + "\" a été observé!";
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "oiseau");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Partager via"));
			}
		});
    }

    private void initButtons(){
        buttonInfoRetour = findViewById(R.id.buttonInfoRetour);
        buttonInfoRetour.setOnClickListener(v -> {
			Intent intent = new Intent(InformationActivity.this, MainActivity.class);
			startActivity(intent);
		});
    }

    private void initFields(){
        imageInfo = (ImageView) findViewById(R.id.imageInfo);
        textInfoEspece = (TextView) findViewById(R.id.textInfoEspece);
        textInfoNumber = (TextView) findViewById(R.id.textInfoNombre);
        textInfoDate = (TextView) findViewById(R.id.textInfoDate);
        textInfoAuteur = (TextView) findViewById(R.id.textInfoAuteur);
        textInfoName = (TextView) findViewById(R.id.textInfoNom);
    }

    /**
     * Initializes the view models that hold the data.
     */
    private void initViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

	/**
	 * Loads the report from the database.
	 * @param id The id of the report.
	 */
	private void loadReport(String id) {
    	reportViewModel.getReport(id);
		reportViewModel
			.getSelectedReportLiveData()
			.observe(
				this,
				selectedReport -> {
					if (selectedReport != null) {
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
