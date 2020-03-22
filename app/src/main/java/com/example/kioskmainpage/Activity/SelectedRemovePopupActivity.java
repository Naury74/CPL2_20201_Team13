package com.example.kioskmainpage.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.kioskmainpage.R;

import java.util.ArrayList;

import static com.example.kioskmainpage.Activity.MainActivity.countDownReset;

public class SelectedRemovePopupActivity extends AppCompatActivity {

    /* 작성자 : 2019-S 코딩노비 김세현 */

    int index;
    String menu_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_remove_popup);
        TextView removeText = (TextView)findViewById(R.id.SelectedRemoveText);
        Button Bdone = (Button)findViewById(R.id.SelectedRemoveDoneButton);
        Button Bcancel = (Button)findViewById(R.id.SelectedRemoveCancelButton);
        Intent intent = getIntent();
     //   countDownReset();//카운트다운리셋
        //상,하단의 바를 제거하고 풀스크린으로 만듬
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);


        // 메뉴 이름 받기 및 텍스트 설정
        index = intent.getIntExtra("index", -1);
        menu_name = "\""+intent.getStringExtra("menu_name")+"\"";
        String showtext = menu_name+"를(을)\n주문 목록에서\n삭제하시겠습니까?";
        removeText.setText(showtext);
        Spannable span = (Spannable) removeText.getText();
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, menu_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //메뉴명 bold처리

        // 확인버튼
        Bdone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Bcancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        }); // 취소버튼
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //팝업 액티비티의 바깥쪽 터치를 무시하도록
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //하단 네비게이션 바를 숨겨놨지만 최하단을 위쪽으로 드래그하면 바가 나와서 뒤로가기 버튼 막음
        return;
    }

}
