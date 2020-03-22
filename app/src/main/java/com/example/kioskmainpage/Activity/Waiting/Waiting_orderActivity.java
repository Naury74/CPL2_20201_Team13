package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kioskmainpage.R;

public class Waiting_orderActivity extends AppCompatActivity implements View.OnClickListener{

    EditText order_waitingNum;
    ImageButton numberPad2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_order);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button waiting_order_btnCancel = (Button)findViewById(R.id.waiting_order_btnCancel);
        waiting_order_btnCancel.setOnClickListener(this);
        Button waiting_order_btnFinish = (Button)findViewById(R.id.waiting_order_btnFinish);
        waiting_order_btnFinish.setOnClickListener(this);

        order_waitingNum = (EditText) findViewById(R.id.order_waitingNum);
        numberPad2 = (ImageButton)findViewById(R.id.ordernumberPad_10);//삭제버튼
        numberPad2.setOnClickListener(this);
        TextView numpad_0 = (TextView)findViewById(R.id.ordernumberPad_0);
        TextView numpad_1 = (TextView)findViewById(R.id.ordernumberPad_1);
        TextView numpad_2 = (TextView)findViewById(R.id.ordernumberPad_2);
        TextView numpad_3 = (TextView)findViewById(R.id.ordernumberPad_3);
        TextView numpad_4 = (TextView)findViewById(R.id.ordernumberPad_4);
        TextView numpad_5 = (TextView)findViewById(R.id.ordernumberPad_5);
        TextView numpad_6 = (TextView)findViewById(R.id.ordernumberPad_6);
        TextView numpad_7 = (TextView)findViewById(R.id.ordernumberPad_7);
        TextView numpad_8 = (TextView)findViewById(R.id.ordernumberPad_8);
        TextView numpad_9 = (TextView)findViewById(R.id.ordernumberPad_9);
        numpad_0.setOnClickListener(this);
        numpad_1.setOnClickListener(this);
        numpad_2.setOnClickListener(this);
        numpad_3.setOnClickListener(this);
        numpad_4.setOnClickListener(this);
        numpad_5.setOnClickListener(this);
        numpad_6.setOnClickListener(this);
        numpad_7.setOnClickListener(this);
        numpad_8.setOnClickListener(this);
        numpad_9.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch(v.getId())
        {
            //취소 버튼
            case R.id.waiting_order_btnCancel:
                finish();
                break;
            //확인 버튼
            case R.id.waiting_order_btnFinish:
                Intent intent_announce = new Intent (this, Waiting_announceActivity.class);

                //서버에서 현재 대기순서가 맞는지 확인후 결과리턴 리턴결과가 true일경우에 밑의 코드 진행.

                intent_announce.putExtra("type","order");
                setResult(RESULT_OK, intent_announce); //설정했다고 알림
                startActivity(intent_announce);
                finish();
                break;
                //숫자패드
            case R.id.ordernumberPad_0:
            case R.id.ordernumberPad_1:
            case R.id.ordernumberPad_2:
            case R.id.ordernumberPad_3:
            case R.id.ordernumberPad_4:
            case R.id.ordernumberPad_5:
            case R.id.ordernumberPad_6:
            case R.id.ordernumberPad_7:
            case R.id.ordernumberPad_8:
            case R.id.ordernumberPad_9:
                String number = order_waitingNum.getText().toString();
                System.out.println( ((TextView)order_waitingNum).getText().toString() );
                number += ((TextView)findViewById(v.getId())).getText().toString();
                order_waitingNum.setText(number);
                break;
            case R.id.ordernumberPad_10:
                String num = order_waitingNum.getText().toString();
                if (!num.equals("")) {
                    order_waitingNum.setText(num.substring(0, num.length() - 1));
                }
                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
