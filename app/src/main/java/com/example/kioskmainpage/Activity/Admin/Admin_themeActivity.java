package com.example.kioskmainpage.Activity.Admin;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.Adapter.RAdapter;
import com.example.kioskmainpage.Adapter.RAdapter3;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.RecyclerDecoration;
import com.example.kioskmainpage.ThemeItem;

import java.util.ArrayList;

public class Admin_themeActivity extends AppCompatActivity {

    ArrayList<RAdapter3.CustomViewHolder> viewHolders;
    RAdapter3 rAdapter3;
    ImageButton admin_btn_left;
    RecyclerView recyclerView;
    ThemeItem themeItem1,themeItem2,themeItem3,themeItem4;
    TextView adminThemeTitle;
    LinearLayout linearLayout;
    Myapplication app;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_theme);

        app = (Myapplication) getApplication();

        linearLayout = (LinearLayout)findViewById(R.id.admin_them_rootLayout);

        admin_btn_left = (ImageButton) findViewById(R.id.admin_theme_btn_left);
        admin_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adminThemeTitle = (TextView) findViewById(R.id.textView4adminTheme);

        final ArrayList<ThemeItem> themeItems = new ArrayList<>();
        themeItem1 = new ThemeItem("WHITE");//화이트 테마아이템
        themeItems.add(themeItem1);

        themeItem2 = new ThemeItem("BLACK");//블랙 테마아이템
        themeItems.add(themeItem2);

        themeItem3 = new ThemeItem("SCHOOL_FOOD");//분식 테마아이템
        themeItems.add(themeItem3);

        themeItem4 = new ThemeItem("add");//add(추가) 버튼
        themeItems.add(themeItem4);

        rAdapter3 = new RAdapter3(themeItems,app.getMainTheme());//현재 존재하는 테마들 추가


        recyclerView = (RecyclerView)findViewById(R.id.theme_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(rAdapter3);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(50);//줄간격 조절 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)
        recyclerView.addItemDecoration(spaceDecoration);

        setTheme();//테마 적용
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();//화면 다시 켜질때 테마 적용
    }

    public void setTheme()
    {
        Myapplication app = (Myapplication) getApplication();
        linearLayout.setBackgroundColor(getColor(app.getMainTheme().getThemeItem().getBACKGROUND_COLOR_ID()));//바탕색
        adminThemeTitle.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));//텍스트
        admin_btn_left.setImageResource(app.getMainTheme().getThemeItem().getADMIN_PASS_LEFT_IC_ID());//화살표
    }
}
