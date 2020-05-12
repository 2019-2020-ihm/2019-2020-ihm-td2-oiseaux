package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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
import pns.si3.ihm.birder.views.auth.SignInActivity;
import pns.si3.ihm.birder.views.reports.MainActivity;

public class MapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	private FirebaseAuth auth;
	private MapView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		IMapController mapController;

		super.onCreate(savedInstanceState);
		Configuration.getInstance().load(   getApplicationContext(),
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
		setContentView(R.layout.activity_map);

		// Initialize firebase.
		auth = FirebaseAuth.getInstance();

		setSpinner();

		map = findViewById(R.id.map);
		map.setTileSource(TileSourceFactory.MAPNIK);
		map.setBuiltInZoomControls(true);
		map.setMultiTouchControls(true);

		mapController = map.getController();
		mapController.setZoom(18);
		GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
		mapController.setCenter(startPoint);


		ArrayList<OverlayItem> items = new ArrayList<OverlayItem>(); // future liste de nos signalisations
		OverlayItem home = new OverlayItem("Mouette", "", new GeoPoint(43.65020,7.00517));
		Drawable m = home.getMarker(0);

		items.add(home); // Lat/Lon decimal degrees
		items.add(new OverlayItem("Merle", "", new GeoPoint(43.64950,7.00517))); // Lat/Lon decimal degrees

		//the Place icons on the map with a click listener
		ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items,
				new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
					@Override
					public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
						//do something
						return true;
					}
					@Override
					public boolean onItemLongPress(final int index, final OverlayItem item) {
						return false;
					}
				});


		mOverlay.setFocusItemsOnTap(true);
		map.getOverlays().add(mOverlay);

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
			case 2: //Compte (connecté) / Se connecter (déconnecté)
			{
				if (auth.getCurrentUser() != null) {
					Intent intent = new Intent(MapActivity.this,AccountActivity.class);
					startActivity(intent);
				}
				else {
					Intent intent = new Intent(MapActivity.this, SignInActivity.class);
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
		List<String> list = new ArrayList<String>();
		list.add("Menu");
		list.add("Dernières signalisations");
		// The user is connected.
		if (auth.getCurrentUser() != null) {
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
