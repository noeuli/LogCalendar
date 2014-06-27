package com.noeuli.logcalendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarEvent {
    private String mTitle;
    private long mStartTime;
    private long mEndTime;
    private String mTimeZone;
    
    public CalendarEvent(String title, long start, long end, String timeZone) {
        mTitle = title;
        mStartTime = start;
        mEndTime = end;
        mTimeZone = timeZone;
    }
    
    public String getTitle() {
         return mTitle;
    }
    
    public long getStartTime() {
        return mStartTime;
    }
    
    public long getEndTime() {
        return mEndTime;
    }
    
    public String toString() {
        return "[" + mTitle + "] " + getTime(mStartTime, mTimeZone) + "~" + getTime(mEndTime, mTimeZone);
    }

    String getTime(long dateInMillis, String timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/d h:mm/z", Locale.KOREA); 
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        String dateString = formatter.format(new Date(dateInMillis));
        return dateString;
    }
}
