package com.kfx.android.util.app;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	   private static MyApplication application;  
	    public MyApplication(){  
	    }  
	    public static MyApplication getInstance(){  
	        if(null == application){  
	            application = new MyApplication();  
	        }  
	        return application;  
	    }  
	    public void addActivity(Activity activity){  
	        activityList.add(activity);  
	    } 
	    public void removeActivity(Activity activity){  
	        activityList.remove(activity);  
	    }  
	    public void exit(){  
	        for(Activity a : activityList){  
	            a.finish();  
	        }	       
	    } 

}