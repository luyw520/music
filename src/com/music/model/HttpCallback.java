package com.music.model;

/**
 * Created by lyw on 2017/8/27.
 */

public interface HttpCallback<T> {
    void onFailure(Exception e);
    void onSuccess(T result);
}
