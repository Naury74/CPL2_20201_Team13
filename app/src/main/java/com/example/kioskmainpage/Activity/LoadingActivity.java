package com.example.kioskmainpage.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.ServerConn.BlankAsynkTask;
import com.example.kioskmainpage.ServerConn.DownloadUnzip;
import com.example.kioskmainpage.Utilities.DeleteFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

public class LoadingActivity extends AppCompatActivity {


    /* 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)*/
    //서버에서 다운로드, 압축해제를 위한 DownloadUnzip 객체 선언
    DownloadUnzip downloadUnzip;
    private String TAG = "LoadingActivity";
    ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> best_menus=new ArrayList<>();
    private ArrayList<String> best_image_links=new ArrayList<>();
    private ArrayList<String> new_image_links=new ArrayList<>();
    private ArrayList<String> new_menus=new ArrayList<>();
    private ArrayList<String> best_menus_ments=new ArrayList<>();
    Myapplication myapp;
    private int PAGE_COUNT;
    private ArrayList<String> folder_names = new ArrayList<>();
    private ArrayList<String> tab_names;
    String bizNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//        //상,하단 바 제거
//        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
//        int newUiOptions = uiOptions;
//        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
//        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        newUiOptions ^= SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
//        this.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        myapp = (Myapplication)getApplication();
        bizNum = myapp.getBizNum();
        downloadUnzip = new DownloadUnzip(this.getFilesDir().getAbsolutePath(), myapp.getBizNum()); //DownloadUnzip 객체 생성

