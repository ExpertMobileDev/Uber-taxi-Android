/**
 * 
 */
package com.automated.taxinow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.android.volley.VolleyError;
import com.automated.taxinow.adapter.DrawerAdapter;
import com.automated.taxinow.models.ApplicationPages;
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
public class HelpActivity extends ActionBarBaseActivitiy implements
		OnItemClickListener {
	private PreferenceHelper preferenceHelper;
	private ParseContent parseContent;
	private ImageView tvNoHistory;
	Calendar cal = Calendar.getInstance();
	private ListView lvHelpMenu;
	private ArrayList<ApplicationPages> listMenu;
	private DrawerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setIconMenu(R.drawable.ub__nav_history);
		setTitle(getString(R.string.text_help));
		setIcon(R.drawable.back);
		lvHelpMenu = (ListView) findViewById(R.id.lvHelpMenu);
		lvHelpMenu.setOnItemClickListener(this);

		listMenu = new ArrayList<ApplicationPages>();
		adapter = new DrawerAdapter(this, listMenu);
		lvHelpMenu.setAdapter(adapter);

		tvNoHistory = (ImageView) findViewById(R.id.ivEmptyView);
		preferenceHelper = new PreferenceHelper(this);
		parseContent = new ParseContent(this);

		getHelpMenus();
	}

	private void getHelpMenus() {
		if (!AndyUtils.isNetworkAvailable(this)) {
			AndyUtils.showToast(
					getResources().getString(R.string.dialog_no_inter_message),
					this);
			return;
		}
		AndyUtils.showCustomProgressDialog(this,
				getResources().getString(R.string.text_please_wait), false,
				null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.GET_PAGES + "?user_type=0");
		new HttpRequester(this, map, Const.ServiceCode.GET_PAGES, true, this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent(HelpActivity.this, MenuDescActivity.class);
		intent.putExtra(Const.Params.TITLE, listMenu.get(position).getTitle());
		intent.putExtra(Const.Params.CONTENT, listMenu.get(position).getData());
		startActivity(intent);
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		AndyUtils.removeCustomProgressDialog();
		switch (serviceCode) {
		case Const.ServiceCode.GET_PAGES:
			AppLog.Log("HelpActivity", "" + response);
			listMenu.clear();
			parseContent.parsePages(listMenu, response);
			if (listMenu.size() > 0) {
				lvHelpMenu.setVisibility(View.VISIBLE);
				tvNoHistory.setVisibility(View.GONE);
			} else {
				lvHelpMenu.setVisibility(View.GONE);
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
