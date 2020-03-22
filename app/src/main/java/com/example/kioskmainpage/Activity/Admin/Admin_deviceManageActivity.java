package com.example.kioskmainpage.Activity.Admin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.FakeHome;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class Admin_deviceManageActivity extends AppCompatActivity implements View.OnClickListener{


    LinearLayout linearLayout;
    TextView adminThememanage,admin_manage_btn_left;
    TextView admin_btn_home_text,admin_btn_sync_text,admin_btn_screen_text,admin_btn_theme_text;
    ImageView admin_btn_home_Image,admin_btn_sync_Image,admin_btn_screen_Image,admin_btn_theme_Image;
    Button btn_home,btn_sync,btn_screen,btn_theme;
    FrameLayout admin_btn_home_layout,admin_btn_sync_layout,admin_btn_screen_layout,admin_btn_theme_layout;

    @Override //화면전환이후 상하단 제거
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_device_manage);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        admin_btn_home_Image = (ImageView)findViewById(R.id.admin_btn_home_Image);
        admin_btn_sync_Image = (ImageView)findViewById(R.id.admin_btn_sync_Image);
        admin_btn_screen_Image = (ImageView)findViewById(R.id.admin_btn_screen_Image);
        admin_btn_theme_Image = (ImageView)findViewById(R.id.admin_btn_theme_Image);

        admin_btn_home_text = (TextView)findViewById(R.id.admin_btn_home_text);
        admin_btn_sync_text = (TextView)findViewById(R.id.admin_btn_sync_text);
        admin_btn_screen_text = (TextView)findViewById(R.id.admin_btn_screen_text);
        admin_btn_theme_text = (TextView)findViewById(R.id.admin_btn_theme_text);

        linearLayout = (LinearLayout)findViewById(R.id.admin_main_rootLayout);
        admin_btn_home_layout = (FrameLayout)findViewById(R.id.admin_btn_home_layout);
        admin_btn_sync_layout = (FrameLayout)findViewById(R.id.admin_btn_sync_layout);
        admin_btn_screen_layout = (FrameLayout)findViewById(R.id.admin_btn_screen_layout);
        admin_btn_theme_layout = (FrameLayout)findViewById(R.id.admin_btn_theme_layout);

        btn_home = (Button)findViewById(R.id.admin_btn_home);
        btn_home.setOnClickListener(this);
        btn_sync = (Button)findViewById(R.id.admin_btn_sync);
        btn_sync.setOnClickListener(this);
        btn_screen = (Button)findViewById(R.id.admin_btn_screen);
        btn_screen.setOnClickListener(this);
        btn_theme = (Button)findViewById(R.id.admin_btn_theme);
        btn_theme.setOnClickListener(this);

        admin_manage_btn_left = (TextView) findViewById(R.id.admin_manage_btn_left);
        admin_manage_btn_left.setOnClickListener(this);

        adminThememanage = (TextView)findViewById(R.id.textView4manage);
        adminThememanage.setOnClickListener(this);

        setTheme();

        Myapplication app = (Myapplication)getApplication();
        app.setAdmindeviceManageContext(this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        setTheme();

        SharedPreferences sharedPreferencesforhome = getSharedPreferences("HomeAppSet", MODE_PRIVATE);
        if(sharedPreferencesforhome.getBoolean("HomeAppSet_status2",false))
        {
            SharedPreferences.Editor editor = sharedPreferencesforhome.edit();
            editor.putBoolean("HomeAppSet_status2", false);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            Log.i("홈앱", "onActivityResult: 설정2");
            editor.commit();

        }
    }

    public void setTheme()
    {
        Myapplication app = (Myapplication) getApplication();
        linearLayout.setBackgroundColor(getColor(app.getMainTheme().getThemeItem().getADMIN_BACKGROUND_COLOR_ID()));//바탕색
        adminThememanage.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));//타이틀색깔
        admin_manage_btn_left.setCompoundDrawablesWithIntrinsicBounds(getDrawable(app.getMainTheme().getThemeItem().getADMIN_PASS_LEFT_IC_ID()),null,null,null);//화살표 아이콘
        admin_manage_btn_left.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));

        admin_btn_home_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_sync_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_screen_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_theme_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));

        if(app.getMainTheme().getTheme_name().equals("BLACK"))
        {
            admin_btn_sync_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_sync_Image.setImageDrawable(getDrawable(R.drawable.admin_sync2));

            admin_btn_home_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_home_Image.setImageDrawable(getDrawable(R.drawable.admin_home2));

            admin_btn_screen_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_screen_Image.setImageDrawable(getDrawable(R.drawable.admin_screen2));

            admin_btn_theme_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_theme_Image.setImageDrawable(getDrawable(R.drawable.admin_theme2));
        }
        else if(app.getMainTheme().getTheme_name().equals("WHITE"))
        {
            admin_btn_sync_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_sync_Image.setImageDrawable(getDrawable(R.drawable.admin_sync1));

            admin_btn_home_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_home_Image.setImageDrawable(getDrawable(R.drawable.admin_home1));

            admin_btn_screen_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_screen_Image.setImageDrawable(getDrawable(R.drawable.admin_screen1));

            admin_btn_theme_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_theme_Image.setImageDrawable(getDrawable(R.drawable.admin_theme1));
        }
        else if(app.getMainTheme() .getTheme_name().equals("SCHOOL_FOOD"))
        {
            admin_btn_sync_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_sync_Image.setImageDrawable(getDrawable(R.drawable.admin_sync4));

            admin_btn_home_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_home_Image.setImageDrawable(getDrawable(R.drawable.admin_home4));

            admin_btn_screen_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_screen_Image.setImageDrawable(getDrawable(R.drawable.admin_screen4));

            admin_btn_theme_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_theme_Image.setImageDrawable(getDrawable(R.drawable.admin_theme4));
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.admin_btn_home:
                Intent intent2home = new Intent(this, Admin_home_popupActivity.class);
                startActivityForResult(intent2home,1);
                break;
            case R.id.admin_btn_sync:
                Intent intent2sync = new Intent(this, Admin_syncActivity.class);
                startActivityForResult(intent2sync,2);
                break;
            case R.id.admin_btn_screen:
                Intent intent2screen = new Intent(this,ScreenOff_popupActivity.class);
                startActivity(intent2screen);
                break;
            case R.id.admin_btn_theme:
                Intent intent2theme = new Intent(this,Admin_themeActivity.class);
                startActivity(intent2theme);
                break;
            case R.id.admin_btn_info:

                break;
            case R.id.admin_manage_btn_left:
                finish();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //팝업 액티비티의 결과를 받는 부분
        //  countDownReset(); //BestNewMenuActivity로 이동하는 카운트다운을 리셋
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //홈앱설정부분
                case 1:

                    break;
                case 2:
                    //데이터 동기화 구현 = 데이터 동기화 메뉴 -> 기기 관리 -> 관리자 메뉴 -> 메인액티비티 -> 로딩페이지로 이동함.
                    Intent intent_deviceManageToAdmin = new Intent();
                    intent_deviceManageToAdmin.putExtra("result",RESULT_OK);
                    intent_deviceManageToAdmin.putExtra("requestCode",1);
                    setResult(RESULT_OK, intent_deviceManageToAdmin);
                    finish();
                    break;
            }
        }
    }



}
