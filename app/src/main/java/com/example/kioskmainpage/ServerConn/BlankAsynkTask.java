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
public class BlankAsynkTask extends AsyncTask<String, Void, ArrayList<String>> {

    final String TAG = "BlankAsynkTask";
    String result_json;
    String biznum;
    ArrayList<String> sand_parameters=new ArrayList<String>();// 보낼 파라미터명들
    ArrayList<String> input_datas=new ArrayList<String>();// 보낼 파라미터값들
    ArrayList<String> get_datas=new ArrayList<String>();// 받을 파라미터명들
    String url;//주문전송
    String type;
//    "http://mobilekiosk.co.kr/admin/api/get_company.php";


    public BlankAsynkTask(String Biznum,String type, ArrayList<String> sand_parameters,ArrayList<String> input_datas,ArrayList<String> get_datas,String url) {
        this.sand_parameters = sand_parameters;
        this.input_datas = input_datas;
        this.get_datas = get_datas;
        biznum = Biznum;
        Log.d(TAG, "요청 type : " + type);
        Log.d(TAG, "요청 sand_parameters : " + type+this.sand_parameters);
        Log.d(TAG, "요청 input_datas : " + type+this.input_datas);
        Log.d(TAG, "요청 get_datas : " + type+this.get_datas);
        this.type = type;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        super.onPostExecute(result);
    }


    @Override
    protected ArrayList<String> doInBackground(String... params) {

        String mb_id = biznum;
        ArrayList<String> result=new ArrayList<String>();
        String serverURL = url;
        String postParameters="";
        {//파라미터 작성
            for(int i =0;i<sand_parameters.size();i++){
                postParameters += sand_parameters.get(i)+"="+input_datas.get(i);
                if(i<sand_parameters.size()-1)
                {
                    postParameters +="&";
                }
            }
        }
        Log.d(TAG, "서버요청 최종 url : "+url+"?" + postParameters);
        System.out.println("서버요청 최종 url : "+url+"?" + postParameters);
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
            ArrayList<String> results;
            try{results = validReturnValue();}catch (Exception e){results=new ArrayList<>();}
            return results;
        } catch (Exception e) {
            result.add("Error: " + e.getMessage());
            return result;
        }

    }

    protected ArrayList<String> validReturnValue() {//서버에 요청 보내고 온 정보 json 형태로 받고 pasing
        ArrayList<String> result=new ArrayList<String>(); //결과값들이 들어갈자리-> 목록과 같은 위치에 데이터가 저장됨 Ex) get_datas의 0번자리에 mb_id가 있다면 0번자리에 mb_id 해당하는 데이터가 저장
        try {
            JSONObject jsonObj = new JSONObject(result_json);
            if (!jsonObj.has("error_no")) {//에러가 안나면 정상 처리
                String mb_id = jsonObj.getString("mb_id");//사업자 번호
                String mb_id_origin = biznum;//기기에 로그인된 사업자아이디
                if (mb_id.equals(mb_id_origin))//같은 사업자 아이디인지 확인 후 진행
                {
                    int size =get_datas.size();
                    String results="";
                    for(int i =0;i<size;i++) {//가져와야하는 데이터 목록을 통해서 가져옴.
                        String temp = jsonObj.getString(get_datas.get(i));//사업자 번호
                        result.add(temp);
                        results+=get_datas.get(i)+"="+result.get(i);
                    }
                    Log.d(TAG, type+"  validReturnValue: "+results);
                } else//사업자 아아디 불일치 - 실패
                {
                    Log.d(TAG, "사업자 아이디 불일치");
                }
            } else {//에러발생시
                String error_no = jsonObj.getString("error_no");
                String error_msg = jsonObj.getString("error_msg");
                result.add("error");
                result.add(error_no);
                result.add(error_msg);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return result;
            //업데이트 정보 받기 실패
        }
        return result;
    }
}
