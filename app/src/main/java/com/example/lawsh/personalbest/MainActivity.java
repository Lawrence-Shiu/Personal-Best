package com.example.lawsh.personalbest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.design.widget.*;
import android.widget.Toast;

import com.example.lawsh.personalbest.fitness.FitnessService;
import com.example.lawsh.personalbest.fitness.FitnessServiceFactory;
import com.example.lawsh.personalbest.fitness.GoogleFitAdapter;
import com.google.android.gms.common.data.DataBufferObserver;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private String ACTIVE_KEY = "ACTIVE_STEPS";
    //private static final String TAG = "mainActivity";

    private Button fitBtn;
    private Button setGoal;

    private boolean start = false;
    private TextView textSteps;
    private TextView goalText;
    private TextView activeText;
    private TextView velocity;
    private EditText goal;
    //private int goal = 5000;


    private int totalSteps = 0;
    private int activeSteps = 0;
    private int oldActive = 0;
    private int totalActiveSteps = 0;
    private int counter = 0;
    private long timeCounter = 0;
    private boolean goalMessageFirstAppearance = true;
    private Congratulations congratsMessage;
    private AlertDialog goalReached;
    private int height = 0;

    public boolean testing = false;

    private FitnessService fitnessService;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("user_goal", Context.MODE_PRIVATE);
        editor = prefs.edit();

        // go to set up screen
        Intent setup = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(setup);

        if(!testing) {

            // set goal text
            goalText = findViewById(R.id.goalText);
            int goal = prefs.getInt("goal", 5000);
            goalText.setText("Goal: " + goal + " steps");

            // set step count text
            textSteps = findViewById(R.id.textSteps);
            int steps = prefs.getInt("steps", 0);
            textSteps.setText(Integer.toString(steps));

            // set active step count text
            activeText = findViewById(R.id.activeText);
            activeSteps = prefs.getInt(ACTIVE_KEY, 0);
            activeText.setText("Active Steps: " + Integer.toString(activeSteps));

            velocity = findViewById(R.id.velocity);
            fitBtn = findViewById(R.id.startWalk);
            setGoal = findViewById(R.id.newGoal);

            congratsMessage = new Congratulations(this);
            goalReached = congratsMessage.onCreateAskGoal(savedInstanceState);
            //goalReached.show();

            FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
                @Override
                public FitnessService create(MainActivity mainActivity) {
                    return new GoogleFitAdapter(mainActivity);
                }
            });
            // create google fit adapter
            fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

            // async runner to constantly update steps
            UpdateAsyncPassiveCount passiveRunner = new UpdateAsyncPassiveCount();
            passiveRunner.execute();
            //UpdateAsyncGoal goalRunner = new UpdateAsyncGoal();
            //goalRunner.execute();

            setGoal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog setCustomGoal =  congratsMessage.onCreateCustomGoal(savedInstanceState);
                    setCustomGoal.show();
                }
            });

            fitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (start == false) {
                        start = true;
                        fitBtn.setText(" End walk/run ");
                        fitBtn.setBackgroundColor(Color.parseColor("#FFF50410"));
                        counter = totalSteps;
                        timeCounter = System.nanoTime();

                    } else {
                        start = false;
                        fitBtn.setText(" Start walk/run ");
                        fitBtn.setBackgroundColor(Color.parseColor("#10f504"));

                        totalActiveSteps = prefs.getInt(ACTIVE_KEY, 0);
                        totalActiveSteps += activeSteps;
                        editor.putInt(ACTIVE_KEY, totalActiveSteps);
                        editor.apply();
                    }
                }
            });
            fitnessService.setup();
        }
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
        totalSteps++; //testing purposes only, take out after
        setActiveSteps();
    }

    public void setActiveSteps(){
        if(start == true){
            activeSteps = totalSteps - counter;
            if(oldActive != activeSteps) {
                String printTotal = "Active Steps: " + Integer.toString(prefs.getInt(ACTIVE_KEY, 0) + activeSteps);
                activeText.setText(printTotal);

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(4);

                double timeElapsed = ((double) System.nanoTime() - timeCounter) / 1000000000.0/60/60;
                //String mph = df.format(activeSteps/timeElapsed);
                double mph = activeSteps / timeElapsed;
                mph *= 1000;
                mph = (int)mph/1000;

                velocity.setText(mph + " MPH");
                oldActive = activeSteps;
            }

        }
    }


    private class UpdateAsyncPassiveCount extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params){
            try {

                while(true) {
                    fitnessService.updateStepCount();
                    if(goalMessageFirstAppearance == true){
                        publishProgress();
                    }
                    try {
                        Thread.sleep(1000);
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
        protected void onPreExecute(){  }

        @Override
        protected void onProgressUpdate(String... text){
            goalMessageFirstAppearance = false;
            int goal = prefs.getInt("goal", 5000);
            if(Integer.parseInt(textSteps.getText().toString()) >= goal) {
                goalReached.show();
            }
        }
    }
    private class UpdateAsyncGoal extends AsyncTask<String, String, String> {
        private String resp;
        @Override
        protected String doInBackground(String... params){
            try {

                while(true) {
                    /*
                    if(goalMessageFirstAppearance == true){
                        publishProgress();
                    }
                    */
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
        protected void onPreExecute(){  }

        @Override
        protected void onProgressUpdate(String... text){
            goalMessageFirstAppearance = false;
            int goal = prefs.getInt("goal", 5000);
            if(Integer.parseInt(textSteps.getText().toString()) >= goal) {
                goalReached.show();
            }
        }
    }

    private class Congratulations implements Observer {

        Activity activity;

        public Congratulations(Activity activity) {
            this.activity = activity;
        }
        public AlertDialog onCreateAskGoal(final Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setTitle("Setup a New Goal");
            builder.setMessage("Congratulations! Do you want to setup a new goal?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AlertDialog goalSpecify = onCreateGoalDialog(savedInstanceState);
                    goalSpecify.show();
                }
            });

            builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }

        public AlertDialog onCreateGoalDialog(final Bundle savedInstanceState) {
            final AlertDialog.Builder goalBuilder = new AlertDialog.Builder(activity);

            goalBuilder.setTitle("Setup a New Goal");
            goalBuilder.setMessage("Do you want to set a custom goal or use a default one?");

            goalBuilder.setPositiveButton("Set a custom goal", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AlertDialog customGoal = onCreateCustomGoal(savedInstanceState);
                    customGoal.show();
                }
            });

            goalBuilder.setNegativeButton("Use a default goal", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AlertDialog defaultGoal = onCreateDefaultGoal(savedInstanceState);
                    defaultGoal.show();
                }
            });

            goalBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return goalBuilder.create();
        }

        public AlertDialog onCreateDefaultGoal(final Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            int goal = prefs.getInt("goal", 5000);
            builder.setTitle("Setup a Default Goal");
            builder.setMessage("Is " + (goal+500) + " steps ok?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    saveDefaultGoal();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }

        public AlertDialog onCreateCustomGoal(final Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Setup a Custom Goal");
            LayoutInflater inflater = activity.getLayoutInflater();
            final View v = inflater.inflate(R.layout.dialog_custom, null);

            builder.setView(v)
                    .setPositiveButton("Set Custom Goal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            saveCustomGoal(v);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }

        @Override
        public void update(Observable o, Object arg) {
            int steps = (int) arg;

        }
    }

    public void saveDefaultGoal() {
        int goal = prefs.getInt("goal", 5000);///////////////////////////
        goalText.setText("Goal: " + (goal + 500) + " steps");
        editor.putInt("goal", goal+500);
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved Goal", Toast.LENGTH_SHORT).show();
        goalMessageFirstAppearance = true;
    }

    public void saveCustomGoal(View v) {
        goal = (EditText)v.findViewById(R.id.custom_goal);

        editor.putInt("goal",Integer.parseInt(goal.getText().toString()));
        editor.apply();
        int stepsGoal = Integer.parseInt(goal.getText().toString());
        goalText.setText("Goal: " + stepsGoal + " steps");
        Toast.makeText(MainActivity.this, "Saved Goal", Toast.LENGTH_SHORT).show();
        goalMessageFirstAppearance = true;
    }

}
