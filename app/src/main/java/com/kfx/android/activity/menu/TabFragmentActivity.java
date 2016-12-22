package com.kfx.android.activity.menu;

import com.kfx.android.R;
import com.kfx.android.R.string;
import com.kfx.android.activity.home.SetupForHomeActivity;
import com.kfx.android.bean.DialogMsg;
import com.kfx.android.util.PopDialogBox;
import com.kfx.android.util.app.MyApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TabFragmentActivity extends FragmentActivity {
	private FragmentManager manager;
	private FragmentTransaction tran;
	private TextView radioGroupTitle;
	private RadioGroup radioGroup;
	private RadioButton radioButton0;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private ImageView aboutUsImageView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_tab_fragment);
		MyApplication.getInstance().addActivity(TabFragmentActivity.this);
		manager = getSupportFragmentManager();
		tran = manager.beginTransaction();
		tran.replace(R.id.content, new HomeMainFragment());
		tran.commit();
		initEnd();
		setListener();
	}

	private void setListener() {
		//点击关于我们的按钮
		aboutUsImageView.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(TabFragmentActivity.this,SetupForHomeActivity.class);
				startActivity(intent);
				overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right); 
//				getActivity().finish();
			}
		});
	}

	public void initEnd() {
		radioButton0 = (RadioButton) findViewById(R.id.tab0);
		radioButton1 = (RadioButton) findViewById(R.id.tab1);
		radioButton2 = (RadioButton) findViewById(R.id.tab2);
		radioButton3 = (RadioButton) findViewById(R.id.tab3);
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroupTitle = (TextView) findViewById(R.id.radioGroupTitle);
		aboutUsImageView =  (ImageView) findViewById(R.id.aboutUsImageView);
		radioGroup.setOnCheckedChangeListener(listener);
		
	}
	
	
	OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			tran = manager.beginTransaction();
			switch (checkedId) {
			case R.id.tab0:
				tran.replace(R.id.content, new HomeMainFragment());
				radioButton0.setTextColor(Color.parseColor("#0083f3"));
				radioButton1.setTextColor(Color.parseColor("#808080"));
				radioButton2.setTextColor(Color.parseColor("#808080"));
				radioButton3.setTextColor(Color.parseColor("#808080"));
				break;
			case R.id.tab1:
				tran.replace(R.id.content, new NewsMainFragment());
				radioButton0.setTextColor(Color.parseColor("#808080"));
				radioButton1.setTextColor(Color.parseColor("#0083f3"));
				radioButton2.setTextColor(Color.parseColor("#808080"));
				radioButton3.setTextColor(Color.parseColor("#808080"));
				radioGroupTitle.setText(R.string.main_news);
				break;
			case R.id.tab2:
				tran.replace(R.id.content, new LawsMainFragment());
				radioButton0.setTextColor(Color.parseColor("#808080"));
				radioButton1.setTextColor(Color.parseColor("#808080"));
				radioButton2.setTextColor(Color.parseColor("#0083f3"));
				radioButton3.setTextColor(Color.parseColor("#808080"));
				radioGroupTitle.setText(R.string.main_laws);
				break;
			case R.id.tab3:
				tran.replace(R.id.content, new ServersMainFragment());
				radioButton0.setTextColor(Color.parseColor("#808080"));
				radioButton1.setTextColor(Color.parseColor("#808080"));
				radioButton2.setTextColor(Color.parseColor("#808080"));
				radioButton3.setTextColor(Color.parseColor("#0083f3"));
				radioGroupTitle.setText(R.string.main_server);
				break;
			}
			tran.commit();
		}
	};
	// private ArrayList<MyOnTouchListener> onTouchListeners = new
	// ArrayList<MyOnTouchListener>(
	// 10);
	MyOnTouchListener myOnTouchListener;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// for (MyOnTouchListener listener : onTouchListeners) {
		// listener.onTouch(ev);
		// }
		myOnTouchListener.onTouch(ev);
		return super.dispatchTouchEvent(ev);
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		// onTouchListeners.add(myOnTouchListener);
		this.myOnTouchListener = myOnTouchListener;
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		// onTouchListeners.remove(myOnTouchListener);
		this.myOnTouchListener = null;
	}

	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return true;
		}
		return true;
	}

	protected void dialog() {
		DialogMsg dialog = new DialogMsg();
		dialog.setQuestion("                  确定要退出吗?");
		dialog.setTitle("提示");
		dialog.setYesMsg("确定");
		dialog.setNoMsg("取消");
		PopDialogBox.getPopDialogBox(dialog, TabFragmentActivity.this,
				"mainLoginOut");
	}
}