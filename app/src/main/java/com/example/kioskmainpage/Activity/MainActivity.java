package com.example.kioskmainpage.Activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Admin.Admin_MainActivity;
import com.example.kioskmainpage.Activity.Admin.Admin_checkfor_password;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceipt_announce_Activity;
import com.example.kioskmainpage.Activity.Pay.PayingPopupActivity;
import com.example.kioskmainpage.Activity.Pay.activity_pay_popup_takeout;
import com.example.kioskmainpage.Activity.Pay.pay_popupActivity;
import com.example.kioskmainpage.Activity.Service_foreground.PersistentService;
import com.example.kioskmainpage.Activity.Service_foreground.RestartService;
import com.example.kioskmainpage.Activity.Sign.SignInActivity;
import com.example.kioskmainpage.Activity.Waiting.Waiting_MainActivity;
import com.example.kioskmainpage.Adapter.PagerAdapter;
import com.example.kioskmainpage.Adapter.RAdapter;
import com.example.kioskmainpage.Adapter.RAdapter2;
import com.example.kioskmainpage.Adapter.SelectedListAdapter;
import com.example.kioskmainpage.Fragment.PageFragment;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.ServerConn.BlankAsynkTask;
import com.example.kioskmainpage.SoldoutCheck;
import com.example.kioskmainpage.Utilities.Chekers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;//기존의 액티비티를 재사용
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "testtest**MAIN";
    TabLayout tabLayout;
    VerticalTabLayout VtabLayout;
    ViewPager viewPager;
    AppBarLayout appBarLayout;
    FrameLayout Tab_FrameLayout;
    Button cancelButton;
    Button payButton;
    BroadcastReceiver screenOnOff;
    ArrayList<String> sold_out_menu_names = new ArrayList<>();//매진 메뉴 리스트
    String result_json="";//서버 전송시 리턴값
    String token_send_url="http://mobilekiosk.co.kr/admin/api/login/TOKEN_SEND.php";//토큰 전송 url
    String sold_out_url = "http://mobilekiosk.co.kr/admin/api/get_soldout.php";//솔드아웃메뉴불러옴
    ListView selectedListview;
    public SelectedListAdapter adapter;
    PagerAdapter pagerAdapter;
    public ArrayList<SelectedMenu> selectedMenus = new ArrayList<SelectedMenu>();
    public static CountDownTimer countDownTimer,countDownTimer2;//BestNewMenuActivity로 이동하는 카운트다운
    boolean mainactivity_status=false;//메인엑티비티 상태
    Myapplication myapp;
    RelativeLayout bottom_layout;
    private ArrayList<String> folder_names = new ArrayList<>();

    String bizNum;
    int resume_count = 0;
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("메인메뉴 pause");
        countDownTimer.cancel();//다른 액티비티 진입 시 타이머 중지
        //countDownTimer=null;
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        System.out.println("메인메뉴 stop");
        countDownTimer.cancel();//다른 액티비티 진입 시 타이머 중지
    }*/

    @Override
    protected void onResume(){
        super.onResume();
        //취소등으로 메인Activity로 복귀 하였을경우 메뉴갯수확인
        if (adapter.getselectedMenus().size() > 0)
            this.showMenu();

        System.out.println("메인메뉴 resume");
        if(resume_count!=0)
        {
            if(bottom_layout.getVisibility() == View.GONE)//하단 메뉴 선택 레이아웃이 안보여질때만 카운터
            countDownTimer.start();//다시 액티비티로 돌아오면 타이머 시작
        }
        else
        {
            resume_count++;
        }

        //화면 다시 올라올때마다 테마 설정
        setTheme();
        countDownReset(); //타이머 리셋
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void setTheme()
    {
        viewPager.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getBACKGROUND_COLOR_ID()));
        appBarLayout.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getTAB_COLOR_ID()));
        if(Chekers.check_device_type().equals("NORMAL")) {
            tabLayout.setSelectedTabIndicatorColor(getColor(myapp.getMainTheme().getThemeItem().getTAB_SELECTEDTAB_INDICATE_BACKGROUND()));
            tabLayout.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getTAB_COLOR_ID()));
            tabLayout.setTabTextColors(getColor(myapp.getMainTheme().getThemeItem().getTAB_TEXT_COLOR_ID()), getColor(myapp.getMainTheme().getThemeItem().getTAB_SELECTED_TEXT_COLOR_ID()));
        }
        Tab_FrameLayout.setBackgroundColor(myapp.getMainTheme().getThemeItem().getTAB_COLOR_ID());
        bottom_layout.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getBACKGROUND_COLOR_ID())); //선택메뉴 레이아웃 배경 색
