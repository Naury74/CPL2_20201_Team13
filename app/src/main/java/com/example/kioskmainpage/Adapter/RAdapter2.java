package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.PopupActivity;
import com.example.kioskmainpage.Utilities.Chaches;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

//BestNewMenuActivity의 New메뉴 리스트 보여주는데 사용
public class RAdapter2 extends RecyclerView.Adapter<RAdapter2.CustomViewHolder> {

    private ArrayList<Menu> mList;
    private boolean isFinished = false;
    double x_ratio = 0.2625;
    double y_ratio = 0.164;
    private MainTheme mainTheme;
    Context mContext;
    Myapplication myapp;
    Chaches imagaeCache = new Chaches();//이미지 캐쉬
    String TAG = "RAdapter2";
    ArrayList<CustomViewHolder> viewHolders;


//메인 메뉴 각 카테고리 메뉴들 리스트 데이터를 읽어서 어댑터뷰에 공급

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ArrayList<RecyclerView.ViewHolder> viewHolders = new ArrayList<>();
        protected ImageView menu_image;
        protected ImageView new_menu_sold_out;
        protected TextView menu_name;
        //2019-S 김세현 디자인변경으로 인해 가격부분 추가

        //menu_item.xml 로 연결
        public CustomViewHolder(View view, Context context) {
            super(view);
            menu_image = (ImageView) view.findViewById(R.id.new_menu_image_view);
            menu_name = (TextView) view.findViewById(R.id.new_menu_name_view);//2019-S 김세현 디자인변경으로 인해 가격부분 추가
            new_menu_sold_out = (ImageView) view.findViewById(R.id.new_menu_sold_out); // 매진용 이미지추가
            mContext = context;
        }

        public ImageView getMenu_image() {
            return menu_image;
        }

        public void setMenu_image(ImageView menu_image) {
            this.menu_image = menu_image;
        }

