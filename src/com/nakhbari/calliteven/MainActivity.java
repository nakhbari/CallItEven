package com.nakhbari.calliteven;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

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

		// Set Notification bar translucency
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);

			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.actionbar_background);

			SystemBarTintManager.SystemBarConfig config = tintManager
					.getConfig();
			findViewById(android.R.id.content).setPadding(0,
					config.getPixelInsetTop(true), config.getPixelInsetRight(),
					config.getPixelInsetBottom());
		}
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
		newDialogFragment.SetNameListItem(new NameListItem(), -1);
		newDialogFragment.show(fm, "NameDialog");
	}

	@Override
	public void NameListItemClicked(int position) {
		// a Name was clicked, so move to the detail fragment
		FragmentTransaction ft = fm.beginTransaction();

		ft.setCustomAnimations(R.animator.enter_from_right,
				R.animator.exit_to_left, R.animator.enter_from_left,
				R.animator.exit_to_right);
		ft.replace(R.id.idFragment, entryListFragment);
		ft.addToBackStack(null).commit();

		// Set the data in the detail fragment
		UpdateEntryListFragment(position, false);

		// Enable the up button on the action bar
		// so the detail fragment can navigate back
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void RemoveCheckedNameListItems(ListView listView) {
		RemoveListViewItems(listView, -1);

	}

	@Override
	public void EditNameEntry(int position) {
		// Open Dialog so a new person can be added to the list
		NameDialogFragment newDialogFragment = new NameDialogFragment();

		// Do not populate the dialog, since it is a NEW entry
		newDialogFragment.SetNameListItem(m_nameEntry.get(position), position);
		newDialogFragment.show(fm, "NameDialog");

	}

	/** ----------------------- NameDialogFragment Callbacks ----------------- */
	@Override
	public void SendNameData(NameListItem item, int position) {
		// When the dialog returns, we must add the name to
		// the structure and inform the name fragment
		if (position >= 0) {
			m_nameEntry.set(position, item);
		} else {
			m_nameEntry.add(item);

		}

		// Sort the List Alphabetically
		Collections.sort(m_nameEntry, new Comparator<NameListItem>() {
			public int compare(NameListItem item1, NameListItem item2) {
				return item1.getName().compareTo(item2.getName());
			}
		});

		UpdateNameListFragment();

	}

	/** ----------------------- EntryListFragment Functions ----------------- */
	private void UpdateEntryListFragment(int namePosition,
			boolean shouldCalculateBalance) {
		// When the dialog returns, we must add the entry to
		// the structure and inform the entry fragment
		entryListFragment.SetData(namePosition, m_nameEntry.get(namePosition)
				.getEntryArray());

		if (shouldCalculateBalance) {

			CalculateBalance(namePosition);
		}

	}

	/** ----------------------- EntryListFragment Callbacks ----------------- */
	@Override
	public void AddNewListEntryClicked(int namePosition) {
		// The Add New entry actionbar item was clicked so we need
		// to open the dialog which the user can put in the details
		// of the new entry
		EntryDialogFragment newDialogFragment = new EntryDialogFragment();
		newDialogFragment.SetEntryListItem(new EntryListItem(), namePosition,
				-1);
		newDialogFragment.show(fm, "EntryDialog");

	}

	@Override
	public void NavigateBackToHome() {
		// Pop back to the last fragment
		fm.popBackStack();
	}

	@Override
	public void RemoveCheckedEntryListItems(ListView listView,
			int nameListPosition) {
		RemoveListViewItems(listView, nameListPosition);

	}

	@Override
	public void EditEntryItem(int namePosition, int entryPosition) {
		// Open Dialog so a new entry item can be added to the list
		EntryDialogFragment newDialogFragment = new EntryDialogFragment();

		newDialogFragment.SetEntryListItem(m_nameEntry.get(namePosition)
				.getEntryArray().get(entryPosition), namePosition,
				entryPosition);
		newDialogFragment.show(fm, "EntryDialog");

	}

	/** ----------------------- EntryDialogFragment Callbacks ----------------- */

	@Override
	public void SendEntryItemData(EntryListItem item, int namePosition,
			int entryPosition) {
		// Update the Entry Fragment with new data
		if (entryPosition >= 0) {
			m_nameEntry.get(namePosition).getEntryArray()
					.set(entryPosition, item);
		} else {
			m_nameEntry.get(namePosition).getEntryArray().add(item);
		}

		// Sort the List by Dates
		Collections.sort(m_nameEntry.get(namePosition).getEntryArray(),
				new Comparator<EntryListItem>() {
					public int compare(EntryListItem item1, EntryListItem item2) {
						return (item1.getCalendar().getTime().compareTo(item2
								.getCalendar().getTime()));
					}
				});

		UpdateEntryListFragment(namePosition, true);

	}

	/** ----------------------------- Functions ------------------------------ */
	private void CalculateBalance(int namePosition) {
		// Calculate how much the person is owed, from the sum of entries
		int balanceSum = 0;

		for (int i = 0; i < m_nameEntry.get(namePosition).getEntryArray()
				.size(); i++) {
			balanceSum += m_nameEntry.get(namePosition).getEntryArray().get(i)
					.getPrice();
		}

		m_nameEntry.get(namePosition).setBalance(balanceSum);
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

	private void RemoveListViewItems(ListView listView,
			final int nameListPosition) {
		// Get array of list items that are checked
		SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
		int numCheckedItems = listView.getCheckedItemCount();

		// iterate through each checked item
		for (int i = numCheckedItems - 1; i >= 0; --i) {
			if (checkedItems.valueAt(i) == false) {
				continue;
			}

			final int position = checkedItems.keyAt(i);

			int positionOnScreen = position
					- listView.getFirstVisiblePosition();

			// check if the item is visible
			if (positionOnScreen >= 0
					&& positionOnScreen < listView.getChildCount()) {
				// if the item is visible, then animate a fadeout
				final View view = listView.getChildAt(positionOnScreen);
				view.animate().alpha(0).setDuration(500)
						.withEndAction(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								view.setAlpha(1);

								if (nameListPosition == -1) {

									m_nameEntry.remove(position);
									UpdateNameListFragment();
								} else {
									m_nameEntry.get(nameListPosition)
											.getEntryArray().remove(position);
									UpdateEntryListFragment(nameListPosition,
											true);
								}
							}

						}).start();
			} else {
				listView.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (nameListPosition == -1) {

							m_nameEntry.remove(position);
							UpdateNameListFragment();
						} else {
							m_nameEntry.get(nameListPosition).getEntryArray()
									.remove(position);
							UpdateEntryListFragment(nameListPosition, true);
						}
					}
				}, 300);
			}

		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);

	}

}
