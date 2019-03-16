package com.example.lawsh.personalbest.adapters;

import com.example.lawsh.personalbest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public interface IDatabase {
    void updateDatabase(String email, Map<String,Object> map, OnSuccessListener<Void> successListener, OnFailureListener failureListener);
    Map<String, Object> getMap(String id, int path);
}
