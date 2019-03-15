package com.example.lawsh.personalbest.adapters;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lawsh.personalbest.OnGetDataListener;
import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FirestoreAdapter {

    private static FirestoreAdapter fireStoreAdapter = new FirestoreAdapter();

    private FirebaseFirestore fstore;

    private static final String TAG = "PendingFriendActivity";

    Map<String,Object> map;
    QuerySnapshot[] qs = new QuerySnapshot[2];

    private FirestoreAdapter(){

    }



    public static FirestoreAdapter getInstance(boolean testing, FirebaseFirestore fstore){
        if (testing)
        {
            FirestoreAdapter testFirestoreAdapter = new FirestoreAdapter();
            testFirestoreAdapter.fstore = fstore;
            return testFirestoreAdapter;
        }

        if (fireStoreAdapter.fstore == null){
            fireStoreAdapter.fstore = FirebaseFirestore.getInstance();
        }
        return fireStoreAdapter;
    }

    public void updateDatabase(String email, Map<String,Object> map, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        fstore.collection("users").document(email).set(map).addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updatePending(String email, Map<String,Object> map, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        fstore.collection("requests").document(email).set(map).addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public FirebaseFirestore getFirestoreInstance() {
        return fstore;
    }

    public void getDatabase(String path, ProgressDialog progressDialog, int index){
        CountDownLatch done = new CountDownLatch(1);
        fstore.collection(path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            qs[index] =  task.getResult();
                            progressDialog.dismiss();
                            Log.d("PendingFriendActivity", "waiting");
                            done.countDown();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        /*
        //while(!(Thread.interrupted())){
            try{
                Log.d("PendingFriendActivity", "done waiting");

                done.await();
            }catch(InterruptedException e){
                Log.d("PendingFriendActivity", "thread interupted");
                Thread.currentThread().interrupt();
            }
        //}*/

    }

    public Map<String, Object> getMap(String email, int path){
        map = new HashMap<>();
        for (QueryDocumentSnapshot document : qs[path]) {
            Log.d(TAG, document.getId() + " user id: " + email + " => " + document.getData());
            if(email.equals(document.getId())){
                map = document.getData();
                Log.d(TAG, document.getId() + " user id: " + email + " => " + document.getData());
            }
        }
        return map;
    }

    public void readData(final OnGetDataListener listener) {
        listener.onStart();
        fstore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //qs = task.getResult();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
