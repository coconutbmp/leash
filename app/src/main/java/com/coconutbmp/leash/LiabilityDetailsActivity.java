package com.coconutbmp.leash;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LiabilityDetailsActivity extends AppCompatActivity {

    CardView returnCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liability_details);

        returnCard = findViewById(R.id.liabilityDetailReturnCard);

        returnCard.setOnClickListener(view -> {
            this.finish();
        });
    }
}
