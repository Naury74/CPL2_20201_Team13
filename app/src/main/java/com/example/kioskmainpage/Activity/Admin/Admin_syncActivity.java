package com.example.kioskmainpage.Activity.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class Admin_syncActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sync);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        ImageButton admin_main_btn_left = (ImageButton)findViewById(R.id.admin_main_btn_left);
        admin_main_btn_left.setOnClickListener(this);

        Button sync_request = (Button) findViewById(R.id.sync_request_btn);
        sync_request.setOnClickListener(this);

        Myapplication app = (Myapplication)getApplication();
        app.setAdminSyncContext(this);
        TextView sync_status_adminSync = (TextView)findViewById(R.id.sync_status_adminSync);

        //동기화 버전(현재기기가 가지고있는 버전) 확인 부분입니다.
        SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);
        String Sync_Version = sharedPreferences.getString("Sync_Version","12345678901234");
        String version_status= Sync_Version.substring(0,4)+"-"+Sync_Version.substring(4,6)+"-"+Sync_Version.substring(6,8)+" "+Sync_Version.substring(8,10)+":"+Sync_Version.substring(10,12)+":"+Sync_Version.substring(12,14);
        sync_status_adminSync.setText(version_status);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.admin_main_btn_left:
                finish();
                break;
            case R.id.sync_request_btn:
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                if(mWifi == null)//인터넷 연결 x
                {
                    Toast.makeText(this,"인터넷을 연결해주십시오",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this,SyncLoadingActivity.class);
                intent.putExtra("type","DataConfirm");
                startActivityForResult(intent,1);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //팝업 액티비티의 결과를 받는 부분
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //데이터 동기화 구현 = 데이터 동기화 메뉴 -> 기기 관리 -> 관리자 메뉴 -> 메인액티비티 -> 로딩페이지로 이동함.
                    Intent intent_syncTodeviceManage = new Intent();
                    intent_syncTodeviceManage.putExtra("result",RESULT_OK);
                    intent_syncTodeviceManage.putExtra("requestCode",2);
                    setResult(RESULT_OK, intent_syncTodeviceManage);
                    finish();
            }
        }
    }
}
