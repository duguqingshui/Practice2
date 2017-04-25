package com.example.practice.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.doman.Messages;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by AMOBBS on 2017/4/12.
 */

public class ChatAdapter extends BaseAdapter {
    private List<Messages> msgList;
    //录音
    private MediaPlayer mPlay = null;
    private boolean isShowOrNot = false;
    public ChatAdapter(List<Messages> msgList) {
        this.msgList = msgList;
    }
    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Messages message= (Messages) getItem(position);
        int headimg= SpUtils.getInt(context,Constant.LOGIN_HEADIMAGE,0);//发送者头像
        int friend_headimg=SpUtils.getInt(context,"friend_headimg",0);//接受者头像
        String loginNickname=SpUtils.getString(context, Constant.LOGIN_NICKNAME, "");
        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_content_item, null);
            holder = new ViewHolder();
            holder.ll_left = (LinearLayout) convertView.findViewById(R.id.ll_left);
            holder.ll_right = (LinearLayout) convertView.findViewById(R.id.ll_right);
            holder.tv_left = (TextView) convertView.findViewById(R.id.tv_left);
            holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            holder.iv_left_img= (ImageView) convertView.findViewById(R.id.iv_left_img);
            holder.iv_left_record= (ImageView) convertView.findViewById(R.id.iv_left_record);
            holder.iv_right_img= (ImageView) convertView.findViewById(R.id.iv_right_img);
            holder.iv_right_record= (ImageView) convertView.findViewById(R.id.iv_right_record);
            holder.iv_left= (ImageView) convertView.findViewById(R.id.iv_left);
            holder.iv_right= (ImageView) convertView.findViewById(R.id.iv_right);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.iv_left.setImageResource(friend_headimg);
        holder.iv_right.setImageResource(headimg);
        //接受者
        if(msgList.get(position).getReceicer().getNickname().equals(loginNickname)){
            holder.ll_left.setVisibility(View.VISIBLE);
            holder.ll_right.setVisibility(View.GONE);
            switch (message.getType()){
                //文本消息
                case 0:
                    holder.tv_left.setVisibility(View.VISIBLE);
                    holder.iv_left_img.setVisibility(View.GONE);
                    holder.iv_left_record.setVisibility(View.GONE);
                    holder.tv_left.setText(msgList.get(position).getContent());
                    break;
                //表示接受录音
                case 1:
                    holder.tv_left.setVisibility(View.GONE);
                    holder.iv_left_img.setVisibility(View.GONE);
                    holder.iv_left_record.setVisibility(View.VISIBLE);

                    holder.iv_left_record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPlay = new MediaPlayer();
                            //播放动画
                            AnimationDrawable aDrawable;
                            holder.iv_left_record.setBackgroundResource(R.drawable.receive_horn);
                            aDrawable=(AnimationDrawable)holder.iv_left_record.getBackground();
                            if (isShowOrNot==false){
                                aDrawable.start();
                                isShowOrNot=true;
                            }
                            else {
                                aDrawable.stop();
                                isShowOrNot=false;
                            }
                            try{
                                String filename = msgList.get(position).getContent().split("/")[4];
                                String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                                File file = new File(path);
                                mPlay.setDataSource(filename);
                                mPlay.prepare();
                                mPlay.start();
                            } catch (IOException e) {
                                Toast.makeText(context,"播放失败",Toast.LENGTH_LONG);
                            }
                        }
                    });
                    break;
                //表示接受图片
                case 2:
                    holder.tv_left.setVisibility(View.GONE);
                    holder.iv_left_img.setVisibility(View.VISIBLE);
                    holder.iv_left_record.setVisibility(View.GONE);


                    try {
                        String filename = msgList.get(position).getContent().split("/")[4];
                        String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        holder.iv_left_img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        //发送者
        else {
            holder.ll_left.setVisibility(View.GONE);
            holder.ll_right.setVisibility(View.VISIBLE);
            switch (message.getType()){
                case 0:
                    holder.tv_right.setVisibility(View.VISIBLE);
                    holder.iv_right_img.setVisibility(View.GONE);
                    holder.iv_right_record.setVisibility(View.GONE);
                    holder.tv_right.setText(msgList.get(position).getContent());
                    break;
                //表示发送录音
                case 1:
                    holder.tv_right.setVisibility(View.GONE);
                    holder.iv_right_img.setVisibility(View.GONE);
                    holder.iv_right_record.setVisibility(View.VISIBLE);

                    holder.iv_right_record.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPlay = new MediaPlayer();
                            //播放动画
                            AnimationDrawable aDrawable;
                            holder.iv_right_record.setBackgroundResource(R.drawable.anim);
                            aDrawable=(AnimationDrawable)holder.iv_right_record.getBackground();
                            aDrawable.start();

                            try{
                                String filename = msgList.get(position).getContent().split("/")[4];
                                String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                                mPlay.setDataSource(path);
                                mPlay.prepare();
                                mPlay.start();
                            } catch (IOException e) {
                                Toast.makeText(context,"播放失败",Toast.LENGTH_LONG);
                            }
                        }
                    });
                    break;
                //表示发送图片
                case 2:
                    holder.tv_right.setVisibility(View.GONE);
                    holder.iv_right_img.setVisibility(View.VISIBLE);
                    holder.iv_right_record.setVisibility(View.GONE);
                    try {
                        String filename = msgList.get(position).getContent().split("/")[4];
                        String path = Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+filename;
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        holder.iv_right_img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        return convertView;
    }
    class ViewHolder{
        LinearLayout ll_left;
        LinearLayout ll_right;
        TextView tv_left;
        TextView tv_right;
        ImageView iv_left_img;
        ImageView iv_left_record;
        ImageView iv_right_img;
        ImageView iv_right_record;
        ImageView iv_right;
        ImageView iv_left;
    }
}
