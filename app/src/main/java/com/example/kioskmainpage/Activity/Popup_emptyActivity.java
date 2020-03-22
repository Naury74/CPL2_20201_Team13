package com.example.kioskmainpage.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.R;

public class Popup_emptyActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_empty);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        TextView empty_btn = (TextView)findViewById(R.id.empty_btn);
        empty_btn.setOnClickListener(this);
    }
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.empty_btn:
                finish();
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

}
