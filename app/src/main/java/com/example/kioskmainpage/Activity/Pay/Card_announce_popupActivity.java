package com.example.kioskmainpage.Activity.Pay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class  Card_announce_popupActivity extends AppCompatActivity implements View.OnClickListener{
    MediaPlayer mediaPlayer;
    ImageView card_announce_iv;

    AnimationDrawable samsungpay_animation,card_animation;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        samsungpay_animation.start();
        card_animation.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_announce_popup);
  //      countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button btnnext = (Button) findViewById(R.id.Card_announce_btn_next);
        btnnext.setOnClickListener(this);
        Button btncancel = (Button) findViewById(R.id.Card_announce_btn_cancel);
        btncancel.setOnClickListener(this);
        Button Card_announce_samsungpay_next = (Button) findViewById(R.id.Card_announce_samsungpay_next);
        Card_announce_samsungpay_next.setOnClickListener(this);
        Button Card_announce_samsungpay_btn_cancel = (Button) findViewById(R.id.Card_announce_samsungpay_btn_cancel);
        Card_announce_samsungpay_btn_cancel.setOnClickListener(this);

        TextView Card_announce_textView2 = (TextView)findViewById(R.id.Card_announce_textView2);
        Card_announce_textView2.setText("카드를 카드 입구에 끝까지 삽입하고\n'결제 진행' 버튼을 눌러주세요.");
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span2 = (Spannable) Card_announce_textView2.getText();
        span2.setSpan(new StyleSpan(Typeface.BOLD), 11,  14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//        span2.setSpan(new ForegroundColorSpan(rounded_red), 11, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span2.setSpan(new StyleSpan(Typeface.BOLD), 20,  27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//        span2.setSpan(new ForegroundColorSpan(rounded_red), 20, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리

        Intent datafrompay = getIntent() ;
        String type  = datafrompay.getStringExtra("type"); //Samsung일시에만

        LinearLayout Card_announce_card_layout = (LinearLayout)findViewById(R.id.Card_announce_card_layout);
        Card_announce_card_layout.setVisibility(View.VISIBLE);
        LinearLayout Card_announce_samsungpay_layout = (LinearLayout)findViewById(R.id.Card_announce_samsungpay_layout);
        Card_announce_samsungpay_layout.setVisibility(View.GONE);
        Log.i("typetypetypetypetype", "onCreate: "+type);
        if(type.matches("SAMSUNGPAY")) //카드가 아닌 삼성페이로 접근시
        {
            Card_announce_card_layout.setVisibility(View.GONE);
            Card_announce_samsungpay_layout.setVisibility(View.VISIBLE);
            TextView card_announce_samsungpay_textView2 = (TextView)findViewById(R.id.Card_announce_samsungpay_textView2);
            TextView card_announce_samsungpay_textView3 = (TextView)findViewById(R.id.card_announce_samsungpay_textView3);
            Spannable span_samsungpay_textView2 = (Spannable) card_announce_samsungpay_textView2.getText();
            span_samsungpay_textView2.setSpan(new StyleSpan(Typeface.BOLD), 0,  7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//            span_samsungpay_textView2.setSpan(new ForegroundColorSpan(rounded_red), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
            span_samsungpay_textView2.setSpan(new StyleSpan(Typeface.BOLD), 45,  47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//            span_samsungpay_textView2.setSpan(new ForegroundColorSpan(rounded_red), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
            Spannable span_samsungpay_textView3 = (Spannable) card_announce_samsungpay_textView3.getText();
            span_samsungpay_textView3.setSpan(new StyleSpan(Typeface.BOLD), 28,  30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//            span_samsungpay_textView3.setSpan(new ForegroundColorSpan(rounded_red), 28, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
            card_announce_samsungpay_textView3.setVisibility(View.GONE);
        }
        Handler mHandler;//안내 음성을 위한 핸들러
        switch (type)
        {
            case "SAMSUNGPAY" :
                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        //소리재생
                        mediaPlayer = MediaPlayer.create(Card_announce_popupActivity.this, R.raw.samsungpay_announce_insert);
                        mediaPlayer.start();
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,1000);//1초느리게 나오게하는 핸들러
                break;
            case "Card":
                mHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        //소리재생
                        mediaPlayer = MediaPlayer.create(Card_announce_popupActivity.this, R.raw.card_announce_insert);
                        mediaPlayer.start();
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,1000);//1초느리게 나오게하는 핸들러
                break;
        }


        //gif설정
        card_announce_iv = (ImageView) findViewById(R.id.Card_announce_imageView);
        card_announce_iv.setImageResource(R.drawable.card_animation);
        card_animation = (AnimationDrawable) card_announce_iv.getDrawable();
//        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(card_announce_iv);
//        Glide.with(this).load(R.drawable.card_payment_gif).into(gifImage);


        ImageView Card_announce_samsungpay_imageView = (ImageView) findViewById(R.id.Card_announce_samsungpay_imageView);
        Card_announce_samsungpay_imageView.setImageResource((R.drawable.samsungpay_animation));
        samsungpay_animation = (AnimationDrawable)  Card_announce_samsungpay_imageView.getDrawable();
//        GlideDrawableImageViewTarget gifImage2 = new GlideDrawableImageViewTarget(Card_announce_samsungpay_imageView);
//        Glide.with(this).load(R.drawable.samsungpay_payment_gif).into(gifImage2);




        if(type.equals("SAMSUNGPAY")){
            Handler mHandler1 = new Handler() {
                public void handleMessage(Message msg) {
                    if(mHandler_status) {
                        super.handleMessage(msg);
                        Intent intent = new Intent(Card_announce_popupActivity.this, Card_popupActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            //결제중 결제진행 버튼이 사라짐.
            Card_announce_samsungpay_next.setVisibility(View.GONE);
            TextView card_announce_samsungpay_textView3 = (TextView)findViewById(R.id.card_announce_samsungpay_textView3);
            card_announce_samsungpay_textView3.setVisibility(View.VISIBLE);
            TextView card_announce_samsungpay_textView2 = (TextView)findViewById(R.id.Card_announce_samsungpay_textView2);
            card_announce_samsungpay_textView2.setVisibility(View.GONE);
            Card_announce_samsungpay_btn_cancel.setBackground(getDrawable(R.drawable.round_button_c1c1c1));
            mHandler1.sendEmptyMessageDelayed(0,5000);
            //결제중입니다 5초 delay
        }

    }
    boolean mHandler_status = true;
    public void set_mHandler_status (boolean bool) {
        mHandler_status = bool;
    }

    @Override
    public void onClick(View v) {
        //결제타입초기화
        Myapplication myapp=(Myapplication) getApplication();
        Button Card_announce_samsungpay_next = (Button) findViewById(R.id.Card_announce_samsungpay_next);
        Button Card_announce_samsungpay_btn_cancel = (Button) findViewById(R.id.Card_announce_samsungpay_btn_cancel);
        switch (v.getId()) {
            case R.id.Card_announce_btn_next:
                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                if(mWifi == null)//인터넷 연결 x
                {
                    Toast.makeText(this,"인터넷을 연결해주십시오",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(this,Card_popupActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.Card_announce_samsungpay_next:
//                //결제중 결제진행 버튼이 사라짐.
//                Card_announce_samsungpay_next.setVisibility(View.GONE);
//                TextView card_announce_samsungpay_textView3 = (TextView)findViewById(R.id.card_announce_samsungpay_textView3);
//                card_announce_samsungpay_textView3.setVisibility(View.VISIBLE);
//                TextView card_announce_samsungpay_textView2 = (TextView)findViewById(R.id.Card_announce_samsungpay_textView2);
//                card_announce_samsungpay_textView2.setVisibility(View.GONE);
//                Card_announce_samsungpay_btn_cancel.setBackground(getDrawable(R.drawable.round_button_c1c1c1));
//                mHandler.sendEmptyMessageDelayed(0,5000);
//                //결제중입니다 3초 delay
                break;
            case R.id.Card_announce_btn_cancel:
                myapp.setCurrentPayType("NULL");
                finish();
                break;
            case R.id.Card_announce_samsungpay_btn_cancel:
                //결제타입초기화
                set_mHandler_status(false);
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

    public void start_animation() {

    }
}
