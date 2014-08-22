package com.nakhbari.calliteven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.nakhbari.calliteven.R.anim;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity implements
		NameListFragment.NameListCommunicator,
		NameDialogFragment.NameDialogCommunicator,
		EntryListFragment.EntryListCommunicator,
		EntryDialogFragment.EntryDialogCommunicator {

	private ArrayList<NameListItem> m_nameEntry = new ArrayList<NameListItem>();

	private FragmentManager fm = getSupportFragmentManager();
	private FragmentTransaction ft = fm.beginTransaction();
	private NameListFragment nameListFragment = new NameListFragment();
	private EntryListFragment entryListFragment = new EntryListFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			ft.add(R.id.idFragment, nameListFragment, "name_list_tag");
			ft.commit();
		}

		// Turn off the up button initially
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

	}

	@Override
	protected void onStart() {
		// Load the saved data on application start
		super.onStart();
		LoadDataStructure();
	}

	@Override
	protected void onStop() {
		// Save the data when the app Stops (before closing)
		super.onStop();
		SaveDataStructure();
	}

	/** ----------------------- NameListFragment Functions ----------------- */
	private void UpdateNameListFragment() {
		// Update the Name Fragment with the newly change name list
		nameListFragment.SetNameListFragment(m_nameEntry);

	}

	/** ----------------------- NameListFragment Callbacks ----------------- */
	@Override
	public void AddNewNameEntryClicked() {
		// Open Dialog so a new person can be added to the list
		NameDialogFragment newDialogFragment = new NameDialogFragment();

		// Do not populate the dialog, since it is a NEW entry
		newDialogFragment.SetNameListItem(new NameListItem());
		newDialogFragment.show(fm, "NameDialog");
	}

	@Override
	public void NameListItemClicked(int position) {
		// a Name was clicked, so move to the detail fragment
		FragmentTransaction ft = fm.beginTransaction();

		ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out,
				R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
		ft.replace(R.id.idFragment, entryListFragment);
		ft.addToBackStack(null).commit();

		// Set the data in the detail fragment
		entryListFragment.SetData(position, m_nameEntry.get(position)
				.getEntryArray());

		// Enable the up button on the action bar
		// so the detail fragment can navigate back
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	/** ----------------------- NameDialogFragment Callbacks ----------------- */
	@Override
	public void SendNewNameData(NameListItem item) {
		// When the dialog returns, we must add the name to
		// the structure and inform the name fragment
		m_nameEntry.add(item);

		// Sort the List Alphabetically
		Collections.sort(m_nameEntry, new Comparator<NameListItem>() {
			public int compare(NameListItem item1, NameListItem item2) {
				return item1.getName().compareTo(item2.getName());
			}
		});

		UpdateNameListFragment();

	}

	/** ----------------------- EntryListFragment Functions ----------------- */
	private void UpdateEntryListFragment(int namePosition) {
		// When the dialog returns, we must add the entry to
		// the structure and inform the entry fragment
		entryListFragment.SetData(namePosition, m_nameEntry.get(namePosition)
				.getEntryArray());
		CalculateBalance(namePosition);

	}

	/** ----------------------- EntryListFragment Callbacks ----------------- */
	@Override
	public void AddNewListEntryClicked(int namePosition) {
		// The Add New entry actionbar item was clicked so we need
		// to open the dialog which the user can put in the details
		// of the new entry
		EntryDialogFragment newDialogFragment = new EntryDialogFragment();
		newDialogFragment.SetEntryListItem(namePosition, new EntryListItem());
		newDialogFragment.show(fm, "EntryDialog");

	}

	@Override
	public void NavigateBackToHome() {
		// Pop back to the last fragment
		fm.popBackStack();
	}

	/** ----------------------- EntryDialogFragment Callbacks ----------------- */

	@Override
	public void SendNewEntryData(int position, EntryListItem item) {
		// Update the Entry Fragment with new data
		m_nameEntry.get(position).getEntryArray().add(item);

		// Sort the List by Dates
		Collections.sort(m_nameEntry.get(position).getEntryArray(),
				new Comparator<EntryListItem>() {
					public int compare(EntryListItem item1, EntryListItem item2) {
						return (item1.getCalendar().getTime().compareTo(item2
								.getCalendar().getTime()));
					}
				});

		UpdateEntryListFragment(position);

	}

	/** ----------------------------- Functions ------------------------------ */
	private void CalculateBalance(int namePosition) {
		// Calculate how much the person is owed, from the sum of entries
		if (m_nameEntry.get(namePosition).getEntryArray().size() != 0) {
			int balanceSum = 0;

			for (int i = 0; i < m_nameEntry.get(namePosition).getEntryArray()
					.size(); i++) {
				balanceSum += m_nameEntry.get(namePosition).getEntryArray()
						.get(i).getPrice();
			}

			m_nameEntry.get(namePosition).setBalance(balanceSum);
		}

	}

	private void SaveDataStructure() {
		InternalDataManager dataManager = new InternalDataManager();
		dataManager.SaveData(m_nameEntry, this);

	}

	private void LoadDataStructure() {
		InternalDataManager dataManager = new InternalDataManager();
		m_nameEntry.clear();
		m_nameEntry.addAll(dataManager.LoadData(this));
		UpdateNameListFragment();

	}

}
