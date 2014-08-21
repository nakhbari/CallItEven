package com.nakhbari.calliteven;

import java.io.FileOutputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
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

		
		if (savedInstanceState == null) {
			ft.add(R.id.idFragment, nameListFragment, "name_list_tag");
			ft.commit();
		}
		

	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LoadDataStructure();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SaveDataStructure();
	}

	/** ----------------------- NameListFragment Functions ----------------- */
	private void UpdateNameListFragment() {
		nameListFragment.SetNameListFragment(m_nameEntry);

	}

	/** ----------------------- NameListFragment Callbacks ----------------- */
	@Override
	public void AddNewNameEntryClicked() {
		NameDialogFragment newDialogFragment = new NameDialogFragment();
		newDialogFragment.SetNameListItem(new NameListItem());
		newDialogFragment.show(fm, "NameDialog");
	}

	@Override
	public void NameListItemClicked(int position) {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.idFragment, entryListFragment);
		ft.addToBackStack(null).commit();
		
		entryListFragment.SetData(position, m_nameEntry.get(position)
				.getEntryArray());

	}

	/** ----------------------- NameDialogFragment Callbacks ----------------- */
	@Override
	public void SendNewNameData(NameListItem item) {
		m_nameEntry.add(item);
		UpdateNameListFragment();

	}

	/** ----------------------- EntryListFragment Functions ----------------- */
	private void UpdateEntryListFragment(int namePosition) {
		entryListFragment.SetData(namePosition, m_nameEntry.get(namePosition)
				.getEntryArray());
		CalculateBalance(namePosition);

	}

	/** ----------------------- EntryListFragment Callbacks ----------------- */
	@Override
	public void AddNewListEntryClicked(int namePosition) {
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
