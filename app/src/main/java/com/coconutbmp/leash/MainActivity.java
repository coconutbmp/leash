package com.coconutbmp.leash;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    SharedPreferences prefs;
    boolean validLogIn, validSignUp;
    Button openSignIn, openSignUp;
    ImageView closeSignIn, closeSignUp;
    TextView moveSignIn, moveSignUp;
    CardView SignInCard, SignUpCard, googleLogIn, facebookLogIn;
    CheckBox StaySignedIn;
    EditText logInEmail, logInPass;
    ActivityResultLauncher<Intent> activityResultLauncher;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        SignInCard = findViewById(R.id.SignInCard);
        SignUpCard = findViewById(R.id.SignUpCard);
        openSignIn = findViewById(R.id.btnShowSignIn);
        openSignUp = findViewById(R.id.btnShowSignUp);
        moveSignIn = findViewById(R.id.lblOpenSignIn);
        moveSignUp = findViewById(R.id.lblOpenSignUp);
        closeSignIn = findViewById(R.id.imgCloseSignIn);
        closeSignUp = findViewById(R.id.imgCloseSignUp);
        googleLogIn = findViewById(R.id.googleCard);
        facebookLogIn = findViewById(R.id.facebookCard);
        StaySignedIn = findViewById(R.id.cbxStaySignedIn);
        logInEmail = findViewById(R.id.edtSignInEmail);
        logInPass = findViewById(R.id.edtSignInPass);

        StaySignedIn.setChecked(prefs.getBoolean("StaySignedIn", false));
        activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        completeGoogleSignIn(data);
                    }
                }
            }
        );

        openSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInCard.setVisibility(View.VISIBLE);
                SignUpCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.GONE);
                openSignUp.setVisibility(View.GONE);
                googleLogIn.setVisibility(View.GONE);
                facebookLogIn.setVisibility(View.GONE);
            }
        });

        openSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpCard.setVisibility(View.VISIBLE);
                SignInCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.GONE);
                openSignUp.setVisibility(View.GONE);
                googleLogIn.setVisibility(View.GONE);
                facebookLogIn.setVisibility(View.GONE);
            }
        });

        closeSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpCard.setVisibility(View.GONE);
                SignInCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.VISIBLE);
                openSignUp.setVisibility(View.VISIBLE);
                googleLogIn.setVisibility(View.VISIBLE);
                facebookLogIn.setVisibility(View.VISIBLE);
            }
        });

        closeSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpCard.setVisibility(View.GONE);
                SignInCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.VISIBLE);
                openSignUp.setVisibility(View.VISIBLE);
                googleLogIn.setVisibility(View.VISIBLE);
                facebookLogIn.setVisibility(View.VISIBLE);
            }
        });

        moveSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInCard.setVisibility(View.VISIBLE);
                SignUpCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.GONE);
                openSignUp.setVisibility(View.GONE);
                googleLogIn.setVisibility(View.GONE);
                facebookLogIn.setVisibility(View.GONE);
            }
        });

        moveSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpCard.setVisibility(View.VISIBLE);
                SignInCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.GONE);
                openSignUp.setVisibility(View.GONE);
                googleLogIn.setVisibility(View.GONE);
                facebookLogIn.setVisibility(View.GONE);
            }
        });

        StaySignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();
                if(StaySignedIn.isChecked() == true) {
                    editor.putBoolean("StaySignedIn", true);
                }
                else{
                    editor.putBoolean("StaySignedIn", false);
                }
                editor.commit();
            }
        });

        googleLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGoogleLogIn();
            }
        });

        facebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doFacebookLogIn();
            }
        });

        logInEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!validateEmail(editable.toString())){
                    logInEmail.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 50), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    logInEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        logInPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!validatePass(editable.toString())){
                    logInPass.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 50), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    logInPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    static public boolean validateEmail(String email){
        if(!email.contains("@") || !email.contains(".")){
            return false;
        }
        return true;
    }

    static public boolean validatePass(String pass){
        boolean capitalFlag = false, numFlag = false, lowerFlag = false;
        if(pass.length() < 8){
            return false;
        }
        for (int i = 0; i < pass.length(); i++){
            char c = pass.charAt(i);
            if(Character.isUpperCase(c)){
                capitalFlag = true;
            }
            else if(Character.isDigit(c)){
                numFlag = true;
            }
            else if(Character.isLowerCase(c)){
                lowerFlag = true;
            }
        }
        if(!capitalFlag || !lowerFlag || !numFlag){
            return false;
        }
        return true;
    }

    void doGoogleLogIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent googleSignIn = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(googleSignIn);
    }

    public void completeGoogleSignIn(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(this, "logged in", Toast.LENGTH_LONG).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult: failed code= " + e.getStatusCode() + e.getStatus().toString());
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
        }
    }

    public void doFacebookLogIn(){
        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });
    }


}