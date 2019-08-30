package com.example.hellobookstore.my;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.User;
import com.example.hellobookstore.util.LoginDao;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder>  implements View.OnClickListener {
	public static final String TAG = "BorrowedBookAdapter---";

	private Context context;
	private List<Book> books;

	class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.book_image)
		ImageView bookImage;
		@BindView(R.id.book_name)
		TextView bookName;
		@BindView(R.id.btn_cancel_borrow)
		Button btnCancelBorrow;

		View bookItemView;

		ViewHolder(View view) {
			super(view);
			bookItemView = view;
			ButterKnife.bind(this, view);
			btnCancelBorrow.setOnClickListener(BorrowedBookAdapter.this);
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
		this.setOnItemClickListener(new BorrowedBookAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				switch(v.getId()) {
					case R.id.btn_cancel_borrow:
						//取消借阅：将该书的状态设为未借阅
						Book bk = books.remove(position);
						Book book = DataSupport.find(Book.class, bk.getId());
						Log.e(TAG, " book id:" + book.getId());
						book.setRented(false);
						book.save();
						//从用户的借阅列表中删除该书
						User user = DataSupport.find(User.class, LoginDao.getUser(context).getId());
						user.removeBookId(book.getId());
						Log.e(TAG, "user.rentedBookString=" + user.getRentedBookString());
						user.save();
						//user.update(user.getId());
						Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
						BorrowedBookAdapter.this.notifyDataSetChanged();
				}
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull BorrowedBookAdapter.ViewHolder holder, int position) {
		if (books != null) {
			Book book = books.get(position);
			Glide.with(context).load(book.getImageUrl()).into(holder.bookImage);
			holder.bookName.setText(book.getBookName());

			holder.btnCancelBorrow.setTag(position);
		}

	}

	@Override
	public int getItemCount() {
		return books == null ? 0 : books.size();
	}


	public interface OnItemClickListener  {
		void onItemClick(View v, int position);
	}

	private OnItemClickListener mOnItemClickListener;//声明自定义的接口


	public void setOnItemClickListener(OnItemClickListener  listener) {
		this.mOnItemClickListener  = listener;
	}

	@Override
	public void onClick(View v) {
		int position = (int) v.getTag();
		if (mOnItemClickListener != null) {
			switch (v.getId()){
				case R.id.recycler_view:
					mOnItemClickListener.onItemClick(v, position);
					break;
				default:
					mOnItemClickListener.onItemClick(v, position);
					break;
			}
		}
	}

}
