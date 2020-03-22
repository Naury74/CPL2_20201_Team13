package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.Pay.CompleteOrder_PayActivity;
import com.example.kioskmainpage.Activity.Pay.Order_fail_popupActivity;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class CouponAndReceipt_announce_Activity extends AppCompatActivity implements View.OnClickListener {

    int type;
    LinearLayout coupon_announce_layout;
    LinearLayout receipt_announce_layout;
    Intent dataforload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_and_receipt_announce_);

   //     countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button coupon_announce_btn_next = (Button)findViewById(R.id.coupon_announce_btn_next);
        coupon_announce_btn_next.setOnClickListener(this);
        Button coupon_announce_btn_cancel = (Button)findViewById(R.id.coupon_announce_btn_cancel);
        coupon_announce_btn_cancel.setOnClickListener(this);
        Button receipt_announce_btn_next = (Button)findViewById(R.id.receipt_announce_btn_next);
        receipt_announce_btn_next.setOnClickListener(this);
        Button receipt_announce_btn_cancel = (Button)findViewById(R.id.receipt_announce_btn_cancel);
        receipt_announce_btn_cancel.setOnClickListener(this);

        coupon_announce_layout = (LinearLayout)findViewById(R.id.coupon_announce_layout);
        receipt_announce_layout = (LinearLayout)findViewById(R.id.receipt_announce_layout);
        //텍스트설정
        //포인트
        TextView coupon_announce_textView1 = (TextView) findViewById(R.id.coupon_announce_textView1);
        TextView coupon_announce_textView3 = (TextView) findViewById(R.id.coupon_announce_textView3);
        //영수증
        TextView receipt_announce_textView2 = (TextView) findViewById(R.id.receipt_announce_textView2);

        int rounded_red = getResources().getColor(R.color.rounded_red);

        //쿠폰 레이아웃 텍스트
        Spannable span_coupon_announce_textView1 = (Spannable) coupon_announce_textView1.getText();
        span_coupon_announce_textView1.setSpan(new StyleSpan(Typeface.BOLD), 0,  7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리

        Spannable span4= (Spannable) coupon_announce_textView3.getText();
        span4.setSpan(new StyleSpan(Typeface.BOLD), 9,  17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span4.setSpan(new StyleSpan(Typeface.BOLD), 21,  28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리

        //영수증 레이아웃 텍스트

        Spannable span_receipt_announce_textView2 = (Spannable) receipt_announce_textView2.getText();
        span_receipt_announce_textView2.setSpan(new StyleSpan(Typeface.BOLD), 0,  11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span_receipt_announce_textView2.setSpan(new StyleSpan(Typeface.BOLD), 19,  30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span_receipt_announce_textView2.setSpan(new ForegroundColorSpan(rounded_red), 19, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        ImageView iv  = (ImageView)findViewById(R.id.imageView4);
        dataforload = getIntent() ;
        type  = dataforload.getIntExtra("type",-1); //21 쿠폰 22 영수증
        if(type == 22)
        {
            coupon_announce_layout.setVisibility(View.GONE);
            receipt_announce_layout.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();


        switch (v.getId()) {
            case R.id.coupon_announce_btn_next: //수정필요
                //와이파이 연결이 안되었을 경우 실패 창 띄움
                if (mWifi==null) {
                    // Do whatever
                    Myapplication myapplication = (Myapplication) getApplication();
                    myapplication.setOrderRetryCount(0);

                    Intent intent = new Intent(this, Order_fail_popupActivity.class);
                    intent.putExtra("fail_type","WIFI_CONNECT");
                    intent.putExtra("next_activity","Coupon");
                    intent.putExtra("type",21);//쿠폰
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(this, Coupon_Activity.class);
                    intent.putExtra("type", 21); //쿠폰
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.coupon_announce_btn_cancel: //쿠폰레이아웃 gone 영수증레이아웃 visible
                coupon_announce_layout = (LinearLayout)findViewById(R.id.coupon_announce_layout);
                receipt_announce_layout = (LinearLayout)findViewById(R.id.receipt_announce_layout);
                coupon_announce_layout.setVisibility(View.GONE);
                receipt_announce_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.receipt_announce_btn_next: //수정필요
                if (mWifi==null) {
                    // Do whatever
                    Myapplication myapplication = (Myapplication) getApplication();
                    myapplication.setOrderRetryCount(0);

                    Intent intent = new Intent(this,Order_fail_popupActivity.class);
                    intent.putExtra("fail_type","WIFI_CONNECT");
                    intent.putExtra("next_activity","Coupon");
                    intent.putExtra("type",22);//쿠폰
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent2 = new Intent(this, Coupon_Activity.class);
                    intent2.putExtra("type", 22); //영수증
                    startActivity(intent2);
                    finish();
                }
                break;
            case R.id.receipt_announce_btn_cancel:
                if (mWifi==null) {
                    // Do whatever
                    Myapplication myapplication = (Myapplication) getApplication();
                    myapplication.setOrderRetryCount(0);

                    Intent intent = new Intent(this,Order_fail_popupActivity.class);
                    intent.putExtra("fail_type","WIFI_CONNECT");
                    intent.putExtra("next_activity","CompleteOrder");
                    intent.putExtra("phonenumber", dataforload.getStringExtra("phonenumber"));//쿠폰
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent ToCompleteOrder_PayActivity = new Intent(this, CompleteOrder_PayActivity.class);
                    ToCompleteOrder_PayActivity.putExtra("phonenumber", dataforload.getStringExtra("phonenumber"));
                    startActivity(ToCompleteOrder_PayActivity);
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        countDownReset();//카운트다운리셋
        //팝업 액티비티의 바깥쪽 터치를 무시하도록
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
