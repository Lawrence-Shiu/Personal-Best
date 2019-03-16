package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.Intent;

import com.example.lawsh.personalbest.adapters.AuthenticationAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GoogleSignIn.class, FirebaseAuth.class, GoogleAuthProvider.class})
public class AuthenticationTest {
    private final String id = "jimmy";
    private AuthenticationAdapter auth;
    private Context context = Mockito.mock(Context.class);
    private GoogleSignInOptions gso = Mockito.mock(GoogleSignInOptions.class);
    private GoogleApiClient client = Mockito.mock(GoogleApiClient.class);
    private Task firebaseSignIn = Mockito.mock(Task.class);;

    private GoogleSignInClient gsc = Mockito.mock(GoogleSignInClient.class);
    private GoogleSignInAccount gsa = Mockito.mock(GoogleSignInAccount.class);
    private FirebaseAuth mAuth = Mockito.mock(FirebaseAuth.class);
    private FirebaseUser currentUser = Mockito.mock(FirebaseUser.class);

    private Intent intent = Mockito.mock(Intent.class);
    private Task task = Mockito.mock(Task.class);
    private OnCompleteListener compListener = Mockito.mock(OnCompleteListener.class);
    private AuthCredential cred = Mockito.mock(AuthCredential.class);

    @Before
    public void setUp(){
        PowerMockito.mockStatic(GoogleSignIn.class, FirebaseAuth.class, GoogleAuthProvider.class);
        Mockito.when(GoogleSignIn.getClient(context,gso)).thenReturn(gsc);
        Mockito.when(GoogleSignIn.getLastSignedInAccount(context)).thenReturn(gsa);
        Mockito.when(FirebaseAuth.getInstance()).thenReturn(mAuth);
        Mockito.when(mAuth.getCurrentUser()).thenReturn(currentUser);

        Mockito.when(GoogleSignIn.getSignedInAccountFromIntent(intent)).thenReturn(task);
        try {
            Mockito.when(task.getResult(ApiException.class)).thenReturn(gsa);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Mockito.when(gsa.getIdToken()).thenReturn(id);

        Mockito.when(GoogleAuthProvider.getCredential(id,null)).thenReturn(cred);

        Mockito.when(mAuth.signInWithCredential(cred)).thenReturn(firebaseSignIn);
        Mockito.when(firebaseSignIn.addOnCompleteListener(compListener)).thenReturn(firebaseSignIn);

        auth = AuthenticationAdapter.getInstance();
        auth.setmGoogleApiClient(context, gso, client);
    }

    @Test
    public void firebaseAuthTest(){
        auth.firebaseAuth(intent, compListener);
        Mockito.verify(mAuth).signInWithCredential(cred);
    }

    @Test
    public void getCurrentUserTest(){
        Assert.assertEquals(auth.getCurrentUser(), currentUser);
    }

    @Test
    public void getAccount(){
        Assert.assertEquals(auth.getAccount(), gsa);
    }

    @Test
    public void getGetClient(){
        Assert.assertEquals(auth.getGsc(), gsc);
    }

}
