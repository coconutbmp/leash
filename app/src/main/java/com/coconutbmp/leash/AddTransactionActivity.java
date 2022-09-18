package com.coconutbmp.leash;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddTransactionActivity extends AppCompatActivity {

    CardView returnCard;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        returnCard = findViewById(R.id.transactionReturnCard);
        returnCard.setOnClickListener(view -> {
            AddTransactionActivity.this.finish();
        });
    }
}
