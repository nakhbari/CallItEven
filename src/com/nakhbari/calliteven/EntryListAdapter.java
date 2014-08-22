package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryListAdapter extends ArrayAdapter<EntryListItem> {

	// List of names
	private ArrayList<EntryListItem> m_rowEntries;
	private String[] arrayWhoPaid;
	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public EntryListAdapter(Context context, int textViewResourceId,
			ArrayList<EntryListItem> rowEntries) {
		super(context, textViewResourceId, rowEntries);
		arrayWhoPaid = context.getResources().getStringArray(R.array.who_paid_array);
		this.m_rowEntries = rowEntries;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View view = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_entry_list, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		EntryListItem entryItem = m_rowEntries.get(position);

		if (entryItem != null) {

			TextView title = (TextView) view.findViewById(R.id.entryTitle);
			TextView price = (TextView) view.findViewById(R.id.entryPrice);
			TextView date = (TextView) view.findViewById(R.id.entryDate);
			TextView whoPaid = (TextView) view.findViewById(R.id.tvWhoPaidEntry);
			DateFormat dateFormat = DateFormat.getDateInstance();

			// Check to see if each individual name is null
			if (title != null) {
				title.setText(entryItem.getTitle());
			}

			if (price != null) {
				if (entryItem.getPrice() >= 0)
				{
					whoPaid.setText(arrayWhoPaid[0]);
				}
				else
				{

					whoPaid.setText(arrayWhoPaid[1]);
				}
				

				price.setText("$" + Long.toString(Math.abs(entryItem.getPrice())));

			}

			if (date != null) {
				date.setText((dateFormat.format(entryItem.getCalendar()
						.getTime()).toString()));

			}

		}

		// Return the view to the activity
		return view;

	}
}
