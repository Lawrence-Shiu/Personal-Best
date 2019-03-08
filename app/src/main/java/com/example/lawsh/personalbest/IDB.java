package com.example.lawsh.personalbest;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public interface IDB {
    void add(EditText msgView, Map<String, String> newMsg);
    void show(TextView chatView);
    void subscribe(Context context, String topicID);
    String getTopicID();
}