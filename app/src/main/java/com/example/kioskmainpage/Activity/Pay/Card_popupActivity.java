package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.CardReceiptInfo;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

import static com.example.kioskmainpage.Activity.MainActivity.countDownTimer;

public class Card_popupActivity extends AppCompatActivity {
    public CountDownTimer countDownTimerforgif;
    private static final String TAG = "Card_Popup";
    ArrayList<SelectedMenu> selectedMenus;
    ImageView ImageView;
    String test_price="10";//카드로 테스트할때는 100원단위로 결제
    String total_price="";//실제 연동 시 적용

    String input_month = "";//할부개월

    MediaPlayer mediaPlayer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimerforgif.cancel();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_popup);
   //     countDownReset();//카운트다운리셋
        //상,하단의 바를 제거하고 풀스크린으로 만듬
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        Myapplication myapp = (Myapplication)getApplication();

        ImageView =(ImageView) findViewById(R.id.Card_Loding_loding_dot);
        countDownTimerforgif_set();
        countDownTimerforgif.start();
        //결제 무시시
        if(myapp.isSkippay())
        {
            Intent intent = new Intent(Card_popupActivity.this, PayingPopupActivity.class);

            switch (myapp.getCurrentPayType())
            {
                case "Card":
                    intent.putExtra("type", 2);
                    break;
                case "SAMSUNGPAY":
                    intent.putExtra("type", 4);
                    break;
            }
            CardReceiptInfo cardReceiptInfo = null ;
            cardReceiptInfo = new CardReceiptInfo("TEST_tid(단말기NO)", "TEST_approvalNo(승인번호)", "TEST_approvalDate(승인날자)", "12345678", 0, (long)myapp.getadapter().getPriceSum(), 0, (long)myapp.getadapter().getPriceSum()/10
                    , "TEST_IssuerName(카드사명)", "TEST_acquierName", "1234567890", "TEST_orgAddress", "TEST_orgCeoName", "010-0000-0000", "MOKI", "TEST_msg");
            myapp.setCurrentCardReceiptInfo(cardReceiptInfo);
            //소리재생
            mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.eventually);
            mediaPlayer.start();
            startActivity(intent);
            finish();
        }
        else{

   //     setContentView(R.layout.activity_card_popup);
        Intent intent2 = getIntent();
        countDownTimer.cancel();
        input_month = intent2.getStringExtra("Installment");
        System.out.println("할부개월 : "+input_month);
        //총 결제가격 계산
        Myapplication app = (Myapplication)getApplication();
        ArrayList<SelectedMenu> selectedMenus = app.getadapter().getselectedMenus();
        int sum=0;

        for(int i=0;i<selectedMenus.size();i++)
        {
            int price = selectedMenus.get(i).getCnt() * Integer.parseInt(selectedMenus.get(i).getMenu_price());
            sum += price;
        }
        total_price = String.valueOf(sum);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("할부개월2 : "+input_month);
                String uri = String.format("appposw://card-approval?&" +
                                "authKey=1234&runStrategy=without-input-view&inputAmount=%s&inputMonth=0&inputAmountEditable=false&inputMonthEditable=false&showResult=false",
                        test_price);
                int REQUEST_CARD = 0;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, REQUEST_CARD);

                //----------------
                /*
                Intent intent = new Intent (Card_popupActivity.this, PayingPopupActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                finish();*/
            }
        },0);}// 5초 정도 딜레이를 준 후 시작
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("result code : "+resultCode);
        if(resultCode == RESULT_OK){
            System.out.println("request Code : " + requestCode);
            switch (requestCode){
            // MainActivity 에서 요청할 때 보낸 요청 코드 (0)
                case 0:
                    String message=null;
                    String approvalNo = null;
                    String tid = data.getStringExtra("tid");//단말기 번호
                    approvalNo = data.getStringExtra("approvalNo");//승인번호
                    String approvalDate = data.getStringExtra("approvalDate");//승인날짜
                    String cardNo = data.getStringExtra("cardNo");//카드번호
                    int monthVal = data.getIntExtra("monthVal",0);//할부개월
                    long sumVal = data.getLongExtra("sumVal",0);//최종결제금액
                    long priceVal = data.getLongExtra("priceVal",0);//물품가액
                    long vatVal = data.getLongExtra("vatVal",0);//부가세액
                    String issuerName = data.getStringExtra("issuerName");//발급사
                    String acquierName = data.getStringExtra("acquierName");//매입사
                    String orgNo = data.getStringExtra("orgNo");//사업자번호
                    String orgAddress = data.getStringExtra("orgAddress");//주소
                    String orgCeoName = data.getStringExtra("orgCeoName");//대표자
                    String orgTel = data.getStringExtra("orgTel");//대표자 전화번호
                    String orgRegName = data.getStringExtra("orgRegName");//상호
                    message = data.getStringExtra("message1");//에러메시지
                    System.out.println("승인번호 : "+approvalNo);
                    System.out.println("승인번호 : "+approvalDate);
                    System.out.println("메시지 : "+message);
                    CardReceiptInfo cardReceiptInfo = null ;

                    cardReceiptInfo = new CardReceiptInfo(tid, approvalNo, approvalDate, cardNo, monthVal, sumVal, priceVal, vatVal
                            , issuerName, acquierName, orgNo, orgAddress, orgCeoName, orgTel, orgRegName, message);
                    if(approvalNo==null)//결제 중간에 취소버튼을 눌럿을 경우엔 X를 default 값으로 둬서 카드결제실패창을 띄움
                    {
                        cardReceiptInfo = new CardReceiptInfo();

                        cardReceiptInfo.setApprovalNo("X");
                        cardReceiptInfo.setMessage1("임의 취소");

                    }
                    else if(approvalNo.equals("X"))
                    {

                        cardReceiptInfo = new CardReceiptInfo(tid, approvalNo, approvalDate, cardNo, monthVal, sumVal, priceVal, vatVal
                                , issuerName, acquierName, orgNo, orgAddress, orgCeoName, orgTel, orgRegName, message);

                    }
                    cardReceiptInfo.setMonthVal(0);

                    Myapplication app = (Myapplication)getApplication();
                    app.setCurrentCardReceiptInfo(cardReceiptInfo);

                    if(cardReceiptInfo.getApprovalNo().equals("X"))//결제 실패
                    {
                        Intent intent = new Intent(Card_popupActivity.this,Card_fail_popupActivity.class);
                        intent.putExtra("type","Card");
                        startActivity(intent);
                        finish();
                        break;
                    }
                    else { //결제 성공
                        Intent intent = new Intent(Card_popupActivity.this, PayingPopupActivity.class);
                        switch (app.getCurrentPayType())
                        {
                            case "Card":
                                intent.putExtra("type", 2);
                            break;
                            case "SAMSUNGPAY":
                                intent.putExtra("type", 4);
                                break;
                        }
                        //소리재생
                        mediaPlayer = MediaPlayer.create(this.getApplicationContext(), R.raw.eventually);
                        mediaPlayer.start();
                        startActivity(intent);
                        finish();
                        break;
                    }
            }
        }
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음
    public void countDownTimerforgif_set(){//히든메뉴 접속용 타이머
        countDownTimerforgif = new CountDownTimer(50000, 300) { // 1000당 1초   5분 1초당 카운트
            int i=0;
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_1);
                        break;
                    case 1:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_2);
                        break;
                    case 2:
                        ImageView.setImageResource(R.drawable.ic_loading_dot_3);
                        break;
                }
                i++;
                i=i%3;
            }
            public void onFinish() {
            }
        };
    }
}
