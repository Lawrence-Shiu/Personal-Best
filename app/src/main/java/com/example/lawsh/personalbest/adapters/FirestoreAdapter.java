package com.example.lawsh.personalbest.adapters;

import android.util.Log;

import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreAdapter {

    private FirebaseFirestore fstore;

    public FirestoreAdapter(FirebaseFirestore fstore) {
        this.fstore = fstore;
    }

    public void updateDatabase(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        fstore.collection("users").document(user.getId()).set(user.toMap()).addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public FirebaseFirestore getFirestoreInstance() {
        return fstore;
    }
}
