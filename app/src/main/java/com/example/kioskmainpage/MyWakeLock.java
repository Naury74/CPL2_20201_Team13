package com.example.kioskmainpage;

import android.content.Context;
import android.os.PowerManager;

public class MyWakeLock {
    private static final String TAG = "myapp:AlarmWakeLock";

    private static PowerManager.WakeLock mWakeLock;

    public static void wakeLock(Context context) {

        if(mWakeLock != null) {
            return;
        }
        System.out.println("끄기");
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG );
        mWakeLock.acquire();
        System.out.println("끄기2");
    }

    public static void releaseWakeLock(Context context) {

        if(mWakeLock != null) {

            mWakeLock.release();
           // mWakeLock = null;
        }
    }
}
