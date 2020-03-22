package com.example.kioskmainpage.MenuManage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.kioskmainpage.Myapplication;
import com.example.kioskmainpage.ServerConn.DownloadUnzip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ErrorManager;

public class MenuManager {
    ArrayList<Menu> menusforbest = new ArrayList<>();
    ArrayList<Menu> menusfornew = new ArrayList<>();

    ArrayList<String> menu_names = new ArrayList<>();
    ArrayList<String> menu_prices = new ArrayList<>();
    ArrayList<String> menu_images = new ArrayList<>();
    ArrayList<String> menu_ments = new ArrayList<>();
    String menu_folder;

    String saveDir;
    String menu_name_path = "/menu_name.txt";
    String menu_price_path = "/menu_price.txt";
    String menu_folder_path = "/category_name.txt";
    String menu_option_path = "/menu_option.txt";
    String menu_option_price_path = "/menu_option_price.txt";
    String menu_ment_path="/best_menu_ment.txt";

    ArrayList<String> parsedOption;
    ArrayList<String> parsedOption_price;
    ArrayList<String> tabNames = new ArrayList<>();
    ArrayList<String> folder_names;

    String bizNum;
    private static final String TAG = "menumanagertesttest";

   // private boolean isBestNew=false;

    public MenuManager(String saveDir, ArrayList<String> folder_names,String bizNum) {

        this.bizNum = bizNum;
        this.saveDir = saveDir+"/"+bizNum;
        this.folder_names = folder_names;
        //this.isBestNew = isBestNew;

        make_all_menus();
    }


    //이미지의 순서가 제대로 나오지않는 현상수정을 위해서 소팅 부분 추가 - 이미지 주소 배열과 이미지 이름 배열을 따로만들어서 동시에 정렬함.
    public File[] q_sort(File imageFileNames[])
    {
        File[] a_sort = imageFileNames;
        int[] sortlist = new int[imageFileNames.length];
        //이미지 주소 갯수만큼 이미지 이름 배열을 제작.
        for(int i= 0;i<a_sort.length;i++) {
            // 이미지 이름(숫자)만 추출
            String temp = imageFileNames[i].toString().substring(imageFileNames[i].toString().length()-8).replaceAll("[^\\d]", "");
            sortlist[i] = Integer.parseInt(temp);
        }
        quickSort(sortlist,0,imageFileNames.length-1,a_sort);
        return a_sort;
    }

    //시간 단축을 위해서 quickSort를 사용하였음
    public void quickSort(int[] arr, int left, int right, File imageFileNames[]) {
        int i, j, pivot, tmp;
        File tmp_F;
        if (left < right) {
            i = left;   j = right;
            pivot = arr[(left+right)/2];
            //분할 과정
            while (i < j) {
                while (arr[j] > pivot) j--;
                // 이 부분에서 arr[j-1]에 접근해서 익셉션 발생가능함.
                while (i < j && arr[i] < pivot) i++;
                //숫자 배열 정렬
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                //이미지주소 정렬
                tmp_F = imageFileNames[i];
                imageFileNames[i] = imageFileNames[j];
                imageFileNames[j] = tmp_F;
            }
            //정렬 과정
            quickSort(arr, left, i - 1, imageFileNames);
            quickSort(arr, i + 1, right, imageFileNames);
        }
    }



