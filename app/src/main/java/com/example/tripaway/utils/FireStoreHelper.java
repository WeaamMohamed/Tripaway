package com.example.tripaway.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireStoreHelper {
    private static final String TAG = "WEAAM";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore dbFireStore;

    public FireStoreHelper()
    {

    }

    static {

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

    public static void openNotesDialog(String selectedDocumentId, Context context, String collectionPath) {


        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection(collectionPath)
                .document(selectedDocumentId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String message ="";
                ArrayList<String> oldNotes = (ArrayList<String>) documentSnapshot.get("notes");
                if(oldNotes != null)
                    for(int i =0; i< oldNotes.size(); i++)
                    {
//                        //addNoteEditText();
//                        editTextList.get(i).setText(oldNotes.get(i));

                        message += oldNotes.get(i) + "\n";

                    }

                if(message.equals(""))
                    message = "You haven't added any notes";


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title
                alertDialogBuilder.setTitle("Notes");

                // set dialog message
                alertDialogBuilder.setMessage(message).setCancelable(false);

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                // To cancel dialog
                alertDialog.setCanceledOnTouchOutside(true);


                // alertDialog.dismiss();
            }
        });


    }

    public static void deleteOldTripFromFireStore(String documentId) {
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("oldTrips")
                .document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("WEAAM", "document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("WEAAM", "Error deleting document: "+  e.getMessage());
                    }
                });


    }

    public static void sendDataFromUpcomingToHistory(String documentId, boolean done) {




        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object>upcomingMapData =  documentSnapshot.getData();
                        sendingDataToOldTripsFireStore(upcomingMapData, done);
                        deleteUpcomingTripFromFireStore(documentId);
                    }
                });









    }

    public static void sendingDataToOldTripsFireStore(Map<String, Object> upcomingMapData, boolean done) {


        upcomingMapData.put("done", done);
        upcomingMapData.put("timestamp", new Timestamp( System.currentTimeMillis()));
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("oldTrips")
                .add(upcomingMapData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.i("WEAAM", "Data Added to FireStore successfully.");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {


                Log.i("WEAAM", "failed to Add data to FireStore: " + e.getMessage());


            }
        });

    }

    public static void deleteUpcomingTripFromFireStore (String documentId){
        dbFireStore.collection("users")
                .document(mAuth.getUid())
                .collection("upcoming")
                .document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("WEAAM", "document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("WEAAM", "Error deleting document: " + e.getMessage());
                    }
                });
    }






}
