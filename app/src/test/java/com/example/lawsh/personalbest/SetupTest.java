package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class SetupTest {

    private SetupActivity setupActivity;
    private EditText feetText;
    private EditText inchesText;
    private Button doneBtn;
    private SharedPreferences pref = RuntimeEnvironment.application.getSharedPreferences("height", Context.MODE_PRIVATE);

    @Before
    public void setup() {
        pref.edit().remove("height");
        setupActivity = Robolectric.setupActivity(SetupActivity.class);
        feetText = setupActivity.findViewById(R.id.feet_input);
        inchesText = setupActivity.findViewById(R.id.in_input);
        doneBtn = setupActivity.findViewById(R.id.done_button);
    }

    @Test
    public void testInitialStartup() {
        Assert.assertEquals(pref.getInt("height", 0), 0);
        feetText.setText("6");
        inchesText.setText("5");
        doneBtn.performClick();

        Assert.assertEquals(pref.getInt("height", 0), 77);
    }

    @Test
    public void testNotInitialStartup() {
        pref.edit().putInt("height", 77).apply();
        Assert.assertNotEquals(pref.getInt("height", 0), 0);
    }

    @Test
    public void testFeetTooTall() {
        Assert.assertEquals(pref.getInt("height", 0), 0);
        feetText.setText("8");
        inchesText.setText("5");
        doneBtn.performClick();

        String lastToast = ShadowToast.getTextOfLatestToast();
        Assert.assertNotEquals(lastToast, null);
        Assert.assertEquals(pref.getInt("height", 0), 0);
    }

    @Test
    public void testInchesInvalid() {
        Assert.assertEquals(pref.getInt("height", 0), 0);
        feetText.setText("6");
        inchesText.setText("13");
        doneBtn.performClick();

        String lastToast = ShadowToast.getTextOfLatestToast();
        Assert.assertNotEquals(lastToast, null);
        Assert.assertEquals(pref.getInt("height", 0), 0);
    }

    @Test
    public void testNoFeetEntry() {
        Assert.assertEquals(pref.getInt("height", 0), 0);
        feetText.setText("");
        inchesText.setText("5");
        doneBtn.performClick();

        String lastToast = ShadowToast.getTextOfLatestToast();
        Assert.assertNotEquals(lastToast, null);
        Assert.assertEquals(pref.getInt("height", 0), 0);
    }

    @Test
    public void testNoInchesEntry() {
        Assert.assertEquals(pref.getInt("height", 0), 0);
        feetText.setText("6");
        inchesText.setText("");
        doneBtn.performClick();

        String lastToast = ShadowToast.getTextOfLatestToast();
        Assert.assertNotEquals(lastToast, null);
        Assert.assertEquals(pref.getInt("height", 0), 0);
    }
}