//        cancelButton.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getOPTION_TOGGLE_COLOR())); //선택메뉴 레이아웃 취소버튼 색
        payButton.setBackgroundResource(myapp.getMainTheme().getThemeItem().getMAIN_PAY_BUTTIN_COLOR()); //선택메뉴 레이아웃 주문버튼 색
    }
    @Override //0715추가
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
        mainactivity_status = hasFocus;
    }

    BroadcastReceiver receiver;
    Intent intentMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Chekers.check_device_type().equals("PLUS")) {
            setContentView(R.layout.activity_main_landscape_plus);
        }
        else {
            setContentView(R.layout.activity_main);
        }

//------------------------------------------------Foregronud 설정용_서비스 초기 생성 후 강제 종료시 재생성 (서비스 유지함)
        intentMyService = new Intent(this, PersistentService.class);

        // 리시버 등록
        receiver = new RestartService();

        try
        {
            IntentFilter mainFilter = new IntentFilter("ACTION.RESTART.PersistentService");

            // 리시버 저장
            registerReceiver(receiver, mainFilter);

            // 서비스 시작
            startService(intentMyService);

        } catch (Exception e) {

            Log.d("MpMainActivity", e.getMessage()+"");
            e.printStackTrace();
        }
//-------------------------------------------------------------------------------------
        bottom_layout = (RelativeLayout)findViewById(R.id.bottomLayout);
        Intent intent = getIntent();

        Myapplication app = (Myapplication)getApplication();
        app.setMainActivityContext(this);

        setScreenchecker();//화면 켜짐꺼짐 확인

        Button hidden = (Button) findViewById(R.id.hidden_menu_button2);
        hidden.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)//버튼 눌렀을때
                {
                    countDownTimer2();
                    countDownTimer2.start();//타이머 시작 (5초이상 누르고 있으면 히든 메뉴 진입)
                    System.out.println("히든버튼 누름");
                }
                else if(action == MotionEvent.ACTION_UP)//손 뗐을 때
                {
                    countDownTimer2.cancel();
                    countDownTimer2=null;
                    System.out.println("히든버튼 손똄");
                }
                return false;
            }
        });

  /*      hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
        folder_names = intent.getStringArrayListExtra("folderNames");

        //상,하단 바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //카운트다운을위해추가됨
        countDownTimer();

        myapp=(Myapplication) getApplication();
        bizNum = myapp.getBizNum();
        Log.d("main","biznum 사업자번호 : "+bizNum);

        myapp.setRAdapters(new ArrayList<RAdapter>());//실시간 soldout 처리에 이용하기위해서 radapter 전역변수로 저장

        myapp.setCountDownTimer(countDownTimer);
        //메뉴 선택,확인 시 하단에 추가 될 리스트 뷰 어댑터 생성 및 적용
        selectedListview = (ListView) findViewById(R.id.selected_items);
        adapter = new SelectedListAdapter(selectedMenus, MainActivity.this,"MainActivity");
        selectedListview.setAdapter(adapter);

        //처음 실행시 전체 매진목록을 받아오는 AsynkTesk
        SoldoutCheck soldoutCheck = new SoldoutCheck();
        soldoutCheck.init(myapp);
        soldoutCheck.execute(sold_out_url,bizNum);

        //Fragment를 생성해서 카테고리별 메뉴를 보여줄 뷰페이져 어댑터 생성 및 적용
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
   //     viewPager.setBackgroundColor(getColor(myapp.getMainTheme().getThemeItem().getBACKGROUND_COLOR_ID()));//테마
        Log.d(TAG, "set adapter to view pager");
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this, folder_names,myapp.getBest_menus(),bizNum);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(folder_names.size());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)//버튼 눌렀을때
                {
             //       System.out.println("타이머 리셋");
                }
                else if(action == MotionEvent.ACTION_UP)//손 뗐을 때
                {
                   /// System.out.println("타이머 리셋2");
                    countDownTimer.cancel();//타이머 취소
                    countDownTimer.start();
                }
                return false;
            }
        });


        //하단에 메뉴추가시 생기는 버튼 클릭이벤트에 대한 리스너 적용
        onClickButton onClickHandler = new onClickButton(adapter);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(onClickHandler);
        payButton = (Button)findViewById(R.id.payButton);
        payButton.setOnClickListener(onClickHandler);

        if(Chekers.check_device_type().equals("NORMAL"))
        {
            //탭 + 뷰페이져 적용
            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            Log.d(TAG, "tablayout setup with view pager");
            tabLayout.setupWithViewPager(viewPager);//탭 + 뷰페이져 적용
            Tab_FrameLayout = (FrameLayout) findViewById(R.id.sliding_tabs_frame);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    LinearLayout tabLayout_temp = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                    TextView tabTextView = (TextView) tabLayout_temp.getChildAt(1);
                    tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
                    tabTextView.setTextScaleX(1.1f);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    LinearLayout tabLayout_temp = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                    TextView tabTextView = (TextView) tabLayout_temp.getChildAt(1);
                    tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
                    tabTextView.setTextScaleX(1);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
        else if(Chekers.check_device_type().equals("PLUS"))
        {
            //탭 + 뷰페이져 적용
            VtabLayout = (VerticalTabLayout) findViewById(R.id.vertical_tabs);
            Log.d(TAG, "VtabLayout setup with view pager");
            VtabLayout.setIndicatorWidth((Integer)(VtabLayout.getWidth()*2/3));
            VtabLayout.setupWithViewPager(viewPager);//탭 + 뷰페이져 적용
            Tab_FrameLayout = (FrameLayout) findViewById(R.id.sliding_tabs_frame);
            VtabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabView tab, int position) {

                }

                @Override
                public void onTabReselected(TabView tab, int position) {

                }
            });
        }

        //주문번호 전역변수를 위함

        //myapp.setOrderNum(0); 100자리 이상 난수를 위해 제거됨
        myapp.getOrderNum();

        new Handler().postDelayed(new Runnable() //처음 메뉴내릴시 어케 처리할까하다가 그냥 딜레이를 주고 Best New메뉴로 이동하게만듬
        {
            @Override
            public void run()
            {
                if(myapp.isitWaitingOn()) { //제거되어야함
                    //waiting으로 이동
                    Intent intent_Waiting = new Intent(MainActivity.this, Waiting_MainActivity.class);
                    intent_Waiting.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                    intent_Waiting.putExtra("folderNames", folder_names);
                    startActivityForResult(intent_Waiting, 5);
                    countDownTimer.cancel();
                }
                else {
                    //BestNewMenu액티비티 띄움 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)*/
                    Intent intent_BestNewMenu = new Intent(MainActivity.this, BestNewMenuActivity.class);
                    intent_BestNewMenu.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                    intent_BestNewMenu.addFlags( FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                    intent_BestNewMenu.putExtra("folderNames", folder_names);
                    startActivityForResult(intent_BestNewMenu, 5);
                    countDownTimer.cancel();
                }
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 1000);

        SearchingToken();
        //앱을 재시작하더라도 fcm유지
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        setTheme();//테마 설정
    }

    public void onDestroy() {
        super.onDestroy();
        // 리시버 삭세를 하지 않으면 에러
        Log.d("MpMainActivity", "Service Destroy");
        unregisterReceiver(receiver);
        unregisterReceiver(screenOnOff);
        myapp.getBackground_checkers().unreregister_broadcastReceiver();
        myapp.setMainActivityContext(null);
    }

    public void setadapter(SelectedListAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        System.out.println( "onNewIntent 호출됨" );// 인텐트를 받은 경우만, 값을 Activity로 전달하도록 합니다.
        if( intent != null )
        {
            processIntent( intent );

        }
        super.onNewIntent(intent);
    }
    // 인텐트를 처리하도록 합니다.
    private void processIntent( Intent intent ) {

        String title = intent.getStringExtra("title");//sold out(혹은 cancel) 시킬 제품명
        String content = intent.getStringExtra("content");//sold out 인지 soldout cancel인지
        Log.d(TAG, "processIntent: "+"title "+title+"content "+content);

        //B&N이 죽지 않게 됨에따라 onActivityResult에서 바로 작동되지 않음.
        if(intent.getBooleanExtra("isOrdered",false))
        onActivityResult(5,RESULT_OK,intent);

        if (content!=null&&content.equals("SOLD_OUT")) //매진
        {
            ArrayList<RAdapter> RAdapters= myapp.getRAdapters();

            Log.d( TAG, "매진 아이템 : "+ title );
            //TODO : 매진 처리
            Log.d(TAG,"RADAPTERs 사이즈 : "+RAdapters.size());
            for(int i=0;i<RAdapters.size();i++)
            {
                RAdapters.get(i).setSoldout(true,title);
            }
            //선택목록제거를 위한부분.
            boolean exist = false;
            for(int i =0;i<adapter.getselectedMenus().size();i++)
            {
                if(adapter.getselectedMenus().get(i).getMenu_name().equals(title))
                {
                    exist =true;
                    adapter.onActivityResult(i, null, 4);
                    adapter.notifyDataSetChanged();
                }
            }
            if (selectedMenus.size() == 0)
                if(Chekers.check_device_type().equals("NORMAL"))
                    (this).hideMenu();
            //Main이 resume 된것이 아니라면 다이얼로그를 호출하지 않음
            if(mainactivity_status){
                if(exist==true){
                    blankDialogForRemoveSelectedList(title);
                }
                else
                    blankDialog();//TODO: 나중에 연결하여 수정할것
            }

        }// 메시지를 받은 것우 처리를 합니다.
        else if(content!=null&&content.equals("SOLD_OUT_CANCEL"))//매진해제
        {
            ArrayList<RAdapter> RAdapters= myapp.getRAdapters();

            Log.d( TAG, "매진 해제 아이템 : "+ title );
            //TODO : 매진 처리
            Log.d(TAG,"RADAPTERs 사이즈 : "+RAdapters.size());
            for(int i=0;i<RAdapters.size();i++)
            {
                RAdapters.get(i).setSoldout(false,title);
            }
            blankDialog();//TODO: 나중에 연결하여 수정할것
        }
        if(content!=null&&content.equals("CHECK_DEVICE")){
            screen_to_Sever();
        }

        return;
    }

    public void SearchingToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("토큰없음");
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token 기기 식별자
                        String token = task.getResult().getToken();
                        myapp.setToken(token);
                        // Log and toast
                        String msg = token;
                        System.out.println("토큰 : "+token);

                        //토큰을 서버로 전송하고 디바이스 번호 받음
                        InsertData insertData = new InsertData();
                        insertData.execute(token_send_url, myapp.getBizNum(), token);
                        Log.d(TAG, msg);
              //          Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void SendMessaging(String SENDER_ID,int messageId,String message)
    {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder("612427862546" + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());

    }

    public void hideMenu() {
        //하단 레이아웃 visibility를 gone으로 설정해서 공간을 차지하지 않고 보이게 않게 만듬

        bottom_layout.setVisibility(View.GONE);
        //countDownTimer.start(); 하단레이아웃 숨길시 타이머 시작
    }

    public void showMenu() {
        //하단 레이아웃 visibility를 gone으로 설정해서 공간을 차지하지 않고 보이게 않게 만듬

        bottom_layout.setVisibility(View.VISIBLE);
        //countDownTimer.cancel(); 하단레이아웃 출연시 타이머 시작
    }

    public class onClickButton implements View.OnClickListener {
        SelectedListAdapter adapter;

        public onClickButton(SelectedListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancelButton:
                    //취소 버튼 클릭 시 실행
                    adapter.clearItem(adapter);
                    if(Chekers.check_device_type().equals("NORMAL"))
                    hideMenu();
                    Toast.makeText(view.getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.payButton:
                    //주문하기 버튼 클릭 시 실행
                    int price =adapter.getPriceSum();
                    Log.d(TAG,"total price ; " + price);

//                    Toast.makeText(view.getContext(),"총 금액 : " + price,  Toast.LENGTH_SHORT).show();
                    System.out.println("어댑터 : "+adapter.getselectedMenus().get(0).getMenu_name());
                    myapp.setadapter(adapter); //전역변수로써 adapter 넘겨줌
//                    myapp.setadapter_setselectedMenus(adapter);
                    myapp.setCurrentOrder_price(price);

                    Intent intent3=new Intent(view.getContext(), activity_pay_popup_takeout.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    intent3.putExtra("sum",price+"원");
//                    intent3.putParcelableArrayListExtra("selectedMenus",selectedMenus);
                    startActivity(intent3);
                    //주문버튼 누를시에 pay_popupActivity로 전환, 총 금액도 전달
                    if(Chekers.check_device_type().equals("NORMAL"))
                        hideMenu();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //팝업 액티비티의 결과를 받는 부분
      //  countDownReset(); //BestNewMenuActivity로 이동하는 카운트다운을 리셋
        Log.i(TAG, "onActivityResult: requestCode : "+requestCode+" resultCode : "+resultCode);
        Menu menu;
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 1:
                    //메뉴 이미지를 클릭해서 팝업 액티비티를 띄웠을 때 requestCode == 1

                    //인텐트에서 선택된 메뉴의 데이터를 받은 후
                    menu = data.getParcelableExtra("menu");
                    ArrayList<Integer> choices = data.getIntegerArrayListExtra("options");

                    //하단 리스트뷰가 있는 레이아웃 visibility를 VISIBLE로 바꿔 보이도록 설정
                    showMenu();
                    //어댑터 내부 arraylist안에 추가 후 변화를 알림
                    adapter.addItem(menu, choices);
                    adapter.notifyDataSetChanged();
                    break;

                case 2:
                    //선택된 메뉴리스트에서 변경 버튼을 눌러 팝업 액티비티를 띄웠을 때 requestCode == 2

                    //변경된 메뉴의 adapter 내부 arraylist에서의 인덱스, 바뀐 선택들을 받음
                    int index = data.getIntExtra("index", -1);
                    ArrayList<Integer> mod_choices = data.getIntegerArrayListExtra("options");

                    //어탭터 내부 arraylist의 해당 인덱스 SelectedMenu 객체의 options attribute를 재설정하고 변화를 알림
                    adapter.onActivityResult(index, mod_choices, 2);
                    adapter.notifyDataSetChanged();
                    break;
                case 4:
                    //선택된 메뉴리스트에서 삭제 버튼을 눌러 삭제 액티비티를 띄웠을 때 requestCode == 4

                    //변경된 메뉴의 adapter 내부 arraylist에서의 인덱스, 바뀐 선택들을 받음
                    index = data.getIntExtra("index", -1);

                    //어탭터 내부 arraylist의 해당 인덱스 SelectedMenu 객체의 options attribute를 재설정하고 변화를 알림
                    adapter.onActivityResult(index, null, 4);
                    adapter.notifyDataSetChanged();
                    if (selectedMenus.size() == 0)
                        if(Chekers.check_device_type().equals("NORMAL"))
                            (this).hideMenu();
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    //B&N로 부터 복귀 requestCode == 5
                    Log.d(TAG, "onActivityResult: "+" 옴");
                    if(data.getBooleanExtra("isOrdered",false))
                    {
                        menu = data.getParcelableExtra("menu");
                        choices = data.getIntegerArrayListExtra("options");

                        //하단 리스트뷰가 있는 레이아웃 visibility를 VISIBLE로 바꿔 보이도록 설정
                        showMenu();
                        //어댑터 내부 arraylist안에 추가 후 변화를 알림
                        adapter.addItem(menu, choices);
                        //adapter.notifyDataSetChanged();
                        //Myapplication myapp = (Myapplication) getApplication();
                        //adapter.setselectedMenus(myapp.getadapter().getselectedMenus());
                        adapter.notifyDataSetChanged();
                        //if (adapter.getCount() > 0) showMenu();
                    }
                    break;
                case 6:
                    //admin에서 오는 2가지 종류 로그아웃과 데이터동기화.
                    switch (myapp.getAdminToMainType()) {
                        case "SYNC" :
                            //데이터 동기화 구현 = 데이터 동기화 메뉴 -> 기기 관리 -> 관리자 메뉴 -> 메인액티비티 -> 로딩페이지로 이동함.
                            Intent intent_SYNC = new Intent(getApplicationContext(), SignInActivity.class);
                            intent_SYNC.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                            intent_SYNC.putExtra("bizNum",myapp.getBizNum());
                            startActivity(intent_SYNC);
                            finish();
                            break;
                    }

                case 7:
                    //시간초과에 따른 B&N이동 타이머 작동 waiting이 적용될경우 waiting으로 이동

                    adapter.clearItem(adapter);// 타이머 종료시 어댑터(현재 선택목록) 초기화시킴
                    if(Chekers.check_device_type().equals("NORMAL"))
                        hideMenu();
                    Toast.makeText(MainActivity.this, "화면을 이동합니다.", Toast.LENGTH_SHORT).show();
                    if(myapp.isitWaitingOn()) {
                        Intent intent_waitingMain = new Intent(MainActivity.this, Waiting_MainActivity.class);
                        intent_waitingMain.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                        intent_waitingMain.putExtra("folderNames", folder_names);
                        startActivity(intent_waitingMain);
                    }
                    else{
                        Intent intent_BestNewMenu = new Intent(MainActivity.this, BestNewMenuActivity.class);
                        intent_BestNewMenu.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                        intent_BestNewMenu.addFlags( FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                        intent_BestNewMenu.putExtra("folderNames", folder_names);
                        startActivityForResult(intent_BestNewMenu, 5);
                    }
                    viewPager.setCurrentItem(0);//초기선택페이지를 가장처음으로.
                    //스크롤을 가장위로 올림 
                    List<Fragment> temp_fragmentList = getSupportFragmentManager().getFragments();
                    for(Fragment PFT : temp_fragmentList)
                    {
                        PageFragment PF = (PageFragment)PFT;
                        PF.getFragment_scrollView().setScrollY(0);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Intent intent2Splash =new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent2Splash);
        System.out.println("홈버튼");
    }

    public void countDownTimer(){
        countDownTimer = new CountDownTimer(60*1000, 1000) { // 1000당 1초   2분 1초당 카운트 TODO: BN으로 가는 타이머 10초되있음 기능확정시 수정할것

            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Intent intent_GoResetPopup = new Intent(MainActivity.this, SelectedResetPopup_Activity.class);
                if(adapter.getPriceSum()==0) //선택된 메뉴들의 가격합이 0일경우 = 선택된 메뉴가없음
                intent_GoResetPopup.putExtra("list_empty",true);
                intent_GoResetPopup.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                startActivityForResult(intent_GoResetPopup, 7);
            }
        };
    }
    public void countDownTimer2(){//히든메뉴 접속용 타이머
        countDownTimer2 = new CountDownTimer(2*1000+500, 1000) { // 1000당 1초   5분 1초당 카운트
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {

               //5초동안 누르면 HiddenMenuActivity 띄움

                Intent intent = new Intent(MainActivity.this, Admin_checkfor_password.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                startActivity(intent);
            }
        };
    }
    public static void countDownReset() { // BestNewMenuActivity로 이동하는 카운트다운을 리셋
        countDownTimer.cancel();
        Log.i(TAG, "countDownReset됨");
        countDownTimer.start();
    }

    //server에 요청
    class InsertData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            result_json=result;
            validReturnValue();
        }


        @Override
        protected String doInBackground(String... params) {

            String mb_id = (String)params[1];
            String token = (String)params[2];

            String serverURL = (String)params[0];
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            String postParameters = "mb_id="+mb_id+"&token="+token+"&device_id="+android_id;
            System.out.println(postParameters);
            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                System.out.println("json 리턴: "+sb.toString());
                bufferedReader.close();


                return sb.toString();
            }catch (Exception e) {

                return new String("Error: " + e.getMessage());
            }

        }
    }
    protected void validReturnValue(){//서버에 회원가입 요청 보내고 온 정보 json 형태로 받고 pasing

        try{
            JSONObject jsonObj = new JSONObject(result_json);

            String mb_id = jsonObj.getString("mb_id");//사업자 번호
            String device_no = jsonObj.getString("device_no");//기기번호
            String table_status = jsonObj.getString("table_status");//테이블 번호 옵션 여부


            Myapplication app = (Myapplication)getApplication();
            if(table_status.equals("Y"))
            {
                app.setTable_status("T");
            }
            else
            {
                app.setTable_status("F");
            }
                String mb_id_origin = app.getBizNum();//기기에 로그인된 사업자아이디
            if(mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인 후 device no 저장
            {
                app.setDeviceNo(device_no);
//                app.setTakeout_status("table_status");TODO: 서버와의 테이블 설정 여부 연동 완료시 주석풀것.
            }
            else//사업자 번호 불일치 - 실패
            {
                Log.d(TAG,"사업자 번호 불일치");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG,"토큰 전송 완료.");

    }
    public void blankDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("품절안내").setMessage("품절 적용중입니다.");
        final AlertDialog soldoutdialog = builder.create();
        soldoutdialog.show();
        new Handler().postDelayed(new Runnable() {

            public void run() {

                soldoutdialog.dismiss();

            }

        }, 2000);//2초
    }
    public void blankDialogForRemoveSelectedList(String s)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message ="품절 제품이 선택목록에서 제거되었습니다.\n제품명 : "+s;
        builder.setTitle("품절안내").setMessage(message);
        final AlertDialog soldoutdialog = builder.create();

        soldoutdialog.show();
        new Handler().postDelayed(new Runnable() {

            public void run() {

                soldoutdialog.dismiss();

            }

        }, 4000);//2초
    }
    public void setScreenchecker() {
        IntentFilter intentFilterForScreen = new IntentFilter();
        intentFilterForScreen.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilterForScreen.addAction(Intent.ACTION_SCREEN_ON);

        screenOnOff = new BroadcastReceiver()
        {
            public static final String ScreenOff = "android.intent.action.SCREEN_OFF";
            public static final String ScreenOn = "android.intent.action.SCREEN_ON";

            public void onReceive(Context contex, Intent intent)
            {
                if (intent.getAction().equals(ScreenOff))
                {
                    Log.e(TAG, "Screen Off");
                    myapp.setScreenonoff("SCREEN_OFF");
                    screen_to_Sever();
                }
                else if (intent.getAction().equals(ScreenOn))
                {
                    Log.e(TAG, "Screen On");
                    myapp.setScreenonoff("SCREEN_ON");
                    screen_to_Sever();
                }
            }
        };
        registerReceiver(screenOnOff,intentFilterForScreen);
    }
    public void screen_to_Sever(){
        ArrayList<String> Screenstatus_send_parameters=new ArrayList<String>();// 보낼 파라미터명들
        ArrayList<String> Screenstatus_input_datas=new ArrayList<String>();// 보낼 파라미터값들
        ArrayList<String> Screenstatus_get_datas=new ArrayList<String>();// 받을 파라미터명들
        Screenstatus_send_parameters.add("mb_id");
        Screenstatus_input_datas.add(myapp.getBizNum());
        Screenstatus_send_parameters.add("token");
        Screenstatus_input_datas.add(myapp.getToken());
        Screenstatus_send_parameters.add("device_no");
        Screenstatus_input_datas.add(myapp.getDeviceNo());
        Screenstatus_send_parameters.add("screen_state");
        Screenstatus_input_datas.add(myapp.getScreenonoff());
        Screenstatus_get_datas.add("mb_id");Screenstatus_get_datas.add("device_no");
        Screenstatus_get_datas.add("screen_state");//TODO:서버에서 화면 API오면변경하기
        BlankAsynkTask sendScreenstatus = new BlankAsynkTask(myapp.getBizNum(),"sand_ScreenSatus",Screenstatus_send_parameters,Screenstatus_input_datas,Screenstatus_get_datas,"http://mobilekiosk.co.kr/admin/api/screen.php");
        sendScreenstatus.execute();
    }

    public boolean onTouchEvent(MotionEvent event) // 외부 터치불가
    {
        if(myapp.getCountDownTimer()!=null) {
            myapp.getCountDownTimer().cancel();
            Log.i(TAG, "countDownReset됨");
            myapp.getCountDownTimer().start();
        }
        return false;
    }


}
