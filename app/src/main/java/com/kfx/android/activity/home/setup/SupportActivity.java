package com.kfx.android.activity.home.setup;
import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SupportActivity extends Activity {
	 private ImageView back_support;
	 private TextView support_text;
	  protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.support_msg);
	 support_text=(TextView) findViewById(R.id.support_text);
		
		LawsService la=new LawsServiceImpl();
		String content=la.getLawsFileToString("com/kfx/android/xml/service/seal/support_msg.txt");
	
		support_text.setText(content);
	 
		back_support = (ImageView)findViewById(R.id.back_support);
		back_support.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				SupportActivity.this.finish();
				SupportActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
			}
		});
	  }
}
