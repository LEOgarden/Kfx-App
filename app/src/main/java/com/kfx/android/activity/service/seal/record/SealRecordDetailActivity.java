package com.kfx.android.activity.service.seal.record;

import com.kfx.android.R;
import com.kfx.android.activity.service.seal.record.map.MapLocationActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SealRecordDetailActivity extends Activity {
	private TextView factory_record_name;
	private TextView factory_record_user;
	private TextView factory_record_telp;
	private TextView factory_record_area;
	private TextView factory_record_address;
	private TextView factory_record_introduction;
	public static double x=0;
	public static double y=0;
	//private ImageView factory_record_image;
	private ImageView iv;
	private ImageView sealCallImageView;
	private ImageView sealPositionImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.seal_record_detail);
		// 设置返回按钮
		// 设置印章通讯录主界面返回按钮
		iv = (ImageView) findViewById(R.id.sealCompanyDetailBackBtn);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SealRecordDetailActivity.this.finish();
			}
		});
		// 获取控件
		factory_record_name = (TextView) findViewById(R.id.factory_record_name);
		factory_record_user = (TextView) findViewById(R.id.factory_record_user);
		factory_record_telp = (TextView) findViewById(R.id.factory_record_telp);
		factory_record_area = (TextView) findViewById(R.id.factory_record_area);
		factory_record_address = (TextView) findViewById(R.id.factory_record_address);
		factory_record_introduction = (TextView) findViewById(R.id.factory_record_introduction);
		//factory_record_image = (ImageView) findViewById(R.id.factory_record_image);
		sealCallImageView = (ImageView) findViewById(R.id.sealCallImageView);
		sealPositionImageView = (ImageView) findViewById(R.id.sealPositionImageView);
		
		Intent intent = getIntent();
		String factory_name = intent
				.getStringExtra("demo.android.factory.name");
		factory_record_name.setText(factory_name);
		String factory_user = intent
				.getStringExtra("demo.android.factory.user");
		factory_record_user.setText(factory_user);
		String factory_telp = intent
				.getStringExtra("demo.android.factory.telp");
		factory_record_telp.setText(factory_telp);
		String factory_address = intent
				.getStringExtra("demo.android.factory.address");
		factory_record_address.setText(factory_address);
		String factory_province = intent
				.getStringExtra("demo.android.factory.province");
		String factory_city = intent
				.getStringExtra("demo.android.factory.city");
		factory_record_area.setText(factory_province + " " + factory_city);
		String factory_introduction = intent
				.getStringExtra("demo.android.factory.introduction");
		x=Double.parseDouble(intent.getStringExtra("demo.android.factory.xPosition"));
		y=Double.parseDouble(intent.getStringExtra("demo.android.factory.yPosition"));
		String position = "东经:"
				+ intent.getStringExtra("demo.android.factory.xPosition")
				+ "北纬:"
				+ intent.getStringExtra("demo.android.factory.yPosition");
		String str = factory_name + "位于" + factory_province + "市"
				+ factory_city + "，处于" + position
				+ ",于1999年9月9日在北京东城区东手帕28号201甲（方通过该个人个人个人个人各认购）,"
				+ factory_introduction;
		factory_record_introduction.setText(str);
//		String photoPath = intent
//				.getStringExtra("demo.android.factory.photoPath");
		//拨打电话
		sealCallImageView.setOnClickListener(callListener);
		factory_record_telp.setOnClickListener(callListener);
		factory_record_address.setOnClickListener(positionListener);
		sealPositionImageView.setOnClickListener(positionListener);
	}

	OnClickListener callListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String telp = factory_record_telp.getText().toString()
					.replaceAll(" ", "");
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ telp));
			startActivity(intent);
		}
	};
	OnClickListener positionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent=getIntent();
			intent.putExtra("com.kfx.service.seal.record.factory.address", factory_record_address.getText().toString().replace(" ", ""));
			intent.setClass(SealRecordDetailActivity.this, MapLocationActivity.class);
			startActivity(intent);
		}
	};
}