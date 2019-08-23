package com.example.hellobookstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellobookstore.db.User;
import com.example.hellobookstore.util.LoginDao;
import com.example.hellobookstore.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

//CompoundButton.OnCheckedChangeListener
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

	public static final String TAG = "LoginActivity";

	SharedPreferences.Editor editor;
	SharedPreferences pref;

	@BindView(R.id.username_input)
	EditText usernameInput;
	@BindView(R.id.password_input)
	EditText passwordInput;
	@BindView(R.id.btn_login)
	Button btnLogin;
	@BindView(R.id.btn_register)
	Button btnRegister;
	@BindView(R.id.ck_remember)
	CheckBox ckRemember;
	@BindView(R.id.forget)
	TextView forget;
	private boolean isRemember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ButterKnife.bind(this);

		pref = getSharedPreferences("login_info", MODE_PRIVATE);
		//如果帐号密码已存储
		if (pref.getBoolean("remember", false)) {
			String username = pref.getString("username", "");
			String password = pref.getString("password", "");
			usernameInput.setText(username);
			passwordInput.setText(password);
			ckRemember.setChecked(true);
		}

		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		forget.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_login:
				Log.d(TAG, "click login");
				//注意：如果输入为空，得到的是空字符串，而非null
				String username = usernameInput.getText().toString();
				String password = passwordInput.getText().toString();

				if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
					showAlertDialog("帐号或密码为空,请再次确认！");
				} else {
					User user = new User(username, password);
					user = LoginDao.login(user);
					if (user != null) {
						isRemember = ckRemember.isChecked();
						editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
						if (isRemember) {
							editor.putString("username", username);
							editor.putString("password", password);
							editor.putBoolean("remember", isRemember);
						} else {
							editor.clear();
						}
						editor.apply();
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
					} else {
						showAlertDialog("帐号不存在或密码错误,请再次确认！");
					}
				}
				break;
			case R.id.btn_register:
				Log.d(TAG, "click sign up");
				Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
				startActivity(intent);
				break;
			case R.id.forget:
				break;
		}

	}


	private void showAlertDialog(String s) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
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

//	@Override
//	public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//		if (isChecked) {
//			SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
//			editor.putBoolean("remember", isRemember);
//		}
//	}
}
