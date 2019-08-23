package com.example.hellobookstore.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hellobookstore.R;
import com.example.hellobookstore.db.BookCatalog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookCatalogAdapter extends ArrayAdapter<BookCatalog> {

	private static final String TAG = "BookCatalogAdapter";
	private int resourceId;

	public BookCatalogAdapter(Context context, int resource, List<BookCatalog> objects) {
		super(context, resource, objects);
		this.resourceId = resource;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookCatalog bookCatalog = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.bookName.setText(bookCatalog.getBookCatalogName());
		return view;
	}


	static class ViewHolder {
		@BindView(R.id.book_name)
		TextView bookName;

		ViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}
}