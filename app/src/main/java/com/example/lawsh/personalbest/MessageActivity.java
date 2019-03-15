package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lawsh.personalbest.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    //String TAG = this.class.getSimpleName();

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY;
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";
    public SharedPreferences sharedpreferences;
    IDB chat;
    String from;
    String friendID;
    private Intent intent;
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        buildDocKey();
        sharedpreferences = getSharedPreferences("FirebaseMessaging", Context.MODE_PRIVATE);

        from = sharedpreferences.getString(FROM_KEY, null);
        FirebaseApp.initializeApp(this);
        chat = new FBAdapter(FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY));

        subscribeToNotificationsTopic(); // not sure if in right place

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
        findViewById(R.id.back_btn).setOnClickListener(view -> finish());

        TextView nameView = findViewById((R.id.user_name));
        from = user.getId();
        nameView.setText(friendID);
        sharedpreferences.edit().putString(FROM_KEY, from).apply();
    }

    private void sendMessage() {
        EditText messageView = findViewById(R.id.text_message);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        chat.add(messageView,newMessage);
    }

    private void initMessageUpdateListener() {
        chat.show(findViewById(R.id.chat));
    }

    private void subscribeToNotificationsTopic() {
        chat.subscribe(this, DOCUMENT_KEY);
    }

    public void buildDocKey() {
        String id = user.getId();
        friendID = intent.getStringExtra("friend_email");
        if(id.compareTo(friendID) > 0) {
            DOCUMENT_KEY = friendID + "%" + id;
        } else {
            DOCUMENT_KEY = id + "%" + friendID;
        }
    }
}
