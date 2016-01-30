package com.automated.taxinow.fragments;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.automated.taxinow.R;
import com.automated.taxinow.UberViewPaymentActivity;
import com.automated.taxinow.adapter.PlacesAutoCompleteAdapter;
import com.automated.taxinow.adapter.VehicalTypeListAdapter;
import com.automated.taxinow.component.MyFontButton;
import com.automated.taxinow.interfaces.OnProgressCancelListener;
import com.automated.taxinow.models.Driver;
import com.automated.taxinow.models.Route;
import com.automated.taxinow.models.Step;
import com.automated.taxinow.models.VehicalType;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.parse.VolleyHttpRequest;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.LocationHelper;
import com.automated.taxinow.utils.LocationHelper.OnLocationReceived;
import com.automated.taxinow.utils.PreferenceHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * @author Hardik A Bhalodi
 */

public class UberMapFragment extends UberBaseFragment implements
		OnProgressCancelListener, OnSeekBarChangeListener, OnItemClickListener,
		OnLocationReceived {

	private PlacesAutoCompleteAdapter adapter;
	private AutoCompleteTextView etSource;
	public static boolean isMapTouched = false;
	private float currentZoom = -1;
	private GoogleMap map;

	private LatLng curretLatLng;
	private String strAddress = null;
	private boolean isContinueRequest;
	private Timer timer;
	private WalkerStatusReceiver walkerReceiver;
	private ImageButton btnMyLocation;
	private FrameLayout mapFrameLayout;
	private GridView listViewType;
	private ArrayList<VehicalType> listType;
	private VehicalTypeListAdapter typeAdapter;
	private int selectedPostion = -1;
	private boolean isGettingVehicalType = true;
	private static final int REQUEST_CODE = 1;
	private boolean isLocationFound;
	private MyFontButton btnSelectService;
	private SlidingDrawer drawer;
	private ImageView ivCash;
	private ImageView ivCredit;
	private int paymentMode;
	private LinearLayout llServiceText;
	private RadioButton selectedRd;
	private TextView tvEstimatedTime;
	private ProgressBar pBar;
	private RelativeLayout layoutDuration;
	// private AutoCompleteTextView etDestination;
	private PlacesAutoCompleteAdapter adapterDestination;
	private Marker markerDestination, markerSource;
	private Route route;
	private ArrayList<LatLng> points;
	private PolylineOptions lineOptions;
	private boolean isSource;
	private Polyline polyLine;
	private View layoutBubble;
	private boolean isAddDestination;
	private Button btnSendRequestDesti;
	private View layoutRgService;
	private LinearLayout linearPointer;
	// private SeekBar sb;
	private int value;
	private int start;
	private ValueAnimator anim;
	private int pointer;
	private LinearLayout layoutMarker;
	private TextView tvNo;
	private PreferenceHelper preference;
	private LinearLayout layoutDestination;
	private LinearLayout layoutCardDetails;
	private TextView tvDurationUnit;
	private ImageView ivCard;
	private BroadcastReceiver mReceiver;
	private LinearLayout layoutFareQuote;
	private TextView txtTotalFareQuate;
	private Dialog quoteDialog;
	private Dialog dialog, rateCardDialog;
	private View view;
	private MapView mMapView;
	private Bundle mBundle;
	private LinearLayout linearPointer2;
	private TextView tvMaxSize;
	private TextView tvMinFare;
	private TextView tvETA;
	private String estimatedTimeTxt;
	private AutoCompleteTextView etPopupDestination;
	private PlacesAutoCompleteAdapter adapterPopUpDestination;
	private TextView tvGetFareEst;
	private Dialog destinationDialog;
	private ProgressBar pbMinFare;
	private TextView tvLblMinFare;
	private TextView tvTotalFare;
	private LinearLayout layoutHomeText;
	private LinearLayout layoutHomeEdit;
	private LinearLayout layoutWorkText;
	private LinearLayout layoutWorkEdit;
	private AutoCompleteTextView etHomeAddress;
	private AutoCompleteTextView etWorkAddress;
	private PlacesAutoCompleteAdapter adapterHomeAddress;
	private PlacesAutoCompleteAdapter adapterWorkAddress;
	private TextView tvHomeAddress;
	private TextView tvWorkAddress;
	private ListView nearByList;
	private ArrayAdapter<String> nearByAd;
	private Address address;
	private ProgressBar pbNearby;
	private ArrayList<Driver> listDriver = new ArrayList<Driver>();
	private HashMap<Integer, Marker> nearDriverMarker;
	private Timer timerProvidersLocation;
	private final int LOCATION_SCHEDULE = 5 * 1000;
	private ArrayList<Driver> listUpdatedDriver;
	private Location loc;
	private Dialog referralDialog;
	private EditText etRefCode;
	private int is_skip = 0;
	private LinearLayout llErrorMsg;
	private LocationHelper locHelper;
	private Location myLocation;
	private TextView tvRateCard;
	private RequestQueue requestQueue;

	// PopupWindow window;
	public static UberMapFragment newInstance() {
		UberMapFragment mapFragment = new UberMapFragment();
		return mapFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map, container, false);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		isLocationFound = false;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutMarker = (LinearLayout) view.findViewById(R.id.layoutMarker);
		view.findViewById(R.id.markerBubblePickMeUp).setOnClickListener(this);
		layoutBubble = view.findViewById(R.id.layoutBubble);
		llServiceText = (LinearLayout) view.findViewById(R.id.llServiceText);
		linearPointer = (LinearLayout) view.findViewById(R.id.linearPointer);
		linearPointer2 = (LinearLayout) view.findViewById(R.id.linearPointer2);
		layoutRgService = view.findViewById(R.id.layoutRgService);
		tvEstimatedTime = (TextView) view.findViewById(R.id.tvEstimatedTime);
		tvDurationUnit = (TextView) view.findViewById(R.id.tvDurationUnit);

		tvNo = (TextView) view.findViewById(R.id.tvNo);
		ivCard = (ImageView) view.findViewById(R.id.ivCard);
		layoutCardDetails = (LinearLayout) view
				.findViewById(R.id.layoutCardDetails);
		layoutCardDetails.setOnClickListener(this);

		layoutFareQuote = (LinearLayout) view
				.findViewById(R.id.layoutFareQuote);
		view.findViewById(R.id.tvFareQuote).setOnClickListener(this);
		view.findViewById(R.id.btnFareInfo).setOnClickListener(this);

		if (activity.pHelper.getReferee() == 0) {
			showReferralDialog();
		}

		// sb = (SeekBar) view.findViewById(R.id.seekBar);
		// sb.setOnSeekBarChangeListener(this);
		pBar = (ProgressBar) view.findViewById(R.id.pBar);
		layoutDuration = (RelativeLayout) view
				.findViewById(R.id.layoutDuration);
		// etSource = (AutoCompleteTextView)
		// view.findViewById(R.id.etEnterSouce);
		// etDestination = (AutoCompleteTextView) view
		// .findViewById(R.id.etEnterDestination);
		layoutDestination = (LinearLayout) view
				.findViewById(R.id.layoutDestination);
		btnSendRequestDesti = (Button) view
				.findViewById(R.id.btnSendRequestDesti);
		btnSendRequestDesti.setOnClickListener(this);
		// view.findViewById(R.id.imgClearSource).setOnClickListener(this);
		// view.findViewById(R.id.imgClearDst).setOnClickListener(this);

		selectedPostion = 0;
		listViewType = (GridView) view.findViewById(R.id.gvTypes);

		mapFrameLayout = (FrameLayout) view.findViewById(R.id.mapFrameLayout);

		mapFrameLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN | MotionEvent.ACTION_MOVE:
					UberMapFragment.isMapTouched = true;
					layoutMarker.setVisibility(View.GONE);
					Log.e("Map", "Touch sdf -------------------------");
					break;

				case MotionEvent.ACTION_UP:
					UberMapFragment.isMapTouched = false;
					Log.e("Map", "NoTouch sdf -------------------------");
					break;
				}
				return true;
			}
		});

		btnMyLocation = (ImageButton) view.findViewById(R.id.btnMyLocation);
		btnSelectService = (MyFontButton) view
				.findViewById(R.id.btnSelectService);
		btnSelectService.setOnClickListener(this);

		drawer = (SlidingDrawer) view.findViewById(R.id.drawer);

		mMapView = (MapView) view.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded();
		preference = new PreferenceHelper(activity);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		IntentFilter filter = new IntentFilter(Const.INTENT_WALKER_STATUS);
		walkerReceiver = new WalkerStatusReceiver();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				walkerReceiver, filter);
		requestQueue = Volley.newRequestQueue(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity.layoutDestination.setVisibility(View.VISIBLE);
		activity.tvTitle.setVisibility(View.GONE);
		etSource = activity.etSource;
		activity.imgClearDst.setOnClickListener(this);
		adapter = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		adapterDestination = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etSource.setAdapter(adapter);
		locHelper = new LocationHelper(activity);
		locHelper.setLocationReceivedLister(this);

		// etDestination.setAdapter(adapterDestination);
		etSource.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final String selectedDestPlace = adapter.getItem(arg2);

				new Thread(new Runnable() {
					@Override
					public void run() {
						final LatLng latlng = getLocationFromAddress(selectedDestPlace);
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								isMapTouched = true;
								curretLatLng = latlng;
								isSource = true;
								setMarker(curretLatLng, isSource);
								setMarkerOnRoad(curretLatLng, curretLatLng);
								animateCameraToMarker(curretLatLng, true);
								stopUpdateProvidersLoaction();
								getAllProviders(curretLatLng);
							}
						});
					}
				}).start();
			}
		});

		// locHelper.setLocationReceivedLister(new OnLocationReceived() {
		// @Override
		// public void onLocationReceived(LatLng latlong) {
		//
		//
		// }
		// });

		locHelper.onStart();

		// etDestination.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// final String selectedDestPlace = adapterDestination
		// .getItem(arg2);
		//
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// final LatLng latlng = getLocationFromAddress(selectedDestPlace);
		// getActivity().runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// isSource = false;
		// setMarker(latlng, isSource);
		// setMarkerOnRoad(latlng, latlng);
		// }
		// });
		// }
		// }).start();
		// }
		// });
		listType = new ArrayList<VehicalType>();
		typeAdapter = new VehicalTypeListAdapter(activity, listType, this);
		listViewType.setAdapter(typeAdapter);
		getVehicalTypes();
		// drawer.lock();
		listViewType.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < listType.size(); i++)
					listType.get(i).isSelected = false;
				listType.get(position).isSelected = true;
				// btnSelectService.setCompoundDrawables(new , top, right,
				// bottom)
				// onItemClick(position);
				getAllProviders(curretLatLng);
				selectedPostion = position;
				typeAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.tvTitle.setText(getString(R.string.app_name));
		activity.btnNotification.setVisibility(View.INVISIBLE);
		etSource.setVisibility(View.VISIBLE);
		mMapView.onResume();
		startCheckingStatusUpdate();
	}

	private void showReferralDialog() {
		referralDialog = new Dialog(getActivity(), R.style.MyDialog);
		referralDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		referralDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		referralDialog.setContentView(R.layout.dialog_referral);
		referralDialog.setCancelable(false);
		etRefCode = (EditText) referralDialog.findViewById(R.id.etRefCode);
		llErrorMsg = (LinearLayout) referralDialog
				.findViewById(R.id.llErrorMsg);
		referralDialog.findViewById(R.id.btnRefSubmit).setOnClickListener(this);
		referralDialog.findViewById(R.id.btnSkip).setOnClickListener(this);
		referralDialog.show();

	}

	private void applyReffralCode(boolean isShowLoader) {
		if (isShowLoader)
			AndyUtils.showCustomProgressDialog(activity,
					getString(R.string.progress_loading), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.APPLY_REFFRAL_CODE);
		map.put(Const.Params.REFERRAL_CODE, etRefCode.getText().toString());
		map.put(Const.Params.ID, activity.pHelper.getUserId());
		map.put(Const.Params.TOKEN, activity.pHelper.getSessionToken());
		map.put(Const.Params.IS_SKIP, String.valueOf(is_skip));
		// new HttpRequester(activity, map,
		// Const.ServiceCode.APPLY_REFFRAL_CODE,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.APPLY_REFFRAL_CODE, this, this));

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			// map = ((SupportMapFragment) activity.getSupportFragmentManager()
			// .findFragmentById(R.id.map)).getMap();
			map = ((MapView) view.findViewById(R.id.map)).getMap();
			map.setMyLocationEnabled(false);
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.getUiSettings().setZoomControlsEnabled(false);
			map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location loc) {

				}
			});

			btnMyLocation.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Location loc = map.getMyLocation();
					if (myLocation != null) {
						LatLng latLang = new LatLng(myLocation.getLatitude(),
								myLocation.getLongitude());
						animateCameraToMarker(latLang, true);
					}
				}
			});

			map.setOnCameraChangeListener(new OnCameraChangeListener() {

				public void onCameraChange(CameraPosition camPos) {
					if (currentZoom == -1) {
						currentZoom = camPos.zoom;
					} else if (camPos.zoom != currentZoom) {
						currentZoom = camPos.zoom;
						return;
					}

					if (!isMapTouched) {
						curretLatLng = camPos.target;
						if (!isAddDestination) {
							layoutMarker.setVisibility(LinearLayout.VISIBLE);
							if (listType.size() > 0) {
								stopUpdateProvidersLoaction();
								getAllProviders(curretLatLng);
							}
							getAddressFromLocation(camPos.target, etSource);
						}
					}
					isMapTouched = false;
					// setMarker(camPos.target);
				}

			});
			if (map != null) {
				// Log.i("Map", "Map Fragment");
			}
		}

	}

	@Override
	public void onPause() {
		stopCheckingStatusUpdate();
		stopUpdateProvidersLoaction();
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroyView() {

		SupportMapFragment f = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) {
			try {
				getFragmentManager().beginTransaction().remove(f).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map = null;
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();

		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				walkerReceiver);
		// activity.tvTitle.setVisibility(View.VISIBLE);
		// etSource.setVisibility(View.GONE);
		etSource.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.markerBubblePickMeUp:
			if (isValidate()) {
				showPaymentOptionDialog();
			}
			break;
		case R.id.ivCredit:
			ivCredit.setSelected(true);
			ivCash.setSelected(false);
			paymentMode = 0;
			new PreferenceHelper(getActivity()).putPaymentMode(paymentMode);
			break;
		case R.id.ivCash:
			ivCredit.setSelected(false);
			ivCash.setSelected(true);
			paymentMode = 1;
			new PreferenceHelper(getActivity()).putPaymentMode(paymentMode);
			break;
		case R.id.btnSendRequest:
			dialog.dismiss();
			pickMeUp();
			break;
		case R.id.btnCancelRequest:
			dialog.dismiss();
			break;
		case R.id.btnAddDestination:
			if (!isAddDestination) {
				isAddDestination = true;
				isSource = true;
				setMarkerOnRoad(curretLatLng, curretLatLng);
				layoutDestination.setVisibility(View.VISIBLE);
				btnSendRequestDesti.setVisibility(View.VISIBLE);
				layoutFareQuote.setVisibility(View.VISIBLE);
				layoutBubble.setVisibility(View.GONE);
				llServiceText.setVisibility(View.GONE);
				layoutRgService.setVisibility(View.GONE);
			} else {
				if (markerSource != null) {
					markerSource.remove();
					markerSource = null;
				}
				if (markerDestination != null) {
					markerDestination.remove();
					markerDestination = null;
				}
				if (polyLine != null)
					polyLine.remove();

				isAddDestination = false;
				isSource = true;
				layoutDestination.setVisibility(View.GONE);
				layoutBubble.setVisibility(View.VISIBLE);
				btnSendRequestDesti.setVisibility(View.GONE);
				layoutFareQuote.setVisibility(View.GONE);
				llServiceText.setVisibility(View.VISIBLE);
				layoutRgService.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btnSendRequestDesti:
			showPaymentOptionDialog();
			break;
		case R.id.btnSelectService:
			if (drawer.isOpened()) {
				drawer.animateClose();
				drawer.unlock();
			} else {
				drawer.animateOpen();
				drawer.lock();
			}
			break;
		case R.id.layoutCardDetails:
			startActivity(new Intent(getActivity(),
					UberViewPaymentActivity.class));
			break;
		// case R.id.imgClearSource:
		// etSource.setText("");
		// break;
		case R.id.imgClearDst:
			etSource.setText("");
			break;
		case R.id.tvFareQuote:
			showFareQuote();
			break;
		case R.id.btnFareInfo:
			showVehicleDetails();
			break;
		case R.id.btnOKFareQuote:
			quoteDialog.dismiss();
			break;
		case R.id.tvGetFareEst:
			showDestinationPopup();
			break;
		case R.id.btnEditHome:
			layoutHomeEdit.setVisibility(LinearLayout.VISIBLE);
			layoutHomeText.setVisibility(LinearLayout.GONE);
			break;
		case R.id.btnEditWork:
			layoutWorkEdit.setVisibility(LinearLayout.VISIBLE);
			layoutWorkText.setVisibility(LinearLayout.GONE);
			break;
		case R.id.layoutHomeText:
			if (preference.getWorkAddress() != null)
				sendQuoteRequest(preference.getHomeAddress());
			break;
		case R.id.layoutWorkText:
			if (preference.getWorkAddress() != null)
				sendQuoteRequest(preference.getWorkAddress());
			break;
		case R.id.imgClearDest:
			etPopupDestination.setText("");
			break;
		case R.id.imgClearHome:
			etHomeAddress.setText("");
			break;
		case R.id.imgClearWork:
			etWorkAddress.setText("");
			break;
		case R.id.btnRefSubmit:
			if (etRefCode.getText().length() == 0) {
				AndyUtils.showToast(
						getResources().getString(R.string.text_blank_ref_code),
						activity);
				return;
			} else {
				if (!AndyUtils.isNetworkAvailable(activity)) {
					AndyUtils
							.showToast(
									getResources().getString(
											R.string.dialog_no_inter_message),
									activity);
					return;
				}
				is_skip = 0;
				applyReffralCode(true);
			}
			break;
		case R.id.btnSkip:
			is_skip = 1;
			applyReffralCode(false);
			activity.onBackPressed();
			break;
		// case R.id.tvRateCard:
		// AppLog.Log("", "clicked");
		// showRateCardDialog();
		// break;
		default:
			break;
		}
	}

	private void getAddressFromLocation(final LatLng latlng, final EditText et) {
		et.setText("Waiting for Address");
		et.setTextColor(Color.GRAY);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Geocoder gCoder = new Geocoder(getActivity());
				try {
					final List<Address> list = gCoder.getFromLocation(
							latlng.latitude, latlng.longitude, 1);
					if (list != null && list.size() > 0) {
						address = list.get(0);
						StringBuilder sb = new StringBuilder();
						if (address.getAddressLine(0) != null) {
							if (address.getMaxAddressLineIndex() > 0) {
								for (int i = 0; i < address
										.getMaxAddressLineIndex(); i++) {
									sb.append(address.getAddressLine(i))
											.append("\n");
								}
								sb.append(",");
								sb.append(address.getCountryName());
							} else {
								sb.append(address.getAddressLine(0));
							}
						}

						strAddress = sb.toString();
						strAddress = strAddress.replace(",null", "");
						strAddress = strAddress.replace("null", "");
						strAddress = strAddress.replace("Unnamed", "");
					}
					if (getActivity() == null)
						return;

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!TextUtils.isEmpty(strAddress)) {
								et.setFocusable(false);
								et.setFocusableInTouchMode(false);
								et.setText(strAddress);
								et.setTextColor(getResources().getColor(
										android.R.color.black));
								et.setFocusable(true);
								et.setFocusableInTouchMode(true);
							} else {
								et.setText("");
								et.setTextColor(getResources().getColor(
										android.R.color.black));
							}
							etSource.setEnabled(true);
						}
					});
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}).start();
	}

	private void animateCameraToMarker(LatLng latLng, boolean isAnimate) {
		try {
			etSource.setFocusable(false);
			etSource.setFocusableInTouchMode(false);
			CameraUpdate cameraUpdate = null;

			cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
			if (cameraUpdate != null && map != null) {
				if (isAnimate)
					map.animateCamera(cameraUpdate);
				else
					map.moveCamera(cameraUpdate);
			}
			etSource.setFocusable(true);
			etSource.setFocusableInTouchMode(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// perfect function...
	private void animateMarker(final Marker marker, final LatLng toPosition,
			final Location toLocation, final boolean hideMarker) {
		if (map == null || !this.isVisible() || marker == null
				|| marker.getPosition() == null) {
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

	private LatLng getLocationFromAddress(final String place) {
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

	@Override
	public void onProgressCancel() {
		stopCheckingStatusUpdate();
		cancleRequest();

		// stopCheckingStatusUpdate();
	}

	@Override
	protected boolean isValidate() {
		String msg = null;
		if (curretLatLng == null) {
			msg = getString(R.string.text_location_not_found);
		} else if (selectedPostion == -1) {
			msg = getString(R.string.text_select_type);
		} else if (TextUtils.isEmpty(etSource.getText().toString())
				|| etSource.getText().toString()
						.equalsIgnoreCase("Waiting for Address")) {
			msg = getString(R.string.text_waiting_for_address);
		}
		if (msg == null)
			return true;
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		return false;
	}

	private void pickMeUp() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		} else if (preference.getDefaultCard() == 0
				&& paymentMode == Const.CREDIT) {
			AndyUtils.showToast(getResources().getString(R.string.no_card),
					activity);
			return;
		}
		AndyUtils.showCustomProgressRequestDialog(activity,
				getString(R.string.text_creating_request), true, null);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put(Const.URL, Const.ServiceType.CREATE_REQUEST);
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(activity).getSessionToken());
		map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
		map.put(Const.Params.LATITUDE, String.valueOf(curretLatLng.latitude));
		map.put(Const.Params.LONGITUDE, String.valueOf(curretLatLng.longitude));
		map.put(Const.Params.PAYMENT_OPT,
				String.valueOf(new PreferenceHelper(activity).getPaymentMode()));
		map.put(Const.Params.TYPE,
				String.valueOf(listType.get(selectedPostion).getId()));
		map.put(Const.Params.CARD_ID,
				String.valueOf(new PreferenceHelper(activity).getDefaultCard()));
		map.put(Const.Params.DISTANCE, "1");
		if (markerDestination != null) {
			final LatLng dest = markerDestination.getPosition();

			map.put(Const.Params.DESTI_LATITUDE, String.valueOf(dest.latitude));
			map.put(Const.Params.DESTI_LONGITUDE,
					String.valueOf(dest.longitude));
		}
		// new HttpRequester(activity, map, Const.ServiceCode.CREATE_REQUEST,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.CREATE_REQUEST, this, this));
	}

	private void showPaymentOptionDialog() {
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.payment_option);

		ivCash = (ImageView) dialog.findViewById(R.id.ivCash);
		ivCredit = (ImageView) dialog.findViewById(R.id.ivCredit);
		ivCash.setOnClickListener(this);
		ivCredit.setOnClickListener(this);

		dialog.findViewById(R.id.btnCancelRequest).setOnClickListener(this);
		dialog.findViewById(R.id.btnSendRequest).setOnClickListener(this);

		paymentMode = (int) new PreferenceHelper(getActivity())
				.getPaymentMode();
		if (paymentMode == 1) {
			ivCredit.setSelected(false);
			ivCash.setSelected(true);
		} else {
			ivCredit.setSelected(true);
			ivCash.setSelected(false);
		}
		dialog.show();
	}

	@Override
	public void onTaskCompleted(final String response, int serviceCode) {
		super.onTaskCompleted(response, serviceCode);
		int driverId = 0;
		// AndyUtils.removeCustomProgressDialog();
		switch (serviceCode) {
		case Const.ServiceCode.CREATE_REQUEST:
			AppLog.Log(Const.TAG, "Create Request Response::::" + response);
			AndyUtils.removeCustomProgressRequestDialog();
			if (activity.pContent.isSuccess(response)) {

				activity.pHelper.putRequestId(activity.pContent
						.getRequestId(response));
				Driver driverInfo = activity.pContent.getDriverDetail(response);
				driverId = driverInfo.getDriverId();
				AndyUtils.showCustomProgressDialog(activity,
						getString(R.string.text_contacting), false, this,
						driverInfo);
				stopUpdateProvidersLoaction();
				startCheckingStatusUpdate();
			}

			break;
		case Const.ServiceCode.GET_REQUEST_STATUS:
			AppLog.Log(Const.TAG, "Get Request Response::::" + response);
			if (activity.pContent.isSuccess(response)) {
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:
				case Const.IS_WALKER_ARRIVED:
				case Const.IS_COMPLETED:
				case Const.IS_WALKER_STARTED:
					AndyUtils.removeCustomProgressRequestDialog();
					stopCheckingStatusUpdate();
					Driver driver = activity.pContent.getDriverDetail(response);
					if (this.isVisible())
						activity.gotoTripFragment(driver);
					break;
				case Const.IS_WALKER_RATED:
					stopCheckingStatusUpdate();
					if (this.isVisible())
						activity.gotoRateFragment(activity.pContent
								.getDriverDetail(response));
					break;

				case Const.IS_REQEUST_CREATED:
					if (activity.pHelper.getRequestId() != Const.NO_REQUEST) {
						Driver driverInfo = activity.pContent
								.getDriverDetail(response);
						AndyUtils.showCustomProgressDialog(activity,
								getString(R.string.text_contacting), false,
								this, driverInfo);

						stopUpdateProvidersLoaction();
					}
					isContinueRequest = true;
					break;
				case Const.NO_REQUEST:
					if (!isGettingVehicalType) {
						AndyUtils.removeCustomProgressDialog();
						startUpdateProvidersLocation();
					}
					stopCheckingStatusUpdate();
					break;
				default:
					isContinueRequest = false;
					break;
				}

			} else if (activity.pContent.getErrorCode(response) == Const.REQUEST_ID_NOT_FOUND) {
				AndyUtils.removeCustomProgressDialog();
				activity.pHelper.clearRequestData();
				isContinueRequest = false;
			} else if (activity.pContent.getErrorCode(response) == Const.INVALID_TOKEN) {
				if (activity.pHelper.getLoginBy()
						.equalsIgnoreCase(Const.MANUAL))
					login();
				else
					loginSocial(activity.pHelper.getUserId(),
							activity.pHelper.getLoginBy());
			} else {
				isContinueRequest = true;
			}
			break;
		case Const.ServiceCode.CANCEL_REQUEST:
			if (activity.pContent.isSuccess(response)) {

			}
			activity.pHelper.clearRequestData();
			AndyUtils.removeCustomProgressRequestDialog();
			break;
		case Const.ServiceCode.GET_VEHICAL_TYPES:
			if (activity.pContent.isSuccess(response)) {
				listType.clear();
				activity.pContent.parseTypes(response, listType);
				pointer = listType.size();
				// if (pointer <= 1 && pointer > 4)
				// sb.setEnabled(false);
				isGettingVehicalType = false;
				if (listType.size() > 0) {
					if (listType != null && listType.get(0) != null)
						listType.get(0).isSelected = true;
					typeAdapter.notifyDataSetChanged();
				}

			}
			AndyUtils.removeCustomProgressDialog();
			break;
		case Const.ServiceCode.GET_PROVIDERS:
			try {
				map.getUiSettings().setScrollGesturesEnabled(true);
				AppLog.Log("", "Provider Response : " + response);
				if (new JSONObject(response).getBoolean("success")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							listDriver = new ArrayList<Driver>();
							listDriver = activity.pContent
									.parseNearestDrivers(response);
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setProvirderOnMap();
								}
							});
						}
					}).start();
				} else {
					map.clear();
				}
			} catch (Exception e) {
			}
			break;
		case Const.ServiceCode.GET_DURATION:
			AppLog.Log("", "Duration Response : " + response);
			pBar.setVisibility(View.GONE);
			layoutDuration.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(response)) {
				estimatedTimeTxt = activity.pContent
						.parseNearestDriverDurationString(response);
				String[] durationArr = estimatedTimeTxt.split(" ");
				tvEstimatedTime.setText(durationArr[0]);
				tvDurationUnit.setText(durationArr[1]);
			}
			break;

		case Const.ServiceCode.DRAW_PATH_ROAD:
			if (!TextUtils.isEmpty(response)) {
				route = new Route();
				activity.pContent.parseRoute(response, route);

				final ArrayList<Step> step = route.getListStep();
				System.out.println("step size=====> " + step.size());
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				for (int i = 0; i < step.size(); i++) {
					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					points.addAll(path);
				}
				if (points != null && points.size() > 0) {
					setMarker(new LatLng(points.get(0).latitude,
							points.get(0).longitude), isSource);
					if (isSource) {
						getAddressFromLocation(
								new LatLng(points.get(0).latitude,
										points.get(0).longitude), etSource);
					}
					// else {
					// getAddressFromLocation(
					// new LatLng(points.get(0).latitude,
					// points.get(0).longitude), etDestination);
					// }
					if (markerSource != null && markerDestination != null) {
						showDirection(markerSource.getPosition(),
								markerDestination.getPosition());
					}
				}
			}
			break;
		case Const.ServiceCode.DRAW_PATH:
			if (!TextUtils.isEmpty(response)) {
				route = new Route();
				activity.pContent.parseRoute(response, route);

				final ArrayList<Step> step = route.getListStep();
				System.out.println("step size=====> " + step.size());
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				for (int i = 0; i < step.size(); i++) {

					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					points.addAll(path);

				}

				if (polyLine != null)
					polyLine.remove();
				lineOptions.addAll(points);
				lineOptions.width(10);
				lineOptions.color(Color.BLUE); // #00008B rgb(0,0,139)

				if (lineOptions != null && map != null) {
					polyLine = map.addPolyline(lineOptions);

					LatLngBounds.Builder bld = new LatLngBounds.Builder();

					bld.include(markerSource.getPosition());
					bld.include(markerDestination.getPosition());
					LatLngBounds latLngBounds = bld.build();
					map.moveCamera(CameraUpdateFactory.newLatLngBounds(
							latLngBounds, 30));
				}
			}
			break;
		// case Const.ServiceCode.GET_QUOTE:
		// if (!TextUtils.isEmpty(response)) {
		// try {
		// quoteDialog = new Dialog(getActivity());
		// quoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// quoteDialog.setContentView(R.layout.farequote_popup);
		// quoteDialog.getWindow().setLayout(
		// LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		//
		// txtTotalFareQuate = (TextView) quoteDialog
		// .findViewById(R.id.txtTotalFareQuate);
		// quoteDialog.findViewById(R.id.btnOKFareQuote)
		// .setOnClickListener(this);
		//
		// JSONArray jsonArray = new JSONObject(response)
		// .getJSONArray("routes");
		// JSONArray jArrSub = jsonArray.getJSONObject(0)
		// .getJSONArray("legs");
		// JSONObject legObj = jArrSub.getJSONObject(0);
		//
		// JSONObject durationObj = legObj.getJSONObject("duration");
		// JSONObject distanceObj = legObj.getJSONObject("distance");
		//
		// double minute = durationObj.getDouble("value") / 60;
		// double kms = distanceObj.getDouble("value") / 1000;
		//
		// AppLog.Log("TAG",
		// "Duration Seconds: " + durationObj.getLong("value"));
		// AppLog.Log("TAG",
		// "Distance meter: " + distanceObj.getLong("value"));
		//
		// AppLog.Log("TAG", "Duration kms: " + kms);
		// AppLog.Log("TAG", "Distance minute: " + minute);
		//
		// // String totalQuote = MathUtils.getRound(preference
		// // .getBasePrice()
		// // + (preference.getDistancePrice() * kms)
		// // + (preference.getTimePrice() * minute));
		// // AppLog.Log("TAG", "totalQuote: " + totalQuote);
		// txtTotalFareQuate.setText(activity
		// .getString(R.string.payment_unit)
		// + getFareCalculation(kms));
		//
		// AndyUtils.removeCustomProgressDialog();
		// quoteDialog.show();
		// } catch (Exception e) {
		// AndyUtils.removeCustomProgressDialog();
		// AppLog.Log("UberMapFragment", "" + e);
		// }
		// }
		// break;
		case Const.ServiceCode.GET_FARE_QUOTE:
			if (!TextUtils.isEmpty(response)) {
				try {
					JSONArray jsonArray = new JSONObject(response)
							.getJSONArray("routes");
					JSONArray jArrSub = jsonArray.getJSONObject(0)
							.getJSONArray("legs");
					JSONObject legObj = jArrSub.getJSONObject(0);

					JSONObject durationObj = legObj.getJSONObject("duration");
					JSONObject distanceObj = legObj.getJSONObject("distance");

					double minute = durationObj.getDouble("value") / 60;
					double kms = distanceObj.getDouble("value") / 1000;

					AppLog.Log("TAG",
							"Duration Seconds: " + durationObj.getLong("value"));
					AppLog.Log("TAG",
							"Distance meter: " + distanceObj.getLong("value"));

					AppLog.Log("TAG", "Duration kms: " + kms);
					AppLog.Log("TAG", "Distance minute: " + minute);

					// String totalQuote = MathUtils.getRound(basePrice
					// + (preference.getDistancePrice() * kms)
					// + (preference.getTimePrice() * minute));
					// AppLog.Log("TAG", "totalQuote: " + totalQuote);
					pbMinFare.setVisibility(View.GONE);
					tvTotalFare.setVisibility(View.VISIBLE);
					tvTotalFare.setText(getString(R.string.payment_unit)
							+ getFareCalculation(kms));
				} catch (Exception e) {
					AppLog.Log("UberMapFragment=====",
							"GET_FARE_QUOTE Response: " + e);
				}
			}
			break;
		case Const.ServiceCode.GET_NEAR_BY:
			AppLog.Log("TAG", "Near by : " + response);
			pbNearby.setVisibility(View.GONE);
			nearByList.setVisibility(View.VISIBLE);
			ArrayList<String> resultList = new ArrayList<String>();
			activity.pContent.parseNearByPlaces(response, resultList);
			nearByAd = new ArrayAdapter<String>(getActivity(),
					R.layout.autocomplete_list_text, R.id.tvPlace, resultList);
			nearByList.setAdapter(nearByAd);
			break;
		case Const.ServiceCode.UPDATE_PROVIDERS:
			AppLog.Log("Mapfragment", "UPDATE_PROVIDERS : " + response);
			try {
				if (new JSONObject(response).getBoolean("success")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							listUpdatedDriver = new ArrayList<Driver>();
							listUpdatedDriver = activity.pContent
									.parseNearestDrivers(response);
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									updateProviderOnMap();
								}
							});
						}
					}).start();
				} else {
					map.clear();
				}
			} catch (Exception e) {
			}
			break;
		case Const.ServiceCode.APPLY_REFFRAL_CODE:
			AndyUtils.removeCustomProgressDialog();
			AppLog.Log(Const.TAG, "Referral Response: " + response);
			if (new ParseContent(activity).isSuccess(response)) {
				new PreferenceHelper(activity).putReferee(1);
				referralDialog.dismiss();
				// activity.startActivity(new Intent(activity,
				// MainDrawerActivity.class));
			} else {
				llErrorMsg.setVisibility(View.VISIBLE);
				etRefCode.requestFocus();
			}
			break;
		}
	}

	private class TimerRequestStatus extends TimerTask {
		@Override
		public void run() {
			if (isContinueRequest) {
				isContinueRequest = false;
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

	private void startCheckingStatusUpdate() {
		stopCheckingStatusUpdate();
		if (activity.pHelper.getRequestId() != Const.NO_REQUEST) {
			isContinueRequest = true;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerRequestStatus(), Const.DELAY,
					Const.TIME_SCHEDULE);
		}
	}

	private void stopCheckingStatusUpdate() {
		isContinueRequest = false;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void cancleRequest() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		AndyUtils.removeCustomProgressRequestDialog();
		AndyUtils.showCustomProgressRequestDialog(activity,
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

	class WalkerStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String response = intent.getStringExtra(Const.EXTRA_WALKER_STATUS);
			if (TextUtils.isEmpty(response))
				return;
			stopCheckingStatusUpdate();

			if (activity.pContent.isSuccess(response)) {
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:
				case Const.IS_WALKER_ARRIVED:
				case Const.IS_COMPLETED:
				case Const.IS_WALKER_STARTED:
					AppLog.Log("Map", "Remove ");
					AndyUtils.removeCustomProgressRequestDialog();
					// stopCheckingStatusUpdate();
					Driver driver = activity.pContent.getDriverDetail(response);
					removeThisFragment();
					if (UberMapFragment.this.isVisible())
						activity.gotoTripFragment(driver);
					break;
				case Const.IS_WALKER_RATED:
					// stopCheckingStatusUpdate();
					if (UberMapFragment.this.isVisible())
						activity.gotoRateFragment(activity.pContent
								.getDriverDetail(response));
					break;

				case Const.IS_REQEUST_CREATED:
					Driver driverInfo = activity.pContent
							.getDriverDetail(response);
					AndyUtils.showCustomProgressDialog(activity,
							getString(R.string.text_contacting), false,
							UberMapFragment.this, driverInfo);
					startCheckingStatusUpdate();
					isContinueRequest = true;
					break;
				default:
					isContinueRequest = false;
					break;
				}

			} else if (activity.pContent.getErrorCode(response) == Const.REQUEST_ID_NOT_FOUND) {
				AndyUtils.removeCustomProgressDialog();
				activity.pHelper.clearRequestData();
				isContinueRequest = false;
			} else if (activity.pContent.getErrorCode(response) == Const.INVALID_TOKEN) {
				if (activity.pHelper.getLoginBy()
						.equalsIgnoreCase(Const.MANUAL))
					login();
				else
					loginSocial(activity.pHelper.getUserId(),
							activity.pHelper.getLoginBy());
			} else {
				isContinueRequest = true;
				startCheckingStatusUpdate();
			}
			// startCheckingStatusUpdate();
		}
	}

	private void removeThisFragment() {
		try {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(this).commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getVehicalTypes() {
		AndyUtils.showCustomProgressDialog(activity, "loading", false, null);
		isGettingVehicalType = true;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.GET_VEHICAL_TYPES);
		AppLog.Log(Const.TAG, Const.URL);
		// new HttpRequester(activity, map, Const.ServiceCode.GET_VEHICAL_TYPES,
		// true, this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_VEHICAL_TYPES, this, this));
	}

	public void onItemClick(int pos) {
		selectedPostion = pos;
	}

	private void getAllProviders(LatLng latlang) {
		try {
			// map.getUiSettings().setScrollGesturesEnabled(false);
			estimatedTimeTxt = "";
			pBar.setVisibility(View.VISIBLE);
			layoutDuration.setVisibility(View.GONE);
			if (!AndyUtils.isNetworkAvailable(activity)) {
				AndyUtils.showToast(
						getResources().getString(R.string.no_internet),
						activity);
				return;
			} else if (latlang == null) {
				Toast.makeText(activity,
						getString(R.string.text_location_not_found),
						Toast.LENGTH_SHORT).show();
				return;
			}
			AppLog.Log("TAG", "Provider lat : " + latlang.latitude + " Long :"
					+ latlang.longitude);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.GET_PROVIDERS);
			map.put(Const.Params.ID,
					String.valueOf(activity.pHelper.getUserId()));
			map.put(Const.Params.TOKEN,
					String.valueOf(activity.pHelper.getSessionToken()));
			map.put(Const.Params.USER_LATITUDE,
					String.valueOf(latlang.latitude));
			map.put(Const.Params.USER_LONGITUDE,
					String.valueOf(latlang.longitude));
			// new HttpRequester(activity, map, Const.ServiceCode.GET_PROVIDERS,
			// this);
			requestQueue.add(new VolleyHttpRequest(Method.POST, map,
					Const.ServiceCode.GET_PROVIDERS, this, this));
		} catch (Exception e) {
			AppLog.Log("TAG", "getAllProviderException : " + e);
		}
	}

	private void updateAllProviders(LatLng latlang) {
		try {
			if (!AndyUtils.isNetworkAvailable(activity)) {
				return;
			} else if (latlang == null) {
				return;
			}
			AppLog.Log("TAG", "Update Provider lat : " + latlang.latitude
					+ " Long :" + latlang.longitude);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.GET_PROVIDERS);
			map.put(Const.Params.ID,
					String.valueOf(activity.pHelper.getUserId()));
			map.put(Const.Params.TOKEN,
					String.valueOf(activity.pHelper.getSessionToken()));
			map.put(Const.Params.USER_LATITUDE,
					String.valueOf(latlang.latitude));
			map.put(Const.Params.USER_LONGITUDE,
					String.valueOf(latlang.longitude));
			// new HttpRequester(activity, map,
			// Const.ServiceCode.UPDATE_PROVIDERS, this);
			requestQueue.add(new VolleyHttpRequest(Method.POST, map,
					Const.ServiceCode.UPDATE_PROVIDERS, this, this));
		} catch (Exception e) {
			AppLog.Log("TAG", "updateAllProviderException : " + e);
		}
	}

	private void getDuration(LatLng origin, String lat, String lng) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		} else if (origin == null) {
			return;
		}
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;
		String str_dest = "destination=" + lat + "," + lng;
		String sensor = "sensor=false";
		String parameters = str_origin + "&" + str_dest + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters + "&key="
				+ Const.PLACES_AUTOCOMPLETE_API_KEY;
		AppLog.Log("MapFragment", "Url : " + url);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, url);
		// new HttpRequester(activity, map, Const.ServiceCode.GET_DURATION,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_DURATION, this, this));
	}

	// private void getCommonFareQuote(String source, String destination) {
	// AndyUtils.showCustomProgressDialog(activity,
	// getString(R.string.text_please_wait), false, this);
	// if (!AndyUtils.isNetworkAvailable(activity)) {
	// AndyUtils.showToast(getResources().getString(R.string.no_internet),
	// activity);
	// } else {
	// LatLng origin = getLocationFromAddress(source);
	// LatLng dest = getLocationFromAddress(destination);
	//
	// String str_origin = "origin=" + origin.latitude + ","
	// + origin.longitude;
	// String str_dest = "destination=" + dest.latitude + ","
	// + dest.longitude;
	// String sensor = "sensor=false";
	// String parameters = str_origin + "&" + str_dest + "&" + sensor;
	// String output = "json";
	// String url = "https://maps.googleapis.com/maps/api/directions/"
	// + output + "?" + parameters;
	// AppLog.Log("MapFragment", "Url : " + url);
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put(Const.URL, url);
	// // new HttpRequester(activity, map, Const.ServiceCode.GET_QUOTE,
	// // this);
	// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
	// Const.ServiceCode.GET_ROUTE, this, this));
	// }
	// }

	private void getFareQuote(LatLng origin, String destination) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
		} else {
			try {
				LatLng dest = getLocationFromAddress(destination);

				String str_origin = "origin=" + origin.latitude + ","
						+ origin.longitude;
				String str_dest = "destination=" + dest.latitude + ","
						+ dest.longitude;
				String sensor = "sensor=false";
				String parameters = str_origin + "&" + str_dest + "&" + sensor;
				String output = "json";
				String url = "https://maps.googleapis.com/maps/api/directions/"
						+ output + "?" + parameters + "&key="
						+ Const.PLACES_AUTOCOMPLETE_API_KEY;
				AppLog.Log("MapFragment", "Url : " + url);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(Const.URL, url);
				// new HttpRequester(activity, map,
				// Const.ServiceCode.GET_FARE_QUOTE, this);
				requestQueue.add(new VolleyHttpRequest(Method.POST, map,
						Const.ServiceCode.GET_FARE_QUOTE, this, this));
			} catch (Exception e) {

			}
		}
	}

	private void setMarker(LatLng latLng, boolean isSource) {
		if (!UberMapFragment.this.isVisible())
			return;
		if (getActivity() != null && getActivity().getCurrentFocus() != null) {
			// inputMethodManager.hideSoftInputFromWindow(getActivity()
			// .getCurrentFocus().getWindowToken(), 0);
			activity.hideKeyboard();
		}

		if (latLng != null && map != null) {
			if (isSource) {
				if (markerSource == null) {
					markerSource = map.addMarker(new MarkerOptions()
							.position(
									new LatLng(latLng.latitude,
											latLng.longitude))
							.title(getResources().getString(
									R.string.text_source_pin_title))
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.pin_client_org)));
					// markerSource.setDraggable(true);
				} else {
					markerSource.setPosition(latLng);
				}
				CameraUpdateFactory.newLatLng(latLng);
			} else {
				if (markerDestination == null) {
					MarkerOptions opt = new MarkerOptions();
					opt.position(latLng);
					opt.title(getResources().getString(
							R.string.text_destination_pin_title));
					opt.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.destination_pin));
					markerDestination = map.addMarker(opt);

					markerDestination.setDraggable(true);

					if (markerSource != null) {
						LatLngBounds.Builder bld = new LatLngBounds.Builder();

						bld.include(new LatLng(
								markerSource.getPosition().latitude,
								markerSource.getPosition().longitude));
						bld.include(new LatLng(
								markerDestination.getPosition().latitude,
								markerDestination.getPosition().longitude));
						LatLngBounds latLngBounds = bld.build();
						map.moveCamera(CameraUpdateFactory.newLatLngBounds(
								latLngBounds, 30));
					} else {
						CameraUpdateFactory.newLatLng(latLng);
					}

				} else {
					markerDestination.setPosition(latLng);
				}
			}
		} else {
			Toast.makeText(getActivity(), "Unable to get location..!",
					Toast.LENGTH_LONG).show();
		}
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

		// new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH_ROAD,
		// true, this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.DRAW_PATH_ROAD, this, this));
	}

	private void showDirection(LatLng source, LatLng destination) {
		if (source == null || destination == null) {
			return;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				"http://maps.googleapis.com/maps/api/directions/json?origin="
						+ source.latitude + "," + source.longitude
						+ "&destination=" + destination.latitude + ","
						+ destination.longitude + "&sensor=false");
		// new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH, true,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.DRAW_PATH, this, this));
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		start = seekBar.getProgress();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// if (start == seekBar.getProgress()
		// || (start <= seekBar.getProgress() + 3 && start >= seekBar
		// .getProgress() - 3)) {
		// seekBar.setProgress(start);
		// if (TextUtils.isEmpty(etSource.getText().toString())
		// || etSource.getText().toString()
		// .equalsIgnoreCase("Waiting for Address")) {
		// AndyUtils.showToast(
		// getActivity().getString(
		// R.string.text_waiting_for_address),
		// getActivity());
		// } else if (TextUtils.isEmpty(estimatedTimeTxt)) {
		// AndyUtils.showToast(
		// getActivity().getString(R.string.text_waiting_for_eta),
		// getActivity());
		// } else {
		// showVehicleDetails();
		// }
		// return;
		// }
		// if (pointer == 4) {
		// fourPointer();
		// } else if (pointer == 3) {
		// threePointer();
		// } else if (pointer == 2) {
		// twoPointer();
		// }
		// AppLog.Log("Mapfragment", "Selected Service : " + selectedPostion);
		// if (listDriver.size() > 0) {
		// activity.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// setProvirderOnMap();
		// }
		// });
		// } else {
		// pBar.setVisibility(View.VISIBLE);
		// }
	}

	// private void twoPointer() {
	// value = sb.getProgress();
	//
	// if (value >= 50) {
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 100);
	// } else if (value < 50) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }
	//
	// private void threePointer() {
	// value = sb.getProgress();
	// if (value < 25) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// } else if (value >= 25 && value < 75) {
	// // Log.e("value", "" + value);
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 50);
	// } else if (value >= 75) {
	// selectedPostion = 2;
	// anim = ValueAnimator.ofInt(value, 100);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }
	//
	// private void fourPointer() {
	// value = sb.getProgress();
	//
	// if (value <= 17) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// } else if (value > 17 && value <= 50) {
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 33);
	// } else if (value > 50 && value <= 83) {
	// selectedPostion = 2;
	// anim = ValueAnimator.ofInt(value, 66);
	// } else if (value >= 83) {
	// selectedPostion = 3;
	// anim = ValueAnimator.ofInt(value, 100);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }

	private void setProvirderOnMap() {
		VehicalType vehicle = null;

		if (listType != null && listType.size() > selectedPostion) {
			vehicle = listType.get(selectedPostion);
		}
		if (vehicle == null) {
			return;
		}
		if (map != null) {
			map.clear();
		}

		nearDriverMarker = new HashMap<Integer, Marker>();
		for (int i = 0; i < listDriver.size(); i++) {
			Driver driver = listDriver.get(i);
			if (vehicle.getId() == driver.getVehicleTypeId()) {
				MarkerOptions mo = new MarkerOptions();
				mo.flat(true);
				mo.anchor(0.5f, 0.5f);
				mo.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.pin_driver));
				mo.title(getString(R.string.text_drive_location));
				mo.position(new LatLng(driver.getLatitude(), driver
						.getLongitude()));

				nearDriverMarker.put(driver.getDriverId(), map.addMarker(mo));
			}
		}

		boolean isGetProvider = false;
		for (int i = 0; i < listDriver.size(); i++) {
			Driver driver = listDriver.get(i);
			if (vehicle.getId() == driver.getVehicleTypeId()) {
				isGetProvider = true;
				getDuration(curretLatLng, String.valueOf(driver.getLatitude()),
						String.valueOf(driver.getLongitude()));
				break;
			}
		}
		if (!isGetProvider) {
			layoutDuration.setVisibility(View.GONE);
			pBar.setVisibility(View.VISIBLE);
		}
		startUpdateProvidersLocation();
	}

	private void updateProviderOnMap() {
		try {
			VehicalType vehicle = listType.get(selectedPostion);
			for (int i = 0; i < listDriver.size(); i++) {
				Driver driver = listDriver.get(i);
				if (vehicle.getId() == driver.getVehicleTypeId()) {
					for (int j = 0; j < listUpdatedDriver.size(); j++) {
						Driver updatedDriver = listUpdatedDriver.get(j);
						if (driver.getDriverId() == updatedDriver.getDriverId()) {
							Location driverLocation = new Location("");
							driverLocation.setLatitude(updatedDriver
									.getLatitude());
							driverLocation.setLongitude(updatedDriver
									.getLongitude());
							driverLocation.setBearing((float) updatedDriver
									.getBearing());
							animateMarker(nearDriverMarker.get(i),
									new LatLng(updatedDriver.getLatitude(),
											updatedDriver.getLongitude()),
									driverLocation, false);
							break;
						}
					}
				}
			}

			boolean isGetProvider = false;
			for (int i = 0; i < listUpdatedDriver.size(); i++) {
				Driver driver = listUpdatedDriver.get(i);
				if (vehicle.getId() == driver.getVehicleTypeId()) {
					isGetProvider = true;
					getDuration(curretLatLng,
							String.valueOf(driver.getLatitude()),
							String.valueOf(driver.getLongitude()));
					break;
				}
			}
			listDriver.clear();
			listDriver.addAll(listUpdatedDriver);
			if (!isGetProvider) {
				layoutDuration.setVisibility(View.GONE);
				pBar.setVisibility(View.VISIBLE);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showFareQuote() {
		// if (etSource.getText().toString().trim().length() > 0) {
		// if (etDestination.getText().toString().trim().length() > 0)
		// getCommonFareQuote(etSource.getText().toString(), etDestination
		// .getText().toString());
		// else
		// AndyUtils.showToast("Please set destination address",
		// getActivity());
		// } else
		// AndyUtils.showToast("Please set source address", getActivity());
	}

	private void showVehicleDetails() {
		if (listType != null && listType.size() > 0) {
			VehicalType vehicle = (VehicalType) listType.get(selectedPostion);
			AppLog.Log("", "MAX:" + vehicle.getMaxSize());
			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.vehicle_details);
			dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			tvMaxSize = (TextView) dialog.findViewById(R.id.tvMaxSize);
			tvMinFare = (TextView) dialog.findViewById(R.id.tvMinFare);
			tvLblMinFare = (TextView) dialog.findViewById(R.id.tvLblMinFare);
			tvETA = (TextView) dialog.findViewById(R.id.tvETA);
			tvGetFareEst = (TextView) dialog.findViewById(R.id.tvGetFareEst);
			pbMinFare = (ProgressBar) dialog.findViewById(R.id.pbMinFare);
			tvTotalFare = (TextView) dialog.findViewById(R.id.tvTotalFare);
			tvRateCard = (TextView) dialog.findViewById(R.id.tvRateCard);
			tvRateCard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showRateCardDialog();
				}
			});
			tvGetFareEst.setOnClickListener(this);
			tvETA.setText(estimatedTimeTxt);
			tvMaxSize.setText(vehicle.getMaxSize() + " PERSON");
			tvMinFare.setText(activity.getString(R.string.payment_unit)
					+ vehicle.getMinFare());
			dialog.show();
		}
	}

	private void showRateCardDialog() {
		VehicalType vehicle = (VehicalType) listType.get(selectedPostion);
		rateCardDialog = new Dialog(getActivity());
		rateCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		rateCardDialog.setContentView(R.layout.dialog_rate_card);
		rateCardDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		TextView tvRateVehicleTypeName, tvRateBasePrice, tvRateDistanceCost, tvRateTimeCost;
		tvRateVehicleTypeName = (TextView) rateCardDialog
				.findViewById(R.id.tvRateVehicleTypeName);
		tvRateBasePrice = (TextView) rateCardDialog
				.findViewById(R.id.tvRateBasePrice);
		tvRateDistanceCost = (TextView) rateCardDialog
				.findViewById(R.id.tvRateDistanceCost);
		tvRateTimeCost = (TextView) rateCardDialog
				.findViewById(R.id.tvRateTimeCost);
		tvRateVehicleTypeName.setText(vehicle.getName());

		tvRateBasePrice.setText(getString(R.string.payment_unit)
				+ (int) vehicle.getBasePrice() + " "
				+ getString(R.string.text_for) + " "
				+ vehicle.getBaseDistance() + vehicle.getUnit());
		tvRateDistanceCost.setText(getString(R.string.payment_unit)
				+ (int) vehicle.getPricePerUnitDistance()
				+ getString(R.string.text_per) + vehicle.getUnit());
		tvRateTimeCost.setText(getString(R.string.payment_unit)
				+ (int) vehicle.getPricePerUnitTime()
				+ getString(R.string.text_per) + getString(R.string.text_min));

		// tvRateDistanceCost.setText(getString(R.string.payment_unit)
		// + preference.getDistancePrice() + "/"
		// + getString(R.string.text_miles));
		// tvRateTimeCost.setText(getString(R.string.payment_unit)
		// + preference.getTimePrice() + "/"
		// + getString(R.string.text_mins));
		rateCardDialog.show();
	}

	private void showDestinationPopup() {
		destinationDialog = new Dialog(getActivity());
		destinationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		destinationDialog.setContentView(R.layout.destination_popup);
		etPopupDestination = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etPopupDestination);
		etHomeAddress = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etHomeAddress);
		etWorkAddress = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etWorkAddress);
		tvHomeAddress = (TextView) destinationDialog
				.findViewById(R.id.tvHomeAddress);
		tvWorkAddress = (TextView) destinationDialog
				.findViewById(R.id.tvWorkAddress);
		tvHomeAddress.setText(preference.getHomeAddress());
		tvWorkAddress.setText(preference.getWorkAddress());
		etHomeAddress.setText(preference.getHomeAddress());
		etWorkAddress.setText(preference.getWorkAddress());

		layoutHomeText = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutHomeText);
		layoutHomeEdit = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutHomeEdit);
		layoutWorkText = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutWorkText);
		layoutWorkEdit = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutWorkEdit);
		layoutHomeText.setOnClickListener(this);
		layoutWorkText.setOnClickListener(this);
		destinationDialog.findViewById(R.id.imgClearDest).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.imgClearHome).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.imgClearWork).setOnClickListener(
				this);

		destinationDialog.findViewById(R.id.btnEditHome).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.btnEditWork).setOnClickListener(
				this);
		nearByList = (ListView) destinationDialog.findViewById(R.id.nearByList);
		pbNearby = (ProgressBar) destinationDialog.findViewById(R.id.pbNearby);

		adapterPopUpDestination = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etPopupDestination.setAdapter(adapterPopUpDestination);
		adapterHomeAddress = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etHomeAddress.setAdapter(adapterHomeAddress);
		adapterWorkAddress = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etWorkAddress.setAdapter(adapterWorkAddress);
		destinationDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		etPopupDestination.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sendQuoteRequest(adapterPopUpDestination.getItem(arg2));
			}
		});
		etHomeAddress.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedPlace = adapterHomeAddress.getItem(arg2);
				preference.putHomeAddress(selectedPlace);
				tvHomeAddress.setText(selectedPlace);
				layoutHomeEdit.setVisibility(LinearLayout.GONE);
				layoutHomeText.setVisibility(LinearLayout.VISIBLE);
			}
		});
		etWorkAddress.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedPlace = adapterWorkAddress.getItem(arg2);
				preference.putWorkAddress(selectedPlace);
				tvWorkAddress.setText(selectedPlace);
				layoutWorkEdit.setVisibility(LinearLayout.GONE);
				layoutWorkText.setVisibility(LinearLayout.VISIBLE);
			}
		});
		nearByLocations();
		nearByList.setOnItemClickListener(this);
		destinationDialog.show();
	}

	private void sendQuoteRequest(String destination) {
		pbMinFare.setVisibility(View.VISIBLE);
		tvMinFare.setVisibility(View.GONE);
		tvLblMinFare.setVisibility(View.GONE);
		tvTotalFare.setVisibility(View.GONE);
		tvGetFareEst.setText(destination);
		getFareQuote(curretLatLng, destination);
		destinationDialog.dismiss();
	}

	private void nearByLocations() {
		StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
				+ Const.TYPE_NEAR_BY + Const.OUT_JSON);
		sb.append("?sensor=true&key=" + Const.PLACES_AUTOCOMPLETE_API_KEY);
		sb.append("&location=" + curretLatLng.latitude + ","
				+ curretLatLng.longitude);
		sb.append("&radius=500");
		AppLog.Log("", "Near location Url : " + sb.toString());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, sb.toString());
		// new HttpRequester(activity, map, Const.ServiceCode.GET_NEAR_BY, true,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.GET, map,
				Const.ServiceCode.GET_NEAR_BY, this, this));

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		sendQuoteRequest(nearByAd.getItem(arg2) + ", " + address.getLocality()
				+ ", " + address.getAdminArea() + ", "
				+ address.getCountryName());
	}

	private void startUpdateProvidersLocation() {
		timerProvidersLocation = new Timer();
		timerProvidersLocation.scheduleAtFixedRate(new TrackLocation(), 0,
				LOCATION_SCHEDULE);
	}

	private void stopUpdateProvidersLoaction() {
		if (timerProvidersLocation != null) {
			timerProvidersLocation.cancel();
			timerProvidersLocation = null;
		}
	}

	class TrackLocation extends TimerTask {
		public void run() {
			// if (isContinueDriverRequest) {
			// isContinueDriverRequest = false;
			// getDriverLocation();
			// }
			updateAllProviders(curretLatLng);
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

		if (location != null) {
			// drawTrip(latlong);
			myLocation = location;
		}
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

			// isLocationEnable = true;
			LatLng latLang = new LatLng(location.getLatitude(),
					location.getLongitude());
			curretLatLng = latLang;
			getAllProviders(latLang);
			animateCameraToMarker(latLang, false);
		} else {
			activity.showLocationOffDialog();
		}
	}

	private String getFareCalculation(double distanceInKm) {
		VehicalType vehicle = (VehicalType) listType.get(selectedPostion);
		double basePrice = vehicle.getBasePrice();
		int baseDistance = vehicle.getBaseDistance();
		double distanceCost = vehicle.getPricePerUnitDistance();
		// double timeCost = Double.parseDouble(vehicle.getPricePerUnitTime());

		double fare = (distanceInKm - baseDistance) * distanceCost + basePrice;
		DecimalFormat format = new DecimalFormat("0.00");
		String finalFare = format.format(fare);

		return finalFare;
	}
}