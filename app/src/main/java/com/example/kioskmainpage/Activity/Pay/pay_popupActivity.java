package com.example.kioskmainpage.Activity.Pay;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.Popup_emptyActivity;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

public class pay_popupActivity extends AppCompatActivity implements View.OnClickListener{

    Myapplication myapp;
//    ArrayList<SelectedMenu> selectedMenus;
    String takeOut;

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().from(this).inflate(R.layout.activity_pay_popup,null);
        setContentView(view);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //countDownReset();//카운트다운리셋

        TextView btnKaKao = (TextView)findViewById(R.id.btnKaKao);
        TextView btnZero = (TextView)findViewById(R.id.btnZero);
        TextView btnCard = (TextView)findViewById(R.id.btnCard);
        TextView btnSamsungPay = (TextView)findViewById(R.id.btnSamsungPay);
        TextView btnCancle = (TextView)findViewById(R.id.btnCancel);


        btnKaKao.setOnClickListener(this); //카카오 qr 결제버튼
        btnZero.setOnClickListener(this); //제로페이 qr 결제버튼
        btnCard.setOnClickListener(this); //카드결제버튼
        btnSamsungPay.setOnClickListener(this); //카드결제버튼
        btnCancle.setOnClickListener(this); //취소버튼

        myapp=(Myapplication) getApplication();
        String bizNum = myapp.getBizNum();

//        Intent intent_sum =getIntent();
        String summ = String.valueOf(myapp.getCurrentOrder_price());
        summ = summ.replace("원","");//원 제거
        summ = NumberFormat.getInstance().format(Integer.parseInt(summ));//쉼표표시
        TextView tv = (TextView)findViewById(R.id.sum);
        tv.setText(summ+"원");
//        takeOut = intent_sum.getStringExtra("takeout");//포장유무 값 받아옴
        //총 금액 textview에 입력

