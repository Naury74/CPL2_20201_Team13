package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Admin.Admin_themeActivity;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.ThemeItem;

import java.util.ArrayList;

//Admin_themeActivity의 테마 리스트 보여줌
public class RAdapter3 extends RecyclerView.Adapter<RAdapter3.CustomViewHolder> {
    private ArrayList<ThemeItem> themeItems;
    private ArrayList<String> best_menus;
    private boolean isFinished = false;
    private MainTheme mainTheme;
    double y_ratio = 0.3125;// 400/1280
    ArrayList<RAdapter3.CustomViewHolder> viewHolders;
    private int checkedTheme=0;
    //메인 메뉴 각 카테고리 메뉴들 리스트 데이터를 읽어서 어댑터뷰에 공급

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        Context mContext;

        public int position;
        public ImageButton theme_image;
        public TextView theme_name;
        public LinearLayout theme_layout;
        //menu_item.xml 로 연결
        public CustomViewHolder(View view, Context context) {
            super(view);

            this.theme_image = (ImageButton)view.findViewById(R.id.theme_image_button);
            this.theme_name = (TextView)view.findViewById(R.id.theme_name);
            this.theme_layout = (LinearLayout) view.findViewById(R.id.theme_layout);
            this.mContext=context;

        }

        public Context getContext() {
            return mContext;
        }
    }

    public RAdapter3(ArrayList<ThemeItem> themeItems, MainTheme mainTheme){
        this.themeItems = themeItems;
        this.mainTheme = mainTheme;
        viewHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public RAdapter3.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.theme_item,viewGroup,false);

        RAdapter3.CustomViewHolder viewHolder = new RAdapter3.CustomViewHolder(view, viewGroup.getContext());

        return viewHolder;
    }

    public int getCheckedTheme() {
        return checkedTheme;
    }

    public void setCheckedTheme(int checkedTheme) {
        this.checkedTheme = checkedTheme;
    }


    @Override
    public void onBindViewHolder(@NonNull final RAdapter3.CustomViewHolder viewHolder, final int position) {
        //ListView 퍼포먼스 높이기 위한 viewHolder, findViewById 호출 시 비용 큰 문제 해

        Myapplication app = (Myapplication)viewHolder.getContext().getApplicationContext();
        //ThemeItem themeItem = themeItems.get(position);

        Display display = ((WindowManager)viewHolder.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        height = (int) ((double)height*y_ratio);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height);
        viewHolder.theme_image.setLayoutParams(layoutParams);


        if(position==0)//첫 번째 항목은 white
        {
            if(app.getMainTheme().getTheme_name().equals("WHITE"))//현재 설정된게 white이면 화이트체크이미지
            {
                viewHolder.theme_image.setImageResource(R.drawable.white_skin_check_img);
                checkedTheme = 0;
            }
            else
            {
                viewHolder.theme_image.setImageResource(R.drawable.white_skin_img);
            }
            viewHolder.theme_name.setText("기본");
            viewHolder.theme_name.setTextColor(viewHolder.getContext().getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        }
        else if(position==1)//두 번째 항목은 black
        {
            if(app.getMainTheme().getTheme_name().equals("BLACK"))//현재 설정된게 black이면 블랙체크이미지
            {
                viewHolder.theme_image.setImageResource(R.drawable.black_skin_check_img);
                checkedTheme = 1;
            }
            else
            {
                viewHolder.theme_image.setImageResource(R.drawable.black_skin_img);
            }
            viewHolder.theme_name.setText("Dark");
            viewHolder.theme_name.setTextColor(viewHolder.getContext().getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        }
        else if(position==2)//세 번째 항목은 추가 테마
        {
            if(app.getMainTheme().getTheme_name().equals("SCHOOL_FOOD"))//현재 설정된게 School Food 이면 체크이미지
            {
                viewHolder.theme_image.setImageResource(R.drawable.black_skin_check_img);
                checkedTheme = 1;
            }
            else
            {
                viewHolder.theme_image.setImageResource(R.drawable.black_skin_img);
            }
            viewHolder.theme_name.setText("School Food");
            viewHolder.theme_name.setTextColor(viewHolder.getContext().getColor(app.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
        }
        else if(position==3)//네 번째 항목은 추가
        {
            if(app.getMainTheme().getTheme_name().equals("WHITE"))
                viewHolder.theme_image.setImageResource(R.drawable.theme_add_black_img);
            else if(app.getMainTheme().getTheme_name().equals("BLACK"))
                viewHolder.theme_image.setImageResource(R.drawable.theme_add_white_img);
            else if(app.getMainTheme().getTheme_name().equals("SCHOOL_FOOD"))
                viewHolder.theme_image.setImageResource(R.drawable.theme_add_white_img);

            viewHolder.theme_name.setText("");
            viewHolder.theme_name.setTextColor(viewHolder.getContext().getColor(R.color.transparent));
        }

        //버튼 클릭 시 테마 적용 기능
        viewHolder.theme_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checked=checkedTheme;

                if(position==themeItems.size()-1)
                {
                    // TODO : DLC 스킨 추가 할 때 여기서 코드 추가
                    return;
                }
                Toast.makeText(viewHolder.getContext(),"테마가 적용되었습니다.",Toast.LENGTH_SHORT).show();
                viewHolders.get(checked).theme_image.setImageResource(themeItems.get(checked).getThemeNonCheckImg_IC_ID());//기존 체크 항목 체크 해제
                Myapplication myapplication = (Myapplication)viewHolder.getContext().getApplicationContext();
                myapplication.setMainTheme(new MainTheme(themeItems.get(position).getTheme_name()));
                ((ImageButton)v).setImageResource(themeItems.get(position).getThemeCheckImg_IC_ID());
                checkedTheme = position;

                viewHolders.get(viewHolders.size()-1).theme_image.setImageResource(myapplication.getMainTheme().getThemeItem().getThemeAddImage_IC_ID());

                for(int j=0;j<viewHolders.size();j++)
                {
                    viewHolders.get(j).theme_name.setTextColor(viewHolder.getContext().getColor(myapplication.getMainTheme().getThemeItem().getTEXT_COLOR_ID()));
                }
                //액티비티에서 바로 테마 설정
                ((Admin_themeActivity)(viewHolder.getContext())).setTheme();
            }
        });
        viewHolders.add(viewHolder);
    }

    public ArrayList<CustomViewHolder> getViewHolders() {
        return viewHolders;
    }

    public void setViewHolders(ArrayList<CustomViewHolder> viewHolders) {
        this.viewHolders = viewHolders;
    }

    @Override
    public int getItemCount() {
        return themeItems.size();
    }



}
