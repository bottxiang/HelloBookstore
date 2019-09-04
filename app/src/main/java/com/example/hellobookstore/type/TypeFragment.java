package com.example.hellobookstore.type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.example.hellobookstore.home.BookActivity;
import com.example.hellobookstore.home.HomeFragment;
import com.example.hellobookstore.util.HttpUtil;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hellobookstore.util.Utility.KEY;
import static com.example.hellobookstore.util.Utility.PN;
import static com.example.hellobookstore.util.Utility.RN;
import static com.example.hellobookstore.util.Utility.handleBookResponse;
import static com.example.hellobookstore.util.Utility.handleCatalogResponse;


public class TypeFragment extends Fragment {

	public static final String TAG = "TypeFragment---";

	private static final String ARG_PARAM = "param";


	View rootView;

	@BindView(R.id.bookCatalog_listView)
	ListView bookCatalogListView;
	@BindView(R.id.book_listView)
	ListView bookListView;
	@BindView(R.id.title)
	TextView title;
	private List<Book> mCurrentLists;
	List<BookCatalog> mCatalogs;
	List<Book> mBookLists;

	BookListAdapter bookListAdapter;
	BookCatalogAdapter adapter;
	private Context context;
	private boolean isVisible;
	private boolean isPrepared;

	public TypeFragment() {
		// Required empty public constructor
	}


	public static TypeFragment newInstance(String param) {
		TypeFragment fragment = new TypeFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_PARAM, param);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
		Log.e(TAG, "onCreate");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			lazyLoad();
		}else{
			isVisible = false;
			// fragment is no longer visible
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_type, container, false);
		}
		//初始化控件
		ButterKnife.bind(this, rootView);
		isPrepared = true;
		//填充控件数据
		lazyLoad();
		return rootView;
	}

	private void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		}
		loadData();
	}


	private void loadData() {
		title.setText(getArguments().getString(ARG_PARAM));
		mCatalogs = DataSupport.findAll(BookCatalog.class);
		String categoryId = "242";
		mBookLists = DataSupport.where("catalogId=" + categoryId).find(Book.class);
		adapter = new BookCatalogAdapter(getContext(), R.layout.bookcatalog_item, mCatalogs);
		bookListAdapter = new BookListAdapter(getContext(), mBookLists);

		bookCatalogListView.setAdapter(adapter);
		bookListView.setAdapter(bookListAdapter);
		bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Book book = mBookLists.get(i);
				Intent intent = new Intent(context, BookActivity.class);
				intent.putExtra("bookId", book.getId());
				context.startActivity(intent);
			}
		});

		if (mCatalogs.size() > 0) {

			queryBook();

		} else {
			queryCatalogFromServer();
		}
	}

	private void queryBook() {
		bookCatalogListView.setOnItemClickListener((adapterView, parent, position, id) -> {
			BookCatalog category = mCatalogs.get(position);
			String currentId = category.getBookCatalogId();
			mBookLists.clear();
			mCurrentLists = DataSupport.where("catalogId=" + currentId).find(Book.class);

			if (mCurrentLists.size() > 0) {
				mBookLists.addAll(mCurrentLists);
			} else {
				queryBookFromServer(currentId);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mCurrentLists = DataSupport.where("catalogId=" + currentId).find(Book.class);
				mBookLists.addAll(mCurrentLists);
			}
			mCurrentLists.clear();
			bookListAdapter.notifyDataSetChanged();
		});
	}

	public static void queryBookFromServer(String catalogId) {
		String adress = "http://apis.juhe.cn/goodbook/query?key=" + KEY +
				"&catalog_id=" + catalogId +
				"&pn=" + PN +
				"&rn=" + RN;
//		String adress = "http://192.168.65.66:8080/atguigu/juhe/" +
//				catalogId + "book.json";
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
		String adress = "http://apis.juhe.cn/goodbook/catalog?key=" + KEY;
		//String adress = "http://192.168.65.66:8080/atguigu/juhe/catalog.json";
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