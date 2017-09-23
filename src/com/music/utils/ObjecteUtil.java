package com.music.utils;

/**
 * Created by lyw on 2017/9/23.
 */

public class ObjecteUtil {
    public static <T> T checkNotNull(T obj) {
        if(obj == null) {
            throw new NullPointerException();
        } else {
            return obj;
        }
    }
}
