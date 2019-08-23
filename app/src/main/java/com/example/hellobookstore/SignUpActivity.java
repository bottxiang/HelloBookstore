package com.example.hellobookstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellobookstore.db.User;
import com.example.hellobookstore.util.LoginDao;
import com.example.hellobookstore.util.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {



	@BindView(R.id.username_signup)
	EditText usernameSignup;
	@BindView(R.id.password_signup)
	EditText passwordSignup;
	@BindView(R.id.password_signup_repeat)
	EditText passwordSignupRepeat;
	@BindView(R.id.email_signup)
	EditText emailSignup;
	@BindView(R.id.btn_submit)
	Button btnSubmit;
	@BindView(R.id.btn_cancel)
	Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		ButterKnife.bind(this);
	}

	@OnClick({R.id.btn_submit, R.id.btn_cancel})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.btn_submit:
				String username = usernameSignup.getText().toString();
				String password = passwordSignup.getText().toString();
				String passwordRepeat = passwordSignupRepeat.getText().toString();
				String email = emailSignup.getText().toString();
				if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordRepeat) || TextUtils.isEmpty(email)) {
					showAlertDialog("请填写完整的注册信息！");
				} else if(LoginDao.isAccountExisted(username)) {
					showAlertDialog("帐号已存在！");
				} else if(isRequiredPassword(password, passwordRepeat)) {
					LoginDao.addUser(new User(username, password, email));
					Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_LONG);
					Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
					this.startActivity(intent);
					this.finish();
				}
				break;
			case R.id.btn_cancel:
				Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
				this.startActivity(intent);
				this.finish();
				break;
		}
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
		AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
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
}
