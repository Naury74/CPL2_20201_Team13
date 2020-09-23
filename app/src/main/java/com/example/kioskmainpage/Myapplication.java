package com.example.kioskmainpage;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.kioskmainpage.Adapter.RAdapter;

import com.example.kioskmainpage.Adapter.SelectedListAdapter;
import com.example.kioskmainpage.Utilities.Chaches;


import java.util.ArrayList;

public class Myapplication extends Application {
    private int OrderNum = (int)(Math.random()*100);
    private int WaitingNum = (int)(Math.random()*10);
    private static final String TAG = "statics";
    private  String BizNum;
    private static boolean  WaitingOn = false;
    private static boolean SkipDown = false;
    private static boolean Autologin = false;
    private static boolean Skippay = false;
    private ArrayList<String> soldouts = new ArrayList<String>();
    private ArrayList<RAdapter> RAdapters;
    private Context adminSyncContext;
    private Context adminMainContext;
    private Context admindeviceManageContext;
    private Context mainActivityContext;
    private Context BestNewMenuActivityContext;
    private Context ReaderUnavailableActivitiyContext=null;
    private String token=null;
    private Application application;
    private String currentPayType="NULL";
    private CardReceiptInfo currentCardReceiptInfo;
    private KakaoPayReceiptInfo currentKakaoPayReceiptInfo;
    private MainTheme mainTheme;
    public SelectedListAdapter adapter;
    private CountDownTimer countDownTimer;
    private int OrderRetryCount=0;
    private int currentOrder_price;//현재 주문 금액
    private String currentOrder_id;//서버에서 받은 주문테이블 고유번호
    private String deviceNo;//서버에서 받은 디바이스 번호
    //창의 비율 자동조절을 위한 부분
    private int Size_width;
    private int Size_height;
    //동기화를 위한부분
    private String lastest_SyncVersion;
    //로그아웃과 동기화를 위한 타입 LOGOUT, SYNC
    private String AdminToMainType = "DEFAULT";
    //리더기 연결 확인용
    private Background_Checkers background_checkers;
    private boolean reader_status=false;
    //포장선택
    private String takeout_status;
    //이미지 캐쉬
    private Chaches imagaeCache = new Chaches();
    //사용자 데이터
    private ArrayList<String> User_data =new ArrayList<String>();
    //테이블
    private String table_status = "F";// 테이블 설정 상태
    private String tablenum = "0"; //테이블번호
    // 화면상태
    private String Screenonoff="SCREEN_ON";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    //사용자 데이터
    public ArrayList<String> getUser_data() {return User_data;}
    public void setUser_data(ArrayList<String> user_data) {this.User_data=user_data;}


    public ArrayList<RAdapter> getRAdapters() {
        return RAdapters;
    }

    public void setRAdapters(ArrayList<RAdapter> RAdapters) {
        this.RAdapters = RAdapters;
    }

    public String getCurrentOrder_id() {
        return currentOrder_id;
    }

    public void setCurrentOrder_id(String currentOrder_id) {
        this.currentOrder_id = currentOrder_id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
        Log.d(TAG, "setDeviceNo: "+deviceNo);
    }

    public int getCurrentOrder_price() {
        return currentOrder_price;
    }

    public void setCurrentOrder_price(int currentOrder_price) {
        this.currentOrder_price = currentOrder_price;
    }

    public int getOrderRetryCount() {
        return OrderRetryCount;
    }

    public void setOrderRetryCount(int orderRetryCount) {
        OrderRetryCount = orderRetryCount;
    }

    public Context getAdminSyncContext() {
        return adminSyncContext;
    }

    public void setAdminSyncContext(Context adminSyncContext) {
        this.adminSyncContext = adminSyncContext;
    }

    public KakaoPayReceiptInfo getCurrentKakaoPayReceiptInfo() {
        return currentKakaoPayReceiptInfo;
    }

    public void setCurrentKakaoPayReceiptInfo(KakaoPayReceiptInfo currentKakaoPayReceiptInfo) {
        this.currentKakaoPayReceiptInfo = currentKakaoPayReceiptInfo;
    }

