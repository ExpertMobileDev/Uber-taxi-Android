package com.automated.taxinow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.android.volley.VolleyError;
import com.automated.taxinow.adapter.ScheduleAdapter;
import com.automated.taxinow.models.PTour;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Jay Agravat
 * 
 */
public class BookingActivity extends ActionBarBaseActivitiy implements
		OnItemSelectedListener {
	private PreferenceHelper preferenceHelper;
	private ParseContent parseContent;
	private Spinner spnTourType;
	ArrayList<String> tourType = new ArrayList<String>();
	private ArrayList<String> scheduleList = new ArrayList<String>();
	private ArrayList<String> timeList = new ArrayList<String>();
	ArrayList<String> tourPackages = new ArrayList<String>();
	ArrayList<String> personList = new ArrayList<String>();
	private ArrayAdapter<String> typeAdapter;
	private Spinner spnTourPackage;
	private ArrayAdapter<String> packageAdapter;
	private Bundle bundle;
	private Bundle fullDayBundle;
	private Bundle halfDayBundle;
	private ArrayList<PTour> fullDayPriceList;
	private ArrayList<PTour> halfDayPriceList;
	private double fullDayCommonPrice;
	private double halfDayCommonPrice;
	private Spinner spnTourSchedule;
	private ScheduleAdapter scheduleAdapter;
	private Spinner spnTourPersons;
	private ArrayAdapter<String> personAdapter;
	private double totalTarrif;
	private PTour pTour;
	private TextView tvTotalTarrif;
	private TextView txtTourDate;
	private int day;
	private int month;
	private int year;
	Calendar cal = Calendar.getInstance();
	private OnDateSetListener dateset;
	private DatePickerDialog tourDatePiker;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);
		bundle = getIntent().getExtras();
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
		setTitle(bundle.getString(Const.Params.TOUR_NAME));
		btnActionMenu.setVisibility(View.INVISIBLE);
		preferenceHelper = new PreferenceHelper(this);
		parseContent = new ParseContent(this);
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		spnTourPackage = (Spinner) findViewById(R.id.spnTourPackage);
		spnTourType = (Spinner) findViewById(R.id.spnTourType);
		spnTourSchedule = (Spinner) findViewById(R.id.spnTourSchedule);
		spnTourPersons = (Spinner) findViewById(R.id.spnTourPersons);
		tvTotalTarrif = (TextView) findViewById(R.id.tvTotalTarrif);
		txtTourDate = (TextView) findViewById(R.id.txtTourDate);

		fullDayBundle = bundle.getBundle(Const.Params.FULL_DAY);
		halfDayBundle = bundle.getBundle(Const.Params.HALF_DAY);

		fullDayPriceList = (ArrayList<PTour>) fullDayBundle
				.getSerializable(Const.Params.FULL_DAY_PRIVATE_PRICE);
		halfDayPriceList = (ArrayList<PTour>) halfDayBundle
				.getSerializable(Const.Params.HALF_DAY_PRIVATE_PRICE);

		fullDayCommonPrice = fullDayBundle
				.getDouble(Const.Params.FULL_DAY_COMMON_PRICE);
		halfDayCommonPrice = halfDayBundle
				.getDouble(Const.Params.HALF_DAY_COMMON_PRICE);

		tourPackages.add(getResources().getString(R.string.text_common_tour));
		tourPackages
				.add(getResources().getString(R.string.private_luxury_tour));
		if (fullDayPriceList.size() > 0 && halfDayPriceList.size() > 0) {
			tourType.add(getResources().getString(R.string.text_full_day_tour));
			tourType.add(getResources().getString(R.string.text_half_day_tour));
		} else if (fullDayPriceList.size() == 0) {
			tourType.add(getResources().getString(R.string.text_half_day_tour));
		} else if (halfDayPriceList.size() == 0) {
			tourType.add(getResources().getString(R.string.text_full_day_tour));
		}
		packageAdapter = new ArrayAdapter<String>(this,
				R.layout.tour_spinner_item, R.id.tvSpnItem, tourPackages);
		spnTourPackage.setAdapter(packageAdapter);
		typeAdapter = new ArrayAdapter<String>(this,
				R.layout.tour_spinner_item, R.id.tvSpnItem, tourType);
		spnTourType.setAdapter(typeAdapter);
		scheduleAdapter = new ScheduleAdapter(this, scheduleList, timeList);
		spnTourSchedule.setAdapter(scheduleAdapter);
		personAdapter = new ArrayAdapter<String>(this,
				R.layout.tour_spinner_item, R.id.tvSpnItem, personList);
		spnTourPersons.setAdapter(personAdapter);

		spnTourPackage.setOnItemSelectedListener(this);
		spnTourType.setOnItemSelectedListener(this);
		spnTourSchedule.setOnItemSelectedListener(this);
		spnTourPersons.setOnItemSelectedListener(this);
		findViewById(R.id.btnBooking).setOnClickListener(this);
		txtTourDate.setOnClickListener(this);

		dateset = new OnDateSetListener() {
			private String userDate;

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				userDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
				txtTourDate.setText(userDate);
			}
		};
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		AndyUtils.removeCustomProgressDialog();
		switch (serviceCode) {
		case Const.ServiceCode.BOOK_TOUR:
			AppLog.Log("BookingActivity", "Book tour Response : " + response);
			if (parseContent.isSuccess(response)) {
				AndyUtils
						.showToast(getString(R.string.text_tour_success), this);
			} else {
				AndyUtils.showToast(getString(R.string.text_tour_fail), this);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBooking:
			if (preferenceHelper.getDefaultCard() == 0) {
				AndyUtils.showToast(getResources().getString(R.string.no_card),
						this);
				startActivity(new Intent(this, UberViewPaymentActivity.class));
			} else {
				if (txtTourDate.getText().toString()
						.equals(getString(R.string.text_booking_date))) {
					AndyUtils.showToast(
							getString(R.string.msg_err_booking_date), this);
				} else {
					bookTour();
				}
			}
			break;
		case R.id.btnActionNotification:
			onBackPressed();
			break;
		case R.id.txtTourDate:
			tourDatePiker = new DatePickerDialog(this, dateset, year, month,
					day);
			tourDatePiker.getDatePicker().setMinDate(
					System.currentTimeMillis() - 1000);
			tourDatePiker.show();
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

	private void bookTour() {
		if (!AndyUtils.isNetworkAvailable(this)) {
			AndyUtils.showToast(
					getResources().getString(R.string.dialog_no_inter_message),
					this);
			return;
		}
		AndyUtils.showCustomProgressDialog(this,
				getResources().getString(R.string.text_booking_tours), false,
				null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.BOOK_TOUR);
		map.put(Const.Params.ID, String.valueOf(preferenceHelper.getUserId()));
		map.put(Const.Params.TOKEN,
				String.valueOf(preferenceHelper.getSessionToken()));
		map.put(Const.Params.TOUR_ID,
				String.valueOf(bundle.getInt(Const.Params.TOUR_ID)));
		map.put(Const.Params.PACKAGE, spnTourPackage.getSelectedItem()
				.toString());
		map.put(Const.Params.SCHEDULE, spnTourSchedule.getSelectedItem()
				.toString());
		map.put(Const.Params.PERSON, spnTourPersons.getSelectedItem()
				.toString());
		map.put(Const.Params.PRICE, String.valueOf(totalTarrif));
		map.put(Const.Params.TOUR_TYPE, spnTourType.getSelectedItem()
				.toString());
		map.put(Const.Params.BOOKING_DATE, txtTourDate.getText().toString());
		new HttpRequester(this, map, Const.ServiceCode.BOOK_TOUR, this);
	}

	@Override
	public void onItemSelected(AdapterView<?> view, View arg1, int arg2,
			long arg3) {
		if (view == spnTourPackage) {
			personList.clear();
			spnTourPersons.setSelection(0);
			if (spnTourPackage.getSelectedItemPosition() == 0) {
				int commonTourMaxPerson = 8;
				for (int i = 1; i <= commonTourMaxPerson; i++) {
					personList.add(i + " Person");
				}
			} else {
				if (spnTourType.getSelectedItem().toString()
						.equals(getString(R.string.text_full_day_tour))) {
					for (int i = 0; i < fullDayPriceList.size(); i++) {
						pTour = fullDayPriceList.get(i);
						personList.add("Max. " + pTour.getpTourPerson()
								+ " Person");
					}
				} else {
					for (int i = 0; i < halfDayPriceList.size(); i++) {
						pTour = halfDayPriceList.get(i);
						personList.add("Max. " + pTour.getpTourPerson()
								+ " Person");
					}
				}
			}
			personAdapter.notifyDataSetChanged();
		}
		if (view == spnTourType) {
			scheduleList.clear();
			timeList.clear();
			if (spnTourType.getSelectedItem().toString()
					.equals(getString(R.string.text_full_day_tour))) {
				scheduleList.add(getString(R.string.text_morning));
				timeList.add(getString(R.string.text_departure) + ":"
						+ fullDayBundle.getString(Const.Params.FULL_DAY_DTIME)
						+ " " + getString(R.string.text_return) + ":"
						+ fullDayBundle.getString(Const.Params.FULL_DAY_RTIME));
				if (spnTourPackage.getSelectedItemPosition() == 1) {
					personList.clear();
					for (int i = 0; i < fullDayPriceList.size(); i++) {
						pTour = fullDayPriceList.get(i);
						personList.add("Max. " + pTour.getpTourPerson()
								+ " Person");
					}
					spnTourPersons.setSelection(0);
				}
			} else {
				scheduleList.add(getString(R.string.text_morning));
				scheduleList.add(getString(R.string.text_afternoon));
				timeList.add(getString(R.string.text_departure)
						+ ":"
						+ halfDayBundle
								.getString(Const.Params.HALF_DAY_MORNING_DTIME)
						+ " "
						+ getString(R.string.text_return)
						+ ":"
						+ halfDayBundle
								.getString(Const.Params.HALF_DAY_MORNING_RTIME));
				timeList.add(getString(R.string.text_departure)
						+ ":"
						+ halfDayBundle
								.getString(Const.Params.HALF_DAY_AFTER_DTIME)
						+ " "
						+ getString(R.string.text_return)
						+ ":"
						+ halfDayBundle
								.getString(Const.Params.HALF_DAY_AFTER_RTIME));
				if (spnTourPackage.getSelectedItemPosition() == 1) {
					personList.clear();
					for (int i = 0; i < halfDayPriceList.size(); i++) {
						pTour = halfDayPriceList.get(i);
						personList.add("Max. " + pTour.getpTourPerson()
								+ " Person");
						spnTourPersons.setSelection(0);
					}
				}
			}
			scheduleAdapter.notifyDataSetChanged();
			personAdapter.notifyDataSetChanged();
		}
		String selectedType = spnTourType.getSelectedItem().toString();
		// Common Tour Calculation
		if (spnTourPackage.getSelectedItemPosition() == 0) {
			int totalPersons = spnTourPersons.getSelectedItemPosition() + 1;
			if (selectedType.equals(getString(R.string.text_full_day_tour))) {
				totalTarrif = fullDayCommonPrice * totalPersons;
			} else {
				totalTarrif = halfDayCommonPrice * totalPersons;
			}
		}
		// Private Tour Calculation
		else {
			if (selectedType.equals(getString(R.string.text_full_day_tour))) {
				totalTarrif = fullDayPriceList.get(
						spnTourPersons.getSelectedItemPosition())
						.getpTourPrice();
			} else {
				totalTarrif = halfDayPriceList.get(
						spnTourPersons.getSelectedItemPosition())
						.getpTourPrice();
			}
		}
		tvTotalTarrif.setText("R" + totalTarrif);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}
}