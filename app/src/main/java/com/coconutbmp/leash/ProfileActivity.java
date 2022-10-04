package com.coconutbmp.leash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ProfileActivity extends AppCompatActivity {
    CardView returnCard, editCard, deleteCard;
    TextView name, surname, email, budgets, incomes, expenses, liabilities, transactions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        hook();

        returnCard.setOnClickListener(view->{
            this.finish();
        });
    }

    public void hook(){
        returnCard = findViewById(R.id.profileReturnCard);
        editCard = findViewById(R.id.profileEditCard);
        deleteCard = findViewById(R.id.profileDeleteCard);
    }
}
