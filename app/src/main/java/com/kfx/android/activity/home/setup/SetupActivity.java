package com.kfx.android.activity.home.setup;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.activity.home.setup.detail.CopyrightActivity;
import com.kfx.android.activity.home.setup.detail.FeedbackActivity;
import com.kfx.android.activity.home.setup.detail.FnIntroductionActivity;
import com.kfx.android.bean.DialogMsg;
import com.kfx.android.util.PopDialogBox;
import com.kfx.android.util.app.MyApplication;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.conn.ConCla;
import com.kfx.android.util.version.AppVersionUtil;
import com.kfx.android.view.CustomDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SetupActivity extends Activity {
	 private ImageView back_setup,dynamic;
	 private TextView version_dynamic;
	 private RelativeLayout clearCahceBtn;
	 private Button exit_system;
	 private RelativeLayout about_system,copyrightbtn,version_update_btn,fn_introduction,feedback;
	 public static Map<String,Object> map2=new HashMap<String, Object>();
	 @Override 
	 protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.setup_msg);
	 MyApplication.getInstance().addActivity(SetupActivity.this);
	 /**--------------------返回 start--------------------*/
	 back_setup = (ImageView)findViewById(R.id.back_setup);	
	 back_setup.setOnClickListener(backListener);
	 /**--------------------返回 end--------------------*/
	 /**--------------------功能介绍 start--------------------*/
	 fn_introduction=(RelativeLayout) findViewById(R.id.fn_introduction);
	 fn_introduction.setOnClickListener(fnintroductionListener);
	 /**--------------------功能介绍 end--------------------*/
	 /**--------------------关于系统 start--------------------*/
	 about_system=(RelativeLayout) findViewById(R.id.about_system);
	 about_system.setOnClickListener(aboutSystemListener);
	 /**--------------------关于系统 end--------------------*/
	 /**--------------------安全退出 start--------------------*/
	 exit_system=(Button) findViewById(R.id.exit_system);
	 exit_system.setOnClickListener(exitListener);	 
	 /**--------------------安全退出 end--------------------*/
	 /**--------------------清空缓存start--------------------*/
	 clearCahceBtn=(RelativeLayout) findViewById(R.id.clearCahceBtn);
	 clearCahceBtn.setOnClickListener(clearListener);
	 /**--------------------清空缓存 end--------------------*/
	 /**--------------------清空缓存start--------------------*/
	 feedback=(RelativeLayout) findViewById(R.id.feedback);
	 feedback.setOnClickListener(feedbackListener);
	 /**--------------------清空缓存 end--------------------*/
	 /**--------------------版本更新start--------------------**/
	 boolean flg= AppVersionUtil.isNewstVersion(SetupActivity.this);
	 version_dynamic=(TextView) findViewById(R.id.version_dynamic);
	 dynamic=(ImageView) findViewById(R.id.dynamic);
	 version_update_btn=(RelativeLayout) findViewById(R.id.version_update);
	 if(flg){
		 //最新版本 
		 version_dynamic.setText("已是最新版本");
		 dynamic.setVisibility(View.GONE);
		 
	 }else{
		 //需要更新
		 version_dynamic.setText("您的系统有新的版本");
		 dynamic.setVisibility(View.VISIBLE);
		 version_update_btn.setOnClickListener(updateListener);
	 }
	 /**--------------------版本更新end--------------------**/
	 
	 /**--------------------版权声明start--------------------*/
	 copyrightbtn=(RelativeLayout) findViewById(R.id.copyrightbtn);
	 copyrightbtn.setOnClickListener(copyrightListener);
	 /**--------------------版权声明end--------------------*/
	 }
	 OnClickListener copyrightListener=new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetupActivity.this, CopyrightActivity.class);
				startActivity(intent);
				SetupActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			}
		};
	 OnClickListener backListener=new OnClickListener() {		
		@Override
		public void onClick(View v) {
			MyApplication.getInstance().removeActivity(SetupActivity.this);
			SetupActivity.this.finish();
			SetupActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
		}
	};
	 OnClickListener exitListener=new OnClickListener() {		
		@Override
		public void onClick(View v) {
			 DialogMsg dialog=new DialogMsg();
			 dialog.setQuestion("                  确定要退出吗?");
			 dialog.setTitle("提示");
			 dialog.setYesMsg("确定");
			 dialog.setNoMsg("取消");
			 PopDialogBox.getPopDialogBox(dialog, SetupActivity.this, "safeLoginOut");
		}
	};
	 OnClickListener clearListener=new OnClickListener() {
		 @Override
		 public void onClick(View v) {			
			 DialogMsg dialog=new DialogMsg();
			 dialog.setQuestion("你确定要清除缓存内的信息和图片么?");
			 dialog.setTitle("提示");
			 dialog.setYesMsg("确定");
			 dialog.setNoMsg("取消");
			 PopDialogBox.getPopDialogBox(dialog, SetupActivity.this, "clearCache");
			 Toast.makeText(SetupActivity.this, "清空缓存", Toast.LENGTH_LONG).show();			
		 }
	 };
	 OnClickListener fnintroductionListener=new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SetupActivity.this, FnIntroductionActivity.class);
				startActivity(intent);
				SetupActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			}
		};
	 OnClickListener aboutSystemListener=new OnClickListener() {		
		@Override
		public void onClick(View v) {
			Map<String,Object> map=AppVersionUtil.getVersionForLoaction(SetupActivity.this); 
			String str="";
			if(map!=null){
				str="系统名称："+getResources().getString(R.string.app_name)+"\n"+"版本号："+map.get("code")+"\n版本名称:"+map.get("name")+"\n开发者："+getResources().getString(R.string.app_company);
			}
			DialogMsg dialog=new DialogMsg();
			dialog.setTitle("关于系统");
			dialog.setQuestion(str);
			dialog.setNoMsg("关闭");
			PopDialogBox.getPopDialogBox(dialog, SetupActivity.this, "aboutSystem");
		}
	};
	OnClickListener feedbackListener=new OnClickListener() {		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SetupActivity.this, FeedbackActivity.class);
			startActivity(intent);
			SetupActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
		}
	};
	OnClickListener updateListener=new OnClickListener() {		
		@Override
		public void onClick(View arg0) {
			 Toast.makeText(SetupActivity.this, "版本更新", Toast.LENGTH_LONG).show();	
			 promptUpdateApk();			
		}
	};
	public void promptUpdateApk(){
		String path=ConCla.getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_update_xml);
		Map<String,Object> map=AppVersionUtil.getVersionForLoaction(SetupActivity.this);   
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
			CustomDialog.Builder builder = new CustomDialog.Builder(SetupActivity.this);
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
				            }  
				        });
			builder.create().show();
			/**---------------检查更新版本end------------------*/
		}	
	}
	/*  
	 * 从服务器中下载APK  
	 */    
	public void downLoadApk(final String path) {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(SetupActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载");
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
				} catch (Exception e) {
					Toast.makeText(SetupActivity.this, "下载失败",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}.start();
	}
	private void openFile(File file) {
		// TODO Auto-generated method stub
		file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
		"application/vnd.android.package-archive");
		startActivity(intent);
	}	
	  
}