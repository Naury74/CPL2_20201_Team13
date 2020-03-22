package com.example.kioskmainpage.Activity.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class SyncCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_complete);

        Button sync_complete_confirm = (Button) findViewById(R.id.sync_complete_btn_confirm);
        sync_complete_confirm.setOnClickListener(this);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.sync_complete_btn_confirm:
                finish();
                break;
        }
    }
}
