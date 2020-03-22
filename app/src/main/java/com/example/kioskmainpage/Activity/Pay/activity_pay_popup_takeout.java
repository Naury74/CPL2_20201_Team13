package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

import static android.view.View.GONE;


public class activity_pay_popup_takeout extends AppCompatActivity {
//    String price;
//    ArrayList<SelectedMenu> selectedMenus;
    TextView btnWrap, btnHall, takeout_type_selected_TextView, takeout_text, shop_text, textView;
    Myapplication myapp;
    LinearLayout takeout_type_select, takeout_type_selected;
    ImageView takeout_type_selected_ImageView, shop_image, takeout_image;
    String type;
    MainTheme mainTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_popup_takeout);
        myapp = (Myapplication) getApplication();
        btnWrap = (TextView) findViewById(R.id.btnWrap);
        btnHall = (TextView) findViewById(R.id.btnHall);
        textView = (TextView) findViewById(R.id.textView);
        takeout_text = (TextView) findViewById(R.id.takeout_text);
        takeout_image = (ImageView) findViewById(R.id.takeout_image);
        shop_text = (TextView) findViewById(R.id.shop_text);
        shop_image = (ImageView) findViewById(R.id.shop_image);
        takeout_type_selected_TextView = (TextView) findViewById(R.id.takeout_type_selected_TextView);
        takeout_type_select = (LinearLayout) findViewById(R.id.takeout_type_select);
        takeout_type_selected = (LinearLayout) findViewById(R.id.takeout_type_selected);
        takeout_type_selected_ImageView = (ImageView) findViewById(R.id.takeout_type_selected_ImageView);

        View view = getLayoutInflater().from(this).inflate(R.layout.activity_pay_popup, null);

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

//        Intent intent_sum = getIntent();
////        price = intent_sum.getStringExtra("sum");
////        selectedMenus = intent_sum.getParcelableArrayListExtra("selectedMenus");//메뉴구성 및 총가격 받아옴
        setTheme();
        takeout_type_selected.setVisibility(GONE);
        textView.setVisibility(GONE);
    }

    public void onTakeoutClick(View v) {//결제화면 불러오기 및 포장 유무, 메뉴구성, 총가격 넘김
        switch (v.getId()) {
            case R.id.btnWrap:
                takeout_type_select.setVisibility(GONE);
                type = "TAKEOUT";
                takeout_type_selected_ImageView.setImageResource(R.drawable.ic_ico_takeout);
                takeout_type_selected_TextView.setText("테이크아웃\n선택하시겠습니까?");
                Spannable sb1 = (Spannable) takeout_type_selected_TextView.getText();
                sb1.setSpan(new StyleSpan(Typeface.BOLD), 0,  6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
                takeout_type_selected.setVisibility(View.VISIBLE);
                break;
            case R.id.btnHall:
                takeout_type_select.setVisibility(GONE);
                type = "HERE";
                takeout_type_selected_ImageView.setImageResource(R.drawable.ic_ico_shop);
                takeout_type_selected_TextView.setText("매장\n선택하시겠습니까?");
                Spannable sb2 = (Spannable) takeout_type_selected_TextView.getText();
                sb2.setSpan(new StyleSpan(Typeface.BOLD), 0,  3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
                takeout_type_selected.setVisibility(View.VISIBLE);
                break;
            case R.id.takeout_btn_confirm:
                Intent intentTopay;
                if (myapp.isTable_status().equals("T")&&type.equals("HERE"))
                    intentTopay = new Intent(v.getContext(), Table_numActivity.class);//테이블 서빙 설정 TRUE ->Table_numActivity
                else
                    intentTopay = new Intent(v.getContext(), pay_popupActivity.class);//테이블 서빙 설정 FALSE ->pay_popupActivity

                intentTopay.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                intentTopay.putExtra("sum", price + "원");
//                intentTopay.putParcelableArrayListExtra("selectedMenus", selectedMenus);
                myapp.setTakeout_status(type);
                Log.d("DSA", "onTakeoutClick: " + myapp.getTakeout_status());
                startActivity(intentTopay);
                finish();
                break;
            case R.id.takeout_btn_Cancel1:
                finish();
            case R.id.takeout_btn_Cancel_selected:
                takeout_type_select.setVisibility(View.VISIBLE);
                takeout_type_selected.setVisibility(View.GONE);
                break;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;

    }

    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음


    public void setTheme() {
        Myapplication app = (Myapplication) getApplication();//앱 객체 생성
        mainTheme = app.getMainTheme();// 테마 연결
        textView.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR()));//TAKE OUT 메뉴 선택 텍스트 색상
        takeout_text.setTextColor(getColor(mainTheme.getThemeItem().getTAKEOUT_TEXT_COLOR()));//TAKE OUT '포장' 텍스트 색상
        takeout_image.setImageResource(mainTheme.getThemeItem().getTAKEOUT_IMAGE());//TAKE OUT '포장' 이미지
        shop_text.setTextColor(getColor(mainTheme.getThemeItem().getSHOP_TEXT_COLOR()));//TAKE OUT '매장' 텍스트 색상
        shop_image.setImageResource(mainTheme.getThemeItem().getSHOP_IMAGE());//TAKE OUT '매장' 이미지
        takeout_type_select.setBackgroundResource(mainTheme.getThemeItem().getTAKE_OUT_BACKGROUND_ROUND_COLOR());//TAKE OUT 라운드 배경 색상
        takeout_type_selected.setBackgroundResource(mainTheme.getThemeItem().getTAKE_OUT_BACKGROUND_ROUND_COLOR2());//TAKE OUT 라운드 2번째 배경 색상
        takeout_type_selected_TextView.setTextColor(getColor(mainTheme.getThemeItem().getTAKEOUT_SECOND_TEXT_COLOR()));//TAKE OUT 두번째 팝업 글자 색상
    }
}
