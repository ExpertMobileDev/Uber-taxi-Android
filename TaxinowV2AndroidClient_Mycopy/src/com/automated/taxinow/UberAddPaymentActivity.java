package com.automated.taxinow;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.automated.taxinow.parse.HttpRequester;
import com.automated.taxinow.parse.ParseContent;
import com.automated.taxinow.parse.VolleyHttpRequest;
import com.automated.taxinow.utils.AndyUtils;
import com.automated.taxinow.utils.AppLog;
import com.automated.taxinow.utils.Const;
import com.automated.taxinow.utils.PreferenceHelper;
import com.mobile.connect.exception.PWError;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.listener.PWTokenObtainedListener;
import com.mobile.connect.listener.PWTransactionListener;
import com.mobile.connect.provider.PWTransaction;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.util.TextUtils;

/**
 * @author Hardik A Bhalodi
 */
public class UberAddPaymentActivity extends ActionBarBaseActivitiy
// implements PWTokenObtainedListener, PWTransactionListener
{
	private static final String TAG = "UberAddPaymentActivity";
	private Button btnAddPayment, btnPaymentSkip;
	private ImageView btnScan;
	private final int MY_SCAN_REQUEST_CODE = 111;
	private EditText etCreditCardNum, etCvc, etYear, etMonth;
	// private String patternVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
	// private String patternMasterCard = "^5[1-5][0-9]{14}$";
	// private String patternAmericanExpress = "^3[47][0-9]{13}$";
	public static final String[] PREFIXES_AMERICAN_EXPRESS = { "34", "37" };
	public static final String[] PREFIXES_DISCOVER = { "60", "62", "64", "65" };
	public static final String[] PREFIXES_JCB = { "35" };
	public static final String[] PREFIXES_DINERS_CLUB = { "300", "301", "302",
			"303", "304", "305", "309", "36", "38", "37", "39" };
	public static final String[] PREFIXES_VISA = { "4" };
	public static final String[] PREFIXES_MASTERCARD = { "50", "51", "52",
			"53", "54", "55" };
	public static final String AMERICAN_EXPRESS = "American Express";
	public static final String DISCOVER = "Discover";
	public static final String JCB = "JCB";
	public static final String DINERS_CLUB = "Diners Club";
	public static final String VISA = "Visa";
	public static final String MASTERCARD = "MasterCard";
	public static final String UNKNOWN = "Unknown";
	public static final int MAX_LENGTH_STANDARD = 16;
	public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
	public static final int MAX_LENGTH_DINERS_CLUB = 14;
	private String type;
	// private PWCreditCardType cardType;
	// private boolean currentTokenization;
	// private PWProviderBinder _binder;
	//
	// private ServiceConnection _serviceConnection = new ServiceConnection() {
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// _binder = (PWProviderBinder) service;
	// // we have a connection to the service
	// try {
	// _binder.initializeProvider(PWProviderMode.LIVE,
	// Const.APPLICATIONIDENTIFIER, Const.PROFILETOKEN);
	// _binder.addTokenObtainedListener(UberAddPaymentActivity.this);
	// _binder.addTransactionListener(UberAddPaymentActivity.this);
	// } catch (PWException ee) {
	// setStatusText("Error initializing the provider.");
	// // error initializing the provider
	// ee.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// _binder = null;
	// }
	// };
	private EditText etHolder;
	private RequestQueue requestQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_payment);
		// startService(new Intent(this,
		// com.mobile.connect.service.PWConnectService.class));
		// bindService(new Intent(this,
		// com.mobile.connect.service.PWConnectService.class),
		// _serviceConnection, Context.BIND_AUTO_CREATE);
		setTitle(getString(R.string.text_add_payment));
		setIconMenu(R.drawable.ic_payment);
		setIcon(R.drawable.back);
		requestQueue = Volley.newRequestQueue(this);
		btnAddPayment = (Button) findViewById(R.id.btnAddPayment);
		btnPaymentSkip = (Button) findViewById(R.id.btnPaymentSkip);
		btnPaymentSkip.setVisibility(View.GONE);
		btnScan = (ImageView) findViewById(R.id.btnScan);
		etCreditCardNum = (EditText) findViewById(R.id.edtRegisterCreditCardNumber);
		etCreditCardNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (TextUtils.isBlank(s.toString())) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							null, null, null, null);
				}
				type = getType(s.toString());

				if (type.equals(VISA)) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ub__creditcard_visa), null,
							null, null);

				} else if (type.equals(MASTERCARD)) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ub__creditcard_mastercard),
							null, null, null);

				} else if (type.equals(AMERICAN_EXPRESS)) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ub__creditcard_amex), null,
							null, null);

				} else if (type.equals(DISCOVER)) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ub__creditcard_discover), null,
							null, null);

				} else if (type.equals(DINERS_CLUB)) {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							getResources().getDrawable(
									R.drawable.ub__creditcard_discover), null,
							null, null);

				} else {
					etCreditCardNum.setCompoundDrawablesWithIntrinsicBounds(
							null, null, null, null);
				}
				if (etCreditCardNum.getText().toString().length() == 16) {
					etMonth.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		etCvc = (EditText) findViewById(R.id.edtRegistercvc);
		etYear = (EditText) findViewById(R.id.edtRegisterexpYear);
		etMonth = (EditText) findViewById(R.id.edtRegisterexpMonth);
		etYear.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (etYear.getText().toString().length() == 4) {
					etCvc.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		etMonth.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (etMonth.getText().toString().length() == 2) {
					etYear.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		etHolder = (EditText) findViewById(R.id.edtRegisterCreditCardHolder);
		btnScan.setOnClickListener(this);
		btnAddPayment.setOnClickListener(this);
		findViewById(R.id.btnPaymentSkip).setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnAddPayment:
			if (isValidate()) {
				saveCreditCard();
			}
			break;
		case R.id.btnScan:
			scan();
			break;
		case R.id.btnActionNotification:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		if (etCreditCardNum.getText().length() == 0
				|| etCvc.getText().length() == 0
				|| etMonth.getText().length() == 0
				|| etYear.getText().length() == 0) {
			AndyUtils.showToast("Enter Proper data", this);
			return false;
		}
		return true;
	}

	private void scan() {
		Intent scanIntent = new Intent(this, CardIOActivity.class);

		// required for authentication with card.io
		// scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN,
		// Const.MY_CARDIO_APP_TOKEN);

		// customize these values to suit your needs.
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default:
																		// true
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default:
																		// false
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default:
																				// false

		// hides the manual entry button
		// if set, developers should provide their own manual entry
		// mechanism in
		// the app
		scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default:
																				// false

		// MY_SCAN_REQUEST_CODE is arbitrary and is only used within this
		// activity.
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MY_SCAN_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null
						&& data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
					CreditCard scanResult = data
							.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

					// Never log a raw card number. Avoid displaying it, but if
					// necessary use getFormattedCardNumber()
					// resultStr = "Card Number: " +
					// scanResult.getRedactedCardNumber()
					// + "\n";
					etCreditCardNum.setText(scanResult.getRedactedCardNumber());

					// Do something with the raw number, e.g.:
					// myService.setCardNumber( scanResult.cardNumber );

					if (scanResult.isExpiryValid()) {
						// resultStr += "Expiration Date: " +
						// scanResult.expiryMonth
						// +
						// "/"
						// + scanResult.expiryYear + "\n";
						etMonth.setText(scanResult.expiryMonth + "");
						etYear.setText(scanResult.expiryYear + "");
					}

					if (scanResult.cvv != null) {
						// Never log or display a CVV
						// resultStr += "CVV has " + scanResult.cvv.length()
						// + " digits.\n";
						etCvc.setText(scanResult.cvv);
					}

					// if (scanResult.postalCode != null) {
					// resultStr += "Postal Code: " + scanResult.postalCode +
					// "\n";
					// }
				} else {
					// resultStr = "Scan was canceled.";
					AndyUtils.showToast("Scan was canceled.", this);
				}
			} else {
				AndyUtils.showToast("Scan was uncessfull.", this);
			}
			break;

		}

	}

	public void saveCreditCard() {
		// AndyUtils.showCustomProgressDialog(this,
		// getString(R.string.adding_payment), false, null);
		// PWPaymentParams paymentParams = null;
		// try {
		// if (getType(etCreditCardNum.getText().toString().trim()).equals(
		// VISA)) {
		// cardType = PWCreditCardType.VISA;
		// } else if (getType(etCreditCardNum.getText().toString().trim())
		// .equals(AMERICAN_EXPRESS)) {
		// cardType = PWCreditCardType.AMEX;
		// } else if (getType(etCreditCardNum.getText().toString().trim())
		// .equals(DINERS_CLUB)) {
		// cardType = PWCreditCardType.DINERS;
		// } else if (getType(etCreditCardNum.getText().toString().trim())
		// .equals(JCB)) {
		// cardType = PWCreditCardType.JCB;
		// } else if (getType(etCreditCardNum.getText().toString().trim())
		// .equals(MASTERCARD)) {
		// cardType = PWCreditCardType.MASTERCARD;
		// }
		// paymentParams = _binder.getPaymentParamsFactory()
		// .createCreditCardTokenizationParams(
		// etHolder.getText().toString(), cardType,
		// etCreditCardNum.getText().toString(),
		// etYear.getText().toString(),
		// etMonth.getText().toString(),
		// etCvc.getText().toString());
		// } catch (PWProviderNotInitializedException e) {
		// AndyUtils.removeCustomProgressDialog();
		// AndyUtils.showToast("Error: Provider not initialized!", this);
		// return;
		// } catch (PWException e) {
		// AndyUtils.removeCustomProgressDialog();
		// AndyUtils.showToast("Error: Invalid Parameters!", this);
		// return;
		// }
		//
		// setStatusText("Preparing...");
		// currentTokenization = true;
		//
		// try {
		// _binder.createAndRegisterObtainTokenTransaction(paymentParams);
		// } catch (PWException e) {
		// setStatusText("Error: Could not contact Gateway!");
		// }
		Card card = new Card(etCreditCardNum.getText().toString(),
				Integer.parseInt(etMonth.getText().toString()),
				Integer.parseInt(etYear.getText().toString()), etCvc.getText()
						.toString());

		boolean validation = card.validateCard();
		if (validation) {
			AndyUtils.showCustomProgressDialog(this,
					getString(R.string.adding_payment), false, null);
			new Stripe().createToken(card, Const.PUBLISHABLE_KEY,
					new TokenCallback() {
						public void onSuccess(Token token) {
							// getTokenList().addToList(token);
							// AndyUtils.showToast(token.getId(), activity);
							String lastFour = etCreditCardNum.getText()
									.toString().toString();
							lastFour = lastFour.substring(lastFour.length() - 4);
							addCard(token.getId(), lastFour);
							// finishProgress();
						}

						public void onError(Exception error) {
							AndyUtils.showToast("Error",
									UberAddPaymentActivity.this);
							// finishProgress();
							AndyUtils.removeCustomProgressDialog();
						}
					});
		} else if (!card.validateNumber()) {
			// handleError("The card number that you entered is invalid");
			AndyUtils.showToast("The card number that you entered is invalid",
					this);
		} else if (!card.validateExpiryDate()) {
			// handleError("");
			AndyUtils.showToast(
					"The expiration date that you entered is invalid", this);
		} else if (!card.validateCVC()) {
			// handleError("");
			AndyUtils.showToast("The CVC code that you entered is invalid",
					this);

		} else {
			// handleError("");
			AndyUtils.showToast(
					"The card details that you entered are invalid", this);
		}
	}

	public String getType(String number) {
		if (!TextUtils.isBlank(number)) {
			if (TextUtils.hasAnyPrefix(number, PREFIXES_AMERICAN_EXPRESS)) {
				return AMERICAN_EXPRESS;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_DISCOVER)) {
				return DISCOVER;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_JCB)) {
				return JCB;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_DINERS_CLUB)) {
				return DINERS_CLUB;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_VISA)) {
				return VISA;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_MASTERCARD)) {
				return MASTERCARD;
			} else {
				return UNKNOWN;
			}
		}
		return UNKNOWN;

	}

	private void addCard(String stripeToken, String lastFour) {
		// AppLog.Log(TAG, "Final token : " + peachToken.substring(3));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.ADD_CARD);
		map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(this).getSessionToken());
		map.put(Const.Params.STRIPE_TOKEN, stripeToken);
		map.put(Const.Params.LAST_FOUR, lastFour);
		// map.put(Const.Params.CARD_TYPE, type);
		// new HttpRequester(this, map, Const.ServiceCode.ADD_CARD, this);
		requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				Const.ServiceCode.ADD_CARD, this, this));
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		AppLog.Log(Const.TAG, response);
		AndyUtils.removeCustomProgressDialog();
		super.onTaskCompleted(response, serviceCode);
		switch (serviceCode) {
		case Const.ServiceCode.ADD_CARD:

			if (new ParseContent(this).isSuccess(response)) {
				AndyUtils.showToast(getString(R.string.text_add_card_scucess),
						this);
				setResult(Activity.RESULT_OK);
			} else {
				AndyUtils.showToast(
						getString(R.string.text_not_add_card_unscucess), this);
				setResult(Activity.RESULT_CANCELED);
			}
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void setStatusText(final String string) {
		AppLog.Log(TAG, string);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		AppLog.Log(Const.TAG, error.getMessage());

	}

	// @Override
	// public void obtainedToken(String token, PWTransaction transaction) {
	// setStatusText("Obtained a token!");
	// setStatusText(token);
	// String lastFour = etCreditCardNum.getText().toString();
	// if (lastFour.length() > 4) {
	// AppLog.Log(TAG, "lastFour : " + lastFour);
	// lastFour = lastFour.substring(lastFour.length() - 4);
	// addCard(token, lastFour);
	// Log.i(Const.APPLICATIONIDENTIFIER, token);
	// }
	// }
	//
	// @Override
	// public void creationAndRegistrationFailed(PWTransaction transaction,
	// PWError error) {
	// setStatusText("Error contacting the gateway.");
	// Log.e("com.payworks.customtokenization.TokenizationActivity",
	// error.getErrorMessage());
	// }
	//
	// @Override
	// public void creationAndRegistrationSucceeded(PWTransaction transaction) {
	// // check if it is our registration transaction
	// // setStatusText("Processing...");
	// // if (currentTokenization) {
	// // // execute it
	// // try {
	// // _binder.obtainToken(transaction);
	// // } catch (PWException e) {
	// // setStatusText("Invalid Transaction.");
	// // e.printStackTrace();
	// // }
	// // } else {
	// // // execute it
	// // try {
	// // _binder.debitTransaction(transaction);
	// // } catch (PWException e) {
	// // setStatusText("Invalid Transaction.");
	// // e.printStackTrace();
	// // }
	// // }
	// }
	//
	// @Override
	// public void transactionFailed(PWTransaction arg0, PWError error) {
	// setStatusText("Error contacting the gateway.");
	// Log.e("com.payworks.customtokenization.TokenizationActivity",
	// error.getErrorMessage());
	// }
	//
	// @Override
	// public void transactionSucceeded(PWTransaction transaction) {
	// // if (!currentTokenization) { // our debit succeeded
	// // setStatusText("Charged token "
	// // + transaction.getPaymentParams().getAmount() + " EURO!");
	// // }
	// }
}
