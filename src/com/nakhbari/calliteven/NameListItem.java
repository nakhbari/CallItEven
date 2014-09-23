package com.nakhbari.calliteven;

import java.util.ArrayList;

public class NameListItem {
	private String m_Name;
	private double m_Balance;
	private ArrayList<EntryListItem> m_EntryArray;
	private ListOfLatestEntryItems m_listOfLatestItems;

	public NameListItem() {
		m_Name = "default";
		m_Balance = 0;
		m_EntryArray = new ArrayList<EntryListItem>();
		m_listOfLatestItems = new ListOfLatestEntryItems();
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		this.m_Name = name;
	}

	public double getBalance() {
		return m_Balance;
	}

	public void setBalance(double balance) {
		this.m_Balance = balance;
	}

	public ArrayList<EntryListItem> getEntryArray() {
		return m_EntryArray;
	}

	public void setEntryArray(ArrayList<EntryListItem> array) {
		this.m_EntryArray = array;
	}
	
	public void setListOfLatestEntries(ListOfLatestEntryItems list){
		m_listOfLatestItems = list;
	}
	
	public ListOfLatestEntryItems getListOfLatestEntries(){
		return m_listOfLatestItems;
	}
}
