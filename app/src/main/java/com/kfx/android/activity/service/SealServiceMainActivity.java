package com.kfx.android.activity.service;

import com.kfx.android.R;
import com.kfx.android.activity.service.seal.record.SealRecordMainActivity;
import com.kfx.android.activity.service.seal.val.SealUserLoginActivity;
import com.kfx.android.activity.service.seal.val.SealValSealActivity;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.gps.GPSUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SealServiceMainActivity extends Activity {
	private ImageView iv;
	private RelativeLayout recrodBtn;
	private RelativeLayout valBtn; 
    private Location mLocation;
    private ListMapFileCache fileCacheForFile; 	
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_seal_main);
		//存取GPS坐标位置
		ListMapService service=new ListMapServiceImpl();		
		fileCacheForFile=new ListMapFileCache();		
		service.saveStrForFile("0,0", fileCacheForFile, "seal_record_location");
		//返回按钮设置
		iv=(ImageView) findViewById(R.id.sealServiceMainBackBtn);
		iv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SealServiceMainActivity.this.finish();
			}
		});
		//点击通讯录按钮
		recrodBtn=(RelativeLayout) findViewById(R.id.sealRecordBtn);
		recrodBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				String msg="";
				ListMapService service=new ListMapServiceImpl();
				fileCacheForFile=new ListMapFileCache();
				if (GPSUtil.gpsIsOpen(SealServiceMainActivity.this)) {
					mLocation = GPSUtil.getLocation(SealServiceMainActivity.this);
					if (mLocation != null) {
						service.saveStrForFile(
								mLocation.getLongitude() + ","
										+ mLocation.getLatitude(),
								fileCacheForFile, "seal_record_location");
						msg = "维度:" + mLocation.getLatitude() + "\n经度:"
								+ mLocation.getLongitude();
					} else {
						msg = "GPS开启获取不到数据";
						service.saveStrForFile("0,0",
								fileCacheForFile, "seal_record_location");
					}
				} else {
					msg = "GPS未开启获取不到数据";
					service.saveStrForFile("0,0", fileCacheForFile,
							"seal_record_location");
				}				
				Toast toast = Toast.makeText(SealServiceMainActivity.this,
						msg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				intent.setClass(SealServiceMainActivity.this, SealRecordMainActivity.class);
				startActivity(intent);				
			}
		});
		//点击印章验证按钮
		valBtn = (RelativeLayout) findViewById(R.id.sealValBtn);
		valBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListMapService service1 = new ListMapServiceImpl();
				fileCacheForFile = new ListMapFileCache();
				
				String loginMsg = service1.getStrForFile(fileCacheForFile,
						"sealuser_loginmsg");
				if (loginMsg != null && loginMsg.trim().length() > 0) {
					// 隐藏登录 显示注销按钮
					Intent intent = new Intent();
					intent.setClass(SealServiceMainActivity.this,
							SealValSealActivity.class);					
					startActivity(intent);					
				} else {
					Intent intent = new Intent();
					intent.setClass(SealServiceMainActivity.this,
							SealUserLoginActivity.class);
					intent.putExtra("sealUserToLoginLayoutFlg", "sealValLayout");
					startActivity(intent);					
				}
				
				//finish();
				//SealServiceMainActivity.this.finish();
			}
		});
		
	}
}