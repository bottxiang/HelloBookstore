package com.example.hellobookstore.util;


import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
	private static final String TAG = "HttpUtil";
	public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				try {
					OkHttpClient client = new OkHttpClient();
					Request requst = new Request.Builder().url(address).build();
					client.newCall(requst).enqueue(callback);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "run: 发送Http请求异常");
				}
//			}
//		}).start();

	}
}
