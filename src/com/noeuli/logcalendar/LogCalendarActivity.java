package com.noeuli.logcalendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
//import android.content.Context;

public class LogCalendarActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "LogCalendarActivity";
    private static final boolean LOGD = LogCalendar.LOGD;
    
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    
//    private Context mContext;
    private LogCalendar mApp;
    private CalendarList mCalendarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate()");
        
        mApp = (LogCalendar) getApplication();
//        mContext = getApplicationContext();
        mCalendarList = mApp.getCalendarList();

        setContentView(R.layout.log_calendar_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        if (mApp.getCalendarList()==null) {
            mApp.loadCalendarList();
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (LOGD) Log.d(TAG, "onNavigationDrawerItemSelected(" + position + ")");
        
        String title = (String) getTitle();
        
        if (mCalendarList != null) {
            mCalendarList.setSelectedCalendarIndex(position);
            title = (String) mCalendarList.getSelectedCalendarTitle();
            if (LOGD) Log.d(TAG, "onNavigationDrawerItemSelected(" + position + ") title=" + title);
        }
        if (title != null) updateDrawerTitle(position, title);
    }
    
    private void updateDrawerTitle(int position, String title) {
        if (LOGD) Log.d(TAG, "updateDrawerTitle(" + position + ", " + title + ")");
        
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = PlaceholderFragment.newInstance(position + 1, title);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void onSectionAttached(int number) {
        if (LOGD) Log.d(TAG, "onSectionAttached(" + number + ")");
        
        if (mCalendarList != null) {
            mCalendarList.setSelectedCalendarIndex(number - 1);
            CharSequence title = mCalendarList.getSelectedCalendarTitle();
            if (title != null) mTitle = title;
        }
    }

    public void restoreActionBar() {
        if (LOGD) Log.d(TAG, "restoreActionBar(): mTitle=" + mTitle);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (LOGD) Log.d(TAG, "onCreateOptionsMenu() isDrawerOpen=" + mNavigationDrawerFragment.isDrawerOpen() + " menu=" + menu);
        
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.log_calendar, menu);
            restoreActionBar();
            int id = LogCalendar.INVALID_ID;
            if (mCalendarList != null) id = mCalendarList.getSelectedCalendarIndex();
            updateDrawerTitle(id, (String)mTitle);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (LOGD) Log.d(TAG, "onOptionsItemSelected() item=" + item);
        
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, LogCalendarSettings.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        if (mNavigationDrawerFragment != null) {
            mNavigationDrawerFragment.refreshDrawerList();
        }
        // reset
        onNavigationDrawerItemSelected(0);
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "LogCalendarActivity.PlaceholderFragment";
        
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_TITLE = "section_title";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String title) {
            Log.i(TAG, "newInstance(" + sectionNumber + ", " + title + " ) " + instanceCount);

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        private static int instanceCount = 0;
        public PlaceholderFragment() {
            Log.i(TAG, "PlaceholderFragment Constructor. count=" + instanceCount);
            instanceCount++;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.i(TAG, "onCreateView() " + instanceCount);
            
            View rootView = inflater.inflate(R.layout.log_calendar_drawer, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            String text = Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))
                    + getArguments().getString(ARG_SECTION_TITLE);
            textView.setText(text);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.i(TAG, "onAttach() " + activity);
            
            ((LogCalendarActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
