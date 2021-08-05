package com.example.tripaway;

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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.facebook.FacebookSdk;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText etName, etEmail, etPassword;
    private FirebaseAuth mAuth;

    Button btnFacebook, btnGoogle, btnTwitter;

    TextView tvLogin;
    LoginButton btnFacebookLogin;
    final String TAG = "WEAAM";
    CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TODO:
        FacebookSdk.sdkInitialize(getApplicationContext());

        btnRegister = findViewById(R.id.btn_sign_up);
        etName = findViewById(R.id.edit_txt_name);
        etEmail = findViewById(R.id.edit_txt_email);
        etPassword = findViewById(R.id.edit_txt_password);

        btnFacebook = findViewById(R.id.btn_facebook);
        btnGoogle = findViewById(R.id.btn_google);
        btnTwitter = findViewById(R.id.btn_twitter);
        tvLogin = findViewById(R.id.txt_login_click);

        //btnFacebookLogin = (LoginButton) findViewById(R.id.facebook_login_button);

//btnFacebookLogin.setReadPermissions("Facebook");
        mCallbackManager = CallbackManager.Factory.create();



        // Callback registration
        btnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Toast.makeText(RegisterActivity.this,  "Welcome!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Authentication with facebook Successfully");
//                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//                startActivity(intent);
                Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                //   set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                // App code
                //TODO:
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                //TODO:
            }
        });



        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                //to replace current activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
                RegisterActivity.this.finish();

            }
        });

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> createUser());



        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });







    }

    private void createUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString();

        //TODO: validation

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty(email))
        {

            etEmail.setError("Email cannot be empty.");
            etEmail.requestFocus();
        }
        if(!email.matches(emailPattern))
        {

            etEmail.setError("Enter valid email.");
            etEmail.requestFocus();
        }
         if(TextUtils.isEmpty(password))
        {
            etPassword.setError("password cannot be empty.");
            etPassword.requestFocus();

        }
         if(password.length() < 6)
         {
             etPassword.setError("password must be more than 6 characters.");
             etPassword.requestFocus();

         }
         if(TextUtils.isEmpty(name))
        {
            etName.setError("name cannot be empty.");
            etName.requestFocus();
        }



            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Registered Successfully");

                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                   //   set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);



                    }
                    else {

                        //TODO: use red toast
                        Toast.makeText(RegisterActivity.this, "Registration  failed", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Registration  failed" + task.getException().getMessage());


                    }

                }
            });







    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null) {
//            //to refresh activity from itself
//            finish();
//            startActivity(getIntent());
//            Toast.makeText(this, "User is signed in", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}