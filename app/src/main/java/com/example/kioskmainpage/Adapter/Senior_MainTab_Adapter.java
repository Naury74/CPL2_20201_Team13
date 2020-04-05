package com.example.kioskmainpage.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kioskmainpage.Fragment.Senior_Tab_Fragment;

public class Senior_MainTab_Adapter extends FragmentStatePagerAdapter {

    int mNumOfTabs = 4;
    private String tabTitles[] = new String[]{"커피","음료","빵","기타"};
    private Context context;

    public Senior_MainTab_Adapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return Senior_Tab_Fragment.newinstance(position+1);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
