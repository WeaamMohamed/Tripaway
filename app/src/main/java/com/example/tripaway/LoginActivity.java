package com.example.tripaway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripaway.utils.AuthHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvSignUp, txtForgetPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    final String TAG = "WEAAM";
    private final static int RC_SIGN_IN = 123;
    FirebaseFirestore dbFireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FacebookSdk.sdkInitialize(getApplicationContext());

        etEmail = findViewById(R.id.edit_txt_email);
        etPassword = findViewById(R.id.edit_txt_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.txt_signup);
        txtForgetPassword = findViewById(R.id.txt_forgetPassword);
        mAuth = FirebaseAuth.getInstance();
        dbFireStore = FirebaseFirestore.getInstance();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                //to replace current activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                LoginActivity.this.finish();



            }
        });


        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(validate(email, password))
                {
                    AuthHelper.loginUser(email, password, LoginActivity.this);
                }



            }
        });


    }




    private boolean validate(String email, String password) {
        boolean valid = true;


        //TODO: validation

        if(email.isEmpty())
        {
            etEmail.setError("Email cannot be empty.");
            etEmail.requestFocus();
            valid = false;
        }
        else
        {
            etEmail.setError(null);

        }
        if(password.isEmpty())
        {
            etPassword.setError("password cannot be empty.");
            valid = false;

        }
        else
        {
            etPassword.setError(null);
        }

        return valid;
    }



}