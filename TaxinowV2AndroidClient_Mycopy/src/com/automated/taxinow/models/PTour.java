package com.automated.taxinow.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PTour implements Serializable {
	double pTourPrice;
	int pTourType, pTourPerson;

	public double getpTourPrice() {
		return pTourPrice;
	}

	public void setpTourPrice(double pTourPrice) {
		this.pTourPrice = pTourPrice;
	}

	public int getpTourPerson() {
		return pTourPerson;
	}

	public void setpTourPerson(int pTourPerson) {
		this.pTourPerson = pTourPerson;
	}

	public int getpTourType() {
		return pTourType;
	}

	public void setpTourType(int pTourType) {
		this.pTourType = pTourType;
	}
}