package com.example.kioskmainpage.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Admin.SyncCompleteActivity;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.Activity.Waiting.Order_Method_Selection_Popup;
import com.example.kioskmainpage.Adapter.AutoScrollAdapter;
import com.example.kioskmainpage.Adapter.RAdapter2;
import com.example.kioskmainpage.Adapter.SelectedListAdapter;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.MenuManage.MenuManager;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.CameraPreview;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class BestNewMenuActivity extends AppCompatActivity implements Camera.FaceDetectionListener{

    /* 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)*/

    TextView allmenu,easy_order_move;

    //  BestMenuAdapter mBestMenuAdapter ;
    AutoScrollViewPager mViewPager;
    public static Activity activity;

    ListView selectedListview;
    public SelectedListAdapter adapter;
    public ArrayList<SelectedMenu> selectedMenus = new ArrayList<SelectedMenu>();

    AutoScrollAdapter autoScrollAdapter;
    RecyclerView recyclerView;
    private RAdapter2 NewMenuadapter;

    private ArrayList<Menu> menus = new ArrayList<>();//best
    private ArrayList<Menu> menus2 = new ArrayList<>();//new

    private ArrayList<String> folder_names = new ArrayList<>();
    private ArrayList<String> tab_names;
    public Context context;

    private static final String TAG = "BestNewMenuActivity";
    MenuManager menuManager, menuManager2;
    TextView best_textview;
    ArrayList<String> best_menus, new_menus, best_image_links, best_menus_ments, new_image_links;
    LinearLayout rootlayout;
    boolean isScroll = true;
    MainTheme mainTheme;
    AutoScroll AutoScrollForBN = new AutoScroll();

    Myapplication myapp = (Myapplication) getApplication();


    @Override
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

    //테마 설정
    public void setTheme() {
        Myapplication app = (Myapplication) getApplication();
        mainTheme = app.getMainTheme();

        rootlayout.setBackgroundColor(getColor(mainTheme.getThemeItem().getBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID()));
        allmenu.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        allmenu.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(mainTheme.getThemeItem().getBEST_NEW_PASS_RIGHT_IC_ID()), null);//테마
        best_textview.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        recyclerView.setBackgroundColor(getColor(mainTheme.getThemeItem().getNEW_MENU_BACKGROUND_COLOR_ID()));
    }

    //--------------------------------------------얼굴인식 전역변수
    private static CameraPreview surfaceView;
    private SurfaceHolder holder;
    private static Button camera_preview_button;
    private static Camera mCamera;
    private int RESULT_PERMISSIONS = 100;
    public static BestNewMenuActivity getInstance;
    public static boolean STATE = false;
    public AttributeSet att;
    ImageView cameraFocus;
    //----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_new_menu);

        Myapplication app = (Myapplication) getApplication();
        mainTheme = app.getMainTheme();
        //상,하단 바 제거ㅠ
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //------------------------------------------------------------얼굴인식
        setInit();
        activity = BestNewMenuActivity.this;
        mCamera.setFaceDetectionListener(this);
        //-----------------------------------------------------------

        rootlayout = (LinearLayout) findViewById(R.id.best_new_rootlayout);
        //  rootlayout.setBackgroundColor(getColor(mainTheme.getThemeItem().getBEST_NEW_ACTIVITY_BACKGROUND_COLOR_ID()));//테마

        //전체 메뉴 보기 버튼(클릭시 BestNewMenu액티비티가 꺼지면서 자연스럽게 MainActivity가 등장
        allmenu = (Button) findViewById(R.id.allmenu_bestmenu);//2020.04.03텍스트에서 버튼으로 수정
        //  allmenu.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        //     allmenu.setCompoundDrawablesWithIntrinsicBounds(null,null, getDrawable(mainTheme.getThemeItem().getBEST_NEW_PASS_RIGHT_IC_ID()),null);//테마
        best_textview = (TextView) findViewById(R.id.best_bestmenu);//BEST
        // best_textview.setTextColor(getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        app.setBestNewMenuActivityContext(this);
        Intent intent = getIntent();
        folder_names = intent.getStringArrayListExtra("folderNames");

        easy_order_move = (Button) findViewById(R.id.easy_order);//easy order intent

        //best메뉴 이름들 가져오기
        best_menus = app.getBest_menus();
        new_menus = app.getNew_menus();
        best_menus_ments = app.getBest_ments();
        best_image_links = app.getBest_image_links();
        new_image_links = app.getNew_image_links();
        menuManager = new MenuManager(this.getFilesDir().getAbsolutePath() + "", folder_names, app.getBizNum());
        menuManager2 = new MenuManager(this.getFilesDir().getAbsolutePath() + "", folder_names, app.getBizNum());
        getBestMenusList();//best메뉴리스트 만들기
        getNewMenuList();//new메뉴리스트만들기
        TextView best_mb_name = (TextView)findViewById(R.id.best_mb_name);
        best_mb_name.setText(app.getUser_data().get(0));

        //best메뉴 생성
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewpager_best);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewPager.getLayoutParams();
        autoScrollAdapter = new AutoScrollAdapter(this, menus, layoutParams.height, mainTheme);
        mViewPager.setAdapter(autoScrollAdapter);
        //mViewPager.setInterval(5000);기존속도
        mViewPager.setInterval(7500);
        mViewPager.setScrollDurationFactor(2);
        mViewPager.startAutoScroll();
        mViewPager.setCurrentItem(0);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);


        //  new메뉴 생성
