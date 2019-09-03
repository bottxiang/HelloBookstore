package com.example.hellobookstore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.example.hellobookstore.db.Event;
import com.example.hellobookstore.db.User;
import com.example.hellobookstore.db.Weather;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Utility {

	public static final String TAG="Utility---";
	public static final String KEY="987f68f7779e63af4c7f404586d4a4ea";
	public static final String WEATHER_KEY="accbd471971dfce2cfd202c5e4de7ab8";
	public static final String EVENT_KEY="b61cd7ece4b9388de10eb42984b7c5e4";
	public static final String PN="0";
	public static final String RN="20";


	public static boolean handleEventResponse(List<Event> events, String response) {
		try {
			if (TextUtils.isNotEmpty(response)) {
				JSONObject json = new JSONObject(response);
				String reason = json.getString("reason");
				if (reason.equals("success")) {
					JSONArray result = json.getJSONArray("result");
					for (int i = 0; i < result.length(); i++) {
						JSONObject eventListJson = result.getJSONObject(i);
						String date = eventListJson.getString("date");
						String title = eventListJson.getString("title");
						String e_id = eventListJson.getString("e_id");
						Event event = new Event(date, title, e_id);
						events.add(event);
					}
					Log.e(TAG,"event_list添加成功！");
					return true;
				} else {
					Log.e(TAG,"event_list: FAILD");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean handleDetailResponse(List<Event> events, String e_id, String response) {
		try {
			if (TextUtils.isNotEmpty(response)) {
				JSONObject json = new JSONObject(response);
				String reason = json.getString("reason");
				if (reason.equals("success")) {
					JSONArray result = json.getJSONArray("result");
					JSONObject detail = result.getJSONObject(0);
					String content = detail.getString("content");
					JSONArray urls = detail.getJSONArray("picUrl");
					JSONObject url = urls.getJSONObject(0);
					String picUrl = url.getString("url");
					for(Event event : events) {
						if (e_id.equals(event.getE_id())) {
							event.setContent(content);
							event.setPicUrl(picUrl);
						}
					}
					Log.e(TAG,"event_detail添加成功！");
					return true;
				} else {
					Log.e(TAG,"event_detail: FAILD");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;

	}
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

	//由书籍id数组返回书籍对象列表
	public static List<Book> getBooks(String[] borrowedBookIds) {
		List<Book> books = new ArrayList<>();
		for (int i = 0; i < borrowedBookIds.length; i++) {
			int bookId = borrowedBookIds[i].equals("") ? 0 : Integer.valueOf(borrowedBookIds[i]);
			Book book = DataSupport.find(Book.class, bookId);
			if (book != null) {
				books.add(book);
			}
		}
		return books;
	}

	//生成借书消息
	public static String generateMessage(Context context) {

		SharedPreferences pref = context.getSharedPreferences("login_info", MODE_PRIVATE);
		String username = pref.getString("username", "");
		User user = LoginDao.getUser(username);
		Log.e(TAG, "username: " + user.getUsername());
		String[] borrowedBookIds = user.getRentedBookString().split(" ");
		List<Book> books = Utility.getBooks(borrowedBookIds);
		StringBuilder message = new StringBuilder("读者" + user.getUsername() + "，您当前共借了"
				+ books.size() + "本书，分别是");
		for (Book book : books) {
			message.append("《" + book.getBookName() + "》，");
		}
		message.append("请阅读后按时归还！");

		return message.toString();
	}


	public static Weather handleWeatherResponse(String response) {
		try {
			if (TextUtils.isNotEmpty(response)) {
				JSONObject json = new JSONObject(response);
				String reason = json.getString("reason");

				JSONObject result = json.getJSONObject("result");
				String cityName = result.getString("city");
				JSONObject weatherContent = result.getJSONObject("realtime");
				String degree = weatherContent.getString("temperature");
				String weatherInfo = weatherContent.getString("info");
				Weather weather = new Weather(cityName, degree, weatherInfo);
				return weather;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
