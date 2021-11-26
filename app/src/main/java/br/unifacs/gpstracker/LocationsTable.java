package br.unifacs.gpstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationsTable extends AppCompatActivity {


    private static ArrayList<Map> locations = new ArrayList<>();
    private ListView lv_savedLocations;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_table);
        lv_savedLocations = (ListView) findViewById(R.id.lv_savedLocations);
        Firebase locs = new Firebase();
        locations = locs.getLocations();
        lv_savedLocations.setAdapter(new ArrayAdapter<Map>(this, android.R.layout.simple_list_item_1, locations));


    }






}