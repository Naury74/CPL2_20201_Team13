package com.example.kioskmainpage.Activity.Admin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kioskmainpage.Activity.FakeHome;
import com.example.kioskmainpage.Adapter.PagerAdapter_Homeset;
import com.example.kioskmainpage.R;

public class Admin_home_popupActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
   static ImageButton image_passright, image_passleft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_popup);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        ImageButton image_passright = (ImageButton)findViewById(R.id.image_passright);
        image_passright.setOnClickListener(this);
        ImageButton image_passleft = (ImageButton)findViewById(R.id.image_passleft);
        image_passleft.setOnClickListener(this);
        ImageButton home_imageButton_finish = (ImageButton)findViewById(R.id.home_imageButton_finish);
        Button tab_layout_homeset_btn_confirm = (Button)findViewById(R.id.tab_layout_homeset_btn_confirm);
        tab_layout_homeset_btn_confirm.setOnClickListener(this);
        home_imageButton_finish.setOnClickListener(this);
        home_imageButton_finish.bringToFront();
        mViewPager = (ViewPager) findViewById(R.id.home_ViewPager);
        PagerAdapter_Homeset pageradapter_homeset = new PagerAdapter_Homeset(getSupportFragmentManager(),3);
        mViewPager.setAdapter(pageradapter_homeset);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_homeset);
        tabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.home_imageButton_finish:
            {
                finish();
                break;
            }
            case R.id.tab_layout_homeset_btn_confirm:
                if(!isMyLauncherDefault()) {

                    SharedPreferences sharedPreferences = getSharedPreferences("HomeAppSet", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("HomeAppSet_status",true);
                    editor.putBoolean("HomeAppSet_status2",true);
                    editor.commit();
                    PackageManager p = getPackageManager();
                    ComponentName cN = new ComponentName(getApplicationContext(), FakeHome.class);
                    p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    Intent selector = new Intent(Intent.ACTION_MAIN);
                    selector.addCategory(Intent.CATEGORY_HOME);
                    startActivity(selector);
                    p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    Log.i("홈앱", "onActivityResult: 설정1");
                }
                finish();
                break;
            case R.id.image_passleft:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
                break;
            case R.id.image_passright:
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        Log.i("위치", "onPageScrolled: "+position);
        ImageButton image_passright = (ImageButton)findViewById(R.id.image_passright);
        ImageButton image_passleft = (ImageButton)findViewById(R.id.image_passleft);
        switch (position)
        {
            case 0:
                image_passleft.setVisibility(View.GONE);
                image_passright.setVisibility(View.VISIBLE);
                break;
            case 1:
                image_passleft.setVisibility(View.VISIBLE);
                image_passright.setVisibility(View.VISIBLE);
                break;
            case 2:
                image_passleft.setVisibility(View.VISIBLE);
                image_passright.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageSelected(int position)
    {

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
    private boolean isMyLauncherDefault() {
        PackageManager localPackageManager = getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        String str = localPackageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        return str.equals(getPackageName());
    }
}
