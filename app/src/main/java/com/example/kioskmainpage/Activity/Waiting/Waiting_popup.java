package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.R;

public class Waiting_popup extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //버튼리스너
        Button waiting_popup_to_nopay = (Button)findViewById(R.id.waiting_popup_to_nopay);
        Button waiting_popup_to_main = (Button)findViewById(R.id.waiting_popup_to_main);
        Button waiting_popup_btn_cancel = (Button)findViewById(R.id.waiting_popup_btn_cancel);
        waiting_popup_to_nopay.setOnClickListener(this);
        waiting_popup_to_main.setOnClickListener(this);
        waiting_popup_btn_cancel.setOnClickListener(this);

        //글자 스타일변경
        TextView waiting_popup_text1 = (TextView)findViewById(R.id.waiting_popup_text1) ;
        TextView waiting_popup_text2 = (TextView)findViewById(R.id.waiting_popup_text2) ;

        int rounded_red = getResources().getColor(R.color.rounded_red); //색깔

        Spannable spanfortext1= (Spannable) waiting_popup_text1.getText();
        spanfortext1.setSpan(new ForegroundColorSpan(rounded_red), 19, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        spanfortext1.setSpan(new RelativeSizeSpan(1.1f), 19, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 크기처리
        spanfortext1.setSpan(new StyleSpan(Typeface.BOLD), 19,  28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        Spannable spanfortext2= (Spannable) waiting_popup_text2.getText();
        spanfortext2.setSpan(new StyleSpan(Typeface.BOLD), 0,  6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        spanfortext2.setSpan(new StyleSpan(Typeface.BOLD), 21,  23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) //외부터치방지
    {
        return false;

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waiting_popup_to_nopay:
                Intent intent_To_waiting_nopay = new Intent(this, Waiting_nopayActivity.class);
                startActivity(intent_To_waiting_nopay);
                finish();
                break;
            case R.id.waiting_popup_to_main:
                Intent intent = new Intent();
                intent.putExtra("result","OK");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.waiting_popup_btn_cancel:
                Intent intent2 = new Intent();
                intent2.putExtra("result","NO");
                setResult(1234, intent2);
                finish();
                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
