package com.example.kioskmainpage.Activity;

import android.app.ActivityManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.TaskKiller;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class ReaderUnavailableActivitiy extends AppCompatActivity {
    private CountDownTimer countDownTimerForReader1,countDownTimerForReader2,countDownTimerForReader3;//UI용 카운트 다운
    Myapplication myapp;
    int uiOptions;
    int newUiOptions;
    boolean thread_status = true;
    private static Handler mHandler ;
    String TAG = "ReaderUnavailableActivitiy";
    TaskKiller TK;
    int count=0;//전체 재시도횟수
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        myapp.setReaderUnavailableActivitiyContext(null);
        if(countDownTimerForReader1!=null)
            countDownTimerForReader1.cancel();
        if(countDownTimerForReader2!=null)
            countDownTimerForReader2.cancel();
        if(countDownTimerForReader3!=null)
            countDownTimerForReader3.cancel();
        thread_status=false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_unavailable_activitiy);
        myapp = (Myapplication) getApplication();

        //상,하단의 바를 제거하고 풀스크린으로 만듬
        uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        TK = new TaskKiller((ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE),(UsageStatsManager)this.getSystemService(Context.USAGE_STATS_SERVICE));
        myapp.setReaderUnavailableActivitiyContext(this);
        restartSearch();
        Reader_status_checker();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0 :
                        countDownTimerForReader("END",5);
                        break;
                    case 1 :
                        countDownTimerForReader("check",30);
                        break;
                    case 2 :
                        countDownTimerForReader("Restart",3);
                        break;
                }
            }
        } ;
    }

    public void Reader_status_checker() {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (thread_status) {
                    Log.d("리더기 연결상태 확인중", "");
                    myapp.checkreader_status();
                    if (myapp.getReader_status()==false) {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                        }
                        Log.d("리더기 연결상태 = ", String.valueOf(myapp.getReader_status()));
                    } else {
                        if(countDownTimerForReader1!=null)
                        countDownTimerForReader1.cancel();
                        if(countDownTimerForReader2!=null)
                            countDownTimerForReader2.cancel();
                        mHandler.sendEmptyMessage(0) ;
                        thread_status = false;
                    }
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public void restartSearch(){
        myapp.getBackground_checkers().call_reader_config(this);
        final Intent readerUN2Splash = new Intent(this, SplashActivity.class);
        readerUN2Splash.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);//SPLASH 화면이 뜨지 않게함
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(readerUN2Splash);
                mHandler.sendEmptyMessage(1);
            }
        }, 1500);
    }

    public void click_to_reader_config(View v) {
        myapp.getBackground_checkers().call_reader_config(this);
    }

    public void click_to_finish(View v) {
        myapp.setReaderUnavailableActivitiyContext(null);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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

    public void countDownTimerForReader(String types, final int basetime){
        switch (types)
        {
            case "check":
                countDownTimerForReader1 = new CountDownTimer(basetime*1000+2000, 1000) { // 1000당 1초
                    TextView tx = (TextView) findViewById(R.id.reader_status_text);
                    int time=basetime;
                    public void onTick(long millisUntilFinished) {
                        if(time<1)
                            tx.setText("리더기 연결시도중입니다.");
                        else
                            tx.setText("리더기 연결시도중입니다."+"\n"+time);
                        time--;
                    }
                    public void onFinish() {
                        if(count>5)
                        {
                            tx.setText("리더기 자동 연결 실패\n\n점원에게 문의하세요.");
                        }
                        else{
                            mHandler.sendEmptyMessage(2);
                            count++;
                        }
                    }
                };
                countDownTimerForReader1.start();
                break;
            case "Restart":
                countDownTimerForReader2 = new CountDownTimer(basetime*1000+2000, 1000) { // 1000당 1초
                    TextView tx = (TextView) findViewById(R.id.reader_status_text);
                    int time=basetime;
                    public void onTick(long millisUntilFinished) {
                        if(time<1)
                            tx.setText("리더기 연결을 재시도합니다.");
                        else
                            tx.setText("리더기 연결을 재시도합니다."+"\n"+time);
                        time--;
                    }
                    public void onFinish() {
                        TK.targetkill("kr.infinix.hpay.appposw");
                        restartSearch();
                    }
                };
                countDownTimerForReader2.start();
                break;
            case "END":
                countDownTimerForReader3 = new CountDownTimer(basetime*1000+1000, 1000) { // 1000당 1초
                    TextView tx = (TextView) findViewById(R.id.reader_status_text);
                    int time=basetime;
                    public void onTick(long millisUntilFinished) {
                        if(time<1)
                            tx.setText("리더기 연결되었습니다.\n잠시후 이동합니다.\n" + myapp.getBackground_checkers().getDevice_name());
                        else
                            tx.setText("리더기 연결되었습니다.\n잠시후 이동합니다.\n" + myapp.getBackground_checkers().getDevice_name()+"\n"+time);
                        time--;
                    }
                    public void onFinish() {
                        finish();
                    }
                };
                countDownTimerForReader3.start();
                break;
        }
    }
}
