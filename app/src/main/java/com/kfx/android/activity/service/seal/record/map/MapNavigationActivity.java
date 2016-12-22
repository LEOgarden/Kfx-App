package com.kfx.android.activity.service.seal.record.map;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kfx.android.R;
import com.kfx.android.activity.service.SealServiceMainActivity;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;
import com.kfx.android.util.common.Properties;
import com.kfx.android.util.gps.Distance;
import com.kfx.android.util.gps.GPSUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.SealService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.SealServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapNavigationActivity extends Activity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener ,OnGetPoiSearchResultListener{
	private ImageView sealMapNavigationBackBtn;
	Button mBtnPre = null;
	Button mBtnNext = null;
	int nodeIndex = -1;
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	private TextView popupText = null;
	public static String address = "天安门";
	public static String factory_name = "天安门";
	public static double xPosition = 0;
	public static double yPosition = 0;
	public TextView tv, startPositionTV, endPositionTV;
	public LatLng kLocation = null;
	public String PID = null;
	public Location mLocation = null;
	List<Map<String, Object>> sealRecords = new ArrayList<Map<String, Object>>();
	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;
	// 搜索相关
	RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	PoiSearch pSearch;	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取地图控件引用
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.seal_map_navigation);
		// CharSequence titleLable = "路线规划功能";
		// setTitle(titleLable);
		// 初始化地图
		startPositionTV = (TextView) findViewById(R.id.startPositionTV);
		endPositionTV = (TextView) findViewById(R.id.endPositionTV);
		Intent intent = getIntent();
		String addressParam = intent
				.getStringExtra("com.kfx.service.seal.record.factory.address");
		String factory_nameParam = intent
				.getStringExtra("demo.android.factory.name");
		String x = intent.getStringExtra("demo.android.factory.xPosition");
		String y = intent.getStringExtra("demo.android.factory.yPosition");
		this.address = addressParam;
		this.factory_name = factory_nameParam;
		this.xPosition = Double.parseDouble(x);
		this.yPosition = Double.parseDouble(y);
		startPositionTV = (TextView) findViewById(R.id.startPositionTV);
		endPositionTV = (TextView) findViewById(R.id.endPositionTV);
		setLocation();
		if(mLocation == null){
			startPositionTV.setText("时代财富天地");
		}
		endPositionTV.setText(factory_nameParam);
		mMapView = (MapView) findViewById(R.id.map_navigation);
		mBaidumap = mMapView.getMap();

		mBtnPre = (Button) findViewById(R.id.pre);
		mBtnNext = (Button) findViewById(R.id.next);
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		pSearch = PoiSearch.newInstance();
		pSearch.setOnGetPoiSearchResultListener(this);
		sealMapNavigationBackBtn = (ImageView) findViewById(R.id.sealNavigationMapBackBtn);
		sealMapNavigationBackBtn
				.setOnClickListener(sealMapNavigationBackListener);
		SearchButtonProcess();
	}

	// 关闭界面
	OnClickListener sealMapNavigationBackListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Intent intent=new Intent();
			// intent.setClass(SealUserRegisterActivity.this,
			// SealUserLoginActivity.class);
			// startActivity(intent);
			finish();
		}
	};

	public void SearchButtonProcess() {
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBaidumap.clear();
		// Toast.makeText(MapNavigationActivity.this,
		// "---:"+this.address,Toast.LENGTH_LONG).show();
		// 处理搜索按钮响应
		// EditText editSt = (EditText) findViewById(R.id.sealFullNameValText);
		// EditText editEn = (EditText) findViewById(R.id.sealCodeValText);
		// 设置起终点信息，对于tranist search 来说，城市名无意义,116.2941300000
		System.out.println(this.xPosition);
		System.out.println(this.yPosition);
		PlanNode stNode=null;// a
		// PlanNode stNode = PlanNode.withLocation(new
		// LatLng(116.2941300000,39.8380200000));//b
		// PlanNode enNode = PlanNode.withLocation(new
		// LatLng(this.xPosition,this.yPosition));//b
		PlanNode enNode =null;// c
		// PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京",
		// "北京新路发广告有限公司");//d
		// ac bd bc
		// 实际使用中请对起点终点城市进行正确的设定
		startPositionTV = (TextView) findViewById(R.id.startPositionTV);
		endPositionTV = (TextView) findViewById(R.id.endPositionTV);
		if(startPositionTV.getText() != null&&startPositionTV.getText().toString().trim().length() > 0){
			stNode = PlanNode.withCityNameAndPlaceName("北京", startPositionTV.getText().toString().trim());
		}else{
			if(mLocation != null){
				stNode=PlanNode.withLocation(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
			}else{
				Toast.makeText(MapNavigationActivity.this, "无法定位当前位置，请输入当前位置", Toast.LENGTH_LONG).show();
				return;
			}		
		}
		
		if(endPositionTV.getText() != null&&endPositionTV.getText().toString().trim().length() > 0){
			enNode = PlanNode.withCityNameAndPlaceName("北京", endPositionTV.getText().toString().trim());
		}else{
			enNode = PlanNode.withCityNameAndPlaceName("北京", this.factory_name);	
		}
		

		mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
				enNode));
	}

	public void setLocation(){
		String msg = "";		
		ListMapMemoryCache MemoryCacheForFile = new ListMapMemoryCache(this);
		ListMapFileCache fileCacheForFile = new ListMapFileCache();
		SealService service = new SealServiceImpl();
		ListMapService service1 = new ListMapServiceImpl();
		sealRecords = service.getSealRecords("com.kfx.azs.bean.seal.Factory",
				MemoryCacheForFile, fileCacheForFile, "listforSealRecorss",
				new Date());
		if (GPSUtil.gpsIsOpen(MapNavigationActivity.this)) {
			mLocation = GPSUtil.getLocation(MapNavigationActivity.this);
			if (mLocation != null) {
				service1.saveStrForFile(mLocation.getLongitude() + ","
						+ mLocation.getLatitude(), fileCacheForFile,
						"seal_record_location");
				msg = "维度:" + mLocation.getLatitude() + "\n经度:"
						+ mLocation.getLongitude();
			} else {
				msg = "GPS开启,但无法定位到当前位置";
				service1.saveStrForFile("0,0", fileCacheForFile,
						"seal_record_location");
			}
		} else {
			msg = "GPS未开启";
			service1.saveStrForFile("0,0", fileCacheForFile,
					"seal_record_location");
		}
		Toast toast = Toast.makeText(MapNavigationActivity.this, msg,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void getNearbyFactory(View v) {
		
		mBaidumap.clear();
		Toast.makeText(MapNavigationActivity.this, "附近的刻制社", Toast.LENGTH_LONG)
				.show();  
		setLocation();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (mLocation != null) {
			double latitude = mLocation.getLatitude();
			double longitude = mLocation.getLongitude();
			mapList = Distance.getNearByDistanceFactory(sealRecords,
					latitude, longitude,
					Properties.factory_distance);
			setOverlayOptions("当前位置","",latitude,longitude);			
		} else {
			startPositionTV = (TextView) findViewById(R.id.startPositionTV);
			endPositionTV = (TextView) findViewById(R.id.endPositionTV);

			if (startPositionTV.getText() != null
					&& startPositionTV.getText().toString().trim().length() > 0) {
				String str = startPositionTV.getText().toString().trim();
				pSearch.searchInCity((new PoiCitySearchOption()).city("北京")
						.keyword(str));
				if (kLocation != null) {
					setOverlayOptions("当前位置:\n",startPositionTV.getText().toString(),kLocation.latitude, kLocation.longitude);	
					mapList = Distance.getNearByDistanceFactory(sealRecords,
							kLocation.latitude, kLocation.longitude,
							Properties.factory_distance);
				} else {
					Toast.makeText(MapNavigationActivity.this,
							"你输入的位置无法查询，请确定后再次输入 " + kLocation + PID,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(MapNavigationActivity.this,
						"无法定位当前位置，请你输入定位当前位置", Toast.LENGTH_LONG).show();
			}
		}
		if (mapList.size() > 0) {
			for (Map<String, Object> map : mapList) {
				factory_name = (String) map.get("name");
				address = (String) map.get("address");
				String factory_x = map.get("x").toString();
				String factory_y = map.get("y").toString();
				// 经度
				double factory_x1 = Double.parseDouble(factory_x);
				// 维度
				double factory_y1 = Double.parseDouble(factory_y);
				setOverlayOptions(factory_name,address,factory_y1,factory_x1);
			}
		}
	}

	public void setOverlayOptions(String factory_name,String address,double factory_x1,double factory_y1){
		tv = new TextView(MapNavigationActivity.this);
		tv.setText("刻制社:" + factory_name + "\n地址:" + address + "\n");
		tv.setMaxWidth(250);
		tv.setTextSize(10);
		tv.setTextColor(Color.parseColor("#202020"));
		tv.setBackgroundResource(R.drawable.position10);
		BitmapDescriptor descriptor = BitmapDescriptorFactory
				.fromView(tv);
		OverlayOptions options = new MarkerOptions().icon(descriptor)
				.title("abc")
				.position(new LatLng(factory_x1, factory_y1)).rotate(0)
				.draggable(true).zIndex(5).visible(true);
		mBaidumap.addOverlay(options);// map.MyLocationConfiguration.LocationMode
	}
	/**
	 * 发起路线规划搜索示例
	 * 
	 * @param v
	 */
	public void SearchButtonProcess(View v) {
		// 重置浏览节点的路线数据
		route = null;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		mBaidumap.clear();
		// Toast.makeText(MapNavigationActivity.this,
		// "---:"+this.address,Toast.LENGTH_LONG).show();
		// 处理搜索按钮响应
		// 设置起终点信息，对于tranist search 来说，城市名无意义,116.2941300000
		// PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京",
		// "时代财富天地");//a		
		PlanNode stNode=null;
		PlanNode enNode=null;
		startPositionTV = (TextView) findViewById(R.id.startPositionTV);
		endPositionTV = (TextView) findViewById(R.id.endPositionTV);
		if(startPositionTV.getText() != null&&startPositionTV.getText().toString().trim().length() > 0){
			stNode = PlanNode.withCityNameAndPlaceName("北京", startPositionTV.getText().toString().trim());
		}else{
			setLocation();
			if(mLocation != null){
				stNode=PlanNode.withLocation(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
			}else{
				Toast.makeText(MapNavigationActivity.this, "无法定位当前位置，请输入当前位置", Toast.LENGTH_LONG).show();
				return;
			}		
		}
		
		if(endPositionTV.getText() != null&&endPositionTV.getText().toString().trim().length() > 0){
			enNode = PlanNode.withCityNameAndPlaceName("北京", endPositionTV.getText().toString().trim());
		}else{
			enNode = PlanNode.withCityNameAndPlaceName("北京", this.factory_name);	
		}
		
		//PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", this.factory_name);//c
		//PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "新路发广告公司");// d
		// ac bd bc
		// 实际使用中请对起点终点城市进行正确的设定
		if (v.getId() == R.id.drive) {
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode)
					.to(enNode));
		} else if (v.getId() == R.id.transit) {
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode)
					.city("北京").to(enNode));
		} else if (v.getId() == R.id.walk) {
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode)
					.to(enNode));
		}
	}

	
	/**
	 * 节点浏览示例
	 * 
	 * @param v
	 */
	public void nodeClick(View v) {
		if (route == null || route.getAllStep() == null) {
			return;
		}
		if (nodeIndex == -1 && v.getId() == R.id.pre) {
			return;
		}
		// 设置节点索引
		if (v.getId() == R.id.next) {
			if (nodeIndex < route.getAllStep().size() - 1) {
				nodeIndex++;
			} else {
				return;
			}
		} else if (v.getId() == R.id.pre) {
			if (nodeIndex > 0) {
				nodeIndex--;
			} else {
				return;
			}
		}
		// 获取节结果信息
		LatLng nodeLocation = null;
		String nodeTitle = null;
		Object step = route.getAllStep().get(nodeIndex);
		if (step instanceof DrivingRouteLine.DrivingStep) {
			nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
		} else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
		} else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrace()
					.getLocation();
			nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
		}

		if (nodeLocation == null || nodeTitle == null) {
			return;
		}
		// 移动节点至中心
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		// show popup
		popupText = new TextView(MapNavigationActivity.this);
		popupText.setBackgroundResource(R.drawable.popup);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		BitmapDescriptor btv = BitmapDescriptorFactory.fromView(popupText);
		mBaidumap.showInfoWindow(new InfoWindow(btv, nodeLocation, null));

	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			((Button) v).setText("自定义起终点图标");
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();

		} else {
			((Button) v).setText("系统起终点图标");
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();

		}
		useDefaultIcon = !useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapNavigationActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {

		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapNavigationActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapNavigationActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			mBtnPre.setVisibility(View.VISIBLE);
			mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.pisition1);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.position7);
			}
			return null;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.pisition1);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.position7);
			}
			return null;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.pisition1);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory
						.fromResource(R.drawable.position7);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mSearch.destroy();
		pSearch.destroy();
		mMapView.onDestroy();
		super.onDestroy();
	}

	OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
		@Override
		public void onGetPoiResult(PoiResult poiResult) {
			// TODO Auto-generated method stub
			String poiname = poiResult.getAllPoi().get(0).name;
			String poiadd = poiResult.getAllPoi().get(0).address;
			String idString = poiResult.getAllPoi().get(0).uid;
			kLocation = poiResult.getAllPoi().get(0).location;			
		}

		@Override
		public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
		}
	};
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapNavigationActivity.this, "抱歉，未找到该地点",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			String poiname = result.getAllPoi().get(0).name;
			String poiadd = result.getAllPoi().get(0).address;
			String idString = result.getAllPoi().get(0).uid;
			kLocation = result.getAllPoi().get(0).location;	
		}		
	}	
}