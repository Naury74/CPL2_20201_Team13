package com.example.kioskmainpage.Activity.Pay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.LoadingActivity;
import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceipt_announce_Activity;
import com.example.kioskmainpage.Activity.Sign.SignInActivity;
import com.example.kioskmainpage.Adapter.SelectedListAdapter;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import kicc.module.KiccModule;

public class PayingPopupActivity extends AppCompatActivity {
    private static final String TAG = "PayingPopupActivity";

    String result_json;
    String url = "http://mobilekiosk.co.kr/admin/api/order.php";//주문전송
    View view;
    String type;
    String payType;
    CardReceiptInfo currentCardReceiptInfo;
//    ArrayList<SelectedMenu> selectedMenus;
    Myapplication app;
    //카카오페이용
    String KiccPath = "/sdcard/kicc/";
    String ip = "203.233.72.55";//카카오페이 결제 서버 IP주소, 테스트용 : 203.233.72.55 -> 실서버 : 203.233.72.21
    String port = "15200";//카카오페이 결제 서버 포트번호, 테스트용 : 15200 -> 실서버 : 15700
    MediaPlayer mediaPlayer;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying_popup);


        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        this.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        app = (Myapplication) getApplication();
        final Intent datafrompay = getIntent();
        type = app.getCurrentPayType();
        TextView PayingPopup_announce_text = (TextView)findViewById(R.id.PayingPopup_announce_text);


            if (type.equals("SAMSUNGPAY")||type.equals("Card")) {
                currentCardReceiptInfo = app.getCurrentCardReceiptInfo();
                System.out.println("결제 승인번호 : " + currentCardReceiptInfo.getApprovalNo());
            }
            //getDataFromAdapter();
            Handler mHandlerForannounce;//결제완료 제거용 음성

            switch (type) {
                case "Card": //카드결제시
                    mHandlerForannounce = new Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            //소리재생
                            mediaPlayer = MediaPlayer.create(PayingPopupActivity.this, R.raw.card_announce_remove);
                            mediaPlayer.start();
                        }
                    };
                    mHandlerForannounce.sendEmptyMessageDelayed(0, 1000);//1초느리게 나오게하는 핸들러
                    break;
                case "SAMSUNGPAY": //삼성페이일시
                    PayingPopup_announce_text.setText("결제가 완료되었습니다");
                    mHandlerForannounce = new Handler() {
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            //소리재생
                            mediaPlayer = MediaPlayer.create(PayingPopupActivity.this, R.raw.samsungpay_announce_complete);
                            mediaPlayer.start();
                        }
                    };
                    mHandlerForannounce.sendEmptyMessageDelayed(0, 1000);//1초느리게 나오게하는 핸들러
                    break;
                default:
                    break;
            }


            //결제중 현재 대기시간
            Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (type) {
                        case "설정필요함":
                            payType = datafrompay.getStringExtra("payType");
                            String pay_result = "";
                            if (payType.equals("KaKao")) //카카오 페이
                            {
                                app.setCurrentPayType("KaKao");//현재 결제 타입
                                String request = datafrompay.getStringExtra("request");
                                try {
                                    //KiccApproval로 결제 요청 보내고 pay_result 에 결제 결과 리턴 받음
                                    //         String TRNO = new String(KiccModule.KiccGetTRNO("KICC",KiccPath),"EUC-KR");//pos 거래번호
                                    pay_result = new String(KiccModule.KiccApproval(3, request, request.length(), 3, "", "", ip, Integer.parseInt(port), 0, "KICC", "700081", "0001", KiccPath), "EUC-KR");
                                    System.out.println("카카오페이 결과 : " + pay_result);

                                    ArrayList<String> parsed = new ArrayList<String>(Arrays.asList(pay_result.split("\\|")));
                                    System.out.println("카카오페이 결과2 : " + parsed.get(0));
                                    int result_type = Integer.parseInt(parsed.get(0));//결과 실패 or 성공

                                    if (result_type == 0)//결제성공
                                    {
                                        ArrayList<String> parsedResult = parsingKakaoPayResult(pay_result);//결과 파싱 하고 전역변수에 삽입

                                        if (parsedResult.get(6).equals("0000"))//R07이 0000이면 결제 성공
                                        {
                                            //주문 정보 전송
                                            sendOrderToServer("KAKAO");
                                   /*     Intent intentQR = new Intent(PayingPopupActivity.this, CouponAndReceipt_announce_Activity.class);
                                        intentQR.putExtra("pay_result", pay_result);
                                        startActivity(intentQR);
                                        finish();*/
                                        } else//결제실패 R20 : 실패 사유
                                        {
                                            Intent intentFail = new Intent(PayingPopupActivity.this, Card_fail_popupActivity.class);
                                            intentFail.putExtra("type", "KaKao");
                                            intentFail.putExtra("fail_type", result_type);
                                            startActivity(intentFail);
                                            finish();
                                        }
                                    } else//결제 실패
                                    {
                                        Intent intentFail = new Intent(PayingPopupActivity.this, Card_fail_popupActivity.class);
                                        intentFail.putExtra("type", "KaKao");
                                        intentFail.putExtra("fail_type", result_type);
                                        startActivity(intentFail);
                                        finish();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            } else//제로페이
                            {
                                app.setCurrentPayType("Zero");//현재 결제 타입
                                //주문정보 전송
                                sendOrderToServer("ZERO");

                            }

                            break;
                        case "Card"://카드 결제
                            app.setCurrentPayType("Card");//현재 결제 타입
                            System.out.println("승인번호 : " + currentCardReceiptInfo.getApprovalNo());
                            sendOrderToServer("CARD");

                            break;
                        case "SAMSUNGPAY":
                            app.setCurrentPayType("SAMSUNGPAY");//현재 결제 타입
                            System.out.println("승인번호 : " + currentCardReceiptInfo.getApprovalNo());
                            sendOrderToServer("SAMSUNGPAY");
                            break;
                    }
                }
            };
            mHandler.sendEmptyMessageDelayed(0, 1500);
            //결제중입니다 2초 delay

    }


    public ArrayList<String> parsingKakaoPayResult(String result) {
        ArrayList<String> parsedBest = new ArrayList<String>(Arrays.asList(result.split(";")));
        ArrayList<String> parsedResult = new ArrayList<>();
        for (int i = 0; i < parsedBest.size(); i++) {
            String[] temp = parsedBest.get(i).split("=");
            if (temp.length == 1)//결과 없을때
            {
                Log.d(TAG, "파싱결과" + (i + 1) + "번 : " + "");
                parsedResult.add("");
            } else {
                Log.d(TAG, "파싱결과" + (i + 1) + "번 : " + temp[1]);
                parsedResult.add(temp[1]);
            }
        }
        KakaoPayReceiptInfo kakaoPayReceiptInfo = new KakaoPayReceiptInfo(parsedResult.get(0)
                , parsedResult.get(1), parsedResult.get(2), parsedResult.get(3), parsedResult.get(4), parsedResult.get(5)
                , parsedResult.get(6), parsedResult.get(7), parsedResult.get(8), parsedResult.get(9), parsedResult.get(10)
                , parsedResult.get(11), parsedResult.get(12), parsedResult.get(13), parsedResult.get(14), parsedResult.get(15)
                , parsedResult.get(16), parsedResult.get(17), parsedResult.get(18), parsedResult.get(19), parsedResult.get(20)
                , parsedResult.get(21), parsedResult.get(22), parsedResult.get(23), parsedResult.get(24), parsedResult.get(25)
                , parsedResult.get(26), parsedResult.get(27), parsedResult.get(28), parsedResult.get(29));
        app.setCurrentKakaoPayReceiptInfo(kakaoPayReceiptInfo);
        return parsedResult;
    }

