package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.enumerations.TrophyEnum;
import pns.si3.ihm.birder.models.Trophy;

public class StatisticsActivity extends AppCompatActivity {
    ArrayList<Trophy> listTrophies = new ArrayList<>();
    ArrayList<Trophy> listTrophiesBloques = new ArrayList<>();
    ListView listTrophiesView;
    ListView listTrophiesBloquesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        createTrophies();
        ArrayList listNoms = new ArrayList();
        for(Trophy trophy : listTrophies){
            String nom = trophy.getTrophyEnum().getName();
            listNoms.add(nom);
        }
        ArrayList listNomsBloque = new ArrayList();
        for(Trophy trophy : listTrophiesBloques){
            String nom = trophy.getTrophyEnum().getName();
            listNomsBloque.add(nom);
        }
        listTrophiesView = (ListView) findViewById(R.id.listViewTrophiesDebloques);
        listTrophiesBloquesView = (ListView) findViewById(R.id.listViewTrophiesbloques);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StatisticsActivity.this, android.R.layout.simple_list_item_1, listNoms);
        listTrophiesView.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(StatisticsActivity.this, android.R.layout.simple_list_item_1, listNomsBloque);
        listTrophiesBloquesView.setAdapter(adapter2);

        Intent intent = new Intent(this, TrophyActivity.class);

        listTrophiesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trophy trophy = listTrophies.get(position);
                intent.putExtra("trophy", trophy);
                startActivity(intent);
            }
        });

        listTrophiesBloquesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trophy trophy = listTrophiesBloques.get(position);
                intent.putExtra("trophy", trophy);
                startActivity(intent);
            }
        });

        Button buttonRetour;

        buttonRetour = (Button) findViewById(R.id.buttonStatRetour);
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    public void createTrophies(){
        Trophy trophy1 = new Trophy("1PhotoTrophe", TrophyEnum.UNEPHOTO);
        Trophy trophy2 = new Trophy("5PhotosTrophe", TrophyEnum.CINQPHOTOS);
        Trophy trophy3 = new Trophy("50PhotosTrophe", TrophyEnum.CINQUANTEPHOTOS);
        Trophy trophy4 = new Trophy("100PhotosTrophe", TrophyEnum.CENTPHOTOS);
        listTrophies.add(trophy1);
        listTrophies.add(trophy2);
        listTrophies.add(trophy3);
        listTrophiesBloques.add(trophy4);

    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
