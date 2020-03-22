package com.example.kioskmainpage.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import com.example.kioskmainpage.Utilities.multistatetogglebutton.MultiStateToggleButton;
import com.example.kioskmainpage.Utilities.multistatetogglebutton.ToggleButton;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class PopupActivity extends AppCompatActivity {

    private static final String TAG = "PopupActivity";
    ArrayList<MultiStateToggleButton> buttons = new ArrayList<>();
    ArrayList<Integer> before_state = new ArrayList<>();
    TextView tv_menu_price;
    Menu menu;
    int index;
    ImageView imageView;
    LinearLayout option_linearLayout;
    MainTheme mainTheme;
    MultiStateToggleButton multiStateToggleButton;
    TextView option_name;
    TextView tv_menu_name;
    Button cancelButton, confirmButton;

    public void setTheme()
    {
        Myapplication app = (Myapplication)getApplication();//앱 객체 생성
        mainTheme = app.getMainTheme();// 테마 연결
        option_linearLayout.setBackgroundResource(mainTheme.getThemeItem().getNUMPAD_BACKGROUND_ROUND_COLOR());// 액티비티 배경
        if(option_name !=null)
        {
            option_name.setTextColor(getColor(mainTheme.getThemeItem().getOPTION_POPUP_MENU_NAME_COLOR()));// 옵션 이름
            option_name.setPaintFlags(option_name.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);// 옵션명 볼드처리
        }
        multiStateToggleButton.setColors(getColor(mainTheme.getThemeItem().getOPTION_TOGGLE_SELECT_COLOR()), getColor(mainTheme.getThemeItem().getOPTION_TOGGLE_COLOR()));//옵션 선택버튼 선택, 미선택
        tv_menu_name.setTextColor(getColor(mainTheme.getThemeItem().getOPTION_POPUP_MENU_NAME_COLOR()));//메뉴 이름
        tv_menu_price.setTextColor(getColor(mainTheme.getThemeItem().getOPTION_POPUP_MENU_NAME_COLOR()));//메뉴 가격
        confirmButton.setBackgroundResource(mainTheme.getThemeItem().getCONFIRM_BTN_BACKGROUND_COLOR());//확인 버튼 색
        confirmButton.setTextColor(getColor(mainTheme.getThemeItem().getOPTION_TOGGLE_COLOR()));//확인 버튼 글자색
        cancelButton.setBackgroundResource(mainTheme.getThemeItem().getCANCEL_BTN_BACKGROUND_COLOR());//취소 버튼 색
        cancelButton.setTextColor(getColor(mainTheme.getThemeItem().getOPTION_POPUP_CANCLE_BUTTON_TEXT_COLOR()));//취소 버튼 글자색
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        //     countDownReset();//카운트다운리셋
        //상,하단의 바를 제거하고 풀스크린으로 만듬
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        confirmButton = (Button)findViewById(R.id.confirmButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        option_linearLayout = (LinearLayout)findViewById(R.id.choose_menu_option_select_layout);

        //인텐트를 받아와서 레이아웃 생성
        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        menu = intent.getParcelableExtra("menu");
        before_state = intent.getIntegerArrayListExtra("options");
        Log.d(TAG, "get index : " +index);

        tv_menu_name = (TextView) findViewById(R.id.menuName);
        tv_menu_name.setText(menu.getMenu_name());
        tv_menu_name.setPaintFlags(tv_menu_name.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
        tv_menu_price = (TextView) findViewById(R.id.menuPrice);
        tv_menu_price.setText(NumberFormat.getInstance().format(Integer.parseInt(menu.getMenu_price())) + "원");


        imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = null;
        File imageFile = new File(menu.getBitmap());
        if(menu.getBitmap().equals("No_Image"))
        {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_image2));
        }
        else{
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        for (int i = 0; i < menu.getOptions().size(); i++) {
            //옵션 이름 생성
            option_name = new TextView(this);
            option_name.setTextColor(getResources().getColor(R.color.light_gray_3));
            LinearLayout.LayoutParams optionNameParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            optionNameParam.setMargins(10, 0, 0, 0);
            option_name.setLayoutParams(optionNameParam);

            option_name.setText(menu.getOptions().get(i).getOption_name());

            ArrayList<String> optionElements_temp_option = new ArrayList<>();//menu.getOptions().get(i).getOptions();
            ArrayList<String> optionElements_temp_option_price = new ArrayList<>();//menu.getOptions_price().get(i).getOptions();

            for(int j=0;j<menu.getOptions().get(i).getOptions().size();j++)
            {
                optionElements_temp_option.add(menu.getOptions().get(i).getOptions().get(j));
                optionElements_temp_option_price.add(menu.getOptions().get(i).getOptions_price().get(j));
            }

                //옵션에 가격을 추가함.
                int checkforall0sum=0;
                for(int checkforall0 = 0 ;checkforall0<menu.getOptions().get(i).getOptions().size();checkforall0++)
                {
                    checkforall0sum = checkforall0sum + Integer.parseInt(optionElements_temp_option_price.get(checkforall0));
                }
                if(checkforall0sum>0) //모든 옵션가격의 합이 0이라면 가격을 입력하지 않음.
                    // 옵션 하단에 가격 입력부분
                    for(int h = 0 ;h<menu.getOptions().get(i).getOptions().size();h++)
                    {
                        String temp;
                        if(Integer.parseInt(optionElements_temp_option_price.get(h)) == 0)
                            temp = optionElements_temp_option.get(h) + "\n0";
                        else
                            temp = optionElements_temp_option.get(h) + "\n+" + optionElements_temp_option_price.get(h)+"원";
                        optionElements_temp_option.set(h,temp);
                    }


            //토글버튼을 생성하고 옵션 엔트리를 적용
            multiStateToggleButton = new MultiStateToggleButton(this);
            LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonParam.setMargins(10, 0, 0, 15);
            multiStateToggleButton.setLayoutParams(buttonParam);
            multiStateToggleButton.setColors(getResources().getColor(R.color.gray_2), getResources().getColor(R.color.light_gray_2));
            ArrayList<String> optionElements = optionElements_temp_option;
            multiStateToggleButton.setElements((List<String>) optionElements_temp_option);

            //토글값변경시 콜백 메서드
            multiStateToggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int position) {
                    int temp = 0;
                    for(int i = 0; i<buttons.size();i++)
                    {
                        temp = temp + Integer.parseInt(menu.getOptions().get(i).getOptions_price().get(buttons.get(i).getValue()));
                    }
                    setTv_menu_price(temp);
                }
            });

            //새롭게 메뉴를 선택하는 건지, 기존 선택 된 메뉴의 변경인지에 따라서 토글버튼 초기 선택 값 지정
            //boolean array형태로 만들어서 적용해야함
            if(before_state==null){
                boolean[] state = new boolean[optionElements.size()];
                state[0] = true;
                multiStateToggleButton.setStates(state);
            }
            else{
                boolean[] before_button_state = new boolean[optionElements.size()];
                before_button_state[before_state.get(i)]=true;
                multiStateToggleButton.setStates(before_button_state);
            }
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.optionLayout);
            linearLayout.addView(option_name);
            linearLayout.addView(multiStateToggleButton);
            buttons.add(multiStateToggleButton);
