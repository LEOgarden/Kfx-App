package com.kfx.android.util;

import java.util.List;
import android.content.Context;
import android.content.DialogInterface;
import com.kfx.android.bean.DialogMsg;
import com.kfx.android.util.app.MyApplication;
import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.view.CustomDialog;

public class PopDialogBox {

	public static void getPopDialogBox(DialogMsg dialogMsg,final Context context,final String flg){
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(dialogMsg.getQuestion());
		builder.setTitle(dialogMsg.getTitle());
		if(dialogMsg.getYesMsg()!=null&&dialogMsg.getYesMsg().length()>0){
			builder.setPositiveButton(dialogMsg.getYesMsg(),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if(flg.equals("clearCache")){
								clearCacheForFile();
							}else if(flg.equals("mainLoginOut")){
								//主界面退出
								loginOut(1);
							}else if(flg.equals("safeLoginOut")){
								//安全退出
								loginOut(2);
							}
						}
					});
		}
		if(dialogMsg.getNoMsg()!=null&&dialogMsg.getNoMsg().length()>0){
			builder.setNegativeButton(dialogMsg.getNoMsg(),  
			        new DialogInterface.OnClickListener() {
			            @Override  
			            public void onClick(DialogInterface dialog, int which) {  
			                dialog.dismiss();  
			            }  
			        }); 
		} 
		builder.create().show();
		
	}
	/**
	 * 清空下载的内存
	 */
	public static void clearCacheForFile(){
		ListMapFileCache listMapFileCache=new ListMapFileCache();
		ImageFileCache imageFileCache=new ImageFileCache();
		ListMapService service=new ListMapServiceImpl();
		service.clearCacheForFile(listMapFileCache, imageFileCache);
	}
	/**
	 * 注销登录
	 * @param i
	 */
	public static void loginOut(int i) {
		// 登录信息，登录标识符设置
		if(i==1){
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		if (i == 2) {
			ListMapFileCache fileCacheForFile = new ListMapFileCache();
			ListMapService service = new ListMapServiceImpl();
			service.saveStrForFile("false", fileCacheForFile,
					"sealuser_loginflg");
			service.saveStrForFile("", fileCacheForFile, "sealuser_loginmsg");
			MyApplication.getInstance().exit();
		}
		
	}
}
