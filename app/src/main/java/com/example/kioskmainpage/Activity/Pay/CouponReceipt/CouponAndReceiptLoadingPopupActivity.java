package com.example.kioskmainpage.Activity.Pay.CouponReceipt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.kioskmainpage.Activity.Admin.Admin_MainActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.Pay.CompleteOrder_PayActivity;
import com.example.kioskmainpage.Activity.Pay.PayingPopupActivity;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class CouponAndReceiptLoadingPopupActivity extends AppCompatActivity {
    public CountDownTimer countDownTimerforgif;
    private static String TAG = "CouponAndReceiptLoadingPopupActivity";
    private String coupon_url = "http://mobilekiosk.co.kr/admin/api/point.php";//쿠폰 적립 url
    private String receipt_url = "http://mobilekiosk.co.kr/admin/api/order_file/";//영수증 전송 url
    //"http://220.66.219.163/kiosk";
    String result_json;
    int type;
    String phonenumber;
    ArrayList<SelectedMenu> selectedMenus;
    ImageView CNRLoding_loding_dot;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimerforgif.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_and_receipt_loding_pop);
        //    countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


//        //GIF 이미지 실행설정 -> svg로 변경되어 주석처리
//        CNRLoding_loding_dot = (ImageView) findViewById(R.id.CNRLoding_loding_dot);
//        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(CNRLoding_loding_dot);
//        Glide.with(this).load(R.drawable.gif_loding_dot).into(gifImage);

        //무엇을 위한 로딩화면인지 정하기 위함
        Intent dataforloading = getIntent() ;
        type  = dataforloading.getIntExtra("type",-1); //21 쿠폰기능로딩 22 영수증로딩
        phonenumber  = dataforloading.getStringExtra("phonenumber"); //전화번호를가져옴
        selectedMenus = dataforloading.getParcelableArrayListExtra("selectedMenus");
        //텍스트 설정
        TextView CNRLoding_text1 = (TextView)findViewById(R.id.CNRLodingtextView1);
