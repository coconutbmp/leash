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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{
    SharedPreferences prefs; // variable to access locally stored user preferences
    ActivityResultLauncher<Intent> activityResultLauncher; // Variable to execute google login after a result from the api is returned
    GoogleSignInClient googleSignInClient; //api variable to access user's google accounts
    InternetRequest internetRequest; //Object to execute http requests from the app
    String url = "http://ec2-13-244-123-87.af-south-1.compute.amazonaws.com/"; // base link where http requests are sent

    // Declare all components used on Login Page
    Button openSignIn, openSignUp, signIn, signUp;
    ImageView closeSignIn, closeSignUp, signInShowPass, signUpShowPass, signUpShowConfirm;
    TextView moveSignIn, moveSignUp, invalidName, invalidSurname, invalidEmail,invalidPass, invalidConfirm, invalidLoginEmail, invalidLoginPass;
    CardView SignInCard, SignUpCard, googleLogIn;
    CheckBox StaySignedIn;
    EditText logInEmail, logInPass, signUpName, signUpSurname, signUpEmail, signUpPass, signUpPassConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE); // initialise and fetch user preferences
        hook(); // hook all components to related xml components

        // check if user wants to to be remembered when logging in
        StaySignedIn.setChecked(prefs.getBoolean("StaySignedIn", false));

        internetRequest = new InternetRequest(); // instantiate Internet Request instance

        // instantiate result handler for google login and set function to handle result
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

        // Navigation and UI interaction
        // Code has slight differences so repetition is necessary
        openSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email;
                String pass;
                if(StaySignedIn.isChecked()){
                    email = prefs.getString("email", null);
                    pass = prefs.getString("pass", null);
                    if(email != null&& pass != null && loginUtils.validateEmail(email) && loginUtils.validatePass(pass, null)){ // validate data
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("email", email); // put email in json for http request
                            //Make http request to login.php
                            internetRequest.doRequest(url+"login.php", MainActivity.this, jsonObject, new RequestHandler() {
                                @Override
                                public void processResponse(String response) {
                                    doRegularLogIn(response, pass); //how to handle server response
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace(); // catch failed request
                        }
                    }
                }else{
                    SignInCard.setVisibility(View.VISIBLE);
                    SignUpCard.setVisibility(View.GONE);
                    openSignIn.setVisibility(View.GONE);
                    openSignUp.setVisibility(View.GONE);
                    googleLogIn.setVisibility(View.GONE);
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
            }
        });

        StaySignedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = prefs.edit();
                if(StaySignedIn.isChecked() == true) {
                    editor.putBoolean("StaySignedIn", true);
                    boolean emailCheck = logInEmail.getText().toString().equals(null) || logInEmail.getText().toString().equals("");
                    boolean passCheck = logInPass.getText().toString().equals(null) || logInPass.getText().toString().equals("");
                    if (!emailCheck){
                        editor.putString("email", logInEmail.getText().toString());
                        editor.putString("pass", logInPass.getText().toString());
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Cannot Save. Empty Inputs", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    editor.putBoolean("StaySignedIn", false);
                    editor.putString("email", null);
                    editor.putString("pass", null);
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

        signUpShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassVisibility(signUpPass, signUpShowPass);
            }
        });

        signUpShowConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassVisibility(signUpPassConfirm, signUpShowConfirm);
            }
        });

        //How to deal with user login
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email;
                String pass;
                // fetch input

                email = logInEmail.getText().toString();
                pass = logInPass.getText().toString();

                if(loginUtils.validateEmail(email) && loginUtils.validatePass(pass, null)){ // validate data
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email); // put email in json for http request
                        //Make http request to login.php
                        internetRequest.doRequest(url+"login.php", MainActivity.this, jsonObject, new RequestHandler() {
                            @Override
                            public void processResponse(String response) {
                                doRegularLogIn(response, pass); //how to handle server response
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace(); // catch failed request
                    }
                }
            }
        });

        // how to handle new user registration
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fetch user input
                String name = signUpName.getText().toString();
                String surname = signUpSurname.getText().toString();
                String email = signUpEmail.getText().toString();
                String pass = signUpPass.getText().toString();
                String confirmPass = signUpPassConfirm.getText().toString();

                boolean valid = loginUtils.validateName(name)
                        && loginUtils.validateName(surname)
                        && loginUtils.validateEmail(email)
                        && loginUtils.validatePass(pass, null)
                        && loginUtils.validatePass(pass, confirmPass); // validate input

                if(valid) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        String hashed = loginUtils.Hash(pass);
                        // add everything to json for request
                        jsonObject.put("FName", name);
                        jsonObject.put("LName", surname);
                        jsonObject.put("email", email);
                        jsonObject.put("pass", hashed);
                        //make http request to register.php
                        internetRequest.doRequest(url + "register.php", MainActivity.this, jsonObject, new RequestHandler() {
                            @Override
                            public void processResponse(String response) {
                                doRegister(response, name, surname); // process server response
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        googleLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doGoogleLogIn();
            }
        });

        // UI Feature, track user input validity in real time
        // display error message to related input

        logInEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!loginUtils.validateEmail(charSequence.toString())){ // validate input
                    // change input color and show error
                    logInEmail.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidLoginEmail.getLayoutParams().height = 30;
                }
                else{
                    // return to normal
                    logInEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidLoginEmail.getLayoutParams().height = 1;
                }
                invalidLoginEmail.requestLayout(); // request layout update
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        logInPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!loginUtils.validatePass(charSequence.toString(), null)){
                    logInPass.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidLoginPass.getLayoutParams().height = 30;
                }
                else{
                    logInPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidLoginPass.getLayoutParams().height = 1;
                }
                invalidLoginPass.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!loginUtils.validateName(charSequence.toString())){
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
                if(!loginUtils.validateName(charSequence.toString())){
                    signUpSurname.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidSurname.getLayoutParams().height = 30;
                }
                else{
                    signUpSurname.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidSurname.getLayoutParams().height = 1;
                }
                invalidSurname.requestLayout();
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
                if(!loginUtils.validateEmail(charSequence.toString())){
                    signUpEmail.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidEmail.getLayoutParams().height = 30;
                }
                else{
                    signUpEmail.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidEmail.getLayoutParams().height = 1;
                }
                invalidEmail.requestLayout();
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
                if(!loginUtils.validatePass(charSequence.toString(), null)){
                    signUpPass.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidPass.getLayoutParams().height = 30;
                }
                else{
                    signUpPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidPass.getLayoutParams().height = 1;
                }
                invalidPass.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpPassConfirm.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!loginUtils.validatePass(signUpPass.getText().toString(), charSequence.toString())){
                    signUpPassConfirm.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 40), PorterDuff.Mode.SRC_ATOP);
                    invalidConfirm.getLayoutParams().height = 30;
                }
                else{
                    signUpPassConfirm.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                    invalidConfirm.getLayoutParams().height = 1;
                }
                invalidConfirm.requestLayout();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() { //when returning to login page, everything should be reset
        super.onResume();

        // clear all edits
        logInEmail.getText().clear();
        logInPass.getText().clear();
        signUpName.getText().clear();
        signUpSurname.getText().clear();
        signUpEmail.getText().clear();
        signUpPass.getText().clear();
        signUpPassConfirm.getText().clear();

        // check user preferences and add inputs back if necessary
        StaySignedIn.setChecked(prefs.getBoolean("StaySignedIn", false));

        if(StaySignedIn.isChecked()){
            String userEmail = prefs.getString("email", null);
            logInEmail.setText(userEmail);
        }
    }

    public void hook(){ // function to hook java components to xml compenents
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
        invalidLoginEmail = findViewById(R.id.lblInvalidLoginEmail);
        invalidLoginPass = findViewById(R.id.lblInvalidLoginPass);
        signInShowPass = findViewById(R.id.imgLogInViewPass);
        signUpShowPass = findViewById(R.id.imgShowSignInPass);
        signUpShowConfirm = findViewById(R.id.imgShowSignInConfirm);
    }

    public void doRegister(String response, String name, String surname){ // how to handle registration server response
        Intent home = new Intent(MainActivity.this, HomeActivity.class); // set up intent to change page
        if(!response.equals("Failed")){ // check response was successful (success returns userID)
            Toast.makeText(MainActivity.this, "Welcome " +name+" "+surname, Toast.LENGTH_SHORT).show(); // Welcome Toast message
            home.putExtra("userID", response); // carry userID to next activity
            Data.setUserID(Integer.parseInt(response)); // store user id in a central location
            startActivity(home); // change activities
        }
        else{ // if failed, display error
            Toast.makeText(MainActivity.this, "An error has occurred, try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void doRegularLogIn(String response, String pass){ // handle login server response
        Intent home = new Intent(MainActivity.this, HomeActivity.class);
        try {
            boolean valid = false;
            String name = "";
            String userID ="";

            JSONArray jsonArray = new JSONArray(response); // format response to JSON array
            for (int i = 0; i < jsonArray.length(); i ++){ // loop through array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (loginUtils.verifyPassword(pass ,jsonObject.getString("user_Password"))){ // find matching password
                    // store necessary info
                    userID = jsonObject.getString("user_ID");
                    name = jsonObject.getString("user_FirstName") + " " + jsonObject.getString("user_LastName");
                    valid = true;
                    break; // exit loop
                }
            }

            if(valid){ // check if user exists
                Toast.makeText(MainActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show(); // welcome toast
                home.putExtra("userID",userID); // carry userID to next activity
                Data.setUserID(Integer.parseInt(userID)); // store user id in a central location
                startActivity(home); // change activity
            }
            else{ // error message
                Toast.makeText(MainActivity.this, "No Such Account Exists", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) { //JSON Object exception
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public void doGoogleLogIn(){ // start google login process
        // Instantiate Sign in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso); // get google sign api client
        googleSignInClient.signOut(); // sign out any existing accounts
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        Intent googleSignIn = googleSignInClient.getSignInIntent(); // get intent
        activityResultLauncher.launch(googleSignIn); // start processing results
    }

    public void completeGoogleSignIn(Intent data){ // handle google login results
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class); // get google account
            String password = prefs.getString("googlePass", "googlePass"); // set default pass for google logins
            // fetch necessary data
            String name = account.getGivenName();
            String surname = account.getFamilyName();
            String email = account.getEmail();

            //create json and put email for http request
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            Intent home = new Intent(MainActivity.this, HomeActivity.class); // make new activity intent
            final String[] userID = {""}; // initialise userID variable

            //make login http request
            internetRequest.doRequest(url + "login.php", MainActivity.this, jsonObject, new RequestHandler() {
                @Override
                public void processResponse(String response) { // handle server response
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        boolean valid = false;
                        if(jsonArray.length() == 0){ // no account exists, create one in the database
                            jsonObject.put("FName", name);
                            jsonObject.put("LName", surname);
                            jsonObject.put("pass", password);

                            // make new request to register user
                            internetRequest.doRequest(url + "register.php", MainActivity.this, jsonObject, new RequestHandler() {
                                @Override
                                public void processResponse(String response) {
                                    if(!response.equals("Failed")){ // handle response for registration request
                                        // welcome toast
                                        Toast.makeText(MainActivity.this, "Welcome " +name+" "+surname, Toast.LENGTH_SHORT).show();
                                        home.putExtra("userID", response); // carry user id across activities
                                        Data.setUserID(Integer.parseInt(response)); // store user id in a central location
                                        startActivity(home); // change activities
                                    }
                                    else{ // error toast for failed response
                                        Toast.makeText(MainActivity.this, "An error has occurred, try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            return; // exit
                        }

                        // if there exists a user, find the right one
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jO = jsonArray.getJSONObject(i);
                            if (jO.getString("user_Password").equals(password)){
                                userID[0] = jO.getString("user_ID");
                                valid = true;
                                break;
                            }
                        }

                        if(valid){
                            Toast.makeText(MainActivity.this, "Welcome "+name+" "+surname, Toast.LENGTH_SHORT).show();
                            Intent home = new Intent(MainActivity.this, HomeActivity.class);
                            home.putExtra("userID", userID[0]);
                            startActivity(home);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "No Such Account Exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult: failed code= " + e.getStatusCode() + e.getStatus().toString());
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void changePassVisibility(EditText edtPass, ImageView sender){ // make passwords visible/invisible
        if(edtPass.getTag().equals("hidden")){ // use preset tag to determine visibility
            edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// make text visible
            sender.setImageResource(R.drawable.ic_baseline_visibility_off_24); // change image
            edtPass.setTag("visible"); // change tag
        }
        else{
            edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            sender.setImageResource(R.drawable.ic_baseline_visibility_24);
            edtPass.setTag("hidden");
        }
    }
}
