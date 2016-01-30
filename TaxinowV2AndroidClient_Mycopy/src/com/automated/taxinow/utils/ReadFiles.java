package com.automated.taxinow.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class ReadFiles {

	public ReadFiles() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Convenient function to read from raw file
	 * 
	 * @param context
	 * @return
	 * @throws java.io.IOException
	 */
	public static String readRawFileAsString(Context context, int rawFile)
			throws java.io.IOException {
		InputStream inputStream = context.getResources().openRawResource(
				rawFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuffer result = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
		reader.close();
		return result.toString();
	}
}