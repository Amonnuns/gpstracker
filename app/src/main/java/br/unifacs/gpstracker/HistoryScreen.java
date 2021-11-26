package br.unifacs.gpstracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class HistoryScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historyscreen);

        Button buttonMap = (Button) findViewById(R.id.buttonMap);
        Button buttonLocations = (Button) findViewById(R.id.buttonLocations);
        Button buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonMap.setOnClickListener(this);
        buttonLocations.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonMap:
                Intent z=new Intent(this,HistoryMaps.class);
                startActivity(z);
                break;
            case R.id.buttonLocations:
                Intent x=new Intent(this,LocationsTable.class);
                startActivity(x);
                break;
            case R.id.buttonDelete:
                System.out.println("Locations deleted");
                Firebase ref = new Firebase();
                ref.deleteLocs();
                Toast.makeText(this, "Locations deleted", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
