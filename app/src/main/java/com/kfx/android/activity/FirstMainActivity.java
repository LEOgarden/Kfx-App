package com.kfx.android.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.kfx.android.R;
import com.kfx.android.activity.menu.TabFragmentActivity;
import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;
import com.kfx.android.util.net.NetLinkWork;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.version.AppVersionUtil;
import com.kfx.android.view.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class FirstMainActivity extends Activity {
	public ListMapFileCache listMapFileCache; 	
	public ImageFileCache imageFileCache;
    public static Map<String,Object> map2=new HashMap<String, Object>();    
    /*
     * @初始化登录界面
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.first_main);
		
		listMapFileCache=new ListMapFileCache();
    	imageFileCache=new ImageFileCache();
    	ListMapService service=new ListMapServiceImpl();
    	//清空缓存 GPS初始化定位 为空,更新版本提示
    	service.clearCacheForFile(listMapFileCache, imageFileCache);
    	service.saveStrForFile("0,0", listMapFileCache, "seal_record_location");
    	//service.saveStrForFile("true", listMapFileCache, "updateVersion_flg");
		/***
		 * 版本更新
		 */	
		//String updateApkFlg=service.getStrForFile(listMapFileCache, "updateVersion_flg");
		//boolean updateFlg = Boolean.parseBoolean(updateApkFlg);//获取检测 true检测 false 不做检测
		boolean netIsConnected=NetLinkWork.isLinkedNetwork(FirstMainActivity.this);
		//再次设置网络  	
		//判断网络连接是否成功
		if(!netIsConnected){
			popSetNetWorkBox();
		}else{
			if (NetLinkWork.isLinkedNetWeb() != null
					&& NetLinkWork.isLinkedNetWeb().equals("success")) {
				String netType = NetLinkWork
						.getNetWorkInfo(FirstMainActivity.this);
				Toast.makeText(FirstMainActivity.this, "网络连接正常，类型:" + netType,
						Toast.LENGTH_SHORT).show();
				System.out.println("连接成功了！---");
				promptUpdateApk();
				service.saveStrForFile("false", listMapFileCache,
						"updateVersion_flg");
			}else{
				
//				new Handler().postDelayed(new Runnable() {  
//
//		            @Override  
//		            public void run() {  
//		                Intent intent = new Intent(FirstMainActivity.this,  
//		                		TabFragmentActivity.class);  
//		                startActivity(intent);  
//		                finish();
//		            }  
//		  
//		        }, 2000); 
    			Toast.makeText(FirstMainActivity.this, "服务器未开启！", Toast.LENGTH_LONG).show();
    			CountTimeStop(2000);
    			
    			
    		}
		}  	
	}	
	
	public void CountDownTime(){
		new CountDownTimer(1000, 100){
			@Override
			public void onFinish() {
				Intent intent =new Intent();
				intent.setClass(FirstMainActivity.this, TabFragmentActivity.class);
				startActivity(intent);				
				FirstMainActivity.this.finish();
			}
			@Override
			public void onTick(long millisUntilFinished) {
			}			
		}.start(); 
	}
	public void CountTimeRestart(long time){
		new CountDownTimer(time, 100){
			@Override
			public void onFinish() {
				Intent i = getBaseContext().getPackageManager()  
				        .getLaunchIntentForPackage(getBaseContext().getPackageName());  
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				startActivity(i);
			}
			@Override
			public void onTick(long millisUntilFinished) {
			}			
		}.start(); 
	}
	public void CountTimeStop(long time){
		new CountDownTimer(time, 100){
			@Override
			public void onFinish() {
				finish();
			}
			@Override
			public void onTick(long millisUntilFinished) {
			}			
		}.start(); 
	}
	public void promptUpdateApk(){
		String path=ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_update_xml);
		Map<String,Object> map=AppVersionUtil.getVersionForLoaction(FirstMainActivity.this);   
		Map<String,Object> map1=new HashMap<String, Object>();
		
		try {
			map1=AppVersionUtil.getVersionForServer(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map2=map1;
		if(!map1.get("version").equals(map.get("name"))){			
			/**---------------检查更新版本start------------------*/
			String str=map1.get("description")+
					"\n本系统版本信息：\n"+"版本号："+map.get("code")+"\n版本名称："+map.get("name")	+"\n最新版本信息：版本号"+map1.get("version")+"\n更新内容："+map1.get("content");
			CustomDialog.Builder builder = new CustomDialog.Builder(FirstMainActivity.this);
			builder.setMessage(str);
			builder.setTitle("你是否要更新最新版本?");
			builder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								String strs=(String) map2.get("url");
								downLoadApk(strs);
							}
						});
			builder.setNegativeButton("取消",  
				        new DialogInterface.OnClickListener() {
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {  
				                dialog.dismiss(); 
								/** ---------------定时跳转 start------------------ */
								CountDownTime();
								/** ---------------定时跳转 end------------------ */
				            }  
				        });
			builder.create().show();			
			/**---------------检查更新版本end------------------*/
		}else{
			CountDownTime();
		}
	}
	/*  
	 * 从服务器中下载APK  
	 */    
	public void downLoadApk(final String path) {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(FirstMainActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载 ");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					final File file = AppVersionUtil
							.getFileFromServer(path, pd);
					pd.dismiss(); // 结束掉进度条对话框
					sleep(1000);
					openFile(file);
					finish();
				} catch (Exception e) {
					Toast.makeText(FirstMainActivity.this, "下载失败",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}.start();
	}
	private void openFile(File file) {
		file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
		"application/vnd.android.package-archive");
		startActivity(intent);
	}
	//设置网络连接
	public void popSetNetWorkBox() {
		Builder b = new Builder(this).setTitle("网络错误")
				.setMessage("网络错误，请检查手机网络设置或尝试重启手机程序");
		b.setPositiveButton("退出", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				finish();
			}
		}).setNeutralButton("设置网络", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(Settings.ACTION_SETTINGS);//ACTION_AIRPLANE_MODE_SETTINGS
				startActivity(intent);
				CountTimeRestart(7000);
			}
		}).setNegativeButton("重试", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent i = getBaseContext().getPackageManager()  
				        .getLaunchIntentForPackage(getBaseContext().getPackageName());  
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
				startActivity(i);	
			}
		}).show();
	}
}