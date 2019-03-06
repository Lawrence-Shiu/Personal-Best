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
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private String fitnessServiceKey = "GOOGLE_FIT";
    private String ACTIVE_KEY = "ACTIVE_STEPS";
    private String PASSIVE_KEY = "PASSIVE_KEY";
    private String[] dayArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    //private static final String TAG = "mainActivity";

    private User user;

    private Button fitBtn;
    private Button setGoal;
    private Button add500;
    private boolean start = false;
    private TextView textSteps;
    private TextView goalText;
    private TextView activeText;
    private TextView velocity;
    private EditText goal;
    private Toolbar mToolbar;

    private int subGoal;
    private int totalSteps = 0;
    private int activeSteps = 0;
    private int oldActive = 0;
    private int totalActiveSteps = 0;
    private int oldTotal = 0;
    private int counter = 0;
    private long timeCounter = 0;
    private boolean goalMessageFirstAppearance = true;
    private Congratulations congratsMessage;
    private AlertDialog goalReached;
    private String oldDay;

    public static int REQ_CODE = 233;
    private UpdateAsyncPassiveCount passiveRunner;

    private FirebaseFirestore acctFirebase;
    private CollectionReference acctCollection;
    private GoogleSignInAccount gsa;

    private String dayOfTheWeek;
    private FitnessService fitnessService;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private SimpleDateFormat sdf;
    private String id;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("PB", Context.MODE_PRIVATE);
        editor = prefs.edit();

        FirebaseApp.initializeApp(MainActivity.this);
        acctFirebase = FirebaseFirestore.getInstance();
        acctCollection = acctFirebase.collection("users");

        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });
        // create google fit adapter
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);
        gsa = GoogleSignIn.getLastSignedInAccount(this);

        id = gsa.getId();
        if(GoogleSignIn.getLastSignedInAccount(MainActivity.this) == null) {
            Log.d("NULL_USER", "Null user");
        } else if(id == null) {
            Log.d("USER_ID_CHECK", "Null ID");
        } else {
            Log.d("USER_ID_CHECK", "Not null ID");
        }

        initializeUser();

        //Get the date
        sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        // go to set up screen
        Intent setup = new Intent(MainActivity.this, SetupActivity.class);
        startActivityForResult(setup, REQ_CODE);

        // Defines UI elements by resource id
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // set goal text
        goalText = findViewById(R.id.goalText);

        // set step count text
        textSteps = findViewById(R.id.textSteps);

        // set active step count text
        activeText = findViewById(R.id.activeText);
        activeSteps = prefs.getInt(ACTIVE_KEY, 0);

        // find buttons and velocity text
        velocity = findViewById(R.id.velocity);
        fitBtn = findViewById(R.id.startWalk);
        setGoal = findViewById(R.id.newGoal);
        add500 = findViewById(R.id.add500);

        // goal congratulation objects
        congratsMessage = new Congratulations(this);
        goalReached = congratsMessage.onCreateAskGoal(savedInstanceState);

        //Set on click listeners for various buttons on main activity
        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog setCustomGoal =  congratsMessage.onCreateCustomGoal(savedInstanceState);
                setCustomGoal.show();
            }
        });
        add500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStepCount(totalSteps+=500);
                Log.d("USER_ID_CHECK", id);
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
                    Toast.makeText(MainActivity.this, "Good Job!", Toast.LENGTH_SHORT).show();

                    user.addActiveSteps(activeSteps);
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
        if (id == R.id.action_progress) {
            Intent prog = new Intent(MainActivity.this, GraphActivity.class);
            int[] active_steps = new int[7];
            int[] passive_steps = new int[7];

            /* Populate int arrays from SharedPreferences */
            for(int i = 0; i < active_steps.length; i++){
                active_steps[i] = prefs.getInt(dayArray[i]+"Active",0);
                passive_steps[i] = prefs.getInt(dayArray[i]+"Passive",0);

            }

            prog.putExtra("ACTIVE_STEPS", active_steps);
            prog.putExtra("PASSIVE_STEPS", passive_steps);
            prog.putExtra("CURRENT_GOAL", prefs.getInt("goal", 5000));
            startActivity(prog);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                initializeUser();

            }
            initializeUiValues();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // async runner to constantly update steps
                            passiveRunner = new UpdateAsyncPassiveCount();
                            passiveRunner.execute();
                        }
                    });
                }
            });
        }
    }

    public void initializeUser() {
        resetActiveSteps();
        int height = prefs.getInt("height", 0);
        int currentGoal = prefs.getInt("goal", 5000);
        int currentSteps = prefs.getInt(PASSIVE_KEY, 0);

        id = gsa.getId();
        Log.d("USER_ID_CHECK", "Not null ID in initializeUser");
        user = new User(id, height, currentGoal, currentSteps, prefs);

        updateDatabase();
    }

    public void initializeUiValues() {
        int steps = user.getSteps();
        int goal = user.getCurrentGoal();
        goalText.setText("Goal: " + goal + " steps");
        textSteps.setText(Integer.toString(steps));
        activeText.setText("Active Steps: " + Integer.toString(activeSteps));
        totalSteps = user.getSteps();

        subGoal = ((totalSteps/500)+1)*500;
    }

    public void updateDatabase() {
        acctFirebase.collection("users").document("user_" + id).set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Firebase", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public void setStepCount(long stepCount) {
        if(oldTotal != totalSteps) {
            textSteps.setText(String.valueOf(stepCount));
            user.setSteps(stepCount);
            updateDatabase();
            setActiveSteps();
            updateWeek();
            oldTotal = totalSteps;

            if(goalMessageFirstAppearance == true) {
                checkGoal();
            }
        }
    }

    public void checkGoal() {
        int goal = user.getCurrentGoal();
        if(totalSteps >= goal) {
            goalMessageFirstAppearance = false;
            goalReached.show();
        }
        if(totalSteps >= subGoal && totalSteps < goal){
            Toast.makeText(MainActivity.this, "You’ve increased your daily steps by over 500 steps. Keep up the good work!", Toast.LENGTH_LONG).show();
            subGoal = ((totalSteps/500)+1)*500;
        }
    }

    public void updateWeek(){
        editor.putInt(dayOfTheWeek+"Passive", totalSteps);
        editor.putInt(dayOfTheWeek+"Active", activeSteps);
        editor.apply();
    }

    public void setActiveSteps(){
        if(start == true){
            activeSteps = totalSteps - counter;

            if(oldActive != activeSteps) {
                String printTotal = "Active Steps: " + Integer.toString(prefs.getInt(ACTIVE_KEY, 0) + activeSteps);
                double stride = user.getHeight() *.413/12/5280;
                System.out.println(stride);
                activeText.setText(printTotal);

                double timeElapsed = ((double) System.nanoTime() - timeCounter) / 1000000000.0/60/60;

                velocity.setText(calculateSpeed(stride, timeElapsed) + " MPH");
                oldActive = activeSteps;
            }
        }
    }

    public double calculateSpeed(double stride, double timeElapsed) {
        double mph = activeSteps*stride / timeElapsed;

        mph *= 1000;
        mph = (int)mph;
        mph = mph/1000;

        return mph;
    }

    public void resetActiveSteps(){
        oldDay = prefs.getString("DOW","");
        if(dayOfTheWeek != oldDay) {
            activeSteps = 0;
            editor.putInt(ACTIVE_KEY, activeSteps);
            oldDay = dayOfTheWeek;
            editor.putString("DOW", oldDay);

            editor.apply();
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
            int goal = user.getCurrentGoal();
            if(totalSteps >= goal) {
                goalMessageFirstAppearance = false;
                goalReached.show();
            }
            if(totalSteps >= subGoal && totalSteps < goal){
                Toast.makeText(MainActivity.this, "You’ve increased your daily steps by over 500 steps. Keep up the good work!", Toast.LENGTH_LONG).show();
                subGoal = ((totalSteps/500)+1)*500;
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
        changeGoal(goal+500);

    }

    public void saveCustomGoal(View v) {
        goal = (EditText)v.findViewById(R.id.custom_goal);
        int stepsGoal = Integer.parseInt(goal.getText().toString());
        changeGoal(stepsGoal);
    }

    public void changeGoal(int newGoal) {
        goalText.setText("Goal: " + newGoal + " steps");
        user.setGoal(newGoal);
        goalMessageFirstAppearance = true;
        notifyGoalChanged();
    }

    public void notifyGoalChanged() {
        Toast.makeText(MainActivity.this, "Saved Goal", Toast.LENGTH_SHORT).show();
        updateDatabase();
    }

}
