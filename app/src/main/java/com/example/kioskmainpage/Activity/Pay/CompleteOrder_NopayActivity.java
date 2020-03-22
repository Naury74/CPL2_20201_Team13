package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class CompleteOrder_NopayActivity extends AppCompatActivity implements View.OnClickListener {
    public int exN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order__nopay);
   //     countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //전역변수 주문번호 받음
        Myapplication app=(Myapplication) getApplication();
        exN=app.getOrderNum()+1;
        app.setOrderNum(exN);

        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);

        //주문번호출력
        TextView order_num = (TextView)findViewById(R.id.order_num);
        order_num.setText(String.valueOf(exN));

    }

    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish://주문완료

                finish();


                break;
        }
    }
}
