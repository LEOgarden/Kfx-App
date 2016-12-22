package com.kfx.android.activity.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.activity.home.DetailActivity;
import com.kfx.android.activity.home.SetupForHomeActivity;
import com.kfx.android.activity.menu.TabFragmentActivity.MyOnTouchListener;
import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ImageGetFromHttp;
import com.kfx.android.util.cache.ImageMemoryCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;
import com.kfx.android.util.img.ImageUtil;
import com.kfx.android.util.map.ListUtil;
import com.kfx.android.util.map.MapUtil;
import com.kfx.android.util.service.ListMapService;
import com.kfx.android.util.service.NewsService;
import com.kfx.android.util.service.impl.ListMapServiceImpl;
import com.kfx.android.util.service.impl.NewsServiceImpl;
import com.kfx.android.util.time.TimeUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.ViewFlipper;

public class HomeMainFragment extends Fragment implements OnGestureListener, OnDoubleTapListener{
	private View rootView;
	public static List<Map<String,Object>> listNewsForHome=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForFlipper=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listForNewsTemp=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listForNewsTemp1=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForNoticeTmp=new ArrayList<Map<String,Object>>();
	
	private ViewFlipper viewFlipper;
	private  GestureDetector mGestureDetector;
	private ListView lv;
	private static float downX;
	private static float downY;
	private static float upX;
	private static float upY;
	
