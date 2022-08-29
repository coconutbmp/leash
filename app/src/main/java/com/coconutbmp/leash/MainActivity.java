package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    boolean validLogIn, validSignUp;
    Button openSignIn, openSignUp;
    ImageView closeSignIn, closeSignUp;
    TextView moveSignIn, moveSignUp;
    CardView SignInCard, SignUpCard, googleLogIn, facebookLogIn;
    CheckBox StaySignedIn;
    EditText logInEmail, logInPass;

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

        logInEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!validateEmail()){
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
                if(!validatePass()){
                    logInPass.getBackground().mutate().setColorFilter(Color.rgb(225, 80, 50), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    logInPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });
    }

    boolean validateEmail(){
        String email = logInEmail.getText().toString();
        if(!email.contains("@") || !email.contains(".")){
            return false;
        }
        return true;
    }

    boolean validatePass(){
        String pass = logInPass.getText().toString();
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
}