package com.example.practice.app.menu.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.practice.R;
import com.example.practice.app.menu.game.game_2084.Game_2048Activity;
import com.example.practice.app.menu.game.luckypan.LuckpanActivity;
import com.example.practice.app.menu.game.wuziqi.WuziqiActivity;
import com.example.practice.view.MCIntent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2017/4/20.
 */

public class GameActivity extends AppCompatActivity {
    @BindView(R.id.luckpan)
    ImageView luckpan;
    @BindView(R.id.wuziqi)
    ImageView wuziqi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.geme);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.luckpan)
    public void OnLuckPanClick(){
        MCIntent.sendIntent(this, LuckpanActivity.class);
    }
    @OnClick(R.id.wuziqi)
    public void OnWuziqiClick(){
        MCIntent.sendIntent(this, WuziqiActivity.class);
    }
    @OnClick(R.id.game2048)
    public void  OnGame2048Click(){
        MCIntent.sendIntent(this, Game_2048Activity.class);
    }
    /**
     * 点击返回按钮，返回上一个页面
     * @param item
     * @return
     */
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
