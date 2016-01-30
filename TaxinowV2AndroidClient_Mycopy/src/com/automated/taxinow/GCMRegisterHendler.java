package com.automated.taxinow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.google.android.gcm.GCMRegistrar;

public class GCMRegisterHendler {

	private Activity activity;

	public GCMRegisterHendler(Activity activity,
			BroadcastReceiver mHandleMessageReceiver) {

		try {
			this.activity = activity;
			checkNotNull(CommonUtilities.SENDER_ID, "SENDER_ID");

			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(activity);
			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(activity);

			// mDisplay = (TextView) findViewById(R.id.display);
			activity.registerReceiver(mHandleMessageReceiver, new IntentFilter(
					CommonUtilities.DISPLAY_MESSAGE_ACTION));
			final String regId = GCMRegistrar.getRegistrationId(activity);
			AppLog.Log(Const.TAG, "registered: regId = " + regId);
			// System.out.println(regId + " +++++++++??????????");
			if (TextUtils.isEmpty(regId)) {
				// Automatically registers application on startup.
				GCMRegistrar.register(activity, CommonUtilities.SENDER_ID);
			} else {
				AppLog.Log(Const.TAG, "Already Device registered: regId = "
						+ regId);
				publishResults(regId, Activity.RESULT_OK);
				// onRegComplete(regId);
				// Device is already registered on GCM, check server.
				/*
				 * if (GCMRegistrar.isRegisteredOnServer(this)) { // Skips
				 * registration. //
				 * mDisplay.append(getString(R.string.already_registered) + //
				 * "\n"); } else { // Try to register again, but not in the UI
				 * thread. // It's also necessary to cancel the thread
				 * onDestroy(), // hence the use of AsyncTask instead of a raw
				 * thread. final Context context = this;
				 * GCMRegistrar.unregister(context);
				 * 
				 * }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// /////////////////////////////////////////////
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(
					"sender id is null please recompile the app");
		}
	}

	// public static void onRegComplete(String registrationId) {
	//
	// this.activity.runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// AndyUtils.removeSimpleProgressDialog();
	// }
	// });

	// System.out.println("device token===>" + registrationId);
	//
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("url", Constanants.SERVER_URL);
	// map.put("action", Constanants.ACTION_UPDATE_TOKEN);
	// map.put("user_id", Me.getUser_id());
	// map.put("user_session", Me.getUser_session());
	// map.put("user_device_type", "android");
	//
	// map.put("user_device_token", registrationId);
	// new ParsingController(GCMRegisterHendler.activity, map,
	// Constanants.UPDATE_TOKEN, true);
	// }

	private void publishResults(String regid, int result) {
		AndyUtils.removeSimpleProgressDialog();
		Intent intent = new Intent(CommonUtilities.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(CommonUtilities.RESULT, result);
		intent.putExtra(CommonUtilities.REGID, regid);
		// System.out.println("sending broad cast");
		activity.sendBroadcast(intent);
	}
}
