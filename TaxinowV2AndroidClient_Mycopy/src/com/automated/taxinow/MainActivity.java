package com.automated.taxinow;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.PreferenceHelper;

public class MainActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	private ImageButton btnSignIn, btnRegister;
	private PreferenceHelper pHelper;
	private boolean isReceiverRegister;
	private int oldOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!TextUtils.isEmpty(new PreferenceHelper(this).getUserId())) {
			startActivity(new Intent(this, MainDrawerActivity.class));
			this.finish();
			return;
		}
		isReceiverRegister = false;
		pHelper = new PreferenceHelper(this);
		setContentView(R.layout.activity_main);

		btnSignIn = (ImageButton) findViewById(R.id.btnSignIn);
		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		// rlLoginRegisterLayout = (RelativeLayout) view
		// .findViewById(R.id.rlLoginRegisterLayout);
		// tvMainBottomView = (MyFontTextView) view
		// .findViewById(R.id.tvMainBottomView);
		btnSignIn.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		if (TextUtils.isEmpty(pHelper.getDeviceToken())) {
			isReceiverRegister = true;
			registerGcmReceiver(mHandleMessageReceiver);
		}
	}

	public void registerGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {
			AndyUtils.showCustomProgressDialog(this,
					getString(R.string.progress_loading), false, null);
			new GCMRegisterHendler(this, mHandleMessageReceiver);

		}
	}

	public void unregisterGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {
			if (mHandleMessageReceiver != null) {
				unregisterReceiver(mHandleMessageReceiver);
			}
		}
	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			AndyUtils.removeCustomProgressDialog();
			if (intent.getAction().equals(CommonUtilities.DISPLAY_REGISTER_GCM)) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					int resultCode = bundle.getInt(CommonUtilities.RESULT);
					if (resultCode == Activity.RESULT_OK) {

					} else {
						Toast.makeText(MainActivity.this,
								getString(R.string.register_gcm_failed),
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent startRegisterActivity = new Intent(MainActivity.this,
				RegisterActivity.class);
		switch (v.getId()) {
		case R.id.btnSignIn:
			startRegisterActivity.putExtra("isSignin", true);
			break;
		case R.id.btnRegister:
			startRegisterActivity.putExtra("isSignin", false);
			break;
		}
		startActivity(startRegisterActivity);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	@Override
	public void onDestroy() {
		if (isReceiverRegister) {
			unregisterGcmReceiver(mHandleMessageReceiver);
			isReceiverRegister = false;
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		openExitDialog();
	}

	public void openExitDialog() {
		final Dialog mDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.exit_layout);
		mDialog.findViewById(R.id.tvExitOk).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.dismiss();
						finish();
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);

					}
				});
		mDialog.findViewById(R.id.tvExitCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.dismiss();
					}
				});
		mDialog.show();
	}
}