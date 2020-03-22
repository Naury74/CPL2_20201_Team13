package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.R;

public class Waiting_nopayActivity extends AppCompatActivity implements View.OnClickListener {

    EditText phonenumber;
    ImageButton numberPad2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_waiting_nopay,null);
        setContentView(view);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        phonenumber = (EditText) findViewById(R.id.phonenumber_edit2);

        Button Waiting_nopay_btnCancel = (Button)findViewById(R.id.Waiting_nopay_btnCancel);
        Waiting_nopay_btnCancel.setOnClickListener(this);
        Button Waiting_nopay_btnFinish = (Button)findViewById(R.id.Waiting_nopay_btnFinish);
        Waiting_nopay_btnFinish.setOnClickListener(this);

        numberPad2 = (ImageButton)findViewById(R.id.nopaynumberPad_10);//삭제버튼
        numberPad2.setOnClickListener(this);
        TextView numpad_0 = (TextView)findViewById(R.id.nopaynumberPad_0);
        TextView numpad_1 = (TextView)findViewById(R.id.nopaynumberPad_1);
        TextView numpad_2 = (TextView)findViewById(R.id.nopaynumberPad_2);
        TextView numpad_3 = (TextView)findViewById(R.id.nopaynumberPad_3);
        TextView numpad_4 = (TextView)findViewById(R.id.nopaynumberPad_4);
        TextView numpad_5 = (TextView)findViewById(R.id.nopaynumberPad_5);
        TextView numpad_6 = (TextView)findViewById(R.id.nopaynumberPad_6);
        TextView numpad_7 = (TextView)findViewById(R.id.nopaynumberPad_7);
        TextView numpad_8 = (TextView)findViewById(R.id.nopaynumberPad_8);
        TextView numpad_9 = (TextView)findViewById(R.id.nopaynumberPad_9);
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
        switch(v.getId()) {
            //취소 버튼
            case R.id.Waiting_nopay_btnCancel:
                finish();
                break;
            //확인 버튼
            case R.id.Waiting_nopay_btnFinish:
                if(phonenumber.getText().toString().length()==11) {
                    Intent intent_announce = new Intent(this, Waiting_announceActivity.class);
                    intent_announce.putExtra("type", "nopay");
                    //
                    //TODO : 서버로 전화번호를 전송하고 서버는 대기번호를 부여하여야함
                    //
                    startActivity(intent_announce);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nopaynumberPad_0:
            case R.id.nopaynumberPad_1:
            case R.id.nopaynumberPad_2:
            case R.id.nopaynumberPad_3:
            case R.id.nopaynumberPad_4:
            case R.id.nopaynumberPad_5:
            case R.id.nopaynumberPad_6:
            case R.id.nopaynumberPad_7:
            case R.id.nopaynumberPad_8:
            case R.id.nopaynumberPad_9:
                String number = phonenumber.getText().toString();
                System.out.println( ((TextView)phonenumber).getText().toString() );
                number += ((TextView)findViewById(v.getId())).getText().toString();
                phonenumber.setText(number);
                break;
            case R.id.nopaynumberPad_10:
                String num = phonenumber.getText().toString();
                if (!num.equals("")) {
                    phonenumber.setText(num.substring(0, num.length() - 1));
                }
                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
