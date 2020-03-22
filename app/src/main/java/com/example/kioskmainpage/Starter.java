package com.example.kioskmainpage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.SplashActivity;

public class Starter extends BroadcastReceiver {

    //기기 부팅 시 자동 어플 실행
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if(action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
