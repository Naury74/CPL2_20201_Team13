package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

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

public class ReceiptCompletePopupActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_complete_popup);
 //       countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button btnFinish = (Button) findViewById(R.id.btnFinish_receipt_complete);
        btnFinish.setOnClickListener(this);

        //텍스트 설정
        TextView receipt_complete_text1 = (TextView)findViewById(R.id.receipt_complete_textView1);
        TextView receipt_complete_text2 = (TextView)findViewById(R.id.receipt_complete_textView2);
        TextView receipt_complete_text3 = (TextView)findViewById(R.id.receipt_complete_textView3);
        String text1;
        String text2 = "전자영수증이\n 문자를 통해 발행되었습니다.";
        String text3 = "모바일 오더 앱에 회원가입하시면\n포인트 적립이 가능합니다.";

        int rounded_red = getResources().getColor(R.color.rounded_red);//색설정

        /* //text1수정 필요시 사용
        receipt_complete_text1.setText(text1);
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span1 = (Spannable) CNRLoding_text1.getText();
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0,  2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span1.setSpan(new ForegroundColorSpan(rounded_red), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        */

        receipt_complete_text2.setText(text2);
        Spannable span2 = (Spannable) receipt_complete_text2.getText();
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0,  5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span2.setSpan(new RelativeSizeSpan(1.2f), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//기존글자크기비례 글자크기변경
        span2.setSpan(new ForegroundColorSpan(rounded_red), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리

        receipt_complete_text3.setText(text3);
        Spannable span3= (Spannable) receipt_complete_text3.getText();
        span3.setSpan(new ForegroundColorSpan(rounded_red), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span3.setSpan(new ForegroundColorSpan(rounded_red), 19, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish_receipt_complete://주문완료
                finish();
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
