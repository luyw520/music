package com.lu.library.base;

import com.lu.library.util.DebugLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.lu.library.base
 * @description: ${TODO}{ 类注释}
 * @date: 2018/6/22 0022
 */

public class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable disposable) {
        DebugLog.d("onSubscribe："+disposable.toString());
        DebugLog.d(Thread.currentThread().getName());
    }

    @Override
    public void onNext(T t) {
        DebugLog.d("onNext");
        DebugLog.d(Thread.currentThread().getName());
    }

    @Override
    public void onError(Throwable throwable) {
        DebugLog.d("onError");
    }

    @Override
    public void onComplete() {
        DebugLog.d("onComplete");
        DebugLog.d(Thread.currentThread().getName());
    }
}
