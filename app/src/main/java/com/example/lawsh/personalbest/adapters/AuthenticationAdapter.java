package com.example.lawsh.personalbest.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.lawsh.personalbest.MainActivity;
import com.example.lawsh.personalbest.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.provider.Settings.System.getString;

public class AuthenticationAdapter {

    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;
    private GoogleSignInClient gsc;
    private GoogleApiClient mGoogleApiClient;

    public AuthenticationAdapter(Context context, GoogleSignInOptions gso, GoogleApiClient client) {
        this.context = context;
        this.gso = gso;
        gsc = GoogleSignIn.getClient(context, gso);
        gsa = GoogleSignIn.getLastSignedInAccount(context);


        mGoogleApiClient = client;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void firebaseAuth(Intent data, OnCompleteListener<AuthResult> completeListener) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account, completeListener);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(FirebaseUser currentUser){
        this.currentUser = currentUser;
    }

    public GoogleSignInAccount getAccount() {
        return gsa;
    }

    public GoogleSignInClient getGsc() {
        return gsc;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct, OnCompleteListener<AuthResult> completeListener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(completeListener);
    }


}
