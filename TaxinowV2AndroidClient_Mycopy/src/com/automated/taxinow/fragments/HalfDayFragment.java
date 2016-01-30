package com.automated.taxinow.fragments;

import java.util.ArrayList;

import com.automated.taxinow.R;
import com.automated.taxinow.models.PTour;
import com.automated.taxinow.utils.Const;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class HalfDayFragment extends TourBaseFragment {

	private View view;
	private Bundle args;
	private TextView tvHDayHightLight;
	private TextView tvHdayCommonPrice;
	private TableLayout tblHdayPrivateTours;
	private ArrayList<PTour> halfDayPriceList;
	private TableRow tblRow;
	private PTour pTour;
	private TextView tvPrivatePrice;
	private TextView tvPrivatePerson;
	private TextView tvMorningDTime;
	private TextView tvMorningRTime;
	private TextView tvAfterDTime;
	private TextView tvAfterRtime;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_halfday, container, false);
		args = getArguments();
		halfDayPriceList = (ArrayList<PTour>) args
				.getSerializable(Const.Params.HALF_DAY_PRIVATE_PRICE);
		if (halfDayPriceList.size() == 0) {
			view.findViewById(R.id.ivEmptyView).setVisibility(View.VISIBLE);
			view.findViewById(R.id.layoutFullDay).setVisibility(View.GONE);
			activity.btnBookTour.setVisibility(View.GONE);
			return view;
		}
		activity.btnBookTour.setVisibility(View.VISIBLE);
		tvHDayHightLight = (TextView) view.findViewById(R.id.tvHDayHightLight);
		tvHdayCommonPrice = (TextView) view
				.findViewById(R.id.tvHdayCommonPrice);
		tblHdayPrivateTours = (TableLayout) view
				.findViewById(R.id.tblHdayPrivateTours);
		tvMorningDTime = (TextView) view.findViewById(R.id.tvMorningDTime);
		tvMorningRTime = (TextView) view.findViewById(R.id.tvMorningRTime);
		tvAfterDTime = (TextView) view.findViewById(R.id.tvAfterDTime);
		tvAfterRtime = (TextView) view.findViewById(R.id.tvAfterRtime);

		tvHDayHightLight.setText("- "
				+ args.getString(Const.Params.HALF_DAY_HIGHLIGHT).replace(",",
						"\n- "));
		tvHdayCommonPrice.setText("R"
				+ args.getDouble(Const.Params.HALF_DAY_COMMON_PRICE));
		tvMorningDTime.setText(args
				.getString(Const.Params.HALF_DAY_MORNING_DTIME));
		tvMorningRTime.setText(args
				.getString(Const.Params.HALF_DAY_MORNING_RTIME));
		tvAfterDTime.setText(args.getString(Const.Params.HALF_DAY_AFTER_DTIME));
		tvAfterRtime.setText(args.getString(Const.Params.HALF_DAY_AFTER_RTIME));

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/OPENSANS-LIGHT.ttf");
		for (int i = 0; i < halfDayPriceList.size(); i++) {
			LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			tvParams.setMargins(
					(int) getResources().getDimension(R.dimen.dimen_fp_margin),
					0, 0,
					(int) getResources().getDimension(R.dimen.dimen_fp_margin));

			tblRow = new TableRow(getActivity());
			pTour = halfDayPriceList.get(i);
			tvPrivatePrice = new TextView(getActivity());
			tvPrivatePrice.setBackground(getResources().getDrawable(
					R.drawable.comman_tour_box));
			tvPrivatePrice.setTextColor(getResources().getColor(
					R.color.tour_text_gray));
			tvPrivatePrice.setTextSize(16);
			tvPrivatePrice.setGravity(Gravity.CENTER);
			tvPrivatePrice.setText("R" + pTour.getpTourPrice());
			tvPrivatePrice.setTypeface(font);

			tvPrivatePerson = new TextView(getActivity());
			tvPrivatePerson.setBackground(getResources().getDrawable(
					R.drawable.comman_tour_box));
			tvPrivatePerson.setTextColor(getResources().getColor(
					R.color.tour_text_gray));
			tvPrivatePerson.setTextSize(16);
			tvPrivatePerson.setGravity(Gravity.CENTER);
			tvPrivatePerson.setText(pTour.getpTourPerson() + " Person");
			tvPrivatePerson.setTypeface(font);
			tvPrivatePerson.setLayoutParams(tvParams);

			tblRow.addView(tvPrivatePrice);
			tblRow.addView(tvPrivatePerson);
			tblHdayPrivateTours.addView(tblRow);
		}
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
