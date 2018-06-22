package com.music.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.lu.library.util.file.FileUtils;
import com.music.MusicApplication;
import com.music.lu.R;
import com.music.utils.DeBug;
import com.lu.library.util.SPUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

import static com.music.utils.AppConstant.BG_INDEX_KEY;


/**
 * Created by lyw on 2017/9/23.
 * 切换播放界面背景
 */

public class ChangeSkinPresenter extends BasePresenter<IBaseView<Drawable>> {
    private ChangeSkinContract.View mChangeSkinView;
    private String apkDir= Environment.getExternalStorageDirectory().getAbsolutePath();
    private String apkName="wyy.apk";
    private String plugPackageName="com.example.lyw.wyy";

    public void start() {

    }
    private int checkedId;
    public void changeBg(Context context){
        int checkedIdTemp = (int) SPUtils.get( BG_INDEX_KEY,0);
        if (checkedIdTemp!=checkedId){
            checkedId=checkedIdTemp;
            AssetManager assetManager =context.getAssets();
            InputStream inputStream=null;
            try {
                String[] paths = assetManager.list("bgs");
                inputStream = assetManager.open("bgs/" + paths[checkedId]);
//				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Drawable drawable=Drawable.createFromResourceStream(context.getResources(), null, inputStream, "src", null);
//                mChangeSkinView.shoChangSkin(drawable);
                if (mViewRef!=null&&mViewRef.get()!=null){
                    mViewRef.get().onSuccess(drawable);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void changeSkin() {
        try {
            File apkFile=new File(filePath);
            if (!apkFile.exists()){
                copyApp();
            }

            AssetManager assetManager=PluginResouce.getPluginAssetManager(apkFile);
            Resources resources=PluginResouce.getPluginResouce(MusicApplication.getInstance().getResources(),assetManager);


            DexClassLoader dexClassLoader=new DexClassLoader(apkFile.getAbsolutePath(),MusicApplication.getInstance().getDir(apkName,Context.MODE_PRIVATE).getAbsolutePath(),null,ClassLoader.getSystemClassLoader());
            Class<?> clazz=dexClassLoader.loadClass(plugPackageName+".R$drawable");
            Field field=clazz.getDeclaredField("btn_play");
            int resId=field.getInt(R.drawable.class);
            DeBug.d(this,"resId:"+resId);
            Drawable drawable= resources.getDrawable(resId);


//            Drawable drawable=dynamicLoadApk(MusicApplication.getInstance(),apkDir,apkName,plugPackageName);
            mChangeSkinView.shoChangSkin(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String filePath;
    private void copyApp(){
        try {
            InputStream inputStream=MusicApplication.getInstance().getAssets().open("wyy.apk");
            File file=new File(MusicApplication.getInstance().getCacheDir(),apkName);
            FileUtils.writeFileFromIS(file,inputStream,false);
            filePath=file.getAbsolutePath();
            DeBug.d(this,"........wyy.apk写入SD卡成功:"+filePath);
        } catch (IOException e) {
            e.printStackTrace();
            DeBug.d(this,"........wyy.apk写入SD卡失败");
        }
    }
    private Drawable dynamicLoadApk(Context context, String apkDir, String apkName, String apkPackageName) throws Exception {
        File optmizedDir=context.getDir("dex",Context.MODE_PRIVATE);
        DeBug.d(this,"........"+optmizedDir.getAbsolutePath());
//        DexClassLoader dexClassLoader = new DexClassLoader(apkDir+File.separator+apkName, optimizedDirectoryFile.getPath(), null, ClassLoader.getSystemClassLoader());
       //获取类加载器 参数1 apk路径 参数2 apk解压后的路径 参数3 本地library 参数4 父类加载器
        DexClassLoader dexClassLoader=new DexClassLoader(apkDir+File.separator+apkName,optmizedDir.getPath(),null,ClassLoader.getSystemClassLoader());
        Class<?> clazz=dexClassLoader.loadClass(apkPackageName+".R$drawable");
        Field field=clazz.getDeclaredField("btn_play");
        int resId=field.getInt(R.id.class);
        Resources mResources=getPluginResources(context,apkPackageName);
        if (mResources!=null){
            return mResources.getDrawable(resId);
        }
        return null;
    }
    private Resources getPluginResources(Context context,String apkName) {

        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method addAAssetPath=assetManager.getClass().getMethod("addAssetPath",String.class);

            addAAssetPath.invoke(assetManager,apkDir+File.separator+apkName);
            Resources mainRes=context.getResources();
            Resources mResounce=new Resources(assetManager,mainRes.getDisplayMetrics(),mainRes.getConfiguration());
            return mResounce;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
