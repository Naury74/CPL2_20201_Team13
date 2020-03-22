package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.R;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class CouponFailActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_fail);
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        TextView CouponFail_text4 = (TextView) findViewById(R.id.CouponFail_text4);
        Spannable span= (Spannable) CouponFail_text4.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD), 5,  9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        Button CouponFail_btn_skip = (Button)findViewById(R.id.CouponFail_btn_skip);
        CouponFail_btn_skip.setOnClickListener(this);
        Button CouponFail_btn_retry = (Button)findViewById(R.id.CouponFail_btn_skip);
        CouponFail_btn_retry.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {//결제화면 불러오기 및 포장 유무, 메뉴구성, 총가격 넘김
        switch (v.getId()) {
            case R.id.CouponFail_btn_skip:
                Intent intent = new Intent(this,CouponAndReceipt_announce_Activity.class);
                intent.putExtra("type",22);//영수증
                startActivity(intent);
                finish();
                break;
            case R.id.CouponFail_btn_retry:
                Intent intentToCoupon = new Intent(this,Coupon_Activity.class);
                intentToCoupon.putExtra("type",21);//쿠폰
                startActivity(intentToCoupon);
                finish();
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
