package com.example.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.practice.R;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by AMOBBS on 2017/2/9.
 */

public class MeItemView extends LinearLayout {
    private TextView item_name;
    private RoundedImageView item_image;

    public MeItemView(Context context) {
        this(context,null);
    }

    public MeItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.me_item_view, this);

        item_name=(TextView)findViewById(R.id.item_name);
        item_image=(RoundedImageView)findViewById(R.id.item_image);
        //获取自定义以及原声属性的操作
    }
    /**
     * @param name	设置标题内容
     */
    public void setTitle(String name){
        item_name.setText(name);
    }

    /**
     * @param image	设置描述内容
     */
    public void setImage(int image){
        item_image.setImageResource(image);
    }
}
