/**
 * 
 */
package com.automated.taxinow.fragments;

import java.util.HashMap;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.automated.taxinow.MainDrawerActivity;
import com.automated.taxinow.R;
import com.automated.taxinow.component.MyFontEdittextView;
import com.automated.taxinow.parse.AsyncTaskCompleteListener;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.parse.VolleyHttpRequest;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Kishan H Dhamat
 * 
 */
public class ReffralCodeFragment extends UberBaseFragmentRegister implements
		AsyncTaskCompleteListener {
	private MyFontEdittextView etRefCode;
	private String token, id;
	private LinearLayout llErrorMsg;
	private int is_skip = 0;
	private RequestQueue requestQueue;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.automated.taxinow.fragments.UberBaseFragmentRegister#onCreate(android
	 * .os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		token = getArguments().getString(Const.Params.TOKEN);
		id = getArguments().getString(Const.Params.ID);
		requestQueue = Volley.newRequestQueue(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		activity.setIconMenu(R.drawable.nav_referral);
		activity.setTitle(getString(R.string.text_referral_code));
		activity.btnNotification.setVisibility(View.INVISIBLE);
		View refView = inflater.inflate(R.layout.ref_code_fragment, container,
				false);
		etRefCode = (MyFontEdittextView) refView.findViewById(R.id.etRefCode);
		etRefCode.setHint(getString(R.string.text_enter_ref_code));
		llErrorMsg = (LinearLayout) refView.findViewById(R.id.llErrorMsg);
		refView.findViewById(R.id.btnRefSubmit).setOnClickListener(this);
		refView.findViewById(R.id.btnSkip).setOnClickListener(this);

		return refView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		etRefCode.requestFocus();
		activity.showKeyboard(etRefCode);
		// (getResources().getString(
		// R.string.text_forget_password));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.automated.taxinow.fragments.UberBaseFragmentRegister#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		activity.currentFragment = Const.FRAGMENT_REFFREAL;
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
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
			applyReffralCode(true);
			this.OnBackPressed();
			break;

		default:
			break;
		}
	}

	private void applyReffralCode(boolean isShowLoader) {
		if (isShowLoader)
			AndyUtils.showCustomProgressDialog(activity,
					getString(R.string.progress_loading), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.APPLY_REFFRAL_CODE);
		map.put(Const.Params.REFERRAL_CODE, etRefCode.getText().toString());
		map.put(Const.Params.ID, id);
		map.put(Const.Params.TOKEN, token);
		map.put(Const.Params.IS_SKIP, String.valueOf(is_skip));
		// new HttpRequester(activity, map,
		// Const.ServiceCode.APPLY_REFFRAL_CODE,
		// this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.APPLY_REFFRAL_CODE, this, this));

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
		AppLog.Log(Const.TAG, "Apply-Referral Response ::: " + response);
		switch (serviceCode) {
		case Const.ServiceCode.APPLY_REFFRAL_CODE:
			if (new ParseContent(activity).isSuccess(response)) {
				new PreferenceHelper(activity).putReferee(1);
				gotoPaymentFragment();
				// activity.startActivity(new Intent(activity,
				// MainDrawerActivity.class));
			} else {
				llErrorMsg.setVisibility(View.VISIBLE);
				etRefCode.requestFocus();
			}
			break;

		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.UberBaseFragmentRegister#isValidate()
	 */
	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.UberBaseFragmentRegister#OnBackPressed()
	 */
	@Override
	public boolean OnBackPressed() {
		// TODO Auto-generated method stub
		// is_skip = 1;
		// applyReffralCode();
		// gotoPaymentFragment();
		return true;
	}

	private void gotoPaymentFragment() {
		UberAddPaymentFragmentRegister paymentFragment = new UberAddPaymentFragmentRegister();
		Bundle bundle = new Bundle();
		bundle.putString(Const.Params.TOKEN, token);
		bundle.putString(Const.Params.ID, id);
		paymentFragment.setArguments(bundle);
		activity.addFragment(paymentFragment, false,
				Const.FRAGMENT_PAYMENT_REGISTER);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		AppLog.Log(Const.TAG, error.getMessage());

	}

}
