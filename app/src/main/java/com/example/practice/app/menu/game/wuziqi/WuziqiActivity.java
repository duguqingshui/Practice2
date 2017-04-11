package com.example.practice.app.menu.game.wuziqi;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.example.practice.R;

/**
 * Created by AMOBBS on 2017/4/11.
 */

public class WuziqiActivity extends Activity {
    private WuziqiPanel mGamePanel;
    private AlertDialog.Builder alertBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wuziqi);

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
}
