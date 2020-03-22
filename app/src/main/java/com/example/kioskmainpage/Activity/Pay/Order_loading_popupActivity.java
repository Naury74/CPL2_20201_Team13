package com.example.kioskmainpage.Activity.Pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceipt_announce_Activity;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.Coupon_Activity;
import com.example.kioskmainpage.CardReceiptInfo;
import com.example.kioskmainpage.KakaoPayReceiptInfo;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Order_loading_popupActivity extends AppCompatActivity {
    public CountDownTimer countDownTimerforgif;
    int test_price = 10;//TODO : 실제 사용 시 실제 결제 금액으로 바꿔야함
    String result_json;
    String url="http://mobilekiosk.co.kr/admin/api/order.php";//주문전송
    final String TAG = "Order_loading";
    String S01="EX";//메이커 코드
    String S02="D2";//전문코드 D1 : 신용승인 , D2 : 신용당일취소 D4 : 반품환불
    String S04="40";//40
    String S05="0700081";//단말기번호TID
    String S06="B K";//통합 동글 FLAG
    String S07="";//
    String S08="T";//WCC 취소는 : T
    String S09="";//카드번호(코드스캔정보)
    String S10="";//
    String S11="";//할부개월
    String S12="";//금액
    String S13="";//
    String S14="";//승인번호
    String S15="";//승인일자
    String S16="";//
    String S17="";//
    String S18="91";//부가세
    String S19="N";//DSC사용유무
    String S26="";//
    //카카오페이용
    String KiccPath = "/sdcard/kicc/";
    String ip = "203.233.72.55";//카카오페이 결제 서버 IP주소, 테스트용 : 203.233.72.55 -> 실서버 : 203.233.72.21
    String port = "15200";//카카오페이 결제 서버 포트번호, 테스트용 : 15200 -> 실서버 : 15700
    KakaoPayReceiptInfo kakaoPayReceiptInfo;
    Myapplication app;
    ImageView loading_dot;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimerforgif.cancel();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_loading_popup);

