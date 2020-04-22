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

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

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
    private ArrayList<String> folder_names = new ArrayList<>();

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

        Senior_OrderListActivity senior_orderListActivity = (Senior_OrderListActivity)Senior_OrderListActivity.activity;
        senior_orderListActivity.finish();
        EasyMenuSelectionActivity easyMenuSelectionActivity = (EasyMenuSelectionActivity)EasyMenuSelectionActivity.activity;
        easyMenuSelectionActivity.finish();

        for(int i=1;i<=5;i++)
        {
            folder_names.add("main_menu_"+i);
        }

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
        countDownTimer = new CountDownTimer(15000, 300) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent intent_BestNewMenu = new Intent(Senior_Pay_OrderComplelte.this, BestNewMenuActivity.class);
                intent_BestNewMenu.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                intent_BestNewMenu.addFlags( FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                intent_BestNewMenu.putExtra("folderNames", folder_names);
                startActivityForResult(intent_BestNewMenu, 5);
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
        countDownTimer.cancel();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onBackPressed() {
        return;
    }
}
