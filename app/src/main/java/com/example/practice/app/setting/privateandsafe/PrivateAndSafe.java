package com.example.practice.app.setting.privateandsafe;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.practice.R;
import com.example.practice.app.setting.privateandsafe.gesturelock.CreateGestureLockActivity;
import com.example.practice.view.MCIntent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AMOBBS on 2017/2/28.
 */

public class PrivateAndSafe extends AppCompatActivity {
    @BindView(R.id.gesture_lock)
    TextView gestureLock;
    @BindView(R.id.change_pass)
    TextView changePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privateandsafe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        setTitle(R.string.privateandsafe);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
    }
    @OnClick(R.id.gesture_lock)
    public void OnLockClick(){
        MCIntent.sendIntentFromAnimLeft(this, CreateGestureLockActivity.class);
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
