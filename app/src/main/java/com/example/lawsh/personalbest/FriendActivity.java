package com.example.lawsh.personalbest;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity implements FriendAdapter.ItemClickListener {

    FriendAdapter adapter;
    Button addFriendBtn;
    Button rmFriendBtn;
    ArrayList<String> friends;
    RecyclerView recyclerView;
    boolean deleteTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // data to populate the RecyclerView with
        friends = new ArrayList<>();

        // set up buttons
        addFriendBtn = findViewById(R.id.addFriend);
        rmFriendBtn = findViewById(R.id.deleteFriend);

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

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(this, friends);
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        if(deleteTrue == false) {
            Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        }else{
            adapter.removeItem(position);
        }
        recyclerView.setAdapter(adapter);
    }

    public void addFriend(String name){
        friends.add(name);
        recyclerView.setAdapter(adapter);
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

}
