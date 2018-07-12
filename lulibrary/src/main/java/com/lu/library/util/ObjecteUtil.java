package com.lu.library.util;

import java.util.Collection;

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

    /**
     * 检测集合是否为空
     * @param collection
     * @return
     */
    public static boolean isCollectionEmpty(Collection collection){
        if (collection==null){
            return true;
        }
        if (collection.isEmpty()){
            return true;
        }
        return false;
    }
}
