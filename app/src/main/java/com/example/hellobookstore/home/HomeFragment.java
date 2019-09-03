package com.example.hellobookstore.home;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.example.hellobookstore.db.Event;
import com.example.hellobookstore.db.Weather;
import com.example.hellobookstore.util.HttpUtil;
import com.example.hellobookstore.util.Utility;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hellobookstore.util.Utility.EVENT_KEY;
import static com.example.hellobookstore.util.Utility.KEY;
import static com.example.hellobookstore.util.Utility.PN;
import static com.example.hellobookstore.util.Utility.RN;
import static com.example.hellobookstore.util.Utility.WEATHER_KEY;
import static com.example.hellobookstore.util.Utility.handleBookResponse;
import static com.example.hellobookstore.util.Utility.handleCatalogResponse;
import static com.example.hellobookstore.util.Utility.handleDetailResponse;
import static com.example.hellobookstore.util.Utility.handleEventResponse;

public class HomeFragment extends Fragment {

	public static final String TAG = "HomeFragment---";

	private static final String ARG_PARAM = "param";

	@BindView(R.id.title)
	TextView textView;
	@BindView(R.id.banner)
	Banner banner;
	@BindView(R.id.message)
	TextView message;
	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;


	@BindView(R.id.event_date)
	TextView eventDate;
	@BindView(R.id.event_title)
	TextView eventTitle;
	//	@BindView(R.id.event_content)
//	TextView eventContent;
	@BindView(R.id.today_in_history)
	FrameLayout todayInHistory;
	@BindView(R.id.weather)
	TextView weatherText;

	@BindView(R.id.bing_pic_img)
	ImageView bingPicImg;
	@BindView(R.id.title_city)
	TextView titleCity;
	@BindView(R.id.weather_info_text)
	TextView weatherInfoText;
	@BindView(R.id.degree_text)
	TextView degreeText;

	View rootView;
	Context context;
	private BookAdapter adapter;
	private List<Integer> images;
	private List<BookCatalog> catalogs;
	private List<Book> books = new ArrayList<>();
	private LocationClient mLocationClient;
	private String city;

	public HomeFragment() {
		// Required empty public constructor
	}