//            setTheme();
        }

        //기존 선택 된 메뉴의 경우 표시되는 값을수정 selected->popup
        if(before_state!=null) {
            //버튼의 현재 선택 위치를 int arraylist 형태로 만듬
            ArrayList<Integer> choices = new ArrayList<>();
            for (int i = 0; i < buttons.size(); i++) {
                choices.add(buttons.get(i).getValue());
            }
            int sum_option_price = 0;//해당위치 옵션의 가격을 다더함
            for(int i=0;i<choices.size();i++)
            {
                sum_option_price = sum_option_price + Integer.parseInt(menu.getOptions().get(i).getOptions_price().get(choices.get(i)));
            }
            setTv_menu_price(sum_option_price);
        }
//        setTheme();
    }

    //값적용부분
    public void setTv_menu_price(int sum_option_price)
    {
        tv_menu_price.setText(NumberFormat.getInstance().format(Integer.parseInt(menu.getMenu_price())+sum_option_price) + "원");
        Log.i(TAG, "setTv_menu_price: "+sum_option_price);//삭제대상
    }

    //확인및 취소버튼 클릭 이벤트
    public void mOnClose(View v) {
//        //실시간 확인 타이머 종료
//        checkforprice_Timer.cancel();
//        t.cancel();

        //취소 버튼 선택시 바로 종료해버림
        if (v.getId() == R.id.cancelButton) {
            //setResult(RESULT_CANCELED);
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            return;
        }

        //버튼의 현재 선택 위치를 int arraylist 형태로 저장해서 넘겨줌
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getValue() < 0) {
                //버튼이 하나라도 선택 안되어있으면 선택해 달라고 Toast를 띄우고 return
                Toast.makeText(this, "선택해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                choices.add(buttons.get(i).getValue());
            }
        }

        Intent intent = new Intent();
        intent.putExtra("index", index);
        Log.d(TAG, "put index : " + index);
        intent.putExtra("menu", menu);
        intent.putIntegerArrayListExtra("options", choices);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleBitmap(imageView,menu.getBitmap());

    }
    private static void recycleBitmap(ImageView iv,String s) {
        if(s.equals("No_Image")==false)
        {
            Drawable d = iv.getDrawable();
            Bitmap b = ((BitmapDrawable) d).getBitmap();
            if (b != null && !b.isRecycled()) {

                b.recycle();
                b = null;
            } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
            d.setCallback(null);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        countDownReset();//카운트다운리셋
        //팝업 액티비티의 바깥쪽 터치를 무시하도록
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //하단 네비게이션 바를 숨겨놨지만 최하단을 위쪽으로 드래그하면 바가 나와서 뒤로가기 버튼 막음
        return;
    }

    @Override
    public void onResume(){
        super.onResume();

//        setTheme();
    }


}
