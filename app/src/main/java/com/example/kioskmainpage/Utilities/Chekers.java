package com.example.kioskmainpage.Utilities;

import android.os.Build;

import java.util.ArrayList;

public class Chekers {

    final static String DEVICES_PLUS_LIST[]=new String[]{"Camel CT2210IPS"};
    boolean check_reader (String reader_name)
    {

        return false;
    }

    public static String check_device_type(){
        String MODEL;
        MODEL = Build.MODEL;
        String TYPE = "NORMAL";
        for(String DEVICES_PLUS :DEVICES_PLUS_LIST){
            if(MODEL.equals(DEVICES_PLUS))
            {
                TYPE = "PLUS";
                break;
            }
        }
        return TYPE;
    }
}
