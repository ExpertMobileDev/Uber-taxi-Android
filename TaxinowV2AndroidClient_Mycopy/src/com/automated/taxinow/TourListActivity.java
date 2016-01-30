package com.automated.taxinow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.android.volley.VolleyError;
import com.automated.taxinow.adapter.TourAdapter;
import com.automated.taxinow.models.Tour;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author Jay Agravat
 * 
 */
public class TourListActivity extends ActionBarBaseActivitiy implements
		OnItemClickListener {
	private PreferenceHelper preferenceHelper;
	private ParseContent parseContent;
	private ImageView tvNoHistory;
	Calendar cal = Calendar.getInstance();
	private ListView lvTours;
	private ArrayList<Tour> listTour;
	private TourAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour);
		setTitle(getString(R.string.text_day_tours));
		setIconMenu(R.drawable.tour_menu_icon);
		lvTours = (ListView) findViewById(R.id.lvTours);
		lvTours.setOnItemClickListener(this);

		listTour = new ArrayList<Tour>();
		adapter = new TourAdapter(this, listTour);
		lvTours.setAdapter(adapter);

		tvNoHistory = (ImageView) findViewById(R.id.ivEmptyView);
		preferenceHelper = new PreferenceHelper(this);
		parseContent = new ParseContent(this);

		getTours();
	}

	private void getTours() {
		if (!AndyUtils.isNetworkAvailable(this)) {
			AndyUtils.showToast(
					getResources().getString(R.string.dialog_no_inter_message),
					this);
			return;
		}
		AndyUtils.showCustomProgressDialog(this,
				getResources().getString(R.string.text_getting_tours), false,
				null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.GET_TOUR + Const.Params.ID + "="
				+ preferenceHelper.getUserId() + "&" + Const.Params.TOKEN + "="
				+ preferenceHelper.getSessionToken());

		new HttpRequester(this, map, Const.ServiceCode.GET_TOUR, true, this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Tour selectedTour = (Tour) adapter.getItem(position);
		Intent intent = new Intent(TourListActivity.this,
				TourDetailsActivity.class);
		intent.putExtra(Const.Params.TOUR, selectedTour);
		startActivity(intent);
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		AndyUtils.removeCustomProgressDialog();
		switch (serviceCode) {
		case Const.ServiceCode.GET_TOUR:
			AppLog.Log("DayTourActivity", "" + response);
			listTour.clear();
			parseContent.parseTours(listTour, response);
			if (listTour.size() > 0) {
				lvTours.setVisibility(View.VISIBLE);
				tvNoHistory.setVisibility(View.GONE);
			} else {
				lvTours.setVisibility(View.GONE);
				tvNoHistory.setVisibility(View.VISIBLE);
			}
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}