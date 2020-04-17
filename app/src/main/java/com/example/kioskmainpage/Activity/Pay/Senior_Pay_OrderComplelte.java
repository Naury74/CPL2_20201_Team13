package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.R;

import java.util.Locale;
import java.util.Random;

public class Senior_Pay_OrderComplelte extends AppCompatActivity {

    int order_num = 1;
    TextView order_number;
    TextView text_announce;
    Intent intent;
    int total_price;
    String takeout;
    String order_type = "complete";
    private TextToSpeech tts;
    MediaPlayer mediaPlayer;
    public CountDownTimer countDownTimer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior__pay__order_complelte);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        randomNumber();

        intent = getIntent();
        total_price = intent.getExtras().getInt("total_price");
        takeout = intent.getExtras().getString("takeout");

        order_number = (TextView)findViewById(R.id.order_number);
        text_announce = (TextView)findViewById(R.id.text_announce);

        order_number.setText(order_num+"");
        Spannable span = (Spannable) text_announce.getText();
        span.setSpan(new ForegroundColorSpan(getColor(R.color.senior_btn_color)), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.1f), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.eventually);
        mediaPlayer.start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("                  주문이 완료 되었습니다. 주문번호를 확인해 주세요. 주문하신 상품의 조리가 완료되면 알려드릴게요!",TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

        countDownTimer_set();
        countDownTimer.start();

    }

    public void randomNumber(){
        Random random = new Random();
        order_num = random.nextInt(999);
    }

    public void countDownTimer_set(){
        countDownTimer = new CountDownTimer(15000, 300) { // 1000당 1초   5분 1초당 카운트
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent intent = new Intent(Senior_Pay_OrderComplelte.this, Senior_OrderListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("init_type",order_type);
                startActivity(intent);
                finish();
            }
        };
    }
    public void onBackPressed() {
        return;
    }
}
