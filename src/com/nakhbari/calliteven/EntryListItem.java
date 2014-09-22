package com.nakhbari.calliteven;

import java.util.Calendar;

public class EntryListItem {

	private String m_Title;
	private Double m_Price;
	private Calendar m_CurrentDate;
	private Calendar m_DueDate;
	private boolean m_IsItemMonetary;
	private String m_Currency;

	public EntryListItem() {
		m_Title = "default";
		m_Price = 0.0;
		m_CurrentDate = Calendar.getInstance();
		m_DueDate = null;
		m_Currency = "$";
		m_IsItemMonetary = true;
	}

	public String getTitle() {
		return m_Title;
	}

	public Double getPrice() {
		return m_Price;
	}

	public Calendar getCurrentDate() {
		return m_CurrentDate;
	}

	public Calendar getDueDate() {
		return m_DueDate;
	}

	public void setTitle(String name) {
		this.m_Title = name;
	}

	public void setPrice(Double price) {
		this.m_Price = price;
	}

	public void setCurrentDate(Calendar cal) {
		this.m_CurrentDate = cal;
	}

	public void setDueDate(Calendar cal) {
		this.m_DueDate = cal;
	}

	public boolean isItemMonetary() {
		return m_IsItemMonetary;
	}

	public void setItemMonetary(boolean isMonetary) {
		m_IsItemMonetary = isMonetary;
	}

	public void setCurrency(String cur) {
		m_Currency = cur;
	}

	public String getCurrency() {
		return m_Currency;
	}
}
