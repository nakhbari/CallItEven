package com.nakhbari.calliteven;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Toast;

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

		// to give support on lower android version, we are not calling
		// getFragmentManager()

		if (savedInstanceState == null) {
			ft.add(R.id.idFragment, nameListFragment, "name_list_tag");
			ft.commit();
		}
	}

	/** ----------------------- NameListFragment Functions ----------------- */
	public void UpdateNameListFragment() {
		nameListFragment.SetNameListFragment(m_nameEntry);

	}

	/** ----------------------- NameListFragment Callbacks ----------------- */
	@Override
	public void AddNewNameEntryClicked() {
		Toast.makeText(getApplicationContext(), "Add new name entry click",
				Toast.LENGTH_SHORT).show();
		NameDialogFragment newDialogFragment = new NameDialogFragment();
		newDialogFragment.SetNameListItem(new NameListItem());
		newDialogFragment.show(fm, "NameDialog");
	}

	@Override
	public void NameListItemClicked(int position) {
		entryListFragment.SetData(position, m_nameEntry.get(position)
				.getEntryArray());

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.idFragment, entryListFragment);
		ft.addToBackStack(null).commit();

	}

	/** ----------------------- NameDialogFragment Callbacks ----------------- */
	@Override
	public void SendNewNameData(NameListItem item) {
		m_nameEntry.add(item);
		UpdateNameListFragment();

	}

	/** ----------------------- EntryListFragment Functions ----------------- */
	public void UpdateEntryListFragment(int namePosition) {
		entryListFragment.SetData(namePosition, m_nameEntry.get(namePosition)
				.getEntryArray());
		CalculateBalance(namePosition);

	}

	/** ----------------------- EntryListFragment Callbacks ----------------- */
	@Override
	public void AddNewListEntryClicked(int namePosition) {
		Toast.makeText(getApplicationContext(), "Add new list entry click",
				Toast.LENGTH_SHORT).show();
		EntryDialogFragment newDialogFragment = new EntryDialogFragment();
		newDialogFragment.SetEntryListItem(namePosition, new EntryListItem());
		newDialogFragment.show(fm, "EntryDialog");

	}

	@Override
	public void EntryListItemClicked(int position) {
		// TODO Auto-generated method stub

	}

	/** ----------------------- EntryDialogFragment Callbacks ----------------- */

	@Override
	public void SendNewEntryData(int position, EntryListItem item) {
		m_nameEntry.get(position).getEntryArray().add(item);
		UpdateEntryListFragment(position);

	}

	/** ----------------------------- Functions ------------------------------ */
	private void CalculateBalance(int namePosition) {
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
}
