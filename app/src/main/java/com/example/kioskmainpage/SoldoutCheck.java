package com.example.kioskmainpage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kioskmainpage.Adapter.RAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SoldoutCheck extends AsyncTask<String, Void, String> {
    String TAG = "SoldoutCheck";
    String result_json="";//서버 전송시 리턴값
    ArrayList<String> sold_out_menu_names = new ArrayList<>();//매진 메뉴 리스트
    Myapplication myapp;
    public void init(Myapplication myapp) {
        this.myapp = myapp;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        result_json = result;
        validReturnValue2();
        Log.d(TAG, "초기 매진설정 완료");
    }


    @Override
    protected String doInBackground(String... params) {

        String mb_id = (String) params[1];

        String serverURL = (String) params[0];

        String postParameters = "mb_id=" + mb_id;
        System.out.println(postParameters);
        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            System.out.println("sold out url : " + serverURL);

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

    protected void validReturnValue2() {//서버에 솔드아웃 메뉴 요청 보내고 온 정보 json 형태로 받고 pasing

        try {
            JSONArray jsonArray = new JSONArray(result_json);
            JSONObject jsonObj;

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                String soldout_item = jsonObj.getString("it_name");//매진메뉴
                sold_out_menu_names.add(soldout_item);


                System.out.println("now sold out : " + soldout_item);
            }

            ArrayList<RAdapter> RAdapters = myapp.getRAdapters();

            //TODO : 매진 처리
            Log.d(TAG, "RADAPTERs 사이즈 : " + RAdapters.size());
            for (int j = 0; j < sold_out_menu_names.size(); j++) {
                Log.d(TAG, "매진 아이템 : " + sold_out_menu_names.get(j));
                for (int i = 0; i < RAdapters.size(); i++) {
                    RAdapters.get(i).setSoldout(true, sold_out_menu_names.get(j));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
