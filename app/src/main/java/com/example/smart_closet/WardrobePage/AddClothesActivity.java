package com.example.smart_closet.WardrobePage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.smart_closet.R;
import com.example.smart_closet.database.DBOpenHelper;
import com.example.smart_closet.domain.Clothes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddClothesActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 1002;
    private Spinner clothesTypeSpinner;
    private Spinner clothesSceneSpinner;
    private Spinner clothesSeasonSpinner;
    private EditText et_clothes_name;
    private EditText et_clothes_color;
    private EditText et_clothes_num;
    private DBOpenHelper dbOpenHelper;
    private TextView tv_add_clothes_by_hand;
    private TextView tv_add_clothes_by_photo;
    private ImageView iv_add_clothes_photo;
    private ImageButton btn_add_clothes_by_link;
    private List<String> identity;
    private ArrayAdapter<String> identityAdapter;

    private int wardrobeId;
    private int roomId;
    private OkHttpClient okHttpClient;

    private static Context context;//全局上下文

    @Override
    protected void onStart() {
        super.onStart();
        dbOpenHelper = new DBOpenHelper(this);
        dbOpenHelper.openReadLink();
        dbOpenHelper.openWriteLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        clothesTypeSpinner = findViewById(R.id.clothesTypeSpinner);
        et_clothes_color = findViewById(R.id.et_clothes_color);
        et_clothes_name = findViewById(R.id.et_clothes_name);
        et_clothes_num = findViewById(R.id.et_clothes_num);
        clothesSceneSpinner = findViewById(R.id.clothesSceneSpinner);
        clothesSeasonSpinner = findViewById(R.id.clothesSeasonSpinner);
        tv_add_clothes_by_hand = findViewById(R.id.tv_add_clothes_by_hand);
        tv_add_clothes_by_photo = findViewById(R.id.tv_add_clothes_by_photo);
        iv_add_clothes_photo = findViewById(R.id.iv_add_clothes_photo);
        btn_add_clothes_by_link = findViewById(R.id.btn_add_clothes_by_link);

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        tv_add_clothes_by_hand.setOnClickListener(this);
        tv_add_clothes_by_photo.setOnClickListener(this);
        btn_add_clothes_by_link.setOnClickListener(this);

        findViewById(R.id.ib_add_clothes_back).setOnClickListener(view -> finish());

        wardrobeId = getIntent().getIntExtra("WARDROBE_ID", 0);
        roomId = getIntent().getIntExtra("ROOM_ID", 0);


        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        context = getApplicationContext();

        //类型选择
        initData1();
        identityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, identity);
        identityAdapter.setDropDownViewResource(R.layout.spinner_item);
        clothesSeasonSpinner.setAdapter(identityAdapter);
    }

    private void initData1() {
        identity = new ArrayList<>(3);
        identity.add("春季");
        identity.add("夏季");
        identity.add("秋季");
        identity.add("冬季");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btn_save):
                Clothes clothes = new Clothes();

                clothes.setType(clothesTypeSpinner.getSelectedItem().toString());
                clothes.setWardrobeId(wardrobeId);
                clothes.setColor(et_clothes_color.getText().toString());
                clothes.setName(et_clothes_name.getText().toString());
                clothes.setScene(clothesSceneSpinner.getSelectedItem().toString());
                clothes.setSeason(clothesSeasonSpinner.getSelectedItem().toString());

                iv_add_clothes_photo.setDrawingCacheEnabled(true);
                Bitmap bitmap = iv_add_clothes_photo.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                clothes.setImage(imageBytes);

                // 设置购买日期
