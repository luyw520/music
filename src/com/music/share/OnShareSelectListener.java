package com.music.share;


/**
 * @author: lyw
 * @package: com.ido.veryfitpro.module.home
 * @description: ${TODO}{分享点击回调接口}
 * @date: 2016/10/9 15:09
 */
public interface OnShareSelectListener {

    /**
     * 用户点击选择分享
     * @param shareType 分享类型
     * @see ShareHelper#SHARE_TYEP_EMAIL
     * @see ShareHelper#SHARE_TYEP_FACEBOOK
     * @see ShareHelper#SHARE_TYEP_TWITTER
     * @see ShareHelper#SHARE_TYEP_QQ
     * @see ShareHelper#SHARE_TYEP_FIRENDS
     * @see ShareHelper#SHARE_TYEP_FLICKR
     * @see ShareHelper#SHARE_TYEP_INSTAGRAM
     * @see ShareHelper#SHARE_TYEP_KAKAOTALK
     * @see ShareHelper#SHARE_TYEP_LINE
     * @see ShareHelper#SHARE_TYEP_LINKEDIN
     * @see ShareHelper#SHARE_TYEP_VK
     * @see ShareHelper#SHARE_TYEP_WECHAT
     * @see ShareHelper#SHARE_TYEP_WHATSAPP
     */
    void onShareSelect(int shareType);

    void onCancel();
}