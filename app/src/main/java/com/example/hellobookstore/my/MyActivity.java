package com.example.hellobookstore.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hellobookstore.ItemView;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends AppCompatActivity {

	public static final String TAG = "MyActivity";

	@BindView(R.id.id)
	ItemView v_id;
	@BindView(R.id.name)
	ItemView v_name;
	@BindView(R.id.password)
	ItemView v_password;
	@BindView(R.id.email)
	ItemView v_email;
	@BindView(R.id.toolbar)
	Toolbar v_toolbar;
	private SharedPreferences pref;
	List<User> users;
	String username;
	private String name;
	private String password;
	private String email;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		ButterKnife.bind(this);

		loadData();
	}

	private void loadData() {
		pref = getSharedPreferences("login_info", MODE_PRIVATE);
		username = pref.getString("username", "");
		users = DataSupport.where("username=?", username).find(User.class);
		if (users.size() == 1) {
			name = users.get(0).getName();
			password = users.get(0).getPassword();
			email = users.get(0).getEmail();
			v_id.setRightText(username);
			v_name.setRightText(name);
			v_password.setRightText(password);
			v_email.setRightText(email);
		} else {
			Log.d(TAG, "数据库不存在此账号" + username);
		}
		setSupportActionBar(v_toolbar);
		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setDisplayHomeAsUpEnabled(true);
			bar.setTitle("个人信息");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					loadData();
				}
				break;
			default:
		}
	}


	@OnClick({R.id.id, R.id.name, R.id.password, R.id.email})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.id:
				break;
			case R.id.name:
				Intent intent1 = new Intent(this, ChangeNameActivity.class);
				intent1.putExtra("username", username);
				intent1.putExtra("name", name);
				startActivityForResult(intent1, 1);
				break;
			case R.id.password:
				Intent intent2 = new Intent(this, ChangePasswordActivity.class);
				intent2.putExtra("username", username);
				startActivityForResult(intent2, 1);
				break;
			case R.id.email:
				Intent intent3 = new Intent(this, ChangeEmailActivity.class);
				intent3.putExtra("username", username);
				intent3.putExtra("email", email);
				startActivityForResult(intent3, 1);
				break;
		}
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
