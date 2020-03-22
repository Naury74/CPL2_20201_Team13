package com.example.kioskmainpage.Activity.Waiting;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.kioskmainpage.Adapter.AutoScrollAdapter;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.MenuManage.MenuManager;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class Waiting_MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> folder_names = new ArrayList<>();
    ArrayList<String> best_menus,best_image_links,best_menus_ments;
    AutoScrollViewPager mViewPager;
    private ArrayList<Menu> menus=new ArrayList<>();//best
    MenuManager menuManager;
    AutoScrollAdapter autoScrollAdapter;
    MainTheme mainTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_main);
        Intent intent = getIntent();
        folder_names = intent.getStringArrayListExtra("folderNames");

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        ImageButton Waiting_Main_btn_to_mainact = (ImageButton)findViewById(R.id.Waiting_Main_btn_to_mainact);
        Waiting_Main_btn_to_mainact.setOnClickListener(this);
        ImageButton Waiting_Main_btn_nopay = (ImageButton)findViewById(R.id.Waiting_Main_btn_nopay);
        Waiting_Main_btn_nopay.setOnClickListener(this);
        ImageButton Waiting_Main_btn_order = (ImageButton)findViewById(R.id.Waiting_Main_btn_order);
        Waiting_Main_btn_order.setOnClickListener(this);

        //best메뉴 이름들 가져오기
        Myapplication app = (Myapplication) getApplication();
        best_menus = app.getBest_menus();
        best_menus_ments = app.getBest_ments();
        best_image_links = app.getBest_image_links();

        menuManager = new MenuManager(this.getFilesDir().getAbsolutePath() +"", folder_names,app.getBizNum());

        getBestMenusList();//best메뉴리스트 만들기

        //best메뉴 생성
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewpager_best_wait);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewPager.getLayoutParams();
        //  mBestMenuAdapter =new BestMenuAdapter(getSupportFragmentManager(),menus);
        //  mViewPager.setAdapter(mBestMenuAdapter);
        mainTheme = app.getMainTheme();
        autoScrollAdapter = new AutoScrollAdapter(this,menus,layoutParams.height, mainTheme);
        mViewPager.setAdapter(autoScrollAdapter);
        mViewPager.setInterval(5000);
        mViewPager.setScrollDurationFactor(2);
        mViewPager.startAutoScroll();
        mViewPager.setCurrentItem(0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_wait);
        tabLayout.setupWithViewPager(mViewPager, true);

    }

    public void onClick(View v) {
        switch(v.getId())
        {
            //주문하기 버튼 누를시에 팝업뜸
            case R.id.Waiting_Main_btn_to_mainact:
                Intent intent_popup = new Intent (this, Waiting_popup.class);
                startActivityForResult(intent_popup, 3);
                break;
            //비결제 웨이팅 버튼
            case R.id.Waiting_Main_btn_nopay:
                Intent intent_nopay = new Intent (this, Waiting_nopayActivity.class);
                startActivity(intent_nopay);
                break;
            //웨이팅 주문 버튼
            case R.id.Waiting_Main_btn_order:
                Intent intent_order = new Intent (this, Waiting_orderActivity.class);
                startActivityForResult(intent_order, 4);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                case 4:
                    finish();
                    break;
            }
    }





    public void getBestMenusList()//best메뉴리스트 만들기
    {
        ArrayList<Menu> all_menus=menuManager.getAllMenusforbest();

        boolean[] check = new boolean[best_menus.size()];
        for (int i=0;i<all_menus.size();i++)
        {
            for(int j=0;j<best_menus.size();j++)
            {

                if(best_menus.get(j).equals(all_menus.get(i).getMenu_name()) && check[j]!=true)
                {
                    all_menus.get(i).setBitmap(best_image_links.get(j));
                    all_menus.get(i).setMenu_ment(best_menus_ments.get(j));
                    menus.add(all_menus.get(i));
                    check[j] = true;
                }
            }
        }
    }


    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
