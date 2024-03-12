package com.example.smart_closet.Room;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.example.smart_closet.R;
import com.example.smart_closet.WardrobePage.WardrobePageActivity;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Room;

import java.util.List;

public class RoomPageActivity extends AppCompatActivity {

    private GridLayout roomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_page);
        roomLayout = findViewById(R.id.gl_add_room);
        // 点击添加房间按钮
        findViewById(R.id.btn_add_room).setOnClickListener(view -> {
            Intent intent = new Intent(RoomPageActivity.this, AddRoomActivity.class);
            startActivity(intent);
        });

        // 点击返回按钮
        findViewById(R.id.ib_room_back).setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        flush_room();
    }

    // 刷新页面
    private void flush_room() {
        System.out.println("flush_room");

        // 清空页面
        roomLayout.removeAllViewsInLayout();

        // 重新加载页面
        DBOpenHelper dbOpenHelper;
        dbOpenHelper = DBOpenHelper.getInstance(this);
        dbOpenHelper.openWriteLink();
        dbOpenHelper.openReadLink();
        List<Room> rooms = dbOpenHelper.getAllRooms();
        if (rooms == null) {
            return;
        }

        // 添加按钮
        for (Room room : rooms) {
            Button myButton = addButton(room.getName(), room.getId(), room.getRoomClothesNum());
            roomLayout.addView(myButton);
            myButton.setOnClickListener(v -> {
                Intent intent = new Intent(RoomPageActivity.this, WardrobePageActivity.class);
                intent.putExtra("ROOM_ID", room.getId());
                startActivity(intent);
            });
            myButton.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomPageActivity.this);
                builder.setTitle("是否删除这个房间")
                        .setPositiveButton("确定", (dialog, which) -> {
                            // 在这里写点击确定按钮后的操作
                            dbOpenHelper.deleteRoom(room.getId());
                            flush_room();
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

    // 将dp值转换为像素值
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // 添加按钮
    private Button addButton(String roomName, int id, int clothesNum) {
        Button myButton = new Button(this);
        myButton.setId(id);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(147));
        layoutParams.topMargin = dpToPx(20);
        myButton.setLayoutParams(layoutParams); // 将dp值转换为像素值
        myButton.setPaddingRelative(dpToPx(32), dpToPx(32), dpToPx(32), dpToPx(32));
        myButton.setBackgroundResource(R.drawable.bg_round);
        myButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.room_logo, 0, 0, 0);
        myButton.setCompoundDrawablePadding(dpToPx(20));

        @SuppressLint("DefaultLocale") String buttonText = String.format("%s\n%s%d%s", roomName,"(", clothesNum, ")");
        myButton.setText(buttonText);
        myButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        return myButton;
    }

}