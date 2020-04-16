package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.R;

import java.util.Locale;

public class Senior_Pay_CardActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private TextView title;
    Intent intent;
    int total_price;
    String takeout;
    private TextToSpeech tts;

    AnimationDrawable card_animation;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        card_animation.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_pay_card);

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

        title = (TextView)findViewById(R.id.title_view);
        Spannable span = (Spannable) title.getText();
        span.setSpan(new ForegroundColorSpan(getColor(R.color.light_green)), 27, 34, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        //span.setSpan(new RelativeSizeSpan(1.5f), 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("아래쪽 카드 투입구에 카드를 끝까지 삽입한 후, 결제 진행 버튼을 눌러주세요",TextToSpeech.QUEUE_FLUSH,null);
                    tts.setSpeechRate((float) 0.4);

                }
            }
        });

        ImageView Card_announce_card_imageView = (ImageView) findViewById(R.id.Card_announce_card_imageView);
        Card_announce_card_imageView.setImageResource((R.drawable.card_animation));
        card_animation = (AnimationDrawable)  Card_announce_card_imageView.getDrawable();
    }

    public void onClick_cancel(View v){
        finish();
    }

    public void onClick_confirm(View v){
        Intent intent2 = new Intent(this,Senior_Pay_Loading.class);
        intent2.putExtra("takeout",takeout);
        intent2.putExtra("total_price",total_price);
        startActivity(intent2);
        finish();
    }
}
