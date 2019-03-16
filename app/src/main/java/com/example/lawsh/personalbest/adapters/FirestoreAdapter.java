package com.example.lawsh.personalbest.adapters;

import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreAdapter {

    private static FirestoreAdapter fireStoreAdapter = new FirestoreAdapter();

    private FirebaseFirestore fstore;


    private static final String TAG = "PendingFriendActivity";

    Map<String,Object> map;
    QuerySnapshot[] qs = new QuerySnapshot[2];


    private FirestoreAdapter() {

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

    public void updateDatabase(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        fstore.collection("users").document(user.getId()).set(user.toMap()).addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public FirebaseFirestore getFirestoreInstance() {
        return fstore;
    }
}
