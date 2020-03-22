package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kioskmainpage.MenuManage.SelectedMenu;
import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.List;

public class ReceiptMenuAdapter extends BaseAdapter {

    private ArrayList<SelectedMenu> SelectedItemList = new ArrayList<SelectedMenu>();

    public ReceiptMenuAdapter(){

    }
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return SelectedItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.receipt_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView product_name = (TextView)convertView.findViewById(R.id.product_name);
        TextView product_price = (TextView)convertView.findViewById(R.id.product_price);
        TextView product_quantity = (TextView)convertView.findViewById(R.id.product_quantity);
        TextView total_price = (TextView)convertView.findViewById(R.id.total_price);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SelectedMenu selectedMenu = SelectedItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        product_name.setText(selectedMenu.getMenu_name());
        product_price.setText(selectedMenu.getPrice_one()+"");
        product_quantity.setText(selectedMenu.getCnt()+"");
        total_price.setText( selectedMenu.getPrice_sum()+"");

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return SelectedItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void setSelectedItemList(ArrayList<SelectedMenu> selectedItemList) {

        SelectedItemList = selectedItemList;
    }
    public void addItem(String menu_name,String menu_price,int cnt)
    {
        SelectedMenu selectedMenu = new SelectedMenu();
        selectedMenu.setCnt(cnt);
        selectedMenu.setMenu_name(menu_name);
        selectedMenu.setMenu_price(menu_price);
        SelectedItemList.add(selectedMenu);
    }
}
