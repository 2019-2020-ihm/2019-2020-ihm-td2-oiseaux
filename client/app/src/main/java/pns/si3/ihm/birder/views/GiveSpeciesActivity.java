package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;

public class GiveSpeciesActivity extends AppCompatActivity {
    Button buttonEnregistrerModification;
    Button buttonRetour;
    ImageView imageOiseau;
    Spinner spinnerEspece;
    List<String> listEspece = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_species);

        listEspece.add("pigeon");
        listEspece.add("pivert");
        listEspece.add("rossignol");
        listEspece.add("perruche");

        buttonRetour = (Button) findViewById(R.id.buttonRetour);
        buttonEnregistrerModification = (Button) findViewById(R.id.buttonEnregistrerModification);
        imageOiseau = (ImageView) findViewById(R.id.imageOiseau);
        imageOiseau.setImageResource(R.drawable.gallery);
        spinnerEspece = (Spinner) findViewById(R.id.editEspeceSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listEspece);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspece.setAdapter(adapter);

        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        buttonEnregistrerModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
