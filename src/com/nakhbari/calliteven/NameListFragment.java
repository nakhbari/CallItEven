package com.nakhbari.calliteven;

import java.util.ArrayList;

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
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

public class NameListFragment extends ListFragment {

	private ArrayList<NameListItem> m_nameEntry = new ArrayList<NameListItem>();
	NameListCommunicator activityCommunicator;
	private NameListAdapter m_Adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_name_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// Setting the custom adapter which will handle the inflation of the
		// individual rows.
		m_Adapter = new NameListAdapter(getActivity(), R.layout.row_name_list,
				m_nameEntry);
		setListAdapter(m_Adapter);

		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
			private int nr = 0;
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
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
					activityCommunicator.RemoveCheckedNameListItems(getListView());
					m_Adapter.clearSelection();
					mode.finish();
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

			}
		});


	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		activityCommunicator.NameListItemClicked(position);
	}

	/** ------------------------ Action Bar ------------------------------------ */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Notify the fragment that there is an option menu to inflate
		setHasOptionsMenu(true);

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// This function will inflate the actionbar icons
		inflater.inflate(R.menu.actionbar_name_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This function will catch when the actionbar button have been clicked

		switch (item.getItemId()) {
		case R.id.addNameItem:
			activityCommunicator.AddNewNameEntryClicked();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** ----------------------- Activity Functions --------------------------- */

	public void SetNameListFragment(ArrayList<NameListItem> array) {
		m_nameEntry.clear();
		m_nameEntry.addAll(array);
		if (m_nameEntry.size() != 0) {

			m_Adapter.notifyDataSetChanged();
		}
	}

	/** ----------------------- Activity Interface --------------------------- */

	@Override
	public void onAttach(Activity activity) {
		// Attach the interface to the activity
		super.onAttach(activity);
		try {
			activityCommunicator = (NameListCommunicator) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement NameListCommunicator");
		}
	}

	public interface NameListCommunicator {
		public void AddNewNameEntryClicked();

		public void NameListItemClicked(int position);
		public void RemoveCheckedNameListItems(ListView list);
	}

	/** --------------- Contextual Action Menu Interface ------------------ */

}
