package com.example.smart_closet.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Administrator;
import com.example.smart_closet.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TextView txt_Im_login;
    private Spinner IdentitySpinner;
    private ArrayAdapter<String> identityAdapter;
    private List<String> identity;
    private ImageView btn_back;
    private EditText EtNewID;
    private EditText EtNewPasswd;
    private Button btn_Im_register;
    private ContentValues values;
    private DBOpenHelper dbHelper;

    @Override
    protected void onStart() {
        super.onStart();
        // 获得数据库帮助器的实例
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        dbHelper.openWriteLink();
        dbHelper.openReadLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        IdentitySpinner = (Spinner)findViewById(R.id.IdentitySpinner);
        EtNewID = (EditText)findViewById(R.id.EtNewID);
        EtNewPasswd = (EditText)findViewById(R.id.EtNewPassd);
        findViewById(R.id.btn_Im_register).setOnClickListener(this);

        //类型选择
        initData1();
        identityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, identity);
        identityAdapter.setDropDownViewResource(R.layout.spinner_item);
        IdentitySpinner.setAdapter(identityAdapter);
        IdentitySpinner.setOnItemSelectedListener(this);

        //点击直接登录
        txt_Im_login = (TextView) findViewById(R.id.txt_Im_login);
        txt_Im_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //返回按钮
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initData1() {
        identity = new ArrayList<>(3);
        identity.add("请选择身份");
        identity.add("普通用户");
        identity.add("管理员");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //点击注册按钮
            case R.id.btn_Im_register:
                if (IdentitySpinner.getSelectedItem().toString().trim().equals("普通用户")) {
                    User user = new User();
                    final String account = EtNewID.getText().toString();
                    final String password = EtNewPasswd.getText().toString();

                    user.setUsername(account);
                    user.setPassword(password);

                    System.out.println("account:"+account);
                    System.out.println("password:"+password);

                    long l = dbHelper.addUser(user);
                    if (l>0){
                        Toast.makeText(UserRegisterActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
                    }
                }else if(IdentitySpinner.getSelectedItem().toString().trim().equals("管理员")){
                    Administrator administrator = new Administrator();
                    final String account = EtNewID.getText().toString();
                    final String password = EtNewPasswd.getText().toString();
                    administrator.setAccount(account);
                    administrator.setPassword(password);
                    long l = dbHelper.addAdministrator(administrator);
                    if (l>0){
                        Toast.makeText(UserRegisterActivity.this, "管理员注册成功", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}