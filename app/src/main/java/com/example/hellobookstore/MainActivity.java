package com.example.hellobookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	TabLayout tabLayout;
	ViewPager viewPager;

	List<String> titles = new ArrayList<>();
	List<Fragment> fragments = new ArrayList<>();

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
		titles.add("书籍分类列表");
		titles.add("个人中心");
		fragments.add(HomeFragment.newInstance("One"));
		fragments.add(HomeFragment.newInstance("Two"));
		fragments.add(HomeFragment.newInstance("Three"));

		ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager(), titles, fragments);
		viewPager.setAdapter(viewPagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

		setTabIcon();

	}

	private void setTabIcon() {
		tabLayout.getTabAt(0).setCustomView(getTabView(0));
		tabLayout.getTabAt(1).setCustomView(getTabView(1));
		tabLayout.getTabAt(2).setCustomView(getTabView(2));
	}

	private View getTabView(int position) {
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tab_item, null);
		ImageView tab_icon = (ImageView) view.findViewById(R.id.tab_icon);
		TextView tab_text = (TextView) view.findViewById(R.id.tab_text);
		tab_icon.setImageResource(icons[position]);
		tab_text.setText(titles.get(position));
		Log.e("xxx", titles.get(position));
		return view;
	}

}
