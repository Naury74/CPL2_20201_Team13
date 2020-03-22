package com.example.kioskmainpage.Activity.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

public class Admin_userActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton admin_user_btn_edit;
    LinearLayout admin_user_edit, admin_user_nonedit,admin_user_edit_btns;
    //원본
    TextView admin_bizname_text,admin_biznnum_text,admin_bizname_edit_text,admin_biznnum_edit_text,
            admin_password_text, admin_email_text,
            admin_phonenum_text,admin_address_text,admin_device_num_text;
    TextView password_check_textview;
    //수정항목
    EditText admin_password_edit_text,admin_password_edit_confirm_text,
            admin_email_edit_text,admin_phonenum_edit_text,admin_address_edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        //위아래 바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //버튼 및 클릭 리스너
        ImageButton admin_user_btn_left = (ImageButton)findViewById(R.id.admin_user_btn_left);// 나가기
        Button admin_edit_cancel_btn = (Button)findViewById(R.id.admin_edit_cancel_btn);// 수정 - 취소
        Button admin_edit_confirm_btn = (Button)findViewById(R.id.admin_edit_confirm_btn);//수정 - 확인
        admin_user_btn_edit = (ImageButton)findViewById(R.id.admin_user_btn_edit); // 수정 이미지버튼
        admin_edit_cancel_btn.setOnClickListener(this);
        admin_edit_confirm_btn.setOnClickListener(this);
        admin_user_btn_edit.setOnClickListener(this);
        admin_user_btn_left.setOnClickListener(this);

        Myapplication myapp=(Myapplication) getApplication();
        ArrayList<String> userdata = myapp.getUser_data(); //mb_name,mb_id,mb_email,mb_hp,mb_addr 순서
        //원본
        admin_bizname_text = (TextView)findViewById(R.id.admin_bizname_text);
        admin_biznnum_text = (TextView)findViewById(R.id.admin_biznnum_text);
        admin_password_text = (TextView)findViewById(R.id.admin_password_text);
        admin_email_text = (TextView)findViewById(R.id.admin_email_text);
        admin_phonenum_text = (TextView)findViewById(R.id.admin_phonenum_text);
        admin_address_text = (TextView)findViewById(R.id.admin_address_text);
        admin_device_num_text = (TextView)findViewById(R.id.admin_device_num_text);
        if(userdata.size()>4){
            admin_bizname_text.setText(userdata.get(0));
            admin_biznnum_text.setText(userdata.get(1));
            admin_email_text.setText(userdata.get(2));
            admin_phonenum_text.setText(userdata.get(3));
            admin_address_text.setText(userdata.get(4));
            admin_device_num_text.setText(myapp.getDeviceNo());
        }
        //수정항목
        admin_bizname_edit_text = (TextView)findViewById(R.id.admin_bizname_edit_text);
        admin_biznnum_edit_text = (TextView)findViewById(R.id.admin_biznnum_edit_text);
        admin_biznnum_edit_text.setOnClickListener(this);//클릭시 Toast메세지 출력
        admin_password_edit_text = (EditText)findViewById(R.id.admin_password_edit_text);
        admin_password_edit_confirm_text = (EditText)findViewById(R.id.admin_password_edit_confirm_text);
        admin_email_edit_text = (EditText)findViewById(R.id.admin_email_edit_text);
        admin_phonenum_edit_text = (EditText)findViewById(R.id.admin_phonenum_edit_text);
        admin_address_edit_text = (EditText)findViewById(R.id.admin_address_edit_text);
        password_check_textview = (TextView)findViewById(R.id.password_check_textview);
        //레이아웃들
        admin_user_edit = (LinearLayout)findViewById(R.id.admin_user_edit);
        admin_user_nonedit = (LinearLayout)findViewById(R.id.admin_user_nonedit);
        admin_user_edit_btns = (LinearLayout)findViewById(R.id.admin_user_edit_btns);


        //비밀번호확인
        //비밀번호확인을 위한 Textwatcher 객체
        TextWatcher TextWatcherForPassword = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //파란색
                int admin_confirm = getResources().getColor(R.color.admin_confirm);
                //비밀번호 7자리~11자리가 입력되었을때만
                if((admin_password_edit_text.getText().toString().length()>6)&(admin_password_edit_text.getText().toString().length()<12)) {
                    //비밀번호가 서로 같은지 확인후 text 및 색 설정
                    if ((admin_password_edit_confirm_text.getText().toString()).equals(admin_password_edit_text.getText().toString())) {
                        password_check_textview.setText("비밀번호가 일치합니다");
                        Spannable span4 = (Spannable) password_check_textview.getText();
                        span4.setSpan(new ForegroundColorSpan(admin_confirm), 0, password_check_textview.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
                    } else
                        password_check_textview.setText("비밀번호가 일치하지 않습니다");
                }
                if(admin_password_edit_text.getText().toString().length()<7)
                    password_check_textview.setText("최소 7글자를 입력해주세요.");//
                if(admin_password_edit_text.getText().toString().length()>11)
                    password_check_textview.setText("최대 11글자를 입력해주세요.");//
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        };

        admin_password_edit_text.addTextChangedListener(TextWatcherForPassword);
        admin_password_edit_confirm_text.addTextChangedListener(TextWatcherForPassword);

        //TODO 서버로 부터 데이터 요청 추가되어야하는 부분
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.admin_user_btn_left:// 나가기
                finish();
                break;
            case R.id.admin_user_btn_edit:// 수정진입 버튼
                //레이아웃 구성의 VIsivillity수정
                admin_user_btn_edit.setVisibility(View.INVISIBLE);
                admin_user_nonedit.setVisibility(View.GONE);
                admin_user_edit.setVisibility(View.VISIBLE);
                admin_user_edit_btns.setVisibility(View.VISIBLE);

                //원본으로부터 값을 받아옴
                admin_bizname_edit_text.setText(admin_bizname_text.getText());
                admin_biznnum_edit_text.setText(admin_biznnum_text.getText());
                admin_password_edit_text.setText(admin_password_text.getText());
                admin_password_edit_confirm_text.setText(admin_password_text.getText());
                admin_email_edit_text.setText(admin_email_text.getText());
                admin_phonenum_edit_text.setText(admin_phonenum_text.getText());
                admin_address_edit_text.setText(admin_address_text.getText());

                break;
            case R.id.admin_edit_cancel_btn:// 수정 - 취소
                //레이아웃 구성의 VIsivillity수정
                admin_user_btn_edit.setVisibility(View.VISIBLE);
                admin_user_nonedit.setVisibility(View.VISIBLE);
                admin_user_edit.setVisibility(View.GONE);
                admin_user_edit_btns.setVisibility(View.GONE);
                break;

            case R.id.admin_edit_confirm_btn://수정 - 확인
                //비밀번호가 서로 맞나 확인
                if((admin_password_edit_confirm_text.getText().toString()).equals(admin_password_edit_text.getText().toString())&(admin_password_edit_text.getText().length()>6)){
                    //레이아웃 구성의 VIsivillity수정
                    admin_user_btn_edit.setVisibility(View.VISIBLE);
                    admin_user_nonedit.setVisibility(View.VISIBLE);
                    admin_user_edit.setVisibility(View.GONE);
                    admin_user_edit_btns.setVisibility(View.GONE);
                    //원본 수정
                    admin_bizname_text.setText(admin_bizname_edit_text.getText());
                    admin_biznnum_text.setText(admin_biznnum_edit_text.getText());
                    admin_password_text.setText(admin_password_edit_text.getText());
                    admin_email_text.setText(admin_email_edit_text.getText());
                    admin_phonenum_text.setText(admin_phonenum_edit_text.getText());
                    admin_address_text.setText(admin_address_edit_text.getText());

                    //TODO 서버로 데이터 수정 요청 추가되어야하는 부분
                }
                else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.admin_biznnum_edit_text://사업자번호는 수정불가능
                Toast.makeText(getApplicationContext(), "사업자번호는 수정이 불가능 합니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
