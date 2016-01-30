package com.automated.taxinow.adapter;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.automated.taxinow.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomPagerAdapter extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;
	private ArrayList<String> mResources;
	private AQuery aQuery;
	private ImageOptions imageOptions;

	public CustomPagerAdapter(Context context, ArrayList<String> mResources) {
		mContext = context;
		this.mResources = mResources;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aQuery = new AQuery(context);
		imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.bg_splash;
	}

	@Override
	public int getCount() {
		return mResources.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View itemView = mLayoutInflater.inflate(R.layout.pager_item, container,
				false);
		ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
		if (TextUtils.isEmpty(mResources.get(position))) {
			aQuery.id(imageView).image(R.drawable.bg_splash);
		} else {
			aQuery.id(imageView).image(mResources.get(position));
		}
		container.addView(itemView);
		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((LinearLayout) object);
	}
}