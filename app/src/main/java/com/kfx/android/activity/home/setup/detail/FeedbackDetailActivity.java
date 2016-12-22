package com.kfx.android.activity.home.setup.detail;

import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedbackDetailActivity extends Activity {
	private TextView feedback_seal_detail;
	private ImageView feedback_Detail_BackBtn;
	private TextView feedback_Content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_seal_detail);
		//设置标题
		Intent intent=getIntent();
		String question=intent.getStringExtra("android.laws.detail.question");
		String answer=intent.getStringExtra("android.laws.datail.answer");
		
		
		feedback_seal_detail=(TextView) findViewById(R.id.feedback_seal_detail);
		feedback_Content=(TextView) findViewById(R.id.feedback_Content);
		
		feedback_seal_detail.setText(question);
		feedback_Content.setText(answer);
		//设置返回按钮
		feedback_Detail_BackBtn=(ImageView) findViewById(R.id.feedback_Detail_BackBtn);
		feedback_Detail_BackBtn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				FeedbackDetailActivity.this.finish();
			}
		});
		
	}
}