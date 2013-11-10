package com.cybersoftteam.app.project_so.controller;

import ws.munday.slidingmenu.SlidingMenuActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class CustomerBasedAcitivy extends SlidingMenuActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * Handle when menu button has clicked
	 * 
	 * @param v
	 *            View
	 */
	public void showMenu(View v) {

	}

	/**
	 * View fragment content
	 * 
	 * @param id
	 *            resource id
	 * @param content
	 *            Fragment content
	 */
	protected void viewContent(int id, Fragment content) {

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can navigate
		// back
		transaction.replace(id, content);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}
}