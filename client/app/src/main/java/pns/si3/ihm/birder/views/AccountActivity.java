package pns.si3.ihm.birder.views;

import androidx.appcompat.app.AppCompatActivity;
import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.views.auth.SignInActivity;
import pns.si3.ihm.birder.views.notifications.NotificationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button buttonNotification;
    Button buttonStat;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        buttonNotification =  (Button) findViewById(R.id.button_gestion_notification);
        buttonStat = (Button) findViewById(R.id.button_stats);
        auth = FirebaseAuth.getInstance();
        setSpinner();

        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        buttonStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        // Faire le bouton paramètre du compte


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:break;
            case 1: // Liste signalisation
            {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
            break;
            case 2: // Map
            {
                Intent intent = new Intent(AccountActivity.this, MapActivity.class);
                startActivity(intent);
            }break;
            case 3: //Compte (connecté) / Se connecter (déconnecté)
            {
                if (auth.getCurrentUser() != null) {
                    // Sign out the user.
                    auth.signOut();

                    // Success toast.
                    Toast.makeText(
                            AccountActivity.this,
                            "Vous avez été déconnecté !",
                            Toast.LENGTH_SHORT
                    ).show();

                    setSpinner();
                }
                else {
                    Intent intent = new Intent(AccountActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setSpinner(){
        final Spinner spinner = findViewById(R.id.spinner_account);
        spinner.setAdapter(null);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<>();
        list.add("Menu");
        list.add("Dernières signalisations");
        list.add("Voir Carte");
        // The user is connected.
        if (auth.getCurrentUser() != null) {
            list.add("Se déconnecter");
        }
        // The user is not connected.
        else {
            list.add("Se connecter");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
