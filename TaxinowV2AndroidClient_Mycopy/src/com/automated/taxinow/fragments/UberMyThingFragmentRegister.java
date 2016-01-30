package com.automated.taxinow.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidquery.AQuery;
import com.automated.taxinow.R;
import com.automated.taxinow.parse.MultiPartRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.ImageHelper;

/**
 * @author Hardik A Bhalodi
 */
public class UberMyThingFragmentRegister extends UberBaseFragmentRegister {

	private Button btnNext;
	private EditText etName, etType, etAge, etNotes;
	private ImageView ivProPic;
	private Uri uri = null;
	private ParseContent pContent;
	private String imageFilePath;
	private Bitmap bmp;
	private String id;
	private String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		token = getArguments().getString(Const.Params.TOKEN);
		id = getArguments().getString(Const.Params.ID);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.my_thing, container, false);
		btnNext = (Button) view.findViewById(R.id.btnNext);
		ivProPic = (ImageView) view.findViewById(R.id.ivChooseMyThing);
		etAge = (EditText) view.findViewById(R.id.etThingAge);
		etName = (EditText) view.findViewById(R.id.etThingName);
		etType = (EditText) view.findViewById(R.id.etThingType);
		etNotes = (EditText) view.findViewById(R.id.etNotes);
		ivProPic.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		activity.currentFragment = Const.FRAGMENT_MYTHING_REGISTER;
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		pContent = new ParseContent(activity);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btnNext:
			if (isValidate())
				registerMyThings();

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
		} else if (TextUtils.isEmpty(imageFilePath)) {
			msg = getString(R.string.text_pro_pic);

		}

		if (msg == null) {
			return true;
		}
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		return false;

	}

	private void gotoPaymentFragment(String id, String token) {
		UberAddPaymentFragmentRegister paymentFragment = new UberAddPaymentFragmentRegister();
		Bundle bundle = new Bundle();
		bundle.putString(Const.Params.TOKEN, token);
		bundle.putString(Const.Params.ID, id);
		paymentFragment.setArguments(bundle);
		activity.addFragment(paymentFragment, false,
				Const.FRAGMENT_PAYMENT_REGISTER);
	}

	private void choosePhotoFromGallary() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		activity.startActivityForResult(i, Const.CHOOSE_PHOTO,
				Const.FRAGMENT_MYTHING_REGISTER);

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
		activity.startActivityForResult(i, Const.TAKE_PHOTO,
				Const.FRAGMENT_MYTHING_REGISTER);
	}

	public String getRealPathFromURI(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
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
					AQuery aQuery = new AQuery(activity);
					aQuery.id(ivProPic).image(imageFilePath);
				} else {
					Toast.makeText(activity, "Unable to select image",
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
					Toast.makeText(activity, "Unable to select image",
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(activity, "Unable to select image",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

	private void showPictureDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
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

	private void registerMyThings() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.REGISTER_MYTHING);
		map.put(Const.Params.AGE, etAge.getText().toString());
		map.put(Const.Params.NAME, etName.getText().toString());
		map.put(Const.Params.NOTES, etNotes.getText().toString());
		map.put(Const.Params.TYPE, etType.getText().toString());
		map.put(Const.Params.ID, id);
		map.put(Const.Params.TOKEN, token);
		map.put(Const.Params.PICTURE, imageFilePath);
		AndyUtils.showCustomProgressDialog(activity, "ADDING THINGS...", true,
				null);
		new MultiPartRequester(activity, map,
				Const.ServiceCode.REGISTER_MYTHING, this);

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
//		System.out.println(response + "<-------");
		switch (serviceCode) {
		case Const.ServiceCode.REGISTER_MYTHING:
			AndyUtils.removeCustomProgressDialog();
			if (pContent.isSuccess(response)) {
				gotoPaymentFragment(id, token);
			}
			break;

		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */

}
