package com.example.hellobookstore.util;

import android.util.Log;

import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {

	public static final String TAG="Utility---";
	public static final String KEY="987f68f7779e63af4c7f404586d4a4ea";
	public static final String PN="0";
	public static final String RN="20";

	public static boolean handleBookResponse(String response, String catalogId) {
		try {
			if (TextUtils.isNotEmpty(response)) {
				JSONObject json = new JSONObject(response);
				String resultCode = json.getString("resultcode");
				if (resultCode.equals("200")) {
					JSONObject result = json.getJSONObject("result");
					JSONArray data = result.getJSONArray("data");
					for (int i = 0; i < data.length(); i++) {
						JSONObject bookJson = data.getJSONObject(i);
						String bookName = bookJson.getString("title");
						String bookCatalog = bookJson.getString("catalog");
						String bookTags = bookJson.getString("tags");
						String bookAbstract = bookJson.getString("sub1");
						String bookContent = bookJson.getString("sub2");
						String imageUrl = bookJson.getString("img");
						String reading = bookJson.getString("reading");
						String bookOnline = bookJson.getString("online");
						String bookBytime = bookJson.getString("bytime");

						Book book = new Book(bookName, bookCatalog, bookTags, bookAbstract,
								bookContent, imageUrl, reading, bookOnline, bookBytime, catalogId);
						book.save();
					}
					Log.e(TAG,"handleBookResponse:OK");
					return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean handleCatalogResponse(String response) {
		Log.e(TAG,"enter into handleCatalogResponse:OK");
		try {
			if (TextUtils.isNotEmpty(response)) {
				Log.e(TAG,"try");
				JSONObject json = new JSONObject(response);
				String resultCode = json.getString("resultcode");
				if (resultCode.equals("200")) {
					Log.e(TAG,"try2");
					JSONArray result = json.getJSONArray("result");
					for (int i = 0; i < result.length(); i++) {
						Log.e(TAG,"try3");
						JSONObject catalogJson = result.getJSONObject(i);
						String bookCatalogId = catalogJson.getString("id");
						String bookCatalogName = catalogJson.getString("catalog");
						BookCatalog bookCatalog = new BookCatalog(bookCatalogId, bookCatalogName);
						bookCatalog.save();
					}
					Log.e(TAG,"handleCatalogResponse:OK");
					return true;
				} else {
					Log.e(TAG,"try4");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void queryBookFromServer(String catalogId) {
//		String adress = "http://apis.juhe.cn/goodbook/query?key=" + KEY +
//				"&catalog_id=" + catalogId +
//				"&pn=" + PN +
//				"&rn=" + RN;
		String adress = "http://192.168.65.66:8080/atguigu/juhe/" +
				catalogId + "book.json";
		Log.e(TAG,"queryBookFromServer:" + adress);
		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG,"queryBookFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				String responseText = response.body().string();
				Log.e(TAG,"queryBookFromServer:responseText=" + responseText);
				Log.e(TAG,"queryBookFromServer:catalogId = " + catalogId);

				handleBookResponse(responseText, catalogId);
			}
		});
	}

	public static void queryCatalogFromServer() {
		//String adress = "http://apis.juhe.cn/goodbook/catalog?key=" + KEY;
		String adress = "http://192.168.65.66:8080/atguigu/juhe/catalog.json";
		Log.e(TAG,"queryCatalogFromServer:" + adress);

		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG,"queryCatalogFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				String responseText = response.body().string();
				Log.e(TAG,"queryCatalogFromServer:responseText=" + responseText);

				handleCatalogResponse(responseText);
			}
		});
	}

}
