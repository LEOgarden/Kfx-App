package com.kfx.android.activity.service.seal.val;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kfx.android.R;
import com.kfx.android.bean.User;
import com.kfx.android.util.js.IDCard;
import com.kfx.android.util.js.JSValUtil;
import com.kfx.android.util.service.UserService;
import com.kfx.android.util.service.impl.UserServiceImpl;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SealUserRegisterActivity extends Activity implements OnClickListener{
	 private ImageView backBtn;
//	 private TimeCount time;
	 private Button btnGetcode,btn_agreement;
	 //用户名输入框
	 private EditText seal_reg_user;
	 //身份证输入框
	 private EditText seal_reg_cert;
	 //密码输入框
	 private EditText seal_reg_pass;
	 //确认密码输入框
	 private EditText seal_reg_repass;
	 //手机号码输入框
	 private EditText seal_reg_telp;
	 //验证码输入框
	private EditText seal_reg_valcode;
	 //短信sdk主对象
	 
	 //注册按钮
	 private Button seal_reg_btn;
	 private String phoneNum;
	 int i = 60;
	 
	 
	 @SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		 public void handleMessage(Message msg) {
			 if (msg.what == -9) {
					btnGetcode.setText("重新发送(" + i + ")");
				} else if (msg.what == -8) {
					btnGetcode.setText("获取验证码");
					btnGetcode.setClickable(true);
					i = 60;
				} else{
					int event = msg.arg1;
					int result = msg.arg2;
					Object data = msg.obj;
					if(result == SMSSDK.RESULT_COMPLETE){
						//短信 注册成功后，返回登录界面，然后提示
						if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
							Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();							
						}else{
							return ;
						}
					}else{
						return ;
					}
				}
		 };
	 };
	 
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sealuser_register);
		
		initView();	
		
		backBtn = (ImageView)findViewById(R.id.sealRegisterBackBtn);
		backBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent();
//				intent.setClass(SealUserRegisterActivity.this, SealUserLoginActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		
		btn_agreement.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(SealUserRegisterActivity.this, SealUserAgreementActivity.class);
				startActivity(intent);
			}
		});
		
		
		/*time = new TimeCount(60000, 1000);

		btnGetcode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				time.start();
			}
		});*/
	}
	
	
	//初始化控件
	private void initView() {
		seal_reg_user=(EditText) findViewById(R.id.seal_reg_user);
		seal_reg_cert=(EditText) findViewById(R.id.seal_reg_cert);
		seal_reg_pass=(EditText) findViewById(R.id.seal_reg_pass);
		seal_reg_repass=(EditText) findViewById(R.id.seal_reg_repass);
		seal_reg_telp=(EditText) findViewById(R.id.seal_reg_telp);
		seal_reg_valcode=(EditText) findViewById(R.id.seal_reg_valcode);
		btnGetcode = (Button) findViewById(R.id.btn_getcode);
		seal_reg_btn=(Button) findViewById(R.id.seal_reg_btn);
		btn_agreement = (Button)findViewById(R.id.btn_agreement);
		btnGetcode.setOnClickListener(this);
		seal_reg_btn.setOnClickListener(this);
		
		
		//初始化smssdk
		SMSSDK.initSDK(this, "1924b37186e4e", "ed93fe9bc163cf5e86c069944fb9dd5b");
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}
	/*class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
*/
		/*@Override
		public void onTick(long millisUntilFinished) {
			btnGetcode.setClickable(false);
			btnGetcode.setText(millisUntilFinished / 1000 + "秒后可重新发送");
			btnGetcode.setTextColor(Color.parseColor("#808080"));
		}*/

		/*@Override
		public void onFinish() {
			btnGetcode.setText("重新获取验证码");
			btnGetcode.setClickable(true);
			btnGetcode.setTextColor(Color.parseColor("#0083f3"));
		}
	}*/
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_getcode:
			//获取手机输入框的text
			phoneNum = seal_reg_telp.getText().toString().trim();
			//判断手机号码
			if(!judgePhoneNum(phoneNum)){
				return;
			}
			btnGetcode.setClickable(false);
			Toast.makeText(SealUserRegisterActivity.this,"正在发送验证码到"+ seal_reg_telp.getText()+"用户", Toast.LENGTH_SHORT).show();
			//发送短信验证
			SMSSDK.getVerificationCode("86", phoneNum);
			//按钮不可点击，并显示倒计时（正在获取）
			//btnGetcode.setClickable(false);
			btnGetcode.setText("重新发送("+i+"秒)");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(; i>0 ; i--){
						handler.sendEmptyMessage(-9);
						if(i <= 0){
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(-8);
				}
			}).start();
			break;
			
		case R.id.seal_reg_btn:

			String reg_user = seal_reg_user.getText().toString().trim();
			String reg_cert = seal_reg_cert.getText().toString().trim();
			String reg_pass = seal_reg_pass.getText().toString().trim();
			String reg_repass = seal_reg_repass.getText().toString().trim();
			String reg_telp = seal_reg_telp.getText().toString().trim();
