package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.provider.DocumentsContract;
import android.util.Log;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private Set<String> pendingFriends;

    private String ACTIVE_KEY = "ACTIVE_STEPS";
    private String PASSIVE_KEY = "PASSIVE_KEY";

    private FirebaseFirestore acctFirebase;
    private FirestoreAdapter fAdapter = FirestoreAdapter.getInstance(false, null);

    //other functionality?

    public enum Fields{
        ID, EMAIL, HEIGHT, CURRENTGOAL, STEPSTAKE, PREF, EDITOR, FRIENDS
    }
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
        fAdapter.updateDatabase(user.getEmail(),user.toMap(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });

        Map<String, Object> map = fAdapter.getMap(friend);
        map.put("friends", email);

        fAdapter.updateDatabase(friend,map,new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
        editor.remove(friend).apply(); //putStringSet("friends", friends).apply();
        fAdapter.updateDatabase(user.getEmail(),user.toMap(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
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
        map.put("pendingFriends", pendingFriends.toString());

        return map;
    }

    public Set<String> getFriends(){
        Map<String, Object> map = fAdapter.getMap(email);
        String f = (String)map.get("friends");
        f = f.substring(1,f.length()-1);
        String[] fr = f.split(",");
        List<String> fList = new ArrayList<>();
        for(String str: fr){
            fList.add(str.trim());
        }
        friends.addAll(fList);
        return friends;
    }

    public void removePendingFriend(String friend) {
        editor.remove(friend).apply();
        pendingFriends.remove(friend);

        fAdapter.updateDatabase(user.getEmail(),user.toMap(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public void pendFriend(String friendEmail){
        editor.putStringSet("pending_friends", friends).apply();
        Map<String, Object> map = fAdapter.getMap(friendEmail);
        map.put("pendingFriends", email);

        Log.d("PendingFriendActivity", friendEmail + ", " + map.toString());
        fAdapter.updateDatabase(friendEmail,map, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("PendingFriendActivity", friendEmail + ", " + map.toString());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public Set<String> getPendingFriends(){
        Map<String, Object> map = fAdapter.getMap(email);
        String f = (String)map.get("pendingFriends");
        f = f.substring(1,f.length()-1);
        String[] fr = f.split(",");
        List<String> fList = new ArrayList<>();
        for(String str: fr){
            fList.add(str.trim());
        }
        pendingFriends.addAll(fList);
        return pendingFriends;
    }

    public void setPendingFriends(Set<String> pendingFriends){
        this.pendingFriends = pendingFriends;
    }

}
