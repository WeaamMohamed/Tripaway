package com.example.tripaway;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripaway.utils.AuthHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText etName, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth;

    Button btnFacebook, btnGoogle, btnTwitter;
    private GoogleSignInClient mGoogleSignInClient;
    TextView tvLogin;
    LoginButton btnFacebookLogin;
    final String TAG = "WEAAM";
    CallbackManager mCallbackManager;
    private final static int RC_SIGN_IN = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TODO:
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
       initializeViews();

        mCallbackManager = CallbackManager.Factory.create();
        btnFacebookLogin.setReadPermissions("email", "public_profile");
        FacebookSdk.sdkInitialize(getApplicationContext());
        createGoogleRequest();

        // Callback registration



        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();

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


        btnRegister.setOnClickListener(view -> createUser());




    }



//    private void handleFacebookAccessToken(AccessToken token) {
//            Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//            mAuth.signInWithCredential(credential)
//                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                        @Override
//                        public void onSuccess(AuthResult authResult) {
//                            Log.d(TAG, "signInWithCredential:success");
//                               FirebaseUser user = mAuth.getCurrentUser();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, "signInWithCredential:fail" + e.getMessage());
////
//
//                }
//            });
//
//
//        }




    private void createGoogleRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }


    //to signIn with google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //TODO:
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void createUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if(validate(name, email, password, confirmPassword))
        {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {



                        //save email, name , uId to FireStore

                        Map<String, Object> usersMap = new HashMap<>();
                        usersMap.put("email", email);
                        usersMap.put("name", name);
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

                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Registered Successfully");
                        Intent i = new Intent(RegisterActivity.this, HomeScreenActivity.class);
                        //   set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);



                    }

                    else {

                        //TODO: use red toast
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Registration  failed" + task.getException().getMessage());


                    }

                }
            });

        }



    }

    private boolean validate(String name, String email, String password, String confirmPassword) {
        boolean valid = true;


        if(name.isEmpty())
        {
            etName.setError("name cannot be empty.");
            etName.requestFocus();
            valid = false;
        }
        else {
            etName.setError(null);
        }

        if(email.isEmpty())
        {
            etEmail.setError("Email cannot be empty.");
            etEmail.requestFocus();
            valid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {

            etEmail.setError("Enter valid email.");
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
            etPassword.requestFocus();
            valid = false;

        }
        else if(password.length() < 6)
        {
            etPassword.setError("password must be more than 6 characters.");
            etPassword.requestFocus();
            valid = false;

        }
        else
        {
            etPassword.setError(null);
        }


        if(confirmPassword.isEmpty())
        {
            etConfirmPassword.setError("Empty.");
            etConfirmPassword.requestFocus();
            valid = false;
        }
        else if(!confirmPassword.equals(password)){

            etConfirmPassword.setError("Password doesn't match.");
            etConfirmPassword.requestFocus();
            valid = false;

        }
        else
        {
            etConfirmPassword.setError(null);
        }


        return valid;
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);



        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                AuthHelper.firebaseAuthWithGoogle(account.getIdToken(), RegisterActivity.this);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed"+ e.getMessage().toString());
            }
        }
    }


    //TODO: progressbar
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//
//
//
//                            //save email, name , uId to FireStore
//
//                            Map<String, Object> usersMap = new HashMap<>();
//                            usersMap.put("email", user.getEmail());
//                            usersMap.put("name", user.getDisplayName());
//                            usersMap.put("uId", mAuth.getUid());
//
//
//
//
//                            //  save users data (email, name, uId) in fireStore
//
//                            FirebaseFirestore dbFireStore = FirebaseFirestore.getInstance();
//                            dbFireStore.collection("users").document(mAuth.getUid())
//                                    .set(usersMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.i(TAG, "added to fireStore successfully ");
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//
//                                    Log.i(TAG, "failed added to fireStore " + e.getMessage());
//                                }
//                            });
//
//
//
//
//                            Intent i = new Intent(RegisterActivity.this, HomeScreenActivity.class);
//                            //   set the new task and clear flags
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(i);
//
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            // updateUI(null);
//                        }
//                    }
//                });
//    }


    public void onFaceBookClicked(View view) {

        if (view == btnRegister) {
            btnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code

                    AuthHelper.handleFacebookAccessToken(loginResult.getAccessToken());

                    Toast.makeText(RegisterActivity.this,  "Welcome!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Authentication with facebook Successfully" + loginResult.getAccessToken().getUserId());
//                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
//                startActivity(intent);
//                Intent i = new Intent(RegisterActivity.this, HomeScreenActivity.class);
//                //   set the new task and clear flags
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);


                }

                @Override
                public void onCancel() {
                    // App code
                    //TODO:
                    Log.i("WEAAM", "onCancel");
                }


                @Override
                public void onError(FacebookException exception) {
                    // App code
                    //TODO:
                    Log.i("WEAAM", "onError");

                }
            });

        }

    }

    private void initializeViews() {
        btnRegister = findViewById(R.id.btn_sign_up);
        etName = findViewById(R.id.edit_txt_name);
        etEmail = findViewById(R.id.edit_txt_email);
        etPassword = findViewById(R.id.edit_txt_password);
        etConfirmPassword = findViewById(R.id.edit_confirm_password);
        btnFacebook = findViewById(R.id.btn_facebook);
        btnGoogle = findViewById(R.id.btn_google);
        btnTwitter = findViewById(R.id.btn_twitter);
        tvLogin = findViewById(R.id.txt_login_click);
        btnFacebookLogin = (LoginButton) findViewById(R.id.btn_facebook_login);
    }

}