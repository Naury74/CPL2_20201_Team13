package com.example.kioskmainpage.Activity.Waiting;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.Barrier;
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
import com.example.kioskmainpage.Activity.SplashActivity;
import com.example.kioskmainpage.Adapter.Senior_MainTab_Adapter;
import com.example.kioskmainpage.Fragment.Senior_Tab_Fragment;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.NaturalLanguageProcessing;
import com.example.kioskmainpage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

public class Senior_MainActivity extends AppCompatActivity {

    Intent intent,intent2;
    private TextToSpeech tts;
    private ConstraintLayout background_record_text;
    private FloatingActionButton voice_btn;
    SpeechRecognizer mRecognizer;
    final int PERMISSION = 1;
    TextView voice_recordText;
    TextView announce_textView;
    int position_category = 0;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabLayout.Tab tab;
    public static Activity activity;
    int cccccc=0;
    int is_call;
    Myapplication myapplication;
    int[] menus_len;
    String menus_name;
    NaturalLanguageProcessing naturalLanguageProcessing;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
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

        myapplication=(Myapplication)getApplication();
        activity = Senior_MainActivity.this;
        menus_len =new int[]{8, 9, 9, 6};
        menus_name = "아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";

        intent = getIntent();
        position_category = intent.getExtras().getInt("position");
        Log.i("position_category", "\nposition_category: " + position_category + "\n");
        Log.d("position_category", "main_ooooooo ");