        //로딩창 실행
        CheckTypesTask task = new CheckTypesTask(this.getFilesDir().getAbsolutePath());
        task.execute();

    }
    public void getCategoryNumber()//카테고리갯수읽기
    {

        //카테고리 개수 읽어오기(folder_names)
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(this.getFilesDir().getAbsolutePath()+"/"+bizNum + "/category_number.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "EUC-KR"));

            String line = br.readLine();
            int category_number = Integer.parseInt(line);
            for(int i=1;i<=category_number;i++)
            {
                folder_names.add("main_menu_"+i);
                categories.add("main_menu_"+i+".zip");
            }
            br.close();
        }
        catch (Exception e)
        {

        }
    }
    public void getBestNew()
    {
        File save_file = new File( this.getFilesDir().getAbsolutePath()+"/"+bizNum + "/best_new");
        //best메뉴 이름들 불러오기
        try {
            //FileInputStream fileInputStream = new FileInputStream(new File(this.getFilesDir().getAbsolutePath()+"/"+bizNum + "/best_new/best.txt" ));
            // BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));

            BufferedReader br = new BufferedReader(new FileReader(save_file + "/main_menu_1/best.txt"));

            String line = br.readLine();
            ArrayList<String> parsedBest = new ArrayList<String>(Arrays.asList(line.split("\\$")));

            for(int i=0;i<parsedBest.size();i++)
            {
                String[] temp = parsedBest.get(i).split("&");

                //best_menus.add(temp[0]);
                //if(i==0)//맨 첫 메뉴는 파싱할때 string 맨 앞 index에 알수없는 문자가 삽입되므로 문자열 잘라서 새로 만듬
                  //  temp[0]=temp[0].substring(1);
                //System.out.println("베스트 이름 : "+temp[0].trim());
                Log.d(TAG,temp[0]);
                best_menus.add(temp[0]);
                File image = new File(this.getFilesDir().getAbsolutePath() +"/"+bizNum+ "/best_new"+ "/" + temp[1]);
                // File imageFileNames[] = image_folder.listFiles();

                //이미지 파일은 저장 경로만 가지고있다가 이미지 버튼에 넣을 때 디코드

                best_image_links.add(image.getAbsolutePath());


            }

            Myapplication myapp = (Myapplication)getApplication();
            myapp.setBest_menus(best_menus);
            myapp.setBest_image_links(best_image_links);
            br.close();
        }
        catch (Exception e)
        {

        }
        //best 메뉴 멘트들 불러오기
        try {
            BufferedReader br = new BufferedReader(new FileReader(save_file + "/main_menu_1/best_menu_ment.txt"));

            String line = br.readLine();
            best_menus_ments = new ArrayList<String>(Arrays.asList(line.split("\\|")));
            //맨 첫 메뉴는 파싱할때 string 맨 앞 index에 알수없는 문자가 삽입되므로 문자열 잘라서 새로 만듬
           // best_menus_ments.add(0,best_menus_ments.get(0).substring(1));

            Myapplication myapp = (Myapplication)getApplication();
            myapp.setBest_ments(best_menus_ments);
            br.close();
        }
        catch (Exception e)
        {

        }
        //new 메뉴 이름들 불러오기 TODO://newmenu부분 new 부분의 변경이 있을시 수정 필요.
        try {
            BufferedReader br = new BufferedReader(new FileReader(save_file + "/main_menu_2/new.txt"));

            String line = br.readLine();
            ArrayList<String> parsedNew = new ArrayList<String>(Arrays.asList(line.split("\\$")));
            for(int i=0;i<parsedNew.size();i++)
            {
                String[] temp = parsedNew.get(i).split("&");//이름$주소 일시에
                new_menus.add(temp[0]);//이름$주소 일시에
//                String temp = parsedNew.get(i); //이름만 있을 일시에
//                new_menus.add(temp); //이름만 있을 일시에

                //best_menus.add(temp[0]);
               // if(i==0)//맨 첫 메뉴는 파싱할때 string 맨 앞 index에 알수없는 문자가 삽입되므로 문자열 잘라서 새로 만듬
                 //   temp[0]=temp[0].substring(1);
                //System.out.println("베스트 이름 : "+temp[0].trim());


//                File image = new File(this.getFilesDir().getAbsolutePath() +"/"+bizNum+ "/best_new" + "/" + temp[1]);
                // File imageFileNames[] = image_folder.listFiles();

                //이미지 파일은 저장 경로만 가지고있다가 이미지 버튼에 넣을 때 디코드

//                new_image_links.add(image.getAbsolutePath());//이름$주소 일시에


            }

            Myapplication myapp = (Myapplication)getApplication();
            myapp.setNew_menus(new_menus);
            myapp.setNew_image_links(new_image_links);
            br.close();
        }
        catch (Exception e)
        {

        }

    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        SharedPreferences sharedPreferences = getSharedPreferences("SyncCheck", MODE_PRIVATE);// 동기화용
        ProgressDialog asyncDialog = new ProgressDialog(
                LoadingActivity.this, R.style.AppCompatAlertDialogStyle);
        String Sync_Version;
        String path;
        public CheckTypesTask(String path){
            this.path=path;
        }

        @Override
        protected void onPreExecute() {
            asyncDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            asyncDialog.setMessage("동기화 중입니다.");
            asyncDialog.setCanceledOnTouchOutside(false);//터치해도 다이얼로그 안 사라짐
            // show dialog 상하단바 제거
            int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
            int newUiOptions = uiOptions;
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            newUiOptions ^= SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            asyncDialog.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            asyncDialog.show();
            super.onPreExecute();
            Sync_Version = sharedPreferences.getString("Sync_Version","12345678901234");//버전확인
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //전체 다운로드, 압축해제 하는 부분
            //TODO: 지금은 앱이 실행될 때마다 다운로드,압축해제를 실행하는데 서버에 DB가 생기면 그대로 만들고 그냥 실행시 load만 하도록 수정

            Log.d(TAG, "어플리케이션이 가지고 있는 버전"+Sync_Version+"  DOUBLE: "+Double.parseDouble(Sync_Version) +"long"+Long.parseLong(Sync_Version));
            Log.d(TAG, "서버로 부터 받은 버전"+myapp.getLastest_SyncVersion()+"  DOUBLE: "+Double.parseDouble(myapp.getLastest_SyncVersion()) +"long"+myapp.getLastest_SyncVersion());
            if(!myapp.isitSkipDown()) //개발완료시 제거
            if(Long.parseLong(myapp.getLastest_SyncVersion())>Long.parseLong(Sync_Version)) {
                try{
                    DeleteFiles deleteFiles = new DeleteFiles(path+"/"+myapp.getBizNum());
                    deleteFiles.setDirEmpty("");
                }catch(Exception e){
                };
                downloadUnzip.doDownUnzip();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            //카테고리 개수 읽어오기기
            getCategoryNumber();
            //best메뉴랑 new메뉴 이름들 불러오기
            getBestNew();

            //업데이트된 버전을 기록
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Sync_Version",myapp.getLastest_SyncVersion());//업데이트된 버전을 기록
            editor.commit();
            //MainActivity로 이동하기위한 intent
            Intent intent_ToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            intent_ToMainActivity.putExtra("folderNames",folder_names);
            startActivity(intent_ToMainActivity);
            asyncDialog.dismiss();
            asyncDialog.cancel(); //메모리 누수방지지
            finish();
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}