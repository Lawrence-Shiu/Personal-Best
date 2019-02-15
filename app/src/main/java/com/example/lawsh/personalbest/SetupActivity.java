package com.example.lawsh.personalbest;

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
                String feetText = findViewById(R.id.feet_input).toString();
                int feet = Integer.parseInt(feetText);

                String inchesText = findViewById(R.id.in_input).toString();
                int inches = Integer.parseInt(inchesText);

                if(inches > 12 || feet > 7) {
                    Toast.makeText(getApplicationContext(), "Please input a valid height", Toast.LENGTH_SHORT);
                }

                heightInInches = feet * 12 + inches;
            }
        });
    }
}