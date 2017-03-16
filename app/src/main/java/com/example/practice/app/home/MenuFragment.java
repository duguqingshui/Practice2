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
import com.example.practice.app.album.AblumActivity;
import com.example.practice.app.exchangeskin.ExchangeSkinActicity;
import com.example.practice.app.about.AboutActivity;
import com.example.practice.app.wallet.WalletActivity;
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
    TextView my_wallet;
    TextView exchange_skin;
    TextView my_expression;
    TextView aboutButton;
    TextView exitButton;
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
        my_wallet=(TextView)view.findViewById(R.id.my_wallet);
        exchange_skin=(TextView)view.findViewById(R.id.exchange_skin);
        my_expression=(TextView)view.findViewById(R.id.my_expression);
        aboutButton=(TextView)view.findViewById(R.id.aboutButton);
        exitButton=(TextView)view.findViewById(R.id.exitButton);

         my_album.setOnClickListener(this);
         my_wallet.setOnClickListener(this);
         exchange_skin.setOnClickListener(this);
         my_expression.setOnClickListener(this);
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
            case R.id.my_wallet:
                MCIntent.sendIntentFromAnimLeft(getActivity(), WalletActivity.class);
                break;
            case R.id.exchange_skin:
                MCIntent.sendIntentFromAnimLeft(getActivity(), ExchangeSkinActicity.class);
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