//        TextView CNRLoding_text2 = (TextView)findViewById(R.id.CNRLodingtextView2);
        TextView CNRLoding_text3 = (TextView)findViewById(R.id.CNRLodingtextView3);
        LinearLayout CNRLodingLayout_1 = (LinearLayout)findViewById(R.id.CNRLodingLayout_1);
        String text1;
        String text3;
        int announce_mint = getResources().getColor(R.color.announce_mint);
        switch (type)
        {
            case 21: //21 쿠폰
                CNRLodingLayout_1.setVisibility(View.VISIBLE);
                text3 = "적립된 포인트는 모바일 오더 앱에서\n 조회 및 사용이 가능합니다";
                CNRLoding_text3.setText(text3);
                Spannable span4= (Spannable) CNRLoding_text3.getText();
//                span4.setSpan(new ForegroundColorSpan(announce_mint), 9, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
                span4.setSpan(new StyleSpan(Typeface.BOLD), 9,  17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//                span4.setSpan(new ForegroundColorSpan(announce_mint), 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
                span4.setSpan(new StyleSpan(Typeface.BOLD), 21,  28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
//                CNRLoding_text2.setVisibility(View.INVISIBLE);
                break;
            case 22: //22 영수증
                CNRLoding_text1.setText("발행중");
//                LinearLayout CNRLodingLayout_for_coupon = (LinearLayout)findViewById(R.id.CNRLodingLayout_for_coupon);
                ImageView imageView4 = (ImageView)findViewById(R.id.imageView4);
                imageView4.setVisibility(View.INVISIBLE);
                text3 = "           잠시만 기다려주세요";
                CNRLoding_text3.setText(text3);

                break;
            default:
                text1 = "type값을 확인해주세요.";
                CNRLoding_text1.setText(text1);
                break;
        }
        countDownTimerforgif_set();
        countDownTimerforgif.start();
        /* //text1수정 필요시 사용
        CNRLoding_text1.setText(text1);
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span1 = (Spannable) CNRLoding_text1.getText();
        span1.setSpan(new StyleSpan(Typeface.BOLD), 0,  2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span1.setSpan(new ForegroundColorSpan(rounded_red), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        */



        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                Myapplication app = (Myapplication)getApplication();

                switch(type)
                {
                    case 21: //21 쿠폰
                        String point = "";
                        point = (app.getCurrentOrder_price() * 0.1) + "";//적립 포인트 결제금액의 10%로 설정
                        InsertData insertData = new InsertData();
                        insertData.execute(coupon_url,app.getBizNum(),"POINT_REQUEST",phonenumber, point, app.getCurrentOrder_id());
                        break;

                    case 22: //22 영수증
                        String filePath = getFilesDir().getAbsolutePath()+"/"+app.getCurrentOrder_id()+".png";
                        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
                        imageAsyncTask.execute(receipt_url,filePath,phonenumber);
                        break;
                }

            }
        }, 3000);// 3초 정도 딜레이를 준 후 시작
    }

    public boolean onTouchEvent(MotionEvent event) // 외부 터치불가
    {
        return false;

    }
    public void onBackPressed() {
        return;
    }//뒤로가기 버튼 막음

    public class ImageAsyncTask extends AsyncTask<String, Void, String> {


        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = UUID.randomUUID().toString();
        int maxBufferSize = 1024;
        public static final int MAX_READ_TIME = 10000;
        public static final int MAX_CONNECT_TIME = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("성공"))
            {
                Intent intent2 = new Intent (CouponAndReceiptLoadingPopupActivity.this, CompleteOrder_PayActivity.class);
                intent2.putExtra("type",2);
                intent2.putExtra("phonenumber", phonenumber);
                startActivity(intent2);
            }
            else
            {
                Toast.makeText(CouponAndReceiptLoadingPopupActivity.this,"영수증 전송 실패",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onPostExecute: "+"영수증 전송 실패");
            }
            finish();

        }


        @Override
        protected String doInBackground(String... params) {

            String urlString = (String)params[0];
            String fileName = (String)params[1];
            String phonenumber = (String)params[2];
            String result = "";
            String lineEnd = "\r\n";

            urlString = urlString+"?"+"hp="+phonenumber;
            String twoHyphens = "--";

            String boundary = "*****";

            try {
                File sourceFile = new File(fileName);
                if(sourceFile.exists())
                    Log.d(TAG,"영수증 존재 URL: "+urlString);
                else
                    Log.d(TAG,"영수증 없음");
                DataOutputStream dos;

                if (!sourceFile.isFile()) {

                    Log.e("uploadFile", "Source File not exist :" + fileName);

                } else {
                    FileInputStream mFileInputStream = new FileInputStream(sourceFile);
                    URL connectUrl = new URL(urlString);

                    // open connection
                    HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    //conn.setRequestProperty("uploaded_file", fileName);

                    // write data
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);


                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024 * 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);



                    // read image

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    mFileInputStream.close();

                    dos.flush(); // finish upload...
                    Log.d(TAG,"영수증 코드 : "+conn.getResponseCode()+" "+conn.getResponseMessage());
                    if (conn.getResponseCode() == 200) {

                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");

                        BufferedReader reader = new BufferedReader(tmp);
                        result = "성공";
                        StringBuffer stringBuffer = new StringBuffer();

                        String line;

                        while ((line = reader.readLine()) != null) {

                            stringBuffer.append(line);

                        }
                        Log.d(TAG,"영수증 리턴 : "+stringBuffer.toString());
                    }

                    mFileInputStream.close();

                    dos.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
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
            String content = (String)params[2];
            String hp = (String)params[3];
            String point = (String)params[4];
            String od_id = (String)params[5];

            String serverURL = (String)params[0];

            String postParameters = "mb_id="+mb_id+"&content="+content+"&hp="+hp+"&point="+point+"&od_id="+od_id;
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
    protected void validReturnValue() {//서버에 포인트 적립 요청 보내고 온 정보 json 형태로 받고 pasing
        try {
            JSONObject jsonObj = new JSONObject(result_json);

            String mb_id = jsonObj.getString("mb_id");//사업자 번호
            String hp = jsonObj.getString("hp");//전화번호
            String msg = jsonObj.getString("msg");//메시지
            String point = jsonObj.getString("point");//방금 적립된 포인트
            String sum_point = jsonObj.getString("sum_point");//이 때까지 쌓인 포인트
            String uesr_authd = jsonObj.getString("uesr_authd");//회원인지 아닌지. Y,N
            String uesr_name = jsonObj.getString("uesr_name");//고객 이름

            Myapplication app = (Myapplication) getApplication();
            String mb_id_origin = app.getBizNum();//기기에 로그인된 사업자아이디

            if (mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인
            {
                if(uesr_authd.equals("N")) //등록된 인원이 아닌경우
                {
                    Intent intent = new Intent (CouponAndReceiptLoadingPopupActivity.this, CouponFailActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if((uesr_authd.equals("Y"))){ //등록된 인원인 경우
                    Intent intent = new Intent(CouponAndReceiptLoadingPopupActivity.this, CouponComplete_Activity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("sum_point", sum_point);
                    intent.putExtra("uesr_name", uesr_name);
                    startActivity(intent);
                    finish();
                }
            }
            else//사업자 아아디 불일치 - 실패
            {
                Log.d(TAG, "사업자 아이디 불일치");
                finish();
            }



        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void countDownTimerforgif_set(){//히든메뉴 접속용 타이머
        CNRLoding_loding_dot = (ImageView) findViewById(R.id.CNRLoding_loding_dot);
        countDownTimerforgif = new CountDownTimer(50000, 300) { // 1000당 1초   5분 1초당 카운트
            int i=0;
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        CNRLoding_loding_dot.setImageResource(R.drawable.ic_loading_dot_1);
                        break;
                    case 1:
                        CNRLoding_loding_dot.setImageResource(R.drawable.ic_loading_dot_2);
                        break;
                    case 2:
                        CNRLoding_loding_dot.setImageResource(R.drawable.ic_loading_dot_3);
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
