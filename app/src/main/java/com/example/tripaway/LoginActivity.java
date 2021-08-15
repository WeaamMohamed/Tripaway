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
    TextView tvSignUp;
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();


            }
        });


    }


    private void loginUser() {


        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(validate(email, password))
        {


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {


                            if(task.isSuccessful())
                            {

                                Log.i(TAG, " Successful login");


                                Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                //   set the new task and clear flags
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                                saveOldTimeAndDateToSQL();



                            }
                            else {


                                //TODO: use red toast
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "login  failed" + task.getException().getMessage());


                            }



                        }
                    });
        }





    }

    private void saveOldTimeAndDateToSQL() {

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed"+ e.getMessage().toString());
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                            //   set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // updateUI(null);
                        }
                    }
                });
    }



}