package com.example.kioskmainpage.Activity.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.Sign.SignInActivity;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class LogoutPopupActivity extends AppCompatActivity implements View.OnClickListener {

    Myapplication myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button cancel = (Button) findViewById(R.id.logout_announce_btn_cancel);
        cancel.setOnClickListener(this);
        Button next = (Button) findViewById(R.id.logout_announce_btn_next);
        next.setOnClickListener(this);

        myapp = (Myapplication)getApplication();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.logout_announce_btn_cancel:
                finish();
                break;
            case R.id.logout_announce_btn_next:
                //자동로그인 설정해제
                SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("AutoLogin_status",false);
                editor.commit();
                //logout 구현 = 전체 엑티비티를 죽이고 -> 로그인페이지로 이동함.
                ((Admin_MainActivity)myapp.getAdminMainContext()).finish();
                ((MainActivity)myapp.getMainActivityContext()).finish();
                ((BestNewMenuActivity)myapp.getBestNewMenuActivityContext()).finish();
                Intent intent_logout = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent_logout);
                finish();
                break;
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("LogoutPopup", "onDestroy: 로그아웃팝업 제거됨");
    }
}
