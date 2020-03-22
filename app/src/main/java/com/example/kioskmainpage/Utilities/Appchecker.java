package com.example.kioskmainpage.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class Appchecker {
    Context context;
    String TAG = "Appchecker";
    final String appPackageName = "kr.infinix.hpay.appposw"; // 앱포스 패키지명
    boolean is_appposw_exist=false;


    public Appchecker(Context context){
        this.context = context;
        checkINFINIXapp();
    }
    public void checkINFINIXapp(){
        final PackageManager packageManager = context.getApplicationContext().getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo info : list) {
            String appActivity      = info.activityInfo.name;
            String appPackageName   = info.activityInfo.packageName;
            String appName          = info.loadLabel(packageManager).toString();

            Drawable drawable = info.activityInfo.loadIcon(packageManager);
            if(appPackageName.equals("kr.infinix.hpay.appposw"))
            {
                is_appposw_exist=true;
            }
            Log.d(TAG, "appName : " + appName + ", appActivity : " + appActivity + ", appPackageName : " + appPackageName);
        }
    }
    public boolean isit_installed(){
        try {
            checkINFINIXapp();
        }catch (Exception e) {}
        return is_appposw_exist;
    }
    public void go_market_infinix(){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
