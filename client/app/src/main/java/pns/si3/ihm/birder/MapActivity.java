package pns.si3.ihm.birder;

import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.os.Bundle;

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

import etudes.fr.demoosm.R;

public class MapActivity extends AppCompatActivity {
	private MapView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		IMapController mapController;

		super.onCreate(savedInstanceState);
		Configuration.getInstance().load(   getApplicationContext(),
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) );
		setContentView(R.layout.activity_map);

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


}
