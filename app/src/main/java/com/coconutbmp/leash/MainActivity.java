package com.coconutbmp.leash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showSignIn = findViewById(R.id.btnShowSignIn);
        Button showSignUp = findViewById(R.id.btnShowSignUp);
        ImageView closeSignIn = findViewById(R.id.imgCloseSignIn);
        ImageView closeSignUp = findViewById(R.id.imgCloseSignUp);
        TextView signUpFromSignIn = findViewById(R.id.lblOpenSignUp);
        CardView signInCard = findViewById(R.id.SignInCard);
        CardView signUpCard = findViewById(R.id.SignUpCard);

        showSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpCard.setVisibility(View.GONE);
                signInCard.setVisibility(View.VISIBLE);
            }
        });

        showSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCard.setVisibility(View.GONE);
                signUpCard.setVisibility(View.VISIBLE);
            }
        });

        closeSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCard.setVisibility(View.GONE);
            }
        });

        closeSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpCard.setVisibility(View.GONE);
            }
        });

        signUpFromSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInCard.setVisibility(View.GONE);
                signUpCard.setVisibility(View.VISIBLE);
            }
        });
    }
}