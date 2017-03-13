package com.example.practice.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by AMOBBS on 2017/3/13.
 */

public class InputWatcher implements TextWatcher {
    private static final String TAG = "InputWatcher" ;
    private Button mBtnClear;
    private EditText mEtContainer ;

    /**
     *
     * @param btnClear 清空按钮 可以是button的子类
     * @param etContainer edittext
     */
    public InputWatcher(Button btnClear, EditText etContainer) {
        if (btnClear == null || etContainer == null) {
            throw new IllegalArgumentException("请确保btnClear和etContainer不为空");
        }
        this.mBtnClear = btnClear;
        this.mEtContainer = etContainer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            if (mBtnClear != null) {
                mBtnClear.setVisibility(View.VISIBLE);
                mBtnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mEtContainer != null) {
                            mEtContainer.getText().clear();
                        }
                    }
                });
            }
        } else {
            if (mBtnClear != null) {
                mBtnClear.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
