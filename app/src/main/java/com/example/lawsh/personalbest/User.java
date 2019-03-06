package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String email;
    private int height; //in inches
    private int stepsTaken;
    private int totalActiveSteps;
    private int currentGoal;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String ACTIVE_KEY = "ACTIVE_STEPS";
    private String PASSIVE_KEY = "PASSIVE_KEY";

    //other functionality?

    public User(String id, String email, int height, int currentGoal, int stepsTaken, SharedPreferences pref) {
        this.id = id;
        this.email = email;
        this.height = height;
        this.currentGoal = currentGoal;
        this.stepsTaken = stepsTaken;
        this.pref = pref;
        this.editor = pref.edit();
    }

    public void setId(String id) {
        this.id = id;
        Log.d("USER_ID", id);
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentGoal() {
        return currentGoal;
    }

    public void setHeight(int height) {
        this.height = height;
        editor.putInt("height", this.height).apply();
    }

    public void setGoal(int newGoal) {
        this.currentGoal = newGoal;
        editor.putInt("goal", this.currentGoal).apply();
    }

    public int getSteps() {
        return stepsTaken;
    }

    public void setSteps (long newSteps) {
        stepsTaken = (int) newSteps;
        editor.putInt(PASSIVE_KEY, stepsTaken);
        editor.apply();
    }

    public void addActiveSteps(int newActiveSteps) {
        totalActiveSteps = pref.getInt(ACTIVE_KEY, 0);
        totalActiveSteps += newActiveSteps;
        editor.putInt(ACTIVE_KEY, totalActiveSteps);
        editor.apply();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("email", email);
        map.put("height", height);
        map.put("stepsTaken", stepsTaken);
        map.put("currentGoal", currentGoal);
        map.put("activeSteps", totalActiveSteps);

        return map;
    }
}
