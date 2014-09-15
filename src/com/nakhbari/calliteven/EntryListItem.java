package com.nakhbari.calliteven;

import java.util.Calendar;

public class EntryListItem {

	private String title;
	private Double price;
	private Calendar calendar;
	private boolean isItemMonetary;

	public EntryListItem() {
		this.title = "default";
		this.price = 0.0;
		this.calendar = Calendar.getInstance();
		isItemMonetary = true;
	}

	public String getTitle() {
		return title;
	}

	public Double getPrice() {
		return price;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setCalendar(Calendar cal) {
		this.calendar = cal;
	}
	
	public boolean isItemMonetary(){
		return isItemMonetary;
	}
	
	public void setItemMonetary(boolean isMonetary)
	{
		isItemMonetary = isMonetary;
	}
}
