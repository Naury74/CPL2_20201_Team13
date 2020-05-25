package com.example.kioskmainpage.Activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
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
import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_MenuSelected_Check;
import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;
import com.example.kioskmainpage.Adapter.RecyclerAdapter_MenuType;
import com.example.kioskmainpage.R;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
//코모란 분석기.
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import android.util.Log;

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
    Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);
    String[] arr = {"커피", "음료", "빵", "기타"};
    public static Activity activity;
    int limit_Distance = 3;

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

        activity = EasyMenuSelectionActivity.this;

        recyclerView = findViewById(R.id.menu_type_recyclerview);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapterMenuType = new RecyclerAdapter_MenuType(arr);


        recyclerView.setAdapter(recyclerAdapterMenuType);
        recyclerView.setHasFixedSize(true);

        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        voice_recordText1 = (TextView) findViewById(R.id.voice_recordText);
        voice_btn1 = (TextView) findViewById(R.id.voice_btn);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
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
                if (status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("원하시는 종류를 선택해주세요", TextToSpeech.QUEUE_FLUSH, null);

                }
            }
        });
        // 코모란 유저사전 생성
        String filename = "KOMORAN_USER_DIC.txt";
        String dataString = "";
        for (int i = 0; i < arr.length; i++) {
            dataString += arr[i] + "\tNNP\n";
        }
        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(dataString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            voice_recordText1.setText("듣고 있어요...");
            voice_recordText1.setBackground(getDrawable(R.drawable.round_text_bg));
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

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
            voice_recordText1.setText(null);
            voice_recordText1.setBackground(null);
        }

        @Override
        public void onResults(Bundle results) {

            //말을하면 분석된 예제들을 rs에 저장합니다.
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            String line_temp = "";
            String line = "";

            //저장된 코모란 유저 사전 파일 읽기
            String path = Environment.getDataDirectory().getAbsolutePath() + "/data/com.example.kioskmainpage/files/KOMORAN_USER_DIC.txt";
            try {
                BufferedReader buf = new BufferedReader(new FileReader(path));
                while ((line = buf.readLine()) != null) {
                    line_temp = line_temp + line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] lines = line_temp.replace("\tNNP", "\n").split("\n");
            String[] KOMORAN_adverb = {"시원", "차갑", "따뜻", "뜨뜻", "뜨겁"};
            String[] hangleNumber1 = {"영", "한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉", "빵", "|", "|", "석", "넉", "|", "|", "|", "|", "|", "공", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
            String[] hangleNumber2 = {"열", "스물", "서른", "마흔", "쉰", "예순", "일흔", "여든", "아흔"};
            String[] hangleNumber3 = {"십", "백", "천"};
            String[] KOMORAN_size = {"크", "작", "조그마하"};
            String speak_menu = "";
            String speak_adverb = "";
            int speak_count = 0;
            String speak_thick = "";
            String speak_size = "";
            int result_serch = 3;
            boolean is1guri = false;

            //테스트문장

            KomoranResult AA = komoran.analyze(rs[0]);


            ///data/data/com.example.kioskmainpage/files/5901020009/main_menu_3
            //예제 meues 추후 변경예정
            String testString = "아메리카노|카라멜 마끼야또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스트레토 비안코";
            String[] menus;
            menus = testString.replace(" ", "").split("\\|");
            String[] C_split = rs[0].split(" ");

            //rs에 담긴 문자열 중에 메뉴가있으면 선택
            String target;
            boolean isequls = false;
            for (int i = 0; i < rs.length; i++) {
                for (int j = 0; j < lines.length; j++)
                    if (rs[i].equals(lines[j])) {
                        isequls = true;
                        target = rs[i];
                        voice_recordText1.setText(target);
                        voice_recordText1.setBackground(getDrawable(R.drawable.round_text_bg));
                        break;
                    }
                if (isequls) break;
            }

            //rs에 담긴 문자열 중에 메뉴가 없을시 실행
            if (!isequls) {
                for (int i = 0; i < rs.length; i++) {
                    for (int j = 0; j < lines.length; j++) {
                        char[] temp1, temp2;
                        temp1 = HangleSplit(rs[i]);
                        temp2 = HangleSplit(lines[j]);
                        if (levenshteinDistance(temp1, temp2) < limit_Distance) {
                            voice_recordText1.setText(lines[j]);
                            voice_recordText1.setBackground(getDrawable(R.drawable.round_text_bg));
                            isequls = true;
                            break;
                        }
                    }
                    if (isequls) break;
                }
            }

            //단어가 arr에 담긴 문장이 한단어가 아닐 경우
            if (!isequls) {
                boolean isinclude_menus = false;
                for (int i = 0; i < menus.length; i++) {
                    //메뉴가 있다면 넘기기
                    if (rs[0].contains(menus[i])) {
                        speak_menu = menus[i];
                        isinclude_menus = true;
                        break;
                    }
                }
                for (int i = 0; i < C_split.length; i++) {
                    for (int j = 0; j < menus.length; j++)
                        if (C_split[i].equals(menus[j])) {
                            isinclude_menus = true;
                            speak_menu = menus[j];
                            break;
                        }
                    if (isinclude_menus) break;
                }


                //음성인식에 메뉴가 없다면 문자 보정을 시작하다.
                if (isinclude_menus == false) {
                    String[] C_list = rs[0].split(" ");
                    for (int i = 0; i < C_list.length; i++) {
                        String temp = "";
                        for (int j = i; j < C_list.length; j++) {
                            temp = temp + C_list[j];
                            char[] chars1 = temp.toCharArray();
                            for (int k = 0; k < menus.length; k++) {
                                char[] chars2 = menus[k].toCharArray();
                                if (levenshteinDistance(chars1, chars2) < result_serch) {
                                    speak_menu = menus[k];
                                    is1guri = true;
                                    break;
                                }
                            }
                            if (is1guri == true) break;
                        }
                        if (is1guri == true) break;
                    }
                }


                //문장에서 메뉴를 끝까지 못찾았을때 출력
                if (is1guri == false) {
                    voice_recordText1.setText("메뉴를 못찾았습니다.");
                    voice_recordText1.setBackground(getDrawable(R.drawable.round_text_bg));
                }

                //문장에서 메뉴를 찾았으면 문장 분석을 시작합니다.

                else {
                    List<String> tokenList_1 = AA.getMorphesByTags("VA", "XR", "NA");
                    //온도선택
                    for (String temp_tokenList : tokenList_1) {
                        for (String temp_KOMORAN_adverb : KOMORAN_adverb) {
                            if (levenshteinDistance(HangleSplit(temp_tokenList), HangleSplit(temp_KOMORAN_adverb)) < result_serch) {
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

                    int temp_1 = 1;
                    String temp_2;
                    String temp_3;

                    intent = new Intent(getApplicationContext(), Senior_MainActivity.class);

                    //카테고리 선택
                    int is_call=1;
                    intent.putExtra("is_call",is_call);
                    intent.putExtra("menus_number",index);
                    intent.putExtra("category", temp_1);

                    //온도
                    if (speak_adverb.equals("")) {
                        temp_2 = "";
                    } else if (Arrays.asList(KOMORAN_adverb).indexOf(speak_adverb) < 2) {
                        temp_2 = "시원하게  ";
                    } else {
                        temp_2 = "따뜻하게  ";
                    }
                    intent.putExtra("Option_adverb", temp_2);

                    //개수
                    intent.putExtra("Option_count", speak_count);

                    //사이즈
                    if (speak_size.equals("")) {
                        temp_3 = "";
                    } else if (Arrays.asList(KOMORAN_size).indexOf(speak_adverb) < 1) {
                        temp_3 = "크게    ";
                    } else {
                        temp_3 = "작게    ";
                    }
                    intent.putExtra("Option_size", temp_3);
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

    public void TTS_Play(View v) {
        tts.speak("원하시는 종류를 선택해주세요", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) {
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
}