//			String reg_valcode = seal_reg_valcode.getText().toString()
//					.trim();
			String returnMsg = "";

			UserService service = new UserServiceImpl();
			boolean flg = false;
			String where_user = "name='" + reg_user + "'";
			String where_telp = "telp='" + reg_telp + "'";
			String where_cert = "certificateid='" + reg_cert + "'";
			if (reg_user.length() > 0) {
				if (reg_cert.length() == 18
						&& IDCard.IDCardValidate(reg_cert)) {
					if (reg_pass.length() >= 6) {
						if (reg_pass.equals(reg_repass)) {
							if (JSValUtil.valSealTelp(reg_telp)) {
								flg = service.valSealUser(where_user);
								if (!flg) {
									flg = service.valSealUser(where_cert);
									if (!flg) {
										flg = service
												.valSealUser(where_telp);
										if (!flg) {
											User user=new User();
											user.setName(reg_user);
											user.setCertificateid(reg_cert);
											user.setPassword(reg_pass);
											user.setTelp(reg_telp);
											flg=service.saveSealRegisterUser(user);
											if(flg){
												returnMsg = "恭喜你，注册成功，你的用户名："+reg_user+" 身份证号："+reg_cert+" 手机号："+reg_telp+" 密码"+reg_pass;
												//Intent intent=new Intent();
												//intent.setClass(SealUserRegisterActivity.this, SealUserLoginActivity.class);
												//startActivity(intent);
												SealUserRegisterActivity.this.finish();
											}else{
												returnMsg = "注册失败";
											}
										} else {
											returnMsg = "手机号已注册";
										}

									} else {
										returnMsg = "身份证号码已经存在";
									}

								} else {
									returnMsg = "用户名已经存在";
								}
							} else {
								returnMsg = "请输入正确的手机号码";
							}
						} else {
							returnMsg = "请确认密码一致";
						}
					} else {
						returnMsg = "密码大于6位";
					}
				} else {
					returnMsg = "请输入合法的身份证号码";
				}
			} else {
				returnMsg = "请输入合法的用户名";
			}
			Toast toast = Toast.makeText(SealUserRegisterActivity.this,
					returnMsg, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			break;					
		}
	}


	private boolean judgePhoneNum(String phoneNum) {
		if(isMatchLength(phoneNum,11) && isMobileNo(phoneNum)){
			return true;
		}
		Toast.makeText(this, "手机号码输入有误", Toast.LENGTH_SHORT).show();
		return false;
	}


	private boolean isMobileNo(String phoneNum) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][3578]\\d{9}";//"[1]第一位数字1,[3578]第二位数字3、5、7、8"
		if(TextUtils.isEmpty(phoneNum)){
			return false;
		}else
		return phoneNum.matches(telRegex);
	}


	private boolean isMatchLength(String phoneNum, int length) {
		if(phoneNum.isEmpty()){
			return false;
		}else{
		return phoneNum.length() == length ? true : false;
		}
	}
	
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}
	
	//匹配短信中的验证码
	private String patternCode(String patternContent){
		if(TextUtils.isEmpty(patternContent)){
			return null;
		}
		
		Pattern p = Pattern.compile(patternContent);
		Matcher matcher = p.matcher(patternContent);
		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}

}