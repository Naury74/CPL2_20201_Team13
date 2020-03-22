package com.example.kioskmainpage.MenuManage;

import android.os.Parcelable;

import java.util.ArrayList;

public class SelectedMenu extends Menu implements Parcelable { //메뉴를 터치해 옵션을 고른후 선택버튼을 눌렀을 때 하단에 표시할 메뉴 객체를 생성하기위함
    private int cnt;
    private ArrayList<Integer> choices;
    private String category;
    private int price_sum,price_one;

    public SelectedMenu()
    {

    }
    public SelectedMenu(String menuName, String menuPrice,int menu_price_final, String bitmap, String category, ArrayList<Option> options, ArrayList<Integer> choices) {
        super(menuName, menuPrice,menu_price_final, category, bitmap, options);
        this.choices = choices;
        this.category = category;
        cnt = 1;
    }

    public int getPrice_one() {

        int sum=0;
        for(int i=0;i<getOptions().size();i++)
        {
            sum += Integer.parseInt(getOptions().get(i).getOptions_price().get(getChoices().get(i)));
        }
        price_one = (Integer.parseInt(getMenu_price())  + sum);
        return price_one;
    }


    public int getPrice_sum() {

        this.price_sum = getPrice_one() * getCnt();
        return price_sum;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setChoices(ArrayList<Integer> choices) {
        this.choices = choices;
    }

    public int getCnt() {
        return cnt;
    }

    @Override
    public ArrayList<Option> getOptions() {
        return super.getOptions();
    }

    @Override
    public String getMenu_folder() {
        return super.getMenu_folder();
    }

    @Override
    public String getBitmap() {
        return super.getBitmap();
    }

    @Override
    public String getMenu_name() {
        return super.getMenu_name();
    }

    @Override
    public String getMenu_price() {
        return super.getMenu_price();
    }

    @Override
    public int getMenu_price_final() {
        return super.getMenu_price_final();
    }

    public ArrayList<Integer> getChoices() {
        return choices;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

}
