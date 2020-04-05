package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.kioskmainpage.Utilities.Senior_MenuItem;
import com.example.kioskmainpage.Utilities.Senior_MenuViewer;

import java.util.ArrayList;

public class Senior_MenuAdapter extends BaseAdapter {

    ArrayList<Senior_MenuItem> items = new ArrayList<Senior_MenuItem>();

    private Context context;

    @Override
    public int getCount(){
        return items.size();
    }

    public void addItem(Senior_MenuItem senior_menuItem){
        items.add(senior_menuItem);
    }

    public Senior_MenuAdapter(Context context){
        this.context = context;
    }

    @Override
    public Senior_MenuItem getItem(int i){
        return items.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        Senior_MenuViewer senior_menuViewer = new Senior_MenuViewer(context);
        senior_menuViewer.setItem(items.get(i));
        return senior_menuViewer;
    }
}
