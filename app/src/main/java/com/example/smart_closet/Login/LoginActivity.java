package com.example.smart_closet.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_closet.HomePage.UserHomeActivity;
import com.example.smart_closet.R;
import com.example.smart_closet.admin.AdminHomeActivity;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Administrator;
import com.example.smart_closet.domain.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{
    private android.widget.Spinner Spinnerone;
    private ArrayAdapter<String> SpinnerAdapter;
    private List<String> Spinner;
    private EditText EtID;
    private EditText EtPasswd;
    private Button btn_Login;
    private TextView txt_userregister;
    private TextView txt_forgetpasswd;
    private ImageView btn_back;
    private Intent intent;
    private DBOpenHelper dbHelper;
    private User user;
    private Administrator administrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // 获得数据库帮助器的实例
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读写连接
        dbHelper.openWriteLink();
        dbHelper.openReadLink();

        Spinnerone = (android.widget.Spinner)findViewById(R.id.Spinnerone);
        EtID = (EditText)findViewById(R.id.EtID);
        EtPasswd =(EditText)findViewById(R.id.EtPasswd);

        findViewById(R.id.txt_userregister).setOnClickListener(this);
        findViewById(R.id.txt_forgetpasswd).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        //点击登录按钮
        findViewById(R.id.btn_Login).setOnClickListener(this);

//        SpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, Spinner);
//        SpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
//        Spinnerone.setAdapter(SpinnerAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //点击新用户注册
            case R.id.txt_userregister:
                intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
                startActivity(intent);
                break;

            //点击忘记密码
            case R.id.txt_forgetpasswd:
                intent = new Intent(LoginActivity.this, EditPasswordActivity.class);
                startActivity(intent);
                break;

            //返回按钮
            case R.id.btn_back:
                intent = new Intent(LoginActivity.this, EnterPageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_Login:
                String account_load = EtID.getText().toString();
                String password_load = EtPasswd.getText().toString();
                //进行判断
                if(Spinnerone.getSelectedItem().toString().trim().equals("管理员")) {
//
                    if(dbHelper.queryAdministrator(account_load,password_load)) {
                        //登录成功转到用户首页并携带用户的名字过去
                        Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        intent.putExtra("account_name",EtID.getText().toString());
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(Spinnerone.getSelectedItem().toString().trim().equals("普通用户"))
                {
//                    去数据库中找账号为account的数据
                    if(dbHelper.queryUser(account_load,password_load)) {
                        //登录成功转到用户首页并携带用户的名字过去
                        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                        intent.putExtra("user_name",EtID.getText().toString());
                        intent.putExtra("account_id", dbHelper.getUserId(account_load));
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this,"您还没有选择任何登录类型！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}