    public void make_all_menus() { //모든 메뉴들에 대한 객체 생성

        for (int i = 0; i < folder_names.size(); i++) {

            System.out.println("best folder names in MENU MANAGER : "+folder_names.get(i));
            File save_file = new File(saveDir +"/" + folder_names.get(i));
            File image_folder = new File(saveDir + "/" + folder_names.get(i) + "/images");

            String line = null;
            if (!save_file.exists()) {
                Log.d(TAG, "save file not exist");
                return;
            }
            try {
                //메뉴이름 txt 파일 읽어오기
              //  FileInputStream fileInputStream = new FileInputStream(new File(saveDir + "/" + folder_names.get(i) + menu_name_path));
                BufferedReader br_name = new BufferedReader(new FileReader(save_file + menu_name_path));

                line = br_name.readLine();
                menu_names = new ArrayList<String>(Arrays.asList(line.split("\\|")));

                br_name.close();

                //메뉴가격 txt 파일 읽어오기
                BufferedReader br_price = new BufferedReader(new FileReader(save_file + menu_price_path));
                line = br_price.readLine();
                menu_prices = new ArrayList<String>(Arrays.asList(line.split("\\|")));
                br_price.close();

                //메뉴카테고리 txt 파일 읽어오기
            //    fileInputStream = new FileInputStream(new File(saveDir + "/" + folder_names.get(i) + menu_folder_path));//한글인코딩
                BufferedReader br_folder = new BufferedReader(new FileReader(save_file + menu_folder_path));
                line = br_folder.readLine();
                menu_folder = line;
                System.out.println("menu_folder : "+menu_folder);
                br_folder.close();
                tabNames.add(line);

                //메뉴옵션 txt 파일 읽어오기
                BufferedReader br_option = new BufferedReader(new FileReader(save_file + menu_option_path));
                line = br_option.readLine();
                if((line==null)==false)
                parsedOption = new ArrayList<String>(Arrays.asList(line.split("@")));
                else{
                    parsedOption = new ArrayList<String>();
                }
                br_option.close();

                //메뉴옵션가격 txt 파일 읽어오기
                BufferedReader br_option_price = new BufferedReader(new FileReader(save_file + menu_option_price_path));
                line = br_option_price.readLine();
                if((line==null)==false)
                    parsedOption_price = new ArrayList<String>(Arrays.asList(line.split("@")));
                else{
                    parsedOption_price = new ArrayList<String>();
                }
                br_option_price.close();

                //옵션이 없는경우 TXT로 부터 불러올때 옵션 Array가 add 되지 않기에 따로 추가함.
                if(parsedOption.size()<menu_names.size())
                {
                    while (menu_names.size()>parsedOption.size())
                    {
                        parsedOption.add("");
                        parsedOption_price.add("");
                    }
                }

                File imageFileNames[] = image_folder.listFiles();
                //이미지의 순서가 섞이는 현상을 막기위한 정렬
                imageFileNames = q_sort(imageFileNames);

                //이미지 파일은 저장 경로만 가지고있다가 이미지 버튼에 넣을 때 디코드
                for (int pos=0,file_pos=0;pos<menu_names.size();pos++,file_pos++) { //pos -> 진행순서  file_pos-> 파일순서
                    if(imageFileNames.length>file_pos) {
                        File f = imageFileNames[file_pos];
                        String f_name = f.getName().replaceAll("[^0-9]", "");
                        if (Integer.parseInt(f_name) == pos + 1) //이미지의 이름의 숫자와 저장되어야할 위치+1일경우 제대로 된자리에 들어가는것.
                        {
                            menu_images.add(f.getAbsolutePath());
                        } else//이미지의 이름의 숫자와 저장되어야할 위치+1이 아닐경우-> 이미지가 없음.
                        {
                            menu_images.add("No_Image");
                            file_pos--;
                        }
                        Log.d(TAG, "menu images file name : " + f_name);
                        Log.d(TAG, "menu menu_images path : " + menu_images.get(pos));
                        Log.d(TAG, "menu images file path : " + f.getAbsolutePath());
                    }
                    else
                    {
                        menu_images.add("No_Image");
                        Log.d(TAG, "menu images file name : " + "No_Image");
                        Log.d(TAG, "menu menu_images path : " + menu_images.get(pos));
                        Log.d(TAG, "menu images file path : " + "No_Image");
                    }

                }

                    if(menu_names.size()<1 || menu_images.size()<1 || menu_prices.size()<1 || menu_folder==null){
                    System.exit(ErrorManager.OPEN_FAILURE);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //옵션 txt파일 읽고 파싱해서 각 메뉴에 넣기
            for (int j = 0; j < menu_names.size(); j++) {
                ArrayList<String> oneOption = new ArrayList<String>(Arrays.asList(parsedOption.get(j).split("\\$")));
                ArrayList<String> oneOption_price = new ArrayList<String>(Arrays.asList(parsedOption_price.get(j).split("\\$")));
                Log.d(TAG, "oneOption size : " + oneOption.size());
                ArrayList<Option> oneMenuOptions = new ArrayList<Option>();
                ArrayList<Option> oneMenuOptions_price = new ArrayList<Option>();
                for (int k = 0; k < oneOption.size(); k++) {
                    //옵션이 없을경우 옵션부분을 파싱하지 않음
                    if(parsedOption.get(j).length()==0)
                        continue;

                    String option_name = oneOption.get(k).substring(0, oneOption.get(k).indexOf("&"));
                    String option_name_price = oneOption_price.get(k).substring(0, oneOption_price.get(k).indexOf("&"));
                    Log.d(TAG, "option name : " + option_name);
                    String entry = oneOption.get(k).substring(oneOption.get(k).indexOf("&") + 1);
                    String entry_price = oneOption_price.get(k).substring(oneOption_price.get(k).indexOf("&") + 1);
                    ArrayList<String> option_entry = new ArrayList<String>(Arrays.asList(entry.split("\\|")));
                    ArrayList<String> option_entry_price = new ArrayList<String>(Arrays.asList(entry_price.split("\\|")));
                    oneMenuOptions.add(new Option(option_name, option_entry, option_entry_price));
                }
                Menu menu = new Menu(menu_names.get(j), menu_prices.get(j), menu_folder, "", menu_images.get(j), oneMenuOptions);
                menusforbest.add(menu);
                menusfornew.add(menu);
                Log.d(TAG, menu.getMenu_name() + " " + menu.getMenu_price() + " " + menu.getMenu_folder());


            }
            menu_names.clear();
            menu_prices.clear();
            menu_images.clear();
            menu_ments.clear();
        }
    }

    public ArrayList<Menu> getMenus(String category, String menusfor) {
        ArrayList<Menu> result = new ArrayList<>();

        switch (menusfor)
        {
            case "NEW":
                for (int i = 0; i < menusfornew.size(); i++) {
                    Menu m = menusfornew.get(i);
                    if (m.menu_folder.equals(category))
                    {
                        Log.i("menu m : ",m.menu_name);
                        result.add(m);
                    }
                }
                break;
            case "BEST":
                for (int i = 0; i < menusforbest.size(); i++) {
                    Menu m = menusforbest.get(i);
                    if (m.menu_folder.equals(category))
                    {
                        Log.i("menu m : ",m.menu_name);
                        result.add(m);
                    }
                }
                break;
        }
        return result;
    }
    public ArrayList<Menu> getAllMenusforbest() {
       return menusforbest;
    }
    public ArrayList<Menu> getAllMenusfornew() {
        return menusfornew;
    }
    public ArrayList<String> getTabNames() {
        return tabNames;
    }


}