//        ImageView loading_dot = (ImageView) findViewById(R.id.order_fail_loading_dot);
//        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loading_dot);
//        Glide.with(this).load(R.drawable.gif_loding_dot).into(gifImage);
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        app = (Myapplication)getApplication();
        countDownTimerforgif_set();
        countDownTimerforgif.start();
        Handler mHandler = new Handler(){
            public void handleMessage(Message msg){
                Intent intent = getIntent();
                String next_activity = intent.getStringExtra("next_activity");
                String fail_type = intent.getStringExtra("fail_type");

                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getActiveNetworkInfo();

                //와이파이 연결 안 되어 있으면 다시 Order_fail로로
                if(mWifi==null)
                {
                    int orderRetry = app.getOrderRetryCount();

                    Intent intent2 = new Intent(Order_loading_popupActivity.this,Order_fail_popupActivity.class);

                    if(orderRetry == 2)//2번 시도해도 실패한 경우 - 결제 취소 후 실패 창
                    {
                        app.setOrderRetryCount(0);//재시도 횟수 초기화

                        intent2.putExtra("fail_type","WIFI_CONNECT");
                        startActivity(intent2);
                        finish();
                        return;
                    }
                    else //아직 2번 이하
                    {
                        Intent intent3 = new Intent(Order_loading_popupActivity.this, Order_fail_popupActivity.class);
                        intent3.putExtra("fail_type","WIFI_CONNECT");
                        startActivity(intent3);
                        finish();
                        return;
                    }

                }
                else //와이파이 연결되어있는 경우
                {
                    int orderRetry = app.getOrderRetryCount();


                    if(orderRetry==2)//2번 시도면 결제 취소
                    {
                        app.setOrderRetryCount(0);//재시도 횟수 초기화
                        //TODO : 결제 취소
                        if(app.getCurrentPayType().equals("KaKao"))//카카오일때
                        {
                         /*   try {
                                String TRNO = new String(KiccModule.KiccGetTRNO("KICC",KiccPath),"EUC-KR");
                                String request= makeRequest();
                                String cancel_result = new String(KiccModule.KiccApproval(3, request, request.length(), 3, "", "", ip, Integer.parseInt(port), 0, "KICC", "700081", TRNO, KiccPath), "EUC-KR");

                                System.out.println("카카오페이 결과 : "+cancel_result);
                                ArrayList<String> parsed = new ArrayList<String>(Arrays.asList(cancel_result.split("\\|")));
                                System.out.println("카카오페이 결과2 : "+parsed.get(0));
                                int result_type = Integer.parseInt(parsed.get(0));//결과 실패 or 성공

                                if(result_type == 0)
                                {
                                    ArrayList<String> parsedResult = parsingKakaoPayResult(cancel_result);//결과 파싱 하고 전역변수에 삽입

                                    if(parsedResult.get(6).equals("0000"))//취소 성공
                                    {
                                        System.out.println("취소성공");
                                    }
                                }
                            }
                            catch (UnsupportedEncodingException e)
                            {
                                e.printStackTrace();
                            }
                            */
                            //주문번호 받기 실패
                            Intent intent_zero = new Intent(Order_loading_popupActivity.this, Order_fail_popupActivity.class);
                            intent_zero.putExtra("fail_type","FAILURE");
                            startActivity(intent_zero);
                            finish();
                            return;
                        }
                        else if(app.getCurrentPayType().equals("Card"))//카드 결제일때
                        {
                            //TODO : 카드 결제 취소
                            System.out.println("승인일자 : "+app.getCurrentCardReceiptInfo().getApprovalDate().substring(0,8));
                            System.out.println("승인번호 : "+app.getCurrentCardReceiptInfo().getApprovalNo());
                            Toast.makeText(Order_loading_popupActivity.this,"결제취소를 위해 사용한 카드를 다시 삽입해주세요.",Toast.LENGTH_LONG).show();
                            String uri = String.format("appposw://card-revoke?&" +
                                            "authKey=1234&runStrategy=without-input-view&inputAmount=%s&inputMonth=0&inputApprovalNo=%s&inputApprovalDate=%s",
                                    test_price,
                                    app.getCurrentCardReceiptInfo().getApprovalNo(),
                                    app.getCurrentCardReceiptInfo().getApprovalDate().substring(0,8)
                            );
                            int REQUEST_CARD = 0;
                            Intent intent_CARD = new Intent(Intent.ACTION_VIEW);
                            intent_CARD.addCategory(Intent.CATEGORY_DEFAULT);
                            intent_CARD.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent_CARD.setData(Uri.parse(uri));
                            startActivityForResult(intent_CARD, REQUEST_CARD);
                        }
                        else
                        {
                            Intent intent_zero = new Intent(Order_loading_popupActivity.this, Order_fail_popupActivity.class);
                            intent_zero.putExtra("fail_type","FAILURE");
                            startActivity(intent_zero);
                            finish();
                            return;
                        }


                    }
                    else//실패 횟수 2번 이하 일 때 재시도
                    {

                        //연결 되어 있으면 서버랑 통신(주문 번호 받아오기)
                        if(app.getCurrentPayType().equals("KaKao"))
                        {
                            sendOrderToServer("KAKAO");
                        }
                        else if(app.getCurrentPayType().equals("Card"))
                        {
                            sendOrderToServer("CARD");
                        }
                        else//제로페이
                        {
                            //주문번호 받기 실패
                            Intent intent_zero = new Intent(Order_loading_popupActivity.this, Order_fail_popupActivity.class);
                            intent_zero.putExtra("fail_type","SERVER_JSON_NORETURN");
                            startActivity(intent_zero);
                            finish();
                            return;
                        }
                    }


                   /* if(next_activity.equals("Coupon"))
                    {
                        Intent intent2 = new Intent(Order_loading_popupActivity.this, Coupon_Activity.class);
                        intent2.putExtra("type", intent.getStringExtra("type")); //영수증
                        startActivity(intent2);
                        finish();
                    }
                    else if(next_activity.equals("CompleteOrder"))
                    {
                        Intent intent2 = new Intent(Order_loading_popupActivity.this, CompleteOrder_PayActivity.class);
                        intent2.putExtra("tablenum_edit",intent.getStringExtra("tablenum_edit"));
                        startActivity(intent2);
                        finish();
                    }*/
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(0,2000);//딜레이 2초


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("result code : "+resultCode);
        if(resultCode == RESULT_OK){
            System.out.println("request Code : " + requestCode);
            switch (requestCode){
                // MainActivity 에서 요청할 때 보낸 요청 코드 (0)
                case 0:

                    String cancelApprovalNo = data.getStringExtra("cancelApprovalNo");
                    String cancelApprovalDate = data.getStringExtra("cancelApprovalDate");

                    Log.d(TAG,"cancelApprovalNo = "+cancelApprovalNo);
                    Intent intent2 = new Intent(Order_loading_popupActivity.this,Order_fail_popupActivity.class);
                    intent2.putExtra("fail_type","FAILURE");
                    startActivity(intent2);
                    finish();
                    return;

            }
        }
    }

    public ArrayList<String> parsingKakaoPayResult(String result)
    {
        ArrayList<String> parsedBest = new ArrayList<String>(Arrays.asList(result.split(";")));
        ArrayList<String> parsedResult = new ArrayList<>();
        for(int i=0;i<parsedBest.size();i++)
        {
            String[] temp = parsedBest.get(i).split("=");
            if(temp.length==1)//결과 없을때
            {
                System.out.println("파싱결과"+(i+1)+"번 : "+"");
                parsedResult.add("");
            }
            else {
                System.out.println("파싱결과"+(i+1)+"번 : "+temp[1]);
                parsedResult.add(temp[1]);
            }
        }
        kakaoPayReceiptInfo = new KakaoPayReceiptInfo(parsedResult.get(0)
                ,parsedResult.get(1),parsedResult.get(2),parsedResult.get(3),parsedResult.get(4),parsedResult.get(5)
                ,parsedResult.get(6),parsedResult.get(7),parsedResult.get(8),parsedResult.get(9),parsedResult.get(10)
                ,parsedResult.get(11),parsedResult.get(12),parsedResult.get(13),parsedResult.get(14),parsedResult.get(15)
                ,parsedResult.get(16),parsedResult.get(17),parsedResult.get(18),parsedResult.get(19),parsedResult.get(20)
                ,parsedResult.get(21),parsedResult.get(22),parsedResult.get(23),parsedResult.get(24),parsedResult.get(25)
                ,parsedResult.get(26),parsedResult.get(27),parsedResult.get(28),parsedResult.get(29));
        return parsedResult;
    }
    public String makeRequest()
    {
        Myapplication app = (Myapplication) getApplication();
        S12 = String.valueOf(app.getCurrentOrder_price());
        S14 = app.getCurrentKakaoPayReceiptInfo().getR12();//승인번호
        S15 = app.getCurrentKakaoPayReceiptInfo().getR10();//승인날짜

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
        return result;
    }

    //서버로 주문정보 전송
    public void sendOrderToServer(String payType)
    {
        String tmp_no="434";
        String totalprice = app.getCurrentOrder_price()+"";
        String pay_type = payType;

        String choice_temp="";
        ArrayList<SelectedMenu> selectedMenus = app.getadapter().getselectedMenus();

        //선택된 메뉴들 url 에 맞게 String 만들기
        String category = selectedMenus.get(0).getCategory();//카테고리
        String product = selectedMenus.get(0).getMenu_name();//상품명
        String qty = selectedMenus.get(0).getCnt()+"";//수량

        String option = selectedMenus.get(0).getOptions().get(0).getOption_name();//옵션
        option += "|"+selectedMenus.get(0).getOptions().get(0).getOptions().get(selectedMenus.get(0).getChoices().get(0));

        for(int i=1;i<selectedMenus.get(0).getOptions().size();i++)
        {
            option += "@" + selectedMenus.get(0).getOptions().get(i).getOption_name();
            option += "|" + selectedMenus.get(0).getOptions().get(i).getOptions().get(selectedMenus.get(0).getChoices().get(i));
        }
        String price = selectedMenus.get(0).getPrice_sum()+"";

        for(int i=1;i<selectedMenus.size();i++)
        {
            category += "$" + selectedMenus.get(i).getCategory();
            product += "$" + selectedMenus.get(i).getMenu_name();
            qty += "$" + selectedMenus.get(i).getCnt();

            option += "$" + selectedMenus.get(i).getOptions().get(0).getOption_name();
            option += "|"+selectedMenus.get(i).getOptions().get(0).getOptions().get(selectedMenus.get(i).getChoices().get(0));
            for(int j=1;j<selectedMenus.get(i).getOptions().size();j++)
            {
                option += "@" + selectedMenus.get(i).getOptions().get(j).getOption_name();
                option += "|"+selectedMenus.get(i).getOptions().get(j).getOptions().get(selectedMenus.get(i).getChoices().get(j));
            }
            price += "$" + selectedMenus.get(i).getPrice_sum();
        }



        Log.d(TAG,"최종 option : "+option);
        InsertData insertData = new InsertData();
//        insertData.execute(url, app.getBizNum(), tmp_no,"T",totalprice,pay_type,category,product,qty,option,price );
        Log.d(TAG, "sendOrderToServer: getTakeout_status "+app.getTakeout_status());
        insertData.execute(url, app.getBizNum(), tmp_no,"T",totalprice,pay_type,category,product,qty,option,price,app.getTakeout_status());

    }

    //server에 주문 정보 전송 후 주문번호 등 리턴값 요청
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
            String tmp_no = (String)params[2];
            String pay = (String)params[3];
            String totalprice = (String)params[4];
            String pay_type = (String)params[5];
            String category = (String)params[6];
            String product = (String)params[7];
            String qty = (String)params[8];
            String option = (String)params[9];
            String price = (String)params[10];
            String serverURL = (String)params[0];
            String takeout = (String)params[11];
            Log.d(TAG, "sendOrderToServer: getTakeout_status2 "+takeout);
            for(int i=0;i<12;i++)
            {
                Log.d(TAG,"params: " +i+" "+(String)params[i]);
            }
            String postParameters = "mb_id="+mb_id+"&tmp_no="+tmp_no+"&pay="+pay+"&totalprice="+totalprice
                    +"&pay_type="+pay_type+"&category="+category+"&product="+product+"&qty="+qty+"&option="+option+"&price="+price+"&takeout="+takeout;
            System.out.println("주문전송 최종 url : "+postParameters);
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
    protected void validReturnValue(){//서버에 요청 보내고 온 정보 json 형태로 받고 pasing
        try{
            JSONObject jsonObj = new JSONObject(result_json);

            if(!jsonObj.has("error_no")) {//에러가 안나면 정상 처리
                String mb_id = jsonObj.getString("mb_id");//사업자 번호
                String tmp_no = jsonObj.getString("tmp_no");//주문번호
                String waiting = jsonObj.getString("waiting");//대기번호
                String od_id = jsonObj.getString("od_id");//주문 테이블 고유키

                Myapplication app = (Myapplication)getApplication();
                String mb_id_origin = app.getBizNum();//기기에 로그인된 사업자아이디

                if(mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인 후 activity 띄움
                {
                    app.setWaitingNum(Integer.parseInt(waiting));//대기번호 설정
                    app.setOrderNum(Integer.parseInt(tmp_no));//주문번호 설정
                    app.setCurrentOrder_id(od_id);//주문테이블 고유키 설정 - 포인트 적립 시 전송
                    //activity 띄운다.
                    Intent intentQR = new Intent(Order_loading_popupActivity.this, CouponAndReceipt_announce_Activity.class);
                    startActivity(intentQR);
                    finish();
                    return;
                }
                else//사업자 아아디 불일치 - 실패
                {
                    Log.d(TAG,"사업자 아이디 불일치");

                    finish();
                }
            }
            else {//에러발생시
                String error_no = jsonObj.getString("error_no");
                String error_msg = jsonObj.getString("error_msg");
                Toast.makeText(Order_loading_popupActivity.this,error_msg,Toast.LENGTH_SHORT).show();
                finish();
                return;
            }


        }catch (JSONException e){
            e.printStackTrace();

            //주문번호 받기 실패
            Intent intent = new Intent(Order_loading_popupActivity.this, Order_fail_popupActivity.class);
            intent.putExtra("fail_type","SERVER_JSON_NORETURN");
            startActivity(intent);
            finish();
            return;
        }
    }
    public void countDownTimerforgif_set(){//히든메뉴 접속용 타이머
        loading_dot = (ImageView) findViewById(R.id.order_fail_loading_dot);
        countDownTimerforgif = new CountDownTimer(50000, 300) { // 1000당 1초   5분 1초당 카운트
            int i=0;
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_1);
                        break;
                    case 1:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_2);
                        break;
                    case 2:
                        loading_dot.setImageResource(R.drawable.ic_loading_dot_3);
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
