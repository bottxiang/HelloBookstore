package com.example.hellobookstore.util;

import android.util.Log;

import com.example.hellobookstore.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginDao {

	public static final String TAG = "LoginDao";

	public static User login(User user) {
		String username = user.getUsername();
		String password = user.getPassword();
		List<User> list = DataSupport.where("username=?", username).find(User.class);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				User fullUser = list.get(i);
				String passwd = fullUser.getPassword();
				if (passwd.equals(password)) {
					return fullUser;
				}
			}
		}
		//帐号不存在或密码错误
		return null;
	}

	//添加用户
	public static void addUser(User user) {
		user.save();
	}

	//判断当前账号是否已经注册
	public static boolean isAccountExisted(String account){
		Log.d(TAG, account);
		List<User> users = DataSupport.where("username=?", account).find(User.class);
		return users.size() > 0;
	}

	//根据username返回user对象
	public static User getUser(String username) {
		List<User> users = DataSupport.where("username=?", username).find(User.class);
		if (users.size() == 1) {
			return users.get(0);
		}
		return null;
	}
}
