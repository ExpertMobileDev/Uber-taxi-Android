package com.automated.taxinow;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.automated.taxinow.component.MyFontButton;
import com.automated.taxinow.component.MyFontEdittextView;
import com.automated.taxinow.component.MyFontTextView;
import com.automated.taxinow.db.DBHelper;
import com.automated.taxinow.models.User;
import com.automated.taxinow.parse.AsyncTaskCompleteListener;
import com.automated.taxinow.parse.MultiPartRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;
import com.soundcloud.android.crop.Crop;

/**
 * @author Kishan H Dhamat
 * 
 */
public class ProfileActivity extends ActionBarBaseActivitiy implements
		OnClickListener, AsyncTaskCompleteListener {
	private MyFontEdittextView etProfileFname, etProfileLName, etProfileEmail,
			etProfileNumber, etProfileAddress, etProfileBio, etProfileZipcode,
			etCurrentPassword, etNewPassword, etRetypePassword;
	private ImageView ivProfile, btnProfileEmailInfo;
	private MyFontButton tvProfileSubmit;
	private DBHelper dbHelper;
	private Uri uri = null;
	private AQuery aQuery;
	private String profileImageData, profileImageFilePath, loginType;
	private Bitmap profilePicBitmap;
	private PreferenceHelper preferenceHelper;
	private ImageOptions imageOptions;
	private final String TAG = "profileActivity";
	private PopupWindow registerInfoPopup;
	private MyFontTextView tvPopupMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setTitle(getString(R.string.text_profile));
		setIconMenu(R.drawable.nav_profile);
		setIcon(R.drawable.back);
		// actionBar.setTitle(getString(R.string.text_profile));
		// findViewById(R.id.llProfileSocial).setVisibility(View.GONE);
		// findViewById(R.id.etProfilePassword).setVisibility(View.GONE);
		// findViewById(R.id.tvProfileSubmit).setVisibility(View.GONE);
		findViewById(R.id.tvProfileCountryCode).setVisibility(View.GONE);
		// findViewById(R.id.llseprateView).setVisibility(View.GONE);
		etProfileFname = (MyFontEdittextView) findViewById(R.id.etProfileFName);
		etProfileLName = (MyFontEdittextView) findViewById(R.id.etProfileLName);
		etProfileEmail = (MyFontEdittextView) findViewById(R.id.etProfileEmail);
		etCurrentPassword = (MyFontEdittextView) findViewById(R.id.etCurrentPassword);
		etNewPassword = (MyFontEdittextView) findViewById(R.id.etNewPassword);
		etRetypePassword = (MyFontEdittextView) findViewById(R.id.etRetypePassword);
		etProfileNumber = (MyFontEdittextView) findViewById(R.id.etProfileNumber);
		etProfileBio = (MyFontEdittextView) findViewById(R.id.etProfileBio);
		etProfileAddress = (MyFontEdittextView) findViewById(R.id.etProfileAddress);
		etProfileZipcode = (MyFontEdittextView) findViewById(R.id.etProfileZipCode);
		tvProfileSubmit = (MyFontButton) findViewById(R.id.tvProfileSubmit);
		ivProfile = (ImageView) findViewById(R.id.ivProfileProfile);
		btnProfileEmailInfo = (ImageView) findViewById(R.id.btnProfileEmailInfo);
		btnProfileEmailInfo.setOnClickListener(this);
		ivProfile.setOnClickListener(this);
		tvProfileSubmit.setOnClickListener(this);
		tvProfileSubmit.setText(getResources().getString(
				R.string.text_edit_profile));

		preferenceHelper = new PreferenceHelper(this);
		// socialId = preferenceHelper.getSocialId();
		loginType = preferenceHelper.getLoginBy();

		AppLog.Log(Const.TAG, "Login type==+> " + loginType);
		if (loginType.equals(Const.MANUAL)) {
			etCurrentPassword.setVisibility(View.VISIBLE);
			etNewPassword.setVisibility(View.VISIBLE);
			etRetypePassword.setVisibility(View.VISIBLE);
			// etCurrentPassword.setText(preferenceHelper.getPassword());
		}

		aQuery = new AQuery(this);
		disableViews();
		imageOptions = new ImageOptions();
		imageOptions.memCache = true;
		imageOptions.fileCache = true;
		imageOptions.targetWidth = 200;
		imageOptions.fallback = R.drawable.default_user;
		setData();

		// popup
		LayoutInflater inflate = LayoutInflater.from(this);
		RelativeLayout layout = (RelativeLayout) inflate.inflate(
				R.layout.popup_info_window, null);
		tvPopupMsg = (MyFontTextView) layout.findViewById(R.id.tvPopupMsg);
		registerInfoPopup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		layout.setOnClickListener(this);
		registerInfoPopup.setBackgroundDrawable(new BitmapDrawable());
		registerInfoPopup.setOutsideTouchable(true);

	}

	private void disableViews() {
		etProfileFname.setEnabled(false);
		etProfileLName.setEnabled(false);
		etProfileEmail.setEnabled(false);
		etProfileNumber.setEnabled(false);
		// etProfileBio.setEnabled(false);
		// etProfileAddress.setEnabled(false);
		// etProfileZipcode.setEnabled(false);
		etCurrentPassword.setEnabled(false);
		etNewPassword.setEnabled(false);
		etRetypePassword.setEnabled(false);
		ivProfile.setEnabled(false);
	}

	private void enableViews() {
		etProfileFname.setEnabled(true);
		etProfileLName.setEnabled(true);
		// etProfileEmail.setEnabled(true);
		etProfileNumber.setEnabled(true);
		// etProfileBio.setEnabled(true);
		// etProfileAddress.setEnabled(true);
		// etProfileZipcode.setEnabled(true);
		etCurrentPassword.setEnabled(true);
		etNewPassword.setEnabled(true);
		etRetypePassword.setEnabled(true);
		ivProfile.setEnabled(true);
	}

	private void setData() {
		// TODO Auto-generated method stub
		dbHelper = new DBHelper(getApplicationContext());
		User user = dbHelper.getUser();
		if (user != null) {
			aQuery.id(ivProfile).progress(R.id.pBar)
					.image(user.getPicture(), imageOptions);
			etProfileFname.setText(user.getFname());
			etProfileLName.setText(user.getLname());
			etProfileEmail.setText(user.getEmail());
			etProfileNumber.setText(user.getContact());
		}
		// etProfileBio.setText(user.getBio());
		// etProfileAddress.setText(user.getAddress());
		// etProfileZipcode.setText(user.getZipcode());
		aQuery.id(R.id.ivProfileProfile).image(user.getPicture(), true, true,
				200, 0, new BitmapAjaxCallback() {

					@Override
					public void callback(String url, ImageView iv, Bitmap bm,
							AjaxStatus status) {
						if (!TextUtils.isEmpty(url)) {
							AppLog.Log(TAG, "URL FROM AQUERY::" + url);
							profileImageData = aQuery.getCachedFile(url)
									.getPath();
							AppLog.Log(TAG, "URL path FROM AQUERY::" + url);
						}
						iv.setImageBitmap(bm);

					}

				});

	}

	private void onUpdateButtonClick() {
		if (etProfileFname.getText().length() == 0) {
			AndyUtils.showToast(
					getResources().getString(R.string.text_enter_name), this);
			return;
		} else if (etProfileLName.getText().length() == 0) {
			AndyUtils.showToast(
					getResources().getString(R.string.text_enter_lname), this);
			return;
		} else if (etProfileEmail.getText().length() == 0) {
			AndyUtils.showToast(
					getResources().getString(R.string.text_enter_email), this);
			return;
		} else if (!AndyUtils.eMailValidation(etProfileEmail.getText()
				.toString())) {
			AndyUtils.showToast(
					getResources().getString(R.string.text_enter_valid_email),
					this);
			return;
		} else if (etCurrentPassword.getVisibility() == View.VISIBLE) {
			if (!TextUtils.isEmpty(etNewPassword.getText())) {
				if (etNewPassword.getText().length() < 6) {
					AndyUtils.showToast(
							getResources().getString(
									R.string.error_valid_password), this);
					return;
				} else if (TextUtils.isEmpty(etCurrentPassword.getText())) {
					AndyUtils.showToast(
							getResources().getString(
									R.string.error_empty_password), this);
					return;
				} else if (etCurrentPassword.getText().length() < 6) {
					AndyUtils.showToast(
							getResources().getString(
									R.string.error_valid_password), this);
					return;
				} else if (TextUtils.isEmpty(etRetypePassword.getText())) {
					AndyUtils.showToast(
							getResources().getString(
									R.string.error_empty_retypepassword), this);
					return;
				} else if (!etRetypePassword.getText().toString()
						.equals(etNewPassword.getText().toString())) {
					AndyUtils.showToast(
							getResources().getString(
									R.string.error_mismatch_password), this);
					return;
				}
			} else if (etCurrentPassword.getVisibility() == View.INVISIBLE) {
				etNewPassword.setVisibility(View.INVISIBLE);
				etRetypePassword.setVisibility(View.INVISIBLE);
			}

		}

		// if (etProfilePassword.getVisibility() == View.VISIBLE) {
		// if (!TextUtils.isEmpty(profileImageData)) {
		// profileImageData = null;
		// profileImageData = aQuery.getCachedFile(profileImageData)
		// .getPath();
		// }
		// }

		if (etProfileNumber.getText().length() == 0) {
			AndyUtils.showToast(
					getResources().getString(R.string.text_enter_number), this);
			return;
		}
		// else if (profileImageData == null || profileImageData.equals("")) {
		// AndyUtils.showToast(
		// getResources().getString(R.string.text_pro_pic), this);
		// return;
		// }
		else {
			updateSimpleProfile(loginType);
		}
	}

	private void updateSimpleProfile(String type) {

		if (!AndyUtils.isNetworkAvailable(this)) {
			AndyUtils.showToast(
					getResources().getString(R.string.dialog_no_inter_message),
					this);
			return;
		}

		AndyUtils.showCustomProgressDialog(this,
				getResources().getString(R.string.progress_update_profile),
				false, null);

		if (type.equals(Const.MANUAL)) {
			AppLog.Log(TAG, "Simple Profile update method");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.UPDATE_PROFILE);
			map.put(Const.Params.ID, preferenceHelper.getUserId());
			map.put(Const.Params.TOKEN, preferenceHelper.getSessionToken());
			map.put(Const.Params.FIRSTNAME, etProfileFname.getText().toString());
			map.put(Const.Params.LAST_NAME, etProfileLName.getText().toString());
			map.put(Const.Params.EMAIL, etProfileEmail.getText().toString());
			map.put(Const.Params.OLD_PASSWORD, etCurrentPassword.getText()
					.toString());
			map.put(Const.Params.NEW_PASSWORD, etCurrentPassword.getText()
					.toString());
			map.put(Const.Params.PICTURE, profileImageData);
			map.put(Const.Params.PHONE, etProfileNumber.getText().toString());
			// map.put(Const.Params.BIO, etProfileBio.getText().toString());
			// map.put(Const.Params.ADDRESS,
			// etProfileAddress.getText().toString());
			// map.put(Const.Params.STATE, "");
			// map.put(Const.Params.COUNTRY, "");
			// map.put(Const.Params.ZIPCODE,
			// etProfileZipcode.getText().toString()
			// .trim());
			new MultiPartRequester(this, map, Const.ServiceCode.UPDATE_PROFILE,
					this);
		} else {
			updateSocialProfile(type);
		}
	}

	private void updateSocialProfile(String loginType) {
		AppLog.Log(TAG, "profile social update  method");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.UPDATE_PROFILE);
		map.put(Const.Params.ID, preferenceHelper.getUserId());
		map.put(Const.Params.TOKEN, preferenceHelper.getSessionToken());
		map.put(Const.Params.FIRSTNAME, etProfileFname.getText().toString());
		map.put(Const.Params.LAST_NAME, etProfileLName.getText().toString());
		map.put(Const.Params.ADDRESS, etProfileAddress.getText().toString());
		map.put(Const.Params.EMAIL, etProfileEmail.getText().toString());
		map.put(Const.Params.PHONE, etProfileNumber.getText().toString());
		map.put(Const.Params.PICTURE, profileImageData);
		map.put(Const.Params.STATE, "");
		map.put(Const.Params.COUNTRY, "");
		map.put(Const.Params.BIO, etProfileBio.getText().toString());
		map.put(Const.Params.ZIPCODE, etProfileZipcode.getText().toString()
				.trim());
		new MultiPartRequester(this, map, Const.ServiceCode.UPDATE_PROFILE,
				this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvProfileSubmit:
			if (tvProfileSubmit
					.getText()
					.toString()
					.equals(getResources()
							.getString(R.string.text_edit_profile))) {
				enableViews();
				etProfileFname.requestFocus();
				tvProfileSubmit.setText(getResources().getString(
						R.string.text_update_profile));
			} else {
				onUpdateButtonClick();
			}
			break;
		case R.id.ivProfileProfile:
			showPictureDialog();
			break;
		case R.id.btnActionNotification:
			onBackPressed();
			break;
		case R.id.btnProfileEmailInfo:
			if (registerInfoPopup.isShowing())
				registerInfoPopup.dismiss();
			else {
				registerInfoPopup.showAsDropDown(btnProfileEmailInfo);
				tvPopupMsg.setText(getString(R.string.text_profile_popup));
			}
			break;
		default:
			break;
		}
	}

	private void showPictureDialog() {
		AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
		pictureDialog.setTitle(getResources().getString(
				R.string.text_choosepicture));
		String[] pictureDialogItems = {
				getResources().getString(R.string.text_gallary),
				getResources().getString(R.string.text_camera) };

		pictureDialog.setItems(pictureDialogItems,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {

						case 0:
							choosePhotoFromGallary();
							break;

						case 1:
							takePhotoFromCamera();
							break;

						}
					}
				});
		pictureDialog.show();
	}

	private void choosePhotoFromGallary() {

		// Intent intent = new Intent();
		// intent.setType("image/*");
		// intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(galleryIntent, Const.CHOOSE_PHOTO);

	}

	private void takePhotoFromCamera() {
		Calendar cal = Calendar.getInstance();
		File file = new File(Environment.getExternalStorageDirectory(),
				(cal.getTimeInMillis() + ".jpg"));

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		uri = Uri.fromFile(file);
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(cameraIntent, Const.TAKE_PHOTO);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Const.CHOOSE_PHOTO) {
			if (data != null) {
				Uri contentURI = data.getData();

				AppLog.Log(TAG, "Choose photo on activity result");
				beginCrop(contentURI);

			}

		} else if (requestCode == Const.TAKE_PHOTO) {

			if (uri != null) {
				profileImageFilePath = uri.getPath();
				AppLog.Log(TAG, "Take photo on activity result");
				beginCrop(uri);

			} else {
				Toast.makeText(
						this,
						getResources().getString(
								R.string.toast_unable_to_selct_image),
						Toast.LENGTH_LONG).show();
			}
		} else if (requestCode == Crop.REQUEST_CROP) {
			AppLog.Log(TAG, "Crop photo on activity result");
			handleCrop(resultCode, data);
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);

		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.uberdriverforx.parse.AsyncTaskCompleteListener#onTaskCompleted(java
	 * .lang.String, int)
	 */
	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		// TODO Auto-generated method stub
		AndyUtils.removeCustomProgressDialog();
		AppLog.Log(TAG, response);
		switch (serviceCode) {
		case Const.ServiceCode.UPDATE_PROFILE:
			AndyUtils.removeCustomProgressDialog();
			if (new ParseContent(this).isSuccessWithStoreId(response)) {
				Toast.makeText(this, getString(R.string.toast_update_success),
						Toast.LENGTH_SHORT).show();
				new DBHelper(this).deleteUser();
				new ParseContent(this).parseUserAndStoreToDb(response);
				new PreferenceHelper(this).putPassword(etCurrentPassword
						.getText().toString());
				onBackPressed();
			} else {
				Toast.makeText(this, getString(R.string.toast_update_failed),
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.ActionBarBaseActivitiy#isValidate()
	 */
	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	private void beginCrop(Uri source) {
		// Uri outputUri = Uri.fromFile(new File(registerActivity.getCacheDir(),
		// "cropped"));
		Uri outputUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), (Calendar.getInstance()
				.getTimeInMillis() + ".jpg")));
		new Crop(source).output(outputUri).asSquare().start(this);
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			AppLog.Log(Const.TAG, "Handle crop");
			profileImageData = getRealPathFromURI(Crop.getOutput(result));
			ivProfile.setImageURI(Crop.getOutput(result));
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(this, Crop.getError(result).getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		AppLog.Log(Const.TAG, error.getMessage());
	}

}
