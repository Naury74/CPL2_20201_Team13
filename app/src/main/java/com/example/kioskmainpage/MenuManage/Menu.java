package com.example.kioskmainpage.MenuManage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Menu implements Parcelable {
    public String menu_name;
    public String menu_price;
    public String menu_folder;
    public String menu_ment;
    public String bitmap;
    public ArrayList<Option> options = new ArrayList<>();

    public int menu_price_final;

    public Menu() {
    }

    public Menu(String menu_name, String menu_price, String menu_folder, String menu_ment,String bitmap, ArrayList<Option> options) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_folder = menu_folder;
        this.menu_ment = menu_ment;
        this.bitmap = bitmap;
        this.options = options;

        menu_price_final = Integer.parseInt(menu_price);
    }

    public Menu(String menu_name, String menu_price, String menu_folder, String bitmap, ArrayList<Option> options) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_folder = menu_folder;
        this.bitmap = bitmap;
        this.options = options;

        menu_price_final = Integer.parseInt(menu_price);
    }
    public Menu(String menu_name, String menu_price,int menu_price_final, String menu_folder, String bitmap, ArrayList<Option> options) {
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.menu_folder = menu_folder;
        this.bitmap = bitmap;
        this.options = options;

        this.menu_price_final = menu_price_final;
    }
    public String getMenu_ment() {
        return menu_ment;
    }

    public void setMenu_ment(String menu_ment) {
        this.menu_ment = menu_ment;
    }

    public String getMenu_folder() {
        return menu_folder;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getMenu_price() {
        return menu_price;
    }

    public String getBitmap() {
        return bitmap;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }


    public int getMenu_price_final() {
        return menu_price_final;
    }

    public void setMenu_price_final(int price_final) {
        this.menu_price_final = price_final;
    }


    public Menu(Parcel in) {
        menu_name = in.readString();
        menu_price = in.readString();
        menu_folder = in.readString();
        bitmap = in.readString();
        in.readTypedList(options, Option.CREATOR);

    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

    public void setMenu_price(String menu_price) {
        this.menu_price = menu_price;
        setMenu_price_final(Integer.parseInt(menu_price));
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setMenu_folder(String menu_folder) {
        this.menu_folder = menu_folder;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(menu_name);
        dest.writeString(menu_price);
        dest.writeString(menu_folder);
        dest.writeString(bitmap);
        dest.writeTypedList(options);

    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };
}
