package com.music.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.music.presenter.BasePresenter;
import com.music.presenter.IBaseView;
import com.lu.library.util.DebugLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


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
    protected  P createPresenter(){
        return null;
    };
    protected  P autoCreatePresenter(){
        try {
            return (P)getArgumentClass().newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPersenter=autoCreatePresenter();
        if (mPersenter!=null){
            try {
                mPersenter.attachView((IBaseView) this);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        super.onCreate(savedInstanceState);

    }



    //得到父类的泛型类T
    public Class getArgumentClass(){
        //返回表示此 Class 所表示的实体类的 直接父类 的 Type。注意，是直接父类
        //这里type结果是 com.dfsj.generic.GetInstanceUtil<com.dfsj.generic.User>
        Type type = getClass().getGenericSuperclass();
        DebugLog.d(type);
        // 判断 是否泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象的数组.
            // 当有多个泛型类时，数组的长度就不是1了
            Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
            Class clazz= (Class) ptype[0];
            DebugLog.d(clazz);
            return clazz;  //将第一个泛型T对应的类返回（这里只有一个）
        } else {
            return Object.class;//若没有给定泛型，则返回Object类
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPersenter!=null){
            mPersenter.onDettach();
        }
    }
}
