package com.lu.library.base;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/3/29 0029.
 * V 表示绑定的View 类型,
 * Params 表示执行方法要传入的参数类型
 */

public abstract class BasePresenter<V,Params> {


    protected WeakReference<V> mWeak;

    /**
     * 执行方法
     */
    public final void execute(Params... paramses){
        if (!isAttachView()){
            return;
        }
        realExeAsyn(paramses);
    }

    /**
     * 真正的业务处理
     */
    protected abstract void realExeAsyn(Params... paramses);
    /**
     *
     * @param v
     */
    public void attachView(V v){
        mWeak=new WeakReference<>(v);
    }

    protected boolean isAttachView(){
        return mWeak!=null&&mWeak.get()!=null;
    }


    public void detachView(){
        if (mWeak!=null){
            mWeak.clear();
        }
    }
}
