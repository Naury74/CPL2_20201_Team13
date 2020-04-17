package com.example.kioskmainpage.Activity;


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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.Adapter.RecyclerAdapter_MenuType;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.Locale;

public class EasyMenuSelectionActivity extends AppCompatActivity {

    Intent intent;
    private TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    TextView voice_btn1;
    TextView voice_recordText1;
    TextView what_do_you_need;
    final int PERMISSION = 1;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter_MenuType recyclerAdapterMenuType;
    String []arr={"커피","음료","빵","기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_menu_selection);

        //상,하단바 제거
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

        recyclerView = findViewById(R.id.menu_type_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapterMenuType = new RecyclerAdapter_MenuType(arr);


        recyclerView.setAdapter(recyclerAdapterMenuType);
        recyclerView.setHasFixedSize(true);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        voice_recordText1 = (TextView)findViewById(R.id.voice_recordText1);
        voice_btn1 = (TextView)findViewById(R.id.voice_btn1);
        what_do_you_need = (TextView)findViewById(R.id.what_do_you_need);

        voice_recordText1.setText("'커피'");

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        voice_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(EasyMenuSelectionActivity.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("저를 눌러 아래처럼 말씀해주세요",TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });

    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            voice_recordText1.setText("듣고 있어요...");
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
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
                default:
                    message = "알 수 없는 오류임";
                    voice_recordText1.setText("'취소 되었어요!'");
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                voice_recordText1.setText("'"+matches.get(i)+"'");
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };
    public void TTS_Play(View v){
        tts.speak(what_do_you_need.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
    public void onBackPressed() {
        return;
    }

}

