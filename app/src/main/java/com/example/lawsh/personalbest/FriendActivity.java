package com.example.lawsh.personalbest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.example.lawsh.personalbest.adapters.Observer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendActivity extends AppCompatActivity implements FriendAdapter.ItemClickListener, Observer {

    FriendAdapter adapter;
    Button addFriendBtn;
    Button rmFriendBtn;
    Button pendBtn;
    ArrayList<String> friends;
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
        this.friends.clear();
        this.friends.addAll(friends);
        recyclerView.setAdapter(adapter);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);

        // update pending friends database
        mProgress.show();
        FirestoreAdapter acctFirebase = FirestoreAdapter.getInstance(false, null);
        acctFirebase.getDatabase("requests", mProgress, 0);


        user.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // data to populate the RecyclerView with
        friends = new ArrayList<>();
        fAdapter = FirestoreAdapter.getInstance(false, null);
        fbase = fAdapter.getFirestoreInstance();

        // set up buttons
        addFriendBtn = findViewById(R.id.addFriend);
        rmFriendBtn = findViewById(R.id.deleteFriend);
        pendBtn = findViewById(R.id.pendFriend);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateAddFriend().show();
            }
        });

        rmFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteTrue == false) {
                    deleteTrue = true;
                    rmFriendBtn.setText("Removing Friends");
                    rmFriendBtn.setBackgroundColor(Color.parseColor("#F703D6"));
                }else{
                    deleteTrue = false;
                    rmFriendBtn.setText("Remove Friend");
                    rmFriendBtn.setBackgroundColor(Color.parseColor("#E91E63"));
                }
            }
        });

        pendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                acctFirebase.getDatabase("users", mProgress, 0);
                mProgress.show();
                acctFirebase.getDatabase("requests", mProgress, 1);
                Intent pendActivity = new Intent(FriendActivity.this, PendingFriendActivity.class);
                startActivity(pendActivity);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                FirestoreAdapter acctFirebase = FirestoreAdapter.getInstance(false, null);
                acctFirebase.getDatabase("users", mProgress, 0);
                mProgress.show();
                acctFirebase.getDatabase("requests", mProgress, 1);
                finish();
            }
        });

        Set f = user.getFriends();
        friends.clear();
        friends.addAll(f);
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(friends);
        adapter = new FriendAdapter(this, friends);
        adapter.setClickListener(this);
        show();
    }

    public void show(){
        //fAdapter.getDatabase(user, mProgress);
        //while(mProgress.isShowing()){}

        //Set f = user.getFriends();
        //friends.clear();
        //friends.addAll(f);
        Collections.sort(friends);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(deleteTrue == false) {
            startMessageActivity(position);
        } else {
            removeFriend(position);
        }
        show();
    }

    public void removeFriend(int position){
        String friend = adapter.getItem(position);
        user.removeFriend(friend);
        show();
    }

    public void addFriend(String name){
        if(!validUser(name))
            notValidFriend();
        user.pendFriend(name);
        //show();
    }

    public void notValidFriend(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Not a valid user");
        builder.setMessage("Please enter a valid user email");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public AlertDialog onCreateAddFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new friend");
        LayoutInflater inflater = this.getLayoutInflater();
        final View v = inflater.inflate(R.layout.add_friend, null);

        builder.setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText friend = (EditText)v.findViewById(R.id.friend_name);
                        addFriend(friend.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public boolean validUser(String email){
        return fAdapter.getMap(email, 0).size() != 0;
    }
  
    public void startMessageActivity(int i){
        // to grab user id
        Intent prev = getIntent();
        // to pass user id and friend email to messageing activity
        Intent activity = new Intent(FriendActivity.this, MessageActivity.class);
        activity.putExtra("friend_email", friends.get(i));
        startActivity(activity);
    }

}
