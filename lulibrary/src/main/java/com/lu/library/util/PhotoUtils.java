package com.lu.library.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.UUID;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.lu.library.util
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/22 0022
 */

public class PhotoUtils {

    private static final String IMAGE_PATH = "";


    public static final int REQUEST_CODE_ACTION_PICK = 0;
    public static final int REQUEST_CODE_ACTION_IMAGE_CAPTURE = 1;
//    public static final int INTENT_REQUEST_CODE_CROP = 2;
//    public static final int INTENT_REQUEST_CODE_FLITER = 3;
//    public static final String HEADER_PATH="";

    /**
     *选择相册
     * @param activity
     */
    public static void selectPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_ACTION_PICK);
    }

    /**
     * 拍照
     * @param activity
     * @param dir      拍照文件存储目录
     * @param fileName 拍照文件名 如果没有则随机生成
     */
    public static String takePicture(Activity activity,String dir,String fileName) {
        StringBuffer stringBuffer=new StringBuffer();
        if (!TextUtils.isEmpty(fileName)){
            stringBuffer.append(fileName);
        }else{
            stringBuffer.append(UUID.randomUUID().toString());
        }
        stringBuffer.append(".jpg");
        File file = new File(dir,stringBuffer.toString());
        Uri cacheImageUri;
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cacheImageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            cacheImageUri = Uri.fromFile(file);
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cacheImageUri);//将拍取的照片保存到指定URI
        activity.startActivityForResult(intent, REQUEST_CODE_ACTION_IMAGE_CAPTURE);
        return file.getAbsolutePath();
    }










}
