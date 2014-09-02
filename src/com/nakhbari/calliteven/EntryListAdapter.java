package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryListAdapter extends ArrayAdapter<EntryListItem> {

	// List of names
	private ArrayList<EntryListItem> m_rowEntries;
	private String[] arrayWhoPaid;
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

	public void setNewSelection(int position, boolean value) {
		mSelection.put(position, value);
		notifyDataSetChanged();
	}

	public boolean isPositionChecked(int position) {
		Boolean result = mSelection.get(position);
		return result == null ? false : result;
	}

	public int getCurrentCheckedPosition() {
		Set<Integer> i = mSelection.keySet();
		if (!i.isEmpty()) {
			return i.iterator().next();
		}

		return -1;
	}

	public void removeSelection(int position) {
		mSelection.remove(position);
		notifyDataSetChanged();
	}

	public void clearSelection() {
		mSelection = new HashMap<Integer, Boolean>();
		notifyDataSetChanged();
	}

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public EntryListAdapter(Context context, int textViewResourceId,
			ArrayList<EntryListItem> rowEntries) {
		super(context, textViewResourceId, rowEntries);
		arrayWhoPaid = context.getResources().getStringArray(
				R.array.who_paid_array);
		this.m_rowEntries = rowEntries;
	}

	static class ViewHolder {
		TextView title;
		TextView price;
		TextView date;
		TextView whoPaid;
		int lastPosition;
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = new ViewHolder();

		// assign the view we are converting to a local variable
		View view = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.row_entry_list, null);

			holder.title = (TextView) view.findViewById(R.id.entryTitle);
			holder.price = (TextView) view.findViewById(R.id.entryPrice);
			holder.date = (TextView) view.findViewById(R.id.entryDate);
			holder.whoPaid = (TextView) view.findViewById(R.id.tvWhoPaidEntry);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// Sliding in animation
		if (position != holder.lastPosition) {
			Animation animation = AnimationUtils
					.loadAnimation(
							getContext(),
							(position > holder.lastPosition) ? R.animator.up_from_bottom
									: R.animator.down_from_top);
			view.startAnimation(animation);
			holder.lastPosition = position;
		}

		if (mSelection.get(position) != null) {
			view.setBackgroundResource(R.color.list_item_long_press); // this is
																		// a
																		// selected
																		// position
																		// so
																		// make
																		// it
																		// dark
																		// bluew
		} else {
			view.setBackgroundResource(R.drawable.list_background_normal);
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

			DateFormat dateFormat = DateFormat.getDateInstance();

			// Check to see if each individual name is null
			if (holder.title != null) {
				holder.title.setText(entryItem.getTitle());
			}

			if (holder.price != null) {
				if (entryItem.getPrice() >= 0) {
					holder.whoPaid.setText(arrayWhoPaid[0]);
				} else {

					holder.whoPaid.setText(arrayWhoPaid[1]);
				}

				holder.price.setText("$"
						+ Long.toString(Math.abs(entryItem.getPrice())));

			}

			if (holder.date != null) {
				holder.date.setText((dateFormat.format(entryItem.getCalendar()
						.getTime()).toString()));

			}

		}

		// Return the view to the activity
		return view;

	}
}
