package com.kfx.android.activity.service.seal.val;

import com.kfx.android.R;
import com.kfx.android.util.service.LawsService;
import com.kfx.android.util.service.impl.LawsServiceImpl;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SealUserAgreementActivity extends Activity {
	private ImageView btn_agreement;
	private TextView agreement_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sealuser_agreement);
		agreement_text=(TextView) findViewById(R.id.agreement_text);
		
		LawsService la=new LawsServiceImpl();
		String content=la.getLawsFileToString("com/kfx/android/xml/service/seal/agree.txt");
	
		agreement_text.setText(content);
		
		
		btn_agreement=(ImageView) findViewById(R.id.btn_agreement);
		btn_agreement.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				SealUserAgreementActivity.this.finish();
			}
		});
		
	}
}
