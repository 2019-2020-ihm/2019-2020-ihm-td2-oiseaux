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
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.adapters.ReportsAdapter;
import pns.si3.ihm.birder.repositories.interfaces.SpeciesRepository;
import pns.si3.ihm.birder.viewmodels.AuthViewModel;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.SpeciesViewModel;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	/**
	 * The tag for the log messages.
	 */
	private static final String TAG = "MainActivity";

	/**
	 * The report view model.
	 */
	private ReportViewModel reportViewModel;

	/**
	 * The authentication view model.
	 */
	private AuthViewModel authViewModel;

	/**
	 * The activity buttons.
	 */
	private Button button;

	/**
	 * The report recycler view.
	 */
	private RecyclerView recyclerView;
	private ReportsAdapter reportsAdapter;
	private RecyclerView.LayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewModels();
		initRecyclerView();
		observeReports();
		initButtons();
	}

	/**
	 * Initializes the view models that hold the data.
	 */
	private void initViewModels() {
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
		authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
	}

	/**
	 * Initializes the recycler view of reports.
	 */
	private void initRecyclerView() {
		// Get the recycler view.
		recyclerView = findViewById(R.id.reports_reycler_view);

		// Set the adapter.
		reportsAdapter = new ReportsAdapter(this);
		recyclerView.setAdapter(reportsAdapter);

		// Set the layout manager.
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
	}

	/**
	 * Observes the reports (in real time).
	 */
	private void observeReports() {
		// Query succeeded.
		reportViewModel
			.getReportsLiveData()
			.observe(
				this,
				reports -> {
					if (reports != null) {
						// Update the recycler view.
						reportsAdapter.setReports(reports);
					}
				}
			);

		// Query failed.
		reportViewModel
			.getReportErrorsLiveData()
			.observe(
				this,
				error -> {
					if (error != null) {
						// Log the error.
						Log.e(TAG, error.getMessage());
					}
				}
			);
	}

	/**
	 * Initializes the activity buttons.
	 */
	private void initButtons() {
		setSpinner();
		button = findViewById(R.id.buttonMain);
		if(!authViewModel.isAuthenticated()){
			button.setVisibility(View.GONE);
		}else{
			button.setVisibility(View.VISIBLE);
		}
		button.setOnClickListener(v -> {
			Intent intent = new Intent(this, ReportActivity.class);
			startActivity(intent);
		});
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
				if (authViewModel.isAuthenticated()) {
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
				if (authViewModel.isAuthenticated()) {
					// Sign out the user.
					authViewModel.signOut();

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
		if (authViewModel.isAuthenticated()) {
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