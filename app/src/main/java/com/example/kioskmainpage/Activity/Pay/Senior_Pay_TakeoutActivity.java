package com.example.kioskmainpage.Activity.Pay;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_MenuOption_SizeSelect;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.Locale;

public class Senior_Pay_TakeoutActivity extends AppCompatActivity {

    Intent intent;
    private TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextView voice_recordText;
    TextView voice_btn;
    TextView announce_textView;
    private TextView title;
    String select_result;
    int total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior__pay__takeout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        intent = getIntent();
        total_price = intent.getExtras().getInt("total_price");

        title = (TextView)findViewById(R.id.title_view);
        Spannable span = (Spannable) title.getText();
        span.setSpan(new ForegroundColorSpan(getColor(R.color.white_blue)), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(getColor(R.color.light_green)), 10, 12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.5f), 0, 2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.5f), 10, 12, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        voice_recordText = (TextView)findViewById(R.id.voice_recordText);
        voice_btn = (TextView)findViewById(R.id.voice_btn);
        announce_textView = (TextView)findViewById(R.id.announce_textView);

        voice_recordText.setText("'먹고 갈게'\n'포장 해줘'");

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Senior_Pay_TakeoutActivity.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("매장에서 드실지, 포장해서 가실지 선택해 주세요",TextToSpeech.QUEUE_FLUSH,null);
                    tts.setSpeechRate((float) 0.4);

                }
            }
        });

    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            //Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
            voice_recordText.setText("듣고 있어요...");
        }

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 불안정";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
                default:
                    message = "알 수 없는 오류";
                    voice_recordText.setText("'취소 되었어요!'");
                    break;
            }
            voice_recordText.setText("'먹고 갈게'\n'포장 해줘'");

            Toast.makeText(getApplicationContext(), "취소 되었어요! ",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {

            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                voice_recordText.setText("'" + matches.get(i) + "'");
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_here:
                select_result = "매장";
                /*Intent intent = new Intent(this, Senior_Pay_SelectPaymentMethod_Activity.class);
                intent.putExtra("takeout",select_result);
                intent.putExtra("total_price",total_price);
                startActivity(intent);
                finish();*/
            case R.id.btn_takeout:
                select_result = "포장";
                /*Intent intent2 = new Intent(this, Senior_Pay_SelectPaymentMethod_Activity.class);
                intent2.putExtra("takeout",select_result);
                intent2.putExtra("total_price",total_price);
                startActivity(intent2);
                finish();*/
        }
    }
}
