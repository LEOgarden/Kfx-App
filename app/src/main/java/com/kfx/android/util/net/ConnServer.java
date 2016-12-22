package com.kfx.android.util.net;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

public class ConnServer extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			HttpClient client=new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 3000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 3000);
			HttpPost post;
			URI url=null;
			try {
				url=new URI(params[0]);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			post = new HttpPost(url);
			Log.i("doInBack1...", params[0]);
			HttpResponse response=client.execute(post);
			
			Log.i("doInBack2...", params[0]);
			if(response.getStatusLine().getStatusCode()==200){
				return "success";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();			
			return "ClientError";
		} catch (IOException e) {
			e.printStackTrace();
			return "ServerError";
		}
		return "success";
	}
}
