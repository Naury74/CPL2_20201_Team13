package com.example.kioskmainpage.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class SelectedResetPopup_Activity extends AppCompatActivity {
    private static final String TAG = "SelectedResetPopup_Activity";

    private CountDownTimer countDownTimerForReset;//BestNewMenuActivity로  카운트다운
    int basetime = 10; //카운트다운 시간
    LinearLayout SoldoutPopup_background;
    TextView SelectedReset_text1, SelectedReset_text2, SelectedReset_text3, SelectedReset_text4, SelectedReset_text1_empty, SelectedReset_text2_empty, SelectedReset_text3_empty, textView14, SelectedReset_secTextview, SelectedReset_secTextview2;
    int rounded_red;


    public void setTheme() {
        Myapplication app = (Myapplication) getApplication();
        SelectedReset_text1.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        SelectedReset_text2.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        SelectedReset_text3.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        SelectedReset_text4.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        SelectedReset_secTextview.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_SECONDS())); //초(카운트다운_)
        SoldoutPopup_background.setBackgroundResource(app.getMainTheme().getThemeItem().getTAKE_OUT_BACKGROUND_ROUND_COLOR());
        SelectedReset_text1_empty.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR())); //메뉴선택 없을때 팝업 글자색
        SelectedReset_text2_empty.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        SelectedReset_text3_empty.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR()));
        textView14.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_COLOR())); //"초"
        SelectedReset_secTextview2.setTextColor(getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_SECONDS())); //초(카운트다운_)
        rounded_red = getResources().getColor(app.getMainTheme().getThemeItem().getSELECTED_RESET_TEXT_HIGHLIGHT());//#EB5061 색상
//        int rounded_red = getResources().getColor(R.color.rounded_red);//#EB5061 색상
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedreset_popup);
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        TextView timer_text = (TextView)findViewById(R.id.SelectedReset_secTextview);
        timer_text.setText(Integer.toString(basetime));

        //주문목록이 비었는지 확인하는 부분.
        Intent intent = getIntent();// MainActivity에서 넘어온 intent를 받아옴
        if(intent.getBooleanExtra("list_empty",false))//주문목록이 있는지 확인
        {
            LinearLayout SelectedReset_linear2_empty = (LinearLayout)findViewById(R.id.SelectedReset_linear2_empty);//주문목록이 비었을시 사용되는 UI
            SelectedReset_linear2_empty.setVisibility(View.VISIBLE);
            LinearLayout SelectedReset_linear1 = (LinearLayout)findViewById(R.id.SelectedReset_linear1);//주문목록이 있을시 사용되는 UI
            SelectedReset_linear1.setVisibility(View.GONE);
        }
        else
        {
            LinearLayout SelectedReset_linear2_empty = (LinearLayout)findViewById(R.id.SelectedReset_linear2_empty);//주문목록이 비었을시 사용되는 UI
            SelectedReset_linear2_empty.setVisibility(View.GONE);
            LinearLayout SelectedReset_linear1 = (LinearLayout)findViewById(R.id.SelectedReset_linear1);//주문목록이 있을시 사용되는 UI
            SelectedReset_linear1.setVisibility(View.VISIBLE);
        }
        //글자 강조 부분 설정
        SoldoutPopup_background = (LinearLayout) findViewById(R.id.SoldoutPopup_background);
        SelectedReset_text1 = (TextView) findViewById(R.id.SelectedReset_text1);
        SelectedReset_text3 = (TextView) findViewById(R.id.SelectedReset_text3);
        SelectedReset_text4 = (TextView) findViewById(R.id.SelectedReset_text4);
        SelectedReset_text1_empty = (TextView) findViewById(R.id.SelectedReset_text1_empty);
        SelectedReset_text2_empty = (TextView) findViewById(R.id.SelectedReset_text2_empty);
        SelectedReset_text3_empty = (TextView) findViewById(R.id.SelectedReset_text3_empty);
        SelectedReset_secTextview2 = (TextView) findViewById(R.id.SelectedReset_secTextview2);
        SelectedReset_secTextview = (TextView) findViewById(R.id.SelectedReset_secTextview);
        textView14 = (TextView) findViewById(R.id.textView14);


        SelectedReset_text2 = (TextView)findViewById(R.id.SelectedReset_text2);// 주문 목록이 있는경우의 텍스트

        setTheme();

        Spannable span1 = (Spannable)SelectedReset_text2.getText();
        span1.setSpan(new StyleSpan(Typeface.BOLD), 18,  22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        TextView SelectedReset_text2_empty = (TextView)findViewById(R.id.SelectedReset_text2_empty);// 주문 목록이 없는경우의 텍스트

        Spannable span2 = (Spannable)SelectedReset_text2_empty.getText();
        span2.setSpan(new ForegroundColorSpan(rounded_red), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span2.setSpan(new StyleSpan(Typeface.BOLD), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리

        countDownTimerForReset();
        countDownTimerForReset.start();
        setTheme();
    }


    public void countDownTimerForReset(){
        countDownTimerForReset = new CountDownTimer(basetime*1000, 1000) { // 1000당 1초
            public void onTick(long millisUntilFinished) {
                TextView timer_text = (TextView)findViewById(R.id.SelectedReset_secTextview);
                TextView timer_text2 = (TextView)findViewById(R.id.SelectedReset_secTextview2);
                String time = Integer.toString(Integer.parseInt(timer_text.getText().toString())-1);
                timer_text.setText(time);
                timer_text2.setText(time);
            }
            public void onFinish() {
                //타이머가 다하면 SelectedResetPopup_Activity가 종료되고 메인화면으로 이동됨. 이후 선택메뉴 초기화 및 베스트&뉴 엑티비티로 이동
                Intent intent_reset = new Intent();
                intent_reset.putExtra("result",RESULT_OK);
                intent_reset.putExtra("requestCode",7);
                setResult(RESULT_OK, intent_reset);
                finish();
            }
        };
    }
    public void SelectedReset_button_confirm(View v){// 버튼을 누를경우 초기화 되지않음, 메인으로 이동
        finish();
    }
    public void SelectedReset_button_move(View v){// 버튼을 누를경우 초기화후 B&N 로 이동
        Intent intent_reset = new Intent();
        intent_reset.putExtra("result",RESULT_OK);
        intent_reset.putExtra("requestCode",7);
        setResult(RESULT_OK, intent_reset);
        finish();
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
