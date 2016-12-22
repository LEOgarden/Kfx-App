package com.kfx.android.activity.service.seal.val;

import com.kfx.android.R;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.js.JSValUtil;
import com.kfx.android.util.md5.MD5Util;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.SealService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.SealServiceImpl;
import com.kfx.android.view.CustomDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SealValSealActivity extends Activity {
	private ImageView iv;
	private Button sealUserValLoginBtn, sealUserValExitBtn, yz_tab_btn,
			nfc_tab_btn;
	private EditText sealFullNameEdTxt;
	private EditText sealCodeEdTxt;
	private EditText sealTelpEdTxt;
	private Button valBtn;
	private TextView result_tv,nfc_result;
	private ListMapFileCache fileCacheForFile;
	private RelativeLayout nfc_tab, relativeLayout2, denglu_btn,
			relativeLayout3;
	private NfcAdapter nfcAdapter;
	TextView promt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seal_common_valseal);
		nfc_tab = (RelativeLayout) findViewById(R.id.nfc_tab);
		yz_tab_btn = (Button) findViewById(R.id.yz_tab_btn);
		nfc_tab_btn = (Button) findViewById(R.id.nfc_tab_btn);
		nfc_result=(TextView)findViewById(R.id.nfc_result);
		// 注销隐藏
		sealUserValLoginBtn = (Button) findViewById(R.id.sealUserValLoginBtn);
		sealUserValExitBtn = (Button) findViewById(R.id.sealUserValExitBtn);
		
		sealUserValLoginBtn.setOnClickListener(loginListener);
		sealUserValExitBtn.setOnClickListener(exitListener);
		
		ListMapService service1 = new ListMapServiceImpl();
		fileCacheForFile = new ListMapFileCache();
		
		String loginMsg = service1.getStrForFile(fileCacheForFile,
				"sealuser_loginmsg");
		if (loginMsg != null && loginMsg.trim().length() > 0) {
			// 隐藏登录 显示注销按钮
			sealUserValLoginBtn.setVisibility(View.GONE);
			sealUserValExitBtn.setVisibility(View.VISIBLE);
		} else {
			// 隐藏注销 显示登录按钮
			sealUserValLoginBtn.setVisibility(View.VISIBLE);
			sealUserValExitBtn.setVisibility(View.GONE);
		}
		//nfc 扫描
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);  
		nfc_tab_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
				denglu_btn = (RelativeLayout) findViewById(R.id.denglu_btn);
				relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
				relativeLayout2.setVisibility(View.GONE);
				denglu_btn.setVisibility(View.GONE);
				relativeLayout3.setVisibility(View.VISIBLE);
				nfc_tab.setBackgroundResource(R.drawable.nfc_tab_2);
				if (nfcAdapter == null) {
					Toast.makeText(SealValSealActivity.this, "设备不支持NFC！",
							Toast.LENGTH_LONG).show();
					// finish();
					// return;
				}
				if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
					Toast.makeText(SealValSealActivity.this,
							"请在系统设置中先启用NFC功能！", Toast.LENGTH_LONG).show();
					// finish();
					// return;
				}
				if (nfcAdapter != null && nfcAdapter.isEnabled()) {
					Toast.makeText(SealValSealActivity.this,
							"系统设置中已经启用NFC功能！", Toast.LENGTH_LONG).show();
					promt=(TextView) findViewById(R.id.common);
				}
			}
		});
		yz_tab_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
				denglu_btn = (RelativeLayout) findViewById(R.id.denglu_btn);
				relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);

				relativeLayout2.setVisibility(View.VISIBLE);
				denglu_btn.setVisibility(View.VISIBLE);
				relativeLayout3.setVisibility(View.GONE);
				nfc_tab.setBackgroundResource(R.drawable.nfc_tab_1);
			}
		});

		// 设置印章通讯录主界面返回按钮
		iv = (ImageView) findViewById(R.id.sealValSealBackBtn);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SealValSealActivity.this.finish();
			}
		});
		//
		sealFullNameEdTxt = (EditText) findViewById(R.id.sealFullNameValText);
		sealCodeEdTxt = (EditText) findViewById(R.id.sealCodeValText);
		sealTelpEdTxt = (EditText) findViewById(R.id.sealTelpValText);
		valBtn = (Button) findViewById(R.id.commonValSealBtn);
		result_tv = (TextView) findViewById(R.id.common);
		valBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String fullName = sealFullNameEdTxt.getText().toString().trim();
				String code = sealCodeEdTxt.getText().toString().trim();
				String telp = sealTelpEdTxt.getText().toString().trim();
				String str = code + fullName + telp;
				String md5 = MD5Util.markMD5(str);
				SealService service = new SealServiceImpl();

				fileCacheForFile = new ListMapFileCache();
				String returnStr = "";
				ListMapService service1 = new ListMapServiceImpl();
				String loginFlg = service1.getStrForFile(fileCacheForFile,
						"sealuser_loginflg");
				//第一次登陆的时候文件空 获取的值为 :"" 赋值为false-----------start
				if(loginFlg.length()==0){
					loginFlg="false";
				}
				//第一次登陆的时候文件空 获取的值为 :"" 赋值为false-----------end
				boolean Loginflg = Boolean.parseBoolean(loginFlg);
				if (Loginflg) {
					if (JSValUtil.valSealCode(code)) {
						if (fullName != null && fullName.trim().length() > 0) {
							if (JSValUtil.valSealTelp(telp)) {
								boolean flg = service.valSealMsg(code, md5);
								if (flg) {
									result_tv.setText("");
									returnStr = "您验证的印章为合法印章!\n印章名称为：" + code
											+ " \n印章编号为:" + fullName
											+ " \n预保留手机号：" + telp;
									result_tv.setText(returnStr);
									result_tv.setTextSize(16);
									result_tv.setTextColor(Color
											.parseColor("#0083f3"));
								} else {
									returnStr = "您输入的印章为非法印章或者录入信息有误，请确认后进行验证";
								}
							} else {
								returnStr = "请输入合法的手机号或者电话号码！";
							}
						} else {
							returnStr = "请输入正确的印章全称！";
						}
					} else {
						returnStr = "请输入正确的13位印章编码";
					}
				} else {
					returnStr = "您还未登录客户端，请先登录系统才能进行验章功能实现";
				}
				Toast toast = Toast.makeText(SealValSealActivity.this,
						returnStr, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});

	}

	// 转到登录界面
	OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SealValSealActivity.this,
					SealUserLoginActivity.class);
			intent.putExtra("sealUserToLoginLayoutFlg", "sealValLayout");
			startActivity(intent);
			SealValSealActivity.this.finish();
		}
	};
	OnClickListener exitListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialog.Builder builder = new CustomDialog.Builder(SealValSealActivity.this);
			builder.setMessage("                 确定要注销用户吗?");
			builder.setTitle("提示");
			builder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							sealUserValLoginBtn.setVisibility(View.VISIBLE);
							sealUserValExitBtn.setVisibility(View.GONE);
							fileCacheForFile = new ListMapFileCache();
							ListMapService servicea = new ListMapServiceImpl();
							servicea.saveStrForFile("false", fileCacheForFile, "sealuser_loginflg");
							servicea.saveStrForFile("", fileCacheForFile, "sealuser_loginmsg");
							SealValSealActivity.this.finish();
						}
					});  
			builder.setNegativeButton("否",  
				        new DialogInterface.OnClickListener() {
				            @Override  
				            public void onClick(DialogInterface dialog, int which) {  
				                dialog.dismiss();  
				            }  
				        });  
			builder.create().show();
		}
	};
    @Override   
    protected void onResume() {   
        super.onResume();   
        //得到是否检测到ACTION_TECH_DISCOVERED触发   
        ListMapService service1 = new ListMapServiceImpl();
		fileCacheForFile = new ListMapFileCache();
		
		String loginMsg = service1.getStrForFile(fileCacheForFile,
				"sealuser_loginmsg");
		if (loginMsg != null && loginMsg.trim().length() > 0) {
			// 隐藏登录 显示注销按钮
			 if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {   
		           //处理该intent   
		           processIntent(getIntent());   
		     } 
		} else {
			Intent intent = new Intent();
			intent.setClass(SealValSealActivity.this,
					SealUserLoginActivity.class);
			intent.putExtra("sealUserToLoginLayoutFlg", "sealValLayout");
			startActivity(intent);
			finish();
		}
         
    }  
    private void processIntent(Intent intent) {   
        //取出封装在intent中的TAG   
		String id=ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
		/*************************************************************/
		Builder builder  = new Builder(SealValSealActivity.this);
		 builder.setTitle("确认" ) ;
		 builder.setMessage(""+NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())+intent.getByteArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES));
		 builder.setPositiveButton("是" ,  null );
		 builder.show();
		/*************************************************************/
        String metaInfo = "";
        metaInfo += "芯片id:"+id+"\n";
        String where="chip='"+id+"'";
        SealService service = new SealServiceImpl();