//            clothes.setPurchaseDate(new Date(System.currentTimeMillis()));

                System.out.println("--------------------");
                System.out.println("name = " + et_clothes_name.getText().toString());
                System.out.println("type name = " + clothesTypeSpinner.getSelectedItem().toString());
                System.out.println("wardrobe id = " + wardrobeId);
                System.out.println("color = " + et_clothes_color.getText().toString());
                System.out.println("scene = " + clothesSceneSpinner.getSelectedItem().toString());
                System.out.println("season = " + clothesSeasonSpinner.getSelectedItem().toString());
                System.out.println("--------------------");

                // 添加衣服
                dbOpenHelper.addClothes(clothes);

                // 更新衣柜中衣服数量
                int clothesNum = Integer.parseInt(et_clothes_num.getText().toString());
                int clothesNumInWardrobe = dbOpenHelper.getClothesNumInWardrobe(wardrobeId);

                System.out.println("====================================");
                System.out.println("clothesNum = " + clothesNum);
                System.out.println("clothesNumInWardrobe = " + clothesNumInWardrobe);

                dbOpenHelper.updateClothesNumInWardrobe(wardrobeId, clothesNum + clothesNumInWardrobe);

                int clothesNumInRoom = dbOpenHelper.getClothesNumInRoom(roomId);
                dbOpenHelper.updateClothesNumInRoom(roomId, clothesNum + clothesNumInRoom);
                finish();
                break;
            case (R.id.btn_cancel):
                finish();
                break;
            case (R.id.tv_add_clothes_by_hand):
                // 打开用户相机
                openCamera();
                break;
            case (R.id.tv_add_clothes_by_photo):
                // 打开用户相册
                openPhotoLibrary();
                break;
            case (R.id.btn_add_clothes_by_link):
                // 弹出对话框，输入商品链接
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入衣物链接");
                builder.setView(R.layout.dialog_add_clothes_by_link);
                builder.setPositiveButton("确定", (dialogInterface, i) -> {
                    // 获取输入的链接
                    EditText et_link = ((AlertDialog) dialogInterface).findViewById(R.id.et_link);

                    String link = et_link.getText().toString();
                    if (TextUtils.isEmpty(link)) {
                        Toast.makeText(AddClothesActivity.this, "链接不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new GetClothesNumIDTask().execute(link);

                    System.out.println("====================================");
                    System.out.println("link = " + link);
                    System.out.println("====================================");

                });
                builder.setNegativeButton("取消", (dialogInterface, i) -> {
                });
                builder.show();
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetClothesNumIDTask extends AsyncTask<String, Void, Clothes> {
        String picUrl;
        String title;
        String color;
        String wather;

        @SuppressLint("WrongThread")
        @Override
        protected Clothes doInBackground(String... strings) {
            String num_iid = null;
            String link = strings[0];
            Clothes clothes = null;
            try {
                String url = "https://api-gw.onebound.cn/taobao/item_password/?key=t7805850841&word=urlencode(" + link +
                        ")&title=no&&lang=zh-CN&secret=20230410";
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .header("User-Agent", "OkHttp Example")
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();

                    System.out.println("====================================");
                    System.out.println("responseBody = " + responseBody);
                    System.out.println("====================================");

                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject itemObject = jsonObject.getJSONObject("item");
                    num_iid = itemObject.getString("num_iid");
                    System.out.println("====================================");
                    System.out.println("num_iid = " + num_iid);
                    System.out.println("====================================");
//                    clothes = new Clothes(title, price, shopName, picUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String url = "https://api-gw.onebound.cn/taobao/item_get/?key=t7805850841&&num_iid=" + num_iid +
                        "&is_promotion=1&&lang=zh-CN&secret=20230410";
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .header("User-Agent", "OkHttp Example")
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = Objects.requireNonNull(response.body()).string();

                    System.out.println("====================================");
                    System.out.println("responseBody = " + responseBody);
                    System.out.println("====================================");

                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject itemObject = jsonObject.getJSONObject("item");

                    title = itemObject.getString("title");
                    picUrl = itemObject.getString("pic_url");
                    picUrl = "https:" + picUrl;

                    JSONArray propsArr = itemObject.getJSONArray("props");
                    for (int i = 0; i < propsArr.length(); i++) {
                        JSONObject prop = propsArr.getJSONObject(i);
                        String name = prop.getString("name");
                        String value = prop.getString("value");

                        if (name.equals("颜色")) {
                            String firstColor = value.split(" ")[0]; // 获取第一个颜色
                            color = firstColor;
                            System.out.println("第一个颜色: " + firstColor);
                            System.out.println("颜色: " + value);
                        } else if (name.equals("适用季节")) {
                            wather = value;
                            System.out.println("使用季节: " + value);
                        }
                    }

                    System.out.println("====================================");
                    System.out.println("title = " + title);
                    System.out.println("picUrl = " + picUrl);
                    System.out.println("====================================");
                    new GetBitmapFromURLTask().execute(picUrl);

//                    ImageView imageView = findViewById(R.id.imageView)

//                    clothes = new Clothes(title, price, shopName, picUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return clothes;
        }

        @Override
        protected void onPostExecute(Clothes clothes) {
            super.onPostExecute(clothes);
            // 这里可以更新UI
            et_clothes_name.setText(title);
            et_clothes_color.setText(color);
            clothesSeasonSpinner.setSelection(getIndex(clothesSeasonSpinner, wather));
            System.out.println("====================================");
            System.out.println("wather = " + wather);
            System.out.println(getIndex(clothesSeasonSpinner, wather));
            System.out.println("====================================");

//            clothesTypeSpinner.setSelection(getIndex(clothesTypeSpinner, "上衣"));
//            clothesSceneSpinner.setSelection(getIndex(clothesSceneSpinner, "日常"));
//            clothesSeasonSpinner.setSelection(getIndex(clothesSeasonSpinner, "春夏"));
        }

        private class GetBitmapFromURLTask extends AsyncTask<String, Void, Bitmap> {

            Bitmap img = null;
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL(picUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    img = BitmapFactory.decodeStream(input);
                    return img;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap clothes) {
                super.onPostExecute(clothes);
                // 这里可以更新UI
                System.out.println("====================================");
                System.out.println("bitmap = " + img);
                System.out.println("====================================");
                iv_add_clothes_photo.setImageBitmap(img);
            }
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        if (myString == null) {
            return index;
        }
        myString = myString.trim();
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void openPhotoLibrary() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_REQUEST_CODE);
    }

    // 处理相册返回的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            // 处理选择的图片
            if (data != null) {
                Uri selectedImage = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    // 将图片显示到 ImageView 中
                    iv_add_clothes_photo.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // 处理拍照后的数据
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // 将图片显示到 ImageView 中
            iv_add_clothes_photo.setImageBitmap(imageBitmap);
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    // 根据链接获取图片
    public Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
