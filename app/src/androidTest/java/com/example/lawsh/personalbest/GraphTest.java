package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.lawsh.personalbest.adapters.IDatabase;
import com.example.lawsh.personalbest.adapters.MockFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static android.content.Context.MODE_PRIVATE;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GraphTest {
    private final int DAYS = 7;
    private final String ACTIVE_KEY = "ACTIVE_STEPS";
    private final String PASSIVE_KEY = "PASSIVE_STEPS";
    SharedPreferences pref;

    private int[] active_steps = {1400, 300, 400, 1600, 1000, 1000, 3000};
    private int[] passive_steps = {1600, 1400, 1000, 2000, 5400, 4000, 9000};
    private int current_goal = 5000;

    @Rule
    public final IntentsTestRule<GraphActivity> gActivityRule =
            new IntentsTestRule<>(GraphActivity.class, true, false);

    @Rule
    public final IntentsTestRule<MessageActivity> mActivityRule =
            new IntentsTestRule<>(MessageActivity.class, true, false);

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
        gActivityRule.launchActivity(result);
    }
/*
    @Test
    public void messageActivity() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent result = new Intent(targetContext, MessageActivity.class);

        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < 30; i++) {
            map.put(i + ACTIVE_KEY, active_steps[i % 7]);
            map.put(i + PASSIVE_KEY, active_steps[i % 7]);
        }
        IDatabase mock = new MockFirestore(map);
        mActivityRule.getActivity().showFriendProgress(map, "asdf");
    }
    */
}
