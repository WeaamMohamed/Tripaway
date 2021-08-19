package com.example.tripaway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tripaway.utils.AuthHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class FacebookAuthActivity extends RegisterActivity {
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_facebook_auth);

        mCallbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code

                    AuthHelper.handleFacebookAccessToken(loginResult.getAccessToken());

                    Intent intent = new Intent(FacebookAuthActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    FacebookAuthActivity.this.finish();

//
//                    Toast.makeText(RegisterActivity.this,  "Welcome!", Toast.LENGTH_SHORT).show();
//                    Log.i(TAG, "Authentication with facebook Successfully" + loginResult.getAccessToken().getUserId());
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

                }


                @Override
                public void onError(FacebookException exception) {
                    // App code
                    //TODO:
                    Toast.makeText(FacebookAuthActivity.this, "Error", Toast.LENGTH_SHORT).show();

                }
            });

        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }

    }




       // LoginManager.getInstance().registerCallback();

