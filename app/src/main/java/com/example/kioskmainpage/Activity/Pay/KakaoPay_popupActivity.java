package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class KakaoPay_popupActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView scan_imageGIF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay_popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button btnnext = (Button) findViewById(R.id.kakaopay_announce_btn_next);
        btnnext.setOnClickListener(this);
        Button btncancel = (Button) findViewById(R.id.kakaopay_announce_btn_cancel);
        btncancel.setOnClickListener(this);


        TextView kakaopay_announce_textView2 = (TextView)findViewById(R.id.kakaopay_announce_textView2);
        kakaopay_announce_textView2.setText("'결제 진행' 버튼을 누른 후 ,\n매장 결제 코드를 띄운 스마트폰을\n기기 하단 카메라에 비춰주세요 ");
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span2 = (Spannable) kakaopay_announce_textView2.getText();
        span2.setSpan(new StyleSpan(Typeface.BOLD), 0,  7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span2.setSpan(new ForegroundColorSpan(rounded_red), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span2.setSpan(new StyleSpan(Typeface.BOLD), 49,  54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span2.setSpan(new ForegroundColorSpan(rounded_red), 49, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리

        scan_imageGIF = (ImageView)findViewById(R.id.kakaopay_announce_imageView);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(scan_imageGIF);
        Glide.with(this).load(R.drawable.kakaopay_gif).into(gifImage);
    }

    @Override
    public void onClick(View v) {
        Myapplication myapp=(Myapplication) getApplication();
        switch (v.getId()) {
            case R.id.kakaopay_announce_btn_next:
                Intent intent = new Intent(this,QR_ScannerActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.kakaopay_announce_btn_cancel:
                myapp.setCurrentPayType("NULL");
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
