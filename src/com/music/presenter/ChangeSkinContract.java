package com.music.presenter;

import android.graphics.drawable.Drawable;

import com.lu.library.base.IBaseView;

/**
 * Created by lyw on 2017/9/23.
 */

public interface ChangeSkinContract {
    interface View extends IBaseView<Presenter> {
        void shoChangSkin(Drawable drawable);
        boolean isActive();
    }

    interface Presenter extends IBasePresenter {
        void changeSkin();
    }
}
