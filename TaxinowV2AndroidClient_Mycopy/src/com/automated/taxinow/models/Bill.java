/**
 * 
 */
package com.automated.taxinow.models;

/**
 * @author Hardik A Bhalodi
 * 
 */
public class Bill {
	private String distance;
	private String time;
	private String basePrice;
	private String distanceCost;
	private String timeCost;
	private String total;
	private String isPaid;
	private String unit;
	private String promoBouns;
	private String referralBouns;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getDistanceCost() {
		return distanceCost;
	}

	public void setDistanceCost(String distanceCost) {
		this.distanceCost = distanceCost;
	}

	public String getTimeCost() {
		return timeCost;
	}

	public void setTimeCost(String timeCost) {
		this.timeCost = timeCost;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}

	public String getPromoBouns() {
		return promoBouns;
	}

	public void setPromoBouns(String promoBouns) {
		this.promoBouns = promoBouns;
	}

	public String getReferralBouns() {
		return referralBouns;
	}

	public void setReferralBouns(String referralBouns) {
		this.referralBouns = referralBouns;
	}

}
