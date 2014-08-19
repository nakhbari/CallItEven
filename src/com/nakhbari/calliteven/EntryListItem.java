package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryListItem {
	
	private String title;
	private int price;
	private Date date;
	private DateFormat dateFormat;

	public EntryListItem() {
		this.title = "default";
		this.price = 0;
		this.date = new Date();
		this.dateFormat = SimpleDateFormat.getDateInstance();
	}

	public String getTitle() {
		return title;
	}

	public int getPrice() {
		return price;
	}

	public Date getDate() {
		return date;
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
