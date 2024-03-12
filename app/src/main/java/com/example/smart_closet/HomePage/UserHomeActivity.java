package com.example.smart_closet.HomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smart_closet.R;
import com.example.smart_closet.Recommend.RecommendPageActivity;
import com.example.smart_closet.Room.RoomPageActivity;
import com.example.smart_closet.Setting.SettingPageActivity;
import com.example.smart_closet.Wear.WearPageActivity;
import com.example.smart_closet.Weather.WeatherHomeActivity;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Clothes;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private DBOpenHelper dbHelper;
    private Button btn_homepage_casual;
    private Button btn_homepage_formal;
    private Button btn_homepage_sport;
    private ImageView imv_weather;
    private GridLayout rootLayout;
    private Button[] btnArgs;
    private int sceneStatus = 0;
    private int account_id;

    protected void onStart() {
        super.onStart();
        // 获得数据库帮助器的实例
        sceneStatus = 0;
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读连接
        dbHelper.openReadLink();
        flushClothesDisplay();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        rootLayout = findViewById(R.id.gl_clothes_scene_display);

        btn_homepage_casual = findViewById(R.id.btn_homepage_casual);
        btn_homepage_formal = findViewById(R.id.btn_homepage_formal);
        btn_homepage_sport = findViewById(R.id.btn_homepage_sport);
        btnArgs = new Button[]{btn_homepage_casual, btn_homepage_sport, btn_homepage_formal};

        findViewById(R.id.HomePageRoom).setOnClickListener(this);
        findViewById(R.id.HomePageWear).setOnClickListener(this);
        findViewById(R.id.HomePageRecommend).setOnClickListener(this);
        findViewById(R.id.HomePageSetting).setOnClickListener(this);
        findViewById(R.id.btn_homepage_casual).setOnClickListener(this);
        findViewById(R.id.btn_homepage_formal).setOnClickListener(this);
        findViewById(R.id.btn_homepage_sport).setOnClickListener(this);
        findViewById(R.id.imv_weather).setOnClickListener(this);

        account_id = getIntent().getIntExtra("account_id", 0);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.HomePageRoom:
                Intent intent = new Intent(UserHomeActivity.this, RoomPageActivity.class);
                startActivity(intent);
                break;
            case R.id.HomePageWear:
                Intent intent1 = new Intent(UserHomeActivity.this, WearPageActivity.class);
                startActivity(intent1);
                break;
            case R.id.HomePageRecommend:
                Intent intent4 = new Intent(UserHomeActivity.this, RecommendPageActivity.class);
                startActivity(intent4);
                break;
            case R.id.HomePageSetting:
                Intent intent3 = new Intent(UserHomeActivity.this, SettingPageActivity.class);
                intent3.putExtra("account_id", account_id);
                startActivity(intent3);
                break;
            case R.id.btn_homepage_casual:
                sceneStatus = 0;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.btn_homepage_sport:
                sceneStatus = 1;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.btn_homepage_formal:
                sceneStatus = 2;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.imv_weather:
                Intent intent2 = new Intent(UserHomeActivity.this, WeatherHomeActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void MakeButtonBlue() {
        for (int i = 0; i < 3; i++) {
            if (i == sceneStatus) {
                btnArgs[i].setBackgroundColor(new Color().parseColor("#51A7E5"));
                btnArgs[i].setTextColor(Color.WHITE);
            } else {
                btnArgs[i].setBackgroundColor(Color.WHITE);
                btnArgs[i].setTextColor(new Color().parseColor("#51A7E5"));
            }
        }
    }

    private String getScene() {
        switch (sceneStatus) {
            case 0:
                return "休闲";
            case 1:
                return "运动";
            case 2:
                return "正装";
        }
        return "casual";
    }

    private void flushClothesDisplay() {
        String scene = getScene();
        List<Clothes> clothesList;
        rootLayout.removeAllViews();

        clothesList = dbHelper.getClothesByScene(scene);

        if (clothesList == null) {
            return;
        }

        for (Clothes clothe : clothesList) {


            addView(clothe);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void addView(Clothes clothe) {
        Context context = getApplicationContext();
        LinearLayout layoutClothesDisplay = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(dpToPx(5), dpToPx(18), dpToPx(18), dpToPx(2));

        layoutClothesDisplay.setLayoutParams(layoutParams);
        layoutClothesDisplay.setBackgroundResource(R.drawable.bg_round);
        layoutClothesDisplay.setOrientation(LinearLayout.HORIZONTAL);

        // 获取衣物图片的字节数组
        byte[] imageBytes = clothe.getImage();

// 将字节数组转为 Bitmap 对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

// 创建 ImageView 并添加到 LinearLayout 中
        ImageView imageClothes = new ImageView(context);
        imageClothes.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(120), dpToPx(120)));
//        imageClothes.setImageResource(R.drawable.clothes_1); // 设置图片资源
        imageClothes.setImageBitmap(bitmap); // 设置图片资源
        imageClothes.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        layoutClothesDisplay.addView(imageClothes);

// 创建 TextView 并添加到 LinearLayout 中
        TextView textClothes = new TextView(context);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        textClothes.setLayoutParams(layoutParams1);

        @SuppressLint("DefaultLocale") String text = String.format("%s\n%s%s", clothe.getName(), "位置在：" , dbHelper.getWardrobeNameByID(clothe.getWardrobeId()));

        textClothes.setText(text);
        textClothes.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        textClothes.setPadding(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
        layoutClothesDisplay.addView(textClothes);

// 将该 LinearLayout 添加到 GridLayout 中
        rootLayout.addView(layoutClothesDisplay);
    }

}