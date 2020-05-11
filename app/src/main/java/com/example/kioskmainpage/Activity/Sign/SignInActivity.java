package com.example.kioskmainpage.Activity.Sign;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.AppposUnavailableActivity;
import com.example.kioskmainpage.Activity.LoadingActivity;
import com.example.kioskmainpage.Background_Checkers;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;

import com.example.kioskmainpage.R;
import com.example.kioskmainpage.ServerConn.BlankAsynkTask;
import com.example.kioskmainpage.ServerConn.TransferToServer;
import com.example.kioskmainpage.ShutdownAdminReceiver;
import com.example.kioskmainpage.Utilities.Appchecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    final public int MY_PERMISSION_REQUEST_STORAGE = 0;
    private static final String TAG = "testtest**signIn";
    Myapplication app;
    String login_url = "http://mobilekiosk.co.kr/admin/api/login.php";//서버 url
    String result_json;//로그인 요청후 리턴값 받아옴
    Button confirmButton;
    EditText EditBizNum, editPassword;
    boolean appowschecked =false;
    AlertDialog alert;
    Background_Checkers background_Checkers;//테스트후 삭제대상
    //테스트후 삭제대상
    TextView TestText1,TestText2;
    boolean threadset=true;

    private static Handler checkers;

    public void checkDataSaving(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int restrictStatus = cm.getRestrictBackgroundStatus();
            switch (restrictStatus) {
                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED:
                    try {
                        alert = new AlertDialog.Builder(SignInActivity.this)
                                .setTitle("주의")
                                .setMessage("\n데이터 세이버 미적용 앱을 활성화시켜주세요.\n")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                        startActivity(intent);
                                    }
                                }).show();
                    } catch (Exception e) {

                    }
                    break;
                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED:
                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED:
                default:
                    return;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_sign_in);


        SharedPreferences sharedPreferencesforhome = getSharedPreferences("HomeAppSet", MODE_PRIVATE);
        if (sharedPreferencesforhome.getBoolean("HomeAppSet_status", false)) {
            SharedPreferences.Editor editor = sharedPreferencesforhome.edit();
            editor.putBoolean("HomeAppSet_status", false);
            editor.commit();
            finish();
        }


        //qr스캐너 관련 권한문제 확인
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //Android 6.0이상 권한문제!(build-sdk 23이상에서 해야함)
            checkPermission();
        } else {
            MakeFolder();
        }
        checkDataSaving(this);

        app = (Myapplication) getApplication();
        app.setMainTheme(new MainTheme("WHITE"));
        System.out.println("테마  " + app.getMainTheme().getThemeItem().getBACKGROUND_COLOR_ID());
        //상,하단 바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        //사업자 번호 및 비밀번호 선언 및 기존 로그인 정보 입력
        EditBizNum = (EditText) findViewById(R.id.biz_num);
        editPassword = (EditText) findViewById(R.id.editPassword2);
        SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
        EditBizNum.setText(sharedPreferences.getString("AutoLogin_bizNum", ""));
        editPassword.setText(sharedPreferences.getString("AutoLogin_password", ""));


        //회원가입 버튼 객체 & 리스너
        TextView signUpButton = (TextView) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 클릭 시 SignUpActivity 띄움
                Intent signUpIntent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        confirmButton = (Button) findViewById(R.id.bizNumConfirmButton);
        confirmButton.setOnClickListener(new bizNumConfrimListener());
        CheckBox signin_checkbox_Autologin = (CheckBox) findViewById(R.id.signin_checkbox_Autologin);
        signin_checkbox_Autologin.setOnCheckedChangeListener(new checnkwaitingListener());


        //자동로그인 부분
        if (sharedPreferences.getBoolean("AutoLogin_status", false)) {
            signin_checkbox_Autologin.setChecked(true);
            //자동로그인
            {
                View view = getLayoutInflater().from(this).inflate(R.layout.activity_sign_in, null);
                //TODO: 서버에서 회원인증절차 구현
                if (EditBizNum.getText().toString().length() != 10) {
                    Toast.makeText(view.getContext(), "사업자번호를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    confirmButton.setClickable(false); //여러번눌러서 LoadingActivity 여러번 접근하는 것을 방지
                    String password = editPassword.getText().toString();
                    if (passwordValidator(password)) {
                        //서버에 인증요청 보냄
                        InsertData insertData = new InsertData();
                        insertData.execute(login_url, EditBizNum.getText().toString(), password);
                    } else {
                        Toast.makeText(view.getContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                        confirmButton.setClickable(true); //버튼 접근허용
                        return;
                    }
                }
            }
        }
        //브로드 케스트용 Application
        app.setApplication(getApplication());
        //크기 자동화용 창의 크기 저장
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        app.setSize_width(dm.widthPixels);
        app.setSize_height(dm.heightPixels);
        Log.d(TAG, "onCreate: 사이즈 가로" + dm.widthPixels + "    세로   " + dm.heightPixels+"  모델  "+Build.MODEL);
        //개발완료후 제거대상
        TextView signin_Title = (TextView) findViewById(R.id.TEXTBizNum); // 로그인 텍스트를 클릭하면 자동입력
        signin_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (EditBizNum.getText().toString()) {
                    case "5901020009":
                        EditBizNum.setText("8423500051");
                        break;
                    default:
                        EditBizNum.setText("5901020009");
                }
                editPassword.setText("1234567");
            }
        });
        CheckBox checkBoxforwaiting = (CheckBox) findViewById(R.id.checkBoxforwaiting);
        checkBoxforwaiting.setOnCheckedChangeListener(new checnkwaitingListener());
        CheckBox checkforSKIPdown = (CheckBox) findViewById(R.id.checkforSKIPdown);
        checkforSKIPdown.setOnCheckedChangeListener(new checnkwaitingListener());
        CheckBox checkBoxforMenutheme = (CheckBox) findViewById(R.id.checkforMenuTheme);
        checkBoxforMenutheme.setOnCheckedChangeListener(new checnkwaitingListener());
        CheckBox checkforSKIPpay = (CheckBox) findViewById(R.id.checkforSKIPpay);
        checkforSKIPpay.setOnCheckedChangeListener(new checnkwaitingListener());
        if (app.isitWaitingOn()) checkBoxforwaiting.setChecked(true);
        if (app.isitSkipDown()) checkforSKIPdown.setChecked(true);
        checkforSKIPdown.setChecked(false); //이제 기본이 다운안받고 드가는거임
        //여기까지 제거대상


        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);

        ComponentName componentName = new ComponentName(getApplicationContext(), ShutdownAdminReceiver.class);
        //스크린 끄고 키는 기능을 위해 권한 묻기
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent4 = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent4.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent4, 0);
        }

        //테스트용 삭제필요(블루투스 연결및해제)
        TextView reader_status = (TextView) findViewById(R.id.reader_status);
        boolean Blue_state = false;
        try {
            BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            Blue_state = true;
            Log.d(TAG, "onCreate:Blue_state_true");
        } catch (Exception e) {
            reader_status.setText("블루투스가 지원되지 않습니다.");
            Blue_state = false;
            Log.d(TAG, "onCreate:Blue_state_false");
        }
        String android_model;
        if (android.os.Build.MODEL.length() < 11)
            android_model = android.os.Build.MODEL;
        else
            android_model = (android.os.Build.MODEL).substring(0, 11);
        if (Blue_state && !(android_model.equals("Android SDK"))) {
            background_Checkers = new Background_Checkers(this, (Myapplication) getApplication());
            background_Checkers.Check_reader_state();
        }


        TestText1 = (TextView)findViewById(R.id.TestText1);//TEST용
        TestText2 = (TextView)findViewById(R.id.TestText2);//TEST용
        checkers = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 50:
                        if (app.getReader_status() == false) {
                            background_Checkers.go_unavailable_popup();
                        }
                        break;
                    case 51: //TEST용
                        if(TestText1!=null&&background_Checkers!=null) {
                            setTestText(TestText1, "bonded : " + background_Checkers.getbonded_Devices());
                            Log.d("블루투스TEST", "bonded: " + background_Checkers.getbonded_Devices());
                        }
                        break;
                    case 52: //TEST용
                        if(TestText2!=null&&background_Checkers!=null) {
                            setTestText(TestText2, "conected : " + background_Checkers.getconnected_Devices());
                            Log.d("블루투스TEST", "conected: " + background_Checkers.getconnected_Devices());
                        }
                        break;
                }
            }
        };

        final Thread thread = new Thread(new Runnable() { //TEST용
            @Override
            public void run() {
                while (threadset) {
                        try {
                            Thread.sleep(1500);
                            checkers.sendEmptyMessage(52);
                            Thread.sleep(1500);
                            checkers.sendEmptyMessage(51);
                        } catch (Exception e) {
                        }
                }
            }
        });
        thread.setDaemon(true);        //TEST용
        thread.start();        //TEST용


        Appchecker appchecker = new Appchecker(this);
        if (appchecker.isit_installed() == false) {
            Intent intentToAppposUnavailableActivity = new Intent(this, AppposUnavailableActivity.class);
            startActivityForResult(intentToAppposUnavailableActivity,1);
        }
        else{
            checkers.sendEmptyMessage(50);//
        }



    }

    public void setTestText(TextView t, String s) {
        TextView t1 = t;
        t.setText(s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK)
        switch (requestCode) {
            case 1:
                appowschecked =true;
                checkers.sendEmptyMessage(50);//
                break;
        }
    }

    public static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }


    @Override
    protected void onDestroy() {
        threadset=false;//TEST
        super.onDestroy();
    }

    public void MakeFolder() {
        File path = new File("/sdcard/kicc/LOG");
        if (!path.isDirectory()) {
            path.mkdirs();
        }

        File path_trlog = new File("/sdcard/kicc/TRLOG");
        if (!path_trlog.isDirectory()) {
            path_trlog.mkdirs();
        }
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,}, MY_PERMISSION_REQUEST_STORAGE);
        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            MakeFolder();
        }


    }

    //TODO : 암호화 알고리즘 선정 - sha256 해시
    public static String sha256(String str) {
        String SHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    //확인 버튼 리스너
    public class bizNumConfrimListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String biz_num = EditBizNum.getText().toString();
            switch (view.getId()) {
                case R.id.bizNumConfirmButton:

                    //TODO: 서버에서 회원인증절차 구현
                    if (biz_num.length() != 10) {
                        Toast.makeText(view.getContext(), "사업자번호를 제대로 입력해주세요", Toast.LENGTH_SHORT).show();

                        return;
                    } else {
                        confirmButton.setClickable(false); //여러번눌러서 LoadingActivity 여러번 접근하는 것을 방지
                        String password = editPassword.getText().toString();
                        if (passwordValidator(password)) {
                            //서버에 인증요청 보냄
                            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                            if (mWifi == null)//인터넷 연결 x
                            {
                                Toast.makeText(SignInActivity.this, "인터넷을 연결해주십시오", Toast.LENGTH_SHORT).show();
                                confirmButton.setClickable(true);
                                return;
                            } else {
                                InsertData insertData = new InsertData();
                                insertData.execute(login_url, biz_num, password);
                            }
                        } else {
                            Toast.makeText(view.getContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                            confirmButton.setClickable(true); //버튼 접근허용
                            return;
                        }
                    }
                    break;
            }
        }
    }


    //비밀번호가 조건에 맞는지 확인
    public boolean passwordValidator(String password) {
        if (password.length() < 6 || password.length() > 12) {
            return false;
        } else
            return true;
    }

    //server에 인증 요청
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
            String mb_password = (String) params[2];


            String serverURL = (String) params[0];

            String postParameters = "mb_id=" + mb_id + "&mb_password=" + mb_password;
            System.out.println(postParameters);
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

    protected void validReturnValue() {//서버에 회원가입 요청 보내고 온 정보 json 형태로 받고 pasing
        try {
            JSONObject jsonObj = new JSONObject(result_json);
            String resultOfreturn = jsonObj.getString("result");
            String msgOfreturn = jsonObj.getString("msg");
            System.out.println(resultOfreturn);
            System.out.println(msgOfreturn);
            if (resultOfreturn.equals("Y"))//로그인완료
            {

                app.setBizNum(EditBizNum.getText().toString());
                SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("AutoLogin_bizNum", EditBizNum.getText().toString());//사업자번호저장
                editor.putString("AutoLogin_password", editPassword.getText().toString());//비밀번호저장
                editor.commit();
                SharedPreferences sharedPreferences2 = getSharedPreferences("SyncCheck", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("SyncCheck", "DEFAULT");//싱크상태 기본설정
                editor2.commit();


                //로그인이 완료되면 서버로 부터 현재 버전을 요청함.
                if (!app.isitSkipDown())//  TODO 개발완료시 제거부분 중괄호 제거후 내용은 남길것
                {
                    //서버로 부터 현재 버전을 요청함.
                    try {
                        TransferToServer transferToServer = new TransferToServer(); //transferToServer 객체생성
                        transferToServer.TransferToServer_datainsert(app.getBizNum(), app);
                        transferToServer.execute();
                    } catch (Exception e) {
                        Log.d(TAG, "서버에 버전 요청 실패");
                    }
                }

                // 사용자 데이터 받아오기.
                ArrayList<String> userdata_sand_parameters=new ArrayList<String>();// 보낼 파라미터명들
                ArrayList<String> userdata_input_datas=new ArrayList<String>();// 보낼 파라미터값들
                ArrayList<String> userdata_get_datas=new ArrayList<String>();// 받을 파라미터명들
                userdata_sand_parameters.add("mb_id");userdata_input_datas.add(app.getBizNum());
                userdata_get_datas.add("mb_name");userdata_get_datas.add("mb_id");userdata_get_datas.add("mb_email");userdata_get_datas.add("mb_hp");userdata_get_datas.add("mb_addr");
                BlankAsynkTask task_userdata = new BlankAsynkTask(app.getBizNum(),"get_company",userdata_sand_parameters,userdata_input_datas,userdata_get_datas,"http://mobilekiosk.co.kr/admin/api/get_company.php");
                try{
                    app.setUser_data(task_userdata.execute().get());
                    Log.d(TAG, "onPostExecute: "+app.getUser_data());
                }catch(Exception e){
                    e.getMessage();
                    Log.d(TAG, "onPostExecute: "+"유저데이터 받기 실패");
                };

                //로그인 완료되면 LoadingActivity 띄운다.
                Intent intent = new Intent(SignInActivity.this, LoadingActivity.class);
                intent.putExtra("bizNum", EditBizNum.getText().toString());
                startActivity(intent);
                finish();
            } else//로그인 실패
            {
                Toast.makeText(SignInActivity.this, msgOfreturn, Toast.LENGTH_SHORT).show();
                confirmButton.setClickable(true); //버튼 접근허용
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class checnkwaitingListener implements CompoundButton.OnCheckedChangeListener {
        Myapplication myapp = (Myapplication) getApplication();

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.signin_checkbox_Autologin:
                    SharedPreferences sharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (isChecked == true) {
                        myapp.setAutologinOn();
                        editor.putBoolean("AutoLogin_status", true);
                    } else {
                        myapp.setAutologinOff();
                        editor.putBoolean("AutoLogin_status", false);
                    }
                    editor.commit();
                    break;
                case R.id.checkBoxforwaiting: // 완성시제거
                    if (isChecked == true)
                        myapp.setWaitingOn();
                    else
                        myapp.setWaitingOff();
                    break;
                case R.id.checkforSKIPdown: // 완성시제거
                    if (isChecked == true)
                        myapp.setSkipDown();
                    else
                        myapp.setSkipDownoff();
                    break;
                case R.id.checkforMenuTheme: // 완성시제거
                    if (isChecked == true)
                        myapp.setMainTheme(new MainTheme("BLACK"));
                    else
                        myapp.setMainTheme(new MainTheme("WHITE"));
                    break;
                case R.id.checkforSKIPpay: // 완성시제거
                    if (isChecked == true) {
                        myapp.setSkippay(true);
                    } else
                        myapp.setSkippay(false);
                    Log.d(TAG, "onCheckedChanged: " + myapp.getSkippay());
                    break;
            }
        }
    }


}
