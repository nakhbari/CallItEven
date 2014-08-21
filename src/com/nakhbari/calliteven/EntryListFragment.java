package com.nakhbari.calliteven;

import java.util.ArrayList;
import java.util.HashMap;

import com.nakhbari.calliteven.NameListFragment.NameListCommunicator;

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
import android.widget.BaseAdapter;

public class EntryListFragment extends ListFragment {
	EntryListCommunicator activityCommunicator;
	private ArrayList<EntryListItem> m_entries = new ArrayList<EntryListItem>();
	private int namePosition = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		activityCommunicator.AddNewListEntryClicked(namePosition);
		return super.onOptionsItemSelected(item);
	}

	/** ----------------------- Activity Functions ----------------- */
	public void SetData(int position, ArrayList<EntryListItem> array) {
		namePosition = position;
		m_entries.clear();
		m_entries.addAll(array);
		if (m_entries.size() != 0 && ((ArrayAdapter<EntryListItem>) getListAdapter()) != null) {

			((ArrayAdapter<EntryListItem>) getListAdapter())
					.notifyDataSetChanged();
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

		public void EntryListItemClicked(int position);
	}
}
