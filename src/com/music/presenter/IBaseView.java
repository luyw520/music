package com.music.presenter;


/**
 * Created by lyw on 2017/9/23.
 */

public interface IBaseView<T> {
    void onSuccess(T data);
    void onFaild(Exception e);
}
