package com.example.kioskmainpage;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.HiddenMenuActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.ServerConn.BlankAsynkTask;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FCM";
    @Override
    public void onNewToken(String token) {
        Log.e("newtoken : ",token);
//        Toast.makeText(this,token,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        System.out.println("메시지 보냄");
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        System.out.println("전송 에러");
        e.printStackTrace();
    }

    //새로운 메시지 받으면 호출
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,remoteMessage.getData().toString());
        //data가 제이슨 형태로 올때
        if(remoteMessage.getData()!=null) {
            String content = remoteMessage.getData().get("content");
            if(remoteMessage.getData().containsKey("title"))//title이 존재하면
            {
                String title = remoteMessage.getData().get("title");
                if(content.equals("SOLD_OUT") || content.equals("SOLD_OUT_CANCEL"))//매진처리
                {
                    sendToActivity(getApplicationContext(), title,content,remoteMessage.getData().toString());//main activity로 보냄
                }
            }
            Log.d(TAG,content);
            if(content.equals("SCREEN_ON"))//스크린 켜기
            {
                Log.d(TAG,content);
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
                PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE|PowerManager.ACQUIRE_CAUSES_WAKEUP,"KIOSK:" );
                wakeLock.acquire();
                wakeLock.release();
                KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
                lock.disableKeyguard();
            }
            else if(content.equals("SCREEN_OFF"))//스크린 끄기
            {
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                devicePolicyManager.lockNow();
            }
            else if(content.equals("CHECK_DEVICE")){//화면 확인 요청
                Myapplication myapp =(Myapplication)getApplicationContext();
                if(myapp.getMainActivityContext()!=null)
                sendToActivity(getApplicationContext(), "",content,remoteMessage.getData().toString());//main activity로 보냄
            }
        }


        //data가 notification으로 올때
     /*   String from = remoteMessage.getFrom();
        Log.d(TAG,"title:" + remoteMessage.getNotification().getTitle()+
                ", body:" + remoteMessage.getNotification().getBody()+
                ", data:" + remoteMessage.getData());
        // 액티비티 쪽으로 메시지를 전달하는 메소드를 호출합니다.
        sendToActivity(getApplicationContext(), remoteMessage.getFrom(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().toString());
*/
    }

    // Activity 쪽으로 메소드를 전달하는 메소드 입니다.
    private void sendToActivity(Context context,String title, String content, String json ){
        Intent intent;

            intent = new Intent(context, MainActivity.class);
        Log.d(TAG, "sendToActivity: "+"FCM수신"+"title "+title+"content "+content);
            intent.putExtra("title", title);
            intent.putExtra("content",content);
            intent.putExtra("json", json);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

    }
}
