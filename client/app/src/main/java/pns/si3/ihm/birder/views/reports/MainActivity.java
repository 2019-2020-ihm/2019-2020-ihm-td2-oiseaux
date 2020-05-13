package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.adapters.ReportsAdapter;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.MapActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "MainActivity";

    private FirebaseAuth auth;
    private Button button;

    private ReportViewModel reportViewModel;

	private RecyclerView recyclerView;
	private ReportsAdapter reportsAdpater;
	private RecyclerView.LayoutManager layoutManager;

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

		initRecyclerView();
		initViewModels();
		observeReports();
    }

	/**
	 * Initializes the recycler view of reports.
	 */
	private void initRecyclerView() {
		// Get the recycler view.
		recyclerView = findViewById(R.id.my_recycler_view);

		// Set the adapter.
		reportsAdpater = new ReportsAdapter(this);
		recyclerView.setAdapter(reportsAdpater);

		// Set the layout manager.
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
	}

	/**
	 * Initializes the view models that hold the data.
	 */
	private void initViewModels() {
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
	}

	private void observeReports() {
		reportViewModel
			.getReportsLiveData()
			.observe(
				this,
				reports -> {
					// Update the reports.
					reportsAdpater.setReports(reports);
				}
			);
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
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            }break;
            case 3:{
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
