package pns.si3.ihm.birder.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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

public class MapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	private MapView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		IMapController mapController;

		super.onCreate(savedInstanceState);
		Configuration.getInstance().load(   getApplicationContext(),
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
		setContentView(R.layout.activity_map);

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

	public void setSpinner(){
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_map);
		spinner.setOnItemSelectedListener(this);
		List<String> list = new ArrayList<String>();
		list.add("Voir Carte");
		list.add("Dernières signalisations");
		list.add("Se connecter");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch(position){
			case 0:break;
			case 1:{
				Intent intent = new Intent(MapActivity.this, MainActivity.class);
				startActivity(intent);
			}break;
			case 2:{
				Intent intent = new Intent(MapActivity.this, SignInActivity.class);
				startActivity(intent);
			}break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
