package com.example.hellobookstore.type;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.home.BookActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookListAdapter extends BaseAdapter {

	private static final String TAG = "BookListAdapter";

	private LayoutInflater mInflater;

	private List<Book> mBookList;

	private Context mContext;

	public BookListAdapter(Context context, List<Book> mBookList) {
		this.mContext = context;
		this.mBookList = mBookList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mBookList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBookList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Book book = mBookList.get(position);
		final ViewHolder viewHolder;

		if (mContext == null) {
			mContext = parent.getContext();
		}

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.booklist_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Glide.with(mContext).load(book.getImageUrl()).into(viewHolder.bookImage);
		viewHolder.bookName.setText(book.getBookName());
		viewHolder.bookAbstract.setText(book.getBookAbstract());
		return convertView;
	}

	class ViewHolder {
		@BindView(R.id.book_image)
		ImageView bookImage;
		@BindView(R.id.book_name)
		TextView bookName;
		@BindView(R.id.book_abstract)
		TextView bookAbstract;
		View bookItemView;
		ViewHolder(View view) {
			bookItemView = view;
			ButterKnife.bind(this, view);
		}
	}
}