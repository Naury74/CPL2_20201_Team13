package com.example.kioskmainpage.Activity.Admin;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class ScreenOff_popupActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_off_popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Button btn_next = (Button) findViewById(R.id.screen_off_btn_next);
        btn_next.setOnClickListener(this);
        Button btn_cancel = (Button) findViewById(R.id.screen_off_btn_cancel);
        btn_cancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.screen_off_btn_next:
                DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                devicePolicyManager.lockNow();
                Myapplication myapp=(Myapplication) getApplication();
                ((Admin_MainActivity)myapp.getAdminMainContext()).finish();
                ((Admin_deviceManageActivity)myapp.getAdmindeviceManageContext()).finish();
                finish();
                break;
            case R.id.screen_off_btn_cancel:
                finish();
                break;
        }
    }
}
