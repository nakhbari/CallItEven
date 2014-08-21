package com.nakhbari.calliteven;

import java.util.Date;

public class EntryListItem {
	
	private String title;
	private int price;
	private Date date;

	public EntryListItem() {
		this.title = "default";
		this.price = 0;
		this.date = new Date();
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
