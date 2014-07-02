package com.noeuli.logcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class LogCalendarSettings extends Activity implements OnClickListener {
    private static final String TAG = "LogCalendarSettings";
    private static final boolean LOGD = LogCalendar.LOGD;

    private LogCalendar mApp;
    private CalendarList mCalendarList;
    private CalendarListAdapter mCalendarListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate()");

        setContentView(R.layout.log_calendar_settings);

        mApp = (LogCalendar) getApplication();
        mCalendarList = mApp.getCalendarList();
        mCalendarListAdapter = new CalendarListAdapter(mCalendarList);

        ListView calendarListView = (ListView) findViewById(R.id.calendarListView);
        if (calendarListView != null) {
            calendarListView.setAdapter(mCalendarListAdapter);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (LOGD) Log.d(TAG, "onClick() id=" + id);

        if (id == R.id.ok) {
            mCalendarList.saveDisplayCalendarList();
            finish();
        } else if (id == R.id.cancel) {
            finish();
        }
    }

    private class CalendarListAdapter extends BaseAdapter {
        private static final String TAG = "LogCalendarSettings.CalendarListAdapter";
        private static final boolean LOGD = LogCalendar.LOGD;

        private CalendarList mCalendarList;
        private LayoutInflater mInflater;
        
        public CalendarListAdapter(CalendarList list) {
            mCalendarList = list;
            mInflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            int count = 0;
            if (mCalendarList != null) count = mCalendarList.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            CalendarInfo item = null;
            if (mCalendarList != null) item = mCalendarList.getItemAt(position);
            return item;
        }

        @Override
        public long getItemId(int position) {
            long id = LogCalendar.INVALID_ID;
            if (mCalendarList != null) id = mCalendarList.getCalendarId(position);
            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CalendarInfo item = null;
            ViewHolder holder = null;

            if (parent == null) {
                Log.w(TAG, "getView(" + position + ") Error. parent view is NULL!");
                return convertView;
            } else if (mCalendarList == null) {
                Log.w(TAG, "getView(" + position + ") Error. calendar list is NULL!");
                return null;
            }
            
            item = mCalendarList.getItemAt(position);
            if (item == null) {
                Log.w(TAG, "getView(" + position + ") Error. item is NULL!");
                return null;
            }
            
            if (convertView == null) {
                holder = new ViewHolder();
                
                convertView = mInflater.inflate(R.layout.calendar_list_item, parent, false);
                if (convertView == null) {
                    Log.w(TAG, "getView(" + position + ") item view inflate error!");
                    return null;
                }
                
                convertView.setId(position);
                holder.title = (TextView) convertView.findViewById(R.id.calendar_title);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.calendar_checkbox);
                holder.checkbox.setId(position);
                holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        int position = buttonView.getId();
                        CalendarInfo item = mCalendarList.getItemAt(position);
                        if (LOGD) {
                            Log.d(TAG, "onCheckedChanged() position=" + position + " checked=" 
                                    + isChecked + " item's value=" + item.isChecked());
                        }
                        item.setChecked(isChecked);
                    }
                });
                convertView.setOnClickListener(mClickListener);
                
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            String title = item.getCalendarTitle();
            boolean checked = item.isChecked();
            
            if (holder.title != null) {
                holder.title.setText(title);
            }
            
            if (holder.checkbox != null) {
                holder.checkbox.setChecked(checked);
            }
            
            return convertView;

        }

        private OnClickListener mClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                ViewHolder holder = (ViewHolder) v.getTag();
                
                holder.checkbox.toggle();
                boolean checked = holder.checkbox.isChecked();
                int position = v.getId();
                CalendarInfo item = null;
                if (mCalendarList != null) {
                    item = mCalendarList.getItemAt(position);
                }
                if (item != null) {
                    item.setChecked(checked);
                }
            }
        };

        private class ViewHolder {
            public TextView title;
            public CheckBox checkbox;
        }
    }
}
