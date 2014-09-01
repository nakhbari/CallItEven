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
import android.widget.Toast;

public class NameDialogFragment extends DialogFragment implements
		View.OnClickListener {
	NameListItem nameListItem;
	EditText etName;
	NameDialogCommunicator activityCommunicator;
	int namePos = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, DialogFragment.STYLE_NORMAL);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		int width = getResources().getDimensionPixelSize(
				R.dimen.name_list_dialog_width);
		int height = getResources().getDimensionPixelSize(
				R.dimen.name_list_dialog_height);

		getDialog().getWindow().setLayout(width, height);
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}

		// set the animations to use on showing and hiding the dialog
		getDialog().getWindow().setWindowAnimations(
				R.style.dialog_animation_fade);
	}

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
			etName.setSelection(nameListItem.getName().length());
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
			if (etName.getText().toString().length() > 0) {
				// fill in the namelistitem with the dialog data
				nameListItem.setName(etName.getText().toString().trim());

				// Send data to the fragment
				activityCommunicator.SendNameData(nameListItem, namePos);

				dismiss();
			} else {
				Toast.makeText(getActivity(), "Please enter a Name",
						Toast.LENGTH_SHORT).show();
			}
		} else {

			dismiss();
		}

	}

	/** ----------------------- Activity Callbacks --------------------------- */

	public void SetNameListItem(NameListItem item, int position) {
		nameListItem = item;
		namePos = position;
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
		public void SendNameData(NameListItem item, int position);
	}
}
