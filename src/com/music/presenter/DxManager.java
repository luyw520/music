package com.music.presenter;

import android.content.Context;

import com.music.utils.DeBug;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * Created by lyw on 2017/9/23.
 */

public class DxManager {
    private Context mContext;
    public DxManager(Context context){
        mContext=context;
    }

    public void loadDex(File dexFilePath){
        File outFile=new File(mContext.getCacheDir(),dexFilePath.getName());
        if (outFile.exists()){
            outFile.delete();
        }

        try {
            DexFile dexFile=DexFile.loadDex(dexFilePath.getAbsolutePath(),outFile.getAbsolutePath(),Context.MODE_PRIVATE);
            Enumeration<String> entry=dexFile.entries();
            while (entry.hasMoreElements()){
                String className=entry.nextElement();

                //找到出BUG的class
                Class<?> realClass=dexFile.loadClass(className,mContext.getClassLoader());
                DeBug.d(this,"找到类............"+className);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
