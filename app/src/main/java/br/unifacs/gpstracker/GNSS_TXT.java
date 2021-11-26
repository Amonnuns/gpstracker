package br.unifacs.gpstracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GNSS_TXT extends AppCompatActivity implements LocationListener, View.OnClickListener{

    private static final int PERMISSION_FINE_LOCATION = 50;
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private MyGnssStatusCallback gnssStatusCallback;
    private ArrayList<String> gnssStats = new ArrayList<>();
    private TextView gnssList;
    private TextView tv_location;
    private GNSS_IMG gnss_img = new GNSS_IMG();
    private int usedSats=0;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnss_txt);
        tv_location = (TextView)findViewById(R.id.tv_locationInfo);
        gnssList = (TextView)findViewById(R.id.gnssList);
        Button buttonImgView = findViewById(R.id.buttonImgView);

        buttonImgView.setOnClickListener(this);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        ativaGNSS();
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

    @Override
    public void onLocationChanged(@NonNull Location location) {

        String mens="Dados da Última posição\n";
        if (location!=null) {
            mens+=String.valueOf("Latitude(graus)=" +Location.convert(location.getLatitude(),Location.FORMAT_SECONDS))+"\n"
                    + String.valueOf("Longitude(graus)= "+Location.convert(location.getLongitude(),Location.FORMAT_SECONDS))+"\n"
                    + String.valueOf("Altitude(Metros sobre o WGS 84)="+location.getAltitude());
        }
        else {
            mens+="Localização Não disponível";
        }
        tv_location.setText(mens);
    }

    public void updateList(String gnssInfo){
        gnssList.setText(gnssInfo);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonImgView){
            startActivity(new Intent(this, GNSS_IMG.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public class MyGnssStatusCallback extends GnssStatus.Callback {

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

            String mens="Dados do Sistema de Posicionamento\n";
            if (status!=null) {
                mens+="Número de Satélites:"+status.getSatelliteCount()+"\n";
                mens+=String.valueOf("Satélites usados para determinar Localização: "+usedSats)+"\n"+"\n";

                usedSats=0;
                for(int i=0;i<status.getSatelliteCount();i++) {
                    if(status.usedInFix(i)){usedSats+=1;}
                    mens+="SVID="+status.getSvid(i)+"-"+status.getConstellationType(i)+
                            " Azi="+status.getAzimuthDegrees(i)+
                            " Elev="+status.getElevationDegrees(i)+"X|"+"\n"
                            +"CNR=" + status.getCn0DbHz(i)+"\n"+"------------------------------------"+"\n";


                  }
            }
            else {
                mens+="GNSS Não disponível";
            }

            updateList(mens);

        }
    }

}

