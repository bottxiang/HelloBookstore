package com.example.hellobookstore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.db.Book;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

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
	//轮播图资源
	private List<Integer> images = new ArrayList<>();
	//
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
		images.add(R.drawable.ad1);
		images.add(R.drawable.ad2);
		images.add(R.drawable.ad3);
		Banner banner = (Banner) view.findViewById(R.id.banner);
		banner.setImageLoader(new GlideImageLoader())//设置图片加载器
				.setImages(images)//设置图片集合
				.start();//banner设置方法全部调用完毕时最后调用
		//书籍推荐
		books.clear();
		Book bk = new Book();
		bk.setBookName("Book Name");
		bk.setImageID(R.drawable.ad1);
		for (int i = 0; i < 50; i++) {
			books.add(bk);
		}
		GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new BookAdapter(books);
		recyclerView.setAdapter(adapter);
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
