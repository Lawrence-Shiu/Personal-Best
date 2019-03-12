package com.example.lawsh.personalbest.adapters;

import android.util.Log;

import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FirestoreAdapter implements IDatabase {

    private FirebaseFirestore fstoreInstance;

    public static volatile FirestoreAdapter fstoreSingleton = new FirestoreAdapter(FirebaseFirestore.getInstance());

    public static FirestoreAdapter getInstance() {
        return fstoreSingleton;
    }

    private FirestoreAdapter(FirebaseFirestore fstore) {
        this.fstoreInstance = fstore;
    }

    @Override
    public void updateDatabase(User user) {
        fstoreInstance.collection("users").document(user.getId()).set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Firebase", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.w("Firebase", "Error writing document", e);
            }
        });
    }

    public FirebaseFirestore getCollectionReference() {
        return fstoreInstance;
    }
}
