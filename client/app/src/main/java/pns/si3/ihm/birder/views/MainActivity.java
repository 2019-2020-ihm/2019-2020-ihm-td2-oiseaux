package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.views.auth.SignInActivity;
import pns.si3.ihm.birder.views.notifications.NotificationActivity;
import pns.si3.ihm.birder.views.reports.ReportActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth auth;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout.
        setContentView(R.layout.activity_main);

        // Initialize firebase.
		auth = FirebaseAuth.getInstance();

		setSpinner();
        button = findViewById(R.id.buttonMain);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignalActivity();
            }
        });

    }

    public void goToSignalActivity() {
        Intent intent = new Intent(this, ReportActivity.class);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(pos){
            case 0:break;
            case 1: // Map
                {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
            break;
            case 2: //Compte (connecté) / Se connecter (déconnecté)
            {
                if (auth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this,AccountActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
            case 3:// Déconnexion (connecté)
                {
				// The user is connected.
				if (auth.getCurrentUser() != null) {
					// Sign out the user.
					auth.signOut();

					// Success toast.
					Toast.makeText(
						MainActivity.this,
						"Vous avez été déconnecté !",
						Toast.LENGTH_SHORT
					).show();

					setSpinner();
				}
				// The user is not connected.
				else {
					// Navigate to sign in.
					Intent intent = new Intent(MainActivity.this, SignInActivity.class);
					startActivity(intent);
				}
            }break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

	@Override
	protected void onRestart() {
		super.onRestart();
		setSpinner();
	}

	public void setSpinner(){
        final Spinner spinner = findViewById(R.id.spinner_main);
        spinner.setAdapter(null);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<>();
        list.add("Menu");
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
