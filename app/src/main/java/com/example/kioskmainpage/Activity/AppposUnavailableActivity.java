package com.example.kioskmainpage.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.Appchecker;

public class AppposUnavailableActivity extends AppCompatActivity {
    Appchecker appchecker;
    int uiOptions;
    int newUiOptions;

    @Override
    protected void onResume() {
        super.onResume();
        appchecker.checkINFINIXapp();
        if(appchecker.isit_installed()==true)
        {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apppos_unavailable);
        appchecker = new Appchecker(this);
        //상,하단의 바를 제거하고 풀스크린으로 만듬
        uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    public void click_to_apppos_market(View v) {
        appchecker.go_market_infinix();
    }
    public void click_to_finish(View v) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        //하단 네비게이션 바를 숨겨놨지만 최하단을 위쪽으로 드래그하면 바가 나와서 뒤로가기 버튼 막음
        return;
    }
}
