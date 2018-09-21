package com.music;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music
 * @description: ${TODO}{ 类注释}
 * @date: 2018/7/12 0012
 */

public class Test {
    public static void main(String[] args){
        Observable.just(1,2,3,4).map(new Function<Integer,String>() {

            @Override
            public String apply(Integer arg0) throws Exception {
                // TODO Auto-generated method stub
                return ""+arg0;
            }
        }).subscribe(new Observer<String>() {


            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(String t) {
                // TODO Auto-generated method stub
                System.out.println("onNext:"+t);
            }

            @Override
            public void onError(Throwable e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onComplete() {
                // TODO Auto-generated method stub
                System.out.println("onComplete");
            }
        });
    }
}
