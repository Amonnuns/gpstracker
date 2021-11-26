package br.unifacs.gpstracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GNSS_IMG extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private MyGnssStatusCallback gnssStatusCallback;
    private static final int PERMISSION_FINE_LOCATION = 50;
    private GNSSView gnssView;
    private TextView satsCount, txtGPS,txtBEIDOU,txtGLONASS, txtGALILEO;
    private ImageView imageGPS, imageGLONASS, imageBEIDOU, imageGALILEO;
    private boolean valueGps = false, valueGlonass=false, valueGalileo=false,valueBeidou=false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnss_img);
        satsCount = (TextView)findViewById(R.id.satsCount);
        gnssView = (GNSSView)findViewById(R.id.gnssview);
        txtGPS = (TextView)findViewById(R.id.txtGPS);
        txtBEIDOU = (TextView)findViewById(R.id.txtBEIDOU);
        txtGLONASS = (TextView)findViewById(R.id.txtGLONASS);
        txtGALILEO = (TextView)findViewById(R.id.txtGALILEO);

        imageGPS = (ImageView) findViewById(R.id.imageGPS);
        imageGLONASS = (ImageView) findViewById(R.id.imageGLONASS);
        imageBEIDOU = (ImageView) findViewById(R.id.imageBEIDOU);
        imageGALILEO = (ImageView) findViewById(R.id.imageGALILEO);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        ativaGNSS();

        imageGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueGps = !valueGps;
                if(valueGps){
                    gnssView.setButtonValue(GnssStatus.CONSTELLATION_GPS);
                }
                else
                    gnssView.setButtonValue(0);
                }
        });

        imageGLONASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueGlonass = !valueGlonass;
                if(valueGlonass){
                    gnssView.setButtonValue(GnssStatus.CONSTELLATION_GLONASS);
                }
                else
                    gnssView.setButtonValue(0);
            }
        });

        imageGALILEO.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        valueGalileo = !valueGalileo ;
                        if(valueGalileo ){
                            gnssView.setButtonValue(GnssStatus.CONSTELLATION_GALILEO);
                        }
                        else
                            gnssView.setButtonValue(0);
                    }
                });

        imageBEIDOU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueBeidou = !valueBeidou ;
                if(valueBeidou ){
                    gnssView.setButtonValue(GnssStatus.CONSTELLATION_BEIDOU);
                }
                else
                    gnssView.setButtonValue(0);
            }
        });



    }

    @Override
    protected void onStop(){
        super.onStop();
        //desativaGNSS();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ativaGNSS(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(locationProvider.getName(),5*1000,0.1f, this);
            gnssStatusCallback = new MyGnssStatusCallback();
            locationManager.registerGnssStatusCallback(gnssStatusCallback);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ativaGNSS();
                    } else {
                        Toast.makeText(this, "A permissão é necessária para acessar o sistema de posicionamento", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setConstelationsCount(GnssStatus status){
        int gps=0,beidou=0,glonass=0,galileo=0;
        for(int i=0;i<status.getSatelliteCount();i++){

            switch (status.getConstellationType(i)){
                case GnssStatus.CONSTELLATION_GPS:
                    gps+=1; break;
                case GnssStatus.CONSTELLATION_BEIDOU:
                    beidou+=1; break;
                case GnssStatus.CONSTELLATION_GLONASS:
                    glonass+=1; break;
                case GnssStatus.CONSTELLATION_GALILEO:
                    galileo+=1; break;
            }

        }

        txtGPS.setText(String.valueOf(gps)+"\n"+"GPS");
        txtBEIDOU.setText(String.valueOf(beidou)+"\n"+"BEIDOU");
        txtGLONASS.setText(String.valueOf(glonass)+"\n"+"GLONASS");
        txtGALILEO.setText(String.valueOf(galileo)+"\n"+"GALILEO");
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private class MyGnssStatusCallback extends GnssStatus.Callback {
        public MyGnssStatusCallback() {
            super();
        }

        @Override
        public void onStarted() {
        }

        @Override
        public void onStopped() {
        }

        @Override
        public void onFirstFix(int ttffMillis) {
        }

        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            satsCount.setText(String.valueOf(status.getSatelliteCount()));
            setConstelationsCount(status);
            gnssView.onSatelliteStatusChanged(status);
            gnssView.invalidate();
        }
    }
}