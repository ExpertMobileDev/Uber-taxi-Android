/**
 * 
 */
package com.automated.taxinow.models;

/**
 * @author Kishan H Dhamat
 * 
 */
public class ApplicationPages {

	private int id;
	private String title, Data;
	private String icon = "";

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return Data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(String data) {
		Data = data;
	}

	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

}
