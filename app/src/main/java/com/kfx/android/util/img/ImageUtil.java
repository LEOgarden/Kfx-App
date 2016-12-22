package com.kfx.android.util.img;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

public class ImageUtil {
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	public static Bitmap getBitmapFromServer(String path,String type) {
    	Bitmap pic = null; 
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		HttpClient client=new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		
		HttpPost request;
		 try {
				request = new HttpPost(new URI(path.toString()));
				HttpResponse response=client.execute(request);
				if(response.getStatusLine().getStatusCode()==200){
				       HttpEntity  entity=response.getEntity();
				       InputStream is = entity.getContent();				   
				       if(type.equals("little")){
					       BitmapFactory.Options options = new BitmapFactory.Options();
					       options.inSampleSize = 6;
					       options.inPurgeable = true;  
					       options.inInputShareable = true;
					       pic =BitmapFactory.decodeStream(is,null,options);
				       }else{
					       pic = BitmapFactory.decodeStream(is);
				       } 
				}				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return pic;
	}
}
