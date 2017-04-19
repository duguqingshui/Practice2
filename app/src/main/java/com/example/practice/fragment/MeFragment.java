package com.example.practice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice.R;
import com.example.practice.app.setting.nodistrub.NoDistrubActivity;
import com.example.practice.app.setting.normal.NormalActivity;
import com.example.practice.app.setting.privateandsafe.PrivateAndSafe;
import com.example.practice.app.setting.privateandsafe.gesturelock.CreateGestureLockActivity;
import com.example.practice.app.setting.privateandsafe.gesturelock.GuestureLockActivity;
import com.example.practice.app.setting.remind.RemindActivity;
import com.example.practice.app.setting.statistics.StatisticalFlowActivity;
import com.example.practice.app.setting.user.UserEditActivity;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MeItemView;


/**
 * Created by AMOBBS on 2017/2/7.
 */

public class MeFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        //1 个人信息
        initPersonal_info();
        //2. 消息提醒
        initRemind();
        //3.聊天设置
        initChatSetting();
        //4.勿扰模式
        initNoDistrub();
        //5.我的隐私
        initPrivate();
        //6.通用设置
        initPublic();
        //7.流量统计
        initTrafficStatistics();
        return view;
    }

    private void initPersonal_info() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_personal_info);
        miv_personal_info.setImage(R.mipmap.icon);
        miv_personal_info.setTitle("编辑资料");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), UserEditActivity.class);
            }
        });
    }
    private void initRemind() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_remind);
        miv_personal_info.setImage(R.mipmap.pictures);
        miv_personal_info.setTitle("消息提醒");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), RemindActivity.class);
            }
        });
    }
    private void initChatSetting() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_chatSettting);
        miv_personal_info.setImage(R.mipmap.collect);
        miv_personal_info.setTitle("聊天设置");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), RemindActivity.class);
            }
        });
    }
    private void initNoDistrub() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_noDistrub);
        miv_personal_info.setImage(R.mipmap.wallet);
        miv_personal_info.setTitle("勿扰模式");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), NoDistrubActivity.class);
            }
        });
    }
    private void initPrivate() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_private);
        miv_personal_info.setImage(R.mipmap.card);
        miv_personal_info.setTitle("隐私和安全");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), PrivateAndSafe.class);
            }
        });
    }
    private void initPublic() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_public);
        miv_personal_info.setImage(R.mipmap.look);
        miv_personal_info.setTitle("通用设置");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), NormalActivity.class);
            }
        });
    }
    private void initTrafficStatistics() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_trafficStatistics);
        miv_personal_info.setImage(R.mipmap.set);
        miv_personal_info.setTitle("流量统计");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), StatisticalFlowActivity.class);
            }
        });
    }
}
