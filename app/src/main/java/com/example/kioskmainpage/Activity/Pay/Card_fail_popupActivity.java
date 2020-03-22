package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.Pay.pay_popupActivity;
import com.example.kioskmainpage.CardReceiptInfo;
import com.example.kioskmainpage.KakaoPayReceiptInfo;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

public class Card_fail_popupActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_fail_popup);

   //     countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Myapplication app = (Myapplication)getApplication();

        Button card_fail_btn_cancel = (Button) findViewById(R.id.card_fail_btn_cancel);
        card_fail_btn_cancel.setOnClickListener(this);
        Button card_fail_btn2_retry = (Button) findViewById(R.id.card_fail_btn2_retry);
        card_fail_btn2_retry.setOnClickListener(this);

        if(type.equals("Card")) //카드결제일 때
        {
            CardReceiptInfo cardReceiptInfo = app.getCurrentCardReceiptInfo();
            TextView fail_reason = (TextView) findViewById(R.id.card_fail_reason_textview);
            fail_reason.setText(cardReceiptInfo.getMessage1());
            Spannable span4 = (Spannable) fail_reason.getText();
            span4.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rounded_red)), 0, fail_reason.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리 - 안내부분
        }
        else if(type.equals("KaKao"))//카카오페이일 때
        {
            KakaoPayReceiptInfo kakaoPayReceiptInfo = app.getCurrentKakaoPayReceiptInfo();
            TextView fail_reason = (TextView) findViewById(R.id.card_fail_reason_textview);

            int result_type = intent.getIntExtra("fail_type",0);//실패 사유

            if (result_type <= -2000 && result_type >= -2099)//통신장애
                fail_reason.setText("통신장애 발생");
            else if (result_type == -1102)
                fail_reason.setText("요청전문이 짧습니다");
            else if (result_type == -1103)
                fail_reason.setText("IP 번호 미입력");
            else if (result_type == -1104)
                fail_reason.setText("포트 번호 미입력");
            else if (result_type == -1105)
                fail_reason.setText("통신 타입 미입력");
            else if (result_type == -1106)
                fail_reason.setText("Sign data 없음");
            else if (result_type == -3001)
                fail_reason.setText("가맹점 다운로드 필요");
            else if (result_type == -3002)
                fail_reason.setText("카드 리딩 실패");
            else if (result_type >= 1)
                fail_reason.setText("승인 거절");
            else
                fail_reason.setText(kakaoPayReceiptInfo.getR20());

            Spannable span4 = (Spannable) fail_reason.getText();
            span4.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rounded_red)), 0, fail_reason.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.card_fail_btn_cancel:
                //결제타입초기화
                Myapplication myapp=(Myapplication) getApplication();
                myapp.setCurrentPayType("NULL");
                finish();
                break;
            case R.id.card_fail_btn2_retry:
                intent = new Intent(this, pay_popupActivity.class);
                Myapplication app = (Myapplication)getApplication();
                ArrayList<SelectedMenu> selectedMenus = app.getadapter().getselectedMenus();
                int sum=0;
                for(int i=0;i<selectedMenus.size();i++)
                {
                    int price = selectedMenus.get(i).getCnt() * Integer.parseInt(selectedMenus.get(i).getMenu_price());
                    sum += price;
                }
                intent.putExtra("sum",String.valueOf(sum)+"원");
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
