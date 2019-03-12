package com.example.lawsh.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import java.util.HashSet;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest( {FirebaseFirestore.class, CollectionReference.class, FirebaseAuth.class,
        FirebaseUser.class, GoogleSignIn.class, GoogleSignInOptions.class,
        GoogleSignInAccount.class, SharedPreferences.class, SharedPreferences.Editor.class,
        Task.class, OnSuccessListener.class, OnFailureListener.class} )
public class FirebaseTest {

    FirestoreAdapter firestoreAdapter;
    User testUser;

    String userId;
    Map<String, Object> userMap;

    @Rule
    public PowerMockRule pmr = new PowerMockRule();

    @Before
    public void setUp() {
        testUser = new User();
    }

    @Test
    public void testFirestoreInit() {/*
        fstore = PowerMockito.mock(FirebaseFirestore.class, Mockito.RETURNS_DEEP_STUBS);
        cRef = PowerMockito.mock(CollectionReference.class, Mockito.RETURNS_DEEP_STUBS);
        dRef = PowerMockito.mock(DocumentReference.class, Mockito.RETURNS_DEEP_STUBS);
        task = PowerMockito.mock(Task.class, Mockito.RETURNS_DEEP_STUBS);
        osl = PowerMockito.mock(OnSuccessListener.class, Mockito.RETURNS_DEEP_STUBS);
        ofl = PowerMockito.mock(OnFailureListener.class, Mockito.RETURNS_DEEP_STUBS);*/

        firestoreAdapter = firestoreAdapter.getInstance();
        //Mockito.when(firestoreAdapter).updateDatabase((fstore, testUser)).thenReturn(null);

        //firestoreAdapter.updateDatabase(testUser);

        //Mockito.verify(dRef).set(testUser.getId());
        //Assert.assertEquals(userId, testUser.getId());
    }


}
