package com.lu.library.base;

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

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
