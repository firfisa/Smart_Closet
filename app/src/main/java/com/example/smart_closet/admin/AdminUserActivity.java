package com.example.smart_closet.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.User;

import java.util.List;

public class AdminUserActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout admin_user_all;
    private Intent intent;
    private DBOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 获得数据库帮助器的实例
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        dbHelper.openWriteLink();
        dbHelper.openReadLink();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        admin_user_all = findViewById(R.id.admin_user_all);
        findViewById(R.id.btn_admin_user_back).setOnClickListener(this);

        LayoutInflater inflater = getLayoutInflater();
        List<User> users = dbHelper.findAllUser();
        for (User user : users) {
            Button button = (Button) inflater.inflate(R.layout.admin_user_item, null);
            button.setText(user.getUsername());
            admin_user_all.addView(button);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_admin_user_back:
                finish();
                break;
        }
    }
}