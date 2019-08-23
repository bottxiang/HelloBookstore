package com.example.hellobookstore;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ItemView extends LinearLayout {

	View dividerTop;

	ImageView leftIcon;

	TextView leftText;

	TextView rightText;

	ImageView rightArrow;

	LinearLayout llRoot;

	View dividerBottom;

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.item_view, this);
		leftIcon = findViewById(R.id.left_icon);
		leftText = findViewById(R.id.left_text);
		rightText = findViewById(R.id.right_text);
		rightArrow = findViewById(R.id.right_arrow);
		dividerTop = findViewById(R.id.divider_top);
		dividerBottom = findViewById(R.id.divider_bottom);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
		leftIcon.setImageDrawable(ta.getDrawable(R.styleable.ItemView_left_icon));
		leftText.setText(ta.getString(R.styleable.ItemView_left_text));
		rightText.setText(ta.getString(R.styleable.ItemView_right_text));
		dividerTop.setVisibility(ta.getBoolean(R.styleable.ItemView_show_top_line, true) ? View.VISIBLE : View.INVISIBLE);
		dividerBottom.setVisibility(ta.getBoolean(R.styleable.ItemView_show_bottom_line, true) ? View.VISIBLE : View.INVISIBLE);
		rightArrow.setVisibility(ta.getBoolean(R.styleable.ItemView_right_arrow, true) ? View.VISIBLE : View.INVISIBLE);
		ta.recycle();
	}

	public void setLeftText(String s) {
		leftText.setText(s);
	}
	public void setRightText(String s) {
		rightText.setText(s);
	}
}
