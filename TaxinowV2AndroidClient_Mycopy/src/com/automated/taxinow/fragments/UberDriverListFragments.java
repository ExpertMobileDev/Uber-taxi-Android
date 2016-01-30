/**
 * 
 */
package com.automated.taxinow.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.automated.taxinow.R;
import com.automated.taxinow.adapter.DriverListAdapter;
import com.automated.taxinow.interfaces.OnProgressCancelListener;
import com.automated.taxinow.models.Driver;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Hardik A Bhalodi
 * 
 */
public class UberDriverListFragments extends UberBaseFragment implements
		OnItemClickListener, OnProgressCancelListener {
	private ListView listViewDriver;
	private DriverListAdapter adapter;
	private ArrayList<Driver> listDriver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View vDriverList = inflater.inflate(R.layout.fragment_driver_list,
				container, false);
		vDriverList.findViewById(R.id.listViewDriver);
		listViewDriver = (ListView) vDriverList
				.findViewById(R.id.listViewDriver);
		listViewDriver.setOnItemClickListener(this);
		return vDriverList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		adapter = new DriverListAdapter(activity, listDriver);
		listViewDriver.setAdapter(adapter);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragment#isValidate()
	 */
	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		// TODO Auto-generated method stub

	}

	private void createRequest(LatLng driverLatLng) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		AndyUtils.showCustomProgressDialog(activity,
				getString(R.string.text_contacting), true, this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.CREATE_REQUEST);
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(activity).getSessionToken());
		map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
		map.put(Const.Params.LATITUDE, String.valueOf(driverLatLng.latitude));
		map.put(Const.Params.LONGITUDE, String.valueOf(driverLatLng.longitude));
		map.put(Const.Params.SCHEDULE_ID, "1");
		map.put(Const.Params.DISTANCE, "0");
		new HttpRequester(activity, map, Const.ServiceCode.CREATE_REQUEST, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragment#onTaskCompleted(java.lang.String,
	 * int)
	 */
	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		// TODO Auto-generated method stub
		switch (serviceCode) {
		case Const.ServiceCode.CREATE_REQUEST:
			if (new ParseContent(activity).isSuccess(response)) {

//				System.out.println("=========>>>>> " + response);
			}
			break;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.interfaces.OnProgressCancelListener#onProgressCancel()
	 */
	@Override
	public void onProgressCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}
