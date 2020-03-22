package com.example.kioskmainpage.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kioskmainpage.R;


public class HomeFragment_2 extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_fragment_2, container, false);

        int red = getResources().getColor(R.color.rounded_red);
        TextView HomeFragment_2_text = (TextView) v.findViewById(R.id.HomeFragment_2_text);
        Spannable span= (Spannable) HomeFragment_2_text.getText();
        span.setSpan(new ForegroundColorSpan(red), HomeFragment_2_text.getText().length()-21, HomeFragment_2_text.getText().length()-9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span.setSpan(new StyleSpan(Typeface.BOLD), HomeFragment_2_text.getText().length()-21, HomeFragment_2_text.getText().length()-9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        return v;
    }

}
