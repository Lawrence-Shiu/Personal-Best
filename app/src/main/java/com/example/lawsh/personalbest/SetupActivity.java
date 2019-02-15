package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetupActivity extends AppCompatActivity {

    private int heightInInches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Button done = findViewById(R.id.done_button);
        done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feetText = ((EditText)findViewById(R.id.feet_input)).getText().toString();
                String inchesText = ((EditText)findViewById(R.id.in_input)).getText().toString();

                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();


                if(feetText.equals("") || inchesText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input a valid height", Toast.LENGTH_SHORT);
                }
                int inches = Integer.parseInt(inchesText);
                int feet = Integer.parseInt(feetText);

                if(inches > 12 || feet > 7) {
                    Toast.makeText(getApplicationContext(), "Please input a valid height", Toast.LENGTH_SHORT);
                }

                heightInInches = feet * 12 + inches;
                edit.putInt("height", heightInInches);
                edit.apply();
                finish();
            }
        });
    }
}