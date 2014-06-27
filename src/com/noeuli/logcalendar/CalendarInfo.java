package com.noeuli.logcalendar;

public class CalendarInfo {
    private int _id;
    private String accName;
    private String accType;
    private String syncId;
    private String displayName;
    private int accessLevel;
    private boolean checked;
    
    public CalendarInfo(int id, String an, String at, String sid,
            String dn, int al) {
        init(id, an, at, sid, dn, al, false);
    }

    public CalendarInfo(int id, String an, String at, String sid,
            String dn, int al, boolean ch) {
        init(id, an, at, sid, dn, al, ch);
    }

    private void init(int id, String an, String at, String sid,
            String dn, int al, boolean ch) {
        _id = id;
        accName = an;
        accType = at;
        syncId = sid;
        displayName = dn;
        accessLevel = al;
        checked = ch;
    }
    
    public String toString() {
        return "id " + _id + " " + accName + " type " + accType
                + " syncId=" + syncId + " " + displayName
                + " accessLevel=" + accessLevel;
    }
    
    public int getId() {
        return _id;
    }
    
    public String getCalendarTitle() {
        return displayName;
    }
    
    public void setChecked(boolean ch) {
        checked = ch;
    }
    
    public boolean isChecked() {
        return checked;
    }
}