//        Log.i("menus : ",menus2.get(0).menu_name);


        NewMenuadapter = new RAdapter2(menus2, mainTheme, (Myapplication) getApplication());
        recyclerView = (RecyclerView) findViewById(R.id.new_menu_recyclerview);
//        recyclerView.setBackgroundColor(getColor(mainTheme.getThemeItem().getNEW_MENU_BACKGROUND_COLOR_ID()));//테마

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.offsetChildrenHorizontal(10);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(NewMenuadapter);

        //item 터치 시 자동 스크롤 정지
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                isScroll = false;//터치 이벤트 발생시 자동 스크롤 정지
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        //자동 스크롤 중 맨 마지막에 가게 되면 다시 첫번쨰로 돌아감(무한 순환)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstItemVisible = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstItemVisible != 0 && firstItemVisible % menus2.size() == 0) {
                    recyclerView.getLayoutManager().scrollToPosition(0);
                }
            }
        });


        AutoScrollForBN.restartAutoScroll();//isScroll이 false로 변경됏을때 5초 후 다시 true로 변경해서 다시 스크롤 시작
        AutoScrollForBN.autoScroll();//recyclerview 자동스크롤
        // 하단바 생성을 위한것들

        //메뉴 선택,확인 시 하단에 추가 될 리스트 뷰 어댑터 생성 및 적용
        selectedListview = (ListView) findViewById(R.id.selected_items);
        adapter = new SelectedListAdapter(selectedMenus, BestNewMenuActivity.this, "BestNewMenuActivity");
        selectedListview.setAdapter(adapter);

        allmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myapplication myapp = (Myapplication) getApplication();
                myapp.setadapter(adapter); //전역변수로써 넘겨줌
                Intent intent = new Intent(BestNewMenuActivity.this, MainActivity.class);
                intent.putExtra("isOrdered", false);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                setResult(RESULT_OK, intent); //설정했다고 알림
                startActivity(intent);
                finish();
                return;
            }
        });



        easy_order_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Myapplication myapp = (Myapplication) getApplication();
                myapp.setadapter(adapter); //전역변수로써 넘겨줌
                Intent intent = new Intent(BestNewMenuActivity.this, EasyMenuSelectionActivity.class);
                intent.putExtra("isOrdered", false);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                setResult(RESULT_OK, intent); //설정했다고 알림
                startActivity(intent);
                finish();
                return;
            }
        }); //간편메뉴 이동


        SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);//동기화 완료시에만 실행
        Log.d(TAG, "onCreate: " + sharedPreferences.getString("SyncCheck", ""));
        switch (sharedPreferences.getString("SyncCheck", "")) {
            case "SYNC":
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SyncCheck", "DEFAULT");//동기화 설정
                editor.commit();
                Intent intentToSyncComplete = new Intent(BestNewMenuActivity.this, SyncCompleteActivity.class);
                intentToSyncComplete.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intentToSyncComplete);
                break;
        }

        CountDownTimer_foreground_use1();//타이머 반복으로 이벤트 연속발생

