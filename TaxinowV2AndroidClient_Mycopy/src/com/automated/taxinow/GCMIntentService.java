/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.automated.taxinow;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;

import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	@SuppressWarnings("hiding")
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		// Log.i(TAG, "Device registered: regId = " + registrationId);
		CommonUtilities.displayMessage(context, "Device Registerd");
		// SSConstanants.DEVICE_TOKEN=registrationId;
		// Create object of SharedPreferences.
		new PreferenceHelper(context).putDeviceToken(registrationId);
		// System.out.println(registrationId + "========>>>>>>");
		AppLog.Log(Const.TAG, registrationId);
		publishResults(registrationId, Activity.RESULT_OK);
		// GCMRegisterHendler.onRegComplete(registrationId);

		/*************************
		 * ParseObject pObj = new ParseObject("PushNoti");
		 * pObj.put("DeviceToken",registrationId); pObj.put("InRange",true);
		 * pObj.put("DeviceType","android"); //pObj.put("ACL","");
		 * pObj.saveInBackground(); displayMessage(context,
		 * getString(R.string.gcm_registered)); //
		 * ServerUtilities.register(context, registrationId);
		 * 
		 */
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		// Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context, "Device Unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {

		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			// Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// Log.i(TAG, "Received message");
		// String message = getString(R.string.gcm_message);
		AppLog.Log(Const.TAG, intent.getExtras() + "");
		String message = intent.getExtras().getString("message");
		String team = intent.getExtras().getString("team");
		AppLog.Log("Notificaton", message);
		AppLog.Log("Team", team);
		String title = intent.getExtras().getString("title");
		Intent pushIntent = new Intent(Const.INTENT_WALKER_STATUS);
		pushIntent.putExtra(Const.EXTRA_WALKER_STATUS, team);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
		LocalBroadcastManager.getInstance(context).sendBroadcast(pushIntent);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		String message = "message deleted " + total;
		CommonUtilities.displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	@Override
	public void onError(Context context, String errorId) {
		// Log.i(TAG, "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		// Log.i(TAG, "Received recoverable error: " + errorId);
		// displayMessage(context,
		// getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private void generateNotification(Context context, String message) {
		// System.out.println("this is message " + message);
		// System.out.println("NOTIFICATION RECEIVED!!!!!!!!!!!!!!" + message);
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context,
				MainDrawerActivity.class);
		notificationIntent.putExtra("fromNotification", "notification");
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		System.out.println("notification====>" + message);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		// notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.ledARGB = 0x00000000;
		notification.ledOnMS = 0;
		notification.ledOffMS = 0;
		notificationManager.notify(0, notification);
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(
				PowerManager.FULL_WAKE_LOCK
						| PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.ON_AFTER_RELEASE, "WakeLock");
		wakeLock.acquire();
		wakeLock.release();
	}

	private void publishResults(String regid, int result) {
		Intent intent = new Intent(CommonUtilities.DISPLAY_REGISTER_GCM);
		intent.putExtra(CommonUtilities.RESULT, result);
		intent.putExtra(CommonUtilities.REGID, regid);
		sendBroadcast(intent);
	}
}