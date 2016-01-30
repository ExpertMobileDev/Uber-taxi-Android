package com.automated.taxinow.fragments;

import java.util.ArrayList;

import com.automated.taxinow.R;
import com.automated.taxinow.models.PTour;
import com.automated.taxinow.utils.Const;
import com.google.android.gms.drive.internal.v;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class FullDayFragment extends TourBaseFragment {

	private View view;
	private Bundle args;
	private TextView tvFDayHightLight;
	private TextView tvFdayCommonPrice;
	private TableLayout tblFdayPrivateTours;
	private ArrayList<PTour> fullDayPriceList = new ArrayList<PTour>();
	private PTour pTour;
	private TextView tvPrivatePrice;
	private TableRow tblRow;
	private TextView tvPrivatePerson;
	private TextView tvFullDayDTime;
	private TextView tvFullDayRTime;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_fullday, container, false);
		args = getArguments();
		fullDayPriceList = (ArrayList<PTour>) args
				.getSerializable(Const.Params.FULL_DAY_PRIVATE_PRICE);
		if (fullDayPriceList.size() == 0) {
			view.findViewById(R.id.ivEmptyView).setVisibility(View.VISIBLE);
			view.findViewById(R.id.layoutFullDay).setVisibility(View.GONE);
			activity.btnBookTour.setVisibility(View.GONE);
			return view;
		}
		activity.btnBookTour.setVisibility(View.VISIBLE);
		tvFDayHightLight = (TextView) view.findViewById(R.id.tvFDayHightLight);
		tvFdayCommonPrice = (TextView) view
				.findViewById(R.id.tvFdayCommonPrice);
		tblFdayPrivateTours = (TableLayout) view
				.findViewById(R.id.tblFdayPrivateTours);
		tvFullDayDTime = (TextView) view.findViewById(R.id.tvFullDayDTime);
		tvFullDayRTime = (TextView) view.findViewById(R.id.tvFullDayRTime);
		tvFullDayDTime.setText(args.getString(Const.Params.FULL_DAY_DTIME));
		tvFullDayRTime.setText(args.getString(Const.Params.FULL_DAY_RTIME));
		tvFDayHightLight.setText("- "
				+ args.getString(Const.Params.FULL_DAY_HIGHLIGHT).replace(",",
						"\n- "));
		tvFdayCommonPrice.setText("R"
				+ args.getDouble(Const.Params.FULL_DAY_COMMON_PRICE));

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/OPENSANS-LIGHT.ttf");
		for (int i = 0; i < fullDayPriceList.size(); i++) {
			LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			tvParams.setMargins(
					(int) getResources().getDimension(R.dimen.dimen_fp_margin),
					0, 0,
					(int) getResources().getDimension(R.dimen.dimen_fp_margin));

			tblRow = new TableRow(getActivity());
			pTour = fullDayPriceList.get(i);
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
			tblFdayPrivateTours.addView(tblRow);
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
