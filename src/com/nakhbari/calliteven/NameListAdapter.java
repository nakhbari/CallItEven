package com.nakhbari.calliteven;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NameListAdapter extends ArrayAdapter<NameListItem> {

	// List of names
	private ArrayList<NameListItem> objects;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public NameListAdapter(Context context, int textViewResourceId,
			ArrayList<NameListItem> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
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
			view = inflater.inflate(R.layout.row_name_list, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		NameListItem i = objects.get(position);

		if (i != null) {

			TextView name = (TextView) view.findViewById(R.id.text1);
			TextView owesWho = (TextView) view.findViewById(R.id.text2);
			TextView balance = (TextView) view.findViewById(R.id.text3);

			// Check to see if each individual name is null
			if (name != null) {
				name.setText(i.getName());
			}

			// Manage what happens with the balance
			if (balance != null) {
				

				if (i.getBalance() < 0) {
					owesWho.setText("Is Owed: ");
					balance.setText("$" + Integer.toString(Math.abs(i.getBalance())));

				} else if (i.getBalance() > 0) {

					owesWho.setText("Owes You: ");
					balance.setText("$" + Integer.toString(Math.abs(i.getBalance())));

				} else {

					owesWho.setText(R.string.no_balance);
					balance.setText("");
				}
			}

		}

		// Return the view to the activity
		return view;

	}

}
