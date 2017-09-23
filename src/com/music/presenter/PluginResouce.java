package com.music.presenter;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by lyw on 2017/9/23.
 */

public class PluginResouce extends Resources {
    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     * @deprecated Resources should not be constructed by apps.
     * See {@link Context#createConfigurationContext(Configuration)}.
     */
    public PluginResouce(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }
    public static AssetManager getPluginAssetManager(File apkFile){
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method[] methods=assetManager.getClass().getMethods();
            for (Method method:methods){
                if (method.getName().equals("addAssetPath")){
                    method.invoke(assetManager,apkFile.getAbsolutePath());
                    return assetManager;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static PluginResouce getPluginResouce(Resources resources,AssetManager assetManager){
        PluginResouce pluginResouce=new PluginResouce(assetManager,resources.getDisplayMetrics(),resources.getConfiguration());
        return pluginResouce;
    }
}