	private ImageMemoryCache memoryCache;  
    private ImageFileCache fileCache; 	   
	private ListMapMemoryCache MemoryCacheForFile;  
    private ListMapFileCache fileCacheForFile; 	
    private TextView newest;
    public static Map<String,Object> mapNotice=new HashMap<String, Object>();

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (rootView == null) {	
			//初始化选取
			rootView = inflater.inflate(R.layout.main_home, null);			

			/**
			 * 处理图片的缓存 初始化缓存处理路径
			 */
			memoryCache=new ImageMemoryCache(getActivity());  
	        fileCache=new ImageFileCache(); 
	    	MemoryCacheForFile=new ListMapMemoryCache(getActivity());  
	    	fileCacheForFile=new ListMapFileCache(); 	
	        //获取新闻 文件下载的类
			NewsService service=new NewsServiceImpl();
			ListMapService service1=new ListMapServiceImpl();
			//获得总条数
			/**        最新通知start       */
			listNewsForNoticeTmp=service.getNewsForListMap("com.kfx.azs.bean.Mail", MemoryCacheForFile, fileCacheForFile, "listforNotices", new Date());
			listNewsForNoticeTmp=MapUtil.getListMapAddForMatTime(listNewsForNoticeTmp);
			listNewsForNoticeTmp=ListUtil.sortNewsBycreateTime(listNewsForNoticeTmp);
				
			newest =  (TextView) rootView.findViewById(R.id.newest);
			if(listNewsForNoticeTmp!=null&&listNewsForNoticeTmp.size()>0){
				mapNotice=listNewsForNoticeTmp.get(0);
			}
			if(mapNotice!=null){
				newest.setText(mapNotice.get("title").toString());
			}
			newest.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					if(mapNotice!=null){
						intent.putExtra("android.news.detail.flg", "Notice");
						intent.putExtra("android.announcement.detail.title", mapNotice.get("title").toString());
						intent.putExtra("android.announcement.detail.target", mapNotice.get("target").toString());
						intent.putExtra("android.announcement.detail.content", mapNotice.get("content").toString());
						intent.putExtra("android.announcement.detail.addressor", mapNotice.get("addressor").toString());
						intent.putExtra("android.announcement.detail.createtime", mapNotice.get("time").toString());
					}
					intent.setClass(getActivity(),DetailActivity.class);
					startActivity(intent);						
				}
			});
			/**        最新通知end       */
			/** * 新闻展示 */
			listForNewsTemp=service.getNewsForListMap("com.kfx.azs.bean.New", MemoryCacheForFile, fileCacheForFile, "listforNews", new Date());
			listForNewsTemp1=MapUtil.getListMapAddForMatTime(listForNewsTemp);
			listForNewsTemp1=MapUtil.getListMapAddImageUrl(listForNewsTemp1);
			listForNewsTemp1=MapUtil.getListMapAddImage(listForNewsTemp1, memoryCache, fileCache,R.drawable.news_default_img);
			//获取新闻的最新时间 保存最新时间
			String newDate=TimeUtil.getLatestTime(listForNewsTemp);
			service1.saveStrForFile(newDate, fileCacheForFile, "newsForLatestTime");
			//获得展览狂的listView
			listNewsForFlipper=MapUtil.getHomeForMap(listForNewsTemp1, "flipper");
			listNewsForHome=MapUtil.getHomeForMap(listForNewsTemp1, "list");
			/**新闻展示*/
			/**
			 * 图片展示层面start 
			 */
			//获取图片的ViewFlipper控件 该控件是用来显示图片的
			//取新闻的前三条数据作为图片展示内容
			viewFlipper = (ViewFlipper) rootView.findViewById(R.id.ViewFlipper1);
			mGestureDetector = new GestureDetector(this);
			for(int i=0;i<listNewsForFlipper.size();i++){
				Map<String, Object> map=listNewsForFlipper.get(i);
				String path=map.get("photoPath").toString();
				Bitmap bm=ImageUtil.getBitmapFromServer(path, "big");
				String title=(String)map.get("title");	
				ImageView iv=new ImageView(getActivity());
				iv.setImageBitmap(bm);	
				iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
				iv.setTag(title);
				System.out.println("iv"+iv.getResources()+"----------"+iv);
				viewFlipper.addView(iv);
			}
			for(int i=0;i<viewFlipper.getChildCount();i++){
				View iv=viewFlipper.getChildAt(i);
				System.out.println("iv------------"+iv);
				
			}
			//点击图片
			viewFlipper.setOnClickListener(l);
			//跳转到详细页面 图片手势左右滑动
			((TabFragmentActivity)getActivity()).registerMyOnTouchListener(myOnTouchListener);			
			// 开始图片自动播放，第一个时间为间隔时间，第二个时间未知
	        final Animation left_in = AnimationUtils.loadAnimation(getActivity(), R.animator.push_left_in);
			final Animation left_out = AnimationUtils.loadAnimation(getActivity(),R.animator.push_left_out);
			new CountDownTimer(4000, 10000) {
				@Override
				public void onFinish() {
					viewFlipper.setInAnimation(left_in);
					viewFlipper.setOutAnimation(left_out);
					viewFlipper.showNext();
					start();
				}
				@Override
				public void onTick(long millisUntilFinished) {
				}
			}.start();			
			//listView设置			
			listNewsForHome=ListUtil.sortNewsBycreateTime(listNewsForHome);
			setNewsListView(listNewsForHome);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);

		}
		return rootView;
	}
	

	
	OnClickListener l=new OnClickListener(){
		public void onClick(View arg0){
			Intent intent=new Intent();
			ImageView iv=(ImageView) viewFlipper.getCurrentView();
			Map<String, Object> map=MapUtil.getHomeForDetailMap(listNewsForFlipper, iv.getTag().toString());
			intent.putExtra("android.news.detail.flg", "News");
			intent.putExtra("android.news.detail.title",map.get("title").toString());
			intent.putExtra("android.news.detail.text", map.get("text").toString());
			intent.putExtra("android.news.detail.createtime", map.get("time").toString());
			intent.putExtra("android.news.detail.imagePath",map.get("photoPath").toString());
			intent.setClass(getActivity(), DetailActivity.class);
			startActivity(intent);
		}
	};

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) { 
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {	
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return false;
	}
	MyOnTouchListener myOnTouchListener=new MyOnTouchListener() {		
		@Override
		public boolean onTouch(MotionEvent ev) {
			System.out.println("触发动作:"+ev.getAction());
			if (ev.getAction() == MotionEvent.ACTION_DOWN)  
	        {  
	            downX =ev.getX();  // 取得按下时的坐标x 
	            downY=ev.getY();  
	            return true;  
	        }else if(ev.getAction()==MotionEvent.ACTION_UP){
				upX=ev.getX();
				upY=ev.getY();
				float distanceX=Math.abs(downX-  upX);
				float distanceY=Math.abs(downY-  upY);
	        	if(downY<400&&downY>140){
		        	if ( downX-  upX> 100){ 
		        		System.out.println("向左滑动");
		                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_left_in));
		                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_left_out));
		                viewFlipper.showNext();
		                ev.setAction(MotionEvent.ACTION_CANCEL);
		            }else if ( downX- upX < -100){
		            	System.out.println("向右滑动");
		                viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_right_in));
		                viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.push_right_out));
		                viewFlipper.showPrevious();	  
		                ev.setAction(MotionEvent.ACTION_CANCEL);
		            }else{
		            	System.out.println("点击");	            	
		            }	        		
	        	}else if(downY<=140){
	        		if ( downX-  upX> 100 ||downX- upX < -100||downY-  upY> 100 ||downY-  upY < -100){
	        			if(distanceX<distanceY){	        				
	        				System.out.println("上下滑");	
	        				ev.setAction(MotionEvent.ACTION_CANCEL);
	        			}else{
	        				if(downX - upX < -100){	        					
	        					System.out.println("向右滑"); 
	        					Intent intent=new Intent();
	        					intent.setClass(getActivity(),SetupForHomeActivity.class);
	        					startActivity(intent);
	        					getActivity().overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right); 	    				
	        				}else if(downX - upX > 100){
	        					System.out.println("向左滑"); 
	        					ev.setAction(MotionEvent.ACTION_CANCEL);
	        				}
	        			}
					}
	        	}else{
	        		if ( downX-  upX> 100 ||downX- upX < -100||downY-  upY> 100 ||downY-  upY < -100){
	        			if(distanceX<distanceY){	        				
	        				System.out.println("上下滑");	
	        				
	        			}else{
	        				System.out.println("左右滑");	 
	        				ev.setAction(MotionEvent.ACTION_CANCEL);
	        			}
					}
	        	}
	        	return mGestureDetector.onTouchEvent(ev);
	        }
			return false;
					
		}
	};
	/**
	 * 新闻图片栏图片自动切换
	 */
	
	/**
	 * 首页设置 通知公告listview
	 * @param listItems 传递查询的listview信息
	 */
	public void setNewsListView(final List<Map<String,Object>> listItems){
		//listview部分	
		SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listItems,R.layout.list_news_item,new String [] {"title","image","time"},new int[] {R.id.list_new_title,R.id.list_news_image,R.id.list_news_time});
		lv=(ListView) rootView.findViewById(R.id.homeListView);
		simpleAdapter.setViewBinder(new ViewBinder() {				
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
		lv.setAdapter(simpleAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {				
				Intent intent=new Intent();
				intent.putExtra("android.news.detail.flg", "News");
				intent.putExtra("android.news.detail.title", listItems.get(arg2).get("title").toString());
				intent.putExtra("android.news.detail.text", listItems.get(arg2).get("text").toString());
				intent.putExtra("android.news.detail.createtime", listItems.get(arg2).get("time").toString());
				intent.putExtra("android.news.detail.imagePath",listItems.get(arg2).get("photoPath").toString());
				intent.setClass(getActivity(), DetailActivity.class);
				startActivity(intent);				
			}
		});
		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						loadMoreData();
					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 缓存图片
	 */
	public Bitmap getBitmap(String url) {  
        // 从内存缓存中获取图片  
        Bitmap result = memoryCache.getBitmapFromCache(url);  
        if (result == null) {  
            // 文件缓存中获取  
            result = fileCache.getImage(url);  
            if (result == null) {  
                // 从网络获取  
                result = ImageGetFromHttp.downloadBitmap(url);  
                if (result != null) {  
                    fileCache.saveBitmap(result, url);  
                    memoryCache.addBitmapToCache(url, result);  
                }  
            } else {  
                // 添加到内存缓存  
                memoryCache.addBitmapToCache(url, result);  
            }  
        }  
        return result;  
    } 
	public void loadMoreData(){
		NewsService service=new NewsServiceImpl();
		ListMapService service1=new ListMapServiceImpl();
		memoryCache=new ImageMemoryCache(getActivity());  
        fileCache=new ImageFileCache(); 
    	MemoryCacheForFile=new ListMapMemoryCache(getActivity());  
    	fileCacheForFile=new ListMapFileCache();
    	String where="";
    	String latestTime=TimeUtil.DateToStr(new Date());
    	List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
    	result.clear();
    	latestTime=service1.getStrForFile(fileCacheForFile, "newsForLatestTime");
		where="createTime>'"+latestTime+"'";
		result=service.getNewsForLatest(where);
		if(result!=null&&result.size()>0){
			for(Map<String,Object> map:result){
				listForNewsTemp.add(map);
			}
			service.saveNewsForLatest(listForNewsTemp, MemoryCacheForFile, fileCacheForFile, "listforNews", new Date());
			result=MapUtil.getListMapAddForMatTime(result);
			result=MapUtil.getListMapAddImageUrl(result);
			result=MapUtil.getListMapAddImage(result, memoryCache, fileCache,R.drawable.news_default_img);
			for(Map<String,Object> map:result){
				listNewsForHome.add(map);
			}
			listNewsForHome=ListUtil.sortNewsBycreateTime(listNewsForHome);
			setNewsListView(listNewsForHome);
	        String newDate=TimeUtil.getLatestTime(listForNewsTemp);
	        service1.saveStrForFile(newDate, fileCacheForFile, "newsForLatestTime");
	        Toast.makeText(getActivity(), "新闻已经成功加载了", Toast.LENGTH_LONG).show();	
		}else{
			Toast.makeText(getActivity(), "新闻已经是最新数据加载", Toast.LENGTH_LONG).show();
		}
		return;
	}	
}