package pns.si3.ihm.birder.views.reports;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

import etudes.fr.demoosm.R;
import pns.si3.ihm.birder.adapters.ReportsAdapter;
import pns.si3.ihm.birder.models.Report;
import pns.si3.ihm.birder.viewmodels.ReportViewModel;
import pns.si3.ihm.birder.viewmodels.UserViewModel;
import pns.si3.ihm.birder.views.AccountActivity;
import pns.si3.ihm.birder.views.ChoiceSpeciesActivity;
import pns.si3.ihm.birder.views.auth.SignInActivity;

public class MapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private MapView map;
	private ReportViewModel reportViewModel;
	private UserViewModel userViewModel;
	private ReportsAdapter reportsAdapter;
	private List<Report> reports;
	IMapController mapController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Configuration.getInstance().load(   getApplicationContext(),
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
		setContentView(R.layout.activity_map);

		setSpinner();
		init();
		initViewModels();
		observeReports();

	}

	/**
	 * Initializes reports.
	 */
	private void init() {
		reportsAdapter = new ReportsAdapter();
	}

	/**
	 * Initializes the view models that hold the data.
	 */
	private void initViewModels() {
		userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
		reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
	}

	private void observeReports() {
		reportViewModel
				.getReportsLiveData()
				.observe(
						this,
						reports -> {
							map = findViewById(R.id.map);
							map.setTileSource(TileSourceFactory.MAPNIK);
							map.setBuiltInZoomControls(true);
							map.setMultiTouchControls(true);

							mapController = map.getController();
							mapController.setZoom(8);
							GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
							mapController.setCenter(startPoint);
							ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); // future liste de nos signalisations
							double i = 0 ;
							int n = 45 + (int)(Math.random() * ((50 - 45) + 1));
							int n2 = 6 + (int)(Math.random() * ((8 - 6) + 1));
							// Update the reports.
							for(Report report : reports){
								items.add(new OverlayItem(report.getSpecies(), "nombre : " + report.getNumber(), new GeoPoint(n + i ,n2 + i)));
								//the Place icons on the map with a click listener
								i = Math.random() ;
								n = 45 + (int)(Math.random() * ((50 - 45) + 1));
								n2 = 6 + (int)(Math.random() * ((8 - 6) + 1));
							}
							ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items,
									new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
										@Override
										public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
											//do something
											return true;
										}

										@Override
										public boolean onItemLongPress(final int index, final OverlayItem item) {
											Intent intent = new Intent(getApplicationContext(),InformationActivity.class);
											intent.putExtra("id",reports.get(index).getId());
											startActivity(intent);
											return false;
										}
									});

							System.out.println(mOverlay.size());
							mOverlay.setFocusItemsOnTap(true);
							map.getOverlays().add(mOverlay);
						}
				);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(position){
			case 0:break;
			case 1: //Dernières signalisations
				{
				Intent intent = new Intent(MapActivity.this, MainActivity.class);
				startActivity(intent);
			}break;
			case 2://Liste des oiseaux
			{
				Intent intent = new Intent(MapActivity.this, ChoiceSpeciesActivity.class);
				intent.putExtra("want", "allSpecies");
				startActivity(intent);
			}
			break;
			case 3: //Compte (connecté) / Se connecter (déconnecté)
			{
				if (userViewModel.isAuthenticated()) {
					Intent intent = new Intent(MapActivity.this, AccountActivity.class);
					startActivity(intent);
				}
				else {
					Intent intent = new Intent(MapActivity.this, SignInActivity.class);
					startActivity(intent);
				}
			}break;
			case 4:// Déconnexion (connecté)
			{
				// The user is connected.
				if (userViewModel.isAuthenticated()) {
					// Sign out the user.
					userViewModel.signOut();

					// Success toast.
					Toast.makeText(
							MapActivity.this,
							"Vous avez été déconnecté !",
							Toast.LENGTH_SHORT
					).show();

					setSpinner();
				}
				// The user is not connected.
				else {
					// Navigate to sign in.
					Intent intent = new Intent(MapActivity.this, SignInActivity.class);
					startActivity(intent);
				}
			}break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}


	public void setSpinner(){
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_map);
		spinner.setOnItemSelectedListener(this);
		List<String> list = new ArrayList<>();
		list.add("Menu");
		list.add("Dernières signalisations");
		list.add("Liste des oiseaux");
		// The user is connected.
		if (userViewModel.isAuthenticated()) {
			list.add("Compte");
			list.add("Se déconnecter");
		}
		// The user is not connected.
		else {
			list.add("Se connecter");
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
}
