package com.automated.taxinow.maputils;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Hardik A. Bhalodi
 * 
 */
@SuppressLint("JavascriptInterface")
public class PolyLineUtils {
	private Activity activity;
//	private OnPolyLineBufferPathSuccessListner onPolySuccessListner;

	public PolyLineUtils() {
		// TODO Auto-generated constructor stub
	}

	public PolyLineUtils(Activity activity) {
		this.activity = activity;
//		this.onPolySuccessListner = (OnPolyLineBufferPathSuccessListner) activity;
	}

	public List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	/**
	 * Decodes an encoded path string into a sequence of LatLngs.
	 */
	public List<LatLng> decode(final String encodedPath) {
		int len = encodedPath.length();

		// For speed we preallocate to an upper bound on the final length, then
		// truncate the array before returning.
		final List<LatLng> path = new ArrayList<LatLng>();
		int index = 0;
		int lat = 0;
		int lng = 0;

		for (int pointIndex = 0; index < len; ++pointIndex) {
			int result = 1;
			int shift = 0;
			int b;
			do {
				b = encodedPath.charAt(index++) - 63 - 1;
				result += b << shift;
				shift += 5;
			} while (b >= 0x1f);
			lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

			result = 1;
			shift = 0;
			do {
				b = encodedPath.charAt(index++) - 63 - 1;
				result += b << shift;
				shift += 5;
			} while (b >= 0x1f);
			lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

			path.add(new LatLng(lat * 1e-5, lng * 1e-5));
		}

		return path;
	}

	/**
	 * Encodes a sequence of LatLngs into an encoded path string.
	 */
	public String encode(final List<LatLng> path) {
		long lastLat = 0;
		long lastLng = 0;

		final StringBuffer result = new StringBuffer();

		for (final LatLng point : path) {
			long lat = Math.round(point.latitude * 1e5);
			long lng = Math.round(point.longitude * 1e5);

			long dLat = lat - lastLat;
			long dLng = lng - lastLng;

			encode(dLat, result);
			encode(dLng, result);

			lastLat = lat;
			lastLng = lng;
		}
		return result.toString();
	}

	private void encode(long v, StringBuffer result) {
		v = v < 0 ? ~(v << 1) : v << 1;
		while (v >= 0x20) {
			result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
			v >>= 5;
		}
		result.append(Character.toChars((int) (v + 63)));
	}

	// Function build url from points and call the buffer function
//	public void getPolyGoneBufferPath(ArrayList<LatLng> points) {
//		String url = "";
//		for (int i = 0; i < points.size(); i++) {
//
//			LatLng loc = points.get(i);
//
//			if (i == 0) {
//				url = loc.latitude + "," + loc.longitude;
//
//			} else {
//				url = url + "|" + loc.latitude + "," + loc.longitude;
//
//			}
//		}
//		getPolyGoneBufferPathUsingJSI(url);
//	}

	// This function Convert polygone to buffer polygone using javascript

//	private void getPolyGoneBufferPathUsingJSI(final String polyUrl) {
//		if (activity != null) {
//
//			WebView wv = new WebView(activity);
//			wv.getSettings().setJavaScriptEnabled(true);
//			wv.setWebViewClient(new WebViewClient() {
//				@Override
//				public void onPageFinished(WebView view, String url) {
//
//					StringBuilder buf = new StringBuilder(
//							"javascript:getPolyGone(");
//					buf.append("\"");
//					buf.append(polyUrl);
//					buf.append("\"");
//					buf.append(")");
//					System.out.println("polyurl========>" + buf.toString());
//					view.loadUrl(buf.toString());
//				}
//			});
//			wv.addJavascriptInterface(new MyJavaScriptInterface() {
//
//				@Override
//				public void receiveValueFromJs(final String str) {
//					if (onPolySuccessListner != null) {
//						onPolySuccessListner.polyLineBufferPathSuccess(str);
//					} else {
//						System.out.println("Listner not register...");
//					}
//
//				}
//			}, "MyAndroid");
//			wv.loadUrl("file:///android_asset/polygonebufferpath.html");
//		}
//	}

}
