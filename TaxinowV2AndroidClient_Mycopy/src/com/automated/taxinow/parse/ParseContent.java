package com.automated.taxinow.parse;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;

import com.automated.taxinow.R;
import com.automated.taxinow.db.DBHelper;
import com.automated.taxinow.maputils.PolyLineUtils;
import com.automated.taxinow.models.ApplicationPages;
import com.automated.taxinow.models.Bill;
import com.automated.taxinow.models.Card;
import com.automated.taxinow.models.Driver;
import com.automated.taxinow.models.DriverLocation;
import com.automated.taxinow.models.History;
import com.automated.taxinow.models.MyThings;
import com.automated.taxinow.models.PTour;
import com.automated.taxinow.models.Reffral;
import com.automated.taxinow.models.Route;
import com.automated.taxinow.models.Step;
import com.automated.taxinow.models.Tour;
import com.automated.taxinow.models.User;
import com.automated.taxinow.models.VehicalType;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;
import com.automated.taxinow.utils.ReadFiles;
import com.google.android.gms.maps.model.LatLng;

/**
 * @author Hardik A Bhalodi
 */
public class ParseContent {
	private Activity activity;
	private PreferenceHelper preferenceHelper;
	private final String KEY_SUCCESS = "success";
	private final String KEY_ERROR = "error";
	private final String NAME = "name";
	private final String AGE = "age";
	private final String TYPE = "type";
	private final String MIN_FARE = "min_fare";
	private final String MAX_SIZE = "max_size";
	private final String NOTES = "notes";
	private final String IMAGE_URL = "image_url";
	private final String THINGS_ID = "thing_id";
	private final String KEY_ERROR_CODE = "error_code";
	private final String KEY_WALKER = "walker";
	private final String BILL = "bill";
	private final String KEY_BILL = "bill";

	private final String IS_WALKER_STARTED = "is_walker_started";
	private final String IS_WALKER_ARRIVED = "is_walker_arrived";
	private final String IS_WALK_STARTED = "is_walk_started";
	private final String IS_WALKER_RATED = "is_walker_rated";
	private final String IS_COMPLETED = "is_completed";
	private final String STATUS = "status";
	private final String CONFIRMED_WALKER = "confirmed_walker";

	private final String TIME = "time";
	private final String BASE_PRICE = "base_price";
	private final String BASE_DISTANCE = "base_distance";
	private final String DISTANCE_COST = "distance_cost";
	private final String DISTANCE = "distance";
	private final String UNIT = "unit";
	private final String TIME_COST = "time_cost";
	private final String TOTAL = "total";
	private final String IS_PAID = "is_paid";
	private final String START_TIME = "start_time";

	public static final String DATE = "date";

	private final String TYPES = "types";

	private final String ID = "id";

	private final String ICON = "icon";
	private final String IS_DEFAULT = "is_default";
	private final String PRICE_PER_UNIT_TIME = "price_per_unit_time";
	private final String PRICE_PER_UNIT_DISTANCE = "price_per_unit_distance";

	private final String STRIPE_TOKEN = "stripe_token";
	private final String LAST_FOUR = "last_four";
	private final String CREATED_AT = "created_at";
	private final String UPDATED_AT = "updated_at";
	private final String OWNER_ID = "owner_id";
	private final String CARD_TYPE = "card_type";

	private final String PAYMENTS = "payments";

	private final String REQUESTS = "requests";
	private final String WALKER = "walker";
	private final String CUSTOMER_ID = "customer_id";

	private final String REFERRAL_CODE = "referral_code";
	private final String TOTAL_REFERRALS = "total_referrals";
	private final String AMOUNT_EARNED = "total_referrals";
	private final String AMOUNT_SPENT = "total_referrals";
	private final String BALANCE_AMOUNT = "balance_amount";
	private final String WALKERS = "walker_list";
	private final String PROMO_CODE = "promo_code";
	private final String PROMO_BONUS = "promo_bonus";
	private final String REFERRAL_BONUS = "referral_bonus";

