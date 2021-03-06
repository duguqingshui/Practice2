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
import com.example.practice.app.menu.album.AblumActivity;
import com.example.practice.app.menu.about.AboutActivity;
import com.example.practice.app.menu.game.GameActivity;
import com.example.practice.app.menu.wallet.WalletActivity;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.SelfDialog;
import com.makeramen.roundedimageview.RoundedImageView;


/**
 * Created by AMOBBS on 2017/2/22.
 */

public class MenuFragment extends Fragment implements View.OnClickListener{
    RoundedImageView ri_user_image;
    TextView tv_username;
    TextView Personalizedsignature;
    TextView my_album;
    TextView my_expression;
    TextView my_wallet;
    TextView aboutButton;
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
        int user_img=SpUtils.getInt(getActivity(),Constant.LOGIN_HEADIMAGE,1);
        String user_name=SpUtils.getString(getActivity(),Constant.LOGIN_NICKNAME,null);
        String usre_sign=SpUtils.getString(getActivity(),Constant.LOGIN_SIGN,null);
        ri_user_image=(RoundedImageView)view.findViewById(R.id.ri_user_image);
        tv_username=(TextView) view.findViewById(R.id.tv_username);
        Personalizedsignature=(TextView)view.findViewById(R.id.Personalizedsignature);
        my_album=(TextView)view.findViewById(R.id.my_album);
        aboutButton=(TextView)view.findViewById(R.id.aboutButton);
        my_game=(TextView)view.findViewById(R.id.my_game);
        my_wallet=(TextView)view.findViewById(R.id.my_wallet);
        Personalizedsignature.setText(usre_sign);
        ri_user_image.setImageResource(user_img);
        tv_username.setText(user_name);
        my_wallet.setOnClickListener(this);
        my_game.setOnClickListener(this);
         my_album.setOnClickListener(this);
         aboutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.my_album:
                MCIntent.sendIntentFromAnimLeft(getActivity(), AblumActivity.class);
                break;
            case R.id.my_wallet:
                MCIntent.sendIntentFromAnimLeft(getActivity(), WalletActivity.class);
                break;
            case R.id.my_game:
                MCIntent.sendIntentFromAnimLeft(getActivity(), GameActivity.class);
                break;
            case R.id.aboutButton:
                MCIntent.sendIntentFromAnimLeft(getActivity(), AboutActivity.class);
                break;
        }
    }
}
