package com.example.hellobookstore.my;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellobookstore.LoginActivity;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.Book;
import com.example.hellobookstore.db.User;
import com.example.hellobookstore.home.BookAdapter;
import com.example.hellobookstore.util.LoginDao;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BorrowedBooksActivity extends AppCompatActivity {

	private static final String TAG = "BorrowedBooksActivity";


	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;
	private List<Book> borrowedBooks;
	private BorrowedBookAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrowed_books);
		ButterKnife.bind(this);

		loadData();

		setSupportActionBar(toolbar);
		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void loadData() {

		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		User user = LoginDao.getUser(username);

		//String[] borrowedBookIds = user.getRentedBookString().split(" ");
		String[] borrowedBookIds = {"1", "2", "3"};
		borrowedBooks = getBooks(borrowedBookIds);
		Log.e(TAG, "username:" + user.getUsername() + " bookIds:" + borrowedBookIds[0]);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		adapter = new BorrowedBookAdapter(borrowedBooks);
		recyclerView.setAdapter(adapter);
	}

	private List<Book> getBooks(String[] borrowedBookIds) {
		List<Book> books = new ArrayList<>();
		for (String id : borrowedBookIds) {
			Book book = DataSupport.find(Book.class, Integer.valueOf(id));
			books.add(book);
		}
		return books;
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
