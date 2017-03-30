package com.example.practice.app.home.contacts;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.doman.Account;

import java.util.List;

/**
 * Created by AMOBBS on 2017/3/30.
 */

public class AddressBookAdapter extends BaseAdapter {
    private List<Account> mList;
    public AddressBookAdapter(List<Account> list) {
        mList = list;
    }
    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_friend, null);
            holder = new ViewHolder();
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.iv_photo= (ImageView) convertView.findViewById(R.id.iv_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = mList.get(position).getNickname();
        int state = mList.get(position).getState();
        int headimg=mList.get(position).getHeadimg();
        holder.iv_photo.setImageResource(headimg);

        holder.tv_nickname.setText("昵称： " + name);
        if (state == 1) {
            holder.tv_state.setText("状态： onLine");
            holder.tv_nickname.setTextColor(Color.RED);
            holder.tv_state.setTextColor(Color.RED);
        } else {
            holder.tv_state.setText("状态： offLine");
            holder.tv_nickname.setTextColor(Color.GRAY);
            holder.tv_state.setTextColor(Color.GRAY);
        }
        return convertView;
    }
    class ViewHolder {
        TextView tv_nickname;
        TextView tv_state;
        ImageView iv_photo;
    }
}

