package com.example.smart_closet.WardrobePage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Clothes;

import java.util.List;

public class ClothesDisplayPageActivity extends AppCompatActivity {

    private int wardrobe_id;
    private int room_id;
    private GridLayout rootLayout;
    private ScrollView scrollView;
    private TextView tv_wardrobe_title;
    private int flag = 1;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_display_page);

        wardrobe_id = getIntent().getIntExtra("WARDROBE_ID", 0);
        room_id = getIntent().getIntExtra("ROOM_ID", 0);

        rootLayout = findViewById(R.id.gl_clothes_display);
        scrollView = findViewById(R.id.sv_clothes_display);

        tv_wardrobe_title = findViewById(R.id.tv_wardrobe_title);

        findViewById(R.id.ib_clothes_display_back).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.btn_add_clothes).setOnClickListener(view -> {
            Intent intent = new Intent(ClothesDisplayPageActivity.this, AddClothesActivity.class);
            intent.putExtra("WARDROBE_ID", wardrobe_id);
            intent.putExtra("ROOM_ID", room_id);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        flush_clothes();
        // 将 ScrollView 滚动到最底部
        if (flag == 1) {
            flag = 0;
            return;
        }
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbOpenHelper = DBOpenHelper.getInstance(this);
        dbOpenHelper.openReadLink();
    }

    private void flush_clothes() {
        System.out.println("flush_clothes");
        rootLayout.removeAllViewsInLayout();

        List<Clothes> clothesList = dbOpenHelper.getClothesByWardrobeId(wardrobe_id);

        String wardrobe_name = dbOpenHelper.getWardrobeNameByID(wardrobe_id);
        tv_wardrobe_title.setText(wardrobe_name);

        if (clothesList == null) {
            return;
        }

        for (Clothes clothes : clothesList) {
//            System.out.println("--------------------");
//            System.out.println(clothes.getName());
//            System.out.println(clothes.getColor());
//            System.out.println(clothes.getWardrobeId());
//            System.out.println("--------------------");

            addButton(clothes);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // 动态添加衣物按钮
    private void addButton(Clothes clothe) {

        Context context = getApplicationContext();

        LinearLayout layoutClothesDisplay = new LinearLayout(context);
        layoutClothesDisplay.setId(View.generateViewId());

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = dpToPx(120);
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        // 设置相邻 LinearLayout 之间的间距
        layoutParams.setMargins(dpToPx(26), dpToPx(26), dpToPx(26), dpToPx(26));
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        layoutClothesDisplay.setLayoutParams(layoutParams);
        layoutClothesDisplay.setOrientation(LinearLayout.VERTICAL);
        layoutClothesDisplay.setBackgroundResource(R.drawable.bg_round);

        layoutClothesDisplay.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ClothesDisplayPageActivity.this);
            builder.setTitle("是否删除这件衣物")
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 在这里写点击确定按钮后的操作
                        dbOpenHelper.deleteClothes(clothe.getId());
                        flush_clothes();
                    })
                    .setNegativeButton("手滑了", (dialog, which) -> {
                        // 在这里写点击取消按钮后的操作
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });

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
        ivClothesDisplay.setImageBitmap(bitmap); // 设置图片资源
        layoutClothesDisplay.addView(ivClothesDisplay); // 添加到LinearLayout中

        // 添加衣物名称
        TextView tvClothesDisplay = new TextView(context);
        tvClothesDisplay.setId(View.generateViewId());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.setMargins(dpToPx(15), dpToPx(15), dpToPx(15), dpToPx(15));
        tvClothesDisplay.setLayoutParams(layoutParams1);
        tvClothesDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        tvClothesDisplay.setTextColor(getResources().getColor(android.R.color.black));
        tvClothesDisplay.setText(clothe.getName());
        tvClothesDisplay.setGravity(Gravity.CENTER);

        layoutClothesDisplay.addView(tvClothesDisplay); // 添加到 LinearLayout 中
        rootLayout.addView(layoutClothesDisplay);
    }
}