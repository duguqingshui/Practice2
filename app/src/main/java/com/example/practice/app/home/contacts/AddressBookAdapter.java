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
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by AMOBBS on 2017/3/30.
 */

public class AddressBookAdapter extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    Date date = new Date();
    int todayTime = Integer.parseInt(sdf.format(date));
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
            holder.friends_nickname = (TextView) convertView.findViewById(R.id.friends_nickname);
            holder.friends_sex=(ImageView)convertView.findViewById(R.id.friends_sex);
            holder.friends_age=(TextView)convertView.findViewById(R.id.friends_age);
            holder.friends_state = (TextView) convertView.findViewById(R.id.friends_state);
            holder.friends_photo= (RoundedImageView) convertView.findViewById(R.id.friends_photo);
            holder.friends_sign=(TextView)convertView.findViewById(R.id.friends_sign);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = mList.get(position).getNickname();
        int state = mList.get(position).getState();
        int headimg=mList.get(position).getHeadimg();
        int sex=mList.get(position).getSex();
        String birthday=mList.get(position).getBirthday();
        try {
            Date date = sdf.parse(birthday);
            int  selectTime=Integer.parseInt(sdf.format(date));;
            int age=todayTime-selectTime;
            holder.friends_age.setText(age+"岁");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sign=mList.get(position).getSign();
        holder.friends_photo.setImageResource(headimg);
        holder.friends_nickname.setText("昵称： " + name);
        holder.friends_sign.setText(sign);
        if (state == 1) {
            holder.friends_state.setText("[onLine]");
            holder.friends_nickname.setTextColor(Color.RED);
            holder.friends_state.setTextColor(Color.RED);
        } else {
            holder.friends_state.setText("[offLine]");
            holder.friends_nickname.setTextColor(Color.GRAY);
            holder.friends_state.setTextColor(Color.GRAY);
        }
        if (sex==0){
            holder.friends_sex.setImageResource(R.drawable.man);
        }
        else {
            holder.friends_sex.setImageResource(R.drawable.woman);
        }
        return convertView;
    }
    class ViewHolder {
        TextView friends_nickname;
        TextView friends_state;
        RoundedImageView friends_photo;
        TextView friends_sign;
        ImageView friends_sex;
        TextView friends_age;
    }
}

