package com.kfx.android.activity.menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.activity.home.DetailActivity;
import com.kfx.android.activity.menu.TabFragmentActivity.MyOnTouchListener;
import com.kfx.android.util.cache.ImageFileCache;
import com.kfx.android.util.cache.ImageMemoryCache;
import com.kfx.android.util.cache.ListMapFileCache;
import com.kfx.android.util.cache.ListMapMemoryCache;
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
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class NewsMainFragment extends Fragment  implements OnGestureListener, OnDoubleTapListener{
	private View rootView;
	private TabHost tabHost;
	public static List<Map<String,Object>> listNewsForNews=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForNews1=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForAnnounce=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForAnnounce1=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForNotice=new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> listNewsForNotice1=new ArrayList<Map<String,Object>>();
	private ListView newsListView;
	private ListView noticeListView;
	private ListView informListView;
	private ImageMemoryCache memoryCache;  
    private ImageFileCache fileCache; 	   
	private ListMapMemoryCache MemoryCacheForFile;  
    private ListMapFileCache fileCacheForFile; 	
    private  GestureDetector mGestureDetector;
	private static float downX;
	private static float downY;
	private static float upX;
	private static float upY;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.main_news, null);
			
			memoryCache=new ImageMemoryCache(getActivity());  
	        fileCache=new ImageFileCache(); 
	    	MemoryCacheForFile=new ListMapMemoryCache(getActivity());  
	    	fileCacheForFile=new ListMapFileCache(); 
	    	
	    	mGestureDetector = new GestureDetector(this);
	    	((TabFragmentActivity)getActivity()).registerMyOnTouchListener(newOnTouchListener);	
			
			tabHost = (TabHost)rootView.findViewById(android.R.id.tabhost);
	        tabHost.setup();
	        TabSpec tabSpec=tabHost.newTabSpec("page1");
	        tabSpec.setIndicator("新闻");
	        tabSpec.setContent(R.id.page1);
	        tabHost.addTab(tabSpec);	        
	        tabSpec=tabHost.newTabSpec("page2");
	        tabSpec.setIndicator("公告");
	        tabSpec.setContent(R.id.page2);
	        tabHost.addTab(tabSpec);
	        tabSpec=tabHost.newTabSpec("page3");
	        tabSpec.setIndicator("通知");
	        tabSpec.setContent(R.id.page3);
	        tabHost.addTab(tabSpec);	        
	        updateTabStyle(tabHost);
	        //设置tabHost标题
	        tabHost.setOnTabChangedListener(new OnTabChangeListener() {				
				@Override
				public void onTabChanged(String tabId) {
					int index=0;
					if(tabId.equals("page1")){
						index=0;
					}else if(tabId.equals("page2")){
						index=1;
					}else if(tabId.equals("page3")){
						index=2;
					}
					for(int i=0;i<3;i++){
						RelativeLayout tabView1 = (RelativeLayout) tabHost.getTabWidget().getChildAt(i);
						TextView text1 = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); 
						if(index==i){ 
							tabView1.setBackgroundResource(R.drawable.bg_gonggao_1);
							text1.setTextColor(0xFF0083F3);
						}else{
			                tabView1.setBackgroundResource(R.drawable.bg_gonggao1);   
			                text1.setTextColor(0xFF808080);
						}
					}
		
				}				
			});
	        
	        NewsService service=new NewsServiceImpl();
	        ListMapService service1=new ListMapServiceImpl();
	        //新闻
	        listNewsForNews=service.getNewsForListMap("com.kfx.azs.bean.New", MemoryCacheForFile, fileCacheForFile, "listforNews", new Date());
	        listNewsForNews1=MapUtil.getListMapAddForMatTime(listNewsForNews);
	        listNewsForNews1=MapUtil.getListMapAddImageUrl(listNewsForNews1);
	        listNewsForNews1=MapUtil.getListMapAddImage(listNewsForNews1, memoryCache, fileCache,R.drawable.news_default_img);
	        
			String newDate=TimeUtil.getLatestTime(listNewsForNews);
			service1.saveStrForFile(newDate, fileCacheForFile, "newsForLatestTime");
	        //公告
	        listNewsForAnnounce=service.getNewsForListMap("com.kfx.azs.bean.Mail", MemoryCacheForFile, fileCacheForFile, "listforAnnounces", new Date());
	        listNewsForAnnounce1=MapUtil.getListMapAddForMatTime(listNewsForAnnounce);
			
	        String newDateForAnnounce=TimeUtil.getLatestTime(listNewsForAnnounce);
			service1.saveStrForFile(newDateForAnnounce, fileCacheForFile, "announceForLatestTime");
	        //通知
			listNewsForNotice=service.getNewsForListMap("com.kfx.azs.bean.Mail", MemoryCacheForFile, fileCacheForFile, "listforNotices", new Date());
	        listNewsForNotice1=MapUtil.getListMapAddForMatTime(listNewsForNotice);
			
	        String newDateForNotice=TimeUtil.getLatestTime(listNewsForNotice);
			service1.saveStrForFile(newDateForNotice, fileCacheForFile, "noticeForLatestTime");
	        
	        listNewsForNews1=ListUtil.sortNewsBycreateTime(listNewsForNews1);
	        listNewsForAnnounce1=ListUtil.sortNewsBycreateTime(listNewsForAnnounce1);
	        listNewsForNotice1=ListUtil.sortNewsBycreateTime(listNewsForNotice1);
	        //设置新闻标题
	        setNewsForListView(listNewsForNews1);
			//设置公告标题
	        setAnnounceForListView(listNewsForAnnounce1);
			//设置通知标题
	        setNoticeForListView(listNewsForNotice1);			
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	
	private void updateTabStyle(TabHost mTabHost) {		
		 final TabWidget tabWidget = mTabHost.getTabWidget();   
	        for (int i = 0; i < tabWidget.getChildCount(); i++) {   
	            RelativeLayout tabView = (RelativeLayout) mTabHost.getTabWidget().getChildAt(i);	               
	            TextView text = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);   
	            text.setTextSize(14);               
	            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) text.getLayoutParams();   
	            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
	            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;   
	            text.setLayoutParams(params);   
	            text.setGravity(Gravity.CENTER);	               
	           if (mTabHost.getCurrentTab() == i) {   	               
	                tabView.setBackgroundResource(R.drawable.bg_gonggao_1);  // 选中     
	                text.setTextColor(0xFF0083F3);
	                text.setTextSize(18);
	            } else {   	               
	                tabView.setBackgroundResource(R.drawable.bg_gonggao1);   // 未选中    
	                text.setTextColor(0xFF808080);
	                text.setTextSize(18);
	            }	            
	        }
	}
	public void setNewsForListView(final List<Map<String, Object>> listNewsForNews){		
		SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listNewsForNews,R.layout.list_news_item,new String [] {"title","image","time"},new int[] {R.id.list_new_title,R.id.list_news_image,R.id.list_news_time});
		newsListView=(ListView) rootView.findViewById(R.id.newsForNewsListView);
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
		newsListView.setAdapter(simpleAdapter);
		newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				Intent intent=new Intent();
				intent.putExtra("android.news.detail.flg", "News");
				intent.putExtra("android.news.detail.title", listNewsForNews.get(arg2).get("title").toString());
				intent.putExtra("android.news.detail.text", listNewsForNews.get(arg2).get("text").toString());
				intent.putExtra("android.news.detail.createtime", listNewsForNews.get(arg2).get("time").toString());
				intent.putExtra("android.news.detail.imagePath",listNewsForNews.get(arg2).get("photoPath").toString());					
				intent.setClass(getActivity(), DetailActivity.class);
				startActivity(intent);				
			}
		});	
		newsListView.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				System.out.println("****************+getLastVisiblePosition"+view.getLastVisiblePosition());
				System.out.println("****************+scrollState"+scrollState);
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {  
                       System.out.println("****************+到低端了 要加载数据了"+view.getCount());
                       setNewMainListView();
                    }  
                } 

			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
	}

	public void setAnnounceForListView(final List<Map<String, Object>> listNewsForAnnounce){		
		
		noticeListView=(ListView) rootView.findViewById(R.id.NoticeForNewsListView);
		SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listNewsForAnnounce,R.layout.list_announce_item,new String [] {"title","addressor","time"},new int[] {R.id.list_announce_title,R.id.list_announce_author,R.id.list_announce_time});
		noticeListView.setAdapter(simpleAdapter);
		noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				Intent intent=new Intent();
				intent.putExtra("android.news.detail.flg", "Announcement");
				intent.putExtra("android.announcement.detail.title", listNewsForAnnounce.get(arg2).get("title").toString());
				intent.putExtra("android.announcement.detail.target", listNewsForAnnounce.get(arg2).get("target").toString());
				intent.putExtra("android.announcement.detail.content", listNewsForAnnounce.get(arg2).get("content").toString());
				intent.putExtra("android.announcement.detail.addressor", listNewsForAnnounce.get(arg2).get("addressor").toString());
				intent.putExtra("android.announcement.detail.createtime", listNewsForAnnounce.get(arg2).get("time").toString());
				intent.setClass(getActivity(), DetailActivity.class);
				startActivity(intent);					
			}
		});	
		noticeListView.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				System.out.println("****************+getLastVisiblePosition"+view.getLastVisiblePosition());
				System.out.println("****************+scrollState"+scrollState);
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {  
                       System.out.println("****************+到低端了 要加载数据了"+view.getCount());
                       setNewMainListView();
                    }  
                } 

			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});		
	}
	
	public void setNoticeForListView(final List<Map<String, Object>> listNewsForNotice){
		
		informListView=(ListView) rootView.findViewById(R.id.informForNewsListView);
		SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listNewsForNotice,R.layout.list_notice_item,new String [] {"title","time"},new int[] {R.id.list_notice_title,R.id.list_notice_time});
		informListView.setAdapter(simpleAdapter);
		informListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				Intent intent=new Intent();
				intent.putExtra("android.news.detail.flg", "Notice");
				intent.putExtra("android.announcement.detail.title", listNewsForNotice.get(arg2).get("title").toString());
				intent.putExtra("android.announcement.detail.target", listNewsForNotice.get(arg2).get("target").toString());
				intent.putExtra("android.announcement.detail.content", listNewsForNotice.get(arg2).get("content").toString());
				intent.putExtra("android.announcement.detail.addressor", listNewsForNotice.get(arg2).get("addressor").toString());
				intent.putExtra("android.announcement.detail.createtime", listNewsForNotice.get(arg2).get("time").toString());
				intent.setClass(getActivity(), DetailActivity.class);
				startActivity(intent);					
			}
		});	
		informListView.setOnScrollListener(new OnScrollListener() {			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				System.out.println("****************+getLastVisiblePosition"+view.getLastVisiblePosition());
				System.out.println("****************+scrollState"+scrollState);
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {  
                       System.out.println("****************+到低端了 要加载数据了"+view.getCount());
                       setNewMainListView();
                    }  
                } 

			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
	}


	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	MyOnTouchListener newOnTouchListener = new MyOnTouchListener() {
		@Override
		public boolean onTouch(MotionEvent ev) {
			System.out.println("触发动作:" + ev.getAction());
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				downX=ev.getX();
				downY=ev.getY();
				return true;
			}else if((ev.getAction() == MotionEvent.ACTION_UP)){
				upX=ev.getX();
				upY=ev.getY();
				float distanceX=Math.abs(downX-  upX);
				float distanceY=Math.abs(downY-  upY);
				System.out.println("哈哈 左右滑动");
				//获取当前host的page值
				int i = tabHost.getCurrentTab(); 
				if ( downX-  upX> 100 ||downX- upX < -100||downY-  upY> 100 ||downY-  upY < -100){
					if(distanceX>distanceY){
						 System.out.println("tabhost第"+i+"个");
						if(downX-  upX> 100){	
							if(i==0){
								tabHost.setCurrentTab(1);								
							}else if(i==1){
								tabHost.setCurrentTab(2);
							}
							System.out.println("哈哈 向左滑动了");							
						}else if(downX- upX < -100){
							if(i==1){
								tabHost.setCurrentTab(0);								
							}else if(i==2){
								tabHost.setCurrentTab(1);
							}
							System.out.println("哈哈 向右滑动了");
						}
					}else{
						System.out.println("哈哈 上下滑动");
						if(downY-  upY < -100){
							//setNewMainListView();
							System.out.println("哈哈 向下滑动了");
						}else if(downY-  upY> 100){
							//setNewMainListView();
							System.out.println("哈哈 向上滑动了");
						}
					}				
				}
				return mGestureDetector.onTouchEvent(ev);				
			}
			return false;			
		}	
	};
	public void setNewMainListView(){
		int i = tabHost.getCurrentTab(); 
		NewsService service=new NewsServiceImpl();
		ListMapService service1=new ListMapServiceImpl();
		memoryCache=new ImageMemoryCache(getActivity());  
        fileCache=new ImageFileCache(); 
    	MemoryCacheForFile=new ListMapMemoryCache(getActivity());  
    	fileCacheForFile=new ListMapFileCache();
    	String where="";
    	String latestTime=TimeUtil.DateToStr(new Date());
    	List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		if(i==0){			
			result.clear();
			latestTime=service1.getStrForFile(fileCacheForFile, "newsForLatestTime");
			where="createTime>'"+latestTime+"'";
			result=service.getNewsForLatest(where);
			if(result!=null&&result.size()>0){
				for(Map<String,Object> map:result){
					listNewsForNews.add(map);
				}
				service.saveNewsForLatest(listNewsForNews, MemoryCacheForFile, fileCacheForFile, "listforNews", new Date());
				System.out.println("保存成功");
				result=MapUtil.getListMapAddForMatTime(result);
				result=MapUtil.getListMapAddImageUrl(result);
				result=MapUtil.getListMapAddImage(result, memoryCache, fileCache,R.drawable.news_default_img);
				System.out.println("result成功加载照片");
				for(Map<String,Object> map:result){
					listNewsForNews1.add(map);
				}
				listNewsForNews1=ListUtil.sortNewsBycreateTime(listNewsForNews1);
		        setNewsForListView(listNewsForNews1);
		        System.out.println("listview成功加载数据");
		        Toast.makeText(getActivity(), "新闻已经成功加载了", Toast.LENGTH_LONG).show();	
		        String newDate=TimeUtil.getLatestTime(listNewsForNews);
		        service1.saveStrForFile(newDate, fileCacheForFile, "newsForLatestTime");
		        
			}else{
				Toast.makeText(getActivity(), "新闻已经是最新数据加载", Toast.LENGTH_LONG).show();
			}					
		}else if(i==1){
			result.clear();
			latestTime=service1.getStrForFile(fileCacheForFile, "announceForLatestTime");
			where="createTime>'"+latestTime+"'";
			result=service.getMailsForLatest(where);
			if(result!=null&&result.size()>0){
				for(Map<String,Object> map:result){
					listNewsForAnnounce.add(map);
				}
				service.saveNewsForLatest(listNewsForAnnounce, MemoryCacheForFile, fileCacheForFile, "listforAnnounces", new Date());
				System.out.println("保存成功");
				result=MapUtil.getListMapAddForMatTime(result);
				for(Map<String,Object> map:result){
					listNewsForAnnounce1.add(map);
				}
				listNewsForAnnounce1=ListUtil.sortNewsBycreateTime(listNewsForAnnounce1);
				setAnnounceForListView(listNewsForAnnounce1);
		        String newDateForAnnounce=TimeUtil.getLatestTime(listNewsForAnnounce);
				service1.saveStrForFile(newDateForAnnounce, fileCacheForFile, "announceForLatestTime");
			}else{
				Toast.makeText(getActivity(), "公告已经是最新数据加载", Toast.LENGTH_LONG).show();
			}
		}else if(i==2){
			result.clear();
			latestTime=service1.getStrForFile(fileCacheForFile, "noticeForLatestTime");
			where="createTime>'"+latestTime+"'";
			result=service.getMailsForLatest(where);
			if(result!=null&&result.size()>0){
				for(Map<String,Object> map:result){
					listNewsForNotice.add(map);
				}
				service.saveNewsForLatest(listNewsForNotice, MemoryCacheForFile, fileCacheForFile, "listforNotices", new Date());
				System.out.println("保存成功");
				result=MapUtil.getListMapAddForMatTime(result);
				for(Map<String,Object> map:result){
					listNewsForNotice1.add(map);
				}
				listNewsForNotice1=ListUtil.sortNewsBycreateTime(listNewsForNotice1);
				setNoticeForListView(listNewsForNotice1);
				String newDateForNotice=TimeUtil.getLatestTime(listNewsForNotice);
				service1.saveStrForFile(newDateForNotice, fileCacheForFile, "noticeForLatestTime");
			}else{
				Toast.makeText(getActivity(), "通知已经是最新数据加载", Toast.LENGTH_LONG).show();
			}		
		}
		return;
	}	
}