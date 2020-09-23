package com.example.kioskmainpage;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;


import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;

public class NaturalLanguageProcessing {
    final String TAG="Kiosk";
    Context context;
    Komoran komoran=new Komoran(DEFAULT_MODEL.LIGHT);
    String[] menus_name;
    String[] returnString;
    SpeechRecognizer speechRecognizer;
    JSONObject jsonObject=new JSONObject();
    int activityNum;
    int textViewId;
    String[] rs;
    NaturalLanguageProcessing()
    {

    }
    public void setNaturalLanguageProcessing(String[] menus_name, Context context, int activityNum, int textViewId) {
        this.menus_name=menus_name;
        this.context=context;
        this.activityNum=activityNum;
        this.textViewId=textViewId;
    }

    public void setKomoranUserDic(String data){
        //패키지 이름 가져오기
        String package_=getClass().getPackage().getName();
        Log.d(TAG, "setKomoranUserDic: "+package_);

        String fileName= "UserDic.txt";
        FileWriter writer = null;

        String path= Environment.getDataDirectory().getAbsolutePath()+"/data/"+package_+"/files";
        Log.d(TAG, "setKomoranUserDic: "+path);

        WriteTextFile(path,fileName,data);
    }

    public void startSpeechRecognizer(String[] stringsData) throws JSONException {

        komoran.setUserDic("/data/data/com.example.kioskmainpage/files/UserDic.txt");
        rs = stringsData;

        boolean isinclude_menus = false;
        String[] KOMORAN_adverb = {"시원", "차갑", "따뜻", "뜨뜻", "뜨겁"};
        String[] KOMORAN_thick = {"달달하게", "보통", "진하게", "더 진하게"};
        String[] hangleNumber1 = {"영", "한", "두", "세", "네", "다섯", "여섯", "일곱", "여덟", "아홉", "빵", "|", "|", "석", "넉", "|", "|", "|", "|", "|", "공", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] hangleNumber2 = {"열", "스물", "서른", "마흔", "쉰", "예순", "일흔", "여든", "아흔"};
        String[] hangleNumber3 = {"십", "백", "천"};
        String[] KOMORAN_size = {"크", "작", "조그마하"};
        String menus_name = "아메리카노|카라멜 마끼아또|카페모카|카페라떼|카페모카|화이트초코|아포카토|리스레스토 비얀코|그린 티 크림|모카 푸라푸치노|바닐라크림|에스프레소 프라푸치노|자바칩 푸라푸치노|카라멜 푸라푸치노|화이트 딸기 크림|초콜릿 크림|초콜릿 모카|7레이어 가나슈|레드벨벳 크림치즈|생크림 카스텔라|블루베리 쿠키 치즈케이크|포콜릿 데블스 케이크|촉촉 생크림 케이크|크레이프 치즈|클라우드 치즈|호두 당근 케이크|나이트로 쇼콜라|나이트로 콜드브루|돌체 콜드브루|바닐라크림 콜드브루|콜드브루 폼|콜드브루 몰트";
        String[] menus_name_split = menus_name.split("\\|");
        KomoranResult[] result = new KomoranResult[rs.length];
        String speak_menu = "";
        String speak_adverb = "";
        int speak_count = 0;
        String speak_thick = "";
        String speak_size = "";
        int result_serch = 2;
        int rs_index = 0;
        int[] menus_len = {8, 9, 9, 6};
        String data="";
        for (int i = 0; i < rs.length; i++) {
            result[i] = komoran.analyze(rs[i]);
        }
        for (int i = 0; i < rs.length; i++) {
            List<Token> tokenList_1 = result[i].getTokenList();
            for(Token A:tokenList_1)
                Log.d(TAG, "startSpeechRecognizer: "+A.toString());
        }

        lab:
        for (int i = 0; i < rs.length; i++) {
            List<String> tokenList_1 = result[i].getMorphesByTags("NNP");

            for (int j = 0; j < tokenList_1.size(); j++) {

                for (int k = 0; k < menus_name_split.length; k++) {
                    Log.d(TAG, "startSpeechRecognizer: "+tokenList_1.get(j)+"\t\t\t"+menus_name_split[k]);
                    if (tokenList_1.get(j).contains(menus_name_split[k])) {
                        isinclude_menus = true;
                        speak_menu = tokenList_1.get(j);
                        break lab;
                    }
                }
            }
        }

        lab2:
        if (!isinclude_menus) {
            for (int ii = 0; ii < rs.length; ii++) {
                String[] C_list = rs[ii].split(" ");
                for (int i = 0; i < C_list.length; i++) {
                    String temp = "";
                    for (int j = i; j < C_list.length; j++) {
                        temp = temp + C_list[j];
                        char[] chars1 = temp.toCharArray();
                        for (int k = 0; k < menus_name_split.length; k++) {
                            char[] chars2 = menus_name_split[k].toCharArray();
                            if (levenshteinDistance(chars1, chars2) < result_serch) {
                                speak_menu = menus_name_split[k];
                                isinclude_menus = true;
                                rs_index = ii;
                                break lab2;
                            }
                        }
                    }
                }
            }
        }

        //문장에서 메뉴를 끝까지 못찾았을때
        if (isinclude_menus == false) {
            data = "메뉴를 못찾았습니다.";
            jsonObject.put("is_intent_go",false);
            TargetSetTextView(textViewId, activityNum, data);
        }

        //문장에서 메뉴를 찾았으면 문장 분석을 시작합니다.
        else {
            List<String> tokenList_1 = result[rs_index].getMorphesByTags("VA", "XR", "NA");
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
            //진하기 선택
            for (String temp_tokenList : tokenList_1) {
                for (String temp_KOMORAN_thick : KOMORAN_thick) {
                    if (levenshteinDistance(HangleSplit(temp_tokenList), HangleSplit(temp_KOMORAN_thick)) < 2) {
                        speak_thick = temp_KOMORAN_thick;
                    }
                }
            }

            //개수 선택
            List<String> tokenList_2 = result[rs_index].getMorphesByTags("SN", "NR","MM");
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

            int index = Arrays.asList(menus_name_split).indexOf(speak_menu);
            int inttemp = 0;
            int category = 0;
            for (int i = 0; i < menus_len.length; i++) {
                inttemp += menus_len[i];
                if (rs_index < inttemp)
                    category = i;
            }
            String adverb;
            String size;


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
            if (speak_thick.equals("진하게")){

            }
            speak_thick += "\t";
            Log.d(TAG, "onResults: " + data);

            jsonObject.put("size",size);
            jsonObject.put("adverb",adverb);
            jsonObject.put("count",speak_count);
            jsonObject.put("speak_thick",speak_thick);
            jsonObject.put("menu_number",index);
            jsonObject.put("is_intent_go",true);
        }
    }




    //텍스트내용을 경로의 텍스트 파일에 쓰기
    public void WriteTextFile(String foldername, String filename, String contents){
        try{
            File dir = new File (foldername);
            //디렉토리 폴더가 없으면 생성함
            if(!dir.exists()){
                dir.mkdir();
            }
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, false);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void TargetSetTextView(int textViewId,int activityNum,String data) {
        switch (activityNum) {
            case 1:
                TextView textView = (TextView)((Senior_MainActivity) context).findViewById(textViewId);
                textView.setText(data);
        }
    }
    //편집거리 알고리즘 부속 Min 함수
    private int getMinimum(int val1, int val2, int val3) {
        int minNumber = val1;
        if (minNumber > val2) minNumber = val2;
        if (minNumber > val3) minNumber = val3;
        return minNumber;
    }

    //편집거리 알고리즘
    private int levenshteinDistance(char[] s, char[] t) {
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

    private char[] HangleSplit(String A) {
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

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
