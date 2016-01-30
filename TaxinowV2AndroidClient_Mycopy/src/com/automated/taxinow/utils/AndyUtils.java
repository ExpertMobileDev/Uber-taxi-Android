package com.automated.taxinow.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.automated.taxinow.R;
import com.automated.taxinow.interfaces.OnProgressCancelListener;
import com.automated.taxinow.models.Driver;

/**
 * @author Hardik A Bhalodi
 */
@SuppressLint("NewApi")
public class AndyUtils {
	static float density = 1;
	private static ProgressDialog mProgressDialog;
	private static Dialog mDialog;
	private static OnProgressCancelListener progressCancelListener;
	private int driverId = 0;

	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		return display.getWidth();
	}

	public static void showSimpleProgressDialog(Context context, String title,
			String msg, boolean isCancelable) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(context, title, msg);
				mProgressDialog.setCancelable(isCancelable);
			}

			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}

		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showSimpleProgressDialog(Context context) {
		showSimpleProgressDialog(context, null, "Loading...", false);
	}

	// public static void showCustomeProgressDialog(Context context, String msg,
	// boolean isCancelable) {
	// try {
	//
	// if (msg == null || msg.equalsIgnoreCase("")) {
	// msg = "Loading...";
	// }
	// if (mDialog == null) {
	// mDialog = new Dialog(context);
	// mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// mDialog.setCancelable(isCancelable);
	// mDialog.setContentView(R.layout.custom_progress_dialog);
	// }
	// TextView txtView = (TextView) mDialog.findViewById(R.id.textView);
	// txtView.setText(msg);
	// if (!mDialog.isShowing())
	// mDialog.show();
	//
	// } catch (IllegalArgumentException ie) {
	// ie.printStackTrace();
	// } catch (RuntimeException re) {
	// re.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void removeSimpleProgressDialog() {
		try {
			if (mProgressDialog != null) {
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static void removeCustomeProgressDialog() {
	// try {
	// if (mDialog != null) {
	// if (mDialog.isShowing()) {
	// mDialog.dismiss();
	// mDialog = null;
	// }
	// }
	// } catch (IllegalArgumentException ie) {
	// ie.printStackTrace();
	//
	// } catch (RuntimeException re) {
	// re.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

	public static void showNetworkErrorMessage(final Context context) {
		Builder dlg = new AlertDialog.Builder(context);
		dlg.setCancelable(false);
		dlg.setTitle("Error");
		dlg.setMessage("Network error has occured. Please check the network status of your phone and retry");
		dlg.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dlg.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
				System.exit(0);
			}
		});
		dlg.show();
	}

	public static void showOkDialog(String title, String msg, Activity act) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(act);
		if (title != null) {

			TextView dialogTitle = new TextView(act);
			dialogTitle.setText(title);
			dialogTitle.setPadding(10, 10, 10, 10);
			dialogTitle.setGravity(Gravity.CENTER);
			dialogTitle.setTextColor(Color.WHITE);
			dialogTitle.setTextSize(20);
			dialog.setCustomTitle(dialogTitle);

		}
		if (msg != null) {
			dialog.setMessage(msg);
		}
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		AlertDialog dlg = dialog.show();
		TextView messageText = (TextView) dlg
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);

	}

	public static float getDisplayMetricsDensity(Context context) {
		density = context.getResources().getDisplayMetrics().density;

		return density;
	}

	public static int getPixel(Context context, int p) {
		if (density != 1) {
			return (int) (p * density + 0.5);
		}
		return p;
	}

	public static Animation FadeAnimation(float nFromFade, float nToFade) {
		Animation fadeAnimation = new AlphaAnimation(nToFade, nToFade);

		return fadeAnimation;
	}

	public static Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromRight;
	}

	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromLeft;
	}

	public static Animation inFromBottomAnimation() {
		Animation inFromBottom = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromBottom;
	}

	public static Animation outToLeftAnimation() {
		Animation outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToLeft;
	}

	public static Animation outToRightAnimation() {
		Animation outToRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToRight;
	}

	public static Animation outToBottomAnimation() {
		Animation outToBottom = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f);

		return outToBottom;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean eMailValidation(String emailstring) {
		if (null == emailstring || emailstring.length() == 0) {
			return false;
		}
		Pattern emailPattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	//
	// public static SimpleDateFormat inputDateTime = new SimpleDateFormat(
	// "yyyy-MM-dd HH:mm:ss");
	// public static SimpleDateFormat inputDate = new SimpleDateFormat(
	// "yyyy-MM-dd");
	// public static SimpleDateFormat output = new SimpleDateFormat(
	// "MMMM dd, yyyy");
	// public static SimpleDateFormat outputDateFormate = new SimpleDateFormat(
	// "MMM dd, yyyy");
	// public static SimpleDateFormat simpleFormate = new SimpleDateFormat(
	// "yyyyMMdd");

	public static String urlBuilderForGetMethod(Map<String, String> g_map) {

		StringBuilder sbr = new StringBuilder();
		int i = 0;
		if (g_map.containsKey("url")) {
			sbr.append(g_map.get("url"));
			g_map.remove("url");
		}
		for (String key : g_map.keySet()) {
			if (i != 0) {
				sbr.append("&");
			}
			sbr.append(key + "=" + g_map.get(key));
			i++;
		}
		System.out.println("Builder url = " + sbr.toString());
		return sbr.toString();
	}

	public static int isValidate(String... fields) {
		if (fields == null) {
			return 0;
		}

		for (int i = 0; i < fields.length; i++) {
			if (TextUtils.isEmpty(fields[i])) {
				return i;
			}

		}
		return -1;
	}

	public static void showToast(String msg, Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static String UppercaseFirstLetters(String str) {
		boolean prevWasWhiteSp = true;
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				if (prevWasWhiteSp) {
					chars[i] = Character.toUpperCase(chars[i]);
				}
				prevWasWhiteSp = false;
			} else {
				prevWasWhiteSp = Character.isWhitespace(chars[i]);
			}
		}
		return new String(chars);
	}

	// public static String CapitalizeFirstLetterInStringUsingWordUtils(String
	// str) {
	//
	// // // only the first letter of each word is capitalized.
	// // String wordStr =
	// // WordUtils.capitalize("this is first WORD capital test.");
	// // //Capitalize method capitalizes only first character of a String
	// // System.out.println("wordStr= " + wordStr);
	// // This method capitalizes first character of a String and make rest of
	// // the characters lowercase
	// return WordUtils.capitalizeFully(str);
	//
	// }
	// public static String encodeTobase64(Bitmap image) throws IOException {
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
	// byte[] bs = outputStream.toByteArray();
	// String imageEncoded = Base64.encodeBytes(bs);
	// return imageEncoded;
	// }

	public static void buttonEffect(ImageButton button, final int alpha) {

		button.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			public boolean onTouch(View v, MotionEvent event) {
				ImageButton btn = (ImageButton) v;
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: {

					if (Build.VERSION.SDK_INT > 15) {
						btn.setImageAlpha(alpha);

					} else {
						btn.setAlpha(alpha);
					}

					break;
				}
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:

					if (Build.VERSION.SDK_INT > 15) {

						btn.setImageAlpha(255);
					} else {
						btn.setAlpha(255);
					}

					break;
				}
				return false;
			}
		});

	}

	public static final SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	}

	public static String convertToUTF8(String s) {
		String out = null;
		try {
			out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static void showCustomProgressDialog(Context context, String title,
			boolean iscancelable,
			OnProgressCancelListener progressCancelListener) {
		if (mDialog != null && mDialog.isShowing())
			return;
		AndyUtils.progressCancelListener = progressCancelListener;

		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		ImageView imageView = (ImageView) mDialog
				.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		RelativeLayout includeDriver = (RelativeLayout) mDialog
				.findViewById(R.id.includeDriver);
		includeDriver.setVisibility(View.GONE);
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		imageView.setBackgroundResource(R.drawable.loading);
		final AnimationDrawable frameAnimation = (AnimationDrawable) imageView
				.getBackground();
		mDialog.setCancelable(iscancelable);
		imageView.post(new Runnable() {

			@Override
			public void run() {
				frameAnimation.start();
				frameAnimation.setOneShot(false);
			}
		});
		mDialog.show();
	}

	public static void showCustomProgressDialog(Context context, String title,
			boolean iscancelable,
			OnProgressCancelListener progressCancelListener, Driver driver) {
		if (mDialog != null && mDialog.isShowing())
			return;
		AndyUtils.progressCancelListener = progressCancelListener;
		ImageOptions imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.default_user;
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		ImageView imageView = (ImageView) mDialog
				.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		RelativeLayout includeDriver = (RelativeLayout) mDialog
				.findViewById(R.id.includeDriver);
		if (driver == null)
			includeDriver.setVisibility(View.GONE);
		else {
			includeDriver.setVisibility(View.VISIBLE);
			ImageView ivDriverPhoto = (ImageView) mDialog
					.findViewById(R.id.ivDriverPhoto);
			TextView tvDriverName = (TextView) mDialog
					.findViewById(R.id.tvDriverName);
			TextView tvTaxiModel = (TextView) mDialog
					.findViewById(R.id.tvTaxiModel);
			TextView tvTaxiNo = (TextView) mDialog.findViewById(R.id.tvTaxiNo);
			TextView tvRateStar = (TextView) mDialog
					.findViewById(R.id.tvRateStar);

			new AQuery(context).id(ivDriverPhoto).progress(R.id.pBar)
					.image(driver.getPicture(), imageOptions);
			tvDriverName.setText(driver.getFirstName() + " "
					+ driver.getLastName());
			tvTaxiModel.setText(driver.getCarModel());
			tvTaxiNo.setText(driver.getCarNumber());
			tvRateStar.setText(MathUtils.getRound((float) driver.getRating())
					+ "");
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		imageView.setBackgroundResource(R.drawable.loading);
		final AnimationDrawable frameAnimation = (AnimationDrawable) imageView
				.getBackground();
		mDialog.setCancelable(iscancelable);
		imageView.post(new Runnable() {

			@Override
			public void run() {
				frameAnimation.start();
				frameAnimation.setOneShot(false);
			}
		});
		mDialog.show();
	}

	public static void removeCustomProgressDialog() {
		try {
			AndyUtils.progressCancelListener = null;
			if (mDialog != null) {
				// if (mProgressDialog.isShowing()) {
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showCustomProgressRequestDialog(Context context,
			String title, boolean iscancelable,
			OnProgressCancelListener progressCancelListener) {
		if (mDialog != null && mDialog.isShowing())
			return;
		AndyUtils.progressCancelListener = progressCancelListener;

		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		ImageView imageView = (ImageView) mDialog
				.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		imageView.setBackgroundResource(R.drawable.loading);
		final AnimationDrawable frameAnimation = (AnimationDrawable) imageView
				.getBackground();
		mDialog.setCancelable(iscancelable);
		imageView.post(new Runnable() {
			@Override
			public void run() {
				frameAnimation.start();
				frameAnimation.setOneShot(false);
			}
		});
		mDialog.show();
	}

	public static void removeCustomProgressRequestDialog() {
		try {
			AndyUtils.progressCancelListener = null;
			if (mDialog != null) {
				// if (mProgressDialog.isShowing()) {
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encodeTobase64(Bitmap image) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
		byte[] bs = outputStream.toByteArray();
		String imageEncoded = Base64.encodeBytes(bs);
		return imageEncoded;
	}

	public static void showErrorToast(int id, Context ctx) {
		String msg = "";
		switch (id) {
		case 401:
			msg = ctx.getResources().getString(R.string.error_401);
			break;
		case 402:
			msg = ctx.getResources().getString(R.string.error_402);
			break;
		case 403:
			msg = ctx.getResources().getString(R.string.error_403);
			break;
		case 404:
			msg = ctx.getResources().getString(R.string.error_404);
			break;
		case 405:
			msg = ctx.getResources().getString(R.string.error_405);
			break;
		case 406:
			msg = ctx.getResources().getString(R.string.error_406);
			break;
		case 407:
			msg = ctx.getResources().getString(R.string.error_407);
			break;
		case 408:
			msg = ctx.getResources().getString(R.string.error_408);
			break;
		case 409:
			msg = ctx.getResources().getString(R.string.error_409);
			break;
		case 410:
			msg = ctx.getResources().getString(R.string.error_410);
			break;
		case 411:
			msg = ctx.getResources().getString(R.string.error_411);
			break;
		case 412:
			msg = ctx.getResources().getString(R.string.error_412);
			break;
		case 413:
			msg = ctx.getResources().getString(R.string.error_413);
			break;
		case 414:
			msg = ctx.getResources().getString(R.string.error_414);
			break;
		case 415:
			msg = ctx.getResources().getString(R.string.error_415);
			break;
		case 416:
			msg = ctx.getResources().getString(R.string.error_416);
			break;
		case 417:
			msg = ctx.getResources().getString(R.string.error_417);
			break;
		default:
			break;
		}
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}
}
