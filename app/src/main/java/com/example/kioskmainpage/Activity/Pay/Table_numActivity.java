package com.example.kioskmainpage.Activity.Pay;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceiptLoadingPopupActivity;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceipt_announce_Activity;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.ReceiptPopupActivity;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import java.util.ArrayList;
public class Table_numActivity extends AppCompatActivity implements View.OnClickListener{
    int type;
    EditText tablenum_edit;
    TextView[] numberPad = new TextView[10];
    ImageButton numberPad2;
//    String price;
//    ArrayList<SelectedMenu> selectedMenus;
    MainTheme mainTheme;
    ImageView imageView;
    TextView tablenum_TextView_title;
    TextView tablenum_numberPad_1,tablenum_numberPad_2,tablenum_numberPad_3,tablenum_numberPad_4,
            tablenum_numberPad_5,tablenum_numberPad_6,tablenum_numberPad_7,tablenum_numberPad_8,tablenum_numberPad_9,
            tablenum_numberPad_0,tablenum_numberPad_no1;
    Button btnFinish;
    Button btnCancel;
    LinearLayout tablenum_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().from(this).inflate(R.layout.activity_table_num,null);
        setContentView(view);
        //    countDownReset();//카운트다운리셋

        imageView = (ImageView)findViewById(R.id.tablenum_imageView);
        tablenum_layout = (LinearLayout)findViewById(R.id.tablenum_layout);
        tablenum_TextView_title = (TextView)findViewById(R.id.tablenum_TextView_title);
        tablenum_numberPad_1 =(TextView)findViewById(R.id.tablenum_numberPad_1);
        tablenum_numberPad_2 =(TextView)findViewById(R.id.tablenum_numberPad_2);
        tablenum_numberPad_3 =(TextView)findViewById(R.id.tablenum_numberPad_3);
        tablenum_numberPad_4 =(TextView)findViewById(R.id.tablenum_numberPad_4);
        tablenum_numberPad_5 =(TextView)findViewById(R.id.tablenum_numberPad_5);
        tablenum_numberPad_6 =(TextView)findViewById(R.id.tablenum_numberPad_6);
        tablenum_numberPad_7 =(TextView)findViewById(R.id.tablenum_numberPad_7);
        tablenum_numberPad_8 =(TextView)findViewById(R.id.tablenum_numberPad_8);
        tablenum_numberPad_9 =(TextView)findViewById(R.id.tablenum_numberPad_9);
        tablenum_numberPad_0 =(TextView)findViewById(R.id.tablenum_numberPad_0);
        tablenum_numberPad_no1 =(TextView)findViewById(R.id.tablenum_numberPad_no1);

//        Intent intent_sum =getIntent();
//        price =intent_sum.getStringExtra("sum");
//        selectedMenus = intent_sum.getParcelableArrayListExtra("selectedMenus");//메뉴구성 및 총가격 받아옴

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        tablenum_edit = (EditText) findViewById(R.id.tablenum_edit);

        btnFinish = (Button) findViewById(R.id.tablenum_btnFinish);
        btnCancel = (Button) findViewById(R.id.tablenum_btnCancel);
        btnCancel.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        for(int i=0;i<=9;i++) {
            numberPad[i] = (TextView) view.findViewWithTag("tablenum_numberPad_" + i);
            numberPad[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = tablenum_edit.getText().toString();
                    System.out.println( ((TextView)view).getText().toString() );
                    number += ((TextView)view).getText().toString();
                    tablenum_edit.setText(number);

                }
            });
        }
        numberPad2 = (ImageButton) view.findViewWithTag("tablenum_numberPad_10");//삭제버튼
        numberPad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = tablenum_edit.getText().toString();
                if (!number.equals("")) {
                    tablenum_edit.setText(number.substring(0, number.length() - 1));
                }
            }
        });

        setTheme();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tablenum_btnFinish://입력완료
                if(tablenum_edit.getText().toString().length()>0){
                    Log.i("Table_numActivity", "테이블번호: " + tablenum_edit.getText().toString());
                    Myapplication myapp = (Myapplication)getApplication();
                    myapp.setTablenum(tablenum_edit.getText().toString());
                    Intent intentTopay_popupActivity = new Intent(this,pay_popupActivity.class);
                    intentTopay_popupActivity.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    intentTopay_popupActivity.putExtra("sum",price+"원");
//                    intentTopay_popupActivity.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                    startActivity(intentTopay_popupActivity);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "테이블 번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tablenum_btnCancel:
                Intent intentToactivity_pay_popup_takeout = new Intent(this,activity_pay_popup_takeout.class);
                intentToactivity_pay_popup_takeout.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                intentToactivity_pay_popup_takeout.putExtra("sum",price+"원");
//                intentToactivity_pay_popup_takeout.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                startActivity(intentToactivity_pay_popup_takeout);
                finish();
                break;
        }
    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

    public void setTheme(){
        Myapplication app = (Myapplication)getApplication();//앱 객체 생성
        mainTheme = app.getMainTheme();// 테마 연결
        imageView.setImageResource(mainTheme.getThemeItem().getTABLE_NUMPAD_ICON_IMAGE());
        tablenum_TextView_title.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUM_TEXT_COLOR()));
        tablenum_edit.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));
        tablenum_layout.setBackgroundResource(mainTheme.getThemeItem().getNUMPAD_BACKGROUND_ROUND_COLOR());
        tablenum_numberPad_1.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_2.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_3.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_4.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_5.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_6.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_7.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_8.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_9.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_0.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_no1.setTextColor(getColor(mainTheme.getThemeItem().getTABLE_NUMPAD_TEXT_COLOR()));
        tablenum_numberPad_1.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_2.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_3.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_4.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_5.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_6.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_7.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_8.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_9.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_0.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        tablenum_numberPad_no1.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        numberPad2.setBackgroundResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKGROUND_COLOR());
        numberPad2.setImageResource(mainTheme.getThemeItem().getTABLE_NUMPAD_BACKSPACE_BUTTON());
        btnCancel.setBackgroundResource(mainTheme.getThemeItem().getCANCEL_BTN_BACKGROUND_COLOR());
        btnFinish.setBackgroundResource(mainTheme.getThemeItem().getCONFIRM_BTN_BACKGROUND_COLOR());
    }
}
