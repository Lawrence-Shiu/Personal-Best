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

    public void updateDatabase(User user) {
        fstore.collection("users").document(user.getId()).set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public FirebaseFirestore getFirestoreInstance() {
        return fstore;
    }
}
