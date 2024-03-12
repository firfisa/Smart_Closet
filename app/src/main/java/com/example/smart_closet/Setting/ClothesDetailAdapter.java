package com.example.smart_closet.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.smart_closet.R;

import java.util.List;

public class ClothesDetailAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final int mResource;


    public ClothesDetailAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context); // 初始化 mInflater
        mResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
        }
        // 绑定数据
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        String item = getItem(position);
        nameTextView.setText(item);
        return convertView;
    }
}
