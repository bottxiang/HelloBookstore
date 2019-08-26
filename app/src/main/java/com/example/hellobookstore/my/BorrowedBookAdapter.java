package com.example.hellobookstore.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder> {

	Context context;
	private List<Book> books;

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.book_image)
		ImageView bookImage;
		@BindView(R.id.book_name)
		TextView bookName;

		View bookItemView;

		ViewHolder(View view) {
			super(view);
			bookItemView = view;
			ButterKnife.bind(this, view);
		}
	}

	public BorrowedBookAdapter(List<Book> books) {
		this.books = books;
	}

	@NonNull
	@Override
	public BorrowedBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (context == null) {
			context = parent.getContext();
		}
		View view = LayoutInflater.from(context).inflate(R.layout.borrowed_book_item, parent, false);
		BorrowedBookAdapter.ViewHolder viewHolder = new BorrowedBookAdapter.ViewHolder(view);
		viewHolder.bookItemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull BorrowedBookAdapter.ViewHolder holder, int position) {
		Book book = books.get(position);
		Glide.with(context).load(book.getImageUrl()).into(holder.bookImage);
		holder.bookName.setText(book.getBookName());
	}

	@Override
	public int getItemCount() {
		return books.size();
	}
}
