package com.example.kioskmainpage.Utilities;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kioskmainpage.R;

public class Senior_MenuViewer extends LinearLayout {

    TextView menu_name_view;
    TextView menu_price_view;
    ImageView menu_image_view;

    public Senior_MenuViewer(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.senior_menu_item,this,true);

        menu_name_view = (TextView)findViewById(R.id.menu_name_view);
        menu_price_view = (TextView)findViewById(R.id.menu_price_view);
        menu_image_view = (ImageView) findViewById(R.id.menu_image_view);
       // menu_image_view.setBackground(new ShapeDrawable(new OvalShape()));
        //menu_image_view.setClipToOutline(true);
    }

    public void setItem(Senior_MenuItem senior_menuitem){
        menu_name_view.setText(senior_menuitem.getName());
        menu_price_view.setText(senior_menuitem.getPrice()+"원");
        menu_image_view.setImageResource(senior_menuitem.getImage());
    }
}
