package com.automated.taxinow.parse;

/**
 * @author Hardik A Bhalodi
 */
public interface AsyncTaskCompleteListener {
	void onTaskCompleted(String response, int serviceCode);
}
