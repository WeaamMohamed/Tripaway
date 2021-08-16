package com.example.tripaway.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tripaway.HomeScreenActivity;
import com.example.tripaway.LoginActivity;
import com.example.tripaway.RegisterActivity;
import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class AuthHelper {
    private static final String TAG = "WEAAM";
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore dbFireStore;


    static {
        dbFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }
    public AuthHelper() {


    }


    public static void loginUser(String email, String password, Context context) {



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {


                        if(task.isSuccessful())
                        {

                            Log.i(TAG, " Successful login");


                            Intent i = new Intent(context, HomeScreenActivity.class);
                            //   set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(i);

                            saveOldTimeAndDateToSQL();



                        }
                        else {


                            //TODO: use red toast
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "login  failed" + task.getException().getMessage());


                        }



                    }
                });






    }

    private static void saveOldTimeAndDateToSQL() {

        dbFireStore.collection("users").document(mAuth.getUid())
                .collection("upcoming")
                .get().addOnSuccessListener(data -> {


            if(data.getDocuments() != null)
                for(int i = 0; i< data.getDocuments().size(); i++)
                {
                    Log.i("date",  data.getDocuments().get(i).getString("date"));
                    Log.i("time",  data.getDocuments().get(i).getString("time"));
                }



        });


    }



    public static void firebaseAuthWithGoogle(String idToken, Context context) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();



                            //save email, name , uId to FireStore

                            Map<String, Object> usersMap = new HashMap<>();
                            usersMap.put("email", user.getEmail());
                            usersMap.put("name", user.getDisplayName());
                            usersMap.put("uId", mAuth.getUid());




                            //  save users data (email, name, uId) in fireStore

                            FirebaseFirestore dbFireStore = FirebaseFirestore.getInstance();
                            dbFireStore.collection("users").document(mAuth.getUid())
                                    .set(usersMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "added to fireStore successfully ");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.i(TAG, "failed added to fireStore " + e.getMessage());
                                }
                            });




                            Intent i = new Intent(context, HomeScreenActivity.class);
                            //   set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(i);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // updateUI(null);
                        }
                    }
                });
    }


    public static void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "signInWithCredential:fail" + e.getMessage());
//

            }
        });


    }




}
