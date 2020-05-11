package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.Pay.Senior_Pay_Loading;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.Adapter.SelectedListAdapter;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.Locale;

public class Order_Method_Selection_Popup extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    private TextView title;
    public SelectedListAdapter adapter;
    Intent intent;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__method__selection__popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        this.setFinishOnTouchOutside(false);

        title = (TextView)findViewById(R.id.title_view);
        //Spannable span = (Spannable) title.getText();
        //span.setSpan(new ForegroundColorSpan(getColor(R.color.light_green)), 27, 34, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("원하시는 주문 방식을 선택해 주세요. 키오스크 사용이 처음이시면 간단주문을 눌러 사용해보세요.",TextToSpeech.QUEUE_FLUSH,null);
                    tts.setSpeechRate((float) 0.4);

                }
            }
        });
    }

    public void onClick_normal(View v){
        Myapplication myapp = (Myapplication) getApplication();
        myapp.setadapter(adapter); //전역변수로써 넘겨줌
        Intent intent = new Intent(Order_Method_Selection_Popup.this, MainActivity.class);
        intent.putExtra("isOrdered", false);
        intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
        setResult(RESULT_OK, intent); //설정했다고 알림
        startActivity(intent);
        BestNewMenuActivity bestNewMenuActivity = (BestNewMenuActivity) BestNewMenuActivity.activity;
        bestNewMenuActivity.finish();
        finish();
        return;
    }

    public void onClick_easy(View v){
        Myapplication myapp = (Myapplication) getApplication();
        myapp.setadapter(adapter); //전역변수로써 넘겨줌
        Intent intent = new Intent(Order_Method_Selection_Popup.this, EasyMenuSelectionActivity.class);
        intent.putExtra("isOrdered", false);
        intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
        setResult(RESULT_OK, intent); //설정했다고 알림
        startActivity(intent);
        BestNewMenuActivity bestNewMenuActivity = (BestNewMenuActivity) BestNewMenuActivity.activity;
        bestNewMenuActivity.finish();
        finish();
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onBackPressed() {
        return;
    }
}
