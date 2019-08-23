package com.example.hellobookstore.type;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.BookCatalog;
import com.youth.banner.loader.ImageLoader;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



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

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_type, container, false);
		}
		ButterKnife.bind(this, rootView);

		displayView();

		return rootView;
	}


	private void displayView() {
		title.setText(getArguments().getString(ARG_PARAM));
		final List<BookCatalog> mCatalogs = DataSupport.findAll(BookCatalog.class);
		String categoryId = "242";
		final List<Book> mBookLists = DataSupport.where("catalogId=" + categoryId).find(Book.class);

		if (mCatalogs.size() > 0) {

			BookCatalogAdapter adapter = new BookCatalogAdapter(getContext(), R.layout.bookcatalog_item, mCatalogs);
			BookListAdapter bookListAdapter = new BookListAdapter(getContext(), mBookLists);

			bookCatalogListView.setAdapter(adapter);
			bookListView.setAdapter(bookListAdapter);
			bookCatalogListView.setOnItemClickListener((adapterView, parent, position, id) -> {
				BookCatalog category = mCatalogs.get(position);
				String currentId = category.getBookCatalogId();
				mBookLists.clear();
				List<Book> mCurrentLists = DataSupport.where("catalogId=" + currentId).find(Book.class);

				if (mCurrentLists.size() > 0) {
					for (int i = 0; i < mCurrentLists.size(); i++) {
						mBookLists.add(mCurrentLists.get(i));
					}
				} else {
					//queryBookFromServer(currentId);
				}
				mCurrentLists.clear();
				bookListAdapter.notifyDataSetChanged();
			});

//			bookListView.setOnItemClickListener((adapterView, parent, position, id) -> {
//				Intent intent = new Intent(getContext(), BookActivity.class);
//				Book book = mBookLists.get(position);
//				//传递book对象
//				intent.putExtra("book", new Gson().toJson(book));
//				getContext().startActivity(intent);
//			});
		} else {
			//queryCatalogFromServer();
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
