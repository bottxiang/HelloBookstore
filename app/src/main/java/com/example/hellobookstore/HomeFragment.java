package com.example.hellobookstore;

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
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.example.hellobookstore.util.Utility;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

	public static final String TAG = "HomeFragment---";

	private static final String ARG_PARAM = "param";

	@BindView(R.id.title)
	TextView textView;
	@BindView(R.id.banner)
	Banner banner;
	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;

	private BookAdapter adapter;

	private String mParam1;
	private String mParam2;

	//
//	private List<BookCatalog> catalogs = new ArrayList<>();
//	//
//	private List<Book> books = new ArrayList<>();
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
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		//TextView textView = (TextView) view.findViewById(R.id.frag);
		ButterKnife.bind(this, view);
		displayView(view);

		return view;
	}

	private void displayView(View view) {
		textView.setText(getArguments().getString(ARG_PARAM, "Default"));
		//轮播图
		List<Integer> images = new ArrayList<>();
		images.add(R.drawable.ad1);
		images.add(R.drawable.ad2);
		images.add(R.drawable.ad3);
		Banner banner = (Banner) view.findViewById(R.id.banner);
		banner.setImageLoader(new GlideImageLoader())//设置图片加载器
				.setImages(images)//设置图片集合
				.start();//banner设置方法全部调用完毕时最后调用

		//书籍推荐
		List<Book> books = DataSupport.limit(20).find(Book.class);
		if (books.size() > 0) {
			GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
			recyclerView.setLayoutManager(layoutManager);
			adapter = new BookAdapter(books);
			recyclerView.setAdapter(adapter);
		} else {
			List<BookCatalog> catalogs = DataSupport.findAll(BookCatalog.class);
			if (catalogs.size() > 0) {
				for (int i = 0; i < catalogs.size(); i++) {
					String catalogId = catalogs.get(i).getBookCatalogId();
					Log.e(TAG, "ready to queryBookFromServer");
					Utility.queryBookFromServer(catalogId);
				}
			} else {
				Log.e(TAG, "ready to queryCatalogFromServer");
				Utility.queryCatalogFromServer();
			}
		}
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
