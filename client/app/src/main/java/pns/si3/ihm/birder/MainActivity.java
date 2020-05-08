package pns.si3.ihm.birder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSpinner();

        button = (Button) findViewById(R.id.buttonMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignalActivity();
            }
        });
    }

    public void goToSignalActivity() {
        Intent intent = new Intent(this, SignalActivity.class);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(pos){
            case 0:break;
            case 1:{
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
            break;
            case 2:{
                Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
                startActivity(intent);
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinner(){
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_main);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<>();
        list.add("Menu");
        list.add("Voir Carte");
        list.add("Se connecter");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}
