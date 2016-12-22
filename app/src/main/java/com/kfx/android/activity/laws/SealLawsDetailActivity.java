package com.kfx.android.activity.laws;

import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SealLawsDetailActivity extends Activity {
	private TextView sealLawsTitle;
	private ImageView iv;
	private TextView sealLawsContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laws_seal_detail);
		//设置标题
		Intent intent=getIntent();
		String title=intent.getStringExtra("android.laws.detail.title");
		String filePath=intent.getStringExtra("android.laws.datail.path");
		LawsService la=new LawsServiceImpl();
		String content=la.getLawsFileToString(filePath);
		
		sealLawsTitle=(TextView) findViewById(R.id.laws_seal_detail);
		sealLawsContent=(TextView) findViewById(R.id.sealLawsContent);
		
		sealLawsTitle.setText(title);
		sealLawsContent.setText(content);
		//设置返回按钮
		iv=(ImageView) findViewById(R.id.sealLawsDetailBackBtn);
		iv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SealLawsDetailActivity.this.finish();
			}
		});
		
	}
}