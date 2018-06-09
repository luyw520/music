package com.music.ui.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.music.presenter.BasePresenter;


/**
 * Created by Administrator on 2018/3/29 0029.
 * P 业务处理类型 BasePresenter的子类
 */

public abstract class BaseMVPActivity<P extends BasePresenter> extends BaseActivity{

    protected P mPersenter;
    /**
     * 子activity需复写这个方法。。。。
     * @return
     */
    protected abstract P createPresenter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPersenter=createPresenter();
        if (mPersenter!=null){
            try {
                mPersenter.attachView(this);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        super.onCreate(savedInstanceState);

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPersenter!=null){
            mPersenter.onDettach();
        }
    }
}
