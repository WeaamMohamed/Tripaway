package com.example.tripaway.models;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class FireStoreHelper {
    private static final String TAG = "WEAAM";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore dbFireStore;

    FireStoreHelper()
    {
        mAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();
    }


    public static void saveUpcomingTrip(Map<String, Object> dataMap, String message){


        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .add(dataMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.i(TAG, "Data Added to FireStore successfully." + message);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {


                Log.i(TAG, "failed to Add data to FireStore: " + e.getMessage());


            }
        });


    }

}