//        List<Map<String, Object>> chips=service.getChips(where);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		denglu_btn = (RelativeLayout) findViewById(R.id.denglu_btn);
		relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		relativeLayout2.setVisibility(View.GONE);
		denglu_btn.setVisibility(View.GONE);
		relativeLayout3.setVisibility(View.VISIBLE);
		nfc_tab.setBackgroundResource(R.drawable.nfc_tab_2);
		
//        nfc_result.setText(chips.toString());   
//        Toast.makeText(getApplicationContext(), chips.toString(), Toast.LENGTH_SHORT).show();
        
     }
  //字符序列转换为16进制字符串   
    private String bytesToHexString(byte[] src) {   
        StringBuilder stringBuilder = new StringBuilder("0x");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        char[] buffer = new char[2];   
        for (int i = 0; i < src.length; i++) {   
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);   
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);   
            System.out.println(buffer);   
           stringBuilder.append(buffer);   
        }   
        return stringBuilder.toString();   
    } 
   
    private String ByteArrayToHexString(byte [] inarray) { //converts byte arrays to string
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j) 
            {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
            }
        return out;
    }
}
/*nfc 验章方法 start*/
//private void processIntent(Intent intent) {   
//    //取出封装在intent中的TAG   
//	String id=ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
//    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);   
//    for (String tech : tagFromIntent.getTechList()) {   
//        System.out.println(tech);   
//    }   
//    boolean auth = false;   
//    //读取TAG   
//    //MifareClassic nfc=MifareClassic.get(tagTmp);  
//    MifareClassic mfc = MifareClassic.get(tagFromIntent);   
//    try {   
//        String metaInfo = "";   
//        //Enable I/O operations to the tag from this TagTechnology object.   
//        mfc.connect();   
//        int type = mfc.getType();//获取TAG的类型   
//       int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数   
//        String typeS = "";   
//        switch (type) {   
//        case MifareClassic.TYPE_CLASSIC:   
//            typeS = "TYPE_CLASSIC";   
//            break;   
//        case MifareClassic.TYPE_PLUS:   
//            typeS = "TYPE_PLUS";   
//            break;   
//        case MifareClassic.TYPE_PRO:   
//            typeS = "TYPE_PRO";   
//            break;   
//        case MifareClassic.TYPE_UNKNOWN:   
//            typeS = "TYPE_UNKNOWN";   
//            break;   
//        } 
//
//        metaInfo += "芯片id:"+id+"\n"+"mfc:"+mfc+"\n"+"tag:"+mfc.getTag()+"\n"+"芯片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"   
//                + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";   
//        for (int j = 0; j < sectorCount; j++) {   
//            //Authenticate a sector with key A.   
//            auth = mfc.authenticateSectorWithKeyA(j,   
//                    MifareClassic.KEY_DEFAULT);   
//            int bCount;   
//            int bIndex;   
//            if (auth) {   
//                metaInfo += "Sector " + j + ":验证成功\n";   
//                // 读取扇区中的块   
//                bCount = mfc.getBlockCountInSector(j);   
//                bIndex = mfc.sectorToBlock(j);   
//                for (int i = 0; i < bCount; i++) {   
//                    byte[] data = mfc.readBlock(bIndex);   
//                    metaInfo += "Block " + bIndex + " : "   
//                            + bytesToHexString(data) + "\n";   
//                    bIndex++;   
//                }   
//            } else {   
//                metaInfo += "Sector " + j + ":验证失败\n";   
//            }   
//        } 
//        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
//		denglu_btn = (RelativeLayout) findViewById(R.id.denglu_btn);
//		relativeLayout3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
//		relativeLayout2.setVisibility(View.GONE);
//		denglu_btn.setVisibility(View.GONE);
//		relativeLayout3.setVisibility(View.VISIBLE);
//		nfc_tab.setBackgroundResource(R.drawable.nfc_tab_2);
//        nfc_result.setText(metaInfo);   
//    } catch (Exception e) {   
//       e.printStackTrace();   
//   }   
// }
/*nfc 验章方法 end*/