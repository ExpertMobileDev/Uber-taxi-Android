package com.automated.taxinow.utils;

/**
 * @author Hardik A Bhalodi
 */
public class Const {
	// map
	public static final String TAG = "AUTOMATED TAXI";
	// public static final String PLACES_AUTOCOMPLETE_API_KEY =
	// "AIzaSyAKe3XmUV93WvHJvII4Qzpf0R052mxb0KI";

	// temp key
	public static final String PLACES_AUTOCOMPLETE_API_KEY = "AIzaSyCqcD3tNdTc8uUpa8j1D4_BXUi1Vtr5QF0";

	// Our key
	// public static final String PLACES_AUTOCOMPLETE_API_KEY =
	// "AIzaSyAto7SNCNVJ4gFCciytelh9vTv_w5qrhAQ";
	public static final int MAP_ZOOM = 20;

	// card io
	public static final String PUBLISHABLE_KEY = "pk_test_C0xTsdez4BI0rXKZp6ObLitq";
	public static final String MY_CARDIO_APP_TOKEN = "c15fa417f757415c9d750d1ef5ee5fd8";

	// web services
	public class ServiceType {
		private static final String HOST_URL = "http://taxinew.taxinow.xyz/";

		// private static final String HOST_URL =
		// "http://192.168.0.26/uberforx_new/api/public/";

		private static final String BASE_URL = HOST_URL + "user/";
		public static final String LOGIN = BASE_URL + "login";
		public static final String REGISTER = BASE_URL + "register";
		public static final String ADD_CARD = BASE_URL + "addcardtoken";

		public static final String CREATE_REQUEST = BASE_URL + "createrequest";
		public static final String GET_REQUEST_LOCATION = BASE_URL
				+ "getrequestlocation?";
		public static final String GET_REQUEST_STATUS = BASE_URL
				+ "getrequest?";

		public static final String REGISTER_MYTHING = BASE_URL + "thing?";
		public static final String REQUEST_IN_PROGRESS = BASE_URL
				+ "requestinprogress?";
		public static final String RATING = BASE_URL + "rating";
		public static final String CANCEL_REQUEST = BASE_URL + "cancelrequest";
		public static final String SET_DESTINATION = BASE_URL
				+ "setdestination";
		public static final String GET_PAGES = HOST_URL + "application/pages";
		public static final String GET_PAGES_DETAIL = HOST_URL
				+ "application/page/";
		public static final String GET_VEHICAL_TYPES = HOST_URL
				+ "application/types";
		public static final String FORGET_PASSWORD = HOST_URL
				+ "application/forgot-password";
		public static final String UPDATE_PROFILE = BASE_URL + "update";
		public static final String GET_CARDS = BASE_URL + "cards?";
		public static final String HISTORY = BASE_URL + "history?";
		public static final String GET_PATH = BASE_URL + "requestpath?";
		public static final String GET_REFERRAL = BASE_URL + "referral?";
		public static final String APPLY_REFFRAL_CODE = BASE_URL
				+ "apply-referral";

		public static final String BRAIN_TREE_URL = BASE_URL + "braintreekey?";
		public static final String GET_PROVIDERS = BASE_URL + "provider_list";
		public static final String PAYMENT_TYPE = BASE_URL + "payment_type";
		public static final String DEFAULT_CARD = BASE_URL + "card_selection";
		public static final String GET_TOUR = BASE_URL + "get_tour?";
		public static final String BOOK_TOUR = BASE_URL + "tour_booking";
		public static final String APPLY_PROMO = BASE_URL + "apply-promo";
		public static final String LOGOUT = BASE_URL + "logout";
		// http://uberforxapi.provenlogic.com/provider/rating";

	}

	// prefname
	public static String PREF_NAME = "taxinowclient";

	// fragments tag
	public static String FRAGMENT_REGISTER = "FRAGMENT_REGISTER";
	public static String FRAGMENT_MAIN = "FRAGMENT_MAIN";
	public static String FRAGMENT_MYTHING_REGISTER = "FRAGMENT_MYTHING_REGISTER";
	public static String FRAGMENT_SIGNIN = "FRAGMENT_SIGNIN";
	public static String FRAGMENT_PAYMENT_REGISTER = "ADD_FRAGMENT_PAYMENT_REGISTER";
	public static String FRAGMENT_PAYMENT_ADD = "FRAGMENT_PAYMENT_ADD";
	public static String FRAGMENT_REFFREAL = "FRAGMENT_REFFREAL";
	public static String FRAGMENT_MYTHING_ADD = "FRAGMENT_MYTHING_ADD";
	public static String FRAGMENT_MAP = "FRAGMENT_MAP";
	public static String FRAGMENT_TRIP = "FRAGMENT_TRIP";
	public static final String FOREGETPASS_FRAGMENT_TAG = "FOEGETPASSFRAGMENT";
	public static String FRAGMENT_FEEDBACK = "FRAGMENT_FEEDBACK";
	public static String FRAGMENT_FULLDAY = "FRAGMENT_FULLDAY";
	public static String FRAGMENT_HALFDAY = "FRAGMENT_HALFDAY";
	public static String FRAGMENT_TOUR_DETAILS = "FRAGMENT_TOUR_DETAILS";

