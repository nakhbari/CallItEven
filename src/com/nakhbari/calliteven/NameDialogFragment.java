package com.nakhbari.calliteven;

import com.nakhbari.calliteven.NameListFragment.NameListCommunicator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NameDialogFragment extends DialogFragment implements
		View.OnClickListener {
	NameListItem nameListItem;
	EditText etName;
	NameDialogCommunicator activityCommunicator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_name_dialog, null);

		initializeDialog(view);

		return view;
	}

	private void initializeDialog(View view) {

		etName = (EditText) view.findViewById(R.id.dialogName);

		// Set the edit texts within the Dialog, if the Name item is filled out
		if (etName != null && !(nameListItem.getName() == "default")) {

			etName.setText(nameListItem.getName());
		}

		// Find the yes and cancel buttons in the dialog
		Button yesButton = (Button) view.findViewById(R.id.nameOK);
		Button cancelButton = (Button) view.findViewById(R.id.nameCancel);

		// Listen for clicks
		if (yesButton != null) {

			yesButton.setOnClickListener(this);
		}
		if (cancelButton != null) {

			cancelButton.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {

		// Gets called when an item is clicked
		if (v.getId() == R.id.nameOK) {

			// fill in the namelistitem with the dialog data
			nameListItem.setName(etName.getText().toString().trim());

			// Send data to the fragment
			activityCommunicator.SendNewNameData(nameListItem);

			dismiss();
		} else {

			dismiss();
		}

	}

	/** ----------------------- Activity Callbacks --------------------------- */

	public void SetNameListItem(NameListItem item) {
		nameListItem = item;
	}

	/** ----------------------- Activity Interface --------------------------- */

	@Override
	public void onAttach(Activity activity) {
		// Attach the interface to the activity
		super.onAttach(activity);
		try {
			activityCommunicator = (NameDialogCommunicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NameDialogCommunicator");
		}
	}

	public interface NameDialogCommunicator {
		public void SendNewNameData(NameListItem item);
	}
}
