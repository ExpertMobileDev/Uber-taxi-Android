package com.automated.taxinow.fragments;

import com.android.volley.VolleyError;
import com.automated.taxinow.CommonUtilities;
import com.automated.taxinow.R;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author Hardik A Bhalodi
 */
public class UberMainFragment extends UberBaseFragmentRegister {

	private ImageButton btnSignIn, btnRegister;
	private PreferenceHelper pHelper;
	private boolean isReceiverRegister;
	private int oldOptions;

	// private Animation topToBottomAnimation, bottomToTopAnimation;
	// private RelativeLayout rlLoginRegisterLayout;
	// private MyFontTextView tvMainBottomView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isReceiverRegister = false;
		pHelper = new PreferenceHelper(activity);
		// oldOptions = activity.getWindow().getDecorView()
		// .getSystemUiVisibility();
		// int newOptions = oldOptions;
		// newOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
		// newOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		// activity.getWindow().getDecorView().setSystemUiVisibility(newOptions);

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

		View view = inflater.inflate(R.layout.activity_main, container, false);

		btnSignIn = (ImageButton) view.findViewById(R.id.btnSignIn);
		btnRegister = (ImageButton) view.findViewById(R.id.btnRegister);
		// rlLoginRegisterLayout = (RelativeLayout) view
		// .findViewById(R.id.rlLoginRegisterLayout);
		// tvMainBottomView = (MyFontTextView) view
		// .findViewById(R.id.tvMainBottomView);
		btnSignIn.setOnClickListener(this);
		btnRegister.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (TextUtils.isEmpty(pHelper.getDeviceToken())) {
			isReceiverRegister = true;
			activity.registerGcmReceiver(mHandleMessageReceiver);
		}
		// topToBottomAnimation = AnimationUtils.loadAnimation(activity,
		// R.anim.top_bottom);
		// rlLoginRegisterLayout.setAnimation(topToBottomAnimation);
		// rlLoginRegisterLayout.startAnimation(topToBottomAnimation);
		// bottomToTopAnimation = AnimationUtils.loadAnimation(activity,
		// R.anim.bottom_top);
		// tvMainBottomView.setAnimation(bottomToTopAnimation);
		// tvMainBottomView.startAnimation(bottomToTopAnimation);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		activity.actionBar.hide();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSignIn:
			gotSignInFragment();
			break;
		case R.id.btnRegister:
			goToRegisterFragment();
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	private void gotSignInFragment() {
		UberSignInFragment signInFrag = new UberSignInFragment();
		activity.clearBackStack();
		activity.addFragment(signInFrag, false, Const.FRAGMENT_SIGNIN);
	}

	private void goToRegisterFragment() {
		UberRegisterFragment regFrag = new UberRegisterFragment();
		activity.clearBackStack();
		activity.addFragment(regFrag, false, Const.FRAGMENT_REGISTER);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (isReceiverRegister) {
			activity.unregisterGcmReceiver(mHandleMessageReceiver);
			isReceiverRegister = false;
		}
		super.onDestroy();
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
						Toast.makeText(
								activity,
								getResources().getString(
										R.string.register_gcm_failed),
								Toast.LENGTH_SHORT).show();
						activity.finish();
					}

				}
			}
		}
	};

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */
	// @Override
	// public boolean OnBackPressed() {
	// // TODO Auto-generated method stub
	// Toast.makeText(activity, "main", Toast.LENGTH_SHORT).show();
	// return false;
	// }

}
