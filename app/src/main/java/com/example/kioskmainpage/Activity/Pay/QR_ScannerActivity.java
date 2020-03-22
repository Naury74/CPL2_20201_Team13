package com.example.kioskmainpage.Activity.Pay;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.kioskmainpage.Myapplication;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QR_ScannerActivity extends AppCompatActivity{

    int var=1;
    public int time=0;

    private IntentResult rrs;
    private static final String TAG = "testtesttest";
    TextView status_view;
    String S01="EX";//메이커 코드
    String S02="D1";//전문코드
    String S04="40";//40
    String S05="0700081";//단말기번호TID
    String S06="B K";//통합 동글 FLAG
    String S07="";//
    String S08="B";//WCC
    String S09="";//카드번호(코드스캔정보)
    String S10="";//
    String S11="00";//할부개월
    String S12="1004";//금액
    String S13="";//
    String S14="";//
    String S15="";//
    String S16="";//
    String S17="";//
    String S18="91";//부가세
    String S19="N";//DSC사용유무
    String S26="";//
    //TODO : 5만원 이상 초과일때는 S30, S19 값을 Y로 설정


    private String pay_result;//결제결과리턴값
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_qr_scanner);

    }
    @Override
    protected void onResume() {

        super.onResume();
        if(var==1) {
            //var==1로 지정하여서 qr코드 스캔한후 activity 가 넘어가기전에 한번 더 스캔하는 일을 막음
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CustomScannerActivity.class);
            integrator.initiateScan();
            //스캔
            var++;
            time++;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.d(TAG, "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            //스캔한결과 받음
            rrs = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            S09 = scanResult.getContents();

            Myapplication myapplication = (Myapplication) getApplication();
            S12 = String.valueOf(myapplication.getCurrentOrder_price());
            System.out.println("스캔 결과 : "+S09);

            String result="S01="+S01
                    +";S02="+S02
                    +";S04="+S04
                    +";S05="+S05
                    +";S06="+S06
                    +";S07="+S07
                    +";S08="+S08
                    +";S09="+S09
                    +";S10="+S10
                    +";S11="+S11
                    +";S12="+S12
                    +";S13="+S13
                    +";S14="+S14
                    +";S15="+S15
                    +";S16="+S16
                    +";S17="+S17
                    +";S18="+S18
                    +";S19="+S19
                    +";S26="+S26
                    +";";
            Intent intent2 = new Intent (this, PayingPopupActivity.class);
            intent2.putExtra("type",1);
//                Intent intent = new Intent (this, CouponAndReceiptLoadingPopupActivity.class);
//                intent.putExtra("type",22);
            intent2.putExtra("payType","KaKao");
            intent2.putExtra("request",result);
            startActivity(intent2);
            finish();
      /*      try {
             //   pay_result=new String(KiccModule.KiccApproval(3, result, result.length(), 3, "", "", ip, Integer.parseInt(port), 0, "KICC", "700081", "0001", KiccPath), "EUC-KR");

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

        }
        else{
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
}
