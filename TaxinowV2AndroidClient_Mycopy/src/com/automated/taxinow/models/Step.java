package com.automated.taxinow.models;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Hardik A Bhalodi
 */
public class Step {
	private double startLat;
	private double startLon;
	private double endLat;
	private double endLong;
	private String html_instructions;
	private String strPoint;
	private List<LatLng> listPoints;

	public Step() {
		listPoints = new ArrayList<LatLng>();
	}

	public List<LatLng> getListPoints() {
		return listPoints;
	}

	public void setListPoints(List<LatLng> listPoints) {
		this.listPoints = listPoints;
	}

	public double getStartLat() {
		return startLat;
	}

	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}

	public double getStartLon() {
		return startLon;
	}

	public void setStartLon(double startLon) {
		this.startLon = startLon;
	}

	public double getEndLat() {
		return endLat;
	}

	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}

	public double getEndLong() {
		return endLong;
	}

	public void setEndLong(double endLong) {
		this.endLong = endLong;
	}

	public String getHtml_instructions() {
		return html_instructions;
	}

	public void setHtml_instructions(String html_instructions) {
		this.html_instructions = html_instructions;
	}

	public String getStrPoint() {
		return strPoint;
	}

	public void setStrPoint(String strPoint) {
		this.strPoint = strPoint;
	}

}
