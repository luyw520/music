package com.music.utils;

import android.app.Activity;

import com.lu.library.util.DebugLog;
import com.music.helpers.ShareUtils;
import com.music.lu.R;

public class ShareListener {
    private Activity activity;
    private boolean isScreenShotComplete = true;//是否截完图
    private int type = -1;
    public String path = "";
    /**
     * 百度事件Id
     */
    private static final String EVENT_ID_FB="A9";
    private static final String EVENT_ID_TW="A10";
    private static final String EVENT_ID_WX="A11";
    private static final String EVENT_ID_IN="A12";
    private static final String EVENT_ID_WT="A13";
    private static final String EVENT_ID_QQ="A14";
    private static final String EVENT_ID_WXP="A15";
    public ShareListener(Activity activity, boolean isScreenShotComplete) {
        this.activity = activity;
        this.isScreenShotComplete = isScreenShotComplete;
    }

    public boolean isScreenShotComplete() {
        return isScreenShotComplete;
    }

    public void setIsScreenShotComplete(boolean isScreenShotComplete){
        this.isScreenShotComplete = isScreenShotComplete;
    }

    public void share(String path) {
        this.path = path;
        shareImage(type);
    }

    public void shareImageSelect(int type){
        this.type = type;
        if (isScreenShotComplete) {
            shareImage(type);
        }
    }

    public void shareImageSelect(int type,String path) {
        this.type = type;
        this.path = path;
        if (isScreenShotComplete) {
            shareImage(type);
        }
    }

    public void select(int type, String url) {
//        shareUrl(type, url);
    }

    private void shareImage(int type) {
        switch (type) {

            case 0://wechat
                ShareUtils.shareToWeChat(path, new ShareUtils.OnShareListener() {
                    @Override
                    public void onError(int shareType) {
                        DebugLog.i("分享失败");
                        ToastUtil.showCustomToast(activity, R.string.sharing_fail);
                    }

                    @Override
                    public void onComplete(int shareType) {
                        DebugLog.i("分享成功");
                        ToastUtil.showCustomToast(activity, R.string.sharing_success);
                    }

                    @Override
                    public void onCancel(int shareType) {
                        DebugLog.i("分享取消");
                        ToastUtil.showCustomToast(activity, R.string.sharing_cancel);
                    }
                });
                break;
            case 1://qq
                ShareUtils.shareToQQ(path, new ShareUtils.OnShareListener() {
                    @Override
                    public void onError(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_fail);
                    }

                    @Override
                    public void onComplete(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_success);
                    }

                    @Override
                    public void onCancel(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_cancel);
                    }
                });
                break;
            case 2://朋友圈
                ShareUtils.shareToFirends(path, new ShareUtils.OnShareListener() {
                    @Override
                    public void onError(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_fail);
                    }

                    @Override
                    public void onComplete(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_success);
                    }

                    @Override
                    public void onCancel(int shareType) {
                        ToastUtil.showCustomToast(activity, R.string.sharing_cancel);
                    }
                });
                break;
        }
    }


}