        //글자앞 이미지추가
        SpannableStringBuilder ssbCard = new SpannableStringBuilder(" \n\n신용카드");
        ssbCard.setSpan(new ImageSpan(this, R.drawable.ic_card), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssbCard.setSpan(new RelativeSizeSpan(0.3f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnCard.setText(ssbCard, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder ssbKakao = new SpannableStringBuilder(" \n\n카카오페이");
        ssbKakao.setSpan(new ImageSpan(this, R.drawable.kakaopay_resize), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssbKakao.setSpan(new RelativeSizeSpan(0.3f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnKaKao.setText(ssbKakao, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder ssbZero = new SpannableStringBuilder(" \n\n제로페이");
        ssbZero.setSpan(new ImageSpan(this, R.drawable.zeropay_resize), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssbZero.setSpan(new RelativeSizeSpan(0.3f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnZero.setText(ssbZero, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder ssbSamsung = new SpannableStringBuilder(" \n\n삼성페이");
        ssbSamsung.setSpan(new ImageSpan(this, R.drawable.ic_samsungpay), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssbSamsung.setSpan(new RelativeSizeSpan(0.3f), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnSamsungPay.setText(ssbSamsung, TextView.BufferType.SPANNABLE);

        TextView unavailable[] = new TextView[4]; //사용불가 설정용
        for(int i =0;i<4 ;i++)
        {
            unavailable[i] = (TextView)  view.findViewWithTag("pay_unavailable_"+(i+1));// 0 카드 1 삼성페이 2 제로페이 3 카카오페이
        }
        //다운로드 받은 폴더안에 QR이미지가 있는지 확인. 카카오 및 제로페이
        File qrfiles[] = new File[2];
        qrfiles[0] = new File(getFilesDir().getAbsolutePath()+"/"+myapp.getBizNum()+"/zero_pay_qr.png");
        qrfiles[1] = new File(getFilesDir().getAbsolutePath()+"/"+myapp.getBizNum()+"/kakao_pay_qr.png");
        //QR이미지가 있는지 확인.
        for(int i=0;i<qrfiles.length;i++)
        {
            if(qrfiles[i].exists()==false) {
                unavailable[i+2].setVisibility(View.VISIBLE);// 없다면 사용불가창 VISIBLE
                unavailable[i+2].bringToFront();
                unavailable[i+2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toPopup_empty = new Intent (getApplicationContext(), Popup_emptyActivity.class);
                        startActivity(toPopup_empty);
                    }
                });
            }
        }



        switch (myapp.getCurrentPayType()) { //결제실패시 각각의 결제화면이동용
            case "KaKao":
                //   this.finish();
                Intent intentKaKao = new Intent (this, KakaoPay_popupActivity.class);
                intentKaKao.putExtra("paytype","KaKao");
                Myapplication app = (Myapplication)getApplication();
                app.setCurrentPayType("KaKao");
                startActivity(intentKaKao);
                finish();
                break;
            //제로페이 클릭시에 qr_popupactivity로 전환
            case "Zero":
                //   this.finish();
                Intent intentZero = new Intent (this, Qr_popupActivity.class);
                intentZero.putExtra("paytype","Zero");
                app = (Myapplication)getApplication();
                app.setCurrentPayType("Zero");
                startActivity(intentZero);
                finish();
                break;
            //신용카드결제 클릭시 Card_popupActivity 전환
            case "Card":
                Intent intentCard = new Intent (this, Card_announce_popupActivity.class);
//                intentCard.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                app = (Myapplication)getApplication();
                app.setCurrentPayType("Card");
                intentCard.putExtra("type","Card");
                startActivity(intentCard);
                finish();
                break;
            case "SamsungPay":
                Intent intentSamsungPay = new Intent (this, Card_announce_popupActivity.class);
//                intentSamsungPay.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                app = (Myapplication)getApplication();
                app.setCurrentPayType("SAMSUNGPAY");
                intentSamsungPay.putExtra("type","SAMSUNGPAY");
                startActivity(intentSamsungPay);
                finish();
                break;
                default:
                    break;
        }

    }
    public void onClick(View v)
    {
        Myapplication app;
        switch(v.getId())
        {
            //카카오페이 클릭시에 qr_popupactivity로 전환
            case R.id.btnKaKao:
                Intent intentKaKao = new Intent (this, KakaoPay_popupActivity.class);
                intentKaKao.putExtra("paytype","KaKao");
                app = (Myapplication)getApplication();
                app.setCurrentPayType("KaKao");
                startActivity(intentKaKao);
                finish();
                break;
            //제로페이 클릭시에 qr_popupactivity로 전환
            case R.id.btnZero:
                //   this.finish();
                Intent intentZero = new Intent (this, Qr_popupActivity.class);
                intentZero.putExtra("paytype","Zero");
                app = (Myapplication)getApplication();
                app.setCurrentPayType("Zero");
                startActivity(intentZero);
                finish();
                break;
            //신용카드결제 클릭시 Card_popupActivity 전환
            case R.id.btnCard:
                Intent intentCard = new Intent (this, Card_announce_popupActivity.class);
//                intentCard.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                app = (Myapplication)getApplication();
                app.setCurrentPayType("Card");

                //소리재생
                mediaPlayer = MediaPlayer.create(pay_popupActivity.this, R.raw.eventually);
                mediaPlayer.start();

                intentCard.putExtra("type","Card");
                startActivity(intentCard);
                finish();
                break;
            case R.id.btnSamsungPay:
                Intent intentSamsungPay = new Intent (this, Card_announce_popupActivity.class);
//                intentSamsungPay.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                app = (Myapplication)getApplication();
                app.setCurrentPayType("SAMSUNGPAY");

                //소리재생
                mediaPlayer = MediaPlayer.create(pay_popupActivity.this, R.raw.eventually);
                mediaPlayer.start();

                intentSamsungPay.putExtra("type","SAMSUNGPAY");
                startActivity(intentSamsungPay);
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
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
