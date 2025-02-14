package com.example.kioskmainpage.MenuManage;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Option implements Parcelable {
    String option_name;
    ArrayList<String> options;
    ArrayList<String> options_price;

    public Option(String option_name, ArrayList<String> options, ArrayList<String> options_price){
        this.option_name = option_name;
        this.options = options;
        this.options_price = options_price;
    }

    public ArrayList<String> getOptions_price() {
        return options_price;
    }

    public void setOptions_price(ArrayList<String> options_price) {
        this.options_price = options_price;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public Option(Parcel in){
        option_name = in.readString();
        options = in.readArrayList(null);
        options_price = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(option_name);
        dest.writeList(options);
        dest.writeList(options_price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}
