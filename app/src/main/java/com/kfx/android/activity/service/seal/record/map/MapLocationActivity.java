package com.kfx.android.activity.service.seal.record.map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kfx.android.R;
import com.kfx.android.activity.service.seal.val.SealUserLoginActivity;
import com.kfx.android.activity.service.seal.val.SealValSealActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapLocationActivity extends Activity implements BaiduMap.OnMapClickListener,OnGetRoutePlanResultListener{
	private ImageView sealFactoryMapBackBtn;
	private RelativeLayout  text_position;
 	private Button map_navigation_Btn;
	MapView mMapView = null;  
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;  
	RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用  
	private TextView tv;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //获取地图控件引用  
        SDKInitializer.initialize(getApplicationContext());  
        setContentView(R.layout.seal_factory_map);  
        mMapView = (MapView) findViewById(R.id.bmapView);  
        // 开启定位图层  
        Intent intent=getIntent();
        String address=intent.getStringExtra("com.kfx.service.seal.record.factory.address");
        String factory_name = intent.getStringExtra("demo.android.factory.name");
        String position = "东经:"
				+ intent.getStringExtra("demo.android.factory.xPosition")
				+ "北纬:"
				+ intent.getStringExtra("demo.android.factory.yPosition");
		Toast.makeText(MapLocationActivity.this, "address:"+address, Toast.LENGTH_LONG).show();
		Toast.makeText(MapLocationActivity.this, "position:"+position, Toast.LENGTH_LONG).show();
		String x=intent.getStringExtra("demo.android.factory.xPosition");
		String y= intent.getStringExtra("demo.android.factory.yPosition");
        BaiduMap mBaiduMap=mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);  
       
        mBaiduMap.clear();
        // 构造定位数据  
        MyLocationData locData = new MyLocationData.Builder()  
            .accuracy(0)
            .direction(0).latitude(Float.parseFloat(y))  
            .longitude(Float.parseFloat(x)).build();  
        // 设置定位数据  
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标） 
        tv=new TextView(MapLocationActivity.this);
      /*  text_position=(RelativeLayout) findViewById(R.id.text_position);
        text_position.setVisibility(View.VISIBLE);*/
        //tv=(TextView) findViewById(R.id.map_title);
        tv.setText("刻制社:"+factory_name+"\n地址:"+address+"\n");
		/*Drawable drawable= getResources().getDrawable(R.drawable.position10);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, null, drawable);*/
        tv.setMaxWidth(250);
        tv.setTextSize(10);
        tv.setTextColor(Color.parseColor("#202020"));
        tv.setBackgroundResource(R.drawable.position10);
        //tv.setBackgroundColor(Color.WHITE);
        
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(tv);
       // BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.position7);   
        OverlayOptions options=new MarkerOptions().icon(descriptor).title("abc").position(new LatLng(Double.parseDouble(x), Double.parseDouble(y))).rotate(0).draggable(true).zIndex(5).visible(true);
        mBaiduMap.addOverlay(options);//map.MyLocationConfiguration.LocationMode 
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, descriptor);
        mBaiduMap.setMyLocationConfigeration(config); 
        mBaiduMap.setTrafficEnabled(true);
//        mBaiduMap.setOnMapClickListener(this); 
//        mSearch = RoutePlanSearch.newInstance();  
//        mSearch.setOnGetRoutePlanResultListener(this); 
        // 当不需要定位图层时关闭定位图层  
       // mBaiduMap.setMyLocationEnabled(false);
        
        
        
        /*
         * 地图路线导航
         * */
        map_navigation_Btn = (Button) findViewById(R.id.map_navigation);
        map_navigation_Btn.setOnClickListener(map_navigation_Listener);
        sealFactoryMapBackBtn = (ImageView)findViewById(R.id.sealFactoryMapBackBtn);
        sealFactoryMapBackBtn.setOnClickListener(sealFactoryMapBackListener);
        
    } 
// 关闭界面
    OnClickListener sealFactoryMapBackListener = new OnClickListener() {			
		@Override
		public void onClick(View v) {
//			Intent intent=new Intent();
//			intent.setClass(SealUserRegisterActivity.this, SealUserLoginActivity.class);
//			startActivity(intent);
			finish();
		}
    };
 // 转到导航界面
 	OnClickListener map_navigation_Listener = new OnClickListener() {
 		@Override
 		public void onClick(View v) {
 			Intent intent = getIntent();
 			
 			intent.setClass(MapLocationActivity.this,MapNavigationActivity.class);
 			startActivity(intent);
 		}
 	};
    public void SearchButtonProcess(View v) { 
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "北京西站");  
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "中国农业大学西校区");  
        mSearch.drivingSearch((new DrivingRoutePlanOption())  
                .from(stNode)  
                .to(enNode)); 
    }
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();  
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	 //定制RouteOverly  
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {  
  
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {  
            super(baiduMap);  
        }  
  
        @Override  
        public BitmapDescriptor getStartMarker() {  
            if (useDefaultIcon) {  
                return BitmapDescriptorFactory.fromResource(R.drawable.pisition1);  
            }  
            return null;  
        }  
  
        @Override  
        public BitmapDescriptor getTerminalMarker() {  
            if (useDefaultIcon) {  
                return BitmapDescriptorFactory.fromResource(R.drawable.position7);  
            }  
            return null;  
        }  
    }  
}
