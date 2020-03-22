package com.example.kioskmainpage.Activity.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import com.example.kioskmainpage.ServerConn.TransferToServer;
import com.example.kioskmainpage.ServerConn.DownloadUnzip;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SyncLoadingActivity extends AppCompatActivity {
    public CountDownTimer countDownTimerforgif;
    ImageView loading_dot;
    Myapplication myapp;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_loading);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

//        //GIF 이미지 실행설정
//        final ImageView SyncLoding_loding_dot = (ImageView) findViewById(R.id.SyncLoding_loding_dot);
//        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(SyncLoding_loding_dot);
//        Glide.with(this).load(R.drawable.gif_loding_dot).into(gifImage);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        myapp = (Myapplication)getApplication();
        countDownTimerforgif_set();
        countDownTimerforgif.start();

        if(type.equals("SyncNow"))//"동기화 중"
        {
            TextView sync_Text1 = (TextView)findViewById(R.id.SyncLodingtextView1);
            sync_Text1.setText("동기화 중");
            TextView sync_Text2 = (TextView)findViewById(R.id.SyncLodingtextView2);
            sync_Text2.setVisibility(View.VISIBLE);
        }
        SyncTask SyncTask = new SyncTask();
        SyncTask.execute();

    }

    class SyncTask extends AsyncTask<String, Void, String> {

        String sync_version;
        String lastetsync_version;

        @Override
        protected void onPreExecute() {
            //현재 기기의 버전을 받아오는 부분입니다.
            SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);
            sync_version = sharedPreferences.getString("Sync_Version","12345678901234");
            Double.parseDouble(sync_version);
            TransferToServer transferToServer = new TransferToServer(); //transferToServer 객체생성
            transferToServer.TransferToServer_datainsert(myapp.getBizNum(), myapp);
            try {
                transferToServer.execute();
            }catch(Exception e){
                Log.d("Loading", "onPreExecute: 동기화정보 불러오기 실패");
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (type.equals("SyncNow"))//"동기화 중"
            {
                //TODO : 최신 메뉴 불러옴
                Intent intent1 = new Intent(SyncLoadingActivity.this, SyncCompleteActivity.class);
                startActivity(intent1);
                finish();
            } else if (type.equals("DataConfirm"))//"데이터 확인"
            {
                lastetsync_version = myapp.getLastest_SyncVersion();
                //동기화목록 있으면 Sync changeNeed 없으면 no need
                if (Double.parseDouble(sync_version) < Double.parseDouble(lastetsync_version)) {
                    Intent intent1 = new Intent(SyncLoadingActivity.this, SyncChangeNeedActivity.class);
                    intent1.putExtra("recentVersion", myapp.getLastest_SyncVersion());//받아온 최신버전 코드 넘겨줌, 기기의 현재 버전 코드는 application 전역변수로
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(SyncLoadingActivity.this, SyncChangeNoNeedActivity.class);
                    intent1.putExtra("recentVersion", 2);//받아온 최신버전 코드 넘겨줌, 기기의 현재 버전 코드는 application 전역변수로
                    startActivity(intent1);
                    finish();
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {
            return "";
        }
    }


    // 외부 터치불가
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    public void countDownTimerforgif_set(){//히든메뉴 접속용 타이머
        loading_dot = (ImageView) findViewById(R.id.SyncLoding_loding_dot);
        countDownTimerforgif = new CountDownTimer(50000, 300) { // 1000당 1초   5분 1초당 카운트
            int i=0;
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_1);
                        break;
                    case 1:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_2);
                        break;
                    case 2:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_3);
                        break;
                }
                i++;
                i=i%3;
            }
            public void onFinish() {
            }
        };
    }
}
