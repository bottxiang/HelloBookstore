package com.example.hellobookstore.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

	public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
		OkHttpClient client = new OkHttpClient();
		Request requst = new Request.Builder().url(address).build();
		client.newCall(requst).enqueue(callback);
	}
}
