package com.mukavi.app.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.mukavi.app.R;

public class Activity_Dashboard extends SherlockFragmentActivity implements
		OnClickListener {
	DrawerLayout mDrawerLayout;
	RelativeLayout mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	CharSequence mTitle;
	public static String CUR_PAGE_TITLE = "Title";
	// SLIDING MENU OPTIONS
	RelativeLayout rlSettings;
	RelativeLayout rlFavorite;
	RelativeLayout rlSearch;
	RelativeLayout rlProfile;
	RelativeLayout rlHome;

	TextView labelSettings;
	TextView labelFavorite;
	TextView labelSearch;
	TextView labelProfile;
	TextView labelHome;
	String FragmentVisible;
	private RelativeLayout rlSearchBySingerName;
	private RelativeLayout rlSearchByPopularity;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dashboard);
		initMenu();

		mTitle = getResources().getString(R.string.actionbar_title);// getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(
						R.color.color_actionbar)));
		getSupportActionBar().setIcon(R.color.transparent);
		getSupportActionBar().setTitle(
				getResources().getString(R.string.actionbar_title));

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, // host Activity
				mDrawerLayout, // DrawerLayout object
				R.drawable.ic_drawer, // nav drawer image to replace 'Up' image
				R.string.drawer_open, // "open drawer" description for
										// accessibility
				R.string.drawer_close // "close drawer" description for
										// accessibility
		) {
			@SuppressWarnings("deprecation")
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				findViewById(R.id.searchDrawerData).setVisibility(View.GONE);
				findViewById(R.id.mainDrawerData).setVisibility(View.VISIBLE);
				getSupportActionBar().setTitle(
						getResources().getString(R.string.actionbar_title));
			}

			@SuppressWarnings("deprecation")
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			Fragment newContent = new FragmentSetting();
			getSupportActionBar().setTitle("Settings");
			Bundle bundle = new Bundle();
			bundle.putString(CUR_PAGE_TITLE, "Settings");
			setSelected(rlSettings);
			setSelectedTextColor(labelSettings);
			String FragmentTag = "FragmentSettings";
			newContent.setArguments(bundle);
			switchFragment(newContent, FragmentTag);
		}

	}

	private void switchFragment(Fragment fragment, String tag) {
		mDrawerLayout.closeDrawer(mDrawerList);
		// set name of Visible fragment
		FragmentVisible = tag;
		System.out.println("FragmentVisible=" + tag);
		// invalidate option menu
		invalidateOptionsMenu();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment, tag).addToBackStack(tag)
				.commit();
	}

	private void initMenu() {

		rlSettings = (RelativeLayout) findViewById(R.id.rlSetting);
		rlFavorite = (RelativeLayout) findViewById(R.id.rlFav);
		rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
		rlProfile = (RelativeLayout) findViewById(R.id.rlProfile);
		rlHome = (RelativeLayout) findViewById(R.id.rlHome);
		rlSearchBySingerName = (RelativeLayout) findViewById(R.id.rlSearchBySingerName);
		rlSearchByPopularity = (RelativeLayout) findViewById(R.id.rlSearchByPopularity);
		// find id and set font style
		Typeface font = Typeface.createFromAsset(getAssets(),
				"Roboto-Regular.ttf");
		labelSettings = (TextView) findViewById(R.id.labelSetting);
		labelSettings.setTypeface(font);
		labelFavorite = (TextView) findViewById(R.id.labelFav);
		labelFavorite.setTypeface(font);
		labelSearch = (TextView) findViewById(R.id.labelSearch);
		labelSearch.setTypeface(font);
		labelProfile = (TextView) findViewById(R.id.labelProfile);
		labelProfile.setTypeface(font);
		labelHome = (TextView) findViewById(R.id.labelHome);
		labelHome.setTypeface(font);

		// set click listener
		rlSettings.setOnClickListener(this);
		rlFavorite.setOnClickListener(this);
		rlSearch.setOnClickListener(this);
		rlProfile.setOnClickListener(this);
		rlHome.setOnClickListener(this);
		rlSearchBySingerName.setOnClickListener(this);
		rlSearchByPopularity.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {

		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		// update the main content by replacing fragments
		Fragment newContent = null;
		Bundle bundle = new Bundle();
		String FragmentTag = "";
		switch (v.getId()) {
		case R.id.rlHome:
			newContent = new FragmentDashboard();
			getSupportActionBar().setTitle("Mukavi");
			bundle.putString(CUR_PAGE_TITLE, "Mukavi");
			setSelected(rlHome);
			setSelectedTextColor(labelHome);
			FragmentTag = "FragmentDashboard";
			break;
		case R.id.rlSetting:
			newContent = new FragmentSetting();
			getSupportActionBar().setTitle("Settings");
			bundle.putString(CUR_PAGE_TITLE, "Settings");
			setSelected(rlSettings);
			setSelectedTextColor(labelSettings);
			FragmentTag = "FragmentSettings";
			break;
		case R.id.rlFav:
			newContent = new FragmentFavorite();
			getSupportActionBar().setTitle("Favorite");
			bundle.putString(CUR_PAGE_TITLE, "Favorite");
			setSelected(rlFavorite);
			setSelectedTextColor(labelFavorite);
			FragmentTag = "FragmentFavorite";
			break;
		case R.id.rlSearch:
			getSupportActionBar().setTitle("Search");
			findViewById(R.id.searchDrawerData).setVisibility(View.VISIBLE);
			findViewById(R.id.mainDrawerData).setVisibility(View.GONE);
			/*
			 * newContent = new FragmentSearch();
			 * getSupportActionBar().setTitle("Search");
			 * bundle.putString(CUR_PAGE_TITLE, "Search");
			 * setSelected(rlSearch); setSelectedTextColor(labelSearch);
			 * FragmentTag = "FragmentSearch";
			 */
			break;
		case R.id.rlProfile:
			newContent = new FragmentProfile();
			getSupportActionBar().setTitle("Profile");
			bundle.putString(CUR_PAGE_TITLE, "Profile");
			setSelected(rlProfile);
			setSelectedTextColor(labelProfile);
			FragmentTag = "FragmentProfile";
			break;
		case R.id.rlSearchBySingerName:
			startActivity(new Intent(Activity_Dashboard.this,
					ActivityPlayList.class).putExtra("Title", "Results Found"));
			break;
		case R.id.rlSearchByPopularity:
			startActivity(new Intent(Activity_Dashboard.this,
					ActivityPlayList.class).putExtra("Title", "Results Found"));
			break;
		}
		if (newContent != null) {
			newContent.setArguments(bundle);
			switchFragment(newContent, FragmentTag);
		}
	}

	private void setSelectedTextColor(TextView text) {
		labelSettings.setTextColor(getResources().getColor(
				R.color.slide_menu_text_color));
		labelFavorite.setTextColor(getResources().getColor(
				R.color.slide_menu_text_color));
		labelSearch.setTextColor(getResources().getColor(
				R.color.slide_menu_text_color));
		labelProfile.setTextColor(getResources().getColor(
				R.color.slide_menu_text_color));
		labelHome.setTextColor(getResources().getColor(
				R.color.slide_menu_text_color));
		text.setTextColor(getResources().getColor(
				R.color.slide_menu_selected_text_color));
	}

	private void setSelected(RelativeLayout rl) {
		// reset all selections
		rlSettings.setSelected(false);
		rlFavorite.setSelected(false);
		rlSearch.setSelected(false);
		rlProfile.setSelected(false);
		rlHome.setSelected(false);
		rl.setSelected(true); // set current selection
	}

	// When using the ActionBarDrawerToggle, you must call it during
	// onPostCreate() and onConfigurationChanged()
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/*
	 * @Override public void onBackPressed() { FragmentManager manager =
	 * getSupportFragmentManager(); if (manager.getBackStackEntryCount() > 0) {
	 * FragmentManager.BackStackEntry first = manager .getBackStackEntryAt(0);
	 * manager.popBackStack(first.getId(),
	 * FragmentManager.POP_BACK_STACK_INCLUSIVE); } super.onBackPressed(); }
	 */
}
