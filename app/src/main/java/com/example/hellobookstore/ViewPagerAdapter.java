package com.example.hellobookstore;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<String> titles;
	private List<Fragment> fragments;
	public ViewPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
		super(fm);
		this.titles = titles;
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		super.destroyItem(container, position, object);
	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position % fragments.size());
	}


	@Override
	public int getCount() {
		return fragments.size();
	}

}
