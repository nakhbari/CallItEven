package com.nakhbari.calliteven;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.MultiChoiceModeListener;

public class EntryListFragment extends ListFragment {
	EntryListCommunicator activityCommunicator;
	private ArrayList<EntryListItem> m_entries = new ArrayList<EntryListItem>();
	private int m_namePosition = 0;
	private String m_name = "", m_balance = "", m_Owing = "";
	private EntryListAdapter m_Adapter;

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
		m_Adapter = new EntryListAdapter(getActivity(),
				R.layout.row_entry_list, m_entries);
		setListAdapter(m_Adapter);

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			private int nr = 0;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				if (nr == 1) {
					menu.findItem(R.id.item_edit).setVisible(true);
				} else {

					menu.findItem(R.id.item_edit).setVisible(false);
				}
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				m_Adapter.clearSelection();
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub

				nr = 0;
				getActivity().getMenuInflater().inflate(R.menu.contextual_menu,
						menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {

				case R.id.item_delete:
					nr = 0;
					activityCommunicator.RemoveCheckedEntryListItems(
							getListView(), m_namePosition);
					m_Adapter.clearSelection();
					mode.finish();
					break;

				case R.id.item_edit:
					int pos = m_Adapter.getCurrentCheckedPosition();
					if (pos >= 0) {

						activityCommunicator.EditEntryItem(m_namePosition, pos);
					}

					m_Adapter.clearSelection();
					mode.finish();
					break;
				}
				return false;
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					nr++;
					m_Adapter.setNewSelection(position, checked);

				} else {
					nr--;
					m_Adapter.removeSelection(position);
				}
				mode.setTitle(nr + " selected");
				mode.invalidate();
			}
		});

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

		public void EditEntryItem(int namePosition, int entryPosition);

		public void NavigateBackToHome();

		public void RemoveCheckedEntryListItems(ListView listView,
				int nameListPosition);
	}
}
