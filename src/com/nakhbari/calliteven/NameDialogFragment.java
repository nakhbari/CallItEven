package com.nakhbari.calliteven;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NameDialogFragment extends DialogFragment implements
		View.OnClickListener {
	NameListItem nameListItem;
	SearchView svName;
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

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		activityCommunicator.InitSearchView((SearchView) view
				.findViewById(R.id.dialogSearch));
	}

	private void initializeDialog(View view) {

		svName = (SearchView) view.findViewById(R.id.dialogSearch);

		// Set the edit texts within the Dialog, if the Name item is filled out
		if (svName != null) {

			if (!(nameListItem.getName() == "default")) {

				svName.setQuery(nameListItem.getName(), false);
				// tvName.setSelection(nameListItem.getName().length());
			}
			svName.setIconified(false);
			svName.requestFocus();
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
			if (svName.getQuery().toString().length() > 0) {
				// fill in the namelistitem with the dialog data
				nameListItem.setName(svName.getQuery().toString().trim());

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

	public void SetSearchQuery(String str) {
		if (svName != null) {

			svName.setQuery(str, false);
			svName.clearFocus();
		}
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

		public void InitSearchView(SearchView searchView);
	}
}
