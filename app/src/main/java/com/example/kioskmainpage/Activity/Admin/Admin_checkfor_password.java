package com.example.kioskmainpage.Activity.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.R;

public class Admin_checkfor_password extends AppCompatActivity {

    EditText password_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_checkfor_password);
        password_check = (EditText)findViewById(R.id.password_check);
    }

    public boolean onTouchEvent(MotionEvent event) {
        //팝업 액티비티의 바깥쪽 터치를 무시하도록
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //하단 네비게이션 바를 숨겨놨지만 최하단을 위쪽으로 드래그하면 바가 나와서 뒤로가기 버튼 막음
        return;
    }

    @Override
    public void onResume(){
        super.onResume();

//        setTheme();
    }
    public void checkforpass(View v){
        SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
        String s1 = sharedPreferences.getString("AutoLogin_password", "");
        String s2 =password_check.getText().toString();
        if(s1.equals(s2))
        {
            Intent intent = new Intent(this, Admin_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
    public void finish(View v){
        finish();
    }
}
