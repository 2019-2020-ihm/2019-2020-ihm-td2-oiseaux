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

public class ChoiceSpeciesActivity extends AppCompatActivity {

    /**
     * The return button.
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_choicespecies);

        initElements();
        initViewModels();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent returnReportActivity = new Intent();
                Log.i(TAG,adapter.getItem(position));
                returnReportActivity.putExtra("name", adapter.getItem(position));
                setResult(RESULT_OK, returnReportActivity);
                finish();
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    editText.setError("Veuillez saisir une espèce.");
                }
                else {
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    findSpecies();
                }
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
                                    Log.e(TAG, "Trouvé" + species.getFrenchCommonName());
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
                                Log.e(TAG, error.getMessage());
                                editText.setError("L'espèce que vous avez saisie est invalide.");
                            }
                        }
                );
    }

    private void initViewModels(){
        speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
    }

}
