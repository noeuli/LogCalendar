package com.noeuli.logcalendar;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class LogCalendar extends Application {
    private static final String TAG = "LogCalendar";
    public static final boolean LOGD = true;

    public static final int INVALID_ID = -1;
    
    private Toast mToast;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }
    
    public void showToast(String msg) {
        if (LOGD) Log.d(TAG, "showToast(" + msg + ")");
        
        if (mToast == null) {
            mToast = Toast.makeText(this,  msg,  Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        if (mToast != null) {
            mToast.show();
        }
    }
}
