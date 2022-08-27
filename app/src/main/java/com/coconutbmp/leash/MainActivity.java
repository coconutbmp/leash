package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    Button openSignIn, openSignUp;
    ImageView closeSignIn, closeSignUp;
    TextView moveSignIn, moveSignUp;
    CardView SignInCard, SignUpCard, googleLogIn, facebookLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}