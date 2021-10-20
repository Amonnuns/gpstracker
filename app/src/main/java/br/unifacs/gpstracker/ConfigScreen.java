package br.unifacs.gpstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configscreen);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        RadioGroup degreeGroup = (RadioGroup) findViewById(R.id.degreeGroup);
        RadioGroup speedGroup = (RadioGroup) findViewById(R.id.speedGroup);
        RadioGroup orientationGroup = (RadioGroup) findViewById(R.id.orientationGroup);
        RadioGroup mapGroup = (RadioGroup) findViewById(R.id.mapGroup);
        Switch infoTrafic = (Switch) findViewById(R.id.infoTrafic);


        degreeGroup.check(sharedPreferences.getInt("degreeGroup",0));
        speedGroup.check(sharedPreferences.getInt("speedGroup",0));
        orientationGroup.check(sharedPreferences.getInt("orientationGroup",0));
        mapGroup.check(sharedPreferences.getInt("mapGroup",0));
        infoTrafic.setChecked(sharedPreferences.getBoolean("infoTrafic",false));

        editor = sharedPreferences.edit();

        degreeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor.putInt("degreeGroup",degreeGroup.getCheckedRadioButtonId());
                editor.commit();
            }
        });

        speedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor.putInt("speedGroup",speedGroup.getCheckedRadioButtonId());
                editor.commit();
            }
        });

        orientationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor.putInt("orientationGroup",orientationGroup.getCheckedRadioButtonId());
                editor.commit();
            }
        });

        mapGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                editor.putInt("mapGroup",mapGroup.getCheckedRadioButtonId());
                editor.commit();
            }
        });

        infoTrafic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("infoTrafic", compoundButton.isChecked());
                editor.commit();
            }
        });







    }

}
