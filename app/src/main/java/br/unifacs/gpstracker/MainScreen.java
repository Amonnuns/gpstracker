package br.unifacs.gpstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//tela inicial
public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonConfig = (Button) findViewById(R.id.buttonConfig);
        Button buttonNavigation = (Button) findViewById(R.id.buttonNavigation);
        Button buttonCredits = (Button) findViewById(R.id.buttonCredits);
        Button buttonGNSS = (Button) findViewById(R.id.buttonGNSS);
        Button buttonExit = (Button) findViewById(R.id.buttonExit);
        Button buttonHistory = (Button) findViewById(R.id.buttonHistory);

        buttonConfig.setOnClickListener(this);
        buttonNavigation.setOnClickListener(this);
        buttonCredits.setOnClickListener(this);
        buttonGNSS.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
        buttonHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.buttonConfig:
                Intent i = new Intent(this,ConfigScreen.class);
                startActivity(i);
                break;
            case R.id.buttonNavigation:
                Intent n = new Intent(this,NavigationScreen.class);
                startActivity(n);
                break;
            case R.id.buttonGNSS:
                startActivity(new Intent(this, GNSS_TXT.class));
                break;
            case R.id.buttonCredits:
                Intent x=new Intent(this,CreditsScreen.class);
                startActivity(x);
                break;
            case R.id.buttonHistory:
               startActivity(new Intent(this, HistoryScreen.class));
               break;
            case R.id.buttonExit:
                finish();
                break;
        }
    }
}