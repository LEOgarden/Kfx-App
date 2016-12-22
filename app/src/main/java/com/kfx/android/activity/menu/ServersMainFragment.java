package com.kfx.android.activity.menu;

import com.kfx.android.R;
import com.kfx.android.activity.menu.TabFragmentActivity.MyOnTouchListener;
import com.kfx.android.activity.service.SealServiceMainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ServersMainFragment extends Fragment implements OnGestureListener, OnDoubleTapListener{
	private View rootView;
	private Button sealServiceBtn;
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
			rootView = inflater.inflate(R.layout.main_service, null);//
			mGestureDetector = new GestureDetector(this);
			((TabFragmentActivity)getActivity()).registerMyOnTouchListener(serviceOnTouchListener);
			sealServiceBtn= (Button) rootView.findViewById(R.id.sealServiceBtn);
			sealServiceBtn.setOnClickListener(sealListener);
			
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	OnClickListener sealListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(getActivity(), SealServiceMainActivity.class);
			startActivity(intent);
		}
	};

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
	MyOnTouchListener serviceOnTouchListener = new MyOnTouchListener() {
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
				if ( downX-  upX> 100 ||downX- upX < -100||downY-  upY> 100 ||downY-  upY < -100){
					if(distanceX>distanceY){
						if(downX-  upX> 100){	

							System.out.println("哈哈 向左滑动了");							
						}else if(downX- upX < -100){

							System.out.println("哈哈 向右滑动了");
						}
					}else{
						System.out.println("哈哈 上下滑动");
						if(downY-  upY < -100){
							
							System.out.println("哈哈 向下滑动了");
						}else if(downY-  upY> 100){
							
							System.out.println("哈哈 向上滑动了");
						}
					}
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
				return mGestureDetector.onTouchEvent(ev);				
			}
			return false;			
		}	
	};

}