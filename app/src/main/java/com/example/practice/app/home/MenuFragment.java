package com.example.practice.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.LoginActivity;
import com.example.practice.app.menu.album.AblumActivity;
import com.example.practice.app.menu.exchangeskin.ExchangeSkinActicity;
import com.example.practice.app.menu.about.AboutActivity;
import com.example.practice.app.menu.game.luckypan.LuckpanActivity;
import com.example.practice.app.menu.game.wuziqi.WuziqiActivity;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.SelfDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/2/22.
 */

public class MenuFragment extends Fragment implements View.OnClickListener{
    RoundedImageView ri_user_image;
    TextView tv_username;
    TextView Personalizedsignature;
    TextView my_album;
    TextView exchange_skin;
    TextView my_expression;
    TextView aboutButton;
    TextView exitButton;
    TextView my_game;
    private SelfDialog selfDialog;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_menu,container,false);
        initUI();
        return view;
    }

    private void initUI() {
        ri_user_image=(RoundedImageView)view.findViewById(R.id.ri_user_image);
        tv_username=(TextView) view.findViewById(R.id.tv_username);
        Personalizedsignature=(TextView)view.findViewById(R.id.Personalizedsignature);
        my_album=(TextView)view.findViewById(R.id.my_album);
        exchange_skin=(TextView)view.findViewById(R.id.exchange_skin);
        aboutButton=(TextView)view.findViewById(R.id.aboutButton);
        exitButton=(TextView)view.findViewById(R.id.exitButton);
        my_game=(TextView)view.findViewById(R.id.my_game);
        my_game.setOnClickListener(this);
         my_album.setOnClickListener(this);
         exchange_skin.setOnClickListener(this);
         aboutButton.setOnClickListener(this);
         exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.my_album:
                MCIntent.sendIntentFromAnimLeft(getActivity(), AblumActivity.class);
                break;
            case R.id.exchange_skin:
                MCIntent.sendIntentFromAnimLeft(getActivity(), ExchangeSkinActicity.class);
                break;
            case R.id.my_game:
                MCIntent.sendIntentFromAnimLeft(getActivity(), LuckpanActivity.class);
                break;
            case R.id.aboutButton:
                MCIntent.sendIntentFromAnimLeft(getActivity(), AboutActivity.class);
                break;
            case R.id.exitButton:
                selfDialog=new SelfDialog(getContext());
                selfDialog.setTitle(R.string.common_reminder);
                selfDialog.setMessage("您确定退出登录?");
                selfDialog.show();
                selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        //清空当前账号
                        SpUtils.putString(getContext(),Constant.LOGIN_ACCOUNT, "");
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        selfDialog.dismiss();
                    }

                });
                selfDialog.setNoOnclickListener("取消", new SelfDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        selfDialog.dismiss();
                    }
                });
                break;
        }
    }
}
