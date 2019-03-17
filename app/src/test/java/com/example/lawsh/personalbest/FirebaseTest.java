package com.example.lawsh.personalbest;

import com.example.lawsh.personalbest.adapters.FirestoreAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseFirestore.class})
public class FirebaseTest {

    private final String id = "jimmy_12345";

    private FirestoreAdapter firestoreAdapter;
    private FirebaseFirestore fireBase = Mockito.mock(FirebaseFirestore.class);
    private User user;
    private HashMap<String,Object> map = new HashMap<>();
    private DocumentReference documentReference = Mockito.mock(DocumentReference.class);
    private OnSuccessListener successListener = Mockito.mock(OnSuccessListener.class);
    private OnFailureListener failureListener = Mockito.mock(OnFailureListener.class);

    @Before
    public void setUp(){
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        Task task = Mockito.mock(Task.class);

        PowerMockito.mockStatic(FirebaseFirestore.class);

        Mockito.when(FirebaseFirestore.getInstance()).thenReturn(fireBase);

        firestoreAdapter = FirestoreAdapter.getInstance();
        Mockito.when(fireBase.collection("users")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document(id)).thenReturn(documentReference);
        Mockito.when(documentReference.set(map)).thenReturn(task);
        Mockito.when(task.addOnSuccessListener(successListener)).thenReturn(task);
        Mockito.when(task.addOnFailureListener(failureListener)).thenReturn(task);

        user = Mockito.mock(User.class);
        Mockito.when(user.getEmail()).thenReturn(id);
        Mockito.when(user.toMap()).thenReturn(map);

    }

    @Test
    public void updateTest(){
        firestoreAdapter.updateDatabase(user.getEmail(), user.toMap(),
                successListener, failureListener);
        Mockito.verify(documentReference).set(map);
    }

    @Test
    public void getFireBaseInstanceTest(){
        Assert.assertEquals(firestoreAdapter.getFirestoreInstance(), fireBase);
    }

}
