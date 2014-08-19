package com.nakhbari.calliteven;

import com.nakhbari.calliteven.NameDialogFragment.NameDialogCommunicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EntryDialogFragment extends DialogFragment implements
		View.OnClickListener {
	
	EntryDialogCommunicator activityCommunicator;
	int namePosition = 0;
	EntryListItem entryItem;
	EditText etTitle;
	EditText etPrice;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_entry_dialog, null);

		initializeDialog(view);

		return view;
	}

	private void initializeDialog(View view) {

		etTitle = (EditText) view.findViewById(R.id.dialogEntryTitle);
		etPrice = (EditText) view.findViewById(R.id.dialogEntryPrice);
		// EditText etDate = (EditText) view.findViewById(R.id.dialogEntryDate);

		// Set the edit texts within the Dialog, if the entry item is filled out
		if (entryItem != null && etTitle != null && etPrice != null
				&& !(entryItem.getTitle() == "default")
				&& !(entryItem.getPrice() == 0)) {
			etTitle.setText(entryItem.getTitle());
			etPrice.setText(Integer.toString(entryItem.getPrice()));
		}

		// Find the yes and cancel buttons in the dialog
		Button yesButton = (Button) view.findViewById(R.id.entryOK);
		Button cancelButton = (Button) view.findViewById(R.id.entryCancel);

		// Listen for clicks
		if (yesButton != null) {

			yesButton.setOnClickListener(this);
		}
		if (cancelButton != null) {

			cancelButton.setOnClickListener(this);
		}

	}

	// Gets called when an item is clicked
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.entryOK) {

			// fill in the entrylistitem with the dialog data
			entryItem.setTitle(etTitle.getText().toString().trim());
			entryItem.setPrice(Integer.parseInt(etPrice.getText().toString()));

			// TODO: Manage the Date here

			// Send data to the fragment
			activityCommunicator.SendNewEntryData(namePosition, entryItem);

			dismiss();
		} else {

			dismiss();
		}

	}

	/** ----------------------- Activity Callbacks --------------------------- */

	public void SetEntryListItem(int position, EntryListItem item) {
		entryItem = item;
		namePosition = position;
	}

	/** ----------------------- Activity Interface --------------------------- */

	@Override
	public void onAttach(Activity activity) {
		// Attach the interface to the activity
		super.onAttach(activity);
		try {
			activityCommunicator = (EntryDialogCommunicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EntryDialogCommunicator");
		}
	}

	public interface EntryDialogCommunicator {
		public void SendNewEntryData(int position, EntryListItem item);
	}
}
