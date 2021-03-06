package com.example.practice.app.menu.game.wuziqi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.practice.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2017/4/11.
 */

public class WuziqiActivity extends AppCompatActivity {
    private WuziqiPanel mGamePanel;
    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;
    @BindView(R.id.rule_game)
    TextView game_rule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wuziqi);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        setTitle(R.string.Wuziqi);
        ButterKnife.bind(this);
        //游戏结束时弹出对话框
        alertBuilder = new AlertDialog.Builder(WuziqiActivity.this);
        alertBuilder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGamePanel.restartGame();
            }
        });
        alertBuilder.setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WuziqiActivity.this.finish();
            }
        });
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("此局结束");

        mGamePanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);
        mGamePanel.setOnGameStatusChangeListener(new OnGameStatusChangeListener() {
            @Override
            public void onGameOver(int gameWinResult) {
                switch (gameWinResult) {
                    case WuziqiPanel.WHITE_WIN:
                        alertBuilder.setMessage("白棋胜利!");
                        break;
                    case WuziqiPanel.BLACK_WIN:
                        alertBuilder.setMessage("黑棋胜利!");
                        break;
                    case WuziqiPanel.NO_WIN:
                        alertBuilder.setMessage("和棋!");
                        break;
                }
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });
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
    @OnClick(R.id.rule_game)
    public void OnRuleGameClick(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.rule_game)
                .setMessage(R.string .rule_game_content)
                .setNegativeButton(R.string.common_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }
}
