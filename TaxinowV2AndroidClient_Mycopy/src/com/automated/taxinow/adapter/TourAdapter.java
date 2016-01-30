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
public class TourAdapter extends BaseAdapter {
	private String items[];
	private ViewHolder holder;
	private LayoutInflater inflater;
	ArrayList<Tour> listTour;
	private AQuery aQuery;
	private ImageOptions imageOptions;

	public TourAdapter(Context context, ArrayList<Tour> listTour) {
		this.listTour = listTour;
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
		return listTour.size();
	}

	@Override
	public Object getItem(int position) {
		return listTour.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tour_item, parent, false);
			holder = new ViewHolder();
			holder.tvTourName = (TextView) convertView
					.findViewById(R.id.tvTourName);
			holder.ivTourImage = (ImageView) convertView
					.findViewById(R.id.ivTourImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Tour tour = listTour.get(position);

		holder.tvTourName.setText(tour.getTourName());
		if (TextUtils.isEmpty(tour.getTourImage())) {
			aQuery.id(holder.ivTourImage).image(R.drawable.ic_launcher);
		} else {
			aQuery.id(holder.ivTourImage).image(tour.getTourImage());
		}
		return convertView;
	}

	class ViewHolder {
		public TextView tvTourName;
		public ImageView ivTourImage;
	}
}
