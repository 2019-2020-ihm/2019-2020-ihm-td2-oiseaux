package pns.si3.ihm.birder.views.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.reports.MainActivity;
import pns.si3.ihm.birder.views.MapActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;

public class NotificationActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private ListView listView;
    private ArrayList<NotificationItem> notifications;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        auth = FirebaseAuth.getInstance();
        setSpinner();

        initListOfNotification();

        listView = (ListView) findViewById(R.id.listview_notification);
        NotificationAdapter notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);
        listView.setAdapter(notificationAdapter);


    }

    void initListOfNotification(){
        notifications = new ArrayList<NotificationItem>();
        notifications.add(new NotificationItem("Merle noir", R.drawable.merle_noir));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:break;
            case 1: // Liste signalisation
            {
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            break;
            case 2: // Map
            {
                Intent intent = new Intent(NotificationActivity.this, MapActivity.class);
                startActivity(intent);
            }break;
            case 3: //Compte (connecté) / Se connecter (déconnecté)
            {
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(NotificationActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(NotificationActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
            case 4:// Déconnexion (connecté)
            {
                // The user is connected.
                if (auth.getCurrentUser() != null) {
                    // Sign out the user.
                    auth.signOut();

                    // Success toast.
                    Toast.makeText(
                            NotificationActivity.this,
                            "Vous avez été déconnecté !",
                            Toast.LENGTH_SHORT
                    ).show();

                    setSpinner();
                }
                // The user is not connected.
                else {
                    // Navigate to sign in.
                    Intent intent = new Intent(NotificationActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinner(){
        final Spinner spinner = findViewById(R.id.spinner_notification);
        spinner.setAdapter(null);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<>();
        list.add("Menu");
        list.add("Dernières signalisations");
        list.add("Voir Carte");
        // The user is connected.
        if (auth.getCurrentUser() != null) {
            list.add("Compte");
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
