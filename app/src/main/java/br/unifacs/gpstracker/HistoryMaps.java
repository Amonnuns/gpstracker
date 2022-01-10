package br.unifacs.gpstracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.unifacs.gpstracker.databinding.ActivityHistoryMapsBinding;

public class HistoryMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityHistoryMapsBinding binding;
    private ArrayList<Map> locations = new ArrayList<>();
    private List<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_history_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions();
        Firebase loc = new Firebase();
        locations = loc.getLocations();

        double lat = (double) locations.get(0).get("latitude");
        double log = (double) locations.get(0).get("longitude");

        LatLng firstLoc = new LatLng(lat,log);
        
        CameraPosition cameraPosition = new CameraPosition.Builder().target(firstLoc).zoom(15).build();


        if (locations.size() == 0){
            locations = loc.getLocations();
        }
        else {
            for(Map location : locations){
                lat = (double) location.get("latitude");
                log = (double) location.get("longitude");
                points.add(new LatLng(lat,log));
                firstLoc = new LatLng(lat,log);
            }


        }

        Iterable iterator = new Iterable() {
            @NonNull
            @Override
            public Iterator iterator() {
                return points.iterator();
            }
        };
        polylineOptions.addAll(iterator).width(25).color(Color.BLUE).clickable(true);
        System.out.println(polylineOptions.getPoints());
        Polyline polyline = mMap.addPolyline(polylineOptions);

        //setPolyline(mMap,points);

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void setPolyline(GoogleMap googleMap, List locs){
        GoogleMap map = googleMap;
        List points = locs;


    }
}