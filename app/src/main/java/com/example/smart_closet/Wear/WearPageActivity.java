package com.example.smart_closet.Wear;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Clothes;

import java.util.List;

public class WearPageActivity extends AppCompatActivity implements View.OnClickListener {

    private DBOpenHelper dbHelper;
    private Button btn_wear_page_spring;
    private Button btn_wear_page_summer;
    private Button btn_wear_page_autumn;
    private Button btn_wear_page_winter;
    private ImageButton ib_wear_page_back;
    private ImageButton btn_wear_page_recent;
    private Button[] btnArgs;
    private GridLayout rootLayout;
    private int weatherStatus = 0;
    private int mode = 0;


    protected void onStart() {
        super.onStart();
        // 获得数据库帮助器的实例
        dbHelper = DBOpenHelper.getInstance(this);
        // 打开数据库帮助器的读连接
        dbHelper.openReadLink();
        dbHelper.openWriteLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear_page);

        rootLayout = findViewById(R.id.gl_clothes_weather_display);

        btn_wear_page_spring = findViewById(R.id.btn_wear_page_spring);
        btn_wear_page_summer = findViewById(R.id.btn_wear_page_summer);
        btn_wear_page_autumn = findViewById(R.id.btn_wear_page_autumn);
        btn_wear_page_winter = findViewById(R.id.btn_wear_page_winter);
        btn_wear_page_recent = findViewById(R.id.btn_wear_page_recent);
        ib_wear_page_back = findViewById(R.id.ib_wear_page_back);

        btn_wear_page_spring.setOnClickListener(this);
        btn_wear_page_summer.setOnClickListener(this);
        btn_wear_page_autumn.setOnClickListener(this);
        btn_wear_page_winter.setOnClickListener(this);
        btn_wear_page_recent.setOnClickListener(this);
        ib_wear_page_back.setOnClickListener(this);

        btnArgs=new Button[]{btn_wear_page_spring,btn_wear_page_summer,btn_wear_page_autumn,btn_wear_page_winter};

    }

    @Override
    protected void onResume() {
        super.onResume();
        flushClothesDisplay();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_wear_page_back:
                System.out.println(mode);
                System.out.println("====================================");
                if (mode == 0) {
                    finish();
                } else {
                    mode = 0;
                    flushClothesDisplay();
                }
                break;
            case R.id.btn_wear_page_spring:
                weatherStatus = 0;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.btn_wear_page_summer:
                weatherStatus = 1;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.btn_wear_page_autumn:
                weatherStatus = 2;
                MakeButtonBlue();
                flushClothesDisplay();
                break;
            case R.id.btn_wear_page_winter:
                weatherStatus = 3;
                MakeButtonBlue();
                flushClothesDisplay();
                break;

            case R.id.btn_wear_page_recent:
                mode = 1;
                flushClothesDisplay();
                break;
        }
    }

    public void MakeButtonBlue() {
        for (int i = 0; i < btnArgs.length; i++) {
            if (i == weatherStatus) {
                btnArgs[i].setBackgroundColor(new Color().parseColor("#51A7E5"));
                btnArgs[i].setTextColor(Color.WHITE);
            } else {
                btnArgs[i].setBackgroundColor(Color.WHITE);
                btnArgs[i].setTextColor(new Color().parseColor("#51A7E5"));
            }
        }
    }

    private String getSeason() {
        String season = "";

        switch (weatherStatus) {
            case 0:
                season = "春季";
                break;
            case 1:
                season = "夏季";
                break;
            case 2:
                season = "秋季";
                break;
            case 3:
                season = "冬季";
                break;
        }

        return season;
    }

    private void flushClothesDisplay() {
        String season = getSeason();
        List<Clothes> clothesList;
        rootLayout.removeAllViews();

        clothesList = dbHelper.getClothesBySeason(season, mode);

        if (clothesList == null) {
            return;
        }

        for (Clothes clothe : clothesList) {

            System.out.println("ClothesMame: " + clothe.getName());
            System.out.println("--------------------");

            addButton(clothe);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void addButton(Clothes clothe) {

        Context context = getApplicationContext();

        LinearLayout layoutClothesDisplay = new LinearLayout(context);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = dpToPx(120);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置相邻 LinearLayout 之间的间距
        layoutParams.setMargins(dpToPx(26), dpToPx(20), dpToPx(26), dpToPx(20));
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutClothesDisplay.setLayoutParams(layoutParams);
        layoutClothesDisplay.setOrientation(LinearLayout.VERTICAL);
        layoutClothesDisplay.setBackgroundResource(R.drawable.bg_round);

        // 获取衣物图片的字节数组
        byte[] imageBytes = clothe.getImage();

// 将字节数组转为 Bitmap 对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        // 添加衣物图片
        ImageView ivClothesDisplay = new ImageView(context);
        ivClothesDisplay.setId(View.generateViewId());
        LinearLayout.LayoutParams ivClothesDisplayLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivClothesDisplayLayoutParams.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(5));
        ivClothesDisplay.setLayoutParams(ivClothesDisplayLayoutParams);
//        ivClothesDisplay.setAdjustViewBounds(true); // 设置图片自适应布局
        ivClothesDisplay.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        ivClothesDisplay.setImageResource(R.drawable.clothes_1); // 设置图片资源
        ivClothesDisplay.setImageBitmap(bitmap);
        layoutClothesDisplay.addView(ivClothesDisplay); // 添加到LinearLayout中

//         添加衣物名称
        TextView tvClothesDisplay = new TextView(context);
        tvClothesDisplay.setId(View.generateViewId());
        tvClothesDisplay.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvClothesDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        tvClothesDisplay.setTextColor(getResources().getColor(android.R.color.black));
        tvClothesDisplay.setText(clothe.getName());
        tvClothesDisplay.setGravity(Gravity.CENTER);
        layoutClothesDisplay.addView(tvClothesDisplay); // 添加到 LinearLayout 中

        // 添加“穿戴”按钮
        Button btnWear = new Button(context);
        btnWear.setId(View.generateViewId());
        LinearLayout.LayoutParams btnWearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnWearLayoutParams.setMargins(dpToPx(15), dpToPx(10), dpToPx(15), dpToPx(15));
        btnWear.setLayoutParams(btnWearLayoutParams);
        btnWear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        btnWear.setTextColor(getResources().getColor(android.R.color.white));

        if ( mode == 0 ) {
            btnWear.setText("穿戴");
            btnWear.setOnClickListener(view -> {
                // 点击穿戴按钮，将衣物添加到穿戴列表中
                dbHelper.addClothesToWear(clothe.getId());
                Toast.makeText(context, "穿戴成功", Toast.LENGTH_SHORT).show();
                flushClothesDisplay();
            });
        } else {
            btnWear.setText("取消穿戴");
            btnWear.setOnClickListener(view -> {
                // 点击穿戴按钮，将衣物添加到穿戴列表中
                dbHelper.cancelClothesToWear(clothe.getId());
                Toast.makeText(context, "取消穿戴成功", Toast.LENGTH_SHORT).show();
                flushClothesDisplay();
            });
        }

        btnWear.setBackgroundResource(R.drawable.btn_round);
        btnWear.setGravity(Gravity.CENTER);

        layoutClothesDisplay.addView(btnWear); // 添加到 LinearLayout 中

        rootLayout.addView(layoutClothesDisplay);
    }
}