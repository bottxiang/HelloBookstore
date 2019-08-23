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
					Log.e(TAG,"书籍存入成功！");
					return true;
				} else {
					Log.e(TAG,"handle--Book--Error: resultCode:" + resultCode);
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
				JSONObject json = new JSONObject(response);
				String resultCode = json.getString("resultcode");
				if (resultCode.equals("200")) {
					JSONArray result = json.getJSONArray("result");
					for (int i = 0; i < result.length(); i++) {
						JSONObject catalogJson = result.getJSONObject(i);
						String bookCatalogId = catalogJson.getString("id");
						String bookCatalogName = catalogJson.getString("catalog");
						BookCatalog bookCatalog = new BookCatalog(bookCatalogId, bookCatalogName);
						bookCatalog.save();
					}
					Log.e(TAG,"目录存入成功！");
					return true;
				} else {
					Log.e(TAG,"handle--Catalog--Error: resultCode:" + resultCode);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
