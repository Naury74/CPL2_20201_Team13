package com.example.kioskmainpage.Activity.Senior_MenuOption;

import android.Manifest;
import android.app.Activity;
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

import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.Locale;

public class Senior_MenuOption_SizeSelect extends AppCompatActivity {

    Intent intent;
    int menu_image;
    String menu_name;
    String menu_price;
    String menu_option = "";
    int category_num;
    int menu_count = 1;
    TextView menu_name_view;
    TextView menu_price_view;
    ImageView menu_image_view;
    private TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextView voice_recordText;
    TextView voice_btn;
    TextView announce_textView;
    TextView title_view;
    public static Activity activity;
    ImageView btn_normal_image;
    ImageView btn_large_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_menuoption_sizeselect);

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

        activity = Senior_MenuOption_SizeSelect.this;

        intent = getIntent();
        category_num = intent.getExtras().getInt("category");
        menu_image = intent.getExtras().getInt("menu_image");
        menu_name = intent.getExtras().getString("menu_name");
        menu_price = intent.getExtras().getString("menu_price");
        if(category_num == 1 || category_num == 2){
            menu_option = intent.getExtras().getString("menu_option");
        }

        btn_normal_image = (ImageView)findViewById(R.id.btn_normal_image);
        btn_large_image = (ImageView)findViewById(R.id.btn_large_image);
        menu_name_view = (TextView)findViewById(R.id.menu_name_view);
        menu_price_view = (TextView)findViewById(R.id.menu_price_view);
        menu_image_view = (ImageView) findViewById(R.id.menu_image_view);

        menu_name_view.setText(menu_name);
        menu_price_view.setText(menu_price+"원");
        menu_image_view.setImageResource(menu_image);

        title_view = (TextView)findViewById(R.id.title_view);

        Spannable span = (Spannable) title_view.getText();
        span.setSpan(new ForegroundColorSpan(getColor(R.color.senior_btn_color)), 5, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.2f), 5, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        voice_recordText = (TextView)findViewById(R.id.voice_recordText);
        voice_btn = (TextView)findViewById(R.id.voice_btn);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Senior_MenuOption_SizeSelect.this);
                mRecognizer.setRecognitionListener(listener);
                mRecognizer.startListening(intent);
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("원하시는 크기를 선택해주세요",TextToSpeech.QUEUE_FLUSH,null);
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
            voice_recordText.setBackground(getDrawable(R.drawable.round_text_bg));
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
            voice_recordText.setText(null);
            voice_recordText.setBackground(null);

            Toast.makeText(getApplicationContext(), "취소 되었어요! ",Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {

            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                voice_recordText.setBackground(getDrawable(R.drawable.round_text_bg));
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
            case R.id.btn_normal:
                if(category_num == 1){
                    menu_option = menu_option.concat("선택3: 보통");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 1 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 2){
                    menu_option = menu_option.concat("선택3: 보통");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 2 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 3){
                    menu_option = menu_option.concat("선택1: 보통");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 3 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 4){
                    menu_option = menu_option.concat("선택1: 보통");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 4 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.btn_large:
                if(category_num == 1){
                    menu_option = menu_option.concat("선택3: 크게");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 1 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 2){
                    menu_option = menu_option.concat("선택3: 크게");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 2 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 3){
                    menu_option = menu_option.concat("선택1: 크게");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 3 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
                else if(category_num == 4){
                    menu_option = menu_option.concat("선택1: 크게");
                    Intent intent = new Intent(this, Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("init_type", "add");
                    intent.putExtra("category",category_num);
                    intent.putExtra("menu_image",menu_image);
                    intent.putExtra("menu_name",menu_name);
                    intent.putExtra("menu_price",menu_price);
                    intent.putExtra("menu_option",menu_option);
                    intent.putExtra("menu_count",menu_count);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(this, "Option Selected : "+menu_option+"\n Category 4 is final OptionSelected. ",Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.back_btn:
                finish();
        }
    }
    public void onBackPressed() {
        return;
    }
}
