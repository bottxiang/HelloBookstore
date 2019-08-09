package com.example.hellobookstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.db.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

	Context context;
	private List<Book> books;

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.book_image)
		ImageView bookImage;
		@BindView(R.id.book_name)
		TextView bookName;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	public BookAdapter(List<Book> books) {
		this.books = books;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (context == null) {
			context = parent.getContext();
		}
		View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Book book = books.get(position);
		Glide.with(context).load(book.getImageUrl()).into(holder.bookImage);
		holder.bookName.setText(book.getBookName());
	}

	@Override
	public int getItemCount() {
		return books.size();
	}
}
