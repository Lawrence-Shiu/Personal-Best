package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.provider.DocumentsContract;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User{
    private static final User user = new User();
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

    //default constructor
    private User() {
    }

    /* private User(String id, String email, int height, int currentGoal, int stepsTaken, SharedPreferences pref, Set<String> friends) {
        this.id = id;
        this.email = email;
        this.height = height;
        this.currentGoal = currentGoal;
        this.stepsTaken = stepsTaken;
        this.pref = pref;
        this.editor = pref.edit();
        this.friends = friends;
    }*/

    public static User getInstance(){
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getCurrentGoal() {
        return currentGoal;
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

    public void setEmail(String email){
        this.email = email;
    }

    public void setFriends(Set<String> friends){
        this.friends = friends;
    }

    public void addFriend(String friend) {
        friends.add(friend);
        editor.putStringSet("friends", friends).apply();
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
        editor.remove(friend).apply(); //putStringSet("friends", friends).apply();
    }

    public void setPref(SharedPreferences pref){

        this.pref = pref;
        this.editor = pref.edit();
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

}
