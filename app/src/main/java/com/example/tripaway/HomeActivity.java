package com.example.tripaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {


    Button btnLogout;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogout = findViewById(R.id.logout_button);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
if(googleSignInAccount != null)
{
    //get data of user
    Log.i("WEAAM", googleSignInAccount.getDisplayName());


}

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));


            }
        });


    }
}