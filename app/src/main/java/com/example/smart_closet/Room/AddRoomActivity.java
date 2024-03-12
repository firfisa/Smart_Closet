
package com.example.smart_closet.Room;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Room;

public class AddRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        EditText editText = findViewById(R.id.et_room_name);
        findViewById(R.id.ib_addRoom_back).setOnClickListener(view -> {
            finish();
        });

        // click the add room button
        findViewById(R.id.btn_add_room).setOnClickListener(view -> {
            Room room = new Room();
            room.setName(editText.getText().toString());
            room.setRoomClothesNum(0);
            room.setDescription("nothing");
            DBOpenHelper dbHelper = DBOpenHelper.getInstance(this);
            dbHelper.openWriteLink();
            dbHelper.addRoom(room);
            finish();
        });
    }

}