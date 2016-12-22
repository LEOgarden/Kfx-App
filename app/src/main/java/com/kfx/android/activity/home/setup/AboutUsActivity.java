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

public class AboutUsActivity extends Activity {
	 private ImageView backBtn;
	 private TextView aboutus_gongan;
	  protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.about_us_msg);
	 aboutus_gongan=(TextView) findViewById(R.id.aboutus_gongan);
		
		LawsService la=new LawsServiceImpl();
		String content=la.getLawsFileToString("com/kfx/android/xml/service/seal/about_us_msg.txt");
	
		aboutus_gongan.setText(content);
	 
	    backBtn = (ImageView)findViewById(R.id.back_aboutUs);
		backBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.finish();
				overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
			}
		});
	  }
}
