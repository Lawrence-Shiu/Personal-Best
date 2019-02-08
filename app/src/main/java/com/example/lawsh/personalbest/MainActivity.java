package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.lawsh.personalbest.fitness.FitnessService;
import com.example.lawsh.personalbest.fitness.FitnessServiceFactory;
import com.example.lawsh.personalbest.fitness.GoogleFitAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private String ACTIVE_KEY = "ACTIVE_STEPS";
    //private static final String TAG = "mainActivity";

    private Button fitBtn;
    private boolean start = false;
    private TextView textSteps;
    private TextView goalText;
    private TextView activeText;
    private int goal = 5000;
    private int totalSteps = 0;
    private int activeSteps = 0;
    private int counter = 0;


    private FitnessService fitnessService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goalText = findViewById(R.id.goalText);
        textSteps = findViewById(R.id.textSteps);
        fitBtn = findViewById(R.id.startWalk);
        activeText = findViewById(R.id.activeText);

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });
        //String fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        UpdateAsyncTask runner = new UpdateAsyncTask();
        runner.execute();

        //persistent data for active steps (doesnt work yet)
        //SharedPreferences prefs = this.getSharedPreferences("personal best", Context.MODE_PRIVATE);
        //prefs.edit().putInt(ACTIVE_KEY, activeSteps);
        fitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(start == false){
                    start = true;
                    fitBtn.setText(" End walk/run ");
                    fitBtn.setBackgroundColor(Color.parseColor("#FFF50410"));
                    counter = totalSteps;

                }else{
                    start = false;
                    fitBtn.setText(" Start walk/run ");
                    fitBtn.setBackgroundColor(Color.parseColor("#10f504"));

                    //SharedPreferences prefs = getSharedPreferences("personal best", Context.MODE_PRIVATE);
                    //prefs.getInt("activeSteps", activeSteps);
                    activeSteps += totalSteps - counter;
                    //prefs.edit().putInt(ACTIVE_KEY, activeSteps).apply();

                    activeText.setText("Active Steps: " + activeSteps);
                }
            }
        });
        fitnessService.setup();

    }
    /*
    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setStepCount(long stepCount) {
        textSteps.setText(String.valueOf(stepCount));
        totalSteps = (int)stepCount;
    }

    public void setGoal(int goal){
        this.goal = goal;
        goalText.setText("Goal: " + goal + " steps");
    }

    private class UpdateAsyncTask extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params){

            try {
                while(true) {
                    fitnessService.updateStepCount();
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;

        }
        @Override
        protected void onPostExecute(String result){ }

        @Override
        protected void onPreExecute(){ }

        @Override
        protected void onProgressUpdate(String... text){
        }
    }

}
