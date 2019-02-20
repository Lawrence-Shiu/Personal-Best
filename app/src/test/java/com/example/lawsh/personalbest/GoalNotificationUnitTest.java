package com.example.lawsh.personalbest;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;
import com.example.lawsh.personalbest.fitness.FitnessService;
import com.example.lawsh.personalbest.fitness.FitnessServiceFactory;
import com.example.lawsh.personalbest.fitness.GoogleFitAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)

@Config(sdk = 21, manifest = "AndroidManifest.xml")
public class GoalNotificationUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView currentGoal;
    private TextView textSteps;
    private TextView stepsLabel;
    private Button btnUpdateSteps;
    private long nextStepCount;

    @Before
    public void setUp() throws Exception {
        /*
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity);
            }
        });*/


        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        currentGoal = activity.findViewById(R.id.goalText);
        textSteps = activity.findViewById(R.id.textSteps);
        stepsLabel = activity.findViewById(R.id.stepsLabel);
        btnUpdateSteps = activity.findViewById(R.id.add500);

        Robolectric.flushBackgroundThreadScheduler();
        ShadowApplication.runBackgroundTasks();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubGoalReached() {

        assertEquals("Steps taken today", stepsLabel.getText().toString());
        textSteps.setText("4000");
        currentGoal.setText("5000");
        btnUpdateSteps.performClick();
        btnUpdateSteps.performClick();
        assertEquals("You’ve increased your daily steps by over 500 steps. Keep up the good work!", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void testSubGoalNotReached() {
        textSteps.setText("3500");
        currentGoal.setText("5000");
        btnUpdateSteps.performClick();
        assertNull(ShadowToast.getTextOfLatestToast());
    }
}