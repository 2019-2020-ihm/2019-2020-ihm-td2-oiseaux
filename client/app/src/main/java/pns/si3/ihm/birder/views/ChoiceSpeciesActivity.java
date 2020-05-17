package pns.si3.ihm.birder.views;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.config.Configuration;

import java.util.ArrayList;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.views.reports.InformationOneSpeciesActivity;

public class ChoiceSpeciesActivity extends AppCompatActivity {

    /**
     * The fields of the activity.
     */
    private Button returnButton;
    private EditText editText;
    private ImageView imageViewSearch;
    private SpeciesViewModel speciesViewModel;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private TextView textView;
    private String TAG = "ChoiceSpecies";
    private String speciesId;

    /**
     * What we want to show.
     */
    private String want;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_choicespecies);
		initViewModels();
        want = getIntent().getStringExtra("want");
        initElements();
    }

	private void initViewModels(){
		speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
	}

    private void initElements(){
        returnButton = findViewById(R.id.button_return_choicespecies);
        imageViewSearch = findViewById(R.id.imageView_choice_search);
        editText = findViewById(R.id.edit_speciesname);
        textView = findViewById(R.id.tv_choice);
        listView = findViewById(R.id.list_choicesBird);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if(want.equals("allSpecies")){
                Intent showInformation = new Intent(ChoiceSpeciesActivity.this, InformationOneSpeciesActivity.class);
                Log.i(TAG,adapter.getItem(position));
                setSpeciesId(adapter.getItem(position));
                showInformation.putExtra("speciesId", speciesId);
                startActivity(showInformation);
            } else {
                Intent returnReportActivity = new Intent();
                Log.i(TAG,adapter.getItem(position));
                returnReportActivity.putExtra("name", adapter.getItem(position));
                setResult(RESULT_OK, returnReportActivity);
                finish();
            }
        });

        imageViewSearch.setOnClickListener(v -> {
            if(editText.getText().toString().isEmpty()){
                editText.setError("Veuillez saisir une espèce.");
            }
            else {
                adapter.clear();
                adapter.notifyDataSetChanged();
                findSpecies();
            }
        });
        returnButton.setOnClickListener(v -> finish());

        editText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                imageViewSearch.performClick();
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                return true;
            }
            return false;
        });
    }

    private void setSpeciesId(String frenchCommonName){
        speciesViewModel.getSearchedSpeciesLiveData()
                .observe(this,
                        speciesList -> {
                            if(speciesList != null) {
                                for (Species speciesSelected : speciesList) {
                                    if(speciesSelected.getFrenchCommonName().equals(frenchCommonName)){
                                        speciesId = speciesSelected.getId();
                                    }
                                }
                            }
                        });
    }

    private void findSpecies(){
        speciesViewModel.searchSpecies(editText.getText().toString());
        speciesViewModel
			.getSearchedSpeciesLiveData()
			.observe(
				this,
				foundSpecies -> {
					if (foundSpecies != null) {
						for (Species species : foundSpecies) {
							adapter.add(species.getFrenchCommonName());
							textView.setText("Veuillez choisir une espèce :");
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
						editText.setError("L'espèce que vous avez saisie est invalide.");
					}
				}
			);
    }
}
