package com.kfx.android.activity.home;

import com.kfx.android.R;
import com.kfx.android.util.img.ImageUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	private ImageView iv;
	private TextView news_title;
	private TextView create_date;
	private TextView news_author;
	private TextView news_content;
	private ImageView news_image;
	private TextView announce_title;
	private TextView announce_client;
	private TextView announce_area;
	private TextView announce_sender;
	private TextView announce_time;
	private TextView notice_title;
	private TextView notice_client;
	private TextView notice_area;
	private TextView notice_sender;
	private TextView notice_time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=getIntent();
		String flg=intent.getStringExtra("android.news.detail.flg");
		if(flg.equals("News")){
			setContentView(R.layout.news_detail_msg);
			//获取控件
			news_title=(TextView) findViewById(R.id.news_title);
			news_author=(TextView) findViewById(R.id.news_author);
			create_date=(TextView) findViewById(R.id.create_date);
			news_content=(TextView) findViewById(R.id.news_content);
			news_image=(ImageView) findViewById(R.id.news_image);
			news_title.setText(intent.getStringExtra("android.news.detail.title"));
			news_author.setText("冰雪独厚");
			create_date.setText(intent.getStringExtra("android.news.detail.createtime"));
			news_content.setText("    "+intent.getStringExtra("android.news.detail.text"));
			String path=intent.getStringExtra("android.news.detail.imagePath");
			Bitmap bm=ImageUtil.getBitmapFromServer(path, "big");
			news_image.setImageBitmap(bm);
			

		}else if(flg.equals("Announcement")){
			setContentView(R.layout.detail_announce_msg);
			announce_title=(TextView) findViewById(R.id.announce_title);
			announce_client=(TextView) findViewById(R.id.announce_client);
			announce_area=(TextView) findViewById(R.id.announce_area);
			announce_sender=(TextView) findViewById(R.id.announce_sender);
			announce_time=(TextView) findViewById(R.id.announce_time);
			announce_title.setText(intent.getStringExtra("android.announcement.detail.title"));
			announce_client.setText(intent.getStringExtra("android.announcement.detail.target"));
			announce_area.setText(intent.getStringExtra("android.announcement.detail.content"));
			announce_sender.setText(intent.getStringExtra("android.announcement.detail.addressor"));
			announce_time.setText(intent.getStringExtra("android.announcement.detail.createtime"));			
		}else if(flg.equals("Notice")){
			setContentView(R.layout.detail_notice_msg);
			notice_title=(TextView) findViewById(R.id.notice_title);
			notice_client=(TextView) findViewById(R.id.notice_client);
			notice_area=(TextView) findViewById(R.id.notice_area);
			notice_sender=(TextView) findViewById(R.id.notice_sender);
			notice_time=(TextView) findViewById(R.id.notice_receive);
			notice_title.setText(intent.getStringExtra("android.announcement.detail.title"));
			notice_client.setText(intent.getStringExtra("android.announcement.detail.target"));
			notice_area.setText(intent.getStringExtra("android.announcement.detail.content"));
			notice_sender.setText(intent.getStringExtra("android.announcement.detail.addressor"));
			notice_time.setText(intent.getStringExtra("android.announcement.detail.createtime"));	
		}	
		
		iv=(ImageView) findViewById(R.id.detailBackBtn);
		iv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				DetailActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			super.onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return super.onKeyLongPress(keyCode, event);
		
	}
}