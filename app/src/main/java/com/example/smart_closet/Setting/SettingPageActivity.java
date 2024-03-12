package com.example.smart_closet.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.User;

import java.util.ArrayList;
import java.util.List;

public class SettingPageActivity extends AppCompatActivity {

    private Spinner spinner_setting_clothes_type;
    private Spinner spinner_setting_clothes_detail;
    private TextView textView_spinner_title;
    private EditText et_setting_city;
    private EditText et_setting_age;
    private EditText et_setting_body_height;
    private EditText et_setting_body_weight;
    private EditText et_setting_sex;
    private DBOpenHelper dbOpenHelper;
    private int account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        account_id = getIntent().getIntExtra("account_id", 0);
        System.out.println("account_id: " + account_id+ "====================================");

        List<String[]> clothesDetailList = new ArrayList<>();
        clothesDetailList.add(new String[]{"衬衫", "T恤", "卫衣/套头衫", "背心", "夹克/外套", "西装" });
        clothesDetailList.add(new String[]{"牛仔裤", "休闲裤", "运动裤", "裙子", "短裤" });

        spinner_setting_clothes_type = findViewById(R.id.spinner_setting_clothes_type);
        spinner_setting_clothes_detail = findViewById(R.id.spinner_setting_clothes_detail);
        textView_spinner_title = findViewById(R.id.textView_spinner_title);
        et_setting_city = findViewById(R.id.et_setting_city);
        et_setting_age = findViewById(R.id.et_setting_age);
        et_setting_body_height = findViewById(R.id.et_setting_body_height);
        et_setting_body_weight = findViewById(R.id.et_setting_body_weight);
        et_setting_age = findViewById(R.id.et_setting_age);
        et_setting_sex = findViewById(R.id.et_setting_sex);

        findViewById(R.id.ib_setting_back).setOnClickListener(view -> {
            String city = et_setting_city.getText().toString();
            int age = Integer.parseInt(et_setting_age.getText().toString());
            String body_height = et_setting_body_height.getText().toString();
            String body_weight = et_setting_body_weight.getText().toString();
            String sex = et_setting_sex.getText().toString();

            dbOpenHelper.updateUserInfo(account_id, city, age, body_height, body_weight, sex);
            finish();
        });

        setUserInfo();

        spinner_setting_clothes_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] clothesDetailArray = clothesDetailList.get(position);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingPageActivity.this,
                        R.layout.item_clothes_detail, clothesDetailArray);
                spinner_setting_clothes_detail.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 这个方法会在 Spinner 第一次创建时调用
                // 可以在这里进行一些默认操作
            }
        });

        spinner_setting_clothes_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selectedItemText = parent.getItemAtPosition(position).toString();
                textView_spinner_title.setText(selectedItemText);
                spinner_setting_clothes_detail.setSelection(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void setUserInfo() {

        dbOpenHelper = new DBOpenHelper(this);
        dbOpenHelper.openReadLink();
        dbOpenHelper.openWriteLink();
        System.out.println("dbOpenHelper: " + dbOpenHelper + "====================================");

        User user = dbOpenHelper.getUserInfo(account_id);
        if (user == null) {
            return;
        }
        if (user.getCity() != null) {
            et_setting_city.setText(user.getCity());
        }
        if (user.getAge() != 0) {
            et_setting_age.setText(String.valueOf(user.getAge()));
        }
        if (user.getHeight() != null) {
            et_setting_body_height.setText(user.getHeight());
        }
        if (user.getWeight() != null) {
            et_setting_body_weight.setText(user.getWeight());
        }
        if (user.getSex() != null) {
            et_setting_sex.setText(user.getSex());
        }
    }


}