    public String getCurrentPayType() {
        return currentPayType;
    }

    public void setCurrentPayType(String currentPayType) {
        this.currentPayType = currentPayType;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public MainTheme getMainTheme() {
        return mainTheme;
    }

    public void setMainTheme(MainTheme mainTheme) {
        this.mainTheme = mainTheme;
    }

    private ArrayList<String> best_menus,new_menus,best_image_links,best_ments,new_image_links;

    public ArrayList<String> getNew_image_links() {
        return new_image_links;
    }

    public void setNew_image_links(ArrayList<String> new_image_links) {
        this.new_image_links = new_image_links;
    }

    public ArrayList<String> getBest_ments() {
        return best_ments;
    }

    public void setBest_ments(ArrayList<String> best_ments) {
        this.best_ments = best_ments;
    }

    public ArrayList<String> getBest_image_links() {
        return best_image_links;
    }

    public void setBest_image_links(ArrayList<String> best_image_links) {
        this.best_image_links = best_image_links;
    }

    public SelectedListAdapter getadapter()
    {
        Log.i(TAG, "getadapter");
        return adapter;

    }
    public void setadapter(SelectedListAdapter adapter)
    {
        Log.i(TAG, "setadapter");
        this.adapter=adapter;

    }
    public int getOrderNum()
    {
        return OrderNum;

    }
    public void setOrderNum(int mOrderNum)
    {
        this.OrderNum=mOrderNum;

    }
    public int getWaitingNum()
    {
        return WaitingNum;

    }
    public void setWaitingNum(int WaitingNum)
    {
        this.WaitingNum=WaitingNum;

    }

    public String getBizNum()
    {
        return BizNum;

    }
    public void setBizNum(String BizNum)
    {
        this.BizNum=BizNum;
        Log.i(TAG, "setBizNum: "+this.BizNum);
    }

//    public void setadapter_setselectedMenus(SelectedListAdapter adapter)
//    {
//        Log.i(TAG, "setadapter_setselectedMenus");
//        this.adapter.setselectedMenus(adapter.getselectedMenus());
//
//    }

    public ArrayList<String> getNew_menus() {
        return new_menus;
    }

    public void setNew_menus(ArrayList<String> new_menus) {
        this.new_menus = new_menus;
    }

    public ArrayList<String> getBest_menus() {
        return best_menus;
    }

    public void setBest_menus(ArrayList<String> best_menus) {
        this.best_menus = best_menus;
    }

    public CardReceiptInfo getCurrentCardReceiptInfo() {
        return currentCardReceiptInfo;
    }

    public void setCurrentCardReceiptInfo(CardReceiptInfo currentCardReceiptInfo) {
        this.currentCardReceiptInfo = currentCardReceiptInfo;
    }

    //크기 자동조절용
    public int getSize_width() {return Size_width;}
    public void setSize_width(int size_width) {Size_width = size_width;}
    public int getSize_height() { return Size_height;}
    public void setSize_height(int size_height) { Size_height = size_height;}

    //동기화를 위한 버전getset
    public String getLastest_SyncVersion() {return lastest_SyncVersion; }
    public void setLastest_SyncVersion(String lastest_SyncVersion) {this.lastest_SyncVersion = lastest_SyncVersion;}

    //로그아웃과 동기화를 위한 타입 LOGOUT, SYNC
    public String getAdminToMainType() { return AdminToMainType; }
    public void setAdminToMainType(String adminToMainType) {AdminToMainType = adminToMainType; }

    //자동로그인용
    public void setAutologinOn(){this.Autologin = true;}
    public void setAutologinOff(){this.Autologin = false;}
    public boolean isitAutologinOn(){return this.Autologin;}

    //웨이팅체크용
    public void setWaitingOn(){this.WaitingOn = true;}
    public void setWaitingOff(){this.WaitingOn = false;}
    public boolean isitWaitingOn(){return this.WaitingOn;}
    //다운스킵용
    public void setSkipDown(){this.SkipDown = true;}
    public void setSkipDownoff(){this.SkipDown = false;}
    public boolean isitSkipDown(){return this.SkipDown;}
    //결제스킵용
    public boolean isSkippay() {
        Log.d(TAG, "isSkippay: "+ getSkippay()); return Skippay;}
    public void setSkippay(boolean skippay) {
        Log.d(TAG, "setSkippay: ");Skippay = skippay;}
    public String getSkippay() {
        if(Skippay)return "true";
        else
            return "false";
    }
    //포장여부
    public String getTakeout_status() {
        String tmp = takeout_status;
        return tmp;}
    public void setTakeout_status(String takeout_status) {this.takeout_status = takeout_status;}
    //리더기 연결상태 확인용
    public Context getReaderUnavailableActivitiyContext() {return ReaderUnavailableActivitiyContext;}
    public void setReaderUnavailableActivitiyContext(Context readerUnavailableActivitiyContext) {this.ReaderUnavailableActivitiyContext = readerUnavailableActivitiyContext;}
    public boolean getReader_status() {return reader_status;}
    public void checkreader_status(){background_checkers.Check_reader_state();}
    public void setReader_status(boolean reader_status) {this.reader_status = reader_status;}
    public Background_Checkers getBackground_checkers() {return background_checkers;}
    public void setBackground_checkers(Background_Checkers background_checkers) {this.background_checkers = background_checkers;}
    //테이블번호와 테이블옵션상태.
    public String getTablenum() {return tablenum;}
    public void setTablenum(String tablenum) {this.tablenum = tablenum;}
    public String isTable_status() {return table_status;}
    public void setTable_status(String table_status) {this.table_status = table_status;}

    public Context getAdminMainContext() {return adminMainContext;}
    public void setAdminMainContext(Context adminMainContext) {this.adminMainContext = adminMainContext;}
    public Context getAdmindeviceManageContext() {return admindeviceManageContext;}
    public void setAdmindeviceManageContext(Context admindeviceManageContext) {this.admindeviceManageContext = admindeviceManageContext;}
    public Context getMainActivityContext() {return mainActivityContext;}
    public void setMainActivityContext(Context mainActivityContext) { this.mainActivityContext = mainActivityContext; }



    public Application getApplication() {return application;}
    public void setApplication(Application application) {this.application = application;}
    public Chaches getImagaeCache() {return imagaeCache;}
    public void setImagaeCache(Chaches imagaeCache) {this.imagaeCache = imagaeCache;}
    public Context getBestNewMenuActivityContext() {return BestNewMenuActivityContext;}
    public void setBestNewMenuActivityContext(Context bestNewMenuActivityContext) {BestNewMenuActivityContext=bestNewMenuActivityContext;}
    //화면상태
    public String getScreenonoff() { return Screenonoff;}
    public void setScreenonoff(String screenonoff) {Log.d(TAG, "Screen "+screenonoff); Screenonoff = screenonoff;}
    // 기기토큰
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public ArrayList<String> getSoldouts() {return soldouts; }
    public void setSoldouts(ArrayList<String> soldouts) {this.soldouts = soldouts;}
    public void soldouts_add(String target_menu){
        soldouts.add(target_menu);
        Log.d(TAG, "품절목록에"+target_menu+"추가했습니다.");
    }
    public void soldouts_init(){
        if(soldouts==null)
        soldouts.add("dum");
    }
    public void soldouts_remove(String target_menu){
        if(soldouts!=null)
        for(int i =0;i<soldouts.size();i++)
        {
            if(soldouts.get(i).equals(target_menu)) {
                soldouts.remove(i);
                break;
            }
        }
        else{
            Toast.makeText(this, "품절목록에 없는 대상을 제거 요청했습니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "품절목록에 없는 대상을 제거 요청했습니다.");
        }
    }

    private NaturalLanguageProcessing naturalLanguageProcessing=new NaturalLanguageProcessing();

    public NaturalLanguageProcessing getNaturalLanguageProcessing() {
        return naturalLanguageProcessing;
    }
}