        public Context getContext() {
            return mContext;
        }
    }

    public RAdapter2(ArrayList<Menu> list, MainTheme mainTheme, Myapplication myapp) {
        this.mList = list;
        this.mainTheme = mainTheme;
        this.myapp = myapp;
        viewHolders = new ArrayList<>();
    }

    public void addMenu(Menu menu) {
        mList.add(menu);
    }

    public void removeMenu(int index) {
        mList.remove(index);
    }

    @NonNull
    @Override
    public RAdapter2.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_menu_item, viewGroup, false);

        RAdapter2.CustomViewHolder viewHolder = new RAdapter2.CustomViewHolder(view, viewGroup.getContext());
        return viewHolder;
    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d("RAdapter", "onDetachedFromRecyclerView");
    }

    @Override
    public void onBindViewHolder(@NonNull final RAdapter2.CustomViewHolder viewHolder, int position) {

        //ListView 퍼포먼스 높이기 위한 viewHolder, findViewById 호출 시 비용 큰 문제 해
        position = position % mList.size();
        viewHolder.menu_name.setTextSize(20);

        Menu menu = mList.get(position);

        int width = myapp.getSize_width();
        int height = myapp.getSize_height();
        //    System.out.println("화면 크기 : "+width+", "+height);
        width = (int) ((double) width * x_ratio);
        height = width;
        //       System.out.println("적용 크기 : "+width+", "+height);
        FrameLayout.LayoutParams img_param = new FrameLayout.LayoutParams(width, height);//메인메뉴 원형 이미지들 크기 조절,제한
        // FrameLayout.LayoutParams img_param = new FrameLayout.LayoutParams(210,210);
        img_param.gravity = Gravity.CENTER;

        Bitmap bitmap = null;
        //캐쉬를 이용하여 재활용시 빠른 이미지 변환 key값은 메뉴의 이름으로 함.
        if (imagaeCache.getBitmapFromMemCache(menu.getMenu_name()) == null) //캐쉬목록에 이미지가 없다면
        {
            File imageFile = new File(menu.getBitmap());
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            //이미지관련 최적화
            while (bitmap.getHeight() > height) {
                bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height, true);
            }
            imagaeCache.addBitmapToMemoryCache(menu.getMenu_name(), bitmap); //캐쉬 이미지 추가함.
            Log.d(TAG, "onBindViewHolder: " + menu.getMenu_name() + " 캐쉬 추가됨");
        } else {//캐쉬목록에 이미지가 있다면
            bitmap = imagaeCache.getBitmapFromMemCache(menu.getMenu_name());
            Log.d(TAG, "onBindViewHolder: " + menu.getMenu_name() + " 캐쉬 꺼내씀");
        }

        if(!(bitmap.isRecycled())) {
            viewHolder.menu_image.setLayoutParams(img_param);
            viewHolder.menu_image.setImageBitmap(bitmap);
            ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
            drawable.getPaint().setColor(Color.parseColor("#00000000"));
            drawable.getPaint().setStyle(Paint.Style.FILL);
            viewHolder.menu_image.setBackground(drawable);
            viewHolder.menu_image.setClipToOutline(true);
            viewHolder.menu_image.setAdjustViewBounds(true);
            viewHolder.menu_image.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.menu_image.setOnClickListener(new RAdapter2.menuOnclick(viewHolder.getContext(), menu));
            viewHolder.menu_name.setText(menu.getMenu_name());
            viewHolder.menu_name.setWidth((int)(width*1.2));
            viewHolder.menu_name.setTextColor(viewHolder.getContext().getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));
            String commaNum = NumberFormat.getInstance().format(Integer.parseInt(menu.getMenu_price())) + "원";
            viewHolder.new_menu_sold_out.setVisibility(View.GONE);
            if(myapp!=null) //품절적용중입니다.
            {
                String menu_name = menu.getMenu_name();
                if(myapp.getSoldouts().size()>0)
                {
                    for(String s:myapp.getSoldouts())
                    {
                        if(s.equals(menu_name)) {
                            viewHolder.new_menu_sold_out.setLayoutParams(img_param);
                            viewHolder.new_menu_sold_out.setVisibility(View.VISIBLE);
                            viewHolder.menu_image.setClickable(false);
                        }
                    }
                }
            }
        }
        //String won = Currency.getInstance(Locale.KOREA).getSymbol();
        //viewHolder.menu_price.setText(won + commaNum);
        viewHolders.add(viewHolder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RAdapter2.CustomViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("RAdapter2", "onViewDetachedFromWindow");
        if (isFinished == true) {
            imagaeCache.remove(holder.menu_name.getText().toString());
            recycleBitmap(holder.menu_image);
        }
    }
    public ArrayList<CustomViewHolder> getViewHolders() {
        return viewHolders;
    }
    private static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        if (b != null && !(b.isRecycled())) {
            b.recycle();
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

        d.setCallback(null);
    }

    @Override
    public int getItemCount() {


        return mList == null ? 0 : mList.size() * 2;
    }

    public void setSoldout(boolean soldout, String sold_out_menu) {

        ArrayList<CustomViewHolder> viewHolders = getViewHolders();
        Log.d(TAG, "setSoldout viewHolder size : " + viewHolders.size());
        for (int i = 0; i < viewHolders.size(); i++) {
            CustomViewHolder holder = viewHolders.get(i);
            if (holder.menu_name.getText().toString().equals(sold_out_menu)) {
                if (soldout)//매진여부 판단
                {
                    holder.new_menu_sold_out.setVisibility(View.VISIBLE);
                    holder.menu_image.setClickable(false);
                } else {
                    holder.new_menu_sold_out.setVisibility(View.GONE);
                    holder.menu_image.setClickable(true);
                }
            }
        }
    }
    public class menuOnclick implements View.OnClickListener {

        Context context;
        Menu menu;

        public menuOnclick(Context context, Menu menu) {
            this.context = context;
            this.menu = menu;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PopupActivity.class);
            intent.putExtra("menu", menu);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.putIntegerArrayListExtra("options", null);
            ((BestNewMenuActivity) context).startActivityForResult(intent, 1);
        }
    }

}
