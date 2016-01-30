/**
 * 
 */
package com.automated.taxinow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.automated.taxinow.R;
import com.automated.taxinow.fragments.UberMapFragment;
import com.automated.taxinow.models.VehicalType;

/**
 * @author Hardik A Bhalodi
 * 
 */
public class VehicalTypeListAdapter extends BaseAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	private ArrayList<VehicalType> listVehicalType;
	private LayoutInflater inflater;
	private ViewHolder holder;
	private AQuery aQuery;
	public static int seletedPosition = 0;
	UberMapFragment mapfrag;

	public VehicalTypeListAdapter(Context context,
			ArrayList<VehicalType> listVehicalType, UberMapFragment mapfrag) {
		this.listVehicalType = listVehicalType;
		this.mapfrag = mapfrag;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listVehicalType.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_type, parent,
					false);
			holder = new ViewHolder();
			holder.tvType = (TextView) convertView.findViewById(R.id.tvType);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			holder.ivSelectService = (ImageView) convertView
					.findViewById(R.id.ivSelectService);

			// holder.viewSeprater = (View) convertView
			// .findViewById(R.id.seprateView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		aQuery = new AQuery(convertView);
		holder.tvType.setText(listVehicalType.get(position).getName() + "");
		holder.ivIcon.setTag(position);
		aQuery.id(holder.ivIcon).image(listVehicalType.get(position).getIcon(),
				true, true);
		if (listVehicalType.get(position).isSelected) {
			holder.ivSelectService.setVisibility(View.VISIBLE);
			holder.ivSelectService
					.setImageResource(R.drawable.selected_service);
		} else {
			// holder.ivSelectService.setBackgroundColor(Color.TRANSPARENT);
			holder.ivSelectService.setVisibility(View.INVISIBLE);

		}

		// if (position == listVehicalType.size() - 1) {
		// holder.viewSeprater.setVisibility(View.GONE);
		// } else {
		// holder.viewSeprater.setVisibility(View.VISIBLE);
		// }

		return convertView;
	}

	private class ViewHolder {
		TextView tvType;
		ImageView ivIcon, ivSelectService;
		// View viewSeprater;
	}

	

}
