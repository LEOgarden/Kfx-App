package com.kfx.android.activity.home.setup.detail;

import com.kfx.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FnIntroductionActivity extends Activity {
	 private ImageView fu_introdution_back;
	 protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.fn_introduction);
	 fu_introdution_back = (ImageView)findViewById(R.id.fu_introdution_back);	
	 fu_introdution_back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			finish();
			FnIntroductionActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
			
		}
	});
	
	  }
}
