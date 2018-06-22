package com.lu.library.base;

/**
 * Created by lyw on 2018/3/29 0029.
 * 通用View接口
 * 该接口只包含成功或者失败的回调
 * 如果业务逻辑多的View层接口应该继承这个接口。
 */

public interface IBaseView<T> {
    void success(T t);
    void error(Exception e);
}
