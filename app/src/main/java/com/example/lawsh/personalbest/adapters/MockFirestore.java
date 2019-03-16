package com.example.lawsh.personalbest.adapters;

import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class MockFirestore implements IDatabase {
    Map<String, Object> map;

    public MockFirestore(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public void updateDatabase(User user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        //stubbed
    }

    public Map<String, Object> getMap(String email, int path) {
        return map;
    }
}