	// service codes
	public class ServiceCode {
		public static final int REGISTER = 1;
		public static final int LOGIN = 2;
		public static final int GET_ROUTE = 3;
		public static final int REGISTER_MYTHING = 4;
		public static final int GET_MYTHING = 5;
		public static final int ADD_CARD = 6;
		public static final int PICK_ME_UP = 7;
		public static final int CREATE_REQUEST = 8;
		public static final int GET_REQUEST_STATUS = 9;
		public static final int GET_REQUEST_LOCATION = 10;
		public static final int GET_REQUEST_IN_PROGRESS = 11;
		public static final int RATING = 12;
		public static final int CANCEL_REQUEST = 13;
		public static final int GET_PAGES = 14;
		public static final int GET_PAGES_DETAILS = 15;
		public static final int GET_VEHICAL_TYPES = 16;
		public static final int FORGET_PASSWORD = 18;
		public static final int UPDATE_PROFILE = 19;
		public static final int GET_CARDS = 20;
		public static final int HISTORY = 21;
		public static final int GET_PATH = 22;
		public static final int GET_REFERREL = 23;
		public static final int APPLY_REFFRAL_CODE = 24;
		public static final int BRAIN_TREE_CODE = 25;
		public static final int GET_PROVIDERS = 26;
		public static final int GET_DURATION = 27;
		public static final int DRAW_PATH_ROAD = 28;
		public static final int DRAW_PATH = 29;
		public static final int PAYMENT_TYPE = 30;
		public static final int DEFAULT_CARD = 31;
		public static final int GET_QUOTE = 32;
		public static final int GET_TOUR = 33;
		public static final int GET_FARE_QUOTE = 34;
		public static final int GET_NEAR_BY = 35;
		public static final int BOOK_TOUR = 36;
		public static final int SET_DESTINATION = 37;
		public static final int UPDATE_PROVIDERS = 38;
		public static final int APPLY_PROMO = 39;
		public static final int LOGOUT = 40;
	}

	// service parameters
	public class Params {
		public static final String TITLE = "title";
		public static final String CONTENT = "content";
		public static final String INFORMATIONS = "informations";
		public static final String EMAIL = "email";
		public static final String CODE = "code";
		public static final String REFERRAL_CODE = "referral_code";
		public static final String PASSWORD = "password";
		public static final String OLD_PASSWORD = "old_password";
		public static final String NEW_PASSWORD = "new_password";
		public static final String FIRSTNAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String PHONE = "phone";
		public static final String DEVICE_TOKEN = "device_token";
		public static final String ICON = "icon";
		public static final String DEVICE_TYPE = "device_type";
		public static final String LOCATION_DATA = "locationdata";
		public static final String BIO = "bio";
		public static final String ADDRESS = "address";
		public static final String STATE = "state";
		public static final String COUNTRY = "country";
		public static final String ZIPCODE = "zipcode";
		public static final String LOGIN_BY = "login_by";
		public static final String ID = "id";
		public static final String TOKEN = "token";
		public static final String COMMENT = "comment";
		public static final String RATING = "rating";
		public static final String TAXI_MODEL = "car_model";
		public static final String TAXI_NUMBER = "car_number";
		public static final String NUM_RATING = "num_rating";
		public static final String SOCIAL_UNIQUE_ID = "social_unique_id";
		public static final String PICTURE = "picture";
		public static final String NAME = "name";
		public static final String AGE = "age";
		public static final String TYPE = "type";
		public static final String OWNER = "2";
		public static final String NOTES = "notes";
		public static final String STRIPE_TOKEN = "payment_token";
		// public static final String PEACH_TOKEN = "payment_token";
		public static final String CARD_TYPE = "card_type";
		public static final String LAST_FOUR = "last_four";
		public static final String LONGITUDE = "longitude";
		public static final String BEARING = "bearing";
		public static final String LATITUDE = "latitude";
		public static final String DISTANCE = "distance";
		public static final String SCHEDULE_ID = "schedule_id";
		public static final String REQUEST_ID = "request_id";
		public static final String DEST_LAT = "dest_lat";
		public static final String DEST_LNG = "dest_long";
		public static final String PAYMENT_OPT = "payment_opt";
		public static final String CARD_ID = "card_id";
		public static final String TYPE_ARRAY = "type";
		public static final String USER_LATITUDE = "usr_lat";
		public static final String USER_LONGITUDE = "user_long";
		public static final String DESTI_LATITUDE = "dist_latitude";
		public static final String DESTI_LONGITUDE = "dist_logitude";
		public static final String CASH_OR_CARD = "cash_or_card";
		public static final String DEFAULT_CARD_ID = "default_card_id";
		public static final String FROM_DATE = "from_date";
		public static final String TO_DATE = "to_date";
		public static final String TOUR = "tour";
		public static final String TOUR_DESC = "tour_desc";
		public static final String TOUR_IMAGES = "tour_images";
		public static final String FULL_DAY_HIGHLIGHT = "fullday_highlight";
		public static final String FULL_DAY_COMMON_PRICE = "fullday_price";
		public static final String FULL_DAY_PRIVATE_PRICE = "fullday_private_price";
		public static final String HALF_DAY_HIGHLIGHT = "halfday_highlight";
		public static final String HALF_DAY_COMMON_PRICE = "halfday_price";
		public static final String HALF_DAY_PRIVATE_PRICE = "halfday_private_price";
		public static final String HALF_DAY_MORNING_DTIME = "halfday_m_dTime";
		public static final String HALF_DAY_MORNING_RTIME = "halfday_m_rTime";
		public static final String HALF_DAY_AFTER_DTIME = "halfday_a_dTime";
		public static final String HALF_DAY_AFTER_RTIME = "halfday_a_rTime";
		public static final String FULL_DAY_DTIME = "fullday_dTime";
		public static final String FULL_DAY_RTIME = "fullday_rTime";
		public static final String HALF_DAY = "half_day";
		public static final String FULL_DAY = "full_day";
		public static final String TOUR_ID = "tour_id";
		public static final String TOUR_NAME = "tour_name";
		public static final String PACKAGE = "package";
		public static final String SCHEDULE = "schedule";
		public static final String PERSON = "person";
		public static final String PRICE = "price";
		public static final String TOUR_TYPE = "tour_type";
		public static final String BOOKING_DATE = "booking_date";
		public static final String IS_SKIP = "is_skip";
		public static final String IS_REFEREE = "is_referee";
		public static final String PROMO_CODE = "promo_code";
	}

