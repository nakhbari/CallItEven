package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class EntryDialogFragment extends DialogFragment implements
		View.OnClickListener, OnDateSetListener {

	private static int NUM_DIGITS_BEFORE_DECIMAL = 10;
	private static int NUM_DIGITS_AFTER_DECIMAL = 2;

	EntryDialogCommunicator activityCommunicator;
	int namePosition = 0;
	int entryPosition = 0;
	EntryListItem entryItem;
	EditText etTitle;
	EditText etPrice;
	Button dateButton;
	Spinner spWhoPaid;
	CheckBox cbIsThereMonetaryValue;

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
				R.dimen.entry_list_dialog_width);
		int height = getResources().getDimensionPixelSize(
				R.dimen.entry_list_dialog_height);

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
		View view = inflater.inflate(R.layout.fragment_entry_dialog, null);

		initializeDialog(view);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);
	}

	/** ----------------------- Functions -------------------------------------- */

	private void initializeDialog(View view) {

		etTitle = (EditText) view.findViewById(R.id.dialogEntryTitle);
		etPrice = (EditText) view.findViewById(R.id.dialogEntryPrice);
		etPrice.setFilters(new InputFilter[] { new InputFilterPriceNumber(".") });
		spWhoPaid = (Spinner) view.findViewById(R.id.spWhoPaid);
		cbIsThereMonetaryValue = (CheckBox) view
				.findViewById(R.id.cbMonetaryValue);

		// Find the yes and cancel buttons in the dialog
		Button yesButton = (Button) view.findViewById(R.id.entryOK);
		Button cancelButton = (Button) view.findViewById(R.id.entryCancel);
		dateButton = (Button) view.findViewById(R.id.dialogEntryDate);

		// Set the edit texts within the Dialog, if the entry item is filled out
		if (entryItem != null && etTitle != null && etPrice != null
				&& dateButton != null) {

			etTitle.requestFocus();
			etTitle.requestFocusFromTouch();

			if (!(entryItem.getTitle() == "default")) {
				etTitle.setText(entryItem.getTitle());
				etTitle.setSelection(entryItem.getTitle().length());
				etPrice.setText(Double.toString(entryItem.getPrice()));
				dateButton.setText(GetStringFromCalendar(entryItem
						.getCalendar()));

			} else {

				final Calendar calendar = Calendar.getInstance();
				dateButton.setText(GetStringFromCalendar(calendar));
			}

		}
		// Listen for clicks
		if (yesButton != null) {

			yesButton.setOnClickListener(this);
		}
		if (cancelButton != null) {

			cancelButton.setOnClickListener(this);
		}
		if (dateButton != null) {

			dateButton.setOnClickListener(this);
		}
		if (cbIsThereMonetaryValue != null) {
			cbIsThereMonetaryValue
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								etPrice.setEnabled(true);
								etPrice.setTextColor(Color.BLACK);

								spWhoPaid.setEnabled(true);
								((TextView) spWhoPaid.getChildAt(0))
										.setTextColor(Color.BLACK);
							} else {
								etPrice.setEnabled(false);
								etPrice.setTextColor(Color.LTGRAY);

								spWhoPaid.setEnabled(false);
								((TextView) spWhoPaid.getChildAt(0))
										.setTextColor(Color.LTGRAY);
							}

						}
					});
		}

	}

	// Gets called when an item is clicked
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.entryOK:

			if (etTitle.getText().toString().length() == 0) {
				Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_SHORT)
						.show();
			} else if (cbIsThereMonetaryValue.isChecked()
					&& etPrice.getText().toString().length() == 0) {

				Toast.makeText(getActivity(), "Enter Price", Toast.LENGTH_SHORT)
						.show();
			} else {
				// fill in the entrylistitem with the dialog data
				entryItem.setTitle(etTitle.getText().toString().trim());

				// If there is a monetary value, then set it in the entry item
				if (cbIsThereMonetaryValue.isChecked()) {
					if (spWhoPaid.getSelectedItemPosition() == 0) {

						entryItem.setPrice(Double.parseDouble(etPrice.getText()
								.toString()));

					} else {
						// Then the other person paid and we need to made the
						// price negative
						entryItem.setPrice((-1)
								* Double.parseDouble(etPrice.getText()
										.toString()));
					}
				}

				entryItem.setItemMonetary(cbIsThereMonetaryValue.isChecked());

				// Send data to the fragment
				activityCommunicator.SendEntryItemData(entryItem, namePosition,
						entryPosition);

				dismiss();
			}

			break;
		case R.id.entryCancel:
			dismiss();
			break;

		case R.id.dialogEntryDate:

			final Calendar calendar = Calendar.getInstance();
			final DatePickerDialog datePickerDialog = DatePickerDialog
					.newInstance(this, calendar.get(Calendar.YEAR),
							calendar.get(Calendar.MONTH),
							calendar.get(Calendar.DAY_OF_MONTH), false);
			datePickerDialog.setYearRange(1985, 2028);
			datePickerDialog.show(getFragmentManager(), "DatePicker");
			break;
		}

	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(year, month, day);

		entryItem.setCalendar(cal);

		dateButton.setText(GetStringFromCalendar(cal));
	}

	private String GetStringFromCalendar(Calendar cal) {
		String result = "";

		DateFormat dateFormat = DateFormat.getDateInstance();

		result = dateFormat.format(cal.getTime());
		return result;
	}

	/** ----------------------- Activity Callbacks --------------------------- */

	public void SetEntryListItem(EntryListItem item, int namePosition,
			int entryPosition) {
		entryItem = item;
		this.namePosition = namePosition;
		this.entryPosition = entryPosition;
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
		public void SendEntryItemData(EntryListItem item, int namePosition,
				int entryPosition);
	}

}
