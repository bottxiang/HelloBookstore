package com.example.hellobookstore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.hellobookstore.home.HomeFragment;
import com.example.hellobookstore.my.MyFragment;
import com.example.hellobookstore.type.TypeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	List<String> titles = new ArrayList<>();
	List<Fragment> fragments = new ArrayList<>();


	TabLayout tabLayout;
	ViewPager viewPager;

	ImageView tabIcon;
	TextView tabText;


	private int[] icons = {
			R.color.selector_tab1,
			R.color.selector_tab2,
			R.color.selector_tab3,
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		initData();

	}

	private void initView() {
		tabLayout = (TabLayout) findViewById(R.id.tab);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
	}

	private void initData() {
		titles.add("首页");
//		titles.add("书籍分类列表");
		titles.add("个人中心");
		fragments.add(HomeFragment.newInstance("One"));
//		fragments.add(TypeFragment.newInstance("Two"));
		fragments.add(MyFragment.newInstance("Three"));

		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

		setTabIcon();

	}

	private void setTabIcon() {
		tabLayout.getTabAt(0).setCustomView(getTabView(0));
//		tabLayout.getTabAt(1).setCustomView(getTabView(1));
		tabLayout.getTabAt(1).setCustomView(getTabView(1));
	}

	private View getTabView(int position) {
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, null);
		tabIcon = (ImageView) view.findViewById(R.id.tab_icon);
		tabText = (TextView) view.findViewById(R.id.tab_text);

		tabIcon.setImageResource(icons[position]);
		tabText.setText(titles.get(position));
		Log.e("xxx", titles.get(position));
		return view;
	}

}
