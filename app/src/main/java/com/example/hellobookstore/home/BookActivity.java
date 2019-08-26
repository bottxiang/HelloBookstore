package com.example.hellobookstore.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.User;
import com.example.hellobookstore.util.LoginDao;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

	private static final String TAG = "BookActivity";

	@BindView(R.id.book_image)
	ImageView bookImage;
	@BindView(R.id.book_name)
	TextView bookName;
	@BindView(R.id.book_catalog)
	TextView bookCatalog;
	@BindView(R.id.book_content)
	TextView bookContent;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.book_rented)
	TextView bookRented;
	@BindView(R.id.btn_borrow)
	Button btnBorrow;

	Book book;
	ActionBar actionBar;
	StringBuilder borrowedBookIds = new StringBuilder();

	private User user;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		loadData();

		btnBorrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bookRented.setVisibility(View.VISIBLE);
				btnBorrow.setVisibility(View.GONE);
				book.setRented(true);
				Log.e(TAG, book.getBookName() +"is Rented: " + book.isRented());
				book.update(book.getId());
				user.addBookId(book.getId());
				user.update(user.getId());
			}
		});
	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		loadData();
//		Log.e(TAG, book.getBookName() +"onRestart: " + book.isRented());
//	}

	private void loadData() {
		SharedPreferences pref = getSharedPreferences("login_info", MODE_PRIVATE);
		String username = pref.getString("username", "");
		user = LoginDao.getUser(username);

		Intent intent = getIntent();
		Long id = intent.getLongExtra("bookId", 1);
		book = DataSupport.find(Book.class, id);
		Log.e(TAG, book.getBookName() + "loadData is Rented: " + book.isRented());
		if (book.isRented()) {
			bookRented.setVisibility(View.VISIBLE);
			btnBorrow.setVisibility(View.GONE);
		} else {
			bookRented.setVisibility(View.GONE);
			btnBorrow.setVisibility(View.VISIBLE);
		}
		Glide.with(this).load(book.getImageUrl()).into(bookImage);
		bookName.setText(book.getBookName() + " id:" + book.getId());
		bookCatalog.setText(book.getBookCatalog());
		bookContent.setText(book.getBookContent());
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
