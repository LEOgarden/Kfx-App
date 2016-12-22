package com.kfx.android.activity.home.setup.detail;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.kfx.android.R;
import com.kfx.android.activity.home.SetupForHomeActivity;
import com.kfx.android.util.app.UpdateUserApplication;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.UserService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.UserServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {
	 private ImageView password_change_back;
	 private RelativeLayout password_change_btn;
	 private EditText oldPassword,newPassword1,newPassword2;
	 protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.password_change);
	 UpdateUserApplication.getInstance().addActivity(ChangePasswordActivity.this);
	 password_change_btn=(RelativeLayout) findViewById(R.id.password_change_btn);
	 password_change_back = (ImageView)findViewById(R.id.password_change_back);
	 oldPassword=(EditText) findViewById(R.id.oldPassword);
	 newPassword1=(EditText) findViewById(R.id.newPassword1);
	 newPassword2=(EditText) findViewById(R.id.newPassword2);
	 password_change_back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				UpdateUserApplication.getInstance().removeActivity(ChangePasswordActivity.this);
				ChangePasswordActivity.this.finish();
				ChangePasswordActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
			}
		});
		password_change_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String oldPasswordStr = oldPassword.getText().toString().trim();
				String newPasswordStr1 = newPassword1.getText().toString()
						.trim();
				String newPasswordStr2 = newPassword2.getText().toString()
						.trim();
				try {
					String returnMsg = "";
					ListMapService service = new ListMapServiceImpl();
					ListMapFileCache fileCacheForFile = new ListMapFileCache();
					String loginMsg = service.getStrForFile(fileCacheForFile,
							"sealuser_loginmsg");
					JSONObject jo = new JSONObject(loginMsg);					
					int id = (Integer) jo.get("id");
					String password = jo.get("password").toString();
					String telp = jo.get("telp").toString();
					String name = jo.get("name").toString();
					Map<String, Object> mapTemp = new HashMap<String, Object>();
					mapTemp.put("id", id);
					mapTemp.put("name", name);
					mapTemp.put("telp", telp);
					
					UserService userService = new UserServiceImpl();
					if (oldPasswordStr != null && oldPasswordStr.length() >= 6
							&& oldPasswordStr.equals(password)) {
						if (newPasswordStr1.length() >= 6) {
							if (newPasswordStr1.equals(newPasswordStr2)) {
								if (!oldPasswordStr.equals(newPasswordStr1)) {
									mapTemp.put("password", newPasswordStr1);
									Boolean flg=userService.updateSealUser(mapTemp);
									if(flg){
										returnMsg = "修改密码成功，";
//												+ "\n你输入的新密码是："
//												+ oldPassword.getText().toString()
//												+ " \n你输入的旧密码是："
//												+ newPassword1.getText().toString()
//												+ "\n你输入的确认密码是："
//												+ newPassword2.getText().toString()
//												+ "\n 身份证：" + certificateid;
										service.saveStrForFile("false",
												fileCacheForFile, "sealuser_loginflg");
										service.saveStrForFile("", fileCacheForFile,
												"sealuser_loginmsg");
										Intent intent = new Intent();
										intent.setClass(ChangePasswordActivity.this,
												SetupForHomeActivity.class);
										startActivity(intent);
										UpdateUserApplication.getInstance().exit();
										ChangePasswordActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
									}else{
										returnMsg="修改失败！";
									}

								} else {
									returnMsg = "新密码与旧密码一致";
								}
							} else {
								returnMsg = "请确认密码一致";
							}
						} else {
							returnMsg = "请输入正确的新密码！";
						}
					} else {
						returnMsg = "请输入正确的旧密码！";
					}

					Toast.makeText(ChangePasswordActivity.this, returnMsg,
							Toast.LENGTH_LONG).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	  }
}
