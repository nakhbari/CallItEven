package com.nakhbari.calliteven;

import java.util.ArrayList;

public class NameListItem {
	private String name;
	private long balance;
	private ArrayList<EntryListItem> entryArray;

	public NameListItem() {
		this.name = "default";
		this.balance = 0;
		entryArray = new ArrayList<EntryListItem>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	public ArrayList<EntryListItem> getEntryArray() {
		return entryArray;
	}

	public void setEntryArray(ArrayList<EntryListItem> array) {
		this.entryArray = array;
	}
}
