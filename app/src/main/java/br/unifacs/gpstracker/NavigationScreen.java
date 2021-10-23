package br.unifacs.gpstracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class NavigationScreen extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_FINE_LOCATION = 50;

    //Objeto pelo qual configuramos as funcionalidades de FusedLocationProviderClient
    LocationRequest locationRequest;

    //API utilizada para ler informações de serviços de localização
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationCallback locationCallback;

    private GoogleMap mMap;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_navigation);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(20000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //Obtendo informações de GPS


        // metódo invocado sempre que a o tempo de intervalo é alcançado
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                System.out.println("Location Result");
                Location location = locationResult.getLastLocation();
                updateMap(mMap, location);
            }
        };

        //Obtém o Objeto que administra o ciclo de vida do mapa e é notificado quando o mapa está pronto
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        updateGPS();
    }

    //Chama um metodo após as permissoes forem garantidas
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        updateGPS();
                    } else {
                        Toast.makeText(this, "A permissão é necessária para que o APP funcione corretamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }


    private void updateGPS() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permissão do usuário para acessar a localização dele
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        updateMap(mMap, location);
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    private void stopLocationUpdates() {
        Toast.makeText(this, "Não estamos te rastreando", Toast.LENGTH_SHORT).show();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private void startLocationUpdates() {

        Toast.makeText(this, "Sua localização está sendo rastreada", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            updateGPS();
        }


    }


    private void configGPS(GoogleMap map) {

        //Obtendo as preferências
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        int mapGroup = sharedPreferences.getInt("mapGroup", 0);
        boolean infoTrafic = sharedPreferences.getBoolean("infoTrafic", false);

        //MapGroup
        if (R.id.radioButtonImg == mapGroup) {
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        } else {
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        //Informações do Trafico
        if (infoTrafic) {
            map.setTrafficEnabled(true);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        UiSettings settings = mMap.getUiSettings();
        configGPS(mMap);

        //Move a camêra para a localização desejada
        LatLng x = new LatLng(0, 0);
        settings.setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(x));


    }

    private String infoMap(Location location){
        float speed ;
        String info,coordinate="",lat,lon, speedInfo;

        int degreeGroup = sharedPreferences.getInt("degreeGroup",0);
        int speedGroup = sharedPreferences.getInt("speedGroup",0);

        if (speedGroup == R.id.radioButtonKm){
            speed = (float) (3.6 * location.getSpeed());
            speedInfo = String.format("%.2f %s",speed,"Km/h");
        }else{
            speed = (float) (2.23694 * location.getSpeed());
            speedInfo = String.format("%.2f %s",speed,"Mph");
        }
        switch (degreeGroup){
            case R.id.radioButtonDecimal:
                lat = Location.convert(location.getLatitude(),Location.FORMAT_DEGREES);
                lon = Location.convert(location.getLatitude(),Location.FORMAT_DEGREES);
                coordinate = String.format("%s %s ",lat,lon);
                break;
            case R.id.radioButtonMinuto:
                lat = Location.convert(location.getLatitude(),Location.FORMAT_MINUTES);
                lon = Location.convert(location.getLatitude(),Location.FORMAT_MINUTES);
                coordinate = String.format("%s %s",lat,lon);
                break;
            case R.id.radioButtonSegundo:
                lat = Location.convert(location.getLatitude(),Location.FORMAT_SECONDS);
                lon = Location.convert(location.getLatitude(),Location.FORMAT_SECONDS);
                coordinate = String.format("%s %s",lat,lon);
                break;
        }

        info = String.format("%s %s",coordinate,speedInfo);
        return info;

    }

    private CameraPosition getMapOrientation(Location location, LatLng position){
        UiSettings settings = mMap.getUiSettings();
        int orientationGroup = sharedPreferences.getInt("orientationGroup",0);


        //Configura a orientação do mapa
        switch(orientationGroup){
            case R.id.radioButtonNorth:
                settings.setRotateGesturesEnabled(false);
                return new CameraPosition.Builder().target(position).zoom(19).bearing(0).build();
            case R.id.radioButtonCourse:
                settings.setRotateGesturesEnabled(false);
                return new CameraPosition.Builder().target(position).zoom(19).bearing(location.getBearing()).build();
            case R.id.radioButtonNenhum:
                settings.setAllGesturesEnabled(true);
                return new CameraPosition.Builder().target(position).zoom(19).build();
        }

        return null;
    }


    private void drawCircle(LatLng point, float accuracy, GoogleMap map){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();
        // Specifying the center of the circle
        circleOptions.center(point);
        // Radius of the circle
        circleOptions.radius(accuracy);
        // Border color of the circle
        circleOptions.strokeColor(0xFFFFFFFF);
        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);
        // Border width of the circle
        circleOptions.strokeWidth(2);
        // Adding the circle to the GoogleMap
        map.addCircle(circleOptions);

    }



    @SuppressLint("MissingPermission")
    private void updateMap (GoogleMap googleMap, Location location){


        mMap = googleMap;
        mMap.setMyLocationEnabled(false);
        mMap.clear();

        String info = infoMap(location);

        //Move a camêra para a localização desejada
        LatLng position = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(position).title("Coordinate and Speed").anchor(0.5f,0.5f).flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.nave_espacial)).rotation(location.getBearing())).setSnippet(info);
        drawCircle(position, location.getAccuracy(),mMap);

        CameraPosition cameraPosition = getMapOrientation(location, position);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



}
