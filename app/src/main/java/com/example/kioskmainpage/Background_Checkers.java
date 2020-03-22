package com.example.kioskmainpage;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kioskmainpage.Activity.ReaderUnavailableActivitiy;
import com.example.kioskmainpage.Activity.Sign.SignInActivity2;
import com.example.kioskmainpage.Activity.SplashActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class Background_Checkers {
    String TAG = "Background_Checkers";
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Context context;
    Set<BluetoothDevice> mDevices = mBluetoothAdapter.getBondedDevices();
    ArrayList<String> bluetoothList = new ArrayList<>();
    Myapplication myapp;
    BroadcastReceiver bluetoothReceiver;
    BluetoothDevice lastconnected_devide;
    public Background_Checkers(Context context, Myapplication myapp) {
        this.context = context;
        this.myapp = myapp;
        myapp.setBackground_checkers(this);
        createReceiver();
        set_broadcastReceiver();
        Log.d(TAG, "Background_Checkers: init");
    }


    public void Check_reader_state(){
        Log.d(TAG, "Check_reader_state: start");
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> devices = bm.getConnectedDevices(BluetoothGatt.GATT);
        int status = -1;

        for (BluetoothDevice device : devices) {
            status = bm.getConnectionState(device, BluetoothGatt.GATT);
            String checked ="";
            checked += device.getName();
            if(device.getName().substring(1,3).equals("IN"))
            switch (status)
            {
                case BluetoothProfile.STATE_CONNECTED:
                    checked += "STATE_CONNECTED";
                    if(getbonded_Devices().equals(device.getName())){
                        myapp.setReader_status(true);
                        lastconnected_devide = device;
                    }
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    checked += "STATE_CONNECTING";

                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    checked += "STATE_DISCONNECTED";
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    checked += "STATE_DISCONNECTING";
                    break;
                default:
                    checked = "연결된거 없음";
            }
            else
            {
                    checked += "연결된거 없음";
            }
            // compare status to:
            //   BluetoothProfile.STATE_CONNECTED
            //   BluetoothProfile.STATE_CONNECTING
            //   BluetoothProfile.STATE_DISCONNECTED
            //   BluetoothProfile.STATE_DISCONNECTING
            Log.d(TAG, "Check_reader_state: "+ checked);
        }
    }
    public static boolean isConnected(BluetoothDevice device) {
        try {
            Method m = device.getClass().getMethod("isConnected", (Class[]) null);
            boolean connected = (boolean) m.invoke(device, (Object[]) null);
            return connected;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    private void createReceiver()
    {
        bluetoothReceiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                List<BluetoothDevice> devices = bm.getConnectedDevices(BluetoothGatt.GATT);
                    if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        if(devices.size() == 0)
                        {
                            //  페어링 된 장치가 없는 경우
                            Toast.makeText(context, "페어링된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                            myapp.setReader_status(false);
                            lastconnected_devide=null;
                        }else{
                            //  페어링 된 장치가 있는 경우
                            Toast.makeText(context, "페어링된 기기가 있습니다.", Toast.LENGTH_SHORT).show();
                            for(BluetoothDevice device :mDevices) //인피닉스제품인지 확인.
                            {
                                bluetoothList.add(device.getName());
                                Log.d(device.getName(), device.getAddress());
                                //앱포스 듀얼 제품이라면 getname의 2번째 글자부터3번째까지 IN임
                                if(device.getName().substring(1,3).equals("IN")) {
                                    if(getbonded_Devices().equals(device.getName())){
                                        myapp.setReader_status(true);
                                        lastconnected_devide=device;
                                    }
                                }
                            }
                        }
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        myapp.setReader_status(false);
                        lastconnected_devide=null;
                        Log.d(TAG, "카드 리더기가 연결해제되었습니다.");
                        if(null==myapp.getReaderUnavailableActivitiyContext()) {
                            Intent intent_go_reader_unavailable = new Intent(context, ReaderUnavailableActivitiy.class);
                            intent_go_reader_unavailable.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                            intent_go_reader_unavailable.addFlags( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
                            intent_go_reader_unavailable.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent_go_reader_unavailable);
                        }
                    }
            }
        };
    }
    public BroadcastReceiver getBroadcastReceiver(){return this.bluetoothReceiver;}
    public void call_reader_config(Context c)
    {
        String uri = String.format("appposw://reader-config");
        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent1.addCategory(Intent.CATEGORY_DEFAULT);
        intent1.addCategory(Intent.CATEGORY_BROWSABLE);
        intent1.setData(Uri.parse(uri));
        (c).startActivity(intent1);
    }


    public void set_broadcastReceiver(){
        //broadcastReceiver 설정
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        myapp.getApplication().registerReceiver(bluetoothReceiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        myapp.getApplication().registerReceiver(bluetoothReceiver, filter);
    }
    public void unreregister_broadcastReceiver(){
        myapp.getApplication().unregisterReceiver(bluetoothReceiver);
    }
    public void go_unavailable_popup(){
        Intent intent_go_reader_unavailable = new Intent(context, ReaderUnavailableActivitiy.class);
        intent_go_reader_unavailable.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent_go_reader_unavailable.addFlags( FLAG_ACTIVITY_REORDER_TO_FRONT);//기존의 액티비티를 재사용
        context.startActivity(intent_go_reader_unavailable);
    }
    public String getDevice_name() {
        if(lastconnected_devide!=null)
        return lastconnected_devide.getName();
        else
            return "";
    }

    public String getbonded_Devices() {
        Set<BluetoothDevice>   bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        String result="";
        for(BluetoothDevice device :bondedDevices) //인피닉스제품인지 확인.
        {
            bluetoothList.add(device.getName());
            Log.d(device.getName(), device.getAddress());
            //앱포스 듀얼 제품이라면 getname의 2번째 글자부터3번째까지 IN임
            if(device.getName().substring(1,3).equals("IN")) {
                result = device.getName();
            }
        }
        return result;
    }

    public String getconnected_Devices() {
        String result="";
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> devices = bm.getConnectedDevices(BluetoothGatt.GATT);
        int status = -1;

        for (BluetoothDevice device : devices) {
            status = bm.getConnectionState(device, BluetoothGatt.GATT);
            if (device.getName().substring(1, 3).equals("IN"))
                switch (status) {
                    case BluetoothProfile.STATE_CONNECTED:
                        result = device.getName();
                        break;
                }
        }
        return result;
    }
}
