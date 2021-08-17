package com.example.tripaway;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

public class TwitterActivity extends RegisterActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        // Target specific email with login hint.
        provider.addCustomParameter("lang", "fr");

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(TwitterActivity.this, UpcomingTripsActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(TwitterActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(TwitterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
        } else {
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
//                                    Intent intent = new Intent(TwitterActivity.this, HomeActivity.class);
//                                    startActivity(intent);
                                    Toast.makeText(TwitterActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(TwitterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
        }

    }
}