package com.example.kioskmainpage.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Senior_MenuOption.Senior_MenuSelected_Check;
import com.example.kioskmainpage.Adapter.Senior_MenuAdapter;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.Senior_MenuItem;

public class Senior_Tab_Fragment extends Fragment {

    public static final String ARG_PAGE="ARG_PAGE";

    private int mpage;
    GridView gridView;
    Senior_MenuAdapter senior_menuAdapter;
    ScrollView scrollView;
    private com.melnykov.fab.FloatingActionButton fab;
    int scrollY = 0;
    Context context;

    public static Senior_Tab_Fragment newinstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        Senior_Tab_Fragment fragment = new Senior_Tab_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mpage = getArguments().getInt(ARG_PAGE);
        context = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_senior__tab_, container, false);
        final GridView gridView = (GridView)view.findViewById(R.id.gridView);
        scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        fab = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.fab);

        senior_menuAdapter = new Senior_MenuAdapter(getActivity());

        if(mpage == 1) {
            senior_menuAdapter.addItem(new Senior_MenuItem("아메리카노", "2500", R.drawable.img1_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("카라멜 마끼아또", "3500", R.drawable.img2_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("카페모카", "4000", R.drawable.img3_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("카페라떼", "2800", R.drawable.img4_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("카페모카", "4300", R.drawable.img5_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("화이트초코", "5300", R.drawable.img6_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("아포카토", "4400", R.drawable.img7_1));
            senior_menuAdapter.addItem(new Senior_MenuItem("리스레스토 비얀코", "5500", R.drawable.img8_1));
        }

        if(mpage == 2) {
            senior_menuAdapter.addItem(new Senior_MenuItem("그린 티 크림", "6300", R.drawable.img1_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("모카 푸라푸치노", "5600", R.drawable.img2_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("바닐라크림", "4900", R.drawable.img3_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("에스프레소 프라푸치노", "5800", R.drawable.img4_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("자바칩 푸라푸치노", "6100", R.drawable.img5_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("카라멜 푸라푸치노", "5600", R.drawable.img6_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("화이트 딸기 크림", "5600", R.drawable.img7_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("초콜릿 크림", "5700", R.drawable.img8_2));
            senior_menuAdapter.addItem(new Senior_MenuItem("초콜릿 모카", "5700", R.drawable.img9_2));
        }

        if(mpage == 3) {
            senior_menuAdapter.addItem(new Senior_MenuItem("7레이어 가나슈", "4800", R.drawable.img1_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("레드벨벳 크림치즈", "5500", R.drawable.img2_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("생크림 카스텔라", "4500", R.drawable.img3_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("블루베리 쿠키 치즈케이크", "6800", R.drawable.img4_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("포콜릿 데블스 케이크", "5900", R.drawable.img5_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("촉촉 생크림 케이크", "5200", R.drawable.img6_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("크레이프 치즈", "6500", R.drawable.img7_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("클라우드 치즈", "5500", R.drawable.img8_3));
            senior_menuAdapter.addItem(new Senior_MenuItem("호두 당근 케이크", "6500", R.drawable.img9_3));
        }

        if(mpage == 4) {
            senior_menuAdapter.addItem(new Senior_MenuItem("나이트로 쇼콜라", "6100", R.drawable.img1_4));
            senior_menuAdapter.addItem(new Senior_MenuItem("나이트로 콜드브루", "5800", R.drawable.img3_4));
            senior_menuAdapter.addItem(new Senior_MenuItem("돌체 콜드브루", "5800", R.drawable.img4_4));
            senior_menuAdapter.addItem(new Senior_MenuItem("바닐라크림 콜드브루", "8500", R.drawable.img5_4));
            senior_menuAdapter.addItem(new Senior_MenuItem("콜드브루 폼", "5800", R.drawable.img9_4));
            senior_menuAdapter.addItem(new Senior_MenuItem("콜드브루 몰트", "8000", R.drawable.img6_4));
        }

        gridView.setAdapter(senior_menuAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"이름 : "+ senior_menuAdapter.getItem(i).getName().toString() + " , Price : "+senior_menuAdapter.getItem(i).getPrice().toString(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, Senior_MenuSelected_Check.class);
                intent.putExtra("category",mpage);
                intent.putExtra("menu_image",senior_menuAdapter.getItem(i).getImage());
                intent.putExtra("menu_name",senior_menuAdapter.getItem(i).getName().toString());
                intent.putExtra("menu_price",senior_menuAdapter.getItem(i).getPrice().toString());
                context.startActivity(intent);
                
            }
        });

        fab.attachToListView(gridView);

        // 이벤트 적용
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollY = gridView.getScrollY();
                //gridView.setScrollY(gridView.getBottom());
            }
        });

        return view;
    }

}
