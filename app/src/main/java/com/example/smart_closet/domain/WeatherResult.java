package com.example.smart_closet.domain;

import java.util.List;

public class WeatherResult {
    private String city;
    private String cityid;
    private List<WeatherData> data;
    private int nums;
    private String update_time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public List<WeatherData> getData() {
        return data;
    }

    public void setData(List<WeatherData> data) {
        this.data = data;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getUpdate_time() {
        return update_time;
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "city='" + city + '\'' +
                ", cityid='" + cityid + '\'' +
                ", data=" + data +
                ", nums=" + nums +
                ", update_time='" + update_time + '\'' +
                '}';
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
