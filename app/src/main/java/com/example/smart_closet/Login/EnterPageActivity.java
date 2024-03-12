package com.example.smart_closet.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import com.example.smart_closet.R;
import com.example.smart_closet.HomePage.UserHomeActivity;

public class EnterPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_page);
        RadioButton agree = findViewById(R.id.rb_agree);
        findViewById(R.id.btn_enter).setOnClickListener(view -> {
            if (agree.isChecked()) {
                System.out.println(1);
                Intent intent = new Intent(EnterPageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                System.out.println(0);
            }
        });
    }
}