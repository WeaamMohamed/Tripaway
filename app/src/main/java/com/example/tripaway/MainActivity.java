package com.example.tripaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // My first Commit
    Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //there is user then don't start the login activity and go directly to main
        if (FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            startActivity(new Intent(this,HomeActivity.class));
            this.finish();
        }



        btnSignUp = findViewById(R.id.signup);
        btnLogin = findViewById(R.id.login_btn);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);




            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });


    }
}