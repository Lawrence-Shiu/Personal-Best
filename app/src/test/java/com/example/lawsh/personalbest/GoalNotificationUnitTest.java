package com.example.lawsh.personalbest;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.shadows.ShadowToast;
import com.example.lawsh.personalbest.fitness.FitnessService;
import com.example.lawsh.personalbest.fitness.FitnessServiceFactory;
import com.example.lawsh.personalbest.fitness.GoogleFitAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 17, manifest = "AndroidManifest.xml")
public class GoalNotificationUnitTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView textSteps;
    private Button btnUpdateSteps;
    private long nextStepCount;

    @Before
    public void setUp() throws Exception {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity);
            }
        });


        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        textSteps = activity.findViewById(R.id.textSteps);
        //btnUpdateSteps = activity.findViewById(R.id.buttonUpdateSteps);
        nextStepCount = 4999;
    }
/*
    @Test
    public void testUpdateStepsButton() {
        assertEquals("steps will be shown here", textSteps.getText().toString());
        btnUpdateSteps.performClick();
        assertEquals(String.valueOf(nextStepCount), textSteps.getText().toString());
    }
*/
    @Test
    public void testCongrats() {
        nextStepCount = 5000;
        //btnUpdateSteps.performClick();
        //assertEquals("2000", textSteps.getText().toString());
        //assertEquals("Good job! You're already at 20% of the daily recommended number of steps.", ShadowToast.getTextOfLatestToast());
    }
    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;

        public TestFitnessService(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            mainActivity.setStepCount(nextStepCount);
        }
    }
}

