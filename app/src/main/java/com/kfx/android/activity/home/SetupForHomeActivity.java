package com.kfx.android.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import com.kfx.android.R;
import com.kfx.android.activity.home.setup.AboutUsActivity;
import com.kfx.android.activity.home.setup.PersonalInfoActivity;
import com.kfx.android.activity.home.setup.SetupActivity;
import com.kfx.android.activity.home.setup.SupportActivity;
import com.kfx.android.activity.service.seal.val.SealUserLoginActivity;
import com.kfx.android.util.app.MyApplication;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SetupForHomeActivity extends Activity implements OnGestureListener {
	private ListMapFileCache fileCacheForFile;
	private Button aboutUsButton, setupButton, supportButton;
	private TextView loginButton,personal_info_Button;
	private GestureDetector mGestureDetector;
	private static float downX;
	private static float downY;
	private static float upX;
	private static float upY;
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceStates) {
		super.onCreate(savedInstanceStates);
		setContentView(R.layout.setup_main_home);
		MyApplication.getInstance().addActivity(SetupForHomeActivity.this);
		/** -----------------关于我们 start-------------------- */
		aboutUsButton = (Button) findViewById(R.id.aboutUsButton);
		aboutUsButton.setOnClickListener(aboutUsListener);
		/** -----------------关于我们 end-------------------- */
		/**------------------设置 start-----------------------*/
		setupButton = (Button) findViewById(R.id.setupButton);
		setupButton.setOnClickListener(setupListener);
		/**------------------设置 end-----------------------*/
		/**------------------登录 start-----------------------*/
		loginButton = (TextView) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginListener);
		/**------------------登录 end-----------------------*/
		/**------------------注销 start-----------------------*/
		personal_info_Button = (TextView) findViewById(R.id.personal_info_Button);
		personal_info_Button.setOnClickListener(personal_info_Listener);
		/**------------------注销 end-----------------------*/
		/**------------------技术支持 start-----------------------*/
		supportButton = (Button) findViewById(R.id.supportButton);
		supportButton.setOnClickListener(supportListener);
		/**------------------技术支持end-----------------------*/		
		mGestureDetector = new GestureDetector(this);
		/**------------------判断是否登录的状态---------------------*/
		ListMapService service1 = new ListMapServiceImpl();
		fileCacheForFile = new ListMapFileCache();
		String loginMsg = service1.getStrForFile(fileCacheForFile,
				"sealuser_loginmsg");
		if (loginMsg != null && loginMsg.trim().length() > 0) {
			// 隐藏登录 显示注销按钮
			loginButton.setVisibility(View.GONE);
			personal_info_Button.setVisibility(View.VISIBLE);
			try {
				JSONObject jo = new JSONObject(loginMsg);
				personal_info_Button.setText(jo.getString("name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 隐藏注销 显示登录按钮
			loginButton.setVisibility(View.VISIBLE);
			personal_info_Button.setVisibility(View.GONE);
		}
		
	}
	OnClickListener supportListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupForHomeActivity.this,
					SupportActivity.class);
			startActivity(intent);
			SetupForHomeActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
		}
	};
	OnClickListener setupListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupForHomeActivity.this, SetupActivity.class);
			startActivity(intent);
			SetupForHomeActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
		}
	};
	OnClickListener aboutUsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupForHomeActivity.this, AboutUsActivity.class);
			startActivity(intent);
			SetupForHomeActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
		}
	};
	OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupForHomeActivity.this,
					SealUserLoginActivity.class);
			intent.putExtra("sealUserToLoginLayoutFlg", "sealAboutLayout");
			startActivity(intent);
			SetupForHomeActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			SetupForHomeActivity.this.finish();
		}
	};
	OnClickListener personal_info_Listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupForHomeActivity.this, PersonalInfoActivity.class);
			startActivity(intent);
			SetupForHomeActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			SetupForHomeActivity.this.finish();
		}
	};

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downX = ev.getX(); // 取得按下时的坐标x
			downY = ev.getY();
			return true;
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			upX = ev.getX();
			upY = ev.getY();
			float distanceX = Math.abs(downX - upX);
			float distanceY = Math.abs(downY - upY);
			System.out.println("哈哈 左右滑动");
			// 获取当前host的page值
			if (downX - upX > 100 || downX - upX < -100 || downY - upY > 100
					|| downY - upY < -100) {
				if (distanceX > distanceY) {
					if (downX - upX > 100) {
						MyApplication.getInstance().removeActivity(
								SetupForHomeActivity.this);
						SetupForHomeActivity.this.finish();
						overridePendingTransition(R.animator.in_from_right,
								R.animator.out_to_left);
						System.out.println("哈哈 向左滑动了");
					} else if (downX - upX < -100) {

						System.out.println("哈哈 向右滑动了");
						ev.setAction(MotionEvent.ACTION_CANCEL);
					}
				} else {
					System.out.println("哈哈 上下滑动");
					if (downY - upY < -100) {

						System.out.println("哈哈 向下滑动了");
						ev.setAction(MotionEvent.ACTION_CANCEL);
					} else if (downY - upY > 100) {

						System.out.println("哈哈 向上滑动了");
						ev.setAction(MotionEvent.ACTION_CANCEL);
					}
				}
			}
			return this.mGestureDetector.onTouchEvent(ev);
		}
		return false;
	}
	
	
}
