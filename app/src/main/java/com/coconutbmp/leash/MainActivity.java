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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    SharedPreferences prefs;
    Button openSignIn, openSignUp, signIn, signUp;
    ImageView closeSignIn, closeSignUp, signInShowPass, signUpShowPass, signUpShowConfirm;
    TextView moveSignIn, moveSignUp, invalidName, invalidSurname, invalidEmail,invalidPass, invalidConfirm;
    CardView SignInCard, SignUpCard, googleLogIn, facebookLogIn;
    CheckBox StaySignedIn;
    EditText logInEmail, logInPass, signUpName, signUpSurname, signUpEmail, signUpPass, signUpPassConfirm;
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
        signIn = findViewById(R.id.btnSignIn);
        signUp = findViewById(R.id.btnRegister);
        googleLogIn = findViewById(R.id.googleCard);
        facebookLogIn = findViewById(R.id.facebookCard);
        StaySignedIn = findViewById(R.id.cbxStaySignedIn);
        logInEmail = findViewById(R.id.edtSignInEmail);
        logInPass = findViewById(R.id.edtSignInPass);
        signUpName = findViewById(R.id.edtSignUpName);
        signUpSurname = findViewById(R.id.edtSignUpSurname);
        signUpEmail = findViewById(R.id.edtSignUpEmail);
        signUpPass = findViewById(R.id.edtSignUpPass);
        signUpPassConfirm = findViewById(R.id.edtSignUpConfirmPass);
        invalidName = findViewById(R.id.lblInvalidName);
        invalidSurname = findViewById(R.id.lblInvalidSurname);
        invalidEmail = findViewById(R.id.lblInvalidEmail);
        invalidPass = findViewById(R.id.lblInvalidPass);
        invalidConfirm = findViewById(R.id.lblInvalidConfirm);
        signInShowPass = findViewById(R.id.imgLogInViewPass);

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

        InternetRequest internetRequest = new InternetRequest();
        internetRequest.doRequest("http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/test.php",
                this, "Liam",new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        String g = "";
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                                g = g + jsonObject.getString("name");
                                Toast.makeText(MainActivity.this, g, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        openSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInCard.setVisibility(View.VISIBLE);
                SignUpCard.setVisibility(View.GONE);
                openSignIn.setVisibility(View.GONE);
                openSignUp.setVisibility(View.GONE);
                googleLogIn.setVisibility(View.GONE);
                facebookLogIn.setVisibility(View.GONE);

                if(StaySignedIn.isChecked()){
                    String userEmail = prefs.getString("email", "");
                    String userPass = prefs.getString("pass", "");
                    logInEmail.setText(userEmail);
                    logInPass.setText(userPass);
                }
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

        signInShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassVisibility(logInPass, signInShowPass);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = logInEmail.getText().toString();
                String pass = logInPass.getText().toString();
                if(validateEmail(email) && validatePass(pass, null)){
                    // add database stuff here
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signUpName.getText().toString();
                String surname = signUpSurname.getText().toString();
                String email = signUpEmail.getText().toString();
                String pass = signUpPass.getText().toString();
                String confirmPass = signUpPassConfirm.getText().toString();

                if(validateName(name) && validateName(surname) && validateEmail(email) && validatePass(pass, null) && validatePass(pass, confirmPass)){
                    // add database stuff here
                }
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
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        signUpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validateName(charSequence.toString())){
                    signUpName.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidName.getLayoutParams().height = 30;
                }
                else{
                    signUpName.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidName.getLayoutParams().height = 1;
                }
                invalidName.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validateName(charSequence.toString())){
                    signUpSurname.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidSurname.getLayoutParams().height = 30;
                }
                else{
                    signUpSurname.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidSurname.getLayoutParams().height = 1;
                }
                invalidName.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validateEmail(charSequence.toString())){
                    signUpEmail.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidEmail.getLayoutParams().height = 30;
                }
                else{
                    signUpEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidEmail.getLayoutParams().height = 1;
                }
                invalidName.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validatePass(charSequence.toString(), null)){
                    signUpPass.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidPass.getLayoutParams().height = 30;
                }
                else{
                    signUpPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidPass.getLayoutParams().height = 1;
                }
                invalidName.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpPassConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validatePass(charSequence.toString(), signUpPass.getText().toString())){
                    signUpPassConfirm.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidConfirm.getLayoutParams().height = 30;
                }
                else{
                    signUpPassConfirm.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidConfirm.getLayoutParams().height = 1;
                }
                invalidName.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    static public boolean validateName(String name){
        Pattern special = Pattern.compile("[!@#$%^&*()_+=|<>?{}\\[\\]~/`-]");
        Matcher isSpecial = special.matcher(name);
        if(name.isEmpty() || isSpecial.find()){
            return false;
        }
        for (int i = 0; i < name.length(); i++){
            char c = name.charAt(i);
            if(Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    static public boolean validateEmail(String email){
        if(email.isEmpty() || !email.contains("@") || !email.contains(".")){
            return false;
        }
        return true;
    }

    static public boolean validatePass(String pass, String confirm){
        boolean capitalFlag = false, numFlag = false, lowerFlag = false;
        if(confirm == null){
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
        }
        else if(!confirm.equals(pass)){
            return false;
        }
        return true;
    }

    public void doGoogleLogIn(){
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
            String password = prefs.getString("pass", "N/A");
            String name = account.getGivenName();
            String surname = account.getFamilyName();
            String email = account.getEmail();

            if(password.equals("N/A")){ //if user not in database, make up a pass for database
                //put database stuff here
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("pass", password);//store password locally for next time
                editor.commit();
            }
            else{ //else sign in
                //put database stuff here
            }

            Toast.makeText(this, "Welcome "+name+" "+surname, Toast.LENGTH_LONG).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult: failed code= " + e.getStatusCode() + e.getStatus().toString());
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show();
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

    public void changePassVisibility(EditText edtPass, ImageView sender){
        if(edtPass.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
            edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else{
            edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}