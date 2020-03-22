package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.Adapter.SelectedListAdapter;//나중에제거

import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

public class CompleteOrder_PayActivity extends AppCompatActivity implements View.OnClickListener{
    public int OrderNum;
    public int WaitingNum;
    public int type;
    ArrayList<SelectedMenu> selectedMenus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent dataforloading = getIntent() ;
        type  = dataforloading.getIntExtra("type",-1); //2 영수증
        String phonenumber  = dataforloading.getStringExtra("phonenumber"); //전화번호를가져옴
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order__pay);
  //      countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //TODO: 주문번호와 웨이팅인원은 서버로부터 받어야 하며 받게 될경우 아래 전역변수 코드는 제거되어야함
        //전역변수 주문번호 및 웨이팅인원 받음
        Myapplication app=(Myapplication) getApplication();
        OrderNum=app.getOrderNum();
        WaitingNum=app.getWaitingNum();
        app.setWaitingNum(WaitingNum);
        app.setOrderNum(WaitingNum);


        Button btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        //주문번호 및 대기인원 출력
        TextView order_num = (TextView)findViewById(R.id.order_num);
        order_num.setText(String.valueOf(OrderNum));
//        TextView order_waiting = (TextView)findViewById(R.id.order_waiting);
//        order_waiting.setText("대기인원 : "+String.valueOf(WaitingNum)+"명");
        //대기인원 입력 삭제

        TextView text_announce = (TextView) findViewById(R.id.text_announce);
        TextView title_order_complete = (TextView) findViewById(R.id.title_order_complete);
        ImageView circle = (ImageView)findViewById(R.id.circle);
        ImageView circle2 = (ImageView)findViewById(R.id.circle2);

                switch (app.getCurrentPayType())
        {
            case "KaKao":
                circle.setImageResource(R.drawable.shape_kakao);
                circle2.setImageResource(R.drawable.shape_kakao2);
                break;
            case "Zero":
                circle.setImageResource(R.drawable.shape_zero);
                circle2.setImageResource(R.drawable.shape_zero2);
                break;
            case "Card":
                circle.setImageResource(R.drawable.shape_card);
                circle2.setImageResource(R.drawable.shape_card2);
                break;
            case "SAMSUNGPAY":
                circle.setImageResource(R.drawable.shape_samsung);
                circle2.setImageResource(R.drawable.shape_samsung2);
                break;
        }

        if(type==2) { //영수증이면
            title_order_complete.setText("발행이 완료되었습니다");
            if(phonenumber.length() == 11)
                text_announce.setText("전자영수증이\n"+(phonenumber.substring(0,3)+"-****-"+phonenumber.substring(7,11))+" 번으로\n전송되었습니다.\n");// 전화번호가져와서 넣기
            else if(phonenumber.length() == 10)
                text_announce.setText("전자영수증이\n"+(phonenumber.substring(0,3)+"-***-"+phonenumber.substring(6,10))+" 번으로\n전송되었습니다.\n");
//            SpannableStringBuilder waitingicon_red = new SpannableStringBuilder("  "+order_waiting.getText());
//            waitingicon_red.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rounded_red)), 0, order_waiting.length()+2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리 - 대기인원
//            waitingicon_red.setSpan(new ImageSpan(this, R.drawable.waiting_person_icon_red), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            order_waiting.setText(waitingicon_red, TextView.BufferType.SPANNABLE);
        }
        else {
//            Spannable span4 = (Spannable) text_announce.getText();
//            span4.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rounded_red)), 0, text_announce.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리 - 안내부분

            //대기인원 아이콘 설정
//            SpannableStringBuilder waitingicon_black = new SpannableStringBuilder("  "+order_waiting.getText());
//            waitingicon_black.setSpan(new ImageSpan(this, R.drawable.waiting_person_icon_black), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            order_waiting.setText(waitingicon_black, TextView.BufferType.SPANNABLE);

        }
        app.setCurrentPayType("NULL");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish://주문완료
                SelectedListAdapter adapter;//나중에제거
                Myapplication myapp=(Myapplication) getApplication();//나중에제거
                adapter = new SelectedListAdapter(myapp.getadapter().getselectedMenus(), CompleteOrder_PayActivity.this,"PayingPopupActivity");//나중에제거
                adapter.clearItem(adapter);//나중에제거
                finish();
                break;
//            case R.id.Button_tap:
//                Intent intent = new Intent(this,ReceiptPopupActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
