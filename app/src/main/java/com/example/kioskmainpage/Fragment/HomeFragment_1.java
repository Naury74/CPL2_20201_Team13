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

import org.w3c.dom.Text;

public class HomeFragment_1 extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_fragment_1, container, false);

        TextView HomeFragment_1_text = (TextView) v.findViewById(R.id.HomeFragment_1_text);
        int red = getResources().getColor(R.color.rounded_red);
        Spannable span= (Spannable) HomeFragment_1_text.getText();
        span.setSpan(new ForegroundColorSpan(red), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span.setSpan(new StyleSpan(Typeface.BOLD), 0,  9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
        span.setSpan(new ForegroundColorSpan(red), HomeFragment_1_text.getText().length()-26, HomeFragment_1_text.getText().length()-10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//색깔처리
        span.setSpan(new StyleSpan(Typeface.BOLD), HomeFragment_1_text.getText().length()-26, HomeFragment_1_text.getText().length()-10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리

        // Inflate the layout for this fragment
        return v;
    }

}
