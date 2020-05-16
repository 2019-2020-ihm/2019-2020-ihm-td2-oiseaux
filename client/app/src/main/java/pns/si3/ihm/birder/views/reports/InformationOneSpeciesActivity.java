package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.models.Species;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;

public class InformationOneSpeciesActivity extends AppCompatActivity {

    /**
     * The tag for the log messages.
     */
    private static final String TAG = "InformationOneSpecies";

    /**
     * The fields of the activity.
     */
    private TextView textSpecies;
    private ListView listView;
    private Species species;
    private ArrayList<String> listItems;
    private ArrayAdapter<String> adapter;
    private Button buttonReturn;

    /**
     * The species view model.
     */
    private SpeciesViewModel speciesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_one_species);

        initFields();
        initViewModels();
        loadSpecies();
    }

    private void initFields(){
        textSpecies = findViewById(R.id.textOneSpecies);
        listView = findViewById(R.id.listOneSpecies);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        buttonReturn = findViewById(R.id.buttonOneSpeciesReturn);

        buttonReturn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initViewModels() {
       speciesViewModel = new ViewModelProvider(this).get(SpeciesViewModel.class);
    }

    /**
     * Loads the report.
     */
    private void loadSpecies() {
        // Get the report id.
        Intent intent = getIntent();
        String speciesId = intent.getStringExtra("speciesId");

        // Get the report.
        speciesViewModel.getSpecies(speciesId);
        speciesViewModel
                .getSelectedSpeciesLiveData()
                .observe(
                        this,
                        selectedSpecies -> {
                            if (selectedSpecies != null) {
                                // Update the data.
                                species = selectedSpecies;
                                updateSpecies();
                            }
                        }
                );
    }

    private void updateSpecies(){
        if(species != null) {
            textSpecies.setText(species.getFrenchCommonName());
            adapter.add("Nom scientifique : " + species.getName());
            adapter.add("Taxon : " + species.getTaxon());
            adapter.add("Ordre : " + species.getOrder());
            adapter.add("Famille : " + species.getFamily());
            adapter.add("Genre : " + species.getGenus());
            adapter.add("Espèce éteinte : " + (species.isExtinct() ? "Oui" : "Non"));
            adapter.add("Région de reproduction : " + species.getBreedingRegion());
            adapter.add("Sous-région de reproduction : " + species.getBreedingSubregion());
            adapter.add("Sous-région non reproductrice : " +
                    (species.getNonbreedingSubregion().equals("None") ?
                            "Aucune" : species.getNonbreedingSubregion()));
        }

        }
}
