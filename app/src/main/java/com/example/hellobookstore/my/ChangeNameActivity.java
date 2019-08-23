package com.example.hellobookstore.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hellobookstore.R;
import com.example.hellobookstore.db.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeNameActivity extends AppCompatActivity {

	@BindView(R.id.et_name)
	EditText etName;
	@BindView(R.id.btn_confirm)
	Button btnConfirm;
	@BindView(R.id.toolbar)
	Toolbar v_toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_name);
		ButterKnife.bind(this);

		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		String name = intent.getStringExtra("name");
		etName.setText(name);
		setSupportActionBar(v_toolbar);
		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setDisplayHomeAsUpEnabled(true);
			bar.setTitle("修改昵称");
		}

		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				User user = new User();
				user.setName(etName.getText().toString().trim());
				user.updateAll("username = ?", username);
				setResult(RESULT_OK);
				finish();
			}
		});
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
