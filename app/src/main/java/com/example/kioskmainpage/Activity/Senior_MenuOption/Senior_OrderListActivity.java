package com.example.kioskmainpage.Activity.Senior_MenuOption;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.EasyMenuSelectionActivity;
import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;
import com.example.kioskmainpage.Adapter.Senior_SelectedItem_Adapter;
import com.example.kioskmainpage.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class Senior_OrderListActivity extends AppCompatActivity {

    private ListView mListView;
    Intent intent;
    int menu_image;
    String menu_name;
    String m_price;
    int menu_price;
    String menu_option = "";
    int category_num;
    int menu_count;
    int selected_count;
    int total_price;
    String total_price_text;
    TextView total_price_textview;
    TextView title_text;
    private TextToSpeech tts;

    Handler handler = new Handler();

    Senior_SelectedItem_Adapter senior_selectedItem_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senior_orderlist);

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
        category_num = intent.getExtras().getInt("category");
        menu_image = intent.getExtras().getInt("menu_image");
        menu_name = intent.getExtras().getString("menu_name");
        m_price = intent.getExtras().getString("menu_price");
        menu_option = intent.getExtras().getString("menu_option");
        menu_count = intent.getExtras().getInt("menu_count");

        menu_price = Integer.parseInt(m_price);

        mListView = (ListView)findViewById(R.id.listView);
        dataSetting();
        total_price_textview = (TextView)findViewById(R.id.total_price);
        title_text = (TextView)findViewById(R.id.title_text);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {

                    tts.setLanguage(Locale.KOREAN);
                    tts.speak("주문이 추가되었습니다",TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                DecimalFormat myFormatter = new DecimalFormat("###,###");
                                selected_count = senior_selectedItem_adapter.getCount();
                                total_price = senior_selectedItem_adapter.getPriceSum();
                                total_price_text = myFormatter.format(total_price);
                                total_price_textview.setText(total_price_text+"원");
                                senior_selectedItem_adapter.notifyDataSetChanged();
                                if(selected_count==0){
                                    title_text.setText("장바구니가 텅~ 비었어요");
                                }
                            }
                        });
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            category_num = intent.getExtras().getInt("category");
            menu_image = intent.getExtras().getInt("menu_image");
            menu_name = intent.getExtras().getString("menu_name");
            m_price = intent.getExtras().getString("menu_price");
            menu_option = intent.getExtras().getString("menu_option");
            menu_count = intent.getExtras().getInt("menu_count");
            menu_price = Integer.parseInt(m_price);
            senior_selectedItem_adapter.addItem(menu_name, menu_price, menu_option, menu_count);
            tts.speak("주문이 추가되었습니다",TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    private void dataSetting(){

        senior_selectedItem_adapter = new Senior_SelectedItem_Adapter();

        senior_selectedItem_adapter.addItem(menu_name, menu_price, menu_option, menu_count);

        mListView.setAdapter(senior_selectedItem_adapter);
        mListView.setVerticalScrollBarEnabled(false);

        senior_selectedItem_adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void onClick_add_order(View v) {
        Intent intent = new Intent(this, EasyMenuSelectionActivity.class);
        intent.putExtra("category",category_num);
        intent.putExtra("menu_image",menu_image);
        intent.putExtra("menu_name",menu_name);
        intent.putExtra("menu_price",menu_price);
        intent.putExtra("menu_option",menu_option);
        intent.putExtra("menu_count",menu_count);
        startActivity(intent);
        //Toast.makeText(this, "Payment Result Total : "+ total_price+"원",Toast.LENGTH_SHORT).show();
    }

    public void onClick_payment(View v) {

        Toast.makeText(this, "Payment Result Total : "+ total_price+"원",Toast.LENGTH_SHORT).show();
    }

}
