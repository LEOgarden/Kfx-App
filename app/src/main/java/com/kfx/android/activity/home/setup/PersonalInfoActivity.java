package com.kfx.android.activity.home.setup;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import com.kfx.android.R;
import com.kfx.android.activity.home.SetupForHomeActivity;
import com.kfx.android.activity.home.setup.detail.ChangePasswordActivity;
import com.kfx.android.activity.home.setup.detail.ChangePersonalInfoActivity;
import com.kfx.android.util.app.UpdateUserApplication;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.view.CustomDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class PersonalInfoActivity extends Activity {
	 private ImageView personal_info_BackBtn;
	 private ImageView personalPic;
	 private ListMapFileCache fileCacheForFile;
	 private RelativeLayout info_personal,exit_personal_btn,change_password;
	 private SelectPicPopupWindow menuWindow;
	@Override
	protected void onCreate(Bundle savedInstanceStates) {
		super.onCreate(savedInstanceStates);
		setContentView(R.layout.personal_information);
		UpdateUserApplication.getInstance().addActivity(
				PersonalInfoActivity.this);
		exit_personal_btn = (RelativeLayout) findViewById(R.id.exit_personal_btn);
		info_personal = (RelativeLayout) findViewById(R.id.info_personal);
		change_password = (RelativeLayout) findViewById(R.id.change_password);
		personalPic = (ImageView) findViewById(R.id.personal_pic);

		personal_info_BackBtn = (ImageView) findViewById(R.id.personal_info_BackBtn);
		personal_info_BackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this,
						SetupForHomeActivity.class);
				startActivity(intent);
				PersonalInfoActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
				UpdateUserApplication.getInstance().removeActivity(PersonalInfoActivity.this);
				PersonalInfoActivity.this.finish();
			}
		});

		exit_personal_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomDialog.Builder builder = new CustomDialog.Builder(
						PersonalInfoActivity.this);
				builder.setMessage("                   确定要注销用户吗?");
				builder.setTitle("提示");
				builder.setPositiveButton("是",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								fileCacheForFile = new ListMapFileCache();
								ListMapService servicea = new ListMapServiceImpl();
								servicea.saveStrForFile("false",
										fileCacheForFile, "sealuser_loginflg");
								servicea.saveStrForFile("", fileCacheForFile,
										"sealuser_loginmsg");

								Intent intent = new Intent();
								intent.setClass(PersonalInfoActivity.this,
										SetupForHomeActivity.class);
								startActivity(intent);
								UpdateUserApplication.getInstance().removeActivity(PersonalInfoActivity.this);
								PersonalInfoActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
								PersonalInfoActivity.this.finish();
							}
						});
				builder.setNegativeButton("否",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();

			}
		});
		info_personal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this,
						ChangePersonalInfoActivity.class);
				startActivity(intent);
				PersonalInfoActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			}
		});
		change_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this,
						ChangePasswordActivity.class);
				startActivity(intent);
				PersonalInfoActivity.this.overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
			}
		});
	
		personalPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//实例化SelectPicPopupWindow
				menuWindow = new SelectPicPopupWindow(PersonalInfoActivity.this, itemsOnClick);
				//显示窗口
				menuWindow.showAtLocation(PersonalInfoActivity.this.findViewById(R.id.personal_info) 
						, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);//设置layout在PopupWindow中显示的位置
			}
		});
	}
	
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				getPicFromCarema();				
				break;
			case R.id.btn_pick_photo:
				//从相册中读取本地图片
				getPicFromGallery();
				break;
			}
			
				
		}
		private void getPicFromGallery() {
			 //左起参数：选择行为权限，系统本地相册URI路径
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //向onActivityResult发送intent，requestCode为GET_PIC_FROM_GALLERY
            startActivityForResult(i, 2);
		}
		/**
		 * 从onActivityResult发送请求，得到拍摄生成的图片
		 */
		private void getPicFromCarema() {
			//打开本地相机
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//确定存储拍照得到图片的路径
			File mFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
			try {
				mFile.createNewFile();
			} catch (Exception e) {
			}
			//加载uri型的文件路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
			//向onActivityResult发送intent，requestCode=1
			startActivityForResult(intent, 1);
		}
    	
    };
    
    @SuppressLint("SdCardPath")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch (requestCode) {
		case 1:
			if(resultCode == Activity.RESULT_OK){		
				//检测sd卡是否可用
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
					Log.i("TestFile",  
							"SD card is not avaiable/writeable right now."); 
					return;
				}
				new DateFormat();
				String name = DateFormat.format("yyyyMMdd-hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
				Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
				
				/*Bundle bundle = data.getExtras();
				//获取相机返回的数据，并转换为Bitmap格式
				Bitmap bitmap = (Bitmap) bundle.get("data");*/
				
				FileOutputStream fileOutPutStream = null;
				File file = new File("/sdcard/Image/");
				//创建文件夹
				file.mkdir();
				String fileName = "/sdcard/Image/"+name;
				
				Bitmap bitmap = BitmapFactory.decodeFile(fileName);
				
				try {
					fileOutPutStream = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						fileOutPutStream.flush();
						fileOutPutStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					//将图片显示在控件里
					personalPic.setImageBitmap(bitmap);
				} catch (Exception e) {
				}
			}
			break;

		case 2:
			if(resultCode == RESULT_OK){
				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
					personalPic.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		}
    }
}