	public ParseContent(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		preferenceHelper = new PreferenceHelper(activity);
	}

	public Route parseRoute(String response, Route routeBean) {

		try {
			Step stepBean;
			JSONObject jObject = new JSONObject(response);
			JSONArray jArray = jObject.getJSONArray("routes");
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject innerjObject = jArray.getJSONObject(i);
				if (innerjObject != null) {
					JSONArray innerJarry = innerjObject.getJSONArray("legs");
					for (int j = 0; j < innerJarry.length(); j++) {

						JSONObject jObjectLegs = innerJarry.getJSONObject(j);
						routeBean.setDistanceText(jObjectLegs.getJSONObject(
								"distance").getString("text"));
						routeBean.setDistanceValue(jObjectLegs.getJSONObject(
								"distance").getInt("value"));

						routeBean.setDurationText(jObjectLegs.getJSONObject(
								"duration").getString("text"));
						routeBean.setDurationValue(jObjectLegs.getJSONObject(
								"duration").getInt("value"));

						routeBean.setStartAddress(jObjectLegs
								.getString("start_address"));
						routeBean.setEndAddress(jObjectLegs
								.getString("end_address"));

						routeBean.setStartLat(jObjectLegs.getJSONObject(
								"start_location").getDouble("lat"));
						routeBean.setStartLon(jObjectLegs.getJSONObject(
								"start_location").getDouble("lng"));

						routeBean.setEndLat(jObjectLegs.getJSONObject(
								"end_location").getDouble("lat"));
						routeBean.setEndLon(jObjectLegs.getJSONObject(
								"end_location").getDouble("lng"));

						JSONArray jstepArray = jObjectLegs
								.getJSONArray("steps");
						if (jstepArray != null) {
							for (int k = 0; k < jstepArray.length(); k++) {
								stepBean = new Step();
								JSONObject jStepObject = jstepArray
										.getJSONObject(k);
								if (jStepObject != null) {

									stepBean.setHtml_instructions(jStepObject
											.getString("html_instructions"));
									stepBean.setStrPoint(jStepObject
											.getJSONObject("polyline")
											.getString("points"));
									stepBean.setStartLat(jStepObject
											.getJSONObject("start_location")
											.getDouble("lat"));
									stepBean.setStartLon(jStepObject
											.getJSONObject("start_location")
											.getDouble("lng"));
									stepBean.setEndLat(jStepObject
											.getJSONObject("end_location")
											.getDouble("lat"));
									stepBean.setEndLong(jStepObject
											.getJSONObject("end_location")
											.getDouble("lng"));

									stepBean.setListPoints(new PolyLineUtils()
											.decodePoly(stepBean.getStrPoint()));
									routeBean.getListStep().add(stepBean);
								}

							}
						}
					}

				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return routeBean;
	}

	public boolean isSuccessWithStoreId(String response) {
		AppLog.Log(Const.TAG, response);
		if (TextUtils.isEmpty(response))
			return false;
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				preferenceHelper.putUserId(jsonObject
						.getString(Const.Params.ID));
				preferenceHelper.putSessionToken(jsonObject
						.getString(Const.Params.TOKEN));
				preferenceHelper.putEmail(jsonObject
						.optString(Const.Params.EMAIL));
				preferenceHelper.putLoginBy(jsonObject
						.getString(Const.Params.LOGIN_BY));
				preferenceHelper.putReferee(jsonObject
						.getInt(Const.Params.IS_REFEREE));
				if (!preferenceHelper.getLoginBy().equalsIgnoreCase(
						Const.MANUAL)) {
					preferenceHelper.putSocialId(jsonObject
							.getString(Const.Params.SOCIAL_UNIQUE_ID));
				}

				return true;
			} else {
				AndyUtils.showToast(jsonObject.getString(KEY_ERROR), activity);
				// AndyUtils.showErrorToast(jsonObject.getInt(KEY_ERROR_CODE),
				// activity);
				return false;
				// AndyUtils.showToast(jsonObject.getString(KEY_ERROR),
				// activity);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public User parseUserAndStoreToDb(String response) {
		User user = null;
		try {
			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				user = new User();
				DBHelper dbHelper = new DBHelper(activity);
				user.setUserId(jsonObject.getInt(Const.Params.ID));
				user.setEmail(jsonObject.optString(Const.Params.EMAIL));
				user.setFname(jsonObject.getString(Const.Params.FIRSTNAME));
				user.setLname(jsonObject.getString(Const.Params.LAST_NAME));

				user.setAddress(jsonObject.getString(Const.Params.ADDRESS));
				user.setBio(jsonObject.getString(Const.Params.BIO));
				user.setZipcode(jsonObject.getString(Const.Params.ZIPCODE));
				user.setPicture(jsonObject.getString(Const.Params.PICTURE));
				user.setContact(jsonObject.getString(Const.Params.PHONE));
				dbHelper.createUser(user);

			} else {
				// AndyUtils.showToast(jsonObject.getString(KEY_ERROR),
				// activity);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public boolean isSuccess(String response) {
		if (TextUtils.isEmpty(response))
			return false;
		try {
			// AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				return true;
			} else {
				AndyUtils.showToast(jsonObject.getString(KEY_ERROR), activity);
				// AndyUtils.showErrorToast(jsonObject.getInt(KEY_ERROR_CODE),
				// activity);
				return false;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public DriverLocation getDriverLocation(String response) {
		DriverLocation driverLocation = null;
		LatLng latLng = null;
		if (TextUtils.isEmpty(response))
			return null;
		AppLog.Log(Const.TAG, response);
		try {
			JSONObject jsonObject = new JSONObject(response);
			driverLocation = new DriverLocation();
			latLng = new LatLng(jsonObject.getDouble(Const.Params.LATITUDE),
					jsonObject.getDouble(Const.Params.LONGITUDE));
			driverLocation.setLatLng(latLng);
			driverLocation.setDistance(new DecimalFormat("0.00").format(Double
					.parseDouble(jsonObject.getString(DISTANCE))));
			driverLocation.setBearing(jsonObject
					.getDouble(Const.Params.BEARING));
			driverLocation.setUnit(jsonObject.getString(UNIT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driverLocation;
	}

	public int getErrorCode(String response) {
		if (TextUtils.isEmpty(response))
			return 0;
		try {
			AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getInt(KEY_ERROR_CODE);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public String getMessage(String response) {
		if (TextUtils.isEmpty(response))
			return "";
		try {
			AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			return jsonObject.getString(KEY_ERROR);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public int checkRequestStatus(String response) {
		int status = Const.NO_REQUEST;
		try {
			// AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getInt(CONFIRMED_WALKER) == 0
					&& jsonObject.getInt(STATUS) == 0) {
				return Const.IS_REQEUST_CREATED;
			} else if (jsonObject.getInt(CONFIRMED_WALKER) == 0
					&& jsonObject.getInt(STATUS) == 1) {
				return Const.NO_REQUEST;
			} else if (jsonObject.getInt(CONFIRMED_WALKER) != 0
					&& jsonObject.getInt(STATUS) == 1) {

				if (jsonObject.getInt(IS_WALKER_STARTED) == 0) {
					status = Const.IS_WALKER_STARTED;
				} else if (jsonObject.getInt(IS_WALKER_ARRIVED) == 0) {
					status = Const.IS_WALKER_ARRIVED;
				} else if (jsonObject.getInt(IS_WALK_STARTED) == 0) {
					status = Const.IS_WALK_STARTED;
				} else if (jsonObject.getInt(IS_COMPLETED) == 0) {
					status = Const.IS_COMPLETED;
				} else if (jsonObject.getInt(IS_WALKER_RATED) == 0) {
					status = Const.IS_WALKER_RATED;
				}
			}
			preferenceHelper.putPromoCode(jsonObject.optString(PROMO_CODE));
			String time = jsonObject.optString(START_TIME);
			if (!TextUtils.isEmpty(time)) {
				try {
					TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
					Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
							Locale.ENGLISH).parse(time);
					AppLog.Log("TAG", "START DATE---->" + date.toString()
							+ " month:" + date.getMonth());
					preferenceHelper.putRequestTime(date.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (jsonObject.getString("dest_latitude").length() != 0) {
				preferenceHelper.putClientDestination(new LatLng(jsonObject
						.getDouble("dest_latitude"), jsonObject
						.getDouble("dest_longitude")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public Bill parseBllingInfo(String response) {
		Bill bill = null;
		try {

			JSONObject jsonObject = new JSONObject(response)
					.getJSONObject(KEY_BILL);
			bill = new Bill();
			bill.setBasePrice(jsonObject.getString(BASE_PRICE));
			double distance = Double
					.parseDouble(jsonObject.getString(DISTANCE));
			// bill.setDistance(jsonObject.getString(DISTANCE));
			bill.setUnit(jsonObject.getString(UNIT));
			if (bill.getUnit().equalsIgnoreCase("kms")) {
				distance = distance * 0.62137;

			}
			bill.setDistance(new DecimalFormat("0.00").format(distance));
			bill.setDistanceCost(jsonObject.getString(DISTANCE_COST));
			bill.setTime(jsonObject.getString(TIME));
			bill.setTimeCost(jsonObject.getString(TIME_COST));
			bill.setIsPaid(jsonObject.getString(IS_PAID));
			bill.setTotal(jsonObject.getString(TOTAL));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			bill = null;
			e.printStackTrace();
		}
		return bill;
	}

	public Reffral parseReffrelCode(String response) {
		Reffral reffral = null;
		try {

			JSONObject jsonObject = new JSONObject(response);

			reffral = new Reffral();

			reffral.setReferralCode(jsonObject.getString(REFERRAL_CODE));
			reffral.setAmountSpent(jsonObject.getString(AMOUNT_SPENT));
			reffral.setBalanceAmount(jsonObject.getString(BALANCE_AMOUNT));
			reffral.setTotalReferrals(jsonObject.getString(TOTAL_REFERRALS));
			reffral.setAmountEarned(jsonObject.getString(AMOUNT_EARNED));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			reffral = null;
			e.printStackTrace();
		}
		return reffral;
	}

	public Driver getDriverInfo(String response) {
		Driver driver = null;
		try {
			driver = new Driver();
			AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response)
					.getJSONObject(KEY_WALKER);
			driver.setBio(jsonObject.getString(Const.Params.BIO));
			driver.setFirstName(jsonObject.getString(Const.Params.FIRSTNAME));
			driver.setLastName(jsonObject.getString(Const.Params.LAST_NAME));
			driver.setPhone(jsonObject.getString(Const.Params.PHONE));
			driver.setPicture(jsonObject.getString(Const.Params.PICTURE));
			driver.setLatitude(jsonObject.getDouble(Const.Params.LATITUDE));
			driver.setLongitude(jsonObject.getDouble(Const.Params.LONGITUDE));
			driver.setRating(jsonObject.getDouble(Const.Params.RATING));
			driver.setCarModel(jsonObject.getString(Const.Params.TAXI_MODEL));
			driver.setCarNumber(jsonObject.getString(Const.Params.TAXI_NUMBER));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}

	public Driver getDriverDetail(String response) {
		Driver driver = null;
		try {
			driver = new Driver();
			AppLog.Log(Const.TAG, response);
			JSONObject object = new JSONObject(response);
			JSONObject jsonObject = new JSONObject(response)
					.getJSONObject(KEY_WALKER);
			driver.setBio(jsonObject.getString(Const.Params.BIO));
			driver.setFirstName(jsonObject.getString(Const.Params.FIRSTNAME));
			driver.setLastName(jsonObject.getString(Const.Params.LAST_NAME));
			driver.setPhone(jsonObject.getString(Const.Params.PHONE));
			driver.setPicture(jsonObject.getString(Const.Params.PICTURE));
			driver.setLatitude(jsonObject.getDouble(Const.Params.LATITUDE));
			driver.setLongitude(jsonObject.getDouble(Const.Params.LONGITUDE));
			driver.setRating(jsonObject.getDouble(Const.Params.RATING));
			driver.setCarModel(jsonObject.getString(Const.Params.TAXI_MODEL));
			driver.setCarNumber(jsonObject.getString(Const.Params.TAXI_NUMBER));
			JSONObject jsonObjectBill = new JSONObject(response)
					.optJSONObject(BILL);
			if (jsonObjectBill != null) {
				Bill bill = new Bill();
				bill.setUnit(jsonObjectBill.getString(UNIT));
				double distance = Double.parseDouble(jsonObjectBill
						.getString(DISTANCE));
				if (bill.getUnit().equalsIgnoreCase("kms")) {
					distance = distance * 0.62137;

				}
				bill.setDistance(new DecimalFormat("0.00").format(distance));
				bill.setTime(jsonObjectBill.getString(TIME));
				bill.setBasePrice(jsonObjectBill.getString(BASE_PRICE));
				bill.setTimeCost(jsonObjectBill.getString(TIME_COST));
				bill.setDistanceCost(jsonObjectBill.getString(DISTANCE_COST));
				bill.setTotal(jsonObjectBill.getString(TOTAL));
				bill.setIsPaid(jsonObjectBill.getString(IS_PAID));
				bill.setPromoBouns(jsonObjectBill.getString(PROMO_BONUS));
				bill.setReferralBouns(jsonObjectBill.getString(REFERRAL_BONUS));
				driver.setBill(bill);
			}
			driver.setBearing(jsonObject.optDouble(Const.Params.BEARING));
			// driver.getBill().setUnit(object.getString(UNIT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;

	}

	public ArrayList<LatLng> parsePathRequest(String response,
			ArrayList<LatLng> points) {
		// TODO Auto-generated method stub
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject
						.getJSONArray(Const.Params.LOCATION_DATA);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					points.add(new LatLng(Double.parseDouble(json
							.getString(Const.Params.LATITUDE)),
							Double.parseDouble(json
									.getString(Const.Params.LONGITUDE))));

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return points;
	}

	public int getRequestInProgress(String response) {
		if (TextUtils.isEmpty(response))
			return Const.NO_REQUEST;
		try {
			AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				int requestId = jsonObject.getInt(Const.Params.REQUEST_ID);
				new PreferenceHelper(activity).putRequestId(requestId);
				return requestId;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Const.NO_REQUEST;
	}

	public int getRequestId(String response) {
		if (TextUtils.isEmpty(response))
			return Const.NO_REQUEST;
		try {
			AppLog.Log(Const.TAG, response);
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				int requestId = jsonObject.getInt(Const.Params.REQUEST_ID);
				new PreferenceHelper(activity).putRequestId(requestId);
				return requestId;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Const.NO_REQUEST;
	}

	public ArrayList<String> parseCountryCodes() {
		String response = "";
		ArrayList<String> list = new ArrayList<String>();
		try {
			response = ReadFiles.readRawFileAsString(activity,
					R.raw.countrycodes);

			JSONArray array = new JSONArray(response);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				list.add(object.getString("phone-code") + " "
						+ object.getString("name"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public MyThings parseThings(String response) {
		MyThings things = null;
		try {
			JSONObject jObject = new JSONObject(response);
			things = new MyThings();
			things.setAge(jObject.getString(AGE));
			// things.setName(jObject.getString(NAME));
			things.setType(jObject.getString(TYPE));
			things.setNotes(jObject.getString(NOTES));
			things.setImgUrl(jObject.getString(IMAGE_URL));
			things.setThingId(jObject.getString(THINGS_ID));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			things = null;
		}
		return things;
	}

	public ArrayList<VehicalType> parseTypes(String response,
			ArrayList<VehicalType> list) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray(TYPES);
				for (int i = 0; i < jsonArray.length(); i++) {
					VehicalType type = new VehicalType();
					JSONObject typeJson = jsonArray.getJSONObject(i);
					type.setBasePrice(typeJson.getDouble(BASE_PRICE));
					type.setBaseDistance(typeJson.getInt(BASE_DISTANCE));
					type.setUnit(typeJson.getString(UNIT));
					type.setIcon(typeJson.getString(ICON));
					type.setId(typeJson.getInt(ID));
					type.setName(typeJson.getString(NAME));
					type.setPricePerUnitDistance(typeJson
							.getDouble(PRICE_PER_UNIT_DISTANCE));
					type.setPricePerUnitTime(typeJson
							.getDouble(PRICE_PER_UNIT_TIME));
					type.setMinFare(typeJson.optString(MIN_FARE));
					type.setMaxSize(typeJson.optString(MAX_SIZE));
					list.add(type);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	public ArrayList<ApplicationPages> parsePages(
			ArrayList<ApplicationPages> list, String response) {
		list.clear();

		ApplicationPages applicationPages = new ApplicationPages();
		applicationPages.setId(-1);
		applicationPages.setTitle("Profile");
		applicationPages.setData("");
		list.add(applicationPages);

		applicationPages = new ApplicationPages();
		applicationPages.setId(-2);
		applicationPages.setTitle("Payment");
		applicationPages.setData("");
		list.add(applicationPages);

		applicationPages = new ApplicationPages();
		applicationPages.setId(-3);
		applicationPages.setTitle("History");
		applicationPages.setData("");
		list.add(applicationPages);

		applicationPages = new ApplicationPages();
		applicationPages.setId(-4);
		applicationPages.setTitle("Referral");
		applicationPages.setData("");
		list.add(applicationPages);
		if (TextUtils.isEmpty(response)) {
			return list;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject
						.getJSONArray(Const.Params.INFORMATIONS);
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						applicationPages = new ApplicationPages();
						JSONObject object = jsonArray.getJSONObject(i);
						applicationPages.setId(object.getInt(Const.Params.ID));
						applicationPages.setTitle(object
								.getString(Const.Params.TITLE));
						applicationPages.setData(object
								.getString(Const.Params.CONTENT));
						applicationPages.setIcon(object
								.getString(Const.Params.ICON));
						list.add(applicationPages);
					}
				}
			}
			// else {
			// AndyUtils.showToast(jsonObject.getString(KEY_ERROR), activity);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Card> parseCards(String response, ArrayList<Card> listCards) {

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray(PAYMENTS);
				for (int i = 0; i < jsonArray.length(); i++) {
					Card card = new Card();
					JSONObject cardJson = jsonArray.getJSONObject(i);
					// card.setStripeToken(cardJson.getString(STRIPE_TOKEN));
					card.setLastFour(cardJson.getString(LAST_FOUR));
					card.setStripeToken(cardJson.getString(CUSTOMER_ID));
					card.setId(cardJson.getInt(ID));
					// card.setCreatedAt(cardJson.getString(CREATED_AT));
					// card.setUpdatedAt(cardJson.getString(UPDATED_AT));
					card.setOwnerId(cardJson.getString(OWNER_ID));
					card.setCardType(cardJson.getString(CARD_TYPE));
					if (cardJson.getInt(IS_DEFAULT) == 1)
						card.setDefault(true);
					else
						card.setDefault(false);
					listCards.add(card);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listCards;
	}

	public ArrayList<History> parseHistory(String response,
			ArrayList<History> list) {
		list.clear();

		if (TextUtils.isEmpty(response)) {
			return list;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject.getJSONArray(REQUESTS);
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						History history = new History();
						history.setId(object.getInt(ID));
						history.setDate(object.getString(DATE));

						double distance = Double.parseDouble(object
								.getString(DISTANCE));
						// bill.setDistance(jsonObject.getString(DISTANCE));
						history.setUnit(object.getString(UNIT));
						if (history.getUnit().equalsIgnoreCase("kms")) {
							distance = distance * 0.62137;
						}
						history.setDistance(new DecimalFormat("0.00")
								.format(distance));
						history.setUnit(object.getString(UNIT));
						history.setTime(object.getString(TIME));
						history.setDistanceCost(object.getString(DISTANCE_COST));
						history.setTimecost(object.getString(TIME_COST));
						history.setBasePrice(object.getString(BASE_PRICE));
						history.setTotal(new DecimalFormat("0.00")
								.format(Double.parseDouble(object
										.getString(TOTAL))));
						history.setType(object.getString(TYPE));
						history.setPromoBonus(object.getString(PROMO_BONUS));
						history.setReferralBonus(object
								.getString(REFERRAL_BONUS));
						JSONObject userObject = object.getJSONObject(WALKER);
						history.setFirstName(userObject
								.getString(Const.Params.FIRSTNAME));
						history.setLastName(userObject
								.getString(Const.Params.LAST_NAME));
						history.setPhone(userObject
								.getString(Const.Params.PHONE));
						history.setPicture(userObject
								.getString(Const.Params.PICTURE));
						history.setEmail(userObject
								.getString(Const.Params.EMAIL));
						history.setBio(userObject.getString(Const.Params.BIO));

						list.add(history);
					}
				}

			}
			// else {
			// AndyUtils.showToast(jsonObject.getString(KEY_ERROR), activity);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Driver> parseNearestDrivers(String response) {
		ArrayList<Driver> listDriver = new ArrayList<Driver>();
		if (TextUtils.isEmpty(response))
			return listDriver;
		try {

			JSONArray jsonArray = new JSONObject(response)
					.getJSONArray(WALKERS);
			for (int i = 0; i < jsonArray.length(); i++) {
				Driver driver = new Driver();
				driver.setDriverId(jsonArray.getJSONObject(i).getInt(
						Const.Params.ID));
				driver.setLatitude(jsonArray.getJSONObject(i).getDouble(
						Const.Params.LATITUDE));
				driver.setLongitude(jsonArray.getJSONObject(i).getDouble(
						Const.Params.LONGITUDE));
				driver.setBearing(jsonArray.getJSONObject(i).getDouble(
						Const.Params.BEARING));
				driver.setVehicleTypeId(jsonArray.getJSONObject(i).getInt(
						"type"));
				listDriver.add(driver);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listDriver;
	}

	public int parseNearestDriverDuration(String response) {
		if (TextUtils.isEmpty(response))
			return 0;
		try {
			JSONArray jsonArray = new JSONObject(response)
					.getJSONArray("routes");
			JSONArray jArrSub = jsonArray.getJSONObject(0).getJSONArray("legs");
			long totalSeconds = jArrSub.getJSONObject(0)
					.getJSONObject("duration").getLong("value");
			int totalMinutes = Math.round(totalSeconds / 60);
			return totalMinutes;
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public String parseNearestDriverDurationString(String response) {
		if (TextUtils.isEmpty(response))
			return "1 min";
		try {
			JSONArray jsonArray = new JSONObject(response)
					.getJSONArray("routes");
			JSONArray jArrSub = jsonArray.getJSONObject(0).getJSONArray("legs");

			return jArrSub.getJSONObject(0).getJSONObject("duration")
					.getString("text");
		} catch (JSONException e) {
			e.printStackTrace();
			return "1 min";
		}
	}

	public void parseCardAndPriceDetails(String response) {
		try {
			AppLog.Log("ParseContent", "parseCardAndPriceDetails");
			JSONObject job = new JSONObject(response);
			JSONObject cardObject = job.getJSONObject("card_details");
			preferenceHelper.putDefaultCard(cardObject.getInt("card_id"));
			preferenceHelper
					.putDefaultCardNo(cardObject.getString("last_four"));
			preferenceHelper.putDefaultCardType(cardObject
					.getString("card_type"));

			JSONObject chargeObject = job.getJSONObject("charge_details");
			preferenceHelper.putBasePrice(Float.parseFloat(chargeObject
					.getString("base_price")));
			preferenceHelper.putDistancePrice(Float.parseFloat(chargeObject
					.getString("distance_price")));
			preferenceHelper.putTimePrice(Float.parseFloat(chargeObject
					.getString("price_per_unit_time")));
			if (job.has("owner")) {

				JSONObject walkerObject = job.getJSONObject("owner");
				AppLog.Log("ParseContent",
						"Payment type : " + walkerObject.getInt("payment_type"));
				preferenceHelper.putPaymentMode(walkerObject
						.getInt("payment_type"));
			}
		} catch (JSONException e) {
			AppLog.Log("MainDrawerActivity", "" + e);
		}
	}

	public ArrayList<Tour> parseTours(ArrayList<Tour> list, String response) {
		list.clear();
		if (TextUtils.isEmpty(response)) {
			return list;
		}
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getBoolean(KEY_SUCCESS)) {
				JSONArray jsonArray = jsonObject
						.getJSONArray(Const.Params.TOUR);
				if (jsonArray.length() > 0) {
					Tour tour;
					PTour pTour;
					for (int i = 0; i < jsonArray.length(); i++) {
						tour = new Tour();
						JSONObject object = jsonArray.getJSONObject(i);
						tour.setTourId(object.getInt(Const.Params.ID));
						tour.setTourName(object.getString("tour_name"));
						tour.setTourImage(object.getString("tour_image"));
						tour.setTourDesc(object.getString("description"));
						tour.setFullDayTour(object.getString("full_day_tour"));
						tour.setHalfDayTour(object.getString("half_day_tour"));
						tour.setFullDayPrice(object
								.getDouble("f_day_sheduled_price"));
						tour.setHalfDayPrice(object
								.getDouble("h_day_sheduled_price"));
						tour.setMorningDTime(object.getString("m_depart_time"));
						tour.setMorningRTime(object.getString("m_return_time"));
						tour.setAfterDTime(object.getString("a_depart_time"));
						tour.setAfterRTime(object.getString("a_return_time"));
						tour.setTourNote(object.getString("note"));
						tour.setfDayDTime(object
								.getString("full_day_depart_time"));
						tour.setfDayRTime(object
								.getString("full_day_return_time"));

						// Parse gallery images
						ArrayList<String> imageList = new ArrayList<String>();
						String[] imgArr = object.getString("image_galllery")
								.split(",");
						for (String img : imgArr) {
							imageList.add(img);
						}
						tour.setImgLst(imageList);

						// Private tours parse
						ArrayList<PTour> pTourList = new ArrayList<PTour>();
						JSONArray pTourArr = object
								.getJSONArray("private_tour");
						for (int j = 0; j < pTourArr.length(); j++) {
							JSONObject pTourJObject = pTourArr.getJSONObject(j);

							pTour = new PTour();
							pTour.setpTourPerson(pTourJObject.getInt("persons"));
							pTour.setpTourPrice(pTourJObject
									.getDouble("tour_price"));
							pTour.setpTourType(pTourJObject.getInt("tour_type"));
							pTourList.add(pTour);
						}
						tour.setpTour(pTourList);
						list.add(tour);
					}
				}
			}
			// else {
			// AndyUtils.showToast(jsonObject.getString(KEY_ERROR), activity);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<String> parseNearByPlaces(String response,
			ArrayList<String> resultList) {
		try {
			JSONObject resultObject;
			JSONObject job = new JSONObject(response);
			JSONArray resultArr = job.getJSONArray("results");
			for (int i = 0; i < resultArr.length(); i++) {
				resultObject = resultArr.getJSONObject(i);
				// String fullVicinity = resultObject.getString("vicinity");
				// String[] vicinityArr = resultObject.getString("vicinity")
				// .split(", ");
				// String vicinity = null;
				// if (vicinityArr.length > 2) {
				// vicinity = vicinityArr[vicinityArr.length - 2];
				// vicinity += ", " + vicinityArr[vicinityArr.length - 1];
				// } else {
				// vicinity = fullVicinity;
				// }
				resultList.add(resultObject.getString("name"));
			}
		} catch (JSONException e) {
			AppLog.Log("MainDrawerActivity", "" + e);
		}
		return resultList;
	}
}
