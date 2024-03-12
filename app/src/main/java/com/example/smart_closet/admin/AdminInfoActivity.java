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
import com.example.smart_closet.domain.Administrator;

import java.util.List;

public class AdminInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private DBOpenHelper dbHelper;
    private LinearLayout ll_admin_info_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 获得数据库帮助器的实例
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        dbHelper.openWriteLink();
        dbHelper.openReadLink();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);

        ll_admin_info_all = findViewById(R.id.ll_admin_info_all);

        findViewById(R.id.btn_admin_back).setOnClickListener(this);

        LayoutInflater inflater = getLayoutInflater();
        List<Administrator> admins = dbHelper.findAllAdmin();
        for (Administrator admin : admins) {
            Button button = (Button) inflater.inflate(R.layout.admin_info_item, null);
            button.setText(admin.getAccount());
            ll_admin_info_all.addView(button);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_admin_back) {
            finish();
        }
    }
}