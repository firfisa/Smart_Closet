package com.example.smart_closet.WardrobePage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.example.smart_closet.R;
import com.example.smart_closet.Room.RoomPageActivity;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Room;
import com.example.smart_closet.domain.Wardrobe;

import java.util.List;

public class WardrobePageActivity extends AppCompatActivity {

    private GridLayout roomLayout;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe_page);


        roomLayout = findViewById(R.id.gl_add_wardrobe);
        // get the room id
        roomId = getIntent().getIntExtra("ROOM_ID", 0);

        // click the add wardrobe button
        findViewById(R.id.btn_add_wardrobe).setOnClickListener(view -> {
            Intent intent = new Intent(WardrobePageActivity.this, AddWardrobeActivity.class);
            intent.putExtra("ROOM_ID", roomId);
            startActivity(intent);
        });

        findViewById(R.id.ib_wardrobe_back).setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        flush_wardrobe();
    }

    private void flush_wardrobe() {
        System.out.println("flush_wardrobe");

        roomLayout.removeAllViewsInLayout();

        DBOpenHelper dbOpenHelper;
        dbOpenHelper = DBOpenHelper.getInstance(this);
        dbOpenHelper.openReadLink();
        List<Wardrobe> wardrobes = dbOpenHelper.getWareDrobesByRoomId(roomId);
        if (wardrobes == null) return;

        for (Wardrobe wardrobe : wardrobes) {
            Button myButton = addButton(wardrobe.getName(), wardrobe.getId(), wardrobe.getClothesNum());
            roomLayout.addView(myButton);
            myButton.setOnClickListener(v -> {
                Intent intent = new Intent(WardrobePageActivity.this, ClothesDisplayPageActivity.class);
                intent.putExtra("WARDROBE_ID", wardrobe.getId());
                intent.putExtra("ROOM_ID", roomId);
                startActivity(intent);
            });
            myButton.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(WardrobePageActivity.this);
                builder.setTitle("是否删除这个衣柜")
                        .setPositiveButton("确定", (dialog, which) -> {
                            // 在这里写点击确定按钮后的操作
                            dbOpenHelper.deleteWardrobe(wardrobe.getId());
                            flush_wardrobe();
                        })
                        .setNegativeButton("手滑了", (dialog, which) -> {
                            // 在这里写点击取消按钮后的操作
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            });
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private Button addButton(String wardrobeName, int id, int clothesNum) {
        Button myButton = new Button(this);
        myButton.setId(id);
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(147));
        linearLayout.topMargin = dpToPx(20);
        myButton.setLayoutParams(linearLayout); // 将dp值转换为像素值
        myButton.setPaddingRelative(dpToPx(32), dpToPx(32), dpToPx(32), dpToPx(32));
        myButton.setBackgroundResource(R.drawable.bg_round);
        myButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wardrobe_logo, 0, 0, 0);
        myButton.setCompoundDrawablePadding(dpToPx(20));

        @SuppressLint("DefaultLocale") String buttonText = String.format("%s\n%s%d%s", wardrobeName,"(", clothesNum, ")");
        myButton.setText(buttonText);
        myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        return myButton;
    }
}