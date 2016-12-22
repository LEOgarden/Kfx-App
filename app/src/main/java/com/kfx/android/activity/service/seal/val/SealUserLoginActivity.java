package com.kfx.android.activity.service.seal.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.kfx.android.R;
import com.kfx.android.activity.home.SetupForHomeActivity;
import com.kfx.android.activity.service.SealServiceMainActivity;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.js.IDCard;
import com.kfx.android.util.js.JSValUtil;
import com.kfx.android.util.json.JsonUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.UserService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.UserServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SealUserLoginActivity extends Activity {
	private Button sealUserLoginBtn,sealUserRegisterBtn,forget_button;
	private ImageView sealLoginBackBtn;
	private EditText edUser;
	private EditText edPassword;
    private ListMapFileCache fileCacheForFile; 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sealuser_login);
		sealUserLoginBtn=(Button) findViewById(R.id.sealuser_id);
		sealUserRegisterBtn=(Button) findViewById(R.id.regester);
		
		forget_button=(Button) findViewById(R.id.forget_button);
		edUser=(EditText) findViewById(R.id.edt_seal_user);
		edPassword=(EditText) findViewById(R.id.edt_seal_pass);		
		sealUserLoginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String user=edUser.getText().toString().trim();
				String pass=edPassword.getText().toString().trim();
				String where="";
				if(JSValUtil.valSealTelp(user)){
					where="telp='"+user;
				}else if(user.length()==18&&IDCard.IDCardValidate(user)){
					where="certificateid='"+user;					
				}else{
					where="name='"+user;
				}				
				where=where+"' and password='"+pass+"'";				
				UserService service=new UserServiceImpl();
				ListMapService service1=new ListMapServiceImpl();
				boolean flg=false;
				String returnStr ="";
				flg=service.valSealUser(where);
				if(pass.length()>=4&&user.length()>2){
					if(flg){
						List<Map<String,Object>> userList=new ArrayList<Map<String,Object>>();
						userList=service.searchSealUser(where);
						Map<String,Object> userObject=userList.get(0);
						
						JSONObject jo=JsonUtil.getMapToJsonObject(userObject);
						
						fileCacheForFile=new ListMapFileCache();
						returnStr="恭喜你，登陆成功！";
						/**
						 * 隐藏显示按钮 隐藏登录按钮 显示注销按钮-----------------------------
						 */
						service1.saveStrForFile("true", fileCacheForFile, "sealuser_loginflg");
						service1.saveStrForFile(jo.toString(), fileCacheForFile, "sealuser_loginmsg");
						Intent intent=getIntent();				
						String strLayout=intent.getStringExtra("sealUserToLoginLayoutFlg");
						if(strLayout.endsWith("sealValLayout")){
							intent.setClass(SealUserLoginActivity.this, SealValSealActivity.class);
						}else if(strLayout.endsWith("sealAboutLayout")){
							intent.setClass(SealUserLoginActivity.this, SetupForHomeActivity.class);							
						}
						startActivity(intent);
						SealUserLoginActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
						SealUserLoginActivity.this.finish();
					}else{
				    	returnStr="登录失败,如果你为进行注册请先进行注册！";					
					}
				}else{
					returnStr="请输入正确格式的用户名";	
				}							
				Toast toast = Toast.makeText(SealUserLoginActivity.this,
						returnStr, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();	
			}
		});
		
		sealUserRegisterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(SealUserLoginActivity.this, SealUserRegisterActivity.class);
				startActivity(intent);
				edUser.setText("");
				edPassword.setText("");
			}
		});
		forget_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SealUserLoginActivity.this, "此功能尚未开发，请谅解", Toast.LENGTH_SHORT).show();
			}
		});
		sealLoginBackBtn=(ImageView) findViewById(R.id.sealLoginBackBtn);
		sealLoginBackBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=getIntent();
				String strLayout=intent.getStringExtra("sealUserToLoginLayoutFlg");
				if(strLayout.endsWith("sealValLayout")){
					intent.setClass(SealUserLoginActivity.this, SealServiceMainActivity.class);
				}else if(strLayout.endsWith("sealAboutLayout")){
					intent.setClass(SealUserLoginActivity.this, SetupForHomeActivity.class);
					overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
				}
				startActivity(intent);
				SealUserLoginActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
				SealUserLoginActivity.this.finish();
			}
		});
		
	}
}