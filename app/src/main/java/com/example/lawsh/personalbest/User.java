package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.provider.DocumentsContract;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User implements Serializable {
    private String id;
    private String email;
    private int height; //in inches
    private int stepsTaken;
    private int totalActiveSteps;
    private int currentGoal;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Set<String> friends;

    private String ACTIVE_KEY = "ACTIVE_STEPS";
    private String PASSIVE_KEY = "PASSIVE_KEY";

    private FirebaseFirestore acctFirebase;

    //other functionality?

    public User(String id, String email, int height, int currentGoal, int stepsTaken, SharedPreferences pref, Set<String> friends) {
        this.id = id;
        this.email = email;
        this.height = height;
        this.currentGoal = currentGoal;
        this.stepsTaken = stepsTaken;
        this.pref = pref;
        this.editor = pref.edit();
        this.friends = friends;
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

    public String getEmail() {
        return email;
    }

    public void addFriend(User friend) {
        friends.add(friend.getEmail());
        editor.putStringSet("friends", friends).apply();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("email", email);
        map.put("height", height);
        map.put("stepsTaken", stepsTaken);
        map.put("currentGoal", currentGoal);
        map.put("activeSteps", totalActiveSteps);
        map.put("friends", friends.toString());

        return map;
    }

    public void addFirebase(FirebaseFirestore firebase){
        this.acctFirebase = firebase;
    }
}
