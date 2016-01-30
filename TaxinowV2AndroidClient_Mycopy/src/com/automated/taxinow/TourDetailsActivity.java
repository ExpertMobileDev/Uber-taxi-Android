package com.automated.taxinow;

import java.util.ArrayList;

import com.android.volley.VolleyError;
import com.automated.taxinow.fragments.FullDayFragment;
import com.automated.taxinow.fragments.HalfDayFragment;
import com.automated.taxinow.fragments.TourDetailsFragment;
import com.automated.taxinow.models.PTour;
import com.automated.taxinow.models.Tour;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Jay Agravat
 */
public class TourDetailsActivity extends ActionBarBaseActivitiy {
	public PreferenceHelper pHelper;
	public ParseContent pContent;
	private TextView tvTourDesc;
	private TextView tvTourFullDay;
	private TextView tvTourHalfDay;
	private Tour tour;
	private Bundle detailBundle;
	private Bundle fullDayBundle;
	private ArrayList<PTour> pTourList;
	private ArrayList<PTour> fullDayPriceList = new ArrayList<PTour>();
	private ArrayList<PTour> halfDayPriceList = new ArrayList<PTour>();
	private PTour pTour;
	private Bundle halfDayBundle;
	public Button btnBookTour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour_details);
		btnActionMenu.setVisibility(View.GONE);
		tour = (Tour) getIntent().getSerializableExtra(Const.Params.TOUR);
		Typeface font = Typeface.createFromAsset(getAssets(),
				"fonts/OPENSANS-REGULAR.ttf");
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvTitle
				.getLayoutParams();
		params.setMargins(
				(int) getResources().getDimension(R.dimen.dimen_fp_margin), 0,
				0, 0);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		tvTitle.setLayoutParams(params);
		tvTitle.setTypeface(font);
		setTitle(tour.getTourName());
		pHelper = new PreferenceHelper(this);
		pContent = new ParseContent(this);
		tvTourDesc = (TextView) findViewById(R.id.tvTourDesc);
		tvTourFullDay = (TextView) findViewById(R.id.tvTourFullDay);
		tvTourHalfDay = (TextView) findViewById(R.id.tvTourHalfDay);
		btnBookTour = (Button) findViewById(R.id.btnBookTour);

		tvTourDesc.setOnClickListener(this);
		tvTourFullDay.setOnClickListener(this);
		tvTourHalfDay.setOnClickListener(this);
		btnBookTour.setOnClickListener(this);

		tvTourDesc.setSelected(true);

		detailBundle = new Bundle();
		detailBundle.putString(Const.Params.TOUR_DESC, tour.getTourDesc());
		detailBundle.putStringArrayList(Const.Params.TOUR_IMAGES,
				tour.getImgLst());

		fullDayBundle = new Bundle();
		fullDayBundle.putString(Const.Params.FULL_DAY_HIGHLIGHT,
				tour.getFullDayTour());
		fullDayBundle.putDouble(Const.Params.FULL_DAY_COMMON_PRICE,
				tour.getFullDayPrice());
		fullDayBundle.putString(Const.Params.FULL_DAY_DTIME,
				tour.getfDayDTime());
		fullDayBundle.putString(Const.Params.FULL_DAY_RTIME,
				tour.getfDayRTime());

		halfDayBundle = new Bundle();
		halfDayBundle.putString(Const.Params.HALF_DAY_HIGHLIGHT,
				tour.getHalfDayTour());
		halfDayBundle.putDouble(Const.Params.HALF_DAY_COMMON_PRICE,
				tour.getHalfDayPrice());
		halfDayBundle.putString(Const.Params.HALF_DAY_MORNING_DTIME,
				tour.getMorningDTime());
		halfDayBundle.putString(Const.Params.HALF_DAY_MORNING_RTIME,
				tour.getMorningRTime());
		halfDayBundle.putString(Const.Params.HALF_DAY_AFTER_DTIME,
				tour.getAfterDTime());
		halfDayBundle.putString(Const.Params.HALF_DAY_AFTER_RTIME,
				tour.getAfterRTime());

		// Filter Private tours list
		pTourList = tour.getpTour();
		for (int i = 0; i < pTourList.size(); i++) {
			pTour = pTourList.get(i);
			if (pTour.getpTourType() == Const.FULL_DAY_TOUR)
				fullDayPriceList.add(pTour);
			else
				halfDayPriceList.add(pTour);
		}
		fullDayBundle.putSerializable(Const.Params.FULL_DAY_PRIVATE_PRICE,
				fullDayPriceList);
		halfDayBundle.putSerializable(Const.Params.HALF_DAY_PRIVATE_PRICE,
				halfDayPriceList);

		gotoTourDetailsFragment();
	}

	public void gotoTourDetailsFragment() {
		btnBookTour.setVisibility(View.GONE);
		TourDetailsFragment frag = new TourDetailsFragment();
		frag.setArguments(detailBundle);
		addFragment(frag, false, false, Const.FRAGMENT_TOUR_DETAILS);
	}

	public void gotoFullDayFragment() {
		btnBookTour.setText(getResources().getString(R.string.book_full_day));
		FullDayFragment frag = new FullDayFragment();
		frag.setArguments(fullDayBundle);
		addFragment(frag, false, false, Const.FRAGMENT_FULLDAY);
	}

	public void gotoHalfDayFragment() {
		btnBookTour.setText(getResources().getString(R.string.book_half_day));
		HalfDayFragment frag = new HalfDayFragment();
		frag.setArguments(halfDayBundle);
		addFragment(frag, false, false, Const.FRAGMENT_HALFDAY);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		if (v == tvTourDesc || v == tvTourFullDay || v == tvTourHalfDay) {
			tvTourDesc.setSelected(false);
			tvTourFullDay.setSelected(false);
			tvTourHalfDay.setSelected(false);
			v.setSelected(true);
		}
		switch (v.getId()) {
		case R.id.tvTourDesc:
			gotoTourDetailsFragment();
			break;
		case R.id.tvTourFullDay:
			gotoFullDayFragment();
			break;
		case R.id.tvTourHalfDay:
			gotoHalfDayFragment();
			break;
		case R.id.btnBookTour:
			Intent intent = new Intent(TourDetailsActivity.this,
					BookingActivity.class);
			intent.putExtra(Const.Params.HALF_DAY, halfDayBundle);
			intent.putExtra(Const.Params.FULL_DAY, fullDayBundle);
			intent.putExtra(Const.Params.TOUR_ID, tour.getTourId());
			intent.putExtra(Const.Params.TOUR_NAME, tour.getTourName());
			startActivity(intent);
			break;
		case R.id.btnActionNotification:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		return false;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}