//    private void getDataFromAdapter() {
//        SelectedListAdapter adapter;
//        Myapplication myapp = (Myapplication) getApplication();
//        adapter = new SelectedListAdapter(myapp.getadapter().getselectedMenus(), PayingPopupActivity.this, "PayingPopupActivity");
//        String[] name = new String[adapter.getCount()];
//        String[] options = new String[adapter.getCount()];
//        int[] cnt = new int[adapter.getCount()];
//        String[] category = new String[adapter.getCount()];
//        int[] price = new int[adapter.getCount()];
//        String temp = "";
//        adapter.setselectedMenus(myapp.getadapter().getselectedMenus());
//        for (int i = 0; i < adapter.getCount(); i++) {
//            name[i] = adapter.getselectedMenus().get(i).getMenu_name();
//            price[i] = Integer.parseInt(adapter.getselectedMenus().get(i).getMenu_price());
//            cnt[i] = adapter.getselectedMenus().get(i).getCnt();
//
//            category[i] = adapter.getselectedMenus().get(i).getMenu_folder();
//
//            Log.i(TAG, "getDataFromAdapter: 옵션갯수" + myapp.getadapter().getselectedMenus().get(i).getOptions().size());
//            temp = "";  //옵션부분
//            for (int j = 0; j < myapp.getadapter().getselectedMenus().get(i).getOptions().size(); j++) {
//                String optionName = myapp.getadapter().getselectedMenus().get(i).getOptions().get(j).getOption_name();
//                Log.i(TAG, "getDataFromAdapter: " + j + optionName);
//
//                String selectedOption = myapp.getadapter().getselectedMenus().get(i).getOptions().get(j).getOptions().get(myapp.getadapter().getselectedMenus().get(i).getChoices().get(j));
//                Log.i(TAG, "getDataFromAdapter: " + j + optionName + selectedOption);
//                if (j == 0) {
//                    temp += optionName + "|" + selectedOption;
//                } else {
//                    temp += "@" + optionName + "|" + selectedOption;
//                }
//            }
//            options[i] = temp;
//
//            Log.i(TAG, "getDataFromAdapter: " + "제품명 " + name[i] + " 가격 " + price[i] + " 개수 " + cnt[i] + " 옵션 " + options[i] + " 카테고리 " + category[i]);
//
//        }
//        ;
//
//        //    adapter.clearItem(adapter);
//    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;

    }

    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

    //서버로 주문정보 전송
    public void sendOrderToServer(String payType) {
        String tmp_no = "434";//TODO : 현재사용 안함
        String totalprice = app.getCurrentOrder_price() + "";
        String pay_type = payType;

        ArrayList<SelectedMenu> selectedMenus = app.getadapter().getselectedMenus();

        //선택된 메뉴들 url 에 맞게 String 만들기
        String category = selectedMenus.get(0).getCategory();//카테고리
        String product = selectedMenus.get(0).getMenu_name();//상품명
        String qty = selectedMenus.get(0).getCnt() + "";//수량
        String option="";

        if(selectedMenus.get(0).getOptions().size()!=0) {
            option = selectedMenus.get(0).getOptions().get(0).getOption_name();//옵션
            option += "|" + selectedMenus.get(0).getOptions().get(0).getOptions().get(selectedMenus.get(0).getChoices().get(0));
        };
        for (int i = 1; i < selectedMenus.get(0).getOptions().size(); i++) {
            option += "@" + selectedMenus.get(0).getOptions().get(i).getOption_name();
            option += "|" + selectedMenus.get(0).getOptions().get(i).getOptions().get(selectedMenus.get(0).getChoices().get(i));
        }
        String price = selectedMenus.get(0).getPrice_sum() + "";

        for (int i = 1; i < selectedMenus.size(); i++) {
            category += "$" + selectedMenus.get(i).getCategory();
            product += "$" + selectedMenus.get(i).getMenu_name();
            qty += "$" + selectedMenus.get(i).getCnt();

            if(selectedMenus.get(i).getOptions().size()!=0) {
            option += "$" + selectedMenus.get(i).getOptions().get(0).getOption_name();
            option += "|" + selectedMenus.get(i).getOptions().get(0).getOptions().get(selectedMenus.get(i).getChoices().get(0));
            };

            for (int j = 1; j < selectedMenus.get(i).getOptions().size(); j++) {
                option += "@" + selectedMenus.get(i).getOptions().get(j).getOption_name();
                option += "|" + selectedMenus.get(i).getOptions().get(j).getOptions().get(selectedMenus.get(i).getChoices().get(j));
            }
            price += "$" + selectedMenus.get(i).getPrice_sum();
        }


        Log.d(TAG, "최종 option : " + option);
        InsertData insertData = new InsertData();
        insertData.execute(url, app.getBizNum(), tmp_no, "T", totalprice, pay_type, category, product, qty, option, price, app.getTakeout_status(),app.isTable_status(),app.getTablenum());
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
            result_json = result;
            validReturnValue();
        }


        @Override
        protected String doInBackground(String... params) {

            String mb_id = (String) params[1];
            String tmp_no = (String) params[2];
            String pay = (String) params[3];
            String totalprice = (String) params[4];
            String pay_type = (String) params[5];
            String category = (String) params[6];
            String product = (String) params[7];
            String qty = (String) params[8];
            String option = (String) params[9];
            String price = (String) params[10];
            String serverURL = (String) params[0];
            String takeout = (String) params[11];
            String table_status = (String) params[12];
            String table_num = (String) params[13];
            for (int i = 0; i < params.length; i++) {
                Log.d(TAG, "params: " + i + " " + (String) params[i]);
            }
            String postParameters = "mb_id=" + mb_id + "&tmp_no=" + tmp_no + "&pay=" + pay + "&totalprice=" + totalprice
                    + "&pay_type=" + pay_type + "&category=" + category + "&product=" + product + "&qty=" + qty + "&option=" + option + "&price=" + price + "&takeout=" + takeout + "&table_status=" + table_status + "&table_num=" + table_num;
            System.out.println("주문전송 최종 url : " + postParameters);
            try {
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                System.out.println("json 리턴: " + sb.toString());
                bufferedReader.close();


                return sb.toString();
            } catch (Exception e) {

                return new String("Error: " + e.getMessage());
            }

        }
    }

    protected void validReturnValue() {//서버에 요청 보내고 온 정보 json 형태로 받고 pasing
        try {
            JSONObject jsonObj = new JSONObject(result_json);

            if (!jsonObj.has("error_no")) {//에러가 안나면 정상 처리
                String mb_id = jsonObj.getString("mb_id");//사업자 번호
                String tmp_no = jsonObj.getString("tmp_no");//임시번호
                String od_no = jsonObj.getString("od_no");//주문번호
                String waiting = jsonObj.getString("waiting");//대기번호
                String od_id = jsonObj.getString("od_id");//주문 테이블 고유키
                String takeout = jsonObj.getString("takeout");//테이크아웃여부
                Myapplication app = (Myapplication) getApplication();
                String mb_id_origin = app.getBizNum();//기기에 로그인된 사업자아이디

                if (mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인 후 activity 띄움
                {
                    app.setWaitingNum(Integer.parseInt(waiting));//대기번호 설정
                    app.setOrderNum(Integer.parseInt(od_no));//주문번호 설정
                    app.setCurrentOrder_id(od_id);//주문테이블 고유키 설정 - 포인트 적립 시 전송
                    //activity 띄운다.
                    Intent intentQR = new Intent(PayingPopupActivity.this, CouponAndReceipt_announce_Activity.class);
                    startActivity(intentQR);
                    finish();
                } else//사업자 아아디 불일치 - 실패
                {
                    Log.d(TAG, "사업자 아이디 불일치");
                    //보완조치필요
                    finish();
                }
            } else {//에러발생시
                String error_no = jsonObj.getString("error_no");
                String error_msg = jsonObj.getString("error_msg");
                Toast.makeText(PayingPopupActivity.this, error_msg, Toast.LENGTH_SHORT).show();
                finish();
                //보완조치필요
                return;
            }


        } catch (JSONException e) {
            e.printStackTrace();

            //주문번호 받기 실패
            Intent intent = new Intent(PayingPopupActivity.this, Order_fail_popupActivity.class);
            intent.putExtra("fail_type", "SERVER_JSON_NORETURN");
            startActivity(intent);
            finish();
            return;
        }
    }
}
