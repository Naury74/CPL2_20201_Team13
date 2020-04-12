package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.Waiting.Senior_MainActivity;
import com.example.kioskmainpage.R;

public class RecyclerAdapter_MenuType extends RecyclerView.Adapter<RecyclerAdapter_MenuType.MyViewHolder> {
    String []arr;
    int    []img;

    Context context;

    public RecyclerAdapter_MenuType(String[] arr,int[] img) {
        this.arr = arr;
        this.img=img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_easymenu,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.textView.setText(arr[i]);
        viewHolder.ImageView.setImageResource(img[i]);
    }

    @Override
    public int getItemCount() {
        return arr.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView ImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.menu_type);
            ImageView=itemView.findViewById(R.id.menu_type_icon);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int position=getAdapterPosition();
                    Intent intent = new Intent(context, Senior_MainActivity.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }
            });
        }
    }
}
