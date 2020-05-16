package pns.si3.ihm.birder.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.views.reports.MainActivity;

public class GiveSpeciesActivity extends AppCompatActivity {

    /**
     * The tag for the log messages.
     */
    private static final String TAG = "GiveSpeciesActivity";

    /**
     * The activity fields and buttons.
     */
    private Button buttonRetour;
    private ImageView imageOiseau;
    private ListView listView;
    private EditText editSpeciesName;
    private ImageView searchButton;
    private TextView textInformation;


    /**
     * The others elements.
     */
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private String speciesChoosed;
    private String reportId;
    private String picturePath;

    /**
     * The report view model.
     */
    private ReportViewModel reportViewModel;

    /**
     * The species view model.
     */
    private SpeciesViewModel speciesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_give_species);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            picturePath = (String) bundle.get("picturePath");
            reportId = (String) bundle.get("reportId");
        }

        initFieldsAndButtons();
        initViewModels();
        updateImageView();
    }


    private void initFieldsAndButtons(){
        buttonRetour = findViewById(R.id.buttonRetour);
        imageOiseau = findViewById(R.id.imageOiseau);
        editSpeciesName = findViewById(R.id.edit_speciesname_choice);
        searchButton = findViewById(R.id.imageView_choice_search);
        textInformation = findViewById(R.id.tv_choiceSpecies);
        listView = findViewById(R.id.list_choicesBird_choice);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        buttonRetour.setOnClickListener(v -> goBack());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                speciesChoosed = adapter.getItem(position);
                alertDialog();
            }
        });

        editSpeciesName.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                searchButton.performClick();
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;
            }
            return false;
        });

        searchButton.setOnClickListener(v -> {
            if(editSpeciesName.getText().toString().isEmpty()){
                editSpeciesName.setError("Veuillez saisir une espèce.");
            }
            else {

                adapter.clear();
                adapter.notifyDataSetChanged();
                findSpecies();
            }
        });

    }

    private void initViewModels(){
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
    }

    private void updateImageView(){
        if (picturePath != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference pictureReference = firebaseStorage.getReference(picturePath);
            Glide
                    .with(this)
                    .load(pictureReference)
                    .into(imageOiseau);
        }
    }

    private void findSpecies(){
        speciesViewModel.searchSpecies(editSpeciesName.getText().toString());
        speciesViewModel
                .getSearchedSpeciesLiveData()
                .observe(
                        this,
                        foundSpecies -> {
                            if (foundSpecies != null) {
                                for (Species species : foundSpecies) {
                                    adapter.add(species.getFrenchCommonName());
                                    Log.e(TAG, "Trouvé" + species.getFrenchCommonName());
                                    textInformation.setText("Veuillez choisir une espèce :");
                                }
                            }
                        }
                );
        speciesViewModel
                .getSpeciesErrorsLiveData()
                .observe(
                        this,
                        error -> {
                            if (error != null) {
                                Log.e(TAG, error.getMessage());
                                editSpeciesName.setError("L'espèce que vous avez saisie est invalide.");
                            }
                        }
                );
    }

    /**
     * Update the species of the report @reportId with @speciesChoiced
     */
    private void updateReport(){
        reportViewModel.getReport(reportId);
        reportViewModel.getSelectedReportLiveData()
                .observe(this,
                        report -> {
                            if(report != null){
                                report.setSpecies(speciesChoosed);
                                //Update the report
                                reportViewModel.updateReport(report);
                                reportViewModel
                                        .getUpdatedReportLiveData()
                                        .observe(
                                                this,
                                                insertedReport -> {
                                                    // User updated.
                                                    if (insertedReport != null) {
                                                        Log.e("Notif", "User updated!");
                                                    }
                                                }
                                        );
                            }
                        });
        Intent returnInformationActivity = new Intent();
        setResult(RESULT_OK, returnInformationActivity);
        finish();
    }


    private void alertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Changement de l'espèce")
                .setMessage("Voulez-vous associé l'espèce " + speciesChoosed + " à l'image ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateReport();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
