package com.music.presenter;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IBaseView> {
//    public M mModel;
    private V mView;
    public WeakReference<V> mViewRef;

    public void attachModelView(V pView) {
        mViewRef = new WeakReference<V>(pView);
//        this.mModel = pModel;
    }
    public void attachView(V pView) {
        mViewRef = new WeakReference<V>(pView);
//        this.mModel = pModel;
    }

    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }

    public void onDettach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}

