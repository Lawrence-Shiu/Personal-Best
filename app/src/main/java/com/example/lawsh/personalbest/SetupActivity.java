package com.example.lawsh.personalbest;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private int heightInInches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        final SharedPreferences preferences = getSharedPreferences("PB", MODE_PRIVATE);

        if (preferences.getInt("height", 0) != 0) {
            setResult(RESULT_CANCELED);
            finish();
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", 0);
        map.put("email", User.getInstance().getEmail());
        map.put("height", 0);
        map.put("stepsTaken", 0);
        map.put("currentGoal", 5000);
        map.put("activeSteps", 0);
        map.put("friends", "[]");

        FirestoreAdapter.getInstance().
                updateDatabase(User.getInstance().getEmail(), map, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("PendingFriendActivity", User.getInstance().getEmail() + ", " +
                        User.getInstance().toMap().toString());
            }
            }, new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.d("PendingFriendActivity", "Error writing document", e);
                }
        });

        Button done = findViewById(R.id.done_button);
        done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor edit = preferences.edit();

                String feetText = ((EditText)findViewById(R.id.feet_input)).getText().toString();
                String inchesText = ((EditText)findViewById(R.id.in_input)).getText().toString();

                if(feetText.equals("") || inchesText.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input a valid height", Toast.LENGTH_SHORT).show();
                    return;
                }

                int inches = Integer.parseInt(inchesText);
                int feet = Integer.parseInt(feetText);

                if(inches > 12 || feet > 7) {
                    Toast.makeText(getApplicationContext(), "Please input a valid height", Toast.LENGTH_SHORT).show();
                    return;
                }

                heightInInches = feet * 12 + inches;
                setResult(RESULT_OK);
                edit.putInt("height", heightInInches);
                edit.apply();
                finish();
            }
        });
    }
}