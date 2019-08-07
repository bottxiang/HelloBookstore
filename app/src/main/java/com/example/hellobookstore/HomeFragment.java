package com.example.hellobookstore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends Fragment {

	private static final String ARG_PARAM = "param";

	private String mParam1;
	private String mParam2;

	public HomeFragment() {
		// Required empty public constructor
	}


	public static HomeFragment newInstance(String param) {
		HomeFragment fragment = new HomeFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_PARAM, param);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		TextView textView = (TextView) view.findViewById(R.id.frag);
		textView.setText(getArguments().getString(ARG_PARAM, "Default"));
		return view;
	}
}
