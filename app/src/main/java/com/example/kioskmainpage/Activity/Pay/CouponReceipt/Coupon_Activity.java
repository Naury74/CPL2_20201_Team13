package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

public class Coupon_Activity extends AppCompatActivity implements View.OnClickListener{
    int type;
    public int exN;
    EditText phonenumber;
    TextView[] numberPad = new TextView[10];
    ImageButton numberPad2;

    ArrayList<SelectedMenu> selectedMenus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_coupon,null);
        setContentView(view);
    //    countDownReset();//카운트다운리셋

        //무엇을 위한 화면인가?
        Intent dataforloading = getIntent() ;
        type  = dataforloading.getIntExtra("type",-1); //21 쿠폰 22 영수증

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        TextView coupon_activity = (TextView)findViewById(R.id.coupon_TextView_title);
        Spannable span_samsungpay_textView2 = (Spannable) coupon_activity.getText();
        span_samsungpay_textView2.setSpan(new StyleSpan(Typeface.BOLD), 0,  6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span_samsungpay_textView2.setSpan(new StyleSpan(Typeface.BOLD), 11,  20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리

        phonenumber = (EditText) findViewById(R.id.phonenumber_edit);

        Button btnFinish = (Button) findViewById(R.id.btnFinish_coupon);
        Button btnCancel = (Button) findViewById(R.id.btnCancel_coupon);
        btnCancel.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        for(int i=0;i<=9;i++) {
            numberPad[i] = (TextView) view.findViewWithTag("numberPad_" + i);
            numberPad[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = phonenumber.getText().toString();
                    System.out.println( ((TextView)view).getText().toString() );
                    number += ((TextView)view).getText().toString();
                    phonenumber.setText(number);

                }
            });
        }
        numberPad2 = (ImageButton) view.findViewWithTag("numberPad_10");//삭제버튼
            numberPad2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = phonenumber.getText().toString();
                    if (!number.equals("")) {
                        phonenumber.setText(number.substring(0, number.length() - 1));
                    }
                }
            });
        //전역변수 주문번호 받음
        Myapplication app=(Myapplication) getApplication();
        exN=app.getOrderNum();

        if(type == 22) //영수증일때
        {
            TextView title = (TextView)findViewById(R.id.coupon_TextView_title);
            ImageView imageV = (ImageView)findViewById(R.id.coupon_imageView);
            imageV.setImageResource(R.drawable.receipt_icon);
            title.setText("전자영수증 발행을 위해\n본인 휴대폰 번호를 입력해주세요.\n");
        }


    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish_coupon://주문완료
                /*TODO:전화번호 서버에서 확인 후 적립*/

                if(phonenumber.getText().toString().length()==11 || phonenumber.getText().toString().length() == 10) {
                    /*TODO:전화번호 서버에서 확인 후 적립*/

                    Log.i("asdasd", "폰번호: " + phonenumber.getText().toString());
                    /*TODO:적립로딩띄우기*/
                    //TODO : 주문번호 서버에서 받아오고 실패 시 Order_fail액티비티 띄움
                    switch (type) {
                        case 21:
                            Intent intent = new Intent(this, CouponAndReceiptLoadingPopupActivity.class);
                            intent.putExtra("type", type);//21 = 쿠폰로딩 22 = 영수증로딩
                            intent.putExtra("phonenumber", phonenumber.getText().toString());
                            startActivity(intent);
                            break;
                        case 22:
                            Intent intent2 = new Intent(this, ReceiptPopupActivity.class);
                            intent2.putExtra("phonenumber", phonenumber.getText().toString());
                            startActivity(intent2);
                            break;
                    }
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel_coupon:
                switch (type) {//21 = 쿠폰로딩 22 = 영수증로딩
                    case 21:
                        Intent intent = new Intent(this, CouponAndReceipt_announce_Activity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("phonenumber", phonenumber.getText().toString());
                        startActivity(intent);
                        break;
                    case 22:
                        Intent intent2 = new Intent(this, CouponAndReceipt_announce_Activity.class);
                        intent2.putExtra("type", type);
                        intent2.putExtra("phonenumber", phonenumber.getText().toString());
                        startActivity(intent2);
                        break;
                }
                finish();
                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
