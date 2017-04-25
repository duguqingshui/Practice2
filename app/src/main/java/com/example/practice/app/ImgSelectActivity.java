package com.example.practice.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.adapter.ImageAdapter;
import com.example.practice.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AMOBBS on 2017/4/25.
 */

public class ImgSelectActivity extends AppCompatActivity {
    @BindView(R.id.gridview_img)
    GridView gridview;
    public Integer[] mThumbIds={//显示的图片数组
            R.mipmap.ig1, R.mipmap.camera, R.mipmap.folder, R.mipmap.ic_launcher, R.mipmap.music, R.mipmap.picture, R.mipmap.video,
            R.mipmap.i3, R.mipmap.i4,R.mipmap.i5,R.mipmap.i6,R.mipmap.i7, R.mipmap.i8
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_select);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.img_select);
        ButterKnife.bind(this);
        gridview.setAdapter(new ImageAdapter(this));//调用ImageAdapter.java
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){//监听事件
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(), ""+position,Toast.LENGTH_SHORT).show();//显示信息;
                SpUtils.putInt(getApplicationContext(),"USER_IMG",mThumbIds[position]);
                System.out.println("发送者头像R"+mThumbIds[position]);
                gridview.setVisibility(View.GONE);
                finish();
            }
        });
        gridview.setAdapter(new ImageAdapter(this));//调用ImageAdapter.java
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
