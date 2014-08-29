package com.nakhbari.calliteven;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EntryListFragment extends ListFragment {
	EntryListCommunicator activityCommunicator;
	private ArrayList<EntryListItem> m_entries = new ArrayList<EntryListItem>();
	private int m_namePosition = 0;
	private String m_name = "", m_balance = "", m_Owing = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_entry_list, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Setting the custom adapter which will handle the inflation of the
		// individual rows.
		EntryListAdapter adapter = new EntryListAdapter(getActivity(),
				R.layout.row_entry_list, m_entries);
		setListAdapter(adapter);

	}

	/** ----------------------- Action Bar ------------------------- */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Notify the fragment that there is an option menu to inflate
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// This function will inflate the actionbar icons
		inflater.inflate(R.menu.actionbar_entry_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This function will catch when the actionbar button have been clicked
		switch (item.getItemId()) {

		case R.id.addEntryItem:
			activityCommunicator.AddNewListEntryClicked(m_namePosition);
			break;
		case android.R.id.home:
			activityCommunicator.NavigateBackToHome();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		super.onPause();
	}

	/** ----------------------- Activity Functions ----------------- */
	public void SetData(int position, ArrayList<EntryListItem> array) {
		m_namePosition = position;
		m_entries.clear();
		m_entries.addAll(array);

		if (getListAdapter() != null) {

			((EntryListAdapter) getListAdapter()).notifyDataSetChanged();
		}

	}

	/** ----------------------- Activity Interface ----------------- */

	@Override
	public void onAttach(Activity activity) {
		// Attach the interface to the activity
		super.onAttach(activity);
		try {
			activityCommunicator = (EntryListCommunicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement EntryListCommunicator");
		}
	}

	public interface EntryListCommunicator {
		public void AddNewListEntryClicked(int namePosition);

		public void NavigateBackToHome();
	}
}
