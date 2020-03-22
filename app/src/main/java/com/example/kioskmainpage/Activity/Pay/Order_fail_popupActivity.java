package com.example.kioskmainpage.Activity.Pay;

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

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class Order_fail_popupActivity extends AppCompatActivity {

    TextView order_error,order_fail_reason,order_fail_info1,order_fail_info2;

    Button retry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_fail_popup);

        Myapplication app = (Myapplication) getApplication();

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        order_error = (TextView) findViewById(R.id.order_error_textview);
        order_fail_reason = (TextView) findViewById(R.id.order_fail_reason_textview);
        order_fail_info1 = (TextView) findViewById(R.id.order_fail_info1_textview);
        order_fail_info2 = (TextView) findViewById(R.id.order_fail_info2_textview);
        retry = (Button) findViewById(R.id.order_fail_btn_retry);


        final Intent intent = getIntent();
        String fail_type = intent.getStringExtra("fail_type");


        if(fail_type.equals("WIFI_CONNECT"))//와이파이 연결 안 된 경우
        {
            order_fail_reason.setText("인터넷 연결 불량");
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //로딩 팝업 띄움(2번 시도해도 실패 시 결제 취소완료 메시지 띄움)

                    Myapplication myapplication = (Myapplication) getApplication();
                    myapplication.setOrderRetryCount(myapplication.getOrderRetryCount()+1);

                    Intent intent2 = new Intent(Order_fail_popupActivity.this,Order_loading_popupActivity.class);
                    intent2.putExtra("fail_type",intent.getStringExtra("fail_type"));

                    startActivity(intent2);
                    finish();
                }
            });
        }
        else if(fail_type.equals("FAILURE"))//완전 실패 - 결제 취소 한 상태
        {
            //TODO : 나머지 케이스는 서버 통신 결과에 따라서
            order_error.setText("서비스 장애");
            order_fail_reason.setVisibility(View.GONE);
            order_fail_info1.setVisibility(View.VISIBLE);
            order_fail_info2.setVisibility(View.VISIBLE);

            String temp = order_fail_info1.getText().toString();
            temp = temp.replace("0",app.getCurrentOrder_price()+"");
            order_fail_info1.setText(temp);


            Spannable span1 = (Spannable) order_fail_info1.getText();
            span1.setSpan(new StyleSpan(Typeface.BOLD),order_fail_info1.getText().length()-8,order_fail_info1.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//bold처리

            app.setCurrentPayType("NULL");//현재 결제 타입 지정 해제 함으로써 pay_popup에서 자동으로 다른 결제수단 화면으로 넘어가기 방지

            retry.setText("확인");
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else if(fail_type.equals("SERVER_JSON_NORETURN"))//서버에서 json 리턴값 안줌
        {
            order_fail_reason.setText("서버 상태 불량");
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //로딩 팝업 띄움(2번 시도해도 실패 시 결제 취소완료 메시지 띄움)

                    Myapplication myapplication = (Myapplication) getApplication();
                    myapplication.setOrderRetryCount(myapplication.getOrderRetryCount()+1);

                    Intent intent2 = new Intent(Order_fail_popupActivity.this,Order_loading_popupActivity.class);
                    intent2.putExtra("fail_type",intent.getStringExtra("fail_type"));
                    startActivity(intent2);
                    finish();
                }
            });
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;

    }

}
