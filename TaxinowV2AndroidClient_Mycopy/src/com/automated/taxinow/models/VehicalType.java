/**
 * 
 */
package com.automated.taxinow.models;


/**
 * @author Hardik A Bhalodi
 * 
 */
public class VehicalType {
	private int id;
	private String name;
	private String icon;
	private int isDefault;
	private double pricePerUnitTime;
	private double basePrice, pricePerUnitDistance;
	private int baseDistance;
	public boolean isSelected;
	private String minFare;
	private String maxSize;
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getBaseDistance() {
		return baseDistance;
	}

	public void setBaseDistance(int baseDistance) {
		this.baseDistance = baseDistance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public double getPricePerUnitTime() {
		return pricePerUnitTime;
	}

	public void setPricePerUnitTime(double pricePerUnitTime) {
		this.pricePerUnitTime = pricePerUnitTime;
	}

	public double getPricePerUnitDistance() {
		return pricePerUnitDistance;
	}

	public void setPricePerUnitDistance(double pricePerUnitDistance) {
		this.pricePerUnitDistance = pricePerUnitDistance;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getMinFare() {
		return minFare;
	}

	public void setMinFare(String minFare) {
		this.minFare = minFare;
	}

	public String getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}

}
