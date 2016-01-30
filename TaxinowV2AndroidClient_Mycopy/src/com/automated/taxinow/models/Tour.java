package com.automated.taxinow.models;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Tour implements Serializable {
	int tourId;
	String tourName, tourImage, tourDesc, fullDayTour, halfDayTour,
			morningDTime, morningRTime, afterDTime, afterRTime, tourNote,
			fDayDTime, fDayRTime;
	double fullDayPrice, halfDayPrice;
	ArrayList<String> imgLst = new ArrayList<String>();
	ArrayList<PTour> pTour = new ArrayList<PTour>();

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}

	public String getTourImage() {
		return tourImage;
	}

	public void setTourImage(String toueImage) {
		this.tourImage = toueImage;
	}

	public String getTourDesc() {
		return tourDesc;
	}

	public void setTourDesc(String tourDesc) {
		this.tourDesc = tourDesc;
	}

	public String getFullDayTour() {
		return fullDayTour;
	}

	public void setFullDayTour(String fullDayTour) {
		this.fullDayTour = fullDayTour;
	}

	public String getHalfDayTour() {
		return halfDayTour;
	}

	public void setHalfDayTour(String halfDayTour) {
		this.halfDayTour = halfDayTour;
	}

	public String getMorningDTime() {
		return morningDTime;
	}

	public void setMorningDTime(String morningDTime) {
		this.morningDTime = morningDTime;
	}

	public String getMorningRTime() {
		return morningRTime;
	}

	public void setMorningRTime(String morningRTime) {
		this.morningRTime = morningRTime;
	}

	public String getAfterDTime() {
		return afterDTime;
	}

	public void setAfterDTime(String afterDTime) {
		this.afterDTime = afterDTime;
	}

	public String getAfterRTime() {
		return afterRTime;
	}

	public void setAfterRTime(String afterRTime) {
		this.afterRTime = afterRTime;
	}

	public double getFullDayPrice() {
		return fullDayPrice;
	}

	public void setFullDayPrice(double fullDayPrice) {
		this.fullDayPrice = fullDayPrice;
	}

	public double getHalfDayPrice() {
		return halfDayPrice;
	}

	public void setHalfDayPrice(double halfDayPrice) {
		this.halfDayPrice = halfDayPrice;
	}

	public String getTourNote() {
		return tourNote;
	}

	public void setTourNote(String tourNote) {
		this.tourNote = tourNote;
	}

	public ArrayList<String> getImgLst() {
		return imgLst;
	}

	public void setImgLst(ArrayList<String> imgLst) {
		this.imgLst = imgLst;
	}

	public ArrayList<PTour> getpTour() {
		return pTour;
	}

	public void setpTour(ArrayList<PTour> pTour) {
		this.pTour = pTour;
	}

	public String getfDayDTime() {
		return fDayDTime;
	}

	public void setfDayDTime(String fDayDTime) {
		this.fDayDTime = fDayDTime;
	}

	public String getfDayRTime() {
		return fDayRTime;
	}

	public void setfDayRTime(String fDayRTime) {
		this.fDayRTime = fDayRTime;
	}

}