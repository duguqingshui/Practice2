package com.example.practice.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.practice.R;


public class MyToggleButton extends View implements OnClickListener {
	/*
	 * 按钮滑动的最大距离 backgroundBitmap.width-slideBtn.width
	 */
	private Bitmap backgroundBitmap;// 背景图
	private Bitmap slideBtn;// 滑动按钮
	private Paint paint;
	private float slideBtn_left;// 滑动按钮左边界

	/**
	 *
	 * 在代码里创建对象时 使用此构造方法
	 */
	// public MyToggleButton(Context context) {
	// super(context);
	// }

	/**
	 * 在代码里面创建对象的时候，使用此构造方法
	 */
	public MyToggleButton(Context context) {
		super(context);
	}

	/**
	 * 在布局中声明view 创建是系统自动调用
	 *
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	/*
	 * view 对象显示的屏幕上，有几个重要步骤 1、构造方法创建对象 initView() 2 测量view大小 onMeasure(int,int)
	 * 3确定view的位置 view有一些建议权 决定权在父view手中 onlayout() 4、绘制view内容 onDraw(Canvas)
	 */

	/**
	 * 初始化
	 */
	private void initView() {

		// 初始化图片
		backgroundBitmap = BitmapFactory.decodeResource(getResources(),
				R.mipmap.switch_background);
		slideBtn = BitmapFactory.decodeResource(getResources(),
				R.mipmap.slide_button);
		// 初始化画笔
		paint = new Paint();
		paint.setAntiAlias(true);// 打开抗锯齿

		// 添加监听事件
		setOnClickListener(this);
	}

	/**
	 * 测量尺寸的回调方法 width ：view的宽度 单位 像素 height :高度
	 */
	@Override
	protected void onMeasure(int widthonMeasureSpec, int heighrMeasureSpec) {

		setMeasuredDimension(backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight());
	}

	/**
	 * 当前开关的状态
	 *
	 * @param canvas
	 */
	private boolean currState = false;

	protected void onDraw(Canvas canvas) {

		// 绘制背景

		/**
		 * backgroundBitmap要绘制的图片 left 图片的左边界 top 图片的上边界 paint 绘图片要使用的画笔
		 */
		canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
		// 绘制可滑动的按钮
		canvas.drawBitmap(slideBtn, slideBtn_left, 0, paint);
	}

	/**
	 * 判断是否发生拖动 如果拖动了 就不再响应onclick事件
	 */

	private boolean isDrag = false;

	@Override
	/**
	 * onclick事件在View.onTouchEvent中解析
	 * 系统对onclick事件的解析 过于简陋  只要有down事件 up事件  系统即认为发生了click事件
	 */
	public void onClick(View v) {
		/*
		 * 如果没有拖动，才执行改变状态的动作
		 */
		if (!isDrag) {
			currState = !currState;
			flushState();
		}
	}

	/*
	 * down事件时的x值
	 */
	private int firstX;
	/*
	 * touch事件的上一个X值
	 */
	private int lastX;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		switch (event.getAction()) {
			// 按下时触发
			case MotionEvent.ACTION_DOWN:
				firstX = lastX = (int) event.getX();
				isDrag=false;
				break;
			// 移动时触发

			case MotionEvent.ACTION_MOVE:
				//判断是否发生拖动
				if(Math.abs(event.getX()-firstX)>5){
					isDrag = true;
				}
				// 手指在屏幕移动的距离
				int dis = (int) (event.getX() - lastX);

				// 将本次位置 设置给lastX
				lastX = (int) event.getX();

				// 根据手指移动的距离 改变slideBtn_left的值
				slideBtn_left = slideBtn_left + dis;
				break;
			case MotionEvent.ACTION_UP:
				// 触摸后触发
				if (isDrag) {

					int maxLeft = backgroundBitmap.getWidth() - slideBtn.getWidth(); // slideBtn
					// 左边届最大值
				/*
				 * 根据 slideBtn_left 判断，当前应是什么状态
				 */
					if (slideBtn_left > maxLeft / 2) { // 此时应为 打开的状态
						currState = true;
					} else {
						currState = false;
					}

					flushState();
				}

				break;


		}
		flushView();
		return true;
	}

	/**
	 * 刷新当前状态
	 */
	private void flushState() {
		if (currState) {
			slideBtn_left = backgroundBitmap.getWidth() - slideBtn.getWidth();
		} else {
			slideBtn_left = 0;
		}
		/*
		 * 刷新当前view 会导致onDraw方法的执行
		 */
		flushView();
	}

	/**
	 * 刷新当前视图
	 */
	private void flushView() {
		/*
		 * 对slideBtn_left 值进行判断 确保其在合理的位置
		 */
		int maxleft = backgroundBitmap.getWidth() - slideBtn.getWidth();// slideBtn左边界最大值
		// 是其值大于0
		slideBtn_left = (slideBtn_left > 0) ? slideBtn_left : 0;
		// 其值小于=maxlsft
		slideBtn_left = (slideBtn_left < maxleft)?slideBtn_left
				: maxleft;
		invalidate();
	}
}