//        setTheme();

    }

    public static Camera getCamera(){
        return mCamera;
    }
    private void setInit(){
        getInstance = this;

        mCamera = Camera.open(1);
        setContentView(R.layout.activity_best_new_menu);
        surfaceView = (CameraPreview) findViewById(R.id.preview);

        // SurfaceView 정의 - holder와 Callback을 정의한다.
        holder = surfaceView.getHolder();
        holder.addCallback(surfaceView);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCamera.setFaceDetectionListener(this);

        STATE = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        STATE = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.setFaceDetectionListener(this);

        if (STATE != true) {
            STATE = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        STATE = false;
    }

    public int facedetection_count=0;
    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {

        if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
            /*Toast.makeText(this, "face detected: "+ faces.length +
                    " - Face Location X: " + faces[0].rect.centerX() +
                    " - Y: " + faces[0].rect.centerY() , Toast.LENGTH_SHORT).show();*/
            if(facedetection_count==0) {
                facedetection_count++;
                Myapplication myapp = (Myapplication) getApplication();
                myapp.setadapter(adapter); //전역변수로써 넘겨줌
                Intent intent = new Intent(BestNewMenuActivity.this, Order_Method_Selection_Popup.class);
                intent.putExtra("isOrdered", false);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                setResult(RESULT_OK, intent); //설정했다고 알림
                startActivityForResult(intent,3);
            }

            /*Intent intent = new Intent(this, Popup.class);
            mCamera.release();
            mCamera = null;
            STATE = false;
            startActivity(intent);*/
        }
        else {
        }
    }


    public void CountDownTimer_foreground_use1() {//액티비티 포그라운드 유지용 타이머
        CountDownTimer CDT = new CountDownTimer(10 * 60 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                CountDownTimer_foreground_use2();
            }
        };
        CDT.start();
    }

    public void CountDownTimer_foreground_use2() {//액티비티 포그라운드 유지용 타이머
        CountDownTimer CDT = new CountDownTimer(10 * 60 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                CountDownTimer_foreground_use1();
            }
        };
        CDT.start();
    }

    public class AutoScroll {
        boolean Killthread = true;

        public void restartAutoScroll() {
            Thread thread = new Thread(new Runnable() {
                int count = 0;

                @Override
                public void run() {
                    while (Killthread) {

                        if (!isScroll) {
                            try {
                                Thread.sleep(5000);
//                                setTheme();
                            } catch (Exception e) {

                            }
                            isScroll = true;
                        }
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }

        public void autoScroll() {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if (isScroll) {
                        recyclerView.scrollBy(2, 0);
//                        setTheme();
                    }

                    handler.postDelayed(this, 0);
                }
            };
            handler.postDelayed(runnable, 0);
        }
    }


    public void getBestMenusList()//best메뉴리스트 만들기
    {
        ArrayList<Menu> all_menus = menuManager.getAllMenusforbest();
        if(best_menus!=null)
        {
            ArrayList<Menu> temp_menus = new ArrayList<>();//best
            boolean[] check = new boolean[best_menus.size()];
            for (int i = 0; i < all_menus.size(); i++) {
                for (int j = 0; j < best_menus.size(); j++) {

                    if (best_menus.get(j).equals(all_menus.get(i).getMenu_name()) && check[j] != true) {

                        //viewpager에서는 best_image_links에 잇는 이미지 쓰고 거기서 띄우는 popup에는 main 메뉴에 들어가는 이미지와 같은 이미지 사용
                        //   all_menus.get(i).setBitmap(best_image_links.get(j));
                        all_menus.get(i).setMenu_ment(best_menus_ments.get(j));
                        menus.add(all_menus.get(i));
                        check[j] = true;
                    }
                }
            }
        }
    }

    public void getNewMenuList()//new 메뉴 리스트 만들기
    {
        ArrayList<Menu> all_menus2 = menuManager2.getAllMenusfornew();
        if(new_menus!=null)
        {
            boolean[] check = new boolean[new_menus.size()];
            for (int i = 0; i < all_menus2.size(); i++) {
                for (int j = 0; j < new_menus.size(); j++) {
                    if (new_menus.get(j).equals(all_menus2.get(i).getMenu_name()) && check[j] != true) {
                        check[j] = true;
//                        all_menus2.get(i).setBitmap(new_image_links.get(j)); ->기존 메뉴들의 이미지를 가져와서쓰게 변경됨.
                        menus2.add(all_menus2.get(i));
                        Log.d(TAG, "getNewMenuname"+ menus2.get(menus2.size()-1).getMenu_name());
                    }
                }
            }
        }
    }

    public void hideMenu() {
        //하단 레이아웃 visibility를 gone으로 설정해서 공간을 차지하지 않고 보이게 않게 만듬
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        linearLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //팝업 액티비티의 결과를 받는 부분
        Log.i(TAG, "onActivityResult: requestCode : " + requestCode + " resultCode : " + resultCode);

        if(resultCode==3){
            facedetection_count=0;
        }
        if(resultCode==4){
            Myapplication myapp = (Myapplication) getApplication();
            myapp.setadapter(adapter); //전역변수로써 넘겨줌
            Intent intent = new Intent(BestNewMenuActivity.this, MainActivity.class);
            intent.putExtra("isOrdered", false);
            intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
            setResult(RESULT_OK, intent); //설정했다고 알림
            startActivity(intent);
            finish();
        }
        if(resultCode==5){
            Myapplication myapp = (Myapplication) getApplication();
            myapp.setadapter(adapter); //전역변수로써 넘겨줌
            Intent intent = new Intent(BestNewMenuActivity.this, EasyMenuSelectionActivity.class);
            intent.putExtra("isOrdered", false);
            intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
            setResult(RESULT_OK, intent); //설정했다고 알림
            startActivity(intent);
            finish();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 1:
                    //    isScroll = true;//팝업내리면 스크롤 다시 시작
                    //메뉴 이미지를 클릭해서 팝업 액티비티를 띄웠을 때 requestCode == 1

                    //인텐트에서 선택된 메뉴의 데이터를 받은 후
                    Menu menu = data.getParcelableExtra("menu");
                    ArrayList<Integer> choices = data.getIntegerArrayListExtra("options");

                    //하단 리스트뷰가 있는 레이아웃 visibility를 VISIBLE로 바꿔 보이도록 설정
                    //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottomLayout);
                    //linearLayout.setVisibility(View.VISIBLE);

                    //어댑터 내부 arraylist안에 추가 후 변화를 알림
                    //adapter.addItem(menu, choices);
                    //adapter.notifyDataSetChanged();
                    Intent intent = new Intent(BestNewMenuActivity.this, MainActivity.class);
                    intent.putExtra("isOrdered", true);
                    intent.putExtra("menu", menu);
                    intent.putIntegerArrayListExtra("options", choices);
                    setResult(RESULT_OK, intent);
                    intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
                    startActivity(intent);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED)//팝업띄우고 취소햇을때
        {
            // isScroll=true;
        }
    }

    @Override
    protected void onDestroy() {
        NewMenuadapter.setFinished(true);
        AutoScrollForBN.Killthread = false;
        super.onDestroy();

        mCamera.release();
        mCamera = null;
        STATE = false;
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Intent intent2Splash = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(intent2Splash);
        System.out.println("홈버튼");
    }

    public void onBackPressed() {
        return;
    }// 뒤로가기 막음

    //외부테마용
    private void settheme() {


//        SharedPreferences sharedPreferences = getSharedPreferences("CustomTheme", MODE_PRIVATE);
//        sharedPreferences.getString("AutoLogin_bizNum","");
//
//        SharedPreferences sharedPreferences = getSharedPreferences("CustomTheme", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("AutoLogin_bizNum",EditBizNum.getText().toString());//사업자번호저장
//        editor.putString("AutoLogin_password",editPassword.getText().toString());//비밀번호저장
//        editor.commit();

    }
}
