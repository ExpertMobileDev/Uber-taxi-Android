package com.automated.taxinow.fragments;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.automated.taxinow.R;
import com.automated.taxinow.UberViewPaymentActivity;
import com.automated.taxinow.adapter.PlacesAutoCompleteAdapter;
import com.automated.taxinow.component.MyFontPopUpTextView;
import com.automated.taxinow.component.MyFontTextView;
import com.automated.taxinow.models.Driver;
import com.automated.taxinow.models.DriverLocation;
import com.automated.taxinow.models.Route;
import com.automated.taxinow.models.Step;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.parse.VolleyHttpRequest;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.LocationHelper;
import com.automated.taxinow.utils.LocationHelper.OnLocationReceived;
import com.automated.taxinow.utils.MathUtils;
import com.automated.taxinow.utils.PreferenceHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * @author Hardik A Bhalodi
 */
public class UberTripFragment extends UberBaseFragment implements
		OnLocationReceived {
	private GoogleMap map;
	private PolylineOptions lineOptions;
	private Route route;
	ArrayList<LatLng> points;

	private TextView tvTime, tvDist, tvDriverName, tvRate, tvStatus;
	private Driver driver;
	private Marker myMarker, markerDriver;
	private ImageView ivDriverPhoto;
	private LocationHelper locHelper;
	private boolean isContinueStatusRequest;
	private boolean isContinueDriverRequest;
	private Timer timer, timerDriverLocation;

	private final int LOCATION_SCHEDULE = 5 * 1000;
	private String strDistance;
	private Polyline polyLine;
	private LatLng myLatLng;
	private Location myLocation;
	private boolean isTripStarted = false;

	private final int DRAW_TIME = 5 * 1000;
	private String lastTime;
	private String lastDistance;
	private WalkerStatusReceiver walkerReceiver;
	private boolean isAllLocationReceived = false;
	WakeLock wakeLock;
	private PopupWindow notificationWindow, driverStatusWindow;
	private MyFontPopUpTextView tvPopupMsg, tvJobAccepted, tvDriverStarted,
			tvDriverArrvied, tvTripStarted, tvTripCompleted;
	private ImageView ivJobAccepted, ivDriverStarted, ivDriverArrvied,
			ivTripStarted, ivTripCompleted;
	private boolean isNotificationArrievd = false;
	private MyFontTextView tvTaxiModel;
	private MyFontTextView tvTaxiNo;
	private MyFontTextView tvRateStar;
	private TextView tvEstimatedTime;
	private View markerLayout;
	private TextView tvDurationUnit;
	private ProgressBar pBar;
	private LinearLayout layoutCash;
	private LinearLayout layoutCard;
	private TextView tvCash;
	private TextView tvCardNo;
	private PreferenceHelper preference;
	private ImageView ivCard;
	private BroadcastReceiver mReceiver;
	private MapView mMapView;
	private Bundle mBundle;
	private View view;
	private ImageButton btnCancelTrip;
	private LinearLayout layoutDestination;
	private AutoCompleteTextView etDestination;
	private PlacesAutoCompleteAdapter adapterDestination;
	private Marker markerDestination;
	private Route routeDest;
	private ArrayList<LatLng> pointsDest;
	private PolylineOptions lineOptionsDest;
	private Polyline polyLineDest;
	private RelativeLayout layout;
	private ImageButton ibApplyPromo;
	private Dialog dialog;
	private EditText etPromoCode;
	private ImageView ivPromoResult;
	private TextView tvPromoResult;
	private LinearLayout llErrorMsg;
	private Button btnPromoSubmit, btnPromoSkip;
	private RequestQueue requestQueue;

	// private LatLng destLatlng;

	// private ImageView imgSelectedCash;
	// private ImageView imgSelectedCard;

	// private RatingBar ratingBarTrip;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		PowerManager powerManager = (PowerManager) activity
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				Const.TAG);
		wakeLock.acquire();
		driver = (Driver) getArguments().getParcelable(Const.DRIVER);
		points = new ArrayList<LatLng>();
		route = new Route();
		IntentFilter filter = new IntentFilter(Const.INTENT_WALKER_STATUS);
		walkerReceiver = new WalkerStatusReceiver();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				walkerReceiver, filter);
		isAllLocationReceived = false;
		requestQueue = Volley.newRequestQueue(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity.setTitle(getString(R.string.app_name));
		activity.tvTitle.setVisibility(View.GONE);
		activity.layoutDestination.setVisibility(View.VISIBLE);
		view = inflater.inflate(R.layout.fragment_trip_new, container, false);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		view.findViewById(R.id.btnCall).setOnClickListener(this);
		// view.findViewById(R.id.btnAddDestination).setOnClickListener(this);
		// view.findViewById(R.id.imgClearDst).setOnClickListener(this);
		// etDestination = (AutoCompleteTextView) view
		// .findViewById(R.id.etEnterSouce);
		// layoutDestination = (LinearLayout) view
		// .findViewById(R.id.layoutDestination);
		etDestination = activity.etSource;
		layoutDestination = activity.layoutDestination;
		preference = new PreferenceHelper(activity);
		tvTime = (MyFontTextView) view.findViewById(R.id.tvJobTime);
		tvDist = (MyFontTextView) view.findViewById(R.id.tvJobDistance);
		tvDriverName = (MyFontTextView) view.findViewById(R.id.tvDriverName);
		tvTaxiModel = (MyFontTextView) view.findViewById(R.id.tvTaxiModel);
		tvTaxiNo = (MyFontTextView) view.findViewById(R.id.tvTaxiNo);
		tvRateStar = (MyFontTextView) view.findViewById(R.id.tvRateStar);
		layoutCash = (LinearLayout) view.findViewById(R.id.layoutCash);
		layoutCard = (LinearLayout) view.findViewById(R.id.layoutCard);
		tvCash = (TextView) view.findViewById(R.id.tvCash);
		tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
		ivCard = (ImageView) view.findViewById(R.id.ivCard);
		ibApplyPromo = (ImageButton) view.findViewById(R.id.ibApplyPromo);
		// imgSelectedCash = (ImageView)
		// view.findViewById(R.id.imgSelectedCash);
		// imgSelectedCard = (ImageView)
		// view.findViewById(R.id.imgSelectedCard);
		btnCancelTrip = (ImageButton) view.findViewById(R.id.btnCancelTrip);
		btnCancelTrip.setOnClickListener(this);
		layoutCash.setOnClickListener(this);
		layoutCard.setOnClickListener(this);
		ibApplyPromo.setOnClickListener(this);
		// tvDriverPhone = (MyFontTextView)
		// view.findViewById(R.id.tvDriverPhone);
		ivDriverPhoto = (ImageView) view.findViewById(R.id.ivDriverPhoto);
		// tvRate = (TextView) view.findViewById(R.id.tvRate);
		// tvRate.setText(new DecimalFormat("0.0").format(driver.getRating()));
		// ratingBarTrip = (RatingBar) view.findViewById(R.id.ratingBarTrip);
		// ratingBarTrip.setRating((float) driver.getRating());

		// tvDriverPhone.setText(driver.getPhone());
		tvDriverName
				.setText(driver.getFirstName() + " " + driver.getLastName());
		tvTaxiModel.setText(driver.getCarModel());
		tvTaxiNo.setText(driver.getCarNumber());
		tvRateStar.setText(MathUtils.getRound((float) driver.getRating()) + "");

		tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		mMapView = (MapView) view.findViewById(R.id.maptrip);
		mMapView.onCreate(mBundle);
		setUpMap();
		setDefaultCardDetails();
		if (preference.getPaymentMode() == Const.CASH) {
			layoutCash.setSelected(true);
			layoutCard.setSelected(false);
			tvCash.setTextColor(getResources().getColor(R.color.white));
			// imgSelectedCash.setVisibility(View.VISIBLE);
		} else {
			layoutCash.setSelected(false);
			layoutCard.setSelected(true);
			tvCardNo.setTextColor(getResources().getColor(R.color.white));
			// imgSelectedCard.setVisibility(View.VISIBLE);
		}
		if (preference.getIsTripStarted()) {
			btnCancelTrip.setVisibility(View.GONE);
		}

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		AppLog.Log("TripFragment", "Driver Photo : " + driver.getPicture());
		ImageOptions imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.default_user;
		new AQuery(activity).id(ivDriverPhoto).progress(R.id.pBar)
				.image(driver.getPicture(), imageOptions);
		locHelper = new LocationHelper(activity);
		locHelper.setLocationReceivedLister(this);
		adapterDestination = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etDestination.setAdapter(adapterDestination);
		etDestination.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final String selectedDestPlace = adapterDestination
						.getItem(arg2);
				new Thread(new Runnable() {
					@Override
					public void run() {
						preference
								.putClientDestination(getLocationFromAddress(selectedDestPlace));
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// setMarkerOnRoad(destLatlng, destLatlng);
								setDestination(preference
										.getClientDestination());
								// setDestinationMarker(destLatlng);
								// if (myMarker != null
								// && markerDestination != null) {
								// drawPath(myMarker.getPosition(), destLatlng);
								// }
							}
						});
					}
				}).start();
			}
		});

		locHelper.onStart();
		// PopUp Window
		LayoutInflater inflate = LayoutInflater.from(activity);
		layout = (RelativeLayout) inflate.inflate(
				R.layout.popup_notification_window, null);
		tvPopupMsg = (MyFontPopUpTextView) layout.findViewById(R.id.tvPopupMsg);
		notificationWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.setOnClickListener(this);
		activity.btnNotification.setOnClickListener(this);

		// Big PopUp Window
		RelativeLayout bigPopupLayout = (RelativeLayout) inflate.inflate(
				R.layout.popup_notification_status_window, null);
		tvJobAccepted = (MyFontPopUpTextView) bigPopupLayout
				.findViewById(R.id.tvJobAccepted);
		tvDriverStarted = (MyFontPopUpTextView) bigPopupLayout
				.findViewById(R.id.tvDriverStarted);
		tvDriverArrvied = (MyFontPopUpTextView) bigPopupLayout
				.findViewById(R.id.tvDriverArrvied);
		tvTripStarted = (MyFontPopUpTextView) bigPopupLayout
				.findViewById(R.id.tvTripStarted);
		tvTripCompleted = (MyFontPopUpTextView) bigPopupLayout
				.findViewById(R.id.tvTripCompleted);
		ivJobAccepted = (ImageView) bigPopupLayout
				.findViewById(R.id.ivJobAccepted);
		ivDriverStarted = (ImageView) bigPopupLayout
				.findViewById(R.id.ivDriverStarted);
		ivDriverArrvied = (ImageView) bigPopupLayout
				.findViewById(R.id.ivDriverArrvied);
		ivTripStarted = (ImageView) bigPopupLayout
				.findViewById(R.id.ivTripStarted);
		ivTripCompleted = (ImageView) bigPopupLayout
				.findViewById(R.id.ivTripCompleted);
		driverStatusWindow = new PopupWindow(bigPopupLayout,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		driverStatusWindow.setBackgroundDrawable(new BitmapDrawable());
		// driverStatusWindow.setFocusable(false);
		// driverStatusWindow.setTouchable(true);
		driverStatusWindow.setOutsideTouchable(true);
		showNotificationPopUp(getString(R.string.text_job_accepted));
	}

	private LatLng getLocationFromAddress(final String place) {
		AppLog.Log("Address", "" + place);
		LatLng loc = null;
		Geocoder gCoder = new Geocoder(getActivity());
		try {
			final List<Address> list = gCoder.getFromLocationName(place, 1);
			if (list != null && list.size() > 0) {
				loc = new LatLng(list.get(0).getLatitude(), list.get(0)
						.getLongitude());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loc;
	}

	private void setMarkerOnRoad(LatLng source, LatLng destination) {
		String msg = null;
		if (source == null) {
			msg = "Unable to get source location, please try again";
		} else if (destination == null) {
			msg = "Unable to get destination location, please try again";
		}
		if (msg != null) {
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				"http://maps.googleapis.com/maps/api/directions/json?origin="
						+ source.latitude + "," + source.longitude
						+ "&destination=" + destination.latitude + ","
						+ destination.longitude + "&sensor=false");

		new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH_ROAD,
				true, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCall:
			if (driver != null) {
				String number = driver.getPhone();
				if (!TextUtils.isEmpty(number)) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + number));
					startActivity(callIntent);
				}
			}
			break;
		case R.id.rlPopupWindow:
			notificationWindow.dismiss();
			activity.setIcon(R.drawable.notification_box);
			break;
		case R.id.btnActionNotification:
			showDriverStatusNotification();
			break;
		case R.id.layoutCash:
			layoutCash.setSelected(true);
			layoutCard.setSelected(false);
			tvCash.setTextColor(getResources().getColor(R.color.white));
			tvCardNo.setTextColor(getResources().getColor(R.color.gray));
			preference.putPaymentMode(Const.CASH);
			// imgSelectedCash.setVisibility(View.VISIBLE);
			// imgSelectedCard.setVisibility(View.GONE);
			setPaymentMode(Const.CASH);
			break;
		case R.id.layoutCard:
			if (layoutCard.isSelected()) {
				startActivity(new Intent(getActivity(),
						UberViewPaymentActivity.class));
			} else {
				layoutCash.setSelected(false);
				layoutCard.setSelected(true);
				tvCardNo.setTextColor(getResources().getColor(R.color.white));
				tvCash.setTextColor(getResources().getColor(R.color.gray));
				preference.putPaymentMode(Const.CREDIT);
				// imgSelectedCard.setVisibility(View.VISIBLE);
				// imgSelectedCash.setVisibility(View.GONE);
				setPaymentMode(Const.CREDIT);
			}
			break;
		case R.id.btnCancelTrip:
			cancleRequest();
			break;
		case R.id.btnAddDestination:
			layoutDestination.setVisibility(View.VISIBLE);
			layout.setVisibility(View.GONE);
			break;
		case R.id.imgClearDst:
			etDestination.setText("");
			break;
		case R.id.ibApplyPromo:
			showPromoDialog();
			break;
		case R.id.btnPromoSkip:
			if (dialog != null)
				dialog.dismiss();
			break;
		case R.id.btnPromoSubmit:
			applyPromoCode();
			break;
		default:
			// if(driverStatusWindow.isShowing())
			// driverStatusWindow.dismiss();
			break;
		}
	}

	private void applyPromoCode() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		AndyUtils.showCustomProgressRequestDialog(activity,
				getString(R.string.text_apply_promo), true, null);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put(Const.URL, Const.ServiceType.APPLY_PROMO);
		map.put(Const.Params.TOKEN, activity.pHelper.getSessionToken());
		map.put(Const.Params.ID, activity.pHelper.getUserId());
		map.put(Const.Params.PROMO_CODE, etPromoCode.getText().toString()
				.trim());
		// new HttpRequester(activity, map, Const.ServiceCode.APPLY_PROMO,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.APPLY_PROMO, this, this));
	}

	public void showDriverStatusNotification() {
		activity.setIcon(R.drawable.notification_box);
		if (driverStatusWindow.isShowing())
			driverStatusWindow.dismiss();
		else {
			if (notificationWindow.isShowing())
				notificationWindow.dismiss();
			else
				driverStatusWindow.showAsDropDown(activity.btnNotification);
		}
	}

	public void showNotificationPopUp(String text) {
		tvPopupMsg.setText(text);
		if (!driverStatusWindow.isShowing()) {
			if (!notificationWindow.isShowing()) {
				activity.setIcon(R.drawable.notification_box_arrived);
				notificationWindow.showAsDropDown(activity.btnNotification);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (activity.pHelper.getRequestTime() == Const.NO_TIME)
		// setRequestTime(SystemClock.e);
		mMapView.onResume();
		activity.btnNotification.setVisibility(View.VISIBLE);
		startUpdateDriverLocation();
		startCheckingStatusUpdate();
		registerCardReceiver();
	}

	@Override
	public void onPause() {
		stopUpdateDriverLoaction();
		stopCheckingStatusUpdate();

		super.onPause();
		mMapView.onPause();
	}

	private void setUpMap() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			// map = ((SupportMapFragment) getActivity()
			// .getSupportFragmentManager().findFragmentById(R.id.maptrip))
			// .getMap();
			map = ((MapView) view.findViewById(R.id.maptrip)).getMap();
			// map.setOnMyLocationChangeListener(new
			// OnMyLocationChangeListener() {
			//
			// @Override
			// public void onMyLocationChange(Location arg0) {
			// // TODO Auto-generated method stub
			// drawTrip(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
			// }
			// });
			map.setInfoWindowAdapter(new InfoWindowAdapter() {
				@Override
				public View getInfoWindow(Marker marker) {
					View v = activity.getLayoutInflater().inflate(
							R.layout.info_window_layout, null);
					((MyFontTextView) v).setText(marker.getTitle());
					return v;
				}

				// Defines the contents of the InfoWindow
				@Override
				public View getInfoContents(Marker marker) {
					return null;
				}
			});

			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					marker.showInfoWindow();
					return true;
				}
			});
		}
		initPreviousDrawPath();

	}

	private void setMarkers(LatLng latLang) {
		LatLng latLngDriver = new LatLng(driver.getLatitude(),
				driver.getLongitude());
		setMarker(latLngDriver);
		setDriverMarker(latLngDriver, driver.getBearing());
//		animateCameraToMarkerWithZoom(latLngDriver);
		boundLatLang();

		// showDirection(latLang, latLngDriver);
		// Location locDriver = new Location("");
		// locDriver.setLatitude(driver.getLatitude());
		// locDriver.setLongitude(driver.getLongitude());
		// strDistance = convertMilesFromMeters(loc
		// .distanceTo(locDriver));
		// animateCameraToMarker(latLang);
	}

	private void showPromoDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_promo);
		ivPromoResult = (ImageView) dialog.findViewById(R.id.ivPromoResult);
		tvPromoResult = (TextView) dialog.findViewById(R.id.tvPromoResult);
		etPromoCode = (EditText) dialog.findViewById(R.id.etPromoCode);
		llErrorMsg = (LinearLayout) dialog.findViewById(R.id.llErrorMsg);
		btnPromoSubmit = (Button) dialog.findViewById(R.id.btnPromoSubmit);
		btnPromoSubmit.setOnClickListener(this);
		btnPromoSkip = (Button) dialog.findViewById(R.id.btnPromoSkip);
		btnPromoSkip.setOnClickListener(this);
		if (!TextUtils.isEmpty(activity.pHelper.getPromoCode())) {
			etPromoCode.setText(activity.pHelper.getPromoCode());
			btnPromoSkip.setText(getString(R.string.text_done));
			btnPromoSubmit.setEnabled(false);
			etPromoCode.setEnabled(false);
		}
		dialog.show();
	}

	private void showDirection(LatLng source, LatLng destination) {

		Map<String, String> hashMap = new HashMap<String, String>();

		final String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
				+ source.latitude
				+ ","
				+ source.longitude
				+ "&destination="
				+ destination.latitude
				+ ","
				+ destination.longitude
				+ "&sensor=false";
		new HttpRequester(activity, hashMap, Const.ServiceCode.GET_ROUTE, true,
				this);
		AndyUtils.showCustomProgressDialog(activity,
				getString(R.string.text_getting_direction), false, null);
	}

	private void drawPath(LatLng source, LatLng destination) {
		if (source == null || destination == null) {
			return;
		}
		if (destination.latitude != 0) {
			setDestinationMarker(destination);
			boundLatLang();

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL,
					"http://maps.googleapis.com/maps/api/directions/json?origin="
							+ source.latitude + "," + source.longitude
							+ "&destination=" + destination.latitude + ","
							+ destination.longitude + "&sensor=false");
			// new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH,
			// true,
			// this);
			requestQueue.add(new VolleyHttpRequest(Method.GET, map,
					Const.ServiceCode.DRAW_PATH, this, this));
		}

	}

	private void boundLatLang() {

		try {
			if (myMarker != null && markerDriver != null
					&& markerDestination != null) {
				LatLngBounds.Builder bld = new LatLngBounds.Builder();
				bld.include(new LatLng(myMarker.getPosition().latitude,
						myMarker.getPosition().longitude));
				bld.include(new LatLng(markerDriver.getPosition().latitude,
						markerDriver.getPosition().longitude));
				bld.include(new LatLng(
						markerDestination.getPosition().latitude,
						markerDestination.getPosition().longitude));
				LatLngBounds latLngBounds = bld.build();

				map.animateCamera(CameraUpdateFactory.newLatLngBounds(
						latLngBounds, 50));
			} else if (myMarker != null && markerDriver != null) {
				LatLngBounds.Builder bld = new LatLngBounds.Builder();
				bld.include(new LatLng(myMarker.getPosition().latitude,
						myMarker.getPosition().longitude));
				bld.include(new LatLng(markerDriver.getPosition().latitude,
						markerDriver.getPosition().longitude));
				LatLngBounds latLngBounds = bld.build();

				map.animateCamera(CameraUpdateFactory.newLatLngBounds(
						latLngBounds, 100));
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void onDestroyView() {
		wakeLock.release();
		SupportMapFragment f = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.maptrip);
		if (f != null) {
			try {
				getFragmentManager().beginTransaction().remove(f).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		map = null;
		activity.layoutDestination.setVisibility(View.GONE);
		super.onDestroyView();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTaskCompleted(final String response, int serviceCode) {
		if (!this.isVisible())
			return;
		switch (serviceCode) {
		case Const.ServiceCode.GET_ROUTE:
			AndyUtils.removeCustomProgressDialog();
			if (!TextUtils.isEmpty(response)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						new ParseContent(activity).parseRoute(response, route);

						final ArrayList<Step> step = route.getListStep();
						points = new ArrayList<LatLng>();
						lineOptions = new PolylineOptions();
						lineOptions.geodesic(true);

						for (int i = 0; i < step.size(); i++) {

							List<LatLng> path = step.get(i).getListPoints();
							// System.out.println("step =====> " + i + " and "
							// + path.size());
							points.addAll(path);
						}
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (polyLine != null)
									polyLine.remove();
								lineOptions.addAll(points);
								lineOptions.width(15);
								lineOptions.geodesic(true);
								lineOptions.color(getResources().getColor(
										R.color.skyblue));
								polyLine = map.addPolyline(lineOptions);
								LatLngBounds.Builder bld = new LatLngBounds.Builder();
								bld.include(myMarker.getPosition());
								bld.include(markerDriver.getPosition());
								LatLngBounds latLngBounds = bld.build();
								map.moveCamera(CameraUpdateFactory
										.newLatLngBounds(latLngBounds, 50));
								// tvDist.setText(route.getDistanceText());
								// tvTime.setText(route.getDurationText());
								// tvDist.setText(0 + " KM");
								// tvTime.setText(0 + " MINS");
							}
						});
					}
				}).start();
			}
		case Const.ServiceCode.DRAW_PATH_ROAD:
			if (!TextUtils.isEmpty(response)) {
				routeDest = new Route();
				activity.pContent.parseRoute(response, routeDest);

				final ArrayList<Step> step = routeDest.getListStep();
				System.out.println("step size=====> " + step.size());
				pointsDest = new ArrayList<LatLng>();
				lineOptionsDest = new PolylineOptions();
				lineOptionsDest.geodesic(true);

				for (int i = 0; i < step.size(); i++) {
					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					pointsDest.addAll(path);
				}
				if (pointsDest != null && pointsDest.size() > 0) {
					drawPath(myMarker.getPosition(),
							preference.getClientDestination());
				}
			}
			break;
		case Const.ServiceCode.DRAW_PATH:
			if (!TextUtils.isEmpty(response)) {
				routeDest = new Route();
				activity.pContent.parseRoute(response, routeDest);

				final ArrayList<Step> step = routeDest.getListStep();
				System.out.println("step size=====> " + step.size());
				pointsDest = new ArrayList<LatLng>();
				lineOptionsDest = new PolylineOptions();
				lineOptionsDest.geodesic(true);

				for (int i = 0; i < step.size(); i++) {
					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					pointsDest.addAll(path);
				}
				if (polyLineDest != null)
					polyLineDest.remove();
				lineOptionsDest.addAll(pointsDest);
				lineOptionsDest.width(15);
				lineOptionsDest.color(getResources().getColor(
						R.color.color_text)); // #00008B rgb(0,0,139)
				try {
					if (lineOptionsDest != null && map != null) {
						polyLineDest = map.addPolyline(lineOptionsDest);
						boundLatLang();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case Const.ServiceCode.GET_REQUEST_LOCATION:
			if (activity.pContent.isSuccess(response)) {
				DriverLocation driverLocation = activity.pContent
						.getDriverLocation(response);
				if (driverLocation == null || !this.isVisible()
						|| driverLocation.getLatLng() == null)
					return;
				setDriverMarker(driverLocation.getLatLng(),
						driverLocation.getBearing());

				drawTrip(driverLocation.getLatLng());
				if (isTripStarted) {
					long startTime = Const.NO_TIME;
					if (activity.pHelper.getRequestTime() == Const.NO_TIME) {
						startTime = System.currentTimeMillis();
						activity.pHelper.putRequestTime(startTime);
					} else {
						startTime = activity.pHelper.getRequestTime();
					}

					double distance = Double.parseDouble(driverLocation
							.getDistance());
					// distance = distance / 1625;
					// tvDist.setText(new DecimalFormat("0.00").format(distance)
					// + " " + driverLocation.getUnit());
					tvDist.setText(new DecimalFormat("0.00").format(distance)
							+ " " + driverLocation.getUnit());
					long elapsedTime = System.currentTimeMillis() - startTime;
					lastTime = elapsedTime / (1000 * 60) + " "
							+ getResources().getString(R.string.text_mins);
					tvTime.setText(lastTime);
					// tvTime.setText("0" + " MINS");
					// tvDist.setText("0" + " KM");
				} else
					tvDist.setText(0 + " " + driverLocation.getUnit());
			}
			isContinueDriverRequest = true;
			// setMarker(latLng);
			break;
		case Const.ServiceCode.GET_REQUEST_STATUS:
			if (activity.pContent.isSuccess(response)) {
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_driver_arrvied)));
					// showNotificationPopUp(getString(R.string.text_driver_arrvied));
					changeNotificationPopUpUI(3);
					isContinueStatusRequest = true;
					isTripStarted = false;
					break;
				case Const.IS_COMPLETED:
					btnCancelTrip.setVisibility(View.GONE);
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_trip_started)));
					// showNotificationPopUp(getString(R.string.text_trip_started));
					changeNotificationPopUpUI(4);
					if (!isAllLocationReceived) {
						isAllLocationReceived = true;
						getPath(String.valueOf(activity.pHelper.getRequestId()));
					}
					isContinueStatusRequest = true;
					isTripStarted = true;
					preference.putIsTripStarted(true);
					break;
				case Const.IS_WALKER_ARRIVED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_driver_started)));
					// showNotificationPopUp(getString(R.string.text_driver_started));
					changeNotificationPopUpUI(2);
					isContinueStatusRequest = true;
					break;
				case Const.IS_WALKER_STARTED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_job_accepted)));
					// showNotificationPopUp(getString(R.string.text_job_accepted));
					changeNotificationPopUpUI(1);
					isContinueStatusRequest = true;
					break;
				case Const.IS_WALKER_RATED:
					stopCheckingStatusUpdate();
					isTripStarted = false;
					if (notificationWindow.isShowing())
						notificationWindow.dismiss();
					if (driverStatusWindow.isShowing())
						driverStatusWindow.dismiss();
					driver = activity.pContent.getDriverDetail(response);
					driver.setLastDistance(lastDistance);
					driver.setLastTime(lastTime);
					activity.gotoRateFragment(driver);
					break;

				default:

					break;
				}

			} else {
				isContinueStatusRequest = true;
			}
			break;
		case Const.ServiceCode.GET_PATH:
			AndyUtils.removeCustomProgressDialog();
			activity.pContent.parsePathRequest(response, points);
			initPreviousDrawPath();
			AppLog.Log(Const.TAG, "Path====>" + response + "");
			break;
		case Const.ServiceCode.GET_DURATION:
			pBar.setVisibility(View.GONE);
			AppLog.Log("UberTripFragment", "Duration Response : " + response);

			String[] durationArr = activity.pContent
					.parseNearestDriverDurationString(response).split(" ");
			tvEstimatedTime.setText(durationArr[0]);
			tvDurationUnit.setText(durationArr[1]);
			myMarker.setIcon(BitmapDescriptorFactory.fromBitmap(AndyUtils
					.createDrawableFromView(getActivity(), markerLayout)));

			break;
		case Const.ServiceCode.PAYMENT_TYPE:
			AndyUtils.removeCustomProgressDialog();
			AppLog.Log("UberTripFragment", "Payment type reponse : " + response);
			if (!activity.pContent.isSuccess(response)) {
				AndyUtils.showToast("Sorry, Cannot change payment mode",
						getActivity());
			}
			break;
		case Const.ServiceCode.CANCEL_REQUEST:
			if (activity.pContent.isSuccess(response)) {

			}
			activity.pHelper.clearRequestData();
			stopCheckingStatusUpdate();
			stopUpdateDriverLoaction();
			AndyUtils.removeCustomProgressDialog();
			activity.gotoMapFragment();
			break;
		case Const.ServiceCode.SET_DESTINATION:
			AndyUtils.removeCustomProgressDialog();
			AppLog.Log("Trip", "Destination Response : " + response);
			if (activity.pContent.isSuccess(response)) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// setMarkerOnRoad(destLatlng, destLatlng);
						drawPath(myMarker.getPosition(),
								preference.getClientDestination());
					}
				});
			}
			break;
		case Const.ServiceCode.APPLY_PROMO:
			llErrorMsg.setVisibility(View.VISIBLE);
			AndyUtils.removeCustomProgressDialog();
			if (activity.pContent.isSuccess(response)) {
				activity.pHelper.putPromoCode(etPromoCode.getText().toString());
				tvPromoResult.setText(activity.pContent.getMessage(response));
				ivPromoResult.setSelected(true);
				tvPromoResult.setSelected(true);
				btnPromoSkip.setText(getString(R.string.text_done));
				btnPromoSubmit.setEnabled(false);
				etPromoCode.setEnabled(false);

			} else {
				tvPromoResult.setText(activity.pContent.getMessage(response));
				ivPromoResult.setSelected(false);
				tvPromoResult.setSelected(false);
			}
			break;
		}
	}

	private void changeNotificationPopUpUI(int i) {
		switch (i) {
		case 1:
			ivJobAccepted.setImageResource(R.drawable.checkbox);
			tvJobAccepted.setTextColor(getResources().getColor(
					R.color.color_text));
			break;
		case 2:
			ivJobAccepted.setImageResource(R.drawable.checkbox);
			tvJobAccepted.setTextColor(getResources().getColor(
					R.color.color_text));
			ivDriverStarted.setImageResource(R.drawable.checkbox);
			tvDriverStarted.setTextColor(getResources().getColor(
					R.color.color_text));
			break;
		case 3:
			ivJobAccepted.setImageResource(R.drawable.checkbox);
			tvJobAccepted.setTextColor(getResources().getColor(
					R.color.color_text));
			ivDriverStarted.setImageResource(R.drawable.checkbox);
			tvDriverStarted.setTextColor(getResources().getColor(
					R.color.color_text));
			ivDriverArrvied.setImageResource(R.drawable.checkbox);
			tvDriverArrvied.setTextColor(getResources().getColor(
					R.color.color_text));
			break;
		case 4:
			ivJobAccepted.setImageResource(R.drawable.checkbox);
			tvJobAccepted.setTextColor(getResources().getColor(
					R.color.color_text));
			ivDriverStarted.setImageResource(R.drawable.checkbox);
			tvDriverStarted.setTextColor(getResources().getColor(
					R.color.color_text));
			ivDriverArrvied.setImageResource(R.drawable.checkbox);
			tvDriverArrvied.setTextColor(getResources().getColor(
					R.color.color_text));
			ivTripStarted.setImageResource(R.drawable.checkbox);
			tvTripStarted.setTextColor(getResources().getColor(
					R.color.color_text));
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		return false;
	}

	class TrackLocation extends TimerTask {
		public void run() {
			if (isContinueDriverRequest) {
				isContinueDriverRequest = false;
				getDriverLocation();
			}
		}
	}

	private void getDriverLocation() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				Const.ServiceType.GET_REQUEST_LOCATION + Const.Params.ID + "="
						+ new PreferenceHelper(activity).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(activity).getSessionToken()
						+ "&" + Const.Params.REQUEST_ID + "="
						+ new PreferenceHelper(activity).getRequestId());
		AppLog.Log("TAG",
				Const.ServiceType.GET_REQUEST_LOCATION + Const.Params.ID + "="
						+ new PreferenceHelper(activity).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(activity).getSessionToken()
						+ "&" + Const.Params.REQUEST_ID + "="
						+ new PreferenceHelper(activity).getRequestId());
		// new HttpRequester(activity, map,
		// Const.ServiceCode.GET_REQUEST_LOCATION, true, this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_REQUEST_LOCATION, this, this));

	}

	private void setMarker(LatLng latLng) {
		if (latLng != null) {
			if (map != null && this.isVisible()) {
				if (myMarker == null) {
					markerLayout = ((LayoutInflater) getActivity()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
							.inflate(R.layout.custom_marker_layout, null);
					tvEstimatedTime = (TextView) markerLayout
							.findViewById(R.id.num_txt);
					tvEstimatedTime = (TextView) markerLayout
							.findViewById(R.id.num_txt);
					tvDurationUnit = (TextView) markerLayout
							.findViewById(R.id.tvDurationUnit);
					pBar = (ProgressBar) markerLayout.findViewById(R.id.pBar);

					MarkerOptions opt = new MarkerOptions();
					opt.position(latLng);
					opt.icon(BitmapDescriptorFactory.fromBitmap(AndyUtils
							.createDrawableFromView(getActivity(), markerLayout)));
					opt.title(getString(R.string.text_my_location));
					myMarker = map.addMarker(opt);
					// animateCameraToMarkerWithZoom(latLng);
				} else {
					myMarker.setPosition(latLng);
				}
				drawPath(myMarker.getPosition(),
						preference.getClientDestination());
			}
		}
	}

	private void setDriverMarker(LatLng latLng, double bearing) {
		if (latLng != null) {
			if (map != null && this.isVisible()) {
				if (markerDriver == null) {
					MarkerOptions opt = new MarkerOptions();
					opt.flat(true);
					opt.anchor(0.5f, 0.5f);
					opt.position(latLng);
					opt.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.pin_driver));
					opt.title(getString(R.string.text_drive_location));
					markerDriver = map.addMarker(opt);
				} else {
					Location driverLocation = new Location("");
					driverLocation.setLatitude(latLng.latitude);
					driverLocation.setLongitude(latLng.longitude);
					driverLocation.setBearing((float) bearing);
					animateMarker(markerDriver, latLng, driverLocation, false);
					// if (isCameraZoom) {
					// animateCameraToMarker(latLng);
					// }
				}
				if (myMarker != null && myMarker.getPosition() != null)
					getDirectionsUrl(latLng, myMarker.getPosition());
			}
		}

	}

	private void setDestinationMarker(LatLng latLng) {
		if (latLng != null) {
			if (map != null && this.isVisible()) {
				if (markerDestination == null) {
					MarkerOptions opt = new MarkerOptions();
					opt.position(latLng);
					opt.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.destination_pin));
					opt.title(getString(R.string.text_destination_pin_title));
					markerDestination = map.addMarker(opt);
				} else {
					markerDestination.setPosition(latLng);
				}
			}
		}
	}

	private void startUpdateDriverLocation() {
		isContinueDriverRequest = true;
		timerDriverLocation = new Timer();
		timerDriverLocation.scheduleAtFixedRate(new TrackLocation(), 0,
				LOCATION_SCHEDULE);
	}

	private void stopUpdateDriverLoaction() {
		isContinueDriverRequest = false;
		if (timerDriverLocation != null) {
			timerDriverLocation.cancel();
			timerDriverLocation = null;
		}
	}

	private void animateCameraToMarkerWithZoom(LatLng latLng) {
		CameraUpdate cameraUpdate = null;
		cameraUpdate = CameraUpdateFactory
				.newLatLngZoom(latLng, Const.MAP_ZOOM);
		map.animateCamera(cameraUpdate);
	}

	private void animateCameraToMarker(LatLng latLng) {
		CameraUpdate cameraUpdate = null;
		cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
		map.animateCamera(cameraUpdate);
	}

	private String convertKmFromMeters(float disatanceInMeters) {
		return new DecimalFormat("0.0").format(0.001f * disatanceInMeters);
	}

	private void startCheckingStatusUpdate() {
		stopCheckingStatusUpdate();
		if (activity.pHelper.getRequestId() != Const.NO_REQUEST) {
			isContinueStatusRequest = true;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerRequestStatus(), Const.DELAY,
					Const.TIME_SCHEDULE);
		}
	}

	private void stopCheckingStatusUpdate() {
		isContinueStatusRequest = false;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private class TimerRequestStatus extends TimerTask {
		@Override
		public void run() {
			if (isContinueStatusRequest) {
				isContinueStatusRequest = false;
				getRequestStatus(String
						.valueOf(activity.pHelper.getRequestId()));
			}
		}
	}

	private void getRequestStatus(String requestId) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				Const.ServiceType.GET_REQUEST_STATUS + Const.Params.ID + "="
						+ new PreferenceHelper(activity).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(activity).getSessionToken()
						+ "&" + Const.Params.REQUEST_ID + "=" + requestId);

		// new HttpRequester(activity, map,
		// Const.ServiceCode.GET_REQUEST_STATUS,
		// true, this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_REQUEST_STATUS, this, this));
	}

	private void getPath(String requestId) {
		AndyUtils.showCustomProgressDialog(activity,
				getString(R.string.progress_loading), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				Const.ServiceType.GET_PATH + Const.Params.ID + "="
						+ new PreferenceHelper(activity).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(activity).getSessionToken()
						+ "&" + Const.Params.REQUEST_ID + "=" + requestId);
		// new HttpRequester(activity, map, Const.ServiceCode.GET_PATH, true,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_PATH, this, this));
	}

	private void setRequestTime(long time) {
		activity.pHelper.putRequestTime(time);
	}

	private void drawTrip(LatLng latlng) {
		if (map != null && this.isVisible()) {
			points.add(latlng);
			lineOptions = new PolylineOptions();
			lineOptions.addAll(points);
			lineOptions.width(15);
			lineOptions.geodesic(true);
			lineOptions.color(getResources().getColor(R.color.skyblue));
			map.addPolyline(lineOptions);
		}
	}

	class WalkerStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String response = intent.getStringExtra(Const.EXTRA_WALKER_STATUS);
			AppLog.Log("Response ---- Trip", response);
			if (TextUtils.isEmpty(response))
				return;
			stopCheckingStatusUpdate();

			if (activity.pContent.isSuccess(response)) {
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:

					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_driver_arrvied)));
					showNotificationPopUp(getString(R.string.text_driver_arrvied));
					changeNotificationPopUpUI(3);
					isContinueStatusRequest = true;
					isTripStarted = false;
					break;
				case Const.IS_COMPLETED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_trip_started)));
					showNotificationPopUp(getString(R.string.text_trip_started));
					changeNotificationPopUpUI(4);
					if (!isAllLocationReceived) {
						isAllLocationReceived = true;
						getPath(String.valueOf(activity.pHelper.getRequestId()));
					}
					isContinueStatusRequest = true;
					isTripStarted = true;
					break;
				case Const.IS_WALKER_ARRIVED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_driver_started)));
					showNotificationPopUp(getString(R.string.text_driver_started));
					changeNotificationPopUpUI(2);
					isContinueStatusRequest = true;
					break;
				case Const.IS_WALKER_STARTED:
					tvStatus.setText(Html
							.fromHtml(getString(R.string.text_job_accepted)));
					showNotificationPopUp(getString(R.string.text_job_accepted));
					changeNotificationPopUpUI(1);
					isContinueStatusRequest = true;
					break;
				case Const.IS_WALKER_RATED:
					stopCheckingStatusUpdate();
					isTripStarted = false;
					if (notificationWindow.isShowing())
						notificationWindow.dismiss();
					if (driverStatusWindow.isShowing())
						driverStatusWindow.dismiss();
					driver = activity.pContent.getDriverDetail(response);
					driver.setLastDistance(lastDistance);
					driver.setLastTime(lastTime);
					activity.gotoRateFragment(driver);
					break;
				default:
					break;
				}
			} else {
				isContinueStatusRequest = true;
			}
			startCheckingStatusUpdate();
		}
	}

	private void initPreviousDrawPath() {
		lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(15);
		lineOptions.geodesic(true);
		lineOptions.color(getResources().getColor(R.color.skyblue));
		if (map != null && this.isVisible())
			map.addPolyline(lineOptions);
		points.clear();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();

		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				walkerReceiver);
		if (notificationWindow.isShowing())
			notificationWindow.dismiss();
		if (driverStatusWindow.isShowing())
			driverStatusWindow.dismiss();
		ubregisterCardReceiver();
	}

	// perfect function...
	private void animateMarker(final Marker marker, final LatLng toPosition,
			final Location toLocation, final boolean hideMarker) {
		if (map == null || !this.isVisible()) {
			return;
		}
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = map.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final double startRotation = marker.getRotation();
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * toPosition.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * toPosition.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				float rotation = (float) (t * toLocation.getBearing() + (1 - t)
						* startRotation);
				if (rotation != 0) {
					marker.setRotation(rotation);
				}
				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					if (hideMarker) {
						marker.setVisible(false);
					} else {
						marker.setVisible(true);
					}
				}
			}
		});
	}

	private void getDirectionsUrl(LatLng origin, LatLng destination) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		} else if (origin == null) {
			return;
		}
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;
		String str_dest = "destination=" + destination.latitude + ","
				+ destination.longitude;
		String sensor = "sensor=false";
		String parameters = str_origin + "&" + str_dest + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, url);
		// new HttpRequester(activity, map, Const.ServiceCode.GET_DURATION,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.GET_DURATION, this, this));
	}

	private void setPaymentMode(int type) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		AndyUtils.showCustomProgressDialog(activity,
				getString(R.string.text_changing_payment_mode), true, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.PAYMENT_TYPE);
		map.put(Const.Params.ID, String.valueOf(activity.pHelper.getUserId()));
		map.put(Const.Params.TOKEN,
				String.valueOf(activity.pHelper.getSessionToken()));
		map.put(Const.Params.REQUEST_ID,
				String.valueOf(activity.pHelper.getRequestId()));
		map.put(Const.Params.CASH_OR_CARD, String.valueOf(type));

		// new HttpRequester(activity, map, Const.ServiceCode.PAYMENT_TYPE,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.PAYMENT_TYPE, this, this));
	}

	private void setDefaultCardDetails() {
		if (preference.getDefaultCard() == 0) {
			layoutCard.setVisibility(View.INVISIBLE);
		} else {
			layoutCard.setVisibility(View.VISIBLE);
			tvCardNo.setText("*****" + preference.getDefaultCardNo());
			String type = preference.getDefaultCardType();
			if (type.equalsIgnoreCase(Const.VISA)) {
				ivCard.setImageResource(R.drawable.ub__creditcard_visa);
			} else if (type.equalsIgnoreCase(Const.MASTERCARD)) {
				ivCard.setImageResource(R.drawable.ub__creditcard_mastercard);
			} else if (type.equalsIgnoreCase(Const.AMERICAN_EXPRESS)) {
				ivCard.setImageResource(R.drawable.ub__creditcard_amex);
			} else if (type.equalsIgnoreCase(Const.DISCOVER)) {
				ivCard.setImageResource(R.drawable.ub__creditcard_discover);
			} else if (type.equalsIgnoreCase(Const.DINERS_CLUB)) {
				ivCard.setImageResource(R.drawable.ub__creditcard_discover);
			} else {
				ivCard.setImageResource(R.drawable.ub__nav_payment);
			}
		}
	}

	private void registerCardReceiver() {
		IntentFilter intentFilter = new IntentFilter("card_change_receiver");
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				setDefaultCardDetails();
				AppLog.Log("TripFragment", "Card change Receiver");
			}
		};
		getActivity().registerReceiver(mReceiver, intentFilter);
	}

	private void ubregisterCardReceiver() {
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
		}
	}

	private void cancleRequest() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		AppLog.Log("UberTripFragment",
				"Request ID : " + activity.pHelper.getRequestId());
		AndyUtils.showCustomProgressDialog(activity,
				getString(R.string.text_canceling_request), true, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.CANCEL_REQUEST);
		map.put(Const.Params.ID, String.valueOf(activity.pHelper.getUserId()));
		map.put(Const.Params.TOKEN,
				String.valueOf(activity.pHelper.getSessionToken()));
		map.put(Const.Params.REQUEST_ID,
				String.valueOf(activity.pHelper.getRequestId()));
		// new HttpRequester(activity, map, Const.ServiceCode.CANCEL_REQUEST,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.CANCEL_REQUEST, this, this));
	}

	private void setDestination(LatLng destination) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		if (destination != null) {
			AndyUtils.showCustomProgressDialog(activity,
					getString(R.string.text_adding_dest), true, null);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.SET_DESTINATION);
			map.put(Const.Params.ID,
					String.valueOf(activity.pHelper.getUserId()));
			map.put(Const.Params.TOKEN,
					String.valueOf(activity.pHelper.getSessionToken()));
			map.put(Const.Params.REQUEST_ID,
					String.valueOf(activity.pHelper.getRequestId()));
			map.put(Const.Params.DEST_LAT, String.valueOf(destination.latitude));
			map.put(Const.Params.DEST_LNG,
					String.valueOf(destination.longitude));
			// new HttpRequester(activity, map,
			// Const.ServiceCode.SET_DESTINATION,
			// this);
			requestQueue.add(new VolleyHttpRequest(Method.POST, map,
					Const.ServiceCode.SET_DESTINATION, this, this));
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		AppLog.Log(Const.TAG, error.getMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.automated.taxinow.utils.LocationHelper.OnLocationReceived#
	 * onLocationReceived(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onLocationReceived(LatLng latlong) {
		// TODO Auto-generated method stub
		if (isTripStarted && isAllLocationReceived) {
			// drawTrip(latlong);
			myLocation.setLatitude(latlong.latitude);
			myLocation.setLongitude(latlong.longitude);
			if (!isTripStarted)
				setMarker(latlong);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.automated.taxinow.utils.LocationHelper.OnLocationReceived#
	 * onLocationReceived(android.location.Location)
	 */
	@Override
	public void onLocationReceived(Location location) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.automated.taxinow.utils.LocationHelper.OnLocationReceived#onConntected
	 * (android.os.Bundle)
	 */
	@Override
	public void onConntected(Bundle bundle) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.automated.taxinow.utils.LocationHelper.OnLocationReceived#onConntected
	 * (android.location.Location)
	 */
	@Override
	public void onConntected(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {

			myLocation = location;
			myLatLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			if (UberTripFragment.this.isVisible())
				setMarkers(myLatLng);
		}
	}
}
