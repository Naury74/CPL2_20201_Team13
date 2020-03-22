package com.example.kioskmainpage.Activity.Service_foreground;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public class PersistentService extends Service implements Runnable {

    private static final String TAG = "PersistentService";

    // 서비스 종료시 재부팅 딜레이 시간, activity의 활성 시간을 벌어야 한다.
    private static final int REBOOT_DELAY_TIMER = 10 * 1000;

    private Handler mHandler;
    private boolean mIsRunning;
    private int mStartId = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        unregisterRestartAlarm();
    }

    @Override
    public void run() {
        mIsRunning = true;
    }

    @Override
    public void onDestroy() {

        // 서비스가 죽었을때 알람 등록
        registerRestartAlarm();

        super.onDestroy();

        mIsRunning = false;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Service#onStart(android.content.Intent, int)
     *
     * 서비스가 시작되었을때 run()이 실행되기까지 delay를 handler를 통해서 주고 있다.
     */
    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);

        mStartId = startId;

        // 5분후에 시작
        mHandler = new Handler();
        mIsRunning = true;

    }

    private void registerRestartAlarm() {

        Intent intent = new Intent(PersistentService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += REBOOT_DELAY_TIMER; // 10초 후에 알람이벤트 발생

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,REBOOT_DELAY_TIMER, sender);
    }


    /*
     기존 등록되어있는 알람을 해제한다.
     */
    private void unregisterRestartAlarm() {

        Intent intent = new Intent(PersistentService.this, RestartService.class);
        intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

}
