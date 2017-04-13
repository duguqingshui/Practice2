package com.example.practice.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practice.R;
import com.example.practice.utils.CheckUtil;


public class MyEditView extends LinearLayout implements OnClickListener,
		MyAlertDialog.OnMyAlertDialogClickListener, View.OnLongClickListener {

	private MyEditView myEditView;
	private TextView view_title_tv;
	private TextView view_unit_tv;
	private ImageView view_arrow_im;
	private InterceptScrollEditView view_conent_ed;
	private TextView view_conent_tv;
	private MyEditViewClikListener onclick;
	private MyEditViewClikListenerView onclickView;
	private int viewIDTag;
	private boolean righticon;
	private int titleBackground;
	private Context mContext;
	private boolean isNumber;
	private boolean isNumfloat;
	private boolean isSingleLine;
	private CharSequence mTitle;
	private int mMaxValue;
	private CharSequence mUnit;
	private MyEditViewFinishListener finishListener;
	private int finishViewId;
	private int conentLength;

	//输入表情前的光标位置
	private int cursorPos;
	//输入表情前EditText中的文本
	private String inputAfterText;
	//是否重置了EditText的内容
	private boolean resetText;

	//添加长按复制功能
	private TextView infoTextView;

	@Override
	public boolean onLongClick(View v) {
		final Dialog dialog = new Dialog(getContext());
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.my_ecg_dialog, null);
		dialog.setContentView(contentView);
		dialog.setTitle("长按可复制");
		infoTextView = (TextView) contentView.findViewById(R.id.infoTextView);
		infoTextView.setText(view_conent_tv.getText().toString());
		if (!TextUtils.isEmpty(view_conent_tv.getText())) {
			dialog.show();
		}
		return false;
	}

	private int index;
	private MyEditViewIndexClikListenerView onindexClikView;
	private MyEditViewInputViewListener inputListener;

	public interface MyEditViewInputViewListener {
		void onMyEditInputView(MyEditView myEditView, int index);

	}


	public void setMyEditViewInputView(MyEditViewInputViewListener inputListener, MyEditView myEditView, int index) {

		this.inputListener = inputListener;
		this.myEditView = myEditView;
		this.index = index;

	}

	public interface MyEditViewClikListener {
		void onMyEditClick(int id);

	}

	public interface MyEditViewClikListenerView {
		void onMyEditClickView(int id, MyEditView view);

	}

	public interface MyEditViewIndexClikListenerView {
		void onMyEditClickView(int id, MyEditView view, int index);

	}

	public interface MyEditViewFinishListener {
		void onEditFinish(int finishViewId);
	}

	public void setMyEditViewFinish(MyEditViewFinishListener onclick,
									int viewIDTag) {
		this.finishListener = onclick;
		this.finishViewId = viewIDTag;
	}

	public void setMyEditViewClik(MyEditViewClikListener onclick, int viewIDTag) {
		this.onclick = onclick;
		this.viewIDTag = viewIDTag;
	}

	public void setMyEditViewClikView(MyEditViewClikListenerView onclickView,
									  int viewIDTag, MyEditView myEditView) {
		this.onclickView = onclickView;
		this.viewIDTag = viewIDTag;
		this.myEditView = myEditView;
	}

	public void setMyEditViewClikView(MyEditViewIndexClikListenerView onclickView,
									  int viewIDTag, MyEditView myEditView, int index) {
		this.onindexClikView = onclickView;
		this.viewIDTag = viewIDTag;
		this.myEditView = myEditView;
		this.index = index;
	}

	public MyEditView(Context context) {
		this(context, null);

	}

	public MyEditView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public MyEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mContext = context;
		initView(context);
		initAttrs(context, attrs);
	}

	public void setText(String content) {
		if (righticon) {
			view_conent_tv.setText(content);
		} else {
			view_conent_ed.setText(content);
		}
	}

	public String getText() {
		if (view_conent_tv != null) {
			if (righticon) {
				return view_conent_tv.getText().toString().trim();
			} else {
				String text = view_conent_ed.getText().toString().trim();
				if (text.startsWith("{") || text.startsWith("[")) {
					text = "`" + text;
				}
				return text;
			}
		}
		return "";

	}

	public void clearEditContent() {
		view_conent_ed.setText("");
		view_conent_tv.setText("");
	}

	public EditText getEditText() {
		return view_conent_ed;
	}

	public void setEditTextError() {
		if (view_conent_ed != null) {
			view_conent_ed.setError(String.format("%s 输入有误，请检查", view_title_tv.getText().toString().trim()));
		}
	}

	public TextView getTextView() {
		return view_conent_tv;
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditView);
		mTitle = a.getText(R.styleable.MyEditView_title); // 标题内容
		float titlewidth = a.getDimension(R.styleable.MyEditView_titlewidth, 0); // 标题长度
		float titlesize = a.getDimension(R.styleable.MyEditView_titlesize, 0); // 标题字体大小
		CharSequence hint = a.getText(R.styleable.MyEditView_hinttext); // 提示字
		mUnit = a.getText(R.styleable.MyEditView_unittext); // 单位
		isNumber = a.getBoolean(R.styleable.MyEditView_number, false); // int
		isNumfloat = a.getBoolean(R.styleable.MyEditView_numfloat, false); // float
		isSingleLine = a.getBoolean(R.styleable.MyEditView_isSingleLine, false); // float
		mMaxValue = a.getInt(R.styleable.MyEditView_maxValue, 0); // 录入最大值
		righticon = a.getBoolean(R.styleable.MyEditView_righticon, false); // 选择
		conentLength = a.getInt(R.styleable.MyEditView_conentLength, 20); // 内容长度
		boolean editAble = a.getBoolean(R.styleable.MyEditView_editAble, true); // 是否可编辑
		titleBackground = a.getColor(R.styleable.MyEditView_titleBackground,
				getResources().getColor(R.color.main_background_gray)); // 是否可编辑
		view_title_tv.setText(mTitle);
		view_conent_ed.setHint(hint);
		view_title_tv.setBackgroundColor(titleBackground);

		if (!TextUtils.isEmpty(mUnit)) {
			view_unit_tv.setVisibility(View.VISIBLE);
			view_unit_tv.setText(mUnit);
		}

		if (titlewidth > 0) {
			view_title_tv.setWidth((int) titlewidth);
		}

		if (titlesize > 0) {
			view_title_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) titlesize);
		}

		if (mTitle.equals("")) {
			if (righticon) {
				view_conent_ed.setVisibility(View.GONE);
			}
			view_title_tv.setVisibility(View.GONE);

		}

		if (righticon) {
			view_arrow_im.setVisibility(View.VISIBLE);
			view_conent_tv.setVisibility(View.VISIBLE);
			view_conent_ed.setVisibility(View.GONE);
			view_conent_tv.setOnClickListener(this);
			view_conent_tv.setHint(hint);
			//添加长按复制功能
			view_conent_tv.setOnLongClickListener(this);
		}

		if (isNumber) {
			view_conent_ed.setInputType(InputType.TYPE_CLASS_NUMBER);
			view_conent_ed
					.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
							conentLength)});
		}

		if (isNumfloat) {
			view_conent_ed.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			view_conent_ed
					.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
							conentLength)});
		}

		if(isSingleLine){
			view_conent_ed.setSingleLine(true);
		}

		view_conent_ed
				.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
						conentLength)});
		view_conent_ed.setEnabled(editAble);

		view_conent_ed.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (!resetText) {
					cursorPos = view_conent_ed.getSelectionEnd();
					// 这里用s.toString()而不直接用s是因为如果用s，
					// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
					// inputAfterText也就改变了，那么表情过滤就失败了
					inputAfterText = charSequence.toString();
				}
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (!resetText) {
					CharSequence input = getText();
					if (CheckUtil.containsEmoji(input.toString())) {
						resetText = true;
						Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
						//是表情符号就将文本还原为输入表情符号之前的内容
						setText(inputAfterText);
						CharSequence text = getText();
						if (text instanceof Spannable) {
							Spannable spanText = (Spannable) text;
							Selection.setSelection(spanText, text.length());
						}
					}
				} else {
					resetText = false;
				}
				if (inputListener != null) {
					inputListener.onMyEditInputView(MyEditView.this, index);
				}

			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		if (view_conent_ed.getVisibility() == View.VISIBLE
				&& (isNumber || isNumfloat)) {
			view_conent_ed.addTextChangedListener(watcher);

			view_conent_ed
					.setOnFocusChangeListener(new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (!hasFocus) {
								String content = view_conent_ed.getText()
										.toString();
								if (content.endsWith(".")) {
									view_conent_ed.setText(content.substring(0,
											content.length() - 1));
								}
							}
						}
					});
		}

		// if(mTitle.length()>5){
		// view_title_tv.setTextSize(16);
		// }

		a.recycle();
	}


	public void setEditable(boolean edait) {
		view_conent_ed.setEnabled(edait);
		view_conent_tv.setEnabled(edait);
	}


	public void setMyEditViewSelector() {
		righticon = true;
		view_arrow_im.setVisibility(View.VISIBLE);
		view_conent_tv.setVisibility(View.VISIBLE);
		view_conent_ed.setVisibility(View.GONE);
		view_conent_tv.setOnClickListener(this);
	}

	private void initView(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.my_edit_view, this);

		view_title_tv = (TextView) view.findViewById(R.id.view_title_tv);
		view_conent_ed = (InterceptScrollEditView) view.findViewById(R.id.view_conent_ed);
		view_conent_tv = (TextView) view.findViewById(R.id.view_conent_tv);
		view_unit_tv = (TextView) view.findViewById(R.id.view_unit_tv);
		view_arrow_im = (ImageView) view.findViewById(R.id.view_arrow_im);
	}

	public void changeTitleWidth(int width) {
		view_title_tv.setWidth(width);
	}

	public void changeTitleHeight(int height) {
		view_title_tv.setHeight(height);
	}

	public void addTextChangedListener(TextWatcher textWatcher) {
		view_conent_tv.addTextChangedListener(textWatcher);
		view_conent_ed.addTextChangedListener(textWatcher);
	}


	public String getTitle() {

		return view_title_tv.getText().toString();

	}

	public void setError() {
		view_conent_tv.setError(String.format("%s 输入有误，请检查", view_title_tv.getText().toString().trim()));
		view_conent_ed.setError(String.format("%s 输入有误，请检查", view_title_tv.getText().toString().trim()));
		view_conent_tv.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					view_conent_tv.setError(null);
				}
			}
		});
	}

	public void clearError() {
		view_conent_tv.setError(null);
	}

	@Override
	public void onClick(View v) {
		if (onclick != null) {
			onclick.onMyEditClick(viewIDTag);
		} else if (onclickView != null) {
			onclickView.onMyEditClickView(viewIDTag, myEditView);

		} else if (onindexClikView != null) {
			onindexClikView.onMyEditClickView(viewIDTag, myEditView, index);
		}

	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (isNumber && s.length() > 0 && mMaxValue > 0) {
				if (Float.parseFloat(s.toString().trim()) > mMaxValue) {
					MyAlertDialog.showDialog(mContext, (String) mTitle,
							mMaxValue + "",
							mUnit != null ? (String) mUnit : "",
							MyEditView.this);
				}
			} else if (isNumfloat && s.length() > 0 && mMaxValue > 0) {
				if (s.toString().equals(".")) {
					MCToast.show(R.string.common_first_dot, mContext);
					MyEditView.this.clearEditContent();
				} else {
					if (Float.parseFloat(s.toString().trim()) > mMaxValue) {
						MyAlertDialog.showDialog(mContext, (String) mTitle,
								mMaxValue + "", mUnit != null ? (String) mUnit
										: "", MyEditView.this);
					}
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().equals(".")) {
				MyEditView.this.clearEditContent();
			}
			if (finishListener != null) {
				finishListener.onEditFinish(finishViewId);
			}
		}
	};

	@Override
	public void onAlertDialogAffirmed(String t) {
		view_conent_ed.setText("");
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}

	/**
	 * 判断控件内容为空
	 * @return
     */
	public boolean isEmpty() {
		CharSequence tvContent = view_conent_tv.getText();
		CharSequence edContent = view_conent_ed.getText();
		return TextUtils.isEmpty(tvContent) && TextUtils.isEmpty(edContent);
	}

}
