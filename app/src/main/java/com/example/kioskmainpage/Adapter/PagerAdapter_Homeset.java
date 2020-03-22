package com.example.kioskmainpage.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.kioskmainpage.Activity.Admin.Admin_home_popupActivity;

import com.example.kioskmainpage.Fragment.HomeFragment_1;
import com.example.kioskmainpage.Fragment.HomeFragment_2;
import com.example.kioskmainpage.Fragment.HomeFragment_3;

public class PagerAdapter_Homeset extends FragmentStatePagerAdapter{
    int mNumOfTabs;

    public PagerAdapter_Homeset(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment_1 tab1 = new HomeFragment_1();
                return tab1;
            case 1:
                HomeFragment_2 tab2 = new HomeFragment_2();
                return tab2;
            case 2:
                HomeFragment_3 tab3 = new HomeFragment_3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}