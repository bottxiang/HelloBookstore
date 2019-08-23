package com.example.hellobookstore.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.example.hellobookstore.util.HttpUtil;
import com.example.hellobookstore.util.Utility;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hellobookstore.util.Utility.handleBookResponse;
import static com.example.hellobookstore.util.Utility.handleCatalogResponse;

public class HomeFragment extends Fragment {

	public static final String TAG = "HomeFragment---";

	private static final String ARG_PARAM = "param";

	@BindView(R.id.title)
	TextView textView;
	@BindView(R.id.banner)
	Banner banner;
	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;

	View rootView;

	private BookAdapter adapter;
	private List<Integer> images;
	private List<BookCatalog> catalogs;
	private List<Book> books = new ArrayList<>();

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

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_home, container, false);
		}
		ButterKnife.bind(this, rootView);
		textView.setText(getArguments().getString(ARG_PARAM));
		loadData();
		return rootView;
	}


	private void loadData() {
		images = new ArrayList<>();
		catalogs = new ArrayList<>();
		books = new ArrayList<>();

		images.add(R.drawable.ad1);
		images.add(R.drawable.ad2);
		images.add(R.drawable.ad3);

		banner.setImageLoader(new GlideImageLoader())//设置图片加载器
				.setImages(images)//设置图片集合
				.start();//banner设置方法全部调用完毕时最后调用

		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new BookAdapter(books);
		recyclerView.setAdapter(adapter);

		queryCatalog();

//		List<Book> recommendBooks = DataSupport.limit(10).find(Book.class);
//		books.addAll(recommendBooks);
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
		Log.e(TAG,"开始querybook 22");
		books.clear();
		books.addAll(DataSupport.findAll(Book.class));
		//books.addAll(DataSupport.limit(10).find(Book.class));
		if (books.size() > 0) {
			Log.e(TAG,"开始querybook 33");
			adapter.notifyDataSetChanged();
		} else {
			Log.e(TAG,"开始querybook 44");
			catalogs = DataSupport.findAll(BookCatalog.class);

			for (int i = 0; i < catalogs.size(); i++) {
				Log.e(TAG,"开始querybook 55");
				String catalogId = catalogs.get(i).getBookCatalogId();
				queryBookFromServer(catalogId);
			}

			ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
			int noThreads = currentGroup.activeCount();
			Thread[] threads = new Thread[noThreads];
			currentGroup.enumerate(threads);
			for (Thread t : threads) {
				if (Thread.currentThread().getId()!=t.getId()) {
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			//queryBook();
			books.clear();
			books.addAll(DataSupport.findAll(Book.class));
			Log.e(TAG,"开始querybook 66 " + Thread.currentThread().getName()+ books.size());
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
		Log.e(TAG,"queryBookFromServer:" + adress);
		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG,"queryBookFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

				String responseText = response.body().string();
				Log.e(TAG,"正在从服务器取book, catalogId=" + catalogId +", responseText=" + responseText);
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
		Log.e(TAG,"queryCatalogFromServer:" + adress);

		HttpUtil.sendOkHttpRequest(adress, new Callback() {
			@Override
			public void onFailure(@NotNull Call call, @NotNull IOException e) {
				Log.e(TAG,"queryCatalogFromServer:FAILED");
			}

			@Override
			public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
				String responseText = response.body().string();
				Log.e(TAG,"正在从服务器取catalog, responseText=" + responseText);
				Boolean result = handleCatalogResponse(responseText);
				if (result) {
					getActivity().runOnUiThread(() -> {
						Log.e(TAG,"开始querybook 11");
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
