package com.example.practice.app.menu.game.luckypan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.practice.R;
import com.example.practice.app.menu.game.LuckyPanView;

/**
 * Created by AMOBBS on 2017/4/6.
 */

public class LuckpanActivity extends AppCompatActivity{
    private LuckyPanView mLuckyPanView;
    private ImageView mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckpan);

        mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
        mStartBtn = (ImageView) findViewById(R.id.id_start_btn);

        mStartBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mLuckyPanView.isStart())
                {
                    mStartBtn.setImageResource(R.drawable.stop);
                    mLuckyPanView.luckyStart((int)(Math.random() * 6));
                } else
                {
                    if (!mLuckyPanView.isShouldEnd())

                    {
                        mStartBtn.setImageResource(R.drawable.start);
                        mLuckyPanView.luckyEnd();
                    }
                }
            }
        });
    }
}
