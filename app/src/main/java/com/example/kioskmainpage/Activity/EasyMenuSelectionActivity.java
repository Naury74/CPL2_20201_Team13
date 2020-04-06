package com.example.kioskmainpage.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;
import com.example.kioskmainpage.Adapter.RecyclerAdapter_MenuType;
import com.example.kioskmainpage.R;

public class EasyMenuSelectionActivity extends AppCompatActivity {


private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter_MenuType recyclerAdapterMenuType;
String []arr={"빵","우유","김치","닭"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_menu_selection);
        recyclerView=findViewById(R.id.menu_type_recyclerview);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapterMenuType=new RecyclerAdapter_MenuType(arr);

        recyclerView.setAdapter(recyclerAdapterMenuType);
        recyclerView.setHasFixedSize(true);



    }

}

