package com.kfx.android.activity;

import com.kfx.android.R;

import android.app.Activity;
import android.os.Bundle;

public class ProcAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_home);
        setTitle("北京市公安局治安管理总队便民服务平台");
    }
}