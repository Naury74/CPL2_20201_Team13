package com.example.kioskmainpage.Activity.Pay;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.BackPressCloseHandler;
import com.example.kioskmainpage.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraSettings;


public class CustomScannerActivity extends AppCompatActivity implements View.OnClickListener {

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private BackPressCloseHandler backPressCloseHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        Button btncancel = (Button) findViewById(R.id.QR_scanner_btn_cancel);
        btncancel.setOnClickListener(this);

        TextView QR_scanner_textView2 = (TextView)findViewById(R.id.QR_scanner_textView2);
        QR_scanner_textView2.setText("매장결제 코드를 띄운 스마트폰을\n기기 하단 카메라에 비춰주세요 ");
        int rounded_red = getResources().getColor(R.color.rounded_red);
        Spannable span2 = (Spannable) QR_scanner_textView2.getText();
        span2.setSpan(new StyleSpan(Typeface.BOLD), 29,  34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span2.setSpan(new ForegroundColorSpan(rounded_red), 29, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//텍스트 색깔처리

        backPressCloseHandler = new BackPressCloseHandler(this);
        ViewfinderView viewfinderView = (ViewfinderView)findViewById(R.id.zxing_viewfinder_view);
        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        //전면 카메라 사용
        CameraSettings cameraSettings = new CameraSettings();
        cameraSettings.setRequestedCameraId(1);//1 : 전면카메라라
       barcodeScannerView.getBarcodeView().setCameraSettings(cameraSettings);

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
    //back버튼 두번눌렀을때 조절위해 backPressCloseHandler 로 받음
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.QR_scanner_btn_cancel:
                finish();
                break;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return false;

    }



}