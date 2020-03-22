package com.example.kioskmainpage.Activity.Admin;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.kioskmainpage.Activity.HiddenMenuActivity;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

public class Admin_MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearLayout;
    TextView textView_Title,admin_btn_left;
    TextView admin_btn_user_text,admin_btn_manage_text,admin_btn_customerservice_text,admin_btn_terms_text,admin_btn_info_text,admin_btn_logout_text;
    ImageView admin_btn_user_Image,admin_btn_manage_Image,admin_btn_customerservice_Image,admin_btn_terms_Image,admin_btn_info_Image,admin_btn_logout_Image;
    Button btn_user,btn_manage,btn_customerservice,btn_terms,btn_info,btn_logout;
    FrameLayout admin_btn_user_layout,admin_btn_manage_layout,admin_btn_customerservice_layout,admin_btn_terms_layout,admin_btn_info_layout,admin_btn_logout_layout;

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
        setContentView(R.layout.activity_admin_main);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        admin_btn_user_Image = (ImageView)findViewById(R.id.admin_btn_user_Image);
        admin_btn_manage_Image = (ImageView)findViewById(R.id.admin_btn_manage_Image);
        admin_btn_customerservice_Image = (ImageView)findViewById(R.id.admin_btn_customerservice_Image);
        admin_btn_terms_Image = (ImageView)findViewById(R.id.admin_btn_terms_Image);
        admin_btn_info_Image = (ImageView)findViewById(R.id.admin_btn_info_Image);
        admin_btn_logout_Image = (ImageView)findViewById(R.id.admin_btn_logout_Image);

        admin_btn_user_text = (TextView)findViewById(R.id.admin_btn_user_text);
        admin_btn_manage_text = (TextView)findViewById(R.id.admin_btn_manage_text);
        admin_btn_customerservice_text = (TextView)findViewById(R.id.admin_btn_customerservice_text);
        admin_btn_terms_text = (TextView)findViewById(R.id.admin_btn_terms_text);
        admin_btn_info_text = (TextView)findViewById(R.id.admin_btn_info_text);
        admin_btn_logout_text = (TextView)findViewById(R.id.admin_btn_logout_text);

        linearLayout = (LinearLayout)findViewById(R.id.admin_main_rootLayout);
        admin_btn_user_layout = (FrameLayout)findViewById(R.id.admin_btn_user_layout);
        admin_btn_manage_layout = (FrameLayout)findViewById(R.id.admin_btn_manage_layout);
        admin_btn_customerservice_layout = (FrameLayout)findViewById(R.id.admin_btn_customerservice_layout);
        admin_btn_terms_layout = (FrameLayout)findViewById(R.id.admin_btn_terms_layout);
        admin_btn_info_layout = (FrameLayout)findViewById(R.id.admin_btn_info_layout);
        admin_btn_logout_layout = (FrameLayout)findViewById(R.id.admin_btn_logout_layout);

        btn_user = (Button)findViewById(R.id.admin_btn_user);
        btn_user.setOnClickListener(this);
        btn_manage = (Button)findViewById(R.id.admin_btn_manage);
        btn_manage.setOnClickListener(this);
        btn_customerservice = (Button)findViewById(R.id.admin_btn_customerservice);
        btn_customerservice.setOnClickListener(this);
        btn_terms = (Button)findViewById(R.id.admin_btn_terms);
        btn_terms.setOnClickListener(this);
        btn_info = (Button)findViewById(R.id.admin_btn_info);
        btn_info.setOnClickListener(this);
        btn_logout = (Button)findViewById(R.id.admin_btn_logout);
        btn_logout.setOnClickListener(this);

        admin_btn_left = (TextView) findViewById(R.id.admin_main_btn_left);
        admin_btn_left.setOnClickListener(this);

        textView_Title = (TextView)findViewById(R.id.textView_Title);
        textView_Title.setOnClickListener(this);
        Toast.makeText(this, "관리자 메뉴로 들어오셨습니다.", Toast.LENGTH_SHORT).show();

        Myapplication app = (Myapplication)getApplication();
        app.setAdminMainContext(this);


        setTheme();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
    }

    public void setTheme()
    {
        Myapplication app = (Myapplication) getApplication();
        linearLayout.setBackgroundColor(getColor(app.getMainTheme().getThemeItem().getADMIN_BACKGROUND_COLOR_ID()));//바탕색
        textView_Title.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));//타이틀색깔
        admin_btn_left.setCompoundDrawablesWithIntrinsicBounds(getDrawable(app.getMainTheme().getThemeItem().getADMIN_PASS_LEFT_IC_ID()),null,null,null);//화살표 아이콘
        admin_btn_left.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));

        admin_btn_user_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_manage_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_customerservice_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_terms_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_info_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        admin_btn_logout_text.setTextColor(getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));

        if(app.getMainTheme().getTheme_name().equals("BLACK"))
        {
            admin_btn_user_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_user_Image.setImageDrawable(getDrawable(R.drawable.admin_user2));

            admin_btn_manage_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_manage_Image.setImageDrawable(getDrawable(R.drawable.admin_manage2));

            admin_btn_customerservice_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_customerservice_Image.setImageDrawable(getDrawable(R.drawable.admin_customerservice2));

            admin_btn_terms_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_terms_Image.setImageDrawable(getDrawable(R.drawable.admin_terms2));

            admin_btn_info_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_info_Image.setImageDrawable(getDrawable(R.drawable.admin_info2));

            admin_btn_logout_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke);
            admin_btn_logout_Image.setImageDrawable(getDrawable(R.drawable.admin_logout2));
        }
        else if(app.getMainTheme().getTheme_name().equals("WHITE"))
        {
            admin_btn_user_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_user_Image.setImageDrawable(getDrawable(R.drawable.admin_user1));

            admin_btn_manage_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_manage_Image.setImageDrawable(getDrawable(R.drawable.admin_manage1));

            admin_btn_customerservice_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_customerservice_Image.setImageDrawable(getDrawable(R.drawable.admin_customerservice1));

            admin_btn_terms_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_terms_Image.setImageDrawable(getDrawable(R.drawable.admin_terms1));

            admin_btn_info_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_info_Image.setImageDrawable(getDrawable(R.drawable.admin_info1));

            admin_btn_logout_layout.setBackgroundResource(R.drawable.rounded_white_nonstroke);
            admin_btn_logout_Image.setImageDrawable(getDrawable(R.drawable.admin_logout1));
        }
        else if(app.getMainTheme().getTheme_name().equals("SCHOOL_FOOD"))
        {
            admin_btn_user_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_user_Image.setImageDrawable(getDrawable(R.drawable.admin_user4));

            admin_btn_manage_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_manage_Image.setImageDrawable(getDrawable(R.drawable.admin_manage4));

            admin_btn_customerservice_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_customerservice_Image.setImageDrawable(getDrawable(R.drawable.admin_customerservice4));

            admin_btn_terms_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_terms_Image.setImageDrawable(getDrawable(R.drawable.admin_terms4));

            admin_btn_info_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_info_Image.setImageDrawable(getDrawable(R.drawable.admin_info4));

            admin_btn_logout_layout.setBackgroundResource(R.drawable.rounded_black_nonstroke323232);
            admin_btn_logout_Image.setImageDrawable(getDrawable(R.drawable.admin_logout4));
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.admin_btn_user:
                Intent intent2user = new Intent(this, Admin_userActivity.class);
                startActivity(intent2user);
                break;
            case R.id.admin_btn_sync:
                Intent intent2sync = new Intent(this, Admin_syncActivity.class);
                startActivityForResult(intent2sync,1);
                break;
                case R.id.admin_btn_manage:
                Intent intent2manage = new Intent(this, Admin_deviceManageActivity.class);
                startActivity(intent2manage);
                break;
            case R.id.admin_btn_theme:
                Intent intent2theme = new Intent(this,Admin_themeActivity.class);
                startActivity(intent2theme);
                break;
            case R.id.admin_btn_customerservice:
                break;
            case R.id.admin_btn_terms:
                break;
            case R.id.admin_btn_info:
                break;
            case R.id.admin_btn_logout:
                Intent intent2logout = new Intent(this, LogoutPopupActivity.class);
                startActivity(intent2logout);
                break;
            case R.id.admin_main_btn_left:
                finish();
                break;
            case R.id.textView_Title:
                Intent admin2hidden = new Intent(this, HiddenMenuActivity.class);
                startActivity(admin2hidden);
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("adminMain", "onDestroy: 관리자메뉴액티비티 제거됨");
    }
}

