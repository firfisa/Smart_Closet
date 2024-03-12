package com.example.smart_closet.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.smart_closet.R;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        findViewById(R.id.btn_userChange).setOnClickListener(this);
        findViewById(R.id.btn_clothesChange).setOnClickListener(this);
        findViewById(R.id.btn_dataChange).setOnClickListener(this);
        findViewById(R.id.btn_adminChange).setOnClickListener(this);
        findViewById(R.id.btn_addModel).setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_userChange:
                Intent intent = new Intent(AdminHomeActivity.this, AdminUserActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_clothesChange:
                Intent intent3 = new Intent(AdminHomeActivity.this, AdminClothesActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_dataChange:
                Intent intent4 = new Intent(AdminHomeActivity.this, AdminDataActivity.class);
                startActivity(intent4);
                break;
            case R.id.btn_adminChange:
                Intent intent1 = new Intent(AdminHomeActivity.this, AdminInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_addModel:
                Intent intent2 = new Intent(AdminHomeActivity.this, AddModelActivity.class);
                startActivity(intent2);
                break;
        }
    }
}