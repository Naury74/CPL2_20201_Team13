package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

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

import com.example.kioskmainpage.Activity.Pay.CompleteOrder_PayActivity;
import com.example.kioskmainpage.R;

import java.text.NumberFormat;

public class CouponComplete_Activity extends AppCompatActivity implements View.OnClickListener {

    String phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_complete);
  //      countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        Button btnFinish = (Button) findViewById(R.id.btnFinish_coupon_complete);
        Button btncancel = (Button) findViewById(R.id.btnFinish_coupon_cancel);
        btnFinish.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        //텍스트 설정
        TextView comple_text1 = (TextView)findViewById(R.id.coupon_comple_text1);
        TextView comple_text2 = (TextView)findViewById(R.id.coupon_comple_text2);
        TextView coupon_comple_name = (TextView)findViewById(R.id.coupon_comple_name);
        TextView coupon_comple_phoneNum = (TextView)findViewById(R.id.coupon_comple_phoneNum);

        Intent dataforloading = getIntent() ;
        String text1 = "현재 포인트\n";
        String text1_2;
        String text2 = "전자영수증을 발급하시겠습니까?";
        String name = "홍길동",point;
        phonenumber  = dataforloading.getStringExtra("phonenumber"); //전화번호를가져옴
        String sum_point = dataforloading.getStringExtra("sum_point");//현재 적립되어있는 포인트

        //TODO: 포인트적립시 고객여부 확인후 진행.
        if(dataforloading.getStringExtra("uesr_name").equals(""))
        {
            name="홍길동";
        }
        else {
            name=dataforloading.getStringExtra("uesr_name");
        }


        int pointtemp = Integer.parseInt(sum_point);
        coupon_comple_name.setText("'"+name+"'님");//이름설정

        if(phonenumber.length() == 11)
            coupon_comple_phoneNum.setText(phonenumber.substring(0,3)+"-****-"+phonenumber.substring(7,11));
        else if(phonenumber.length() == 10)
            coupon_comple_phoneNum.setText(phonenumber.substring(0,3)+"-***-"+phonenumber.substring(6,10));

        point = NumberFormat.getInstance().format(pointtemp);//포인트쉼표표시
        text1_2 =point+"P";
        comple_text1.setText(text1+text1_2);
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span2 = (Spannable) comple_text1.getText();
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0,  comple_text1.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span2.setSpan(new RelativeSizeSpan(1.3f), text1.length(), comple_text1.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//기존글자크기비례 글자크기변경

        comple_text2.setText(text2); //전자영수증을 발급하기겠습니까? 부분
        Spannable span3= (Spannable) comple_text2.getText();
        span3.setSpan(new StyleSpan(Typeface.BOLD), 0,  5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//        span3.setSpan(new RelativeSizeSpan(1.2f), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//기존글자크기비례 글자크기변경
//        span3.setSpan(new ForegroundColorSpan(rounded_red), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish_coupon_cancel://취소
                Intent intent = new Intent(this, CompleteOrder_PayActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
                finish();
                break;
            case R.id.btnFinish_coupon_complete://발행
                Intent intent2 = new Intent(this, ReceiptPopupActivity.class);
                intent2.putExtra("phonenumber", phonenumber);
                startActivity(intent2);
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
