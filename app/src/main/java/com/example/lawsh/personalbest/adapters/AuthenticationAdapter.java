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

public class AuthenticationAdapter implements IAuth {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount gsa;
    private GoogleSignInClient gsc;
    private GoogleApiClient mGoogleApiClient;

    public static volatile AuthenticationAdapter authSingleton = new AuthenticationAdapter();

    public static AuthenticationAdapter getInstance() {
        return authSingleton;
    }

    private AuthenticationAdapter() {

    }

    public void setApiFields(Context context, String client_id, FragmentActivity activity) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(client_id) //don't worry about this "error"
                .requestEmail()
                .requestId()
                .build();
        gsc = GoogleSignIn.getClient(context, gso);
        gsa = GoogleSignIn.getLastSignedInAccount(context);


        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d("MainActivity", "Connection Failed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public void firebaseAuth(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public GoogleSignInAccount getAccount() {
        return gsa;
    }

    public GoogleSignInClient getGsc() {
        return gsc;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                        } else {
                            Log.d("MainActivity", "Auth failed");
                        }
                    }
                });
    }
}
