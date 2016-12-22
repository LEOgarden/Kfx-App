package com.kfx.android.activity.home.setup.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.activity.laws.SealLawsDetailActivity;
import com.kfx.android.activity.laws.SealLawsMainActivity;
import com.kfx.android.util.service.adapter.HelpService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class FeedbackActivity extends Activity {
	private ImageView feedback_back;
	List<Map<String,Object>> helpList=new ArrayList<Map<String,Object>>();
	private ListView listView;	
	@Override
	protected void onCreate(Bundle savedInstanceStates) {
		super.onCreate(savedInstanceStates);
		setContentView(R.layout.feedback);
		feedback_back = (ImageView) findViewById(R.id.feedback_back);
		HelpService service = new HelpService();
		helpList=service.getHelps("com/kfx/android/xml/help/help_feedback.xml");
		feedback_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				FeedbackActivity.this.overridePendingTransition(
						R.animator.in_from_right, R.animator.out_to_left);
			}
		});
		
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,
				helpList, R.layout.list_feedback_item, new String[] { "question"}, new int[] { R.id.list_feedback_title});
		listView=(ListView)findViewById(R.id.feedbackListView);
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {				
				Intent intent=new Intent();				
				intent.putExtra("android.laws.detail.question",helpList.get(arg2).get("question").toString());
				intent.putExtra("android.laws.datail.answer",helpList.get(arg2).get("answer").toString());
				intent.setClass(FeedbackActivity.this,FeedbackDetailActivity.class);
				startActivity(intent);				
			}			
		});

	}
}
