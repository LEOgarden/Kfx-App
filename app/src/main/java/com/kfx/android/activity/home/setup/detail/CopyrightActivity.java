package com.kfx.android.activity.home.setup.detail;
import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CopyrightActivity extends Activity {
	 private ImageView back_copyright;
	 private TextView copyright_statement;
	  protected void onCreate(Bundle savedInstanceStates){
	 super.onCreate(savedInstanceStates);
	 setContentView(R.layout.copyright_msg);
	 copyright_statement=(TextView) findViewById(R.id.copyright_statement);
		
		LawsService la=new LawsServiceImpl();
		String content=la.getLawsFileToString("com/kfx/android/xml/service/seal/copyright_statement.txt");
	
		copyright_statement.setText(content);
	 
		back_copyright = (ImageView)findViewById(R.id.back_copyright);
		back_copyright.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				CopyrightActivity.this.finish();
				CopyrightActivity.this.overridePendingTransition(R.animator.in_from_right,R.animator.out_to_left);
			}
		});
	  }
}
