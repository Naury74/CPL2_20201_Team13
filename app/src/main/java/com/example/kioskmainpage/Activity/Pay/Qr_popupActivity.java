package com.example.kioskmainpage.Activity.Pay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.io.File;

public class Qr_popupActivity extends AppCompatActivity implements View.OnClickListener {
    Bitmap bitmap;
    ImageView QrImage; // QR관련 변경
    String Pay_type;
    public static int height;
    public static  int width;
    private static final String TAG = "qrpopup**";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_popup);
   //     countDownReset();//카운트다운리셋
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Myapplication myapp=(Myapplication) getApplication();
        Intent paytypeData = getIntent() ;
        Pay_type  = paytypeData.getStringExtra("paytype"); //KaKao와 Zero

        QrImage = (ImageView) findViewById(R.id.QrImage);
        LinearLayout zeroLayout = (LinearLayout) findViewById(R.id.ZeropayLayout);
        Button PayCancel = (Button) findViewById(R.id.PayCancel);
        Button PayRequest = (Button) findViewById(R.id.PayRequest);
        PayCancel.setOnClickListener(this);
        PayRequest.setOnClickListener(this);
        File imgFile;
        switch (Pay_type)
        {
            case "KaKao":
                imgFile = new File(getFilesDir().getAbsolutePath()+"/"+myapp.getBizNum()+"/kakao_pay_qr.png");// 기존"/qrImage/qrImage.png"에서 "/qrImage.png"로 수정 정식서버로 넘어갈경우 kakao_pay_qr.png
                break;
            case "Zero":
                imgFile = new File(getFilesDir().getAbsolutePath()+"/"+myapp.getBizNum()+"/zero_pay_qr.png");// 기존"/qrImage/qrImage.png"에서 "/qrImage.png"로 수정 정식서버로 넘어갈경우 zero_pay_qr.png
                break;
            default:
                imgFile = new File(getFilesDir().getAbsolutePath()+"/"+myapp.getBizNum()+"/kakao_pay_qr.png");// 기존"/qrImage/qrImage.png"에서 "/qrImage.png"로 수정 정식서버로 넘어갈경우 kakao_pay_qr.png
        }
        Log.i(TAG, "onCreate: "+getFilesDir().getAbsolutePath());
        bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Log.d(TAG, "file abs path : " + imgFile.getAbsolutePath());
        QrImage.setImageBitmap(bitmap);
//        QrImage.setScaleType(ImageView.ScaleType.FIT_XY);

        //서버에서 qr이미지 받아서 띄움
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }
    public Bitmap rotateImage(Bitmap src, float degree)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(), matrix, true);

    }//이미지 회전함수

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            //주문최소 버튼 누를시에 뒤로 돌아감
            case R.id.PayCancel:
                //결제타입초기화
                Myapplication myapp=(Myapplication) getApplication();
                myapp.setCurrentPayType("NULL");
                finish();
                break;
            //결제확인 요청 버튼 누를시에 payingpopupactivity로 전환
            case R.id.PayRequest:
                Intent intent = new Intent (this, PayingPopupActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("payType",Pay_type);
                startActivity(intent);
                finish();
                break;

        }
    }
}
