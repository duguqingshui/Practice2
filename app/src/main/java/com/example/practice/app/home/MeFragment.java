package com.example.practice.app.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.setting.accountManage.AccountManageActivity;
import com.example.practice.app.setting.privateandsafe.PrivateAndSafe;
import com.example.practice.app.setting.remind.RemindActivity;
import com.example.practice.app.setting.user.UserEditActivity;
import com.example.practice.utils.Constant;
import com.example.practice.utils.SpUtils;
import com.example.practice.view.MCIntent;
import com.example.practice.view.MeItemView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AMOBBS on 2017/2/7.
 */

public class MeFragment extends Fragment {
    private View view;
    TextView title;
    private RoundedImageView user_img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(getActivity());
        title=(TextView)view.findViewById(R.id.tv_title);
        title.setText(R.string.setting);
        user_img=(RoundedImageView)view.findViewById(R.id.user_image);
        int img= SpUtils.getInt(getActivity(), Constant.LOGIN_HEADIMAGE,0);
        user_img.setImageResource(img);
        //1.账户管理
        initAccount_manage();
        //2 个人信息
        initPersonal_info();
        //3. 消息提醒
        initRemind();
        //4.我的隐私
        initPrivate();
        return view;
    }
    private void initAccount_manage() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_account_manage);
        miv_personal_info.setImage(R.drawable.usermanage);
        miv_personal_info.setTitle("账户管理");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), AccountManageActivity.class);
            }
        });
    }
    private void initPersonal_info() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_personal_info);
        miv_personal_info.setImage(R.drawable.editinfo);
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
        miv_personal_info.setImage(R.drawable.remind);
        miv_personal_info.setTitle("消息提醒");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), RemindActivity.class);
            }
        });
    }
    private void initPrivate() {
        MeItemView miv_personal_info = (MeItemView) view.findViewById(R.id.miv_private);
        miv_personal_info.setImage(R.drawable.safe);
        miv_personal_info.setTitle("隐私和安全");
        miv_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCIntent.sendIntentFromAnimLeft(getActivity(), PrivateAndSafe.class);
            }
        });
    }
}
