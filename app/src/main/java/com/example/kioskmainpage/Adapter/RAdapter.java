package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.MainActivity;
import com.example.kioskmainpage.Activity.PopupActivity;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.Chekers;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

//MainActivity의 메뉴들 보여줌
public class RAdapter extends RecyclerView.Adapter<RAdapter.CustomViewHolder> {

    private String TAG = "RAdapter";
    private ArrayList<Menu> mList;
    private ArrayList<String> best_menus;
    private boolean isFinished = false;
    private MainTheme mainTheme;
    double x_ratio = 0.2625;
    double PLUS_ratio = 0.22;
    double y_ratio = 0.164;
    ArrayList<CustomViewHolder> viewHolders;
    Myapplication app;
    //메인 메뉴 각 카테고리 메뉴들 리스트 데이터를 읽어서 어댑터뷰에 공급

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView best;
        protected ImageView menu_image;
        protected ImageView menu_soldout;
        protected TextView menu_name;
        protected TextView menu_price;
        protected FrameLayout frameLayout;//2019-S 김세현 디자인변경으로 인해 가격부분 추가
        Context mContext;
        //menu_item.xml 로 연결
        public CustomViewHolder(View view, Context context) {
            super(view);

            this.best = (ImageView) view.findViewById(R.id.best_icon_imageview);
            this.menu_image = (ImageView) view.findViewById(R.id.menu_image_view);
            this.menu_name = (TextView) view.findViewById(R.id.menu_name_view);
            this.menu_price = (TextView) view.findViewById(R.id.menu_price_view); //2019-S 김세현 디자인변경으로 인해 가격부분 추가
            this.mContext = context;
            this.frameLayout = (FrameLayout) view.findViewById(R.id.framelayout_menuitem);
            this.menu_soldout = (ImageView) view.findViewById(R.id.menu_sold_out); // 매진용 이미지추가
        }

