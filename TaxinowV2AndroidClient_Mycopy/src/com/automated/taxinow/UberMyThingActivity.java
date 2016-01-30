package com.automated.taxinow;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidquery.AQuery;
import com.automated.taxinow.models.MyThings;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.MultiPartRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.ImageHelper;
import com.automated.taxinow.utils.PreferenceHelper;

/**
 * @author Hardik A Bhalodi
 */
public class UberMyThingActivity extends ActionBarBaseActivitiy {

	private Button btnNext;
	private EditText etName, etType, etAge, etNotes;
	private ImageView ivProPic;
	private Uri uri = null;
	private ParseContent pContent;
	private String imageFilePath;
	private Bitmap bmp;
	private String imageUrl = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_thing);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setText(getString(R.string.text_update));
		ivProPic = (ImageView) findViewById(R.id.ivChooseMyThing);
		etAge = (EditText) findViewById(R.id.etThingAge);
		etName = (EditText) findViewById(R.id.etThingName);
		etType = (EditText) findViewById(R.id.etThingType);
		etNotes = (EditText) findViewById(R.id.etNotes);
		pContent = new ParseContent(this);
		ivProPic.setOnClickListener(this);
		btnNext.setOnClickListener(this);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		// getMyThings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnNext:
			if (isValidate())
				addMyThings();

			break;
		case R.id.ivChooseMyThing:
			showPictureDialog();
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		String msg = null;
		if (TextUtils.isEmpty(etName.getText().toString())) {
			msg = getString(R.string.text_enter_thingname);
			etName.requestFocus();
		} else if (TextUtils.isEmpty(etAge.getText().toString())) {
			msg = getString(R.string.text_enter_age);
			etAge.requestFocus();
		} else if (TextUtils.isEmpty(etType.getText().toString())) {
			msg = getString(R.string.text_enter_type);
			etType.requestFocus();
		} else if (TextUtils.isEmpty(etNotes.getText().toString())) {
			msg = getString(R.string.text_enter_notes);
			etNotes.requestFocus();
		}
		if (!TextUtils.isEmpty(imageUrl)) {
			imageFilePath = null;
			imageFilePath = new AQuery(this).getCachedFile(imageUrl)
					.getAbsolutePath();
			imageUrl = null;
		}
		if (TextUtils.isEmpty(imageFilePath)) {

			msg = getString(R.string.text_pro_pic);

		}

		if (msg == null) {
			return true;
		}
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		return false;

	}

	private void choosePhotoFromGallary() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, Const.CHOOSE_PHOTO);

	}

	private void takePhotoFromCamera() {
		Calendar cal = Calendar.getInstance();
		File file = new File(Environment.getExternalStorageDirectory(),
				(cal.getTimeInMillis() + ".jpg"));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		uri = Uri.fromFile(file);
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(i, Const.TAKE_PHOTO);
	}

	public String getRealPathFromURI(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case Const.CHOOSE_PHOTO:
			if (data != null) {

				uri = data.getData();

				if (uri != null) {
					imageFilePath = getRealPathFromURI(uri);
					AQuery aQuery = new AQuery(this);
					aQuery.id(ivProPic).image(imageFilePath);
				} else {
					Toast.makeText(this, "Unable to select image",
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case Const.TAKE_PHOTO:
			if (uri != null) {
				imageFilePath = uri.getPath();
				if (imageFilePath != null && imageFilePath.length() > 0) {
					File myFile = new File(imageFilePath);

					try {
						if (bmp != null)
							bmp.recycle();
						bmp = new ImageHelper().decodeFile(myFile);
					} catch (OutOfMemoryError e) {
//						System.out.println("out of bound");
					}
					ivProPic.setImageBitmap(bmp);
					// try {

					break;
				} else {
					Toast.makeText(this, "Unable to select image",
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(this, "Unable to select image",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

	private void showPictureDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.text_choosepicture));
		String[] items = { getString(R.string.text_gallary),
				getString(R.string.text_camera) };

		dialog.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
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
		dialog.show();
	}

	private void addMyThings() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.REGISTER_MYTHING);
		map.put(Const.Params.AGE, etAge.getText().toString());
		map.put(Const.Params.NAME, etName.getText().toString());
		map.put(Const.Params.NOTES, etNotes.getText().toString());
		map.put(Const.Params.TYPE, etType.getText().toString());
		map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(this).getSessionToken());
		map.put(Const.Params.PICTURE, imageFilePath);
		AndyUtils
				.showCustomProgressDialog(this, "ADDING THINGS...", true, null);
		new MultiPartRequester(this, map, Const.ServiceCode.REGISTER_MYTHING,
				this);

	}

	private void getMyThings() {
		AndyUtils.showCustomProgressDialog(this, "Getting Things...", true,
				null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				Const.ServiceType.REGISTER_MYTHING + Const.Params.ID + "="
						+ new PreferenceHelper(this).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(this).getSessionToken());
		new HttpRequester(this, map, Const.ServiceCode.GET_MYTHING, true, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.uberorg.fragments.BaseFragmentRegister#onTaskCompleted(java.lang.
	 * String, int)
	 */
	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		// TODO Auto-generated method stub

		super.onTaskCompleted(response, serviceCode);
		AndyUtils.removeCustomProgressDialog();
//		System.out.println(response + "<-------");
		switch (serviceCode) {
		case Const.ServiceCode.REGISTER_MYTHING:

			if (pContent.isSuccess(response)) {
				Toast.makeText(this, "Things addedd succssfully",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case Const.ServiceCode.GET_MYTHING:
			if (pContent.isSuccess(response)) {
				MyThings myThings = pContent.parseThings(response);
				etAge.setText(myThings.getAge());
				// etName.setText(myThings.getName());
				etNotes.setText(myThings.getNotes());
				etType.setText(myThings.getType());
				imageUrl = myThings.getImgUrl();
				new AQuery(this).id(ivProPic).image(myThings.getImgUrl(),
						getAqueryOption());

			}
			break;
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

}
