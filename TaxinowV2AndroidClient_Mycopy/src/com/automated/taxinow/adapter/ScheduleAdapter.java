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
import com.automated.taxinow.models.Tour;

/**
 * @author Jay Agravat
 */
public class ScheduleAdapter extends BaseAdapter {
	private ViewHolder holder;
	private LayoutInflater inflater;
	ArrayList<String> schedule;
	private ArrayList<String> time;

	public ScheduleAdapter(Context context, ArrayList<String> schedule,
			ArrayList<String> time) {
		this.schedule = schedule;
		this.time = time;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return schedule.size();
	}

	@Override
	public Object getItem(int position) {
		return schedule.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tour_schedule_item, parent,
					false);
			holder = new ViewHolder();
			holder.tvSpnItem = (TextView) convertView
					.findViewById(R.id.tvSpnItem);
			holder.tvSpnTime = (TextView) convertView
					.findViewById(R.id.tvSpnTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvSpnItem.setText(schedule.get(position));
		holder.tvSpnTime.setText(time.get(position));
		return convertView;
	}

	class ViewHolder {
		public TextView tvSpnItem, tvSpnTime;
	}
}
