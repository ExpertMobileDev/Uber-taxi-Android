package com.automated.taxinow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.automated.taxinow.R;
import com.automated.taxinow.models.ApplicationPages;

/**
 * @author Hardik A Bhalodi
 */
public class DrawerAdapter extends BaseAdapter {
	private int images[] = { R.drawable.nav_profile, R.drawable.nav_payment,
			R.drawable.nav_support, R.drawable.nav_share };
	private String items[];
	private ViewHolder holder;
	private LayoutInflater inflater;
	ArrayList<ApplicationPages> listMenu;
	private AQuery aQuery;
	private ImageOptions imageOptions;

	public DrawerAdapter(Context context, ArrayList<ApplicationPages> listMenu) {
		// TODO Auto-generated constructor stub
		this.listMenu = listMenu;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aQuery = new AQuery(context);
		imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.ic_launcher;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMenu.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_item, parent, false);
			holder = new ViewHolder();
			holder.tvMenuItem = (TextView) convertView
					.findViewById(R.id.tvMenuItem);
			holder.ivMenuImage = (ImageView) convertView
					.findViewById(R.id.ivMenuImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			aQuery.id(holder.ivMenuImage).image(R.drawable.nav_profile);
		} else if (position == 1) {
			aQuery.id(holder.ivMenuImage).image(R.drawable.nav_payment);
		} else if (position == 2) {
			aQuery.id(holder.ivMenuImage).image(R.drawable.ub__nav_history);
		} else if (position == 3) {
			aQuery.id(holder.ivMenuImage).image(R.drawable.nav_referral);
		} else if (position == (listMenu.size() - 1)) {
			aQuery.id(holder.ivMenuImage).image(R.drawable.ub__nav_logout);
		} else {
			if (TextUtils.isEmpty(listMenu.get(position).getIcon())) {
				aQuery.id(holder.ivMenuImage).image(R.drawable.ic_launcher);
			} else {
				aQuery.id(holder.ivMenuImage).image(
						listMenu.get(position).getIcon());
			}

		}
		holder.tvMenuItem.setText(listMenu.get(position).getTitle());

		return convertView;
	}

	class ViewHolder {
		public TextView tvMenuItem;
		public ImageView ivMenuImage;
	}

}
