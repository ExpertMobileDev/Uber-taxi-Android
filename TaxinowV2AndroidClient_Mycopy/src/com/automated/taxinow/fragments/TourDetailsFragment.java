package com.automated.taxinow.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.automated.taxinow.R;
import com.automated.taxinow.adapter.CustomPagerAdapter;
import com.automated.taxinow.utils.Const;
import com.viewpagerindicator.CirclePageIndicator;

public class TourDetailsFragment extends TourBaseFragment {
	private View view;
	private CustomPagerAdapter mCustomPagerAdapter;
	private ViewPager mViewPager;
	private CirclePageIndicator imageIndicator;
	private TextView tvTourDetails;
	private Bundle args;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_tour_details, container,
				false);
		
		args = getArguments();
		tvTourDetails = (TextView) view.findViewById(R.id.tvTourDetails);

		tvTourDetails.setText(args.getString(Const.Params.TOUR_DESC, ""));
		mCustomPagerAdapter = new CustomPagerAdapter(getActivity(),
				args.getStringArrayList(Const.Params.TOUR_IMAGES));
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		mViewPager.setAdapter(mCustomPagerAdapter);

		imageIndicator = (CirclePageIndicator) view
				.findViewById(R.id.indicator);
		imageIndicator.setViewPager(mViewPager);

		return view;
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	protected boolean isValidate() {
		return false;
	}
}
