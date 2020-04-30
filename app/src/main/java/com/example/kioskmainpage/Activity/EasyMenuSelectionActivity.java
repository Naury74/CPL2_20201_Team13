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
import com.example.kioskmainpage.Adapter.RecyclerAdapter_MenuType;
import com.example.kioskmainpage.R;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;
//코모란 분석기.
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

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
    //Komoran komoran=new Komoran(DEFAULT_MODEL.LIGHT);
    String []arr={"커피","음료","빵","기타"};
    public static Activity activity;
    int limit_Distance=3;

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

        if ( Build.VERSION.SDK_INT >= 23 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        voice_recordText1 = (TextView)findViewById(R.id.voice_recordText1);
        voice_btn1 = (TextView)findViewById(R.id.voice_btn1);
        what_do_you_need = (TextView)findViewById(R.id.what_do_you_need);

        voice_recordText1.setText("빵");

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
                    tts.speak("원하시는 종류를 선택해주세요",TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });
        // 코모란 유저사전 생성
       /* String filename="KOMORAN_USER_DIC.txt";
        String dataString="";
        for(int i=0;i<arr.length;i++)
        {
            dataString+=arr[i]+"\tNNP\n";
        }
        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(dataString.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

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
            voice_recordText1.setText("'커피'");
        }

        @Override
        public void onResults(Bundle results) {

            //말을하면 분석된 예제들을 rs에 저장합니다.
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            String line_temp="";
            String line="";

            //저장된 코모란 유저 사전 파일 읽기
            String path= Environment.getDataDirectory().getAbsolutePath()+"/data/com.example.kioskmainpage/files/KOMORAN_USER_DIC.txt";
            try {
                BufferedReader buf = new BufferedReader(new FileReader(path));
                while((line=buf.readLine())!=null){
                    line_temp=line_temp+line;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] lines=line_temp.replace("\tNNP","\n").split("\n");

            //rs에 담긴 문자열 중에 메뉴가있으면 선택
            String target;
            boolean isequls=false;
            for(int i=0;i<rs.length;i++) {
                for(int j=0;j<lines.length;j++)
                    if(rs[i].equals(lines[j]))
                    {
                        isequls=true;
                        target=rs[i];
                        voice_recordText1.setText(target);
                        break;
                    }
                if(isequls)break;
            }

            //rs에 담긴 문자열 중에 메뉴가 없을시 실행
            if(!isequls)
            {
                for(int i=0;i<rs.length;i++)
                {
                    for(int j=0;j<lines.length;j++)
                    {
                        char[] temp1,temp2;
                        temp1=HangleSplit(rs[i]);
                        temp2=HangleSplit(lines[j]);
                        if(levenshteinDistance(temp1,temp2)<limit_Distance)
                        {
                            voice_recordText1.setText(lines[j]);
                            isequls=true;
                            break;
                        }
                    }
                    if(isequls)break;
                }
            }

            if(!isequls)
            {
                voice_recordText1.setText("단어검색에 실패하였습니다.");
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

    //편집거리 알고리즘 부속 함수
    public int getMinimum(int val1, int val2, int val3) {
        int minNumber = val1;
        if(minNumber > val2) minNumber = val2;
        if(minNumber > val3) minNumber = val3;
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

    public char[] HangleSplit(String A){
        // 유니코드 한글 시작 : 44032, 끝 : 55199
        final int BASE_CODE = 44032;
        final int BASE_CODE_LAST= 55199;
        final int CHOSUNG = 588;
        final int JUNGSUNG = 28;

        // 초성 리스트. 00 ~ 18
        char[] CHOSUNG_LIST = {
                'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
                'ㅅ', 'ㅆ', 'ㅇ' , 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
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

        char[] charTemp = A.replaceAll(" ","").toCharArray();
        String Temp="";
        for (int i=0;i<charTemp.length;i++) {
            if(!(BASE_CODE<charTemp[i]-1 && charTemp[i]<BASE_CODE_LAST+1)) {
                Temp+=charTemp[i];
                continue;
            }
            int cBase = charTemp[i] - BASE_CODE;

            // 초성의 인덱스
            int c1 = cBase / CHOSUNG;
            // 중성의 인덱스
            int c2 = (cBase - (CHOSUNG * c1)) / JUNGSUNG;
            // 종성의 인덱스
            int c3 = (cBase - (CHOSUNG * c1) - (JUNGSUNG * c2));
            Temp=Temp+Character.toString(CHOSUNG_LIST[c1])+Character.toString(JUNGSUNG_LIST[c2])+Character.toString(JONGSUNG_LIST[c3]);
        }
        Temp.replaceAll(" ","");
        return Temp.toCharArray();
    }
}