        public Context getContext() {
            return mContext;
        }
    }

    public RAdapter(ArrayList<Menu> list, ArrayList<String> best_menus, MainTheme mainTheme,Myapplication app) {
        this.mList = list;
        this.best_menus = best_menus;
        this.mainTheme = mainTheme;
        this.app = app;
        viewHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view, viewGroup.getContext());

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewHolder, int position) {
        //ListView 퍼포먼스 높이기 위한 viewHolder, findViewById 호출 시 비용 큰 문제 해
        viewHolder.menu_name.setTextSize(18);

//        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration( viewHolder.menu_name, 10, 20, 1,
//                TypedValue.COMPLEX_UNIT_DIP);

        viewHolder.menu_price.setTextSize(17);

        Menu menu = mList.get(position);

        Bitmap bitmap = null;
        Display display = ((WindowManager) viewHolder.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        //   System.out.println("화면 크기 : "+width+", "+height);
        if(Chekers.check_device_type().equals("PLUS")){
            width = (int) ((double) height * PLUS_ratio);
        }
        else{
            width = (int) ((double) width * x_ratio);
        }
        height = width;
        //    System.out.println("적용 크기 : "+width+", "+height);
        FrameLayout.LayoutParams img_param = new FrameLayout.LayoutParams(width, height);//메인메뉴 원형 이미지들 크기 조절,제한
        //     FrameLayout.LayoutParams img_param = new FrameLayout.LayoutParams(210,210);
        img_param.gravity = Gravity.CENTER;

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.parseColor("#00000000"));
        drawable.getPaint().setStyle(Paint.Style.FILL);
        viewHolder.menu_image.setBackground(drawable);
        viewHolder.menu_image.setLayoutParams(img_param);
        File imageFile = new File(menu.getBitmap());
        if (menu.getBitmap().equals("No_Image"))//이미지가 없는경우
        {
            viewHolder.menu_image.setImageDrawable(viewHolder.getContext().getResources().getDrawable(R.drawable.ic_no_image2));
        } else {//이미지가 있는경우
            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            //이미지관련 최적화
            while (bitmap.getHeight() > height) {
                bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height, true);
            }
            viewHolder.menu_image.setImageBitmap(bitmap);
        }

        viewHolder.menu_image.setClipToOutline(true);
        viewHolder.menu_image.setAdjustViewBounds(true);
        viewHolder.menu_image.setScaleType(ImageView.ScaleType.FIT_XY);
        viewHolder.menu_image.setOnClickListener(new menuOnclick(viewHolder.getContext(), menu));

        viewHolder.menu_soldout.setLayoutParams(img_param);//매진 표시이미지 크기조절

        viewHolder.menu_name.setText(menu.getMenu_name());
        viewHolder.menu_name.setTextColor(viewHolder.getContext().getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        String commaNum = "￦ " + NumberFormat.getInstance().format(Integer.parseInt(menu.getMenu_price()));
        viewHolder.menu_price.setText(commaNum);
        viewHolder.menu_price.setTextColor(viewHolder.getContext().getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));//테마
        //String won = Currency.getInstance(Locale.KOREA).getSymbol();
        //viewHolder.menu_price.setText(won + commaNum);

        //best 이미지 위치 자동 조정
        RelativeLayout.LayoutParams f_l = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height + 50);//50은 이미지 높이 + (임의마진값)
        viewHolder.frameLayout.setLayoutParams(f_l);

        FrameLayout.LayoutParams best_param = new FrameLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        best_param.gravity = Gravity.CENTER_HORIZONTAL;
        viewHolder.best.setLayoutParams(best_param);

        if (best_menus != null) {
            System.out.println("베스트 메뉴 사이즈 : " + best_menus.size());
            for (int i = 0; i < best_menus.size(); i++) {
                if (best_menus.get(i).equals(menu.getMenu_name()))//베스트 메뉴랑 현재 메뉴랑 이름 같으면 best이미지 띄움
                {
                    viewHolder.best.setVisibility(View.VISIBLE);
                    System.out.println("베스트 메뉴 : " + menu.getMenu_name());
                    break;
                }
            }
        }
        viewHolders.add(viewHolder);

    }

    //테마 설정
    public void setTheme() {
        //viewholders의 크기가 0일때는 바로 종료해서 예외처리
        if (viewHolders.size() == 0)
            return;
        Myapplication app = (Myapplication) viewHolders.get(0).getContext().getApplicationContext();
        mainTheme = app.getMainTheme();
        for (int i = 0; i < viewHolders.size(); i++) {
            viewHolders.get(i).menu_name.setTextColor(viewHolders.get(i).getContext().getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));
            viewHolders.get(i).menu_price.setTextColor(viewHolders.get(i).getContext().getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));
        }
    }

    public ArrayList<CustomViewHolder> getViewHolders() {
        return viewHolders;
    }

    public void setViewHolders(ArrayList<CustomViewHolder> viewHolders) {
        this.viewHolders = viewHolders;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CustomViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("RAdapter", "onViewDetachedFromWindow");
        if (isFinished == true) {
            recycleBitmap(holder.menu_image);
        }
    }

    private static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        if (b != null && !b.isRecycled()) {

            b.recycle();
            b = null;
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

        d.setCallback(null);
    }


    //매진 설정
    public void setSoldout(boolean soldout, String sold_out_menu) {

        ArrayList<CustomViewHolder> viewHolders = getViewHolders();
        Log.d(TAG, "setSoldout viewHolder size : " + viewHolders.size());
        for (int i = 0; i < viewHolders.size(); i++) {
            CustomViewHolder holder = viewHolders.get(i);
            if (holder.menu_name.getText().toString().equals(sold_out_menu)) {
                if (soldout)//매진여부 판단
                {
                    holder.menu_soldout.setVisibility(View.VISIBLE);
                    holder.menu_image.setClickable(false);
                    app.soldouts_add(sold_out_menu);
                } else {
                    holder.menu_soldout.setVisibility(View.GONE);
                    holder.menu_image.setClickable(true);
                    app.soldouts_remove(sold_out_menu);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
            intent.putIntegerArrayListExtra("options", null);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            ((MainActivity) context).startActivityForResult(intent, 1);
        }
    }

}
