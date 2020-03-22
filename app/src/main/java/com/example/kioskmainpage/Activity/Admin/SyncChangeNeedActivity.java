package com.example.kioskmainpage.Activity.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.LoadingActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class SyncChangeNeedActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_change_need);

        Button sync_change_request = (Button) findViewById(R.id.sync_change_btn_request);
        sync_change_request.setOnClickListener(this);
        Button sync_change_cancel = (Button) findViewById(R.id.sync_change_btn_cancel);
        sync_change_cancel.setOnClickListener(this);

        Intent intent = getIntent();
        String lastest_sync_cersion = intent.getStringExtra("recentVersion");// 이전엑티비티로부터 받아온 서버의 버전
        Log.d("SyncChangeNeedActivity", "onCreate: "+lastest_sync_cersion);
        TextView text_current_version = (TextView)findViewById(R.id.text_current_version);//기기
        TextView text_sever_version = (TextView)findViewById(R.id.text_sever_version);//서버

        //기가가 가지고 있는 버전을 텍스트뷰에 표시
        SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);
        String Sync_Version = sharedPreferences.getString("Sync_Version","12345678901234");
        String version_status= "기기 : "+Sync_Version.substring(0,4)+"-"+Sync_Version.substring(4,6)+"-"+Sync_Version.substring(6,8)+" "+Sync_Version.substring(8,10)+":"+Sync_Version.substring(10,12)+":"+Sync_Version.substring(12,14);
        text_current_version.setText(version_status);
        //서버가 가지고 있는 버전을 텍스트뷰에 표시
        version_status= "서버 : "+lastest_sync_cersion.substring(0,4)+"-"+lastest_sync_cersion.substring(4,6)+"-"+lastest_sync_cersion.substring(6,8)+" "+lastest_sync_cersion.substring(8,10)+":"+lastest_sync_cersion.substring(10,12)+":"+lastest_sync_cersion.substring(12,14);
        text_sever_version.setText(version_status);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.sync_change_btn_cancel:
                finish();
                break;
            case R.id.sync_change_btn_request:

                SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SyncCheck","SYNC");//동기화 설정
                editor.commit();
                Myapplication myapp =(Myapplication)getApplication();
                myapp.setAdminToMainType("SYNC");
                ((Admin_syncActivity)myapp.getAdminSyncContext()).finish();
                ((Admin_MainActivity)myapp.getAdminMainContext()).finish();
                ((Admin_deviceManageActivity)myapp.getAdmindeviceManageContext()).finish();
                ((MainActivity)myapp.getMainActivityContext()).finish();
                ((BestNewMenuActivity)myapp.getBestNewMenuActivityContext()).finish();
                Intent intent = new Intent(SyncChangeNeedActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
