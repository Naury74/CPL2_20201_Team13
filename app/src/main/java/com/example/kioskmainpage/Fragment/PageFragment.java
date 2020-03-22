package com.example.kioskmainpage.Fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.kioskmainpage.Adapter.RAdapter;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.Menu;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.RecyclerDecoration;
import com.example.kioskmainpage.Utilities.Chekers;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    public static final String TAG = "PageFragment";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String NAME_PAGE = "NAME_PAGE";
    public static final String MENU_PAGE = "MENU_PAGE";
    public static final String BEST_MENUS = "BEST_MENUS";
    private int mPage;
    private String saveDir;
    private String pagetitle;
    public ArrayList<? extends Menu> menuList;
    public ArrayList<String> best_menus;
    private RAdapter adapter;
    public int numOfMenus;
    int num;
    RecyclerView recyclerView;
    ScrollView fragment_scrollView;
    Myapplication app;
    MainTheme mainTheme;
    public PageFragment() {
        // Required empty public constructor
    }


    public static PageFragment newInstance(int page, String tabName, ArrayList<? extends Parcelable> list, ArrayList<String> best_menus) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(NAME_PAGE, tabName);
        args.putParcelableArrayList(MENU_PAGE, list);
        args.putStringArrayList(BEST_MENUS,best_menus);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PageFragment","onResume");
        //화면 다시 올라올때마다 테마 설정
        adapter.setTheme();
//        fragment_scrollView.setScrollY(0);//프레그먼트가 resume때마다 항상위로 스크롤
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("PageFragment","onStop");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPage = getArguments().getInt(ARG_PAGE);
        pagetitle = getArguments().getString(NAME_PAGE);
        //FragmentPager 어댑터에서 새로운 Fragment 생성시 추가한 메뉴를 받아옴
        menuList = getArguments().getParcelableArrayList(MENU_PAGE);
        numOfMenus = menuList.size();
    //    System.out.println("메뉴개수 : "+numOfMenus);
        best_menus = getArguments().getStringArrayList(BEST_MENUS);
      //  System.out.println("베스트 메뉴 테스트 : "+best_menus.size());
        app = (Myapplication)getActivity().getApplication();
        mainTheme = app.getMainTheme();
        adapter=new RAdapter((ArrayList<Menu>)menuList, (ArrayList<String>)best_menus, mainTheme, app);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        //saveDir = view.getContext().getFilesDir().getAbsolutePath();
        ScrollView scrollView = (ScrollView) view;
        app = (Myapplication)getActivity().getApplication();
        mainTheme = app.getMainTheme();

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN)//버튼 눌렀을때
                {
                    //       System.out.println("타이머 리셋");


                }
                else if(action == MotionEvent.ACTION_UP)//손 뗐을 때
                {
                    System.out.println("타이머 리셋2");
                    app.getCountDownTimer().cancel();//타이머 취소
                    app.getCountDownTimer().start();
                }
                return false;
            }
        });
     //   scrollView.setBackgroundColor(mainTheme.getBACKGROUND_COLOR_ID());
        //스크롤뷰의 수직 스크롤, 뷰페이저의 횡스크롤 충돌을 막음
        scrollView.requestDisallowInterceptTouchEvent(true);
        createView(view);
        fragment_scrollView = (ScrollView)view.findViewById(R.id.fragment_scrollView);
        return view;
    }

    public RAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RAdapter adapter) {
        this.adapter = adapter;
    }

    public void createView(View view) {
        recyclerView = (RecyclerView)view.findViewById(R.id.menu_recyclerview);
        if(Chekers.check_device_type().equals("PLUS")) {
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 4));
        }
        else
        {
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        }
        recyclerView.setAdapter(adapter);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(50);//줄간격 조절 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)
        recyclerView.addItemDecoration(spaceDecoration);
        app.getRAdapters().add(adapter);
    }
    public ScrollView getFragment_scrollView(){
        return fragment_scrollView;
    }
}