	// general
	public static final int CHOOSE_PHOTO = 112;
	public static final int TAKE_PHOTO = 113;
	public static final String URL = "url";
	public static final String DEVICE_TYPE_ANDROID = "android";
	public static final String SOCIAL_FACEBOOK = "facebook";
	public static final String SOCIAL_GOOGLE = "google";
	public static final String MANUAL = "manual";

	// used for request status
	public static final int IS_WALKER_STARTED = 2;
	public static final int IS_WALKER_ARRIVED = 3;
	public static final int IS_WALK_STARTED = 4;
	public static final int IS_COMPLETED = 5;
	public static final int IS_WALKER_RATED = 6;
	public static final int IS_REQEUST_CREATED = 1;

	// used for sending model in to bundle
	public static final String DRIVER = "driver";
	public static final String USER = "user";
	public static final String THINGS = "things";
	// used for schedule request
	public static final long TIME_SCHEDULE = 20 * 1000;
	public static final long DELAY = 0 * 1000;

	// no request id
	public static final int NO_REQUEST = -1;
	public static final int NO_TIME = -1;

	// error code
	public static final int INVALID_TOKEN = 406;
	public static final int REQUEST_ID_NOT_FOUND = 408;

	// notification
	public static final String INTENT_WALKER_STATUS = "walker_status";
	public static final String EXTRA_WALKER_STATUS = "walker_status_extra";

	// payment mode
	public static final int CASH = 1;
	public static final int CREDIT = 0;

	// Peach Payment
	public static final String APPLICATIONIDENTIFIER = "peach.unicab.mcommerce";
	public static final String PROFILETOKEN = "c60f25c78aa04a4baa80b3d627e1f0a5";

	// Card Type
	public static final String[] PREFIXES_AMERICAN_EXPRESS = { "34", "37" };
	public static final String[] PREFIXES_DISCOVER = { "60", "62", "64", "65" };
	public static final String[] PREFIXES_JCB = { "35" };
	public static final String[] PREFIXES_DINERS_CLUB = { "300", "301", "302",
			"303", "304", "305", "309", "36", "38", "37", "39" };
	public static final String[] PREFIXES_VISA = { "4" };
	public static final String[] PREFIXES_MASTERCARD = { "50", "51", "52",
			"53", "54", "55" };
	public static final String AMERICAN_EXPRESS = "American Express";
	public static final String DISCOVER = "Discover";
	public static final String JCB = "JCB";
	public static final String DINERS_CLUB = "Diners Club";
	public static final String VISA = "Visa";
	public static final String MASTERCARD = "MasterCard";
	public static final String UNKNOWN = "Unknown";

	// Tours type
	public static final int FULL_DAY_TOUR = 1;
	public static final int HALF_DAY_TOUR = 0;

	// Placesurls
	public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	public static final String TYPE_NEAR_BY = "/nearbysearch";
	public static final String OUT_JSON = "/json";
}
