package com.music.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lu.library.util.DebugLog;
import com.music.lu.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * @author: sslong
 * @package: com.veryfit.
 * @description: ${TODO}{一句话描述该类的作用}
 * @date: 2016/6/8 10:53
 */
public class ShareUtils {

    public static final int SHARE_TYEP_WECHAT = 8;
    public static final int SHARE_TYEP_QQ = 9;
    public static final int SHARE_TYEP_FIRENDS = 10;

    private static final String SHARE_TEXT = "MUSIC";



    /**
     * 分享微信
     *
     * @param path
     */
    public static void shareToWeChat(String path, final OnShareListener listener) {
        cn.sharesdk.wechat.friends.Wechat.ShareParams params = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
        params.setShareType(Platform.SHARE_IMAGE);
        params.setImagePath(path);
        params.setAuthor(SHARE_TEXT);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int action, Throwable exception) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToWeChat--------------------onError:"+exception.toString());
//                DialogUtil.showToast(R.string.share_fail);
                if (listener != null) {
                    listener.onError(SHARE_TYEP_WECHAT);
                }
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToWeChat--------------------onComplete");
//                DialogUtil.showToast(R.string.sharing_success);
                if (listener != null) {
                    listener.onComplete(SHARE_TYEP_WECHAT);
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToWeChat--------------------onCancel");
                if (listener != null) {
                    listener.onCancel(SHARE_TYEP_WECHAT);
                }
            }
        }); // 设置分享事件回调
        platform.share(params);
    }


    /**
     * 分享微信
     *
     */
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 分享QQ
     *
     * @param path
     */
    public static void shareToQQ(String path, final OnShareListener listener) {

        cn.sharesdk.tencent.qq.QQ.ShareParams params = new cn.sharesdk.tencent.qq.QQ.ShareParams();
        params.setImagePath(path);

        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int action, Throwable exception) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToQQ--------------------onError:"+exception.toString());
//                DialogUtil.showToast(R.string.share_fail);
                if (listener != null) {
                    listener.onError(SHARE_TYEP_QQ);
                }
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToQQ--------------------onComplete");
//                DialogUtil.showToast(R.string.sharing_success);
                if (listener != null) {
                    listener.onComplete(SHARE_TYEP_QQ);
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToQQ--------------------onCancel");
                if (listener != null) {
                    listener.onCancel(SHARE_TYEP_QQ);
                }
            }
        }); // 设置分享事件回调
        platform.share(params);
    }

    /**
     * 分享QQ
     *
     * @param activity
     */

    /**
     * 分享朋友圈
     *
     * @param path
     */
    public static void shareToFirends(String path, final OnShareListener listener) {
//        File deletefile = new File(path);
//        deletefile.delete();
//        ScreenShot.shoot(activity);
        cn.sharesdk.wechat.moments.WechatMoments.ShareParams params = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
        // params.setShareType(Platform.SHARE_TEXT|Platform.SHARE_IMAGE);
        params.setShareType(Platform.SHARE_IMAGE);
        params.setImagePath(path);
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int action, Throwable exception) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments-------------------onError:"+exception.toString());
//                DialogUtil.showToast(R.string.share_fail);
                if (listener != null) {
                    listener.onError(SHARE_TYEP_FIRENDS);
                }
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments--------------------onComplete");
//                DialogUtil.showToast(R.string.sharing_success);
                if (listener != null) {
                    listener.onComplete(SHARE_TYEP_FIRENDS);
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments--------------------onCancel");
                if (listener != null) {
                    listener.onCancel(SHARE_TYEP_FIRENDS);
                }
            }
        }); // 设置分享事件回调
        platform.share(params);
    }

    /**
     * 分享朋友圈
     *
     * @param activity
     */
    public static void shareToFirends(final Activity activity ,String url, final OnShareListener listener) {

        cn.sharesdk.wechat.moments.WechatMoments.ShareParams params = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setImageData(BitmapFactory.decodeResource(activity.getResources(), R.drawable.playing_bar_default_avatar));
        params.setUrl(url);
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int action, Throwable exception) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments--------------------onError:"+exception.toString());
//                DialogUtil.showToast(R.string.share_fail);
                if (listener != null) {
                    listener.onError(SHARE_TYEP_FIRENDS);
                }
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments--------------------onComplete");
//                DialogUtil.showToast(R.string.sharing_success);
                if (listener != null) {
                    listener.onComplete(SHARE_TYEP_FIRENDS);
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                // TODO Auto-generated method stub
                DebugLog.d("shareToMoments--------------------onCancel");
                if (listener != null) {
                    listener.onCancel(SHARE_TYEP_FIRENDS);
                }
            }
        }); // 设置分享事件回调
        platform.share(params);
    }





    /**
     * 分享Linkedin
     */
    public interface OnShareListener {
        void onError(int shareType);

        void onComplete(int shareType);

        void onCancel(int shareType);
    }

}
