package com.automated.taxinow.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Hardik A Bhalodi
 */
public class HttpRequester {

	private Map<String, String> map;
	private AsyncTaskCompleteListener mAsynclistener;
	private int serviceCode;
	private boolean isGetMethod = false;
	private HttpClient httpclient;
	private Activity activity;
	private AsyncHttpRequest request;

	public HttpRequester(Activity activity, Map<String, String> map,
			int serviceCode, AsyncTaskCompleteListener asyncTaskCompleteListener) {
		this.map = map;
		this.serviceCode = serviceCode;
		this.activity = activity;
		this.isGetMethod = false;
		// is Internet Connection Available...

		if (AndyUtils.isNetworkAvailable(activity)) {
			mAsynclistener = (AsyncTaskCompleteListener) asyncTaskCompleteListener;
			request = (AsyncHttpRequest) new AsyncHttpRequest().execute(map
					.get("url"));
		} else {
			showToast("Network is not available!!!");
		}
	}

	public HttpRequester(Activity activity, Map<String, String> map,
			int serviceCode, boolean isGetMethod,
			AsyncTaskCompleteListener asyncTaskCompleteListener) {
		this.map = map;
		this.serviceCode = serviceCode;
		this.isGetMethod = isGetMethod;
		this.activity = activity;
		// is Internet Connection Available...

		if (AndyUtils.isNetworkAvailable(activity)) {
			mAsynclistener = (AsyncTaskCompleteListener) asyncTaskCompleteListener;
			request = (AsyncHttpRequest) new AsyncHttpRequest().execute(map
					.get("url"));
		} else {
			showToast("Network is not available!!!");
		}
	}

	class AsyncHttpRequest extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			map.remove("url");
			try {
				if (!isGetMethod) {
					HttpPost httppost = new HttpPost(urls[0]);
					httpclient = new DefaultHttpClient();

					HttpConnectionParams.setConnectionTimeout(
							httpclient.getParams(), 30000);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

					for (String key : map.keySet()) {
						AppLog.Log(Const.TAG,
								key + "  < === >  " + map.get(key));

						nameValuePairs.add(new BasicNameValuePair(key, map
								.get(key)));
					}

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					ActivityManager manager = (ActivityManager) activity
							.getSystemService(Context.ACTIVITY_SERVICE);

					if (manager.getMemoryClass() < 25) {
						System.gc();
					}
					HttpResponse response = httpclient.execute(httppost);

					String responseBody = EntityUtils.toString(response
							.getEntity());

					return responseBody;

				} else {
					httpclient = new DefaultHttpClient();

					// System.out.println("GET URL " + urls[0]);
					HttpConnectionParams.setConnectionTimeout(
							httpclient.getParams(), 30000);
					HttpGet httpGet = new HttpGet(urls[0]);

					HttpResponse httpResponse = httpclient.execute(httpGet);
					HttpEntity httpEntity = httpResponse.getEntity();

					String responseBody = EntityUtils.toString(httpEntity);
					return responseBody;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError oume) {
				System.gc();

				Toast.makeText(
						activity.getParent().getParent(),
						"Run out of memory please colse the other background apps and try again!",
						Toast.LENGTH_LONG).show();
			} finally {
				if (httpclient != null)
					httpclient.getConnectionManager().shutdown();

			}

			return null;

		}

		@Override
		protected void onPostExecute(String response) {

			if (mAsynclistener != null) {
				mAsynclistener.onTaskCompleted(response, serviceCode);
			}

		}
	}

	private void showToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	public void cancelTask() {

		request.cancel(true);
		// System.out.println("task is canelled");

	}
}
