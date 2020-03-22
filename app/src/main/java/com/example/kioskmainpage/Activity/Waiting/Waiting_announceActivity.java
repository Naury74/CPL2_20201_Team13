package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.R;

public class Waiting_announceActivity extends AppCompatActivity implements View.OnClickListener{
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_announce);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button waiting_announce_btnFinish = (Button)findViewById(R.id.waiting_announce_btnFinish);
        waiting_announce_btnFinish.setOnClickListener(this);

        TextView waiting_announce_title = (TextView)findViewById(R.id.waiting_announce_title);
        Intent paytypeData = getIntent() ;
        type  = paytypeData.getStringExtra("type"); //order와 nopay
        switch (type)
        {
            case "nopay":
                LinearLayout waiting_announce_nopay_layout = (LinearLayout)findViewById(R.id.waiting_announce_nopay_layout);
                waiting_announce_nopay_layout.setVisibility(View.VISIBLE);
            break;
            case "order":
                LinearLayout waiting_announce_order_layout = (LinearLayout)findViewById(R.id.waiting_announce_order_layout);
                waiting_announce_order_layout.setVisibility(View.VISIBLE);
                waiting_announce_title.setText("번호표 주문");
                break;
        }
    }

    public void onClick(View v) {
        switch(v.getId())
        {
            //확인 버튼
            case R.id.waiting_announce_btnFinish:
                switch (type)
                {
                    case "nopay"://비결제 웨이팅일시
                        finish();
                        break;
                    case "order"://웨이팅 주문일시 메인메뉴로가게 만들어야함
                        finish();
                        break;
                }

        }
    }

    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