            if (Build.VERSION.SDK_INT >= 23) {
                // 퍼미션 체크
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO}, PERMISSION);
            }

            voice_recordText = (TextView) findViewById(R.id.voice_recordText);
            FloatingActionButton voice_btn = (FloatingActionButton) findViewById(R.id.voice_btn);
            background_record_text=(ConstraintLayout) findViewById(R.id.background_record_text);

            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            voice_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( background_record_text.getVisibility()==View.VISIBLE)
                        background_record_text.setVisibility(View.GONE);
                    else {
                        background_record_text.setVisibility(View.VISIBLE);
                        mRecognizer = SpeechRecognizer.createSpeechRecognizer(Senior_MainActivity.this);
                        mRecognizer.setRecognitionListener(listener);
                        mRecognizer.startListening(intent);

                        voice_recordText.setText("말하신 음료를 못찾았어요");
                    }
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
       /*
       is_call = intent.getExtras().getInt("is_call");
        if(is_call==1) {
            int menus_number = intent.getExtras().getInt("menus_number", -1);
            int menu_count = intent.getExtras().getInt("Option_count");
            int[] menus_prise = {2500, 3500, 4000, 2800, 4300, 5300, 4400, 5500, 6300, 5600, 4900, 5800, 6100, 5600, 5600, 5700, 5700, 4800, 5500, 4500, 6800, 5900, 5200, 6500, 5500, 6500, 6100, 5800, 5800, 8500, 5800, 8000};
            int[] menus_img_number = {R.drawable.img1_1, R.drawable.img2_1, R.drawable.img3_1, R.drawable.img4_1, R.drawable.img5_1, R.drawable.img6_1, R.drawable.img7_1, R.drawable.img8_1, R.drawable.img1_2, R.drawable.img2_2, R.drawable.img3_2, R.drawable.img4_2, R.drawable.img5_2, R.drawable.img6_2, R.drawable.img7_2, R.drawable.img8_2, R.drawable.img1_3, R.drawable.img9_2, R.drawable.img2_3, R.drawable.img3_3, R.drawable.img4_3, R.drawable.img5_3, R.drawable.img6_3, R.drawable.img7_3, R.drawable.img8_3, R.drawable.img9_3, R.drawable.img1_4, R.drawable.img2_4, R.drawable.img3_4, R.drawable.img4_4, R.drawable.img5_4, R.drawable.img6_4};
            String[] menus_names = menus_name.split("\\|");
            for (int i = 0; i < position_category; i++)
                menus_number += menus_len[i];
            String Option;
            String temp = intent.getExtras().getString("Option_adverb", "");
            Option = temp;
            temp = intent.getExtras().getString("Option_size", "");
            Option += temp;
            String temp_string = Integer.toString(menus_prise[menus_number]);
            intent = new Intent(getApplicationContext(), Senior_OrderListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("category", position_category + 1);
            intent.putExtra("menu_image", menus_img_number[menus_number]);
            intent.putExtra("menu_name", menus_names[menus_number]);
            intent.putExtra("menu_price", temp_string);
            intent.putExtra("is_call", is_call);
            intent.putExtra("menu_count", menu_count);
            Log.d("LOG","main_up_menu_count"+menu_count);
            intent.putExtra("menu_option", Option);
            startActivity(intent);
            finish();
        }
*/

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    if(is_call!=1){
                        tts.speak("원하시는 메뉴를 선택해 주세요",TextToSpeech.QUEUE_FLUSH,null);
                    }
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
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            BackgroundService task=new BackgroundService();
            task.getString(rs);
            task.execute();

            //말을하면 분석된 예제들을 rs에 저장합니다.
          /*  if(cccccc==0){
                String key = "";
                key = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList<String> mResult = results.getStringArrayList(key);

                String[] rs = new String[mResult.size()];
                mResult.toArray(rs);

                Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
                String[] KOMORAN_adverb = {"시원", "차갑", "따뜻", "뜨뜻", "뜨겁"};
                String[] hangleNumber1 = {"영", "한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉", "빵", "|", "|", "석", "넉", "|", "|", "|", "|", "|", "공", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
                String[] hangleNumber2 = {"열", "스물", "서른", "마흔", "쉰", "예순", "일흔", "여든", "아흔"};
                String[] hangleNumber3 = {"십", "백", "천"};
                String[] KOMORAN_size = {"크", "작", "조그마하"};
                String[] KOMORAN_thick = {"달달하게","보통","진하게","더 진하게"};
                String speak_menu = "";
                String speak_adverb = "";
                int speak_count = 0;
                String speak_thick = "";
                String speak_size = "";
                int result_serch = 2;
                int rs_index = 0;
                String menus_name = "아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";
                String[] menus;
                int[] menus_len = {8, 9, 9, 6};
                menus = menus_name.replace(" ", "").split("\\|");
                String[] C_split;

                //문장에서 단어찾기
                boolean isinclude_menus = false;
                for (int k = 0; k < rs.length; k++) {
                    C_split = rs[k].split(" ");
                    for (int i = 0; i < C_split.length; i++) {
                        for (int j = 0; j < menus.length; j++)
                            if (C_split[i].equals(menus[j])) {
                                isinclude_menus = true;
                                speak_menu = menus[j];
                                rs_index = k;
                                break;
                            }
                        if (isinclude_menus) break;
                    }
                }

                //음성인식에 메뉴가 없다면 문자 보정을 시작하다.
                if (isinclude_menus == false) {
                    for (int ii = 0; ii < rs.length; ii++) {
                        String[] C_list = rs[ii].split(" ");
                        for (int i = 0; i < C_list.length; i++) {
                            String temp = "";
                            for (int j = i; j < C_list.length; j++) {
                                temp = temp + C_list[j];
                                char[] chars1 = temp.toCharArray();
                                for (int k = 0; k < menus.length; k++) {
                                    char[] chars2 = menus[k].toCharArray();
                                    if (levenshteinDistance(chars1, chars2) < result_serch) {
                                        speak_menu = menus[k];
                                        isinclude_menus = true;
                                        rs_index = ii;
                                        break;
                                    }
                                }
                                if (isinclude_menus == true) break;
                            }
                            if (isinclude_menus == true) break;
                        }
                        if (isinclude_menus == true) break;
                    }
                }



                KomoranResult AA = komoran.analyze(rs[rs_index]);

                //문장에서 메뉴를 끝까지 못찾았을때 출력
                if (isinclude_menus == false) {
                    voice_recordText.setText("메뉴를 못찾았습니다.");
                    voice_recordText.setBackground(getDrawable(R.drawable.round_text_bg));
                }
                //문장에서 메뉴를 찾았으면 문장 분석을 시작합니다.
                else {
                    List<String> tokenList_1 = AA.getMorphesByTags("VA", "XR", "NA");
                    //온도선택
                    for (String temp_tokenList : tokenList_1) {
                        for (String temp_KOMORAN_adverb : KOMORAN_adverb) {
                            if (levenshteinDistance(HangleSplit(temp_tokenList),HangleSplit(temp_KOMORAN_adverb)) < result_serch) {
                                speak_adverb = temp_KOMORAN_adverb;
                            }
                        }
                    }
                    //사이즈 선택
                    for (String temp_tokenList : tokenList_1) {
                        for (String temp_KOMORAN_size : KOMORAN_size) {
                            if (levenshteinDistance(HangleSplit(temp_tokenList), HangleSplit(temp_KOMORAN_size)) < 2) {
                                speak_size = temp_KOMORAN_size;
                            }
                        }
                    }
                    //진하기 선택
                    for (String temp_tokenList : tokenList_1) {
                        for (String temp_KOMORAN_thick : KOMORAN_thick) {
                            if (levenshteinDistance(HangleSplit(temp_tokenList), HangleSplit(temp_KOMORAN_thick)) < 2) {
                                speak_thick = temp_KOMORAN_thick;
                            }
                        }
                    }

                    //개수 선택
                    List<String> tokenList_2 = AA.getMorphesByTags("SN", "NR");
                    for (String temp_tokenList : tokenList_2) {
                        try {
                            int temp = Integer.parseInt(temp_tokenList);
                            speak_count += temp;
                            continue;
                        } catch (Exception e) {

                        }
                        boolean is_counting = false;
                        for (int i = 0; i < hangleNumber2.length; i++) {
                            if (temp_tokenList.indexOf(hangleNumber2[i]) != -1) {
                                speak_count += (i + 1) * 10;
                                for (int j = 0; j < hangleNumber1.length; j++) {
                                    if (temp_tokenList.indexOf(hangleNumber1[j]) != -1) {
                                        speak_count += j % 10;
                                        is_counting = true;
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        if (!is_counting) {
                            for (int i = 0; i < hangleNumber3.length; i++) {
                                if (temp_tokenList.indexOf(hangleNumber3[i]) != -1) {
                                    int temp_speak_count = (int) Math.pow(10, i + 1);
                                    if (temp_tokenList.indexOf(hangleNumber3[i]) == 0) {
                                        speak_count += temp_speak_count;
                                    } else {
                                        int index = temp_tokenList.indexOf(hangleNumber3[i]);
                                        char[] temp_temp_tokenList = temp_tokenList.toCharArray();
                                        for (int j = 0; j < hangleNumber1.length; j++)
                                            if (Character.toString(temp_temp_tokenList[index - 1]).equals(hangleNumber1[j])) {
                                                speak_count += temp_speak_count * (j % 10);
                                            }
                                    }
                                }
                            }

                            for (int j = 0; j < hangleNumber1.length; j++) {
                                if (temp_tokenList.substring(temp_tokenList.length() - 1).indexOf(hangleNumber1[j]) != -1) {
                                    speak_count += j % 10;
                                    break;
                                }
                            }
                        }
                    }

                    int index = Arrays.asList(menus).indexOf(speak_menu);
                    int inttemp = 0;
                    int category = 0;
                    for (int i = 0; i < menus_len.length; i++) {
                        inttemp += menus_len[i];
                        if (rs_index < inttemp)
                            category = i;
                    }
                    String adverb;
                    String size;

                    //카테고리 선택
                    int is_call = 1;
                    intent.putExtra("is_call", is_call);
                    //온도
                    if (speak_adverb.equals("")) {
                        adverb = "";
                    } else if (Arrays.asList(KOMORAN_adverb).indexOf(speak_adverb) < 2) {
                        adverb = "시원하게  ";
                    } else {
                        adverb = "따뜻하게  ";
                    }

                    //개수

                    //사이즈
                    if (speak_size.equals("")) {
                        size = "";
                    } else if (Arrays.asList(KOMORAN_size).indexOf(speak_adverb) < 1) {
                        size = "크게    ";
                    } else {
                        size = "작게    ";
                    }

                    //진하기
                    speak_thick += "\t";


                    String menus_name_ = "아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";
                    int[] menus_prise = {2500, 3500, 4000, 2800, 4300, 5300, 4400, 5500, 6300, 5600, 4900, 5800, 6100, 5600, 5600, 5700, 5700, 4800, 5500, 4500, 6800, 5900, 5200, 6500, 5500, 6500, 6100, 5800, 5800, 8500, 5800, 8000};
                    int[] menus_img_number = {R.drawable.img1_1, R.drawable.img2_1, R.drawable.img3_1, R.drawable.img4_1, R.drawable.img5_1, R.drawable.img6_1, R.drawable.img7_1, R.drawable.img8_1, R.drawable.img1_2, R.drawable.img2_2, R.drawable.img3_2, R.drawable.img4_2, R.drawable.img5_2, R.drawable.img6_2, R.drawable.img7_2, R.drawable.img8_2, R.drawable.img1_3, R.drawable.img9_2, R.drawable.img2_3, R.drawable.img3_3, R.drawable.img4_3, R.drawable.img5_3, R.drawable.img6_3, R.drawable.img7_3, R.drawable.img8_3, R.drawable.img9_3, R.drawable.img1_4, R.drawable.img2_4, R.drawable.img3_4, R.drawable.img4_4, R.drawable.img5_4, R.drawable.img6_4};
                    String[] menus_names = menus_name_.split("\\|");
                    int menus_number=index;

                    String temp =adverb+size+speak_thick;
                    String temp_string = Integer.toString(menus_prise[menus_number]);
                    intent = new Intent(getApplicationContext(), Senior_OrderListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("category", category + 1);
                    intent.putExtra("menu_image", menus_img_number[menus_number]);
                    intent.putExtra("menu_name", menus_names[menus_number]);
                    intent.putExtra("menu_price", temp_string);
                    intent.putExtra("is_call", is_call);
                    intent.putExtra("menu_count", speak_count);
                    Log.d("LOG","main_menu_count"+speak_count);
                    intent.putExtra("menu_option", temp);
                    cccccc++;
                    startActivity(intent);
                    finish();
                }
            }*/

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
    //편집거리 알고리즘 부속 Min 함수
    public int getMinimum(int val1, int val2, int val3) {
        int minNumber = val1;
        if (minNumber > val2) minNumber = val2;
        if (minNumber > val3) minNumber = val3;
        return minNumber;
    }

    //편집거리 알고리즘
    public int levenshteinDistance(char[] s, char[] t) {
        int m = s.length;
        int n = t.length;

        int[][] d = new int[m + 1][n + 1];

        for (int i = 1; i < m; i++) {
            d[i][0] = i;
        }

        for (int j = 1; j < n; j++) {
            d[0][j] = j;
        }

        for (int j = 1; j < n; j++) {
            for (int i = 1; i < m; i++) {
                if (s[i] == t[j]) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    d[i][j] = getMinimum(d[i - 1][j], d[i][j - 1], d[i - 1][j - 1]) + 1;
                }
            }
        }
        return d[m - 1][n - 1];
    }

    public char[] HangleSplit(String A) {
        // 유니코드 한글 시작 : 44032, 끝 : 55199
        final int BASE_CODE = 44032;
        final int BASE_CODE_LAST = 55199;
        final int CHOSUNG = 588;
        final int JUNGSUNG = 28;

        // 초성 리스트. 00 ~ 18
        char[] CHOSUNG_LIST = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
                'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };

        // 중성 리스트. 00 ~ 20
        char[] JUNGSUNG_LIST = {
                'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ',
                'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ',
                'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ',
                'ㅡ', 'ㅢ', 'ㅣ'
        };

        // 종성 리스트. 00 ~ 27 + 1(1개 없음)
        char[] JONGSUNG_LIST = {
                ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ',
                'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ',
                'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ',
                'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        };

        char[] charTemp = A.replaceAll(" ", "").toCharArray();
        String Temp = "";
        for (int i = 0; i < charTemp.length; i++) {
            if (!(BASE_CODE < charTemp[i] - 1 && charTemp[i] < BASE_CODE_LAST + 1)) {
                Temp += charTemp[i];
                continue;
            }
            int cBase = charTemp[i] - BASE_CODE;

            // 초성의 인덱스
            int c1 = cBase / CHOSUNG;
            // 중성의 인덱스
            int c2 = (cBase - (CHOSUNG * c1)) / JUNGSUNG;
            // 종성의 인덱스
            int c3 = (cBase - (CHOSUNG * c1) - (JUNGSUNG * c2));
            Temp = Temp + Character.toString(CHOSUNG_LIST[c1]) + Character.toString(JUNGSUNG_LIST[c2]) + Character.toString(JONGSUNG_LIST[c3]);
        }
        Temp.replaceAll(" ", "");
        return Temp.toCharArray();
    }
    class BackgroundService extends AsyncTask<String, Void, Void> {
        ProgressDialog logindialog = new ProgressDialog(Senior_MainActivity.this);
        String[] data;
        JSONObject object;
        public void getString(String[] data){
            this.data=data;
        }
        @Override
        protected void onPreExecute() {
            //백그라운드 작업하는동안 보여줄 동작
            logindialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            logindialog.setMessage("언어를 해석중입니다.");
            logindialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Void result) {
            //백그라운드 작업 완료됐을때 호출
            logindialog.dismiss();


            try {
                boolean temp=(Boolean) object.get("is_intent_go");
                if(temp){

                    String menus_name_ = "아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";
                    int[] menus_prise = {2500, 3500, 4000, 2800, 4300, 5300, 4400, 5500, 6300, 5600, 4900, 5800, 6100, 5600, 5600, 5700, 5700, 4800, 5500, 4500, 6800, 5900, 5200, 6500, 5500, 6500, 6100, 5800, 5800, 8500, 5800, 8000};
                    int[] menus_img_number = {R.drawable.img1_1, R.drawable.img2_1, R.drawable.img3_1, R.drawable.img4_1, R.drawable.img5_1, R.drawable.img6_1, R.drawable.img7_1, R.drawable.img8_1, R.drawable.img1_2, R.drawable.img2_2, R.drawable.img3_2, R.drawable.img4_2, R.drawable.img5_2, R.drawable.img6_2, R.drawable.img7_2, R.drawable.img8_2, R.drawable.img1_3, R.drawable.img9_2, R.drawable.img2_3, R.drawable.img3_3, R.drawable.img4_3, R.drawable.img5_3, R.drawable.img6_3, R.drawable.img7_3, R.drawable.img8_3, R.drawable.img9_3, R.drawable.img1_4, R.drawable.img2_4, R.drawable.img3_4, R.drawable.img4_4, R.drawable.img5_4, R.drawable.img6_4};
                    String[] menus_names = menus_name_.split("\\|");
                    int[] menus_len = {8, 9, 9, 6};
                    int menus_num=object.getInt("menu_number");
                    int sum=0;
                    for(int i=0;i<menus_len.length;i++){
                        sum=menus_len[i];
                        if(menus_num<sum){
                            sum=i;
                            break;
                        }
                    }
                    intent2 = new Intent(context, Senior_OrderListActivity.class);
                    intent2.putExtra("menu_count",object.getInt("count"));
                    String stringTemp=(String)object.get("size")+(String)object.get("adverb")+(String)object.get("speak_thick");
                    intent2.putExtra("menu_option",stringTemp);
                    int menus_number=object.getInt("menu_number");

                    intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent2.putExtra("category", sum + 1);
                    intent2.putExtra("menu_image", menus_img_number[menus_number]);
                    intent2.putExtra("menu_name", menus_names[menus_number]);
                    intent2.putExtra("menu_price", Integer.toString(menus_prise[menus_number]));
                    intent2.putExtra("is_call",1);
                    startActivity(intent2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
        @Override
        protected Void doInBackground(String... strings) {
            String KOMORAN_data=menus_name.replace("|","\tNNP\n");
            KOMORAN_data=KOMORAN_data+"\tNNP\n";
            naturalLanguageProcessing=myapplication.getNaturalLanguageProcessing();
            naturalLanguageProcessing.setNaturalLanguageProcessing(menus_name.split("\\|"),context,1,R.id.background_record_text);
            String[] KOMORAN_adverb = {"시원", "차갑", "따뜻", "뜨뜻", "뜨겁"};
            String[] KOMORAN_size = {"크", "작", "조그마하"};
            String[] KOMORAN_thick = {"달달하게","기본","진하게","더 진하게"};

            for(int i=0;i<KOMORAN_adverb.length;i++) {
                KOMORAN_data=KOMORAN_data+KOMORAN_adverb[i]+"\tVA\n";
            }
            for(int i=0;i<KOMORAN_size.length;i++) {
                KOMORAN_data=KOMORAN_data+KOMORAN_size[i]+"\tVA\n";
            }
            for(int i=0;i<KOMORAN_thick.length;i++) {
                KOMORAN_data=KOMORAN_data+KOMORAN_thick[i]+"\tVA\n";
            }
            naturalLanguageProcessing.setKomoranUserDic(KOMORAN_data);

            try {
                naturalLanguageProcessing.startSpeechRecognizer(data);
                object= naturalLanguageProcessing.getJsonObject();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

