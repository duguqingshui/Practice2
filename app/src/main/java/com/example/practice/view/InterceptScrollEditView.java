package com.example.practice.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ScrollView;

@SuppressLint("AppCompatCustomView")
public class InterceptScrollEditView extends EditText {
    public InterceptScrollEditView(Context context) {
        super(context);
    }

    public InterceptScrollEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptScrollEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    float lastY = 0;
    int lastScrollY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                lastScrollY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((event.getY() - lastY > 0 && getScrollY() == 0) || (event.getY() - lastY < 0 && getScrollY() > 0 && getScrollY() + getHeight() >= computeVerticalScrollRange())) {
                } else {
                    ViewParent scrollView = getScrollView(getParent());
                    if(scrollView!=null){
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }

                lastY = event.getY();
                if (getScrollY() - lastScrollY >= 1) {
                    lastScrollY = getScrollY();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    private ViewParent getScrollView(ViewParent view) {
        if(view == null){
            return null;
        }
        if (view instanceof ScrollView) {
            return view;
        } else {
            return getScrollView(view.getParent());
        }
    }
}
