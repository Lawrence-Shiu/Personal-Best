package com.example.lawsh.personalbest;

import android.app.ProgressDialog;
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
import com.example.lawsh.personalbest.adapters.Observer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class PendingFriendActivity extends AppCompatActivity implements FriendAdapter.ItemClickListener, Observer {

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
    ProgressDialog mProgress;
    FirestoreAdapter acctFirebase = FirestoreAdapter.getInstance(false, null);


    private static final String TAG = "friendActivity";

    @Override
    public void update(Set<String> friends, Set<String> pendingFriends){
        /*
        this.pendingFriends.clear();
        this.pendingFriends.addAll(friends);
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);

        // update user database
        mProgress.show();
        FirestoreAdapter acctFirebase = FirestoreAdapter.getInstance(false, null);
        acctFirebase.getDatabase("users", mProgress, 0);


        user.addObserver(this);
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
                mProgress.show();
                acctFirebase.getDatabase("users", mProgress, 0);
                mProgress.show();
                acctFirebase.getDatabase("requests", mProgress, 1);
                finish();
            }
        });

        Set f = user.getPendingFriends();
        pendingFriends.clear();
        pendingFriends.addAll(f);
        Collections.sort(pendingFriends);
        recyclerView = findViewById(R.id.my_recycler_view_pf);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(this, pendingFriends);
        adapter.setClickListener(this);
        show();
    }

    public void show(){
        //Set f = user.getPendingFriends();
        //pendingFriends.clear();
        //pendingFriends.addAll(f);
        for(String p: pendingFriends){
            Log.d("PendingFriend", p);
        }
        Collections.sort(pendingFriends);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(deleteTrue == false) {
            addFriend(position);
        }else{
            removeFriend(position);
        }
    }

    public void removeFriend(int position){
        String friend = adapter.getItem(position);
        user.removePendingFriend(friend);
        pendingFriends.remove(position);
        show();
    }

    public void addFriend(int position){
        String friend = adapter.getItem(position);
        pendingFriends.remove(position);
        user.addFriend(friend);
        user.removePendingFriend(friend);
        show();
    }

}
