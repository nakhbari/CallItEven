package com.nakhbari.calliteven;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class EntryListDetailsFragment extends Fragment implements
		OnClickListener, OnDateSetListener {

	static class ViewHolder {
		EditText etTitle;
		EditText etPrice;
		Button bCurrentDateButton;
		Button bDueDateButton;
		RadioButton rbIsMoneyItem;
		Spinner spWhoPaid;
	}

	EntryListDetailsCommunicator activityCommunicator;
	private int m_namePosition = 0;
	private int m_entryPosition = 0;
	private EntryListItem m_entryItem;
	private ViewHolder holder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_entry_details, null);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

		initializeDialog(view);
		super.onViewCreated(view, savedInstanceState);
	}

	/** ----------------------- Functions -------------------------------------- */

	private void initializeDialog(View view) {

		holder.etTitle = (EditText) view.findViewById(R.id.entryDetailsTitle);
		holder.etPrice = (EditText) view.findViewById(R.id.entryDetailsPrice);
		holder.etPrice
				.setFilters(new InputFilter[] { new InputFilterPriceNumber(".") });
		holder.spWhoPaid = (Spinner) view
				.findViewById(R.id.entryDetailsWhoPaid);
		holder.rbIsMoneyItem = (RadioButton) view
				.findViewById(R.id.entryDetailsMonetary);

		// Find the yes and cancel buttons in the dialog
		Button yesButton = (Button) view.findViewById(R.id.entryOK);
		Button cancelButton = (Button) view.findViewById(R.id.entryCancel);
		holder.bCurrentDateButton = (Button) view
				.findViewById(R.id.entryDetailsCurrentDate);
		holder.bDueDateButton = (Button) view
				.findViewById(R.id.entryDetailsDueDate);

		// Set the edit texts within the Dialog, if the entry item is filled out
		if (m_entryItem != null && holder.etTitle != null
				&& holder.etPrice != null && holder.bCurrentDateButton != null) {

			holder.etTitle.requestFocus();
			holder.etTitle.requestFocusFromTouch();

			if (m_entryItem.getTitle() != "default") {
				holder.etTitle.setText(m_entryItem.getTitle());
				holder.etTitle.setSelection(m_entryItem.getTitle().length());
				holder.etPrice.setText(Double.toString(m_entryItem.getPrice()));
				holder.bCurrentDateButton
						.setText(GetStringFromCalendar(m_entryItem
								.getCurrentDate()));

				holder.bDueDateButton.setText(GetStringFromCalendar(m_entryItem
						.getDueDate()));

			} else {

				final Calendar calendar = Calendar.getInstance();
				holder.bCurrentDateButton
						.setText(GetStringFromCalendar(calendar));
				holder.bDueDateButton.setText("No Date");
			}

		}
		// Listen for clicks
		if (yesButton != null) {

			yesButton.setOnClickListener(this);
		}
		if (cancelButton != null) {

			cancelButton.setOnClickListener(this);
		}
		if (holder.bCurrentDateButton != null) {

			holder.bCurrentDateButton.setOnClickListener(this);
			if (holder.bDueDateButton != null) {

				holder.bDueDateButton.setOnClickListener(this);
			}
		}

	}

	// Gets called when an item is clicked
	@Override
	public void onClick(View v) {

		final Calendar calendar = Calendar.getInstance();
		final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
				this, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), false);
		datePickerDialog.setYearRange(1985, 2028);

		switch (v.getId()) {
		case R.id.entryOK:

			if (holder.etTitle.getText().toString().length() == 0) {
				Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_SHORT)
						.show();
			} else if (holder.rbIsMoneyItem.isChecked()
					&& holder.etPrice.getText().toString().length() == 0) {

				Toast.makeText(getActivity(), "Enter Price", Toast.LENGTH_SHORT)
						.show();
			} else {
				// fill in the entrylistitem with the dialog data
				m_entryItem
						.setTitle(holder.etTitle.getText().toString().trim());

				// If there is a monetary value, then set it in the entry item
				if (holder.rbIsMoneyItem.isChecked()) {
					if (holder.spWhoPaid.getSelectedItemPosition() == 0) {

						m_entryItem.setPrice(Double.parseDouble(holder.etPrice
								.getText().toString()));

					} else {
						// Then the other person paid and we need to made the
						// price negative
						m_entryItem.setPrice((-1)
								* Double.parseDouble(holder.etPrice.getText()
										.toString()));

					}
				}

				m_entryItem.setItemMonetary(holder.rbIsMoneyItem.isChecked());

				// Send data to the fragment
				activityCommunicator.SendEntryItemData(m_entryItem,
						m_namePosition, m_entryPosition);

				getActivity().getFragmentManager().popBackStack();
			}

			break;
		case R.id.entryCancel:
			getActivity().getFragmentManager().popBackStack();
			break;

		case R.id.entryDetailsDueDate:
			datePickerDialog.show(getFragmentManager(), "Due DatePicker");
			break;
		case R.id.entryDetailsCurrentDate:
			datePickerDialog.show(getFragmentManager(), "Current DatePicker");
			break;
		}

	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(year, month, day);
		if (datePickerDialog.getTag() == "Current DatePicker") {
			
			m_entryItem.setCurrentDate(cal);
			holder.bCurrentDateButton.setText(GetStringFromCalendar(cal));
		} else {
			
			m_entryItem.setDueDate(cal);
			holder.bDueDateButton.setText(GetStringFromCalendar(cal));
		}
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
		m_entryItem = item;
		this.m_namePosition = namePosition;
		this.m_entryPosition = entryPosition;
	}

	/** ----------------------- Activity Interface --------------------------- */

	@Override
	public void onAttach(Activity activity) {
		// Attach the interface to the activity
		super.onAttach(activity);
		try {
			activityCommunicator = (EntryListDetailsCommunicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EntryDialogCommunicator");
		}
	}

	public interface EntryListDetailsCommunicator {
		public void SendEntryItemData(EntryListItem item, int namePosition,
				int entryPosition);
	}

}
