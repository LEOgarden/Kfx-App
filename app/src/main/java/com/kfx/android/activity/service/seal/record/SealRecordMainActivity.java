package com.kfx.android.activity.service.seal.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.activity.service.seal.record.map.MapNearByFactoryActivity;
import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ImageMemoryCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;
import com.kfx.android.util.gps.GPSUtil;
import com.kfx.android.util.map.ListUtil;
import com.kfx.android.util.map.MapUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.SealService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.SealServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class SealRecordMainActivity extends Activity {
	private ImageView iv;
	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    SimpleAdapter companyAdapter=null;
    static int provincePosition = 0;
    List<Map<String,Object>> sealRecords=new ArrayList<Map<String,Object>>();
    List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    //省级选项值
    private String[] provinceNameStrs =null;//,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};
    //市级选项值
    private String[][] cityNameStrs = null;  
    static int citynum=0;
    public ListView companyListView;
	private ImageMemoryCache memoryCache;  
    private ImageFileCache fileCache; 	   
	private ListMapMemoryCache MemoryCacheForFile;  
    private ListMapFileCache fileCacheForFile; 	
    public Button nearby_sealfactory;
    public Location mLocation = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seal_record_main);
		//设置印章通讯录主界面返回按钮
		iv=(ImageView) findViewById(R.id.sealRecordMainBackBtn);
		iv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SealRecordMainActivity.this.finish();
			}
		});
		setLocation();
		nearby_sealfactory=(Button) findViewById(R.id.nearby_sealfactory);
		nearby_sealfactory.setOnClickListener(new View.OnClickListener() {				
			@Override
			public void onClick(View v) {
				if(mLocation!=null){
					Intent intent=getIntent();
					intent.setClass(SealRecordMainActivity.this, MapNearByFactoryActivity.class);
					startActivity(intent);	
				}else{
					setLocation();			
				}								
			}
		});
	
		/**
		 * 处理图片的缓存
		 */
		memoryCache=new ImageMemoryCache(this);  
        fileCache=new ImageFileCache(); 
    	MemoryCacheForFile=new ListMapMemoryCache(this);  
    	fileCacheForFile=new ListMapFileCache(); 		
		//初始化下拉框 以及listview
		setSpinner();
	}
	public void setLocation(){
		String msg = "";		
		if (GPSUtil.gpsIsOpen(SealRecordMainActivity.this)) {
			mLocation = GPSUtil.getLocation(SealRecordMainActivity.this);
			if (mLocation != null) {				
				msg = "维度:" + mLocation.getLatitude() + "\n经度:"
						+ mLocation.getLongitude();
			} else {
				msg = "GPS开启,但无法定位到当前位置";				
			}
		} else {
			msg = "GPS未开启";			
		}
		Toast toast = Toast.makeText(SealRecordMainActivity.this, msg,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	private void setSpinner(){  
		SealService service=new SealServiceImpl();
		ListMapService service1=new ListMapServiceImpl();
		String location =service1.getStrForFile(
				fileCacheForFile, "seal_record_location");
		sealRecords=service.getSealRecords("com.kfx.azs.bean.seal.Factory", MemoryCacheForFile, fileCacheForFile, "listforSealRecorss", new Date());
		sealRecords=MapUtil.getListMapAddImage(sealRecords, memoryCache, fileCache, R.drawable.seal_factory_model);
		sealRecords=MapUtil.getListMapAddForDistance(sealRecords, location);
		//获取省份，市区下拉列表
		provinceNameStrs=ListUtil.getProvinceName(sealRecords);		
		cityNameStrs=ListUtil.getCityName(sealRecords, provinceNameStrs);
		
        provinceSpinner = (Spinner)findViewById(R.id.spin_province);
        citySpinner = (Spinner)findViewById(R.id.spin_city);
        
        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(SealRecordMainActivity.this,
                android.R.layout.simple_spinner_item, provinceNameStrs);
        provinceAdapter.setDropDownViewResource(R.layout.drop_down_item);
        provinceSpinner.setAdapter(provinceAdapter);        
        provinceSpinner.setPrompt( " 请选择省份： " );
        provinceSpinner.setSelection(0,true);  //设置默认选中项，此处为默认选中第4个值

        cityAdapter = new ArrayAdapter<String>(SealRecordMainActivity.this, 
                android.R.layout.simple_spinner_item, cityNameStrs[0]);
        cityAdapter.setDropDownViewResource(R.layout.drop_down_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setPrompt("请选择市区：");
        citySpinner.setSelection(0,true); 	
		
		companyListView=(ListView) findViewById(R.id.companyList);
		//provinceNameStrs[0] cityNameStrs[0][0]
		setListNewsForFactory(sealRecords, provinceNameStrs[0], cityNameStrs[0][0]);
        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号
                //将地级适配器的值改变为city[position]中的值
                cityAdapter = new ArrayAdapter<String>(
                		SealRecordMainActivity.this, android.R.layout.simple_spinner_item, cityNameStrs[position]);
                cityAdapter.setDropDownViewResource(R.layout.drop_down_item);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                citySpinner.setPrompt("请选择市区：");
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用        
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0){

            }
        });        
        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int position, long arg3){
                citynum=position;
                setListNewsForFactory(sealRecords,provinceNameStrs[provincePosition],cityNameStrs[provincePosition][position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
	
	public void setListNewsForFactory(List<Map<String,Object>> records,String province,String city){
		listItems=ListUtil.getCompanyMap(sealRecords, province, city);
		listItems=ListUtil.sortFactoryByDistace(listItems);
		System.out.println("ai-------------"+listItems+"----------------------------");
		companyAdapter=new SimpleAdapter(SealRecordMainActivity.this,listItems,R.layout.list_record_item,new String [] {"name","image","artificial_persion_mobile","distance","address"},new int[] {R.id.list_record_title,R.id.list_record_image,R.id.list_record_telp,R.id.list_record_position,R.id.list_record_address});
		companyAdapter.setViewBinder(new ViewBinder() {				
			@Override
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if(view instanceof ImageView && data instanceof Bitmap){
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap)data);
					return true;
				}else{
					return false;
				}
			}
		});
		companyListView.setAdapter(companyAdapter);
		companyListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			  int position, long id) {
			 	Intent intent=new Intent();
			 	intent.putExtra("demo.android.factory.name", listItems.get(position).get("name").toString());
			 	intent.putExtra("demo.android.factory.user", listItems.get(position).get("artificial_person_name").toString());
			 	intent.putExtra("demo.android.factory.address", listItems.get(position).get("address").toString());
			 	intent.putExtra("demo.android.factory.telp", listItems.get(position).get("artificial_persion_mobile").toString());
			 	intent.putExtra("demo.android.factory.city", listItems.get(position).get("area_name").toString());
			 	intent.putExtra("demo.android.factory.province", listItems.get(position).get("area_first_name").toString());
			 	intent.putExtra("demo.android.factory.introduction", listItems.get(position).get("introduction").toString());
			 	intent.putExtra("demo.android.factory.xPosition", listItems.get(position).get("x").toString());
			 	intent.putExtra("demo.android.factory.yPosition", listItems.get(position).get("y").toString());
			 	Object obj=listItems.get(position).get("photoPath");
			 	String path="";
			 	if(obj!=null){
			 		path=listItems.get(position).get("photoPath").toString();
			 	}
			 	intent.putExtra("demo.android.factory.photoPath",path );
			 	intent.setClass(SealRecordMainActivity.this,SealRecordDetailActivity.class);
			 	startActivity(intent);		
			}
		});
	}
}