package com.example.kioskmainpage.Utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class DeleteFiles {
    String TAG = "DeleteFiles";
    String path;
    public DeleteFiles(String path){
        this.path=path;
    }
    public void setDirEmpty(String dirName) {
        String sub_path = path + dirName;
        Log.d(TAG, "setDirEmpty: "+sub_path);
        File dir = new File(sub_path);
        File[] childFileList = dir.listFiles();
        if (dir.exists()) {
            for (File childFile : childFileList) {
                if (childFile.isDirectory()) {
                    Log.d(TAG, "setDirEmpty: "+childFile.getName()+" 진입.");
                    setDirEmpty(dirName+"/"+childFile.getName());
                } else {
                    Log.d(TAG, "setDirEmpty: "+childFile.getName()+" 파일 제거됨.");
                    childFile.delete();
                }
            }
            Log.d(TAG, "setDirEmpty: "+dir.getName()+" 폴더 제거됨.");
            dir.delete();
        }
    }

}
