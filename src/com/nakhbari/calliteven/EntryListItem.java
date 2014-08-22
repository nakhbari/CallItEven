package com.nakhbari.calliteven;

import java.util.Calendar;

public class EntryListItem {

	private String title;
	private long price;
	private Calendar calendar;

	public EntryListItem() {
		this.title = "default";
		this.price = 0;
		this.calendar = Calendar.getInstance();
	}

	public String getTitle() {
		return title;
	}

	public long getPrice() {
		return price;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public void setCalendar(Calendar cal) {
		this.calendar = cal;
	}
}
