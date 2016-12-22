package com.kfx.android.activity.laws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SealLawsMainActivity extends Activity {
	private ImageView iv;
	List<Map<String,Object>> sealLawList=new ArrayList<Map<String,Object>>();
	private ListView listView;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laws_seal_main);
		//点击返回按钮
		iv=(ImageView) findViewById(R.id.sealLawsMainBackBtn);
		iv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SealLawsMainActivity.this.finish();
			}
		});
		
		//设置政策法规listview
		LawsService la=new LawsServiceImpl();
		sealLawList=la.getSealLaws("com/kfx/android/xml/law/seal/seal_file_title.xml");
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,
				sealLawList, R.layout.list_laws_item, new String[] { "name","publish_group","publish_time"
						}, new int[] { R.id.list_laws_title ,R.id.law_publish_office,R.id.law_publish_time});
		listView=(ListView)findViewById(R.id.lawsSealListView);
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				System.out.println("第arg2:"+arg2+"item");
				for(int i=0;i<sealLawList.size();i++){
					System.out.println("第 i:"+i+"item");
					if (i == arg2) {
						//选中后要变灰色的部分
						 ((ImageView)listView.getChildAt(i).findViewById(R.id.seal_law_image)).setImageResource(R.drawable.law_on);
					}else{
						//初始化后的样式
						((ImageView)listView.getChildAt(i).findViewById(R.id.seal_law_image)).setImageResource(R.drawable.law_go);
					}
					
				}
				
				Intent intent=new Intent();				
				intent.putExtra("android.laws.detail.title",sealLawList.get(arg2).get("name").toString());
				intent.putExtra("android.laws.datail.path",sealLawList.get(arg2).get("file_name").toString());
				intent.setClass(SealLawsMainActivity.this,SealLawsDetailActivity.class);
				startActivity(intent);				
			}			
		});
	}
}