	public static HomeFragment newInstance(String param) {
		HomeFragment fragment = new HomeFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_PARAM, param);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_home, container, false);
		}
		ButterKnife.bind(this, rootView);

		loadData();
		return rootView;
	}


	private void loadData() {
		images = new ArrayList<>();
		catalogs = new ArrayList<>();
		books = new ArrayList<>();
		//轮播图
		images.add(R.drawable.ad1);
		images.add(R.drawable.ad2);
		images.add(R.drawable.ad3);

		banner.setImageLoader(new GlideImageLoader())//设置图片加载器
				.setImages(images)//设置图片集合
				.start();//banner设置方法全部调用完毕时最后调用



		//借书消息
		message.setText(Utility.generateMessage(getContext()));

		//历史上的今天
		Event event = getEvent();
		Glide.with(getActivity())
				.asBitmap()
				.load(event.getPicUrl())
				.into(new SimpleTarget<Bitmap>() {
					@Override
					public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
						Drawable drawable = new BitmapDrawable(resource);
						todayInHistory.setBackground(drawable);
					}

				});
		eventDate.setText(event.getDate());
		eventTitle.setText(event.getTitle());

		//天气
		weather();

		//推荐书籍
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new BookAdapter(books);
		recyclerView.setAdapter(adapter);

		queryCatalog();


	}

	private void weather() {
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(new MyLocationListener());
		List<String> permissionList = new ArrayList<>();
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.READ_PHONE_STATE);
		}
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		if (!permissionList.isEmpty()) {
			String[] permissions = permissionList.toArray(new String[permissionList.size()]);
			ActivityCompat.requestPermissions(getActivity(), permissions, 1);
		} else {
			requestLocation();
		}
	}



	private void requestLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0) {
					for (int result : grantResults) {
						if (result != PackageManager.PERMISSION_GRANTED) {
							Toast.makeText(context, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
							getActivity().finish();
							return;
						}
					}
					requestLocation();
				} else {
					Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show();
					getActivity().finish();
				}
				break;
			default:
		}
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			city = bdLocation.getCity();
			city = city.substring(0, city.length()-1);
			Log.e(TAG, city);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			String weatherString = prefs.getString("weather", null);
			if (weatherString != null) {
				Weather weather = Utility.handleWeatherResponse(weatherString);
				showWeatherInfo(weather);
			} else {
				requestWeather(city);
			}
		}


	}

	/**
	 * 根据城市名请求天气信息
	 */
	public void requestWeather(final String city_name) {
		String weatherUrl = "http://apis.juhe.cn/simpleWeather/query?"
				+ "key=" + WEATHER_KEY
				+"&city=" + city_name;
		Log.e(TAG, weatherUrl);
		HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				e.printStackTrace();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.e(TAG, "获取天气信息失败");
						Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

				final String responseText = response.body().string();
				Log.e(TAG, "获取天气信息成功，　responseText＝" + responseText);
				final Weather weather = Utility.handleWeatherResponse(responseText);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (weather != null) {
							SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
							editor.putString("weather", responseText);
							editor.apply();
							showWeatherInfo(weather);
						}
					}
				});
			}
		});
	}

	private void showWeatherInfo(Weather weather) {
		titleCity.setText(weather.getCityName());
		degreeText.setText(weather.getDegree() + "℃");
		weatherInfoText.setText(weather.getWeatherInfo());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
	}

	private Event getEvent() {
		List<Event> events = new ArrayList<>();

		//String adress1 = "http://192.168.65.66:8080/atguigu/juhe/event_list.json";
		String adress1 = "http://v.juhe.cn/todayOnhistory/queryEvent.php?key="
				+ EVENT_KEY
				+ "&date=8/29";
		Log.e(TAG, "queryEventFromServer:" + adress1);
		HttpUtil.sendOkHttpRequest(adress1, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG, "queryEventFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

				String responseText = response.body().string();
				Log.e(TAG, "正在从服务器取event_list, responseText=" + responseText);
				handleEventResponse(events, responseText);
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Random rd = new Random();
		int index = rd.nextInt(events.size());
		String e_id = events.get(index).getE_id();
		String adress2 = "http://v.juhe.cn/todayOnhistory/queryDetail.php?key="
				+ EVENT_KEY
				+ "&e_id=" + e_id;
		Log.e(TAG, "queryDetailFromServer:" + adress2);
		HttpUtil.sendOkHttpRequest(adress2, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG, "queryDetailFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

				String responseText = response.body().string();
				Log.e(TAG, "正在从服务器取event_detail, responseText=" + responseText);
				handleDetailResponse(events, e_id, responseText);
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return events.get(index);
	}


	private void queryCatalog() {
		catalogs = DataSupport.findAll(BookCatalog.class);
		if (catalogs.size() > 0) {
			queryBook();
		} else {
			queryCatalogFromServer();
		}
	}

	private void queryBook() {
		books.clear();
		books.addAll(DataSupport.limit(20).find(Book.class));
		if (books.size() > 0) {
			adapter.notifyDataSetChanged();
		} else {
			catalogs = DataSupport.findAll(BookCatalog.class);

			for (int i = 0; i < catalogs.size(); i++) {
				String catalogId = catalogs.get(i).getBookCatalogId();
				queryBookFromServer(catalogId);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			books.clear();
			books.addAll(DataSupport.limit(20).find(Book.class));
			Log.e(TAG, "开始querybook " + books.size());
			adapter.notifyDataSetChanged();
		}
	}

	public void queryBookFromServer(String catalogId) {
//		String adress = "http://apis.juhe.cn/goodbook/query?key=" + KEY +
//				"&catalog_id=" + catalogId +
//				"&pn=" + PN +
//				"&rn=" + RN;
		String adress = "http://192.168.65.66:8080/atguigu/juhe/" +
				catalogId + "book.json";
		Log.e(TAG, "queryBookFromServer:" + adress);
		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG, "queryBookFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

				String responseText = response.body().string();
				Log.e(TAG, "正在从服务器取book, catalogId=" + catalogId + ", responseText=" + responseText);
				Boolean result = handleBookResponse(responseText, catalogId);
//				if (result) {
//					getActivity().runOnUiThread(() -> {
//						queryBook();
//					});
//				}
			}
		});
	}

	public void queryCatalogFromServer() {
		//String adress = "http://apis.juhe.cn/goodbook/catalog?key=" + KEY;
		String adress = "http://192.168.65.66:8080/atguigu/juhe/catalog.json";
		Log.e(TAG, "queryCatalogFromServer:" + adress);

		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG, "queryCatalogFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				String responseText = response.body().string();
				Log.e(TAG, "正在从服务器取catalog, responseText=" + responseText);
				Boolean result = handleCatalogResponse(responseText);
				if (result) {
					getActivity().runOnUiThread(() -> {
						Log.e(TAG, "开始querybook 11");
						queryBook();
					});
				}
			}
		});
	}


	class GlideImageLoader extends ImageLoader {

		@Override
		public void displayImage(Context context, Object path, ImageView imageView) {
			Glide.with(context)
					.load(path)
					.into(imageView);
		}
	}


}


//		Uri uri = Uri.parse("content://com.thundersoft.BookStore.provider/book");
//		Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				String title = cursor.getString(cursor.getColumnIndex("title"));
//				String imageUrl = cursor.getString(cursor.getColumnIndex("imageUrl"));
//				books.add(new Book(title, imageUrl));
//				Log.e(TAG, "book name is " + title);
//			}
//			GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//			recyclerView.setLayoutManager(layoutManager);
//			adapter = new BookAdapter(books);
//			recyclerView.setAdapter(adapter);
//			cursor.close();
//		}
