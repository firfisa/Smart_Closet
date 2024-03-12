package com.example.smart_closet.WardrobePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Wardrobe;

public class AddWardrobeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wardrobe);
        EditText et_wardrobe_name = findViewById(R.id.et_wardrobe_name);
        int roomId = getIntent().getIntExtra("ROOM_ID", 0);

        findViewById(R.id.ib_addWardrobe_back).setOnClickListener(view -> {
            finish();
        });

        findViewById(R.id.btn_add_wardrobe).setOnClickListener(view -> {
            System.out.println(et_wardrobe_name.getText());
            Wardrobe wardrobe = new Wardrobe();
            wardrobe.setName(et_wardrobe_name.getText().toString());
            wardrobe.setWardrobeClothesNum(0);
            wardrobe.setDescription("nothing");
            wardrobe.setRoomId(getIntent().getIntExtra("ROOM_ID", roomId));
            DBOpenHelper dbHelper = DBOpenHelper.getInstance(this);
            dbHelper.openWriteLink();
            dbHelper.addWardrobe(wardrobe);
            finish();
        });
    }
}