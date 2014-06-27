package com.noeuli.logcalendar;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.CalendarEntity;
import android.util.Log;

public class CalendarList {
    private static final String TAG = "CalendarList";
    private static final boolean LOGD = LogCalendar.LOGD;
    
    private static final String CALENDAR_URI = "content://com.android.calendar";
    private static final String PREFERENCES = "CalendarSettings";
    
    private Context mContext;
    private ContentResolver mResolver;
    private ArrayList<CalendarInfo> mCalendarList;
    private ArrayList<CalendarInfo> mDisplayCalendarList;
    private int mSelectedCalendarId;
    
    public CalendarList(Context ctx) {
        mContext = ctx;
        mResolver = ctx.getContentResolver();
        mCalendarList = new ArrayList<CalendarInfo>();
        mDisplayCalendarList = new ArrayList<CalendarInfo>();
        mSelectedCalendarId = LogCalendar.INVALID_ID;
        
        initCalendarList();
        loadDisplayCalendarList();
    }
    
    private void initCalendarList() {
        if (LOGD) Log.d(TAG, "initCalendarList()");
        
        Uri calendarUri = Uri.parse(CALENDAR_URI + "/calendars");
        String[] selection = new String[] {
                CalendarEntity._ID,
                CalendarEntity.ACCOUNT_NAME,
                CalendarEntity.ACCOUNT_TYPE,
                CalendarEntity._SYNC_ID,
                CalendarEntity.CALENDAR_DISPLAY_NAME,
                CalendarEntity.CALENDAR_ACCESS_LEVEL,
        };
        String[] condition = new String[] {
                "600",
                CalendarContract.ACCOUNT_TYPE_LOCAL,
        };
        Cursor c = mResolver.query(
                calendarUri, selection,
                //null, null, null);  // no condition
                "calendar_access_level>=? and account_type<>?", condition, null);

        if (c==null || !c.moveToFirst()) {
            // System does not have any calendars.
            Log.e(TAG, "No Calenders found.");
            return;
        }
        
        mCalendarList.clear();
        
        try {
            int i=0;

            if (LOGD) {
                int rows = c.getCount();
                int cols = c.getColumnCount();
                Log.w(TAG, "initCalendarId() count=" + rows + " cols=" + cols);
            }

            do {
                CalendarInfo r = new CalendarInfo(c.getInt(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getString(4), c.getInt(5));
                mCalendarList.add(r);
                
                if (LOGD) Log.d(TAG, "initCalendarId() [" + (i++) + "] record: " + r);
            } while (c.moveToNext());

        } catch (Exception e) {
            Log.e(TAG, "Error : Exception occurred on initCalendarId()." , e);
        } finally {
            c.close();
        }
    }
    
    public int size() {
        int size = 0;
        if (mCalendarList != null) {
            size = mCalendarList.size();
        }
        return size;
    }
    
    public int displaySize() {
        int size = 0;
        if (mDisplayCalendarList != null) {
            size = mDisplayCalendarList.size();
        }
        return size;
    }
    
    /* Use position instead of ID, to simplify code.
    private CalendarInfo findItem(ArrayList<CalendarInfo> list, int id) {
        if (list != null) {
            for(CalendarInfo i : mCalendarList) {
                if (i != null && i.getId() == id) {
                    return i;
                }
            }
        }
        return null;
    }
    
    public CalendarInfo getItem(int id) {
        return findItem(mCalendarList, id);
    }
    
    public CalendarInfo getDisplayItem(int id) {
        return findItem(mDisplayCalendarList, id);
    }
    */
    
    private CalendarInfo findItemAt(ArrayList<CalendarInfo> list, int position) {
        if (list != null && list.size() > position) {
            return list.get(position);
        }
        return null;
    }

    public CalendarInfo getItemAt(int position) {
        return findItemAt(mCalendarList, position);
    }
    
    public CalendarInfo getDisplayItemAt(int position) {
        return findItemAt(mDisplayCalendarList, position);
    }
    
    private CharSequence findTitle(ArrayList<CalendarInfo> list, int position) {
        if (list != null && list.size() > position) {
            CalendarInfo info = list.get(position);
            if (info != null) return info.getCalendarTitle();
        }
        return null;
    }
    
    public CharSequence getTitle(int position) {
        return findTitle(mCalendarList, position);
    }
    
    public CharSequence getDisplayTitle(int position) {
        return findTitle(mDisplayCalendarList, position);
    }
    
    public ArrayList<String> getDisplayCalendarTitleArray() {
        ArrayList<String> list = new ArrayList<String>();
        
        for (int i=0; i<mDisplayCalendarList.size(); i++) {
            CalendarInfo info = mDisplayCalendarList.get(i);
            if (info != null) list.add(info.getCalendarTitle());
        }
        
        return list;
    }
    
    public void loadDisplayCalendarList() {
        if (LOGD) Log.d(TAG, "loadDisplayCalendarList()");
        
        try {
            SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if (pref == null) return;
            
            for (int i=0; i<mCalendarList.size(); i++) {
                CalendarInfo info = mCalendarList.get(i);
                String key = String.valueOf(info.getId());
                boolean value = pref.getBoolean(key, false);
                if (LOGD) Log.d(TAG, "loadDisplayCalendarList(" + i + ") key=" + key + " value=" + value);
                info.setChecked(value);
            }

        } catch (Exception e) {
            Log.e(TAG, "loadDisplayCalendarList(): Error, ", e);
        }
    }
    
    public void saveDisplayCalendarList() {
        if (LOGD) Log.d(TAG, "saveDisplayCalendarList()");
        
        try {
            SharedPreferences pref = mContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            if (pref == null) return;
            
            SharedPreferences.Editor ed = pref.edit();
            
            for (int i=0; i<mCalendarList.size(); i++) {
                CalendarInfo info = mCalendarList.get(i);
                String key = String.valueOf(info.getId());
                boolean value = info.isChecked();
                if (LOGD) Log.d(TAG, "saveDisplayCalendarList(" + i + ") key=" + key + " value=" + value);
                ed.putBoolean(key, value);
            }

            ed.commit();
        } catch (Exception e) {
            Log.e(TAG, "saveDisplayCalendarList(): Error, ", e);
        }
    }
    public void setSelectedCalendarId(int id) {
        mSelectedCalendarId = id;
    }
    
    public int getSelectedCalendarId() {
        return mSelectedCalendarId;
    }
    
    public String getSelectedCalendarTitle() {
        return null;
    }
}
