package com.example.hellobookstore.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.hellobookstore.R;
import com.example.hellobookstore.db.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity {

	@BindView(R.id.password_new)
	EditText passwordNew;
	@BindView(R.id.password_new_repeat)
	EditText passwordNewRepeat;
	@BindView(R.id.btn_confirm)
	Button btnConfirm;
	@BindView(R.id.toolbar)
	Toolbar v_toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		ButterKnife.bind(this);


		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		setSupportActionBar(v_toolbar);
		ActionBar bar = getSupportActionBar();
		if (bar != null) {
			bar.setDisplayHomeAsUpEnabled(true);
			bar.setTitle("修改密码");
		}
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String password = passwordNew.getText().toString().trim();
				String passwordRepeat = passwordNewRepeat.getText().toString().trim();
				if (isRequiredPassword(password, passwordRepeat)) {
					User user = new User();
					user.setPassword(password);
					user.updateAll("username = ?", username);
					setResult(RESULT_OK);
					finish();
				}
			}
		});
	}

	private boolean isRequiredPassword(String password, String passwordRepeat) {
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
		//两次密码不同
		if (!password.equals(passwordRepeat)) {
			showAlertDialog("两次输入的密码不一致，请再次确认！");
			return false;
		} else if (!password.matches(regex)) {
			showAlertDialog("密码至少8位，且应包含字母和数字！");
			return false;
		}
		return true;
	}


	private void showAlertDialog(String s) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswordActivity.this);
		dialog.setTitle("警告");
		dialog.setMessage(s);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		dialog.show();
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
