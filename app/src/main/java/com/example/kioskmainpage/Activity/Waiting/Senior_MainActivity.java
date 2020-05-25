package com.example.kioskmainpage.Activity.Waiting;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_MenuSelected_Check;
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_OrderListActivity;
import com.example.kioskmainpage.Adapter.Senior_MainTab_Adapter;
import com.example.kioskmainpage.Fragment.Senior_Tab_Fragment;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.Locale;

public class Senior_MainActivity extends AppCompatActivity {

    Intent intent;
    private TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextView voice_recordText;
    TextView voice_btn;
    TextView announce_textView;
    int position_category = 0;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayout.Tab tab;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상하단바 제거
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
        setContentView(R.layout.activity_senior__main);

        activity = Senior_MainActivity.this;

        intent = getIntent();


            position_category = intent.getExtras().getInt("position");
            Log.i("position_category", "\nposition_category: " + position_category + "\n");

            if (Build.VERSION.SDK_INT >= 23) {
                // 퍼미션 체크
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO}, PERMISSION);
            }

            voice_recordText = (TextView) findViewById(R.id.voice_recordText);
            voice_btn = (TextView) findViewById(R.id.voice_btn);

            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            voice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(Senior_MainActivity.this);
                    mRecognizer.setRecognitionListener(listener);
                    mRecognizer.startListening(intent);
                }
            });

            Senior_MainTab_Adapter senior_mainTab_adapter = new Senior_MainTab_Adapter(getSupportFragmentManager(), this);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            viewPager.setAdapter(senior_mainTab_adapter);

            tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);

            tab = tabLayout.getTabAt(position_category);
            tab.select();

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        intent=getIntent();
        int is_call = intent.getExtras().getInt("is_call");
        if(is_call==1) {

            int menus_number = intent.getExtras().getInt("menus_number");
            int menu_count=intent.getExtras().getInt("Option_count");
            String menus_name="아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";
            int[] menus_prise={2500,3500,4000,2800,4300,5300,4400,5500,6300,5600,4900,5800,6100,5600,5600,5700,5700, 4800,5500,4500,6800,5900,5200,6500,5500,6500,6100,5800,5800,8500,5800,8000};
            int[] menus_img_number={R.drawable.img1_1,R.drawable.img2_1,R.drawable.img3_1,R.drawable.img4_1,R.drawable.img5_1,R.drawable.img6_1,R.drawable.img7_1,R.drawable.img8_1,R.drawable.img1_2,R.drawable.img2_2,R.drawable.img3_2,R.drawable.img4_2,R.drawable.img5_2,R.drawable.img6_2,R.drawable.img7_2,R.drawable.img8_2,R.drawable.img1_3,R.drawable.img9_2,R.drawable.img2_3,R.drawable.img3_3,R.drawable.img4_3,R.drawable.img5_3,R.drawable.img6_3,R.drawable.img7_3,R.drawable.img8_3,R.drawable.img9_3,R.drawable.img1_4,R.drawable.img2_4,R.drawable.img3_4,R.drawable.img4_4,R.drawable.img5_4,R.drawable.img6_4};
            String[] menus_names=menus_name.split("\\|");
            int[] menus_len={8,9,9,6};
            for(int i=0;i<position_category;i++)
                menus_number+=menus_len[i];
            String Option;
            String temp=intent.getExtras().getString("Option_adverb","");
            Option=temp;
            temp=intent.getExtras().getString("Option_size","");
            Option+=temp;
            if(position_category<2) {
                intent = new Intent(getApplicationContext(), Senior_OrderListActivity.class);
                intent.putExtra("category", position_category+1);
                intent.putExtra("menu_image", menus_img_number[menus_number]);
                intent.putExtra("menu_name",menus_names[menus_number]);
                intent.putExtra("menu_price", Integer.toString(menus_prise[menus_number]));
                intent.putExtra("is_call", 1);
                intent.putExtra("menu_count",menu_count);
                intent.putExtra("menu_option",Option);
                startActivity(intent);
            }
        }



        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("원하시는 메뉴를 선택해 주세요",TextToSpeech.QUEUE_FLUSH,null);
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

    public void TTS_Play(View v){
        tts.speak(announce_textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
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
