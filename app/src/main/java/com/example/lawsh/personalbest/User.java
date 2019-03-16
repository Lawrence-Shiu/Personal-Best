package com.example.lawsh.personalbest;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.util.Log;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.example.lawsh.personalbest.adapters.Observer;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.google.j2objc.annotations.ObjectiveCName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User implements Subject {
    private static final User user = new User();
    private String id;
    private String email;
    private int height; //in inches
    private int stepsTaken;
    private int totalActiveSteps;
    private int currentGoal;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private int[] passive_steps;
    private int[] active_steps;

    private Set<String> friends;
    private Set<String> pendingFriends = new HashSet<>();

    private String ACTIVE_KEY = "ACTIVE_STEPS";
    private String PASSIVE_KEY = "PASSIVE_KEY";

    private FirebaseFirestore acctFirebase;
    private FirestoreAdapter fAdapter = FirestoreAdapter.getInstance(false, null);

    //other functionality?
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer ob) {
        observers.add(ob);
    }

    @Override
    public void removeObserver(Observer ob) {
        observers.remove(ob);
    }

    @Override
    public void notifyObserver(){
        for(Observer ob: observers){
            ob.update(friends, pendingFriends);
        }
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

    public void setRecentActivity(int[] passive_steps, int[] active_steps) {
        this.passive_steps = passive_steps;
        this.active_steps = active_steps;
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
        friends.remove("");
       // editor.putStringSet("friends", friends).apply();
        fAdapter.updateDatabase(user.getEmail(),user.toMap(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyObserver();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });

        Map<String, Object> map = fAdapter.getMap(friend,0);
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
        //editor.remove(friend).apply(); //
       // editor.putStringSet("friends", friends).apply();
        fAdapter.getMap(friend,0).put("friends", friends);
        fAdapter.updateDatabase(user.getEmail(),user.toMap(), new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyObserver();
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
        //map.put("pendingFriends", pendingFriends.toString());

        for(int i = 0; i < 30; i++) {
            map.put(i + PASSIVE_KEY, passive_steps[i]);
            map.put(i + ACTIVE_KEY, active_steps[i]);
        }

        return map;
    }

    public Set<String> getFriends(){
        Map<String, Object> map = fAdapter.getMap(email,0);
        String f = (String)map.get("friends");
        if(f == null)
            Log.d("PendingFriendsActivity","map.get(\"friends\") is null");
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
        //editor.remove(friend).apply();
        Map<String, Object> map = fAdapter.getMap(friend,1);
        pendingFriends.remove(friend);
       // editor.putStringSet("pending_friends", friends).apply();
        map.put("pendingFriends", pendingFriends.toString());


        fAdapter.updatePending(user.getEmail(), map, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyObserver();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public void pendFriend(String friendEmail){
        Map<String, Object> map = fAdapter.getMap(friendEmail, 1);
        String str = (String)map.get("pendingFriends");
        String[] s;
        if(str == null) {
            s = new String[0];
        }
        else {
            str = str.substring(1, str.length() - 1);
            //Log.d("PendingFriendActivity", );
            s = str.split(",");

        }
        List<String> tempPend = new ArrayList<>();
        for(String fr: s) {
            tempPend.add(fr.trim());
            Log.d("PendingFriendActivity", fr.trim());
        }
        tempPend.add(email);
        if(tempPend.get(0) == "")
            tempPend.remove(0);
        map.put("pendingFriends", tempPend.toString());

        Log.d("PendingFriendActivity", friendEmail + ", " + map.toString());
        fAdapter.updatePending(friendEmail, map, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyObserver();
                //Log.d("PendingFriendActivity", friendEmail + ", " + map.toString());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public Set<String> getPendingFriends(){

        Map<String, Object> map = fAdapter.getMap(email, 1);
        String f = (String)map.get("pendingFriends");
        if(f == null)
            return new HashSet<>();
        f = f.substring(1,f.length()-1);
        String[] fr = f.split(",");
        List<String> fList = new ArrayList<>();
        for(String str: fr){
            fList.add(str.trim());
        }
        Log.d("PendingFriendActivity", fList.toString());
        //for(String str: fList)

            //Log.d("PendingFriendActivity", str);

        //pendingFriends.add(str);
        pendingFriends.clear();
        pendingFriends.addAll(fList);

        return pendingFriends;
    }

    public void setPendingFriends(Set<String> pendingFriends){
        this.pendingFriends = pendingFriends;
    }

}
