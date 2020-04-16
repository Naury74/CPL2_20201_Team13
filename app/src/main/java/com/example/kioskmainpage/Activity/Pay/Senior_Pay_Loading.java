package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.kioskmainpage.R;

import java.util.Locale;

public class Senior_Pay_Loading extends AppCompatActivity {
    public CountDownTimer countDownTimerforgif;
    ImageView ImageView;
    MediaPlayer mediaPlayer;
    Intent intent;
    int total_price;
    String takeout;
    private TextToSpeech tts;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimerforgif.cancel();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_senior_pay_loading);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        this.setFinishOnTouchOutside(false);

        intent = getIntent();
        total_price = intent.getExtras().getInt("total_price");
        takeout = intent.getExtras().getString("takeout");

        ImageView =(ImageView) findViewById(R.id.Card_Loding_loding_dot);

        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.eventually);
        mediaPlayer.start();

        Handler mHandler;

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("                결제 중입니다. 잠시만 기다려 주세요.",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

        countDownTimerforgif_set();
        countDownTimerforgif.start();
    }

    public void countDownTimerforgif_set(){//히든메뉴 접속용 타이머
        countDownTimerforgif = new CountDownTimer(7000, 300) { // 1000당 1초   5분 1초당 카운트
            int i=0;
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_1);
                        break;
                    case 1:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_2);
                        break;
                    case 2:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_3);
                        break;
                }
                i++;
                i=i%3;
            }
            public void onFinish() {
                finish();
            }
        };
    }
    public void onBackPressed() {
        return;
    }
}
