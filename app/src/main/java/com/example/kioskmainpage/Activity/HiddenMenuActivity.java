package com.example.kioskmainpage.Activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.kioskmainpage.MyWakeLock;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.ShutdownAdminReceiver;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.InvocationTargetException;

public class HiddenMenuActivity extends AppCompatActivity {

    private static final String TAG = "HiddenMenu";
    DevicePolicyManager devicePolicyManager;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_menu);



        Button screen_off = (Button)findViewById(R.id.screen_off);
        screen_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

                ComponentName componentName = new ComponentName(getApplicationContext(), ShutdownAdminReceiver.class);
                //권한묻기
                if(!devicePolicyManager.isAdminActive(componentName)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    startActivityForResult(intent, 0);
                }

                //화면끔
                devicePolicyManager.lockNow();
            }
        });
        Button msg_send = (Button)findViewById(R.id.msg_send);
        msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessaging("etq0iezwtDM:APA91bGbBtkNGWaDwFuJs9kFVwOkNAr0VC9QFkyQkn1RwMNKxmQulBjv9wRe1E_K6Mto5YV9qWQs9Ont3uVd6Czzw5r1XCZhh4GRVI5sQW3CLdmqRbh_8m8c5TGLvg7ZmhTNQPqno_QB",0,"ss");
            }
        });
        //자동로그인 체크박스 부분입니다.
        CheckBox checkbox_AutoLogin = (CheckBox)findViewById(R.id.checkBox_AutoLogin);
        checkbox_AutoLogin.setOnCheckedChangeListener(new CheckButtonListener());
        SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("AutoLogin_status",false)) checkbox_AutoLogin.setChecked(true);// 확인후 체크

    }
    @Override
    protected void onNewIntent(Intent intent)
    {
        System.out.println( "onNewIntent 호출됨" );// 인텐트를 받은 경우만, 값을 Activity로 전달하도록 합니다.
        if( intent != null )
        {
            processIntent( intent );
        }
        super.onNewIntent(intent);
    }
    // 인텐트를 처리하도록 합니다.
    private void processIntent( Intent intent ) {

        String title = intent.getStringExtra("title");
        String from = intent.getStringExtra("from");
        String body = intent.getStringExtra("body");
        String contents = intent.getStringExtra("contents");

        if (from == null) {// from 값이 없는 경우, 값을 전달하지 않습니다. (푸쉬 노티 메시지가 아닌것을 판단하고 처리하지 않는듯).
            Log.d(TAG, "보낸 곳이 없습니다.");
            return;
        }// 메시지를 받은 것우 처리를 합니다.
        if(body.equals("SCREEN_ON"))
        {
            //TODO : LOCK 된 스크린 켜기
            powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
            System.out.println("스크린켜기");
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "myapp : WAKELOCK");
            wakeLock.acquire(); // WakeLock 깨우기
            wakeLock.release(); // WakeLock 해제
        }
        Log.d( TAG, body );
        //     Toast.makeText(this,body,Toast.LENGTH_SHORT).show();
        //    token_text.setText(title+"\n"+body+"\n"+contents);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            if(requestCode==0)
            {
                devicePolicyManager.lockNow();
            }
        }
    }
    public void SendMessaging(String SENDER_ID,int messageId,String message)
    {
        System.out.println("메시지 전송!!");
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder( SENDER_ID+ "@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());

    }
    public class CheckButtonListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.checkBox_AutoLogin:
                    SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(isChecked==true) {
                        editor.putBoolean("AutoLogin_status",true);
                    }
                    else {
                        editor.putBoolean("AutoLogin_status",false);
                    }
                    editor.commit();
                    break;

            }
        }
    }
}
