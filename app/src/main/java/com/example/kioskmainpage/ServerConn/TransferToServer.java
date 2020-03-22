package com.example.kioskmainpage.ServerConn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Pay.CouponReceipt.CouponAndReceipt_announce_Activity;
import com.example.kioskmainpage.Activity.Pay.Order_fail_popupActivity;
import com.example.kioskmainpage.Activity.Pay.Order_loading_popupActivity;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.Myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;


//server에 주문 정보 전송 후 주문번호 등 리턴값 요청
public class TransferToServer extends AsyncTask<String, Void, String> {

    final String TAG = "TransferToServer";
    String result_json;
    String biznum;
    String lastest_update_time;
    String url = "http://mobilekiosk.co.kr/admin/api/update_time.php";//주문전송
    Myapplication myapp;

    public void TransferToServer_datainsert(String Biznum, Myapplication myapplication) {
        myapp = myapplication;

        biznum = Biznum;
        Log.d(TAG, "사업자번호 : " + biznum);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }


    @Override
    protected String doInBackground(String... params) {

        String mb_id = biznum;

        String serverURL = "http://mobilekiosk.co.kr/admin/api/update_time.php";
        String postParameters = "mb_id=" + mb_id;
        System.out.println("동기화요청 최종 url : " + postParameters);
        try {
            URL url = new URL(serverURL);
            Log.d(TAG, "doInBackground: " + url);
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

            result_json = sb.toString();
            validReturnValue();
            return sb.toString();
        } catch (Exception e) {

            return new String("Error: " + e.getMessage());
        }

    }

    protected void validReturnValue() {//서버에 요청 보내고 온 정보 json 형태로 받고 pasing
        try {
            JSONObject jsonObj = new JSONObject(result_json);

            if (!jsonObj.has("error_no")) {//에러가 안나면 정상 처리
                String mb_id = jsonObj.getString("mb_id");//사업자 번호
                String update_time = jsonObj.getString("update_time");//마지막 수정시각

                String mb_id_origin = biznum;//기기에 로그인된 사업자아이디
                if (mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인 후 진행
                {
                    if (update_time.length() > 14) {
                        try {
                            SimpleDateFormat date_format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMddHHmmss");
                            ParsePosition pos = new ParsePosition(0);
                            Date temp_update_time = date_format1.parse(update_time, pos);
                            Log.d(TAG, "서버버전의 형식이 맞지 않음");
                            System.out.println("서버버전의 형식이 맞지 않음");
                            update_time = new_format.format(temp_update_time);
                        }catch (Exception e){
                            Log.d(TAG, "validReturnValue: ="+update_time+"버전 정보 에러");
                        }

                    }
                    lastest_update_time = update_time;
                    Log.d(TAG, update_time);
                    myapp.setLastest_SyncVersion(update_time);

                    Log.d(TAG, "서버로부터 받은 마지막 수정 버전 " + update_time);
                } else//사업자 아아디 불일치 - 실패
                {
                    Log.d(TAG, "사업자 아이디 불일치");
                }
            } else {//에러발생시
                String error_no = jsonObj.getString("error_no");
                String error_msg = jsonObj.getString("error_msg");
            }


        } catch (JSONException e) {
            e.printStackTrace();
            //업데이트 정보 받기 실패
        }

    }
}

