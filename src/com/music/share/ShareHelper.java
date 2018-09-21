package com.music.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.lu.library.Constant;
import com.lu.library.util.AsyncTaskUtil;
import com.lu.library.util.BitmapUtil;
import com.lu.library.util.DebugLog;
import com.lu.library.util.FileUtil;
import com.lu.library.util.NetUtils;
import com.lu.library.util.ScreenUtils;
import com.lu.library.util.ToastUtil;
import com.music.lu.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * @author: lyw
 * @package: com.ido.veryfitpro.module.home
 * @description: ${TODO}{分享辅助类}
 * @date: 2016/10/9 15:09
 */
public class ShareHelper implements OnShareSelectListener, OnShareListener {
    private Activity activity;
    volatile boolean isShotComplete = false;

    public ShareHelper(Activity activity) {
        this.activity = activity;
    }


    public static final int SHARE_TYEP_TWITTER = 1;
    public static final int SHARE_TYEP_FACEBOOK = 2;
    public static final int SHARE_TYEP_WHATSAPP = 3;
    public static final int SHARE_TYEP_INSTAGRAM = 4;
    public static final int SHARE_TYEP_LINKEDIN = 5;
    public static final int SHARE_TYEP_FLICKR = 6;
    public static final int SHARE_TYEP_EMAIL = 7;
    public static final int SHARE_TYEP_WECHAT = 8;
    public static final int SHARE_TYEP_QQ = 9;
    public static final int SHARE_TYEP_FIRENDS = 10;
    public static final int SHARE_TYEP_KAKAOTALK = 11;
    public static final int SHARE_TYEP_LINE = 12;
    public static final int SHARE_TYEP_VK = 13;


    private static final String SHARE_TEXT = "VeryFitPro";
    private static final String SHARE_TITLE = "VeryFitPro";
    public static String path = Constant.APP_ROOT_PATH + "/" + "s_ido.png";


    static class PlatformActionListenerAdapter implements PlatformActionListener {
        OnShareListener listener;
        int type;

        public PlatformActionListenerAdapter(OnShareListener listener, int type) {
            this.listener = listener;
            this.type = type;
        }

        @Override
        public void onError(Platform platform, int action, Throwable exception) {
            // TODO Auto-generated method stub
            DebugLog.d("shareTo  " + type + "--------------------onError");
//            ToastUtil.showToast(R.string.share_fail);
            if (listener != null) {
                listener.onError(type);
            }
        }

        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
            // TODO Auto-generated method stub
            DebugLog.d("shareTo " + type + "--------------------onComplete");
//            ToastUtil.showToast(R.string.sharing_success);
            if (listener != null) {
                listener.onComplete(type);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            // TODO Auto-generated method stub
            DebugLog.d("shareTo " + type + "--------------------onCancel");
            if (listener != null) {
                listener.onCancel(type);
            }
        }
    }

    /**
     * 截屏，保存到SD卡
     * 截取activity屏幕
     * 由于保留需要耗时，因此放在后台线程
     */
    public void shot() {
        FileUtil.deleteFile(path);
        isShotComplete = false;
        final Bitmap bitmap = ScreenUtils.takeScreenShot(activity);
        new AsyncTaskUtil(new AsyncTaskUtil.AsyncTaskCallBackAdapter() {
            @Override
            public Object doInBackground(String... arg0) {
                BitmapUtil.saveBitmap(bitmap, path);
                isShotComplete = true;
                return null;
            }
        }).execute("");

    }

    /**
     * 包含scrollview的
     * 截长屏，保存到SD卡
     * 由于保留需要耗时，因此放在后台线程
     */
    public void shotLongScreen(final ViewGroup viewContainer, final View... views) {
        FileUtil.deleteFile(path);
        isShotComplete = false;

        new AsyncTaskUtil(new AsyncTaskUtil.AsyncTaskCallBackAdapter() {
            @Override
            public Object doInBackground(String... arg0) {
                Bitmap bitmap = ScreenUtils.getTotleScreenShot(viewContainer, views);
                BitmapUtil.saveBitmap(bitmap, path);
                isShotComplete = true;
                return null;
            }
        }).execute("");

    }


    private void share(int type) {
        while (!isShotComplete) {
            try {
                Thread.sleep(10);
                DebugLog.d("还在截屏中 isShotComplete:" + isShotComplete);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        Platform.ShareParams params = null;
        switch (type) {

            case SHARE_TYEP_WECHAT:
                platform = ShareSDK.getPlatform(Wechat.NAME);
                params = new cn.sharesdk.wechat.friends.Wechat.ShareParams();
                params.setTitle("VeryFitPro");
                params.setText("text");
                break;
            case SHARE_TYEP_QQ:
                platform = ShareSDK.getPlatform(QQ.NAME);
                params = new cn.sharesdk.tencent.qq.QQ.ShareParams();
                break;
            case SHARE_TYEP_FIRENDS:
                platform = ShareSDK.getPlatform(WechatFavorite.NAME);
                params = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
                params.setTitle("VeryFitPro");
                params.setText("text");
                break;

        }
//        params.setShareType(Platform.SHARE_WEBPAGE);
//        params.setText(SHARE_TEXT);
        params.setImagePath(path);
        platform.share(params);

    }


    @Override
    public void onShareSelect(int shareType) {
        if (NetUtils.isConnected(activity)) {
            share(shareType);
        } else {
//            ToastUtil.showToast(R.string.network_error);
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(int shareType) {
        switch (shareType) {
            case SHARE_TYEP_QQ:
                ToastUtil.showToast(R.string.share_error_qq);
                break;
            case SHARE_TYEP_WECHAT:
            case SHARE_TYEP_FIRENDS:
                break;
            case SHARE_TYEP_FACEBOOK:
                break;
            case SHARE_TYEP_TWITTER:
                break;
            case SHARE_TYEP_INSTAGRAM:
                break;
            case SHARE_TYEP_WHATSAPP:
                break;
            case SHARE_TYEP_LINKEDIN:
                break;
            case SHARE_TYEP_EMAIL:
                break;
            case SHARE_TYEP_FLICKR:
                break;

            default:
                break;
        }
    }

    @Override
    public void onComplete(int shareType) {
        switch (shareType) {
            case SHARE_TYEP_QQ:
                break;
            case SHARE_TYEP_WECHAT:
            case SHARE_TYEP_FIRENDS:

                break;
            case SHARE_TYEP_FACEBOOK:
                break;
            case SHARE_TYEP_TWITTER:
                break;
            case SHARE_TYEP_INSTAGRAM:
                break;
            case SHARE_TYEP_WHATSAPP:
                break;
            case SHARE_TYEP_LINKEDIN:
                break;
            case SHARE_TYEP_EMAIL:
                break;
            case SHARE_TYEP_FLICKR:
                break;

            default:
                break;
        }
    }

    @Override
    public void onCancel(int shareType) {

    }
}