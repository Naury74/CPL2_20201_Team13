package com.example.kioskmainpage;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class MainTheme {

    private String theme_name;

    private ThemeItem themeItem;

    public MainTheme(ThemeItem themeItem)
    {
        this.themeItem = themeItem;
    }
    public MainTheme(String theme_name) {
        this.theme_name = theme_name;
        this.themeItem = new ThemeItem(theme_name);

    }

    public ThemeItem getThemeItem() {
        return themeItem;
    }

    public void setThemeItem(ThemeItem themeItem) {
        this.themeItem = themeItem;
    }


    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }


}
