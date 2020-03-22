package com.example.kioskmainpage.Utilities;

import android.app.ActivityManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class TaskKiller {
    ActivityManager activitymanager;
    UsageStatsManager usagestatsmanager;
    List<ActivityManager.AppTask> appList;
    List<ActivityManager.RunningAppProcessInfo> appList2;
    public TaskKiller(ActivityManager activitymanager,UsageStatsManager usagestatsmanager){
        this.activitymanager = activitymanager;
        this.usagestatsmanager=usagestatsmanager;
    }
    public void processList(){
        /* 실행중인 process 목록 보기*/
        appList = activitymanager.getAppTasks();
        appList2 = activitymanager.getRunningAppProcesses();
        Log.d("run Process","Package Name : " + appList.size());
        Log.d("RunningAppProcessInfo","Package Name : " + appList2.size());
        for(int i=0; i<appList.size(); i++){
            ActivityManager.AppTask rapi = appList.get(i);
            Log.d("run Process","Package Name : " + rapi.getTaskInfo());
            Log.d("run Process","Package Name : " + rapi);

        }
        for(int i=0; i<appList2.size(); i++){
            ActivityManager.RunningAppProcessInfo rapi2 = appList2.get(i);
            Log.d("RunningAppProcessInfo","Package Name : " + rapi2);
            Log.d("RunningAppProcessInfo","Package Name : " + rapi2.processName);
        }
        activitymanager.killBackgroundProcesses("kr.infinix.hpay.apposw");
    }
    public void targetkill(String pkg_name) {
        activitymanager.killBackgroundProcesses(pkg_name);
    }

}
