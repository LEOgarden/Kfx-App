package com.kfx.android.activity.home.setup.detail;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.kfx.android.R;
import com.kfx.android.activity.service.seal.val.SealUserRegisterActivity;
import com.kfx.android.util.app.UpdateUserApplication;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.js.JSValUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.UserService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.UserServiceImpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePersonalInfoActivity extends Activity {
	private ImageView change_info_BackBtn;
	private TextView personal_cancel,personal_submit, personal_name, personal_telp,
			personal_certificateid;
	private RelativeLayout change_personalinfo_btn, submit_and_cancel,
	RelativeLayout001,RelativeLayout002, RelativeLayout0002,identify_code,RelativeLayout04;
	private EditText personal_telp_edit, personal_name_edit;
	public static int id=0;
	public static String password ="";
	public static String telp="";
	public static String name="";
	public static String certificateid="";

	protected void onCreate(Bundle savedInstanceStates) {
		super.onCreate(savedInstanceStates);
		setContentView(R.layout.personal_change_info);
		change_personalinfo_btn = (RelativeLayout) findViewById(R.id.change_personalinfo_btn);
		submit_and_cancel = (RelativeLayout) findViewById(R.id.submit_and_cancel);
		RelativeLayout002 = (RelativeLayout) findViewById(R.id.RelativeLayout002);
		RelativeLayout0002 = (RelativeLayout) findViewById(R.id.RelativeLayout0002);
		identify_code= (RelativeLayout) findViewById(R.id.identify_code);
		RelativeLayout04= (RelativeLayout) findViewById(R.id.RelativeLayout04);
		RelativeLayout001= (RelativeLayout) findViewById(R.id.RelativeLayout001);
		personal_cancel = (TextView) findViewById(R.id.personal_cancel);
		personal_submit= (TextView) findViewById(R.id.personal_submit);
		personal_name = (TextView) findViewById(R.id.personal_name);
		personal_telp = (TextView) findViewById(R.id.personal_telp);
		personal_certificateid = (TextView) findViewById(R.id.personal_certificateid);
		personal_telp_edit = (EditText) findViewById(R.id.personal_telp_edit);
		personal_name_edit = (EditText) findViewById(R.id.personal_name_edit);
		change_info_BackBtn = (ImageView) findViewById(R.id.change_info_BackBtn);

		ListMapService service = new ListMapServiceImpl();
		ListMapFileCache fileCacheForFile = new ListMapFileCache();
		String loginMsg = service.getStrForFile(fileCacheForFile,
				"sealuser_loginmsg");
		JSONObject jo = null;
		try {
			jo = new JSONObject(loginMsg);
			id = (Integer) jo.get("id");
			password = jo.get("password").toString();
			telp = jo.get("telp").toString();
			name = jo.get("name").toString();
			certificateid=jo.get("certificateid").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//查询个人信息
		personal_name.setText(name);
		personal_certificateid.setText(certificateid);
		personal_telp.setText(telp);

		personal_submit.setOnClickListener(submitListener);
		change_info_BackBtn.setOnClickListener(backListener);
		change_personalinfo_btn.setOnClickListener(editListener);
		personal_cancel.setOnClickListener(cancelListener);

	}
	OnClickListener submitListener=new OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			String reg_user = personal_name_edit.getText().toString().trim();
			String reg_telp = personal_telp_edit.getText().toString().trim();
			String where_user = "name='" + reg_user + "' and id!="+id;
			String where_telp = "telp='" + reg_telp + "' and id!="+id;
			String returnMsg="";
			boolean flg = false;
			UserService service = new UserServiceImpl();
			if (reg_user.length() > 0) {
				if (JSValUtil.valSealTelp(reg_telp)) {
					flg = service.valSealUser(where_user);
					if (!flg) {
						flg = service.valSealUser(where_telp);
						if (!flg) {
							//更新信息
							Map<String, Object> mapTemp = new HashMap<String, Object>();
							mapTemp.put("id", id);
							mapTemp.put("password", password);
							mapTemp.put("name", reg_user);
							mapTemp.put("telp", reg_telp);
							mapTemp.put("certificateid", certificateid);
							flg=service.updateSealUser(mapTemp);
							if(flg){
								personal_certificateid.setOnClickListener(null);
								submit_and_cancel.setVisibility(View.GONE);
								RelativeLayout0002.setVisibility(View.GONE);
								change_personalinfo_btn.setVisibility(View.VISIBLE);
								RelativeLayout002.setVisibility(View.VISIBLE);
								personal_name.setVisibility(View.VISIBLE);
								personal_telp.setVisibility(View.VISIBLE);
								personal_telp_edit.setVisibility(View.GONE);
								personal_name_edit.setVisibility(View.GONE);
								
								RelativeLayout04.setVisibility(View.INVISIBLE);
								identify_code.setVisibility(View.INVISIBLE);
								
								personal_name.setText(reg_user);
								personal_telp.setText(reg_telp);
								
								ListMapService listService = new ListMapServiceImpl();
								ListMapFileCache fileCacheForFile = new ListMapFileCache();
								String loginMsg = listService.getStrForFile(fileCacheForFile,
										"sealuser_loginmsg");
								JSONObject jo = null;
								try {
									jo = new JSONObject(loginMsg);
									jo.put("name", reg_user);
									jo.put("telp", reg_telp);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								listService.saveStrForFile(jo.toString(), fileCacheForFile, "sealuser_loginmsg");
								returnMsg = "修改个人信息成功！";
							}else{
								returnMsg = "修改个人信息失败，请确保网络畅通！";
							}							
						}else{
							returnMsg = "手机号已注册";
						}
					}else{
						returnMsg = "用户名已经存在";
					}
				}else{
					returnMsg = "请输入正确的手机号码";
				}				
			}else{
				returnMsg = "请输入合法的用户名";
			}
			Toast.makeText(ChangePersonalInfoActivity.this, returnMsg, Toast.LENGTH_LONG).show();
		}
	};
	OnClickListener backListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			ChangePersonalInfoActivity.this.finish();
			ChangePersonalInfoActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
		}
	};
	OnClickListener editListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			ListMapService listService = new ListMapServiceImpl();
			ListMapFileCache fileCacheForFile = new ListMapFileCache();
			String loginMsg = listService.getStrForFile(fileCacheForFile,
					"sealuser_loginmsg");
			JSONObject jo = null;
			try {
				jo = new JSONObject(loginMsg);
				telp = jo.get("telp").toString();
				name = jo.get("name").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			personal_name_edit.setText(name);
			personal_telp_edit.setText(telp);
			
			submit_and_cancel.setVisibility(View.VISIBLE);
			RelativeLayout0002.setVisibility(View.VISIBLE);
			change_personalinfo_btn.setVisibility(View.GONE);
			RelativeLayout002.setVisibility(View.GONE);
			personal_name.setVisibility(View.GONE);
			personal_telp.setVisibility(View.GONE);
			personal_telp_edit.setVisibility(View.VISIBLE);
			personal_name_edit.setVisibility(View.VISIBLE);
			identify_code.setVisibility(View.VISIBLE);
			RelativeLayout04.setVisibility(View.VISIBLE);
			
			personal_certificateid.setOnClickListener(certIdListener);
		}
	};
	OnClickListener cancelListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			personal_certificateid.setOnClickListener(null);
			submit_and_cancel.setVisibility(View.GONE);
			RelativeLayout0002.setVisibility(View.GONE);
			change_personalinfo_btn.setVisibility(View.VISIBLE);
			RelativeLayout002.setVisibility(View.VISIBLE);
			personal_name.setVisibility(View.VISIBLE);
			personal_telp.setVisibility(View.VISIBLE);
			personal_telp_edit.setVisibility(View.GONE);
			personal_name_edit.setVisibility(View.GONE);
			
			RelativeLayout04.setVisibility(View.INVISIBLE);
			identify_code.setVisibility(View.INVISIBLE);
		}
	};
	OnClickListener certIdListener=new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Toast.makeText(ChangePersonalInfoActivity.this,
					"身份证不可修改", Toast.LENGTH_SHORT).show();
		}
	};
}
