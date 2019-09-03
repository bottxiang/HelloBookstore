package com.example.hellobookstore.my;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hellobookstore.ItemView;
import com.example.hellobookstore.R;
import com.example.hellobookstore.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

import static android.content.Context.MODE_PRIVATE;


public class MyFragment extends Fragment {

	public static final String TAG = "MyFragment---";

	private static final String ARG_PARAM = "param";


	View rootView;
	@BindView(R.id.h_back)
	ImageView hBack;
	@BindView(R.id.h_head)
	ImageView hHead;
	@BindView(R.id.my)
	ItemView my;
	@BindView(R.id.list)
	ItemView list;
	@BindView(R.id.user_name)
	TextView userName;
	@BindView(R.id.user_email)
	TextView userEmail;
	private SharedPreferences pref;
	private String username;
	private List<User> users;
	private boolean isGetData = false;

	public MyFragment() {
		// Required empty public constructor
	}


	public static MyFragment newInstance(String param) {
		MyFragment fragment = new MyFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_PARAM, param);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_my, container, false);
		}
		ButterKnife.bind(this, rootView);

		loadData();

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	@Nullable
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		if (enter) {

		}
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	private void loadData() {

		//设置背景磨砂效果
		Glide.with(this).load(R.drawable.head2)
				.apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
				.into(hBack);
		//设置圆形图像
		Glide.with(this).load(R.drawable.head2)
				.apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(2, getResources().getColor(R.color.colorWhite))))
				.into(hHead);
		pref = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
		username = pref.getString("username", "");
		users = DataSupport.where("username = ?", username).find(User.class);
		if (users.size() == 1) {
			userName.setText(users.get(0).getName() != null ? users.get(0).getName() : "我还没名字呢");
			userEmail.setText(users.get(0).getEmail());
		} else {
			Log.d(TAG, "数据库不存在此账号" + username);
		}
	}


	@OnClick({R.id.my, R.id.list})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.my:
				Intent intent1 = new Intent(getActivity(), MyActivity.class);
				startActivity(intent1);
				break;
			case R.id.list:
				Intent intent2 = new Intent(getActivity(), BorrowedBooksActivity.class);
				intent2.putExtra("username", username);
				startActivity(intent2);
				break;
		}
	}
}
