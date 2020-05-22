package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.R;

import java.util.Locale;

public class Senior_Pay_Change_Loading extends AppCompatActivity {

    public CountDownTimer countDownTimerforgif;
    public CountDownTimer countDownTimer;
    android.widget.ImageView ImageView;
    MediaPlayer mediaPlayer;
    Intent intent;
    int total_price;
    String takeout;
    private TextToSpeech tts;
    TextView title;
    TextView title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior__pay__change__loading);

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

        title = (TextView)findViewById(R.id.title);
        title2 = (TextView)findViewById(R.id.title2);
        ImageView =(ImageView) findViewById(R.id.Card_Loding_loding_dot);

        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.eventually);
        mediaPlayer.start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("주문 접수 중입니다. 잠시만 기다려 주세요.",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

        countDownTimerforgif_set();
        countDownTimerforgif.start();
    }

    public void countDownTimerforgif_set(){
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
                countDownTimer_set();
                countDownTimer.start();
                title.setText("반환 중");
                title2.setText("거스름 돈을 받아주세요");
                tts.speak("거스름 돈을 받아주세요.",TextToSpeech.QUEUE_FLUSH,null);
            }
        };
    }

    public void countDownTimer_set(){
        countDownTimer = new CountDownTimer(5000, 300) { // 1000당 1초   5분 1초당 카운트
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
                Intent intent = new Intent(Senior_Pay_Change_Loading.this, Senior_Pay_OrderComplelte.class);
                intent.putExtra("takeout",takeout);
                intent.putExtra("total_price",total_price);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        countDownTimerforgif.cancel();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onBackPressed() {
        return;
    }
}
