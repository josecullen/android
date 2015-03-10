package com.example.introandroid;

import com.example.fragments.QuotesFragment;
import com.example.fragments.TitlesFragment.ListSelectionListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class FragmentsActivity extends ActionBarActivity implements
		ListSelectionListener {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;
	private QuotesFragment mDetailsFragment;

	private static final String TAG = "QuoteViewerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and quotes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.activity_fragments);
		
		// Get a reference to the QuotesFragment
		mDetailsFragment = (QuotesFragment) getFragmentManager()
				.findFragmentById(R.id.details);
	}

	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		if (mDetailsFragment.getShownIndex() != index) {

			// Tell the QuoteFragment to show the quote string at position index
			mDetailsFragment.showQuoteAtIndex(index);
		}
	}
}