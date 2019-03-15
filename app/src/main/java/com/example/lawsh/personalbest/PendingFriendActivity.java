package com.example.lawsh.personalbest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Set;

public class PendingFriendActivity extends AppCompatActivity implements FriendAdapter.ItemClickListener{

    FriendAdapter adapter;
    Button rejectBtn;
    ArrayList<String> pendingFriends;
    RecyclerView recyclerView;
    boolean deleteTrue = false;
    User user = User.getInstance();
    FirestoreAdapter fAdapter;
    FirebaseFirestore fbase;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    String email;
    String id;

    private static final String TAG = "friendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_friend);

        // data to populate the RecyclerView with
        pendingFriends = new ArrayList<>();
        fAdapter = FirestoreAdapter.getInstance(false, null);
        fbase = fAdapter.getFirestoreInstance();

        // set up buttons
        rejectBtn = findViewById(R.id.reject);


        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteTrue == false) {
                    deleteTrue = true;
                    rejectBtn.setText("Rejecting Friends");
                    rejectBtn.setBackgroundColor(Color.parseColor("#F703D6"));
                }else{
                    deleteTrue = false;
                    rejectBtn.setText("Reject Friend");
                    rejectBtn.setBackgroundColor(Color.parseColor("#E91E63"));
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.my_recycler_view_pf);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(this, pendingFriends);
        adapter.setClickListener(this);
        show();
    }

    public void show(){
        Set f = user.getPendingFriends();
        pendingFriends.clear();
        pendingFriends.addAll(f);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(deleteTrue == false) {
            addFriend(position);
        }else{
            removeFriend(position);
        }
        show();
    }

    public void removeFriend(int position){
        String friend = adapter.getItem(position);
        user.removePendingFriend(friend);
        pendingFriends.remove(position);
        show();
    }

    public void addFriend(int position){
        String friend = adapter.getItem(position);
        user.addFriend(friend);
        user.removePendingFriend(friend);
        pendingFriends.remove(position);
        show();
    }

}
