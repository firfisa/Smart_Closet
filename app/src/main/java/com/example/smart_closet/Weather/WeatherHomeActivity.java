package com.example.smart_closet.Weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smart_closet.R;
import com.example.smart_closet.domain.WeatherData;
import com.example.smart_closet.domain.WeatherResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String URL_BASE = "https://www.yiketianqi.com/free/week?unescape=1&appid=82412442&appsecret=3ZnxcK0X&city=";
    private Button btWeaAdd;
    private String TAG = "ning";
    public String city = "台州";
    private EditText etAdd;
    private TextView cityTv;
    private TextView temTv;
    private TextView weaTv;
    private TextView windTv;
    private TextView item_date1;
    private TextView item_date2;
    private TextView item_date3;
    private ImageView item_icon1;
    private ImageView item_icon2;
    private ImageView item_icon3;
    private TextView item_temRange1;
    private TextView item_temRange2;
    private TextView item_temRange3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_page);
        btWeaAdd = findViewById(R.id.bu_wea_add);
        etAdd = findViewById(R.id.et_wea_add);
        cityTv = findViewById(R.id.city);
        temTv = findViewById(R.id.tem);
        weaTv = findViewById(R.id.wea);
        windTv = findViewById(R.id.wind);
        item_date1 = findViewById(R.id.item_date1);
        item_date2 = findViewById(R.id.item_date2);
        item_date3 = findViewById(R.id.item_date3);

        item_icon1 = findViewById(R.id.item_icon1);
        item_icon2 = findViewById(R.id.item_icon2);
        item_icon3 = findViewById(R.id.item_icon3);
        item_temRange1 = findViewById(R.id.item_temRange1);
        item_temRange2 = findViewById(R.id.item_temRange2);
        item_temRange3 = findViewById(R.id.item_temRange3);
        btWeaAdd.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(etAdd.getText().toString() != null && etAdd.getText().length()!= 0){
            city = etAdd.getText().toString();
        }
        try {
            ChangeInfo(city);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ChangeInfo(String city) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL_BASE + city).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败" + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String responseDate = response.body().string();
                    Gson gson = new Gson();
                    WeatherResult weatherResult = gson.fromJson(responseDate, WeatherResult.class);
                    runOnUiThread(() -> {
                        String city1 = "%s市";
                        String temInfo = "%s°";
                        String windInfo = "%s%s";
                        if (weatherResult == null) {
                            return;
                        }
                        if (weatherResult.getData() == null) {
                            return;
                        }
                        WeatherData weatherData = weatherResult.getData().get(0);
                        cityTv.setText(String.format(city1,weatherResult.getCity()));
                        temTv.setText(String.format(temInfo,weatherData.getTem_day()));
                        weaTv.setText(String.format(weatherData.getWea()));
                        windTv.setText(String.format(windInfo,weatherData.getWin(),weatherData.getWin_speed()));
                        String wea_img = weatherData.getWea_img();
                        //获取未来三天的天气情况，加载到layout当中
                        List<WeatherData> futureList = weatherResult.getData();
                        String temRange = "%s°-%s°";
                        item_date1.setText(futureList.get(0).getDate());
                        item_temRange1.setText(String.format(temRange,futureList.get(0).getTem_night(),futureList.get(0).getTem_day()));
                        changeIcon(item_icon1,futureList.get(0).getWea_img());

                        item_date2.setText(futureList.get(1).getDate());
                        item_temRange2.setText(String.format(temRange,futureList.get(1).getTem_night(),futureList.get(1).getTem_day()));
                        changeIcon(item_icon2,futureList.get(1).getWea_img());

                        item_date3.setText(futureList.get(2).getDate());
                        item_temRange3.setText(String.format(temRange,futureList.get(2).getTem_night(),futureList.get(2).getTem_day()));
                        changeIcon(item_icon3,futureList.get(2).getWea_img());
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bu_wea_add:
                if(etAdd.getText().toString() != null && etAdd.getText().length()!= 0){
                    city = etAdd.getText().toString();
                }
                ChangeInfo(city);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    private void changeIcon(ImageView item_icon1,String weaImg){

        switch (weaImg) {
            case "xue":
                item_icon1.setBackground(getDrawable(R.drawable.xue_icon));break;
            case "yu":
                item_icon1.setBackground(getDrawable(R.drawable.yu_icon));break;
            case "lei":
                item_icon1.setBackground(getDrawable(R.drawable.lei_icon));break;
            case "wu":
                item_icon1.setBackground(getDrawable(R.drawable.wu_icon));break;
            case "qing":
                item_icon1.setBackground(getDrawable(R.drawable.qing_icon));break;
            case "yin":
                item_icon1.setBackground(getDrawable(R.drawable.yin_icon));break;
            case "yun":
                item_icon1.setBackground(getDrawable(R.drawable.yun_icon));break;
            case "shachen":
                item_icon1.setBackground(getDrawable(R.drawable.sand_icon));break;
            case "bingbao":
                item_icon1.setBackground(getDrawable(R.drawable.bingbao_icon));break;
        }
    }
}