package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;
import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.R;
import com.example.kioskmainpage.Utilities.Senior_SelectedItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Senior_SelectedItem_Adapter extends BaseAdapter {

    private ArrayList<Senior_SelectedItem> mItems = new ArrayList<>();
    String mcount;
    String mprice;
    int item_count;
    int item_price;
    int init_price;
    TextView menuName;
    TextView countView;
    TextView priceView;
    TextView menu_optionView;
    ImageButton minusButton;
    ImageButton plusButton;
    Button deleteButton;
    int index_pos;

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Senior_SelectedItem getItem(int position) {
        this.index_pos = position;
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.selected_orderlist_item, parent, false);
        }

        menuName = (TextView)convertView.findViewById(R.id.menuName);
        countView = (TextView) convertView.findViewById(R.id.countView) ;
        priceView = (TextView) convertView.findViewById(R.id.priceView) ;
        menu_optionView = (TextView)convertView.findViewById(R.id.menu_optionView);
        minusButton = (ImageButton)convertView.findViewById(R.id.minusButton);
        plusButton = (ImageButton)convertView.findViewById(R.id.plusButton);
        deleteButton = (Button)convertView.findViewById(R.id.deleteButton);

        Senior_SelectedItem myItem = getItem(position);

        item_count = myItem.getMenu_count();
        item_price = myItem.getPrice();
        init_price = myItem.getPrice();

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        mcount = ""+item_count;
        mprice = myFormatter.format(item_count*item_price);

        menuName.setText(myItem.getName());
        countView.setText(mcount+"개");
        priceView.setText(mprice+"원");
        menu_optionView.setText(myItem.getOption());

        minusButton.setOnClickListener(new minusListener(myItem, this));
        plusButton.setOnClickListener(new plusListener(myItem, this));
        deleteButton.setOnClickListener(new deleteListener(myItem, index_pos, this));

        return convertView;
    }

    public void addItem(String name, int price, String option, int menu_count) {

        Senior_SelectedItem mItem = new Senior_SelectedItem();

        mItem.setName(name);
        mItem.setPrice(price);
        mItem.setMenu_count(menu_count);
        mItem.setOption(option);

        mItems.add(mItem);

    }

    public class minusListener implements View.OnClickListener {

        Senior_SelectedItem senior_selectedItem;
        Senior_SelectedItem_Adapter senior_selectedItem_adapter;

        public minusListener(Senior_SelectedItem senior_selectedItem, Senior_SelectedItem_Adapter senior_selectedItem_adapter) {
            this.senior_selectedItem = senior_selectedItem;
            this.senior_selectedItem_adapter = senior_selectedItem_adapter;
        }

        @Override
        public void onClick(View view) {
            if(item_count!=1){
                senior_selectedItem.setMenu_count(senior_selectedItem.getMenu_count()-1);
                senior_selectedItem_adapter.notifyDataSetChanged();
            }
        }
    }

    public class plusListener implements View.OnClickListener {

        Senior_SelectedItem senior_selectedItem;
        Senior_SelectedItem_Adapter senior_selectedItem_adapter;

        public plusListener(Senior_SelectedItem senior_selectedItem, Senior_SelectedItem_Adapter senior_selectedItem_adapter) {
            this.senior_selectedItem = senior_selectedItem;
            this.senior_selectedItem_adapter = senior_selectedItem_adapter;
        }

        @Override
        public void onClick(View view) {
            senior_selectedItem.setMenu_count(senior_selectedItem.getMenu_count()+1);
            senior_selectedItem_adapter.notifyDataSetChanged();
        }
    }

    public class deleteListener implements View.OnClickListener {

        Senior_SelectedItem senior_selectedItem;
        Senior_SelectedItem_Adapter senior_selectedItem_adapter;
        int index;

        public deleteListener(Senior_SelectedItem senior_selectedItem, int index, Senior_SelectedItem_Adapter senior_selectedItem_adapter) {
            this.senior_selectedItem = senior_selectedItem;
            this.senior_selectedItem_adapter = senior_selectedItem_adapter;
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            mItems.remove(index);
            senior_selectedItem_adapter.notifyDataSetChanged();
        }
    }

    public int getPriceSum(){
        int sum=0;
        for(int i=0; i<mItems.size();i++){
            int cnt = mItems.get(i).getMenu_count();
            int price = mItems.get(i).getPrice();
            sum += price*cnt;
        }
        return sum;
    }

}
