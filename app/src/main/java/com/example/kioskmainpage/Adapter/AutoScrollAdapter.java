package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kioskmainpage.Activity.BestNewMenuActivity;
import com.example.kioskmainpage.Activity.PopupActivity;
import com.example.kioskmainpage.MainTheme;
import com.example.kioskmainpage.MenuManage.Menu;
import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.R;

import java.io.File;
import java.util.ArrayList;

public class AutoScrollAdapter extends PagerAdapter {

    Context context;
//    ArrayList<String> best_img_links;
    ArrayList<Menu> menus;
    ArrayList<View> viewArrayList = new ArrayList<>();
    Myapplication app;
    MainTheme mainTheme;
    int height;
    public AutoScrollAdapter(Context context, ArrayList<Menu> menus, int height, MainTheme mainTheme) {
        this.context = context;
        this.menus = menus;
        this.height = height;
        this.mainTheme = mainTheme;
        System.out.println("height : "+height);
    }

    public ArrayList<View> getViewArrayList() {
        return viewArrayList;
    }

    public void setViewArrayList(ArrayList<View> viewArrayList) {
        this.viewArrayList = viewArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auto_viewpager,null);
        viewArrayList.add(v);
        ImageButton image_container = (ImageButton) v.findViewById(R.id.bestimg_bestmenu);
        TextView menu_name = (TextView) v.findViewById(R.id.bestname_bestmenu);
       // TextView menu_price = (TextView) v.findViewById(R.id.bestprice_bestmenu);
        TextView menu_ments = (TextView) v.findViewById(R.id.ment_bestmenu);
        menu_ments.setTextColor(context.getColor(mainTheme.getThemeItem().getTEXT_COLOR_ID()));
        menu_name.setText(menus.get(position).getMenu_name());
    //    menu_price.setText(menus.get(position).getMenu_price()+"원");
        menu_ments.setText(menus.get(position).getMenu_ment());

        Myapplication app = (Myapplication)(context.getApplicationContext());
        Bitmap bitmap = null;
        File imageFile = new File(app.getBest_image_links().get(position));//best이미지 파일 생성
        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        //이미지관련 최적화
        while (bitmap.getHeight() > height) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height, true);
        }

        image_container.setImageBitmap(bitmap);
        image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("menu", menus.get(position));
                intent.putIntegerArrayListExtra("options", null);
                ((BestNewMenuActivity)context).startActivityForResult(intent,1);//기존과 다르게 수정됨
            }
        });

        //Glide.with(context).load(data.get(position)).into(image_container);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ImageButton imageButton = ((View)object).findViewById(R.id.bestimg_bestmenu);
        recycleBitmap(imageButton);

        container.removeView((View)object);


    }

    private static void recycleBitmap(ImageButton iv) {
        Drawable d = iv.getDrawable();
        Bitmap b = ((BitmapDrawable)d).getBitmap();
        if (b != null && !b.isRecycled()) {

            b.recycle();
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

        d.setCallback(null);
    }
    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
