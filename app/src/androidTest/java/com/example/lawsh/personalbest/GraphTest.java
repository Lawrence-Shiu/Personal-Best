package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import static android.content.Context.MODE_PRIVATE;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GraphTest {
    private final int DAYS = 7;
    private final String ACTIVE_KEY[] = {"ACTIVE_STEPS", "ACTIVE_STEPS_1", "ACTIVE_STEPS_2",
            "ACTIVE_STEPS_3", "ACTIVE_STEPS_4", "ACTIVE_STEPS_5", "ACTIVE_STEPS_6"};
    private final String PASSIVE_KEY[] = {"PASSIVE_STEPS", "PASSIVE_STEPS_1", "PASSIVE_STEPS_2",
            "PASSIVE_STEPS_3", "PASSIVE_STEPS_4", "PASSIVE_STEPS_5", "PASSIVE_STEPS_6"};
    SharedPreferences pref;

    private int[] active_steps = {1400, 2500, 400, 1600, 5000, 4000, 3000};
    private int[] passive_steps = {600, 400, 1000, 2000, 1400, 4000, 9000};
    private int current_goal = 5000;

    @Rule
    public final IntentsTestRule<GraphActivity> mActivityRule =
            new IntentsTestRule<>(GraphActivity.class, true, false);

    @Before
    public void setUp() {

    }

    @Test
    public void graphActivity() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent result = new Intent(targetContext, GraphActivity.class);
        result.putExtra("ACTIVE_STEPS", active_steps);
        result.putExtra("PASSIVE_STEPS", passive_steps);
        result.putExtra("CURRENT_GOAL", current_goal);
        mActivityRule.launchActivity(result);
    }
}
