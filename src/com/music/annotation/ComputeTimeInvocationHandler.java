package com.music.annotation;

import com.music.utils.DeBug;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by lyw on 2017/10/3.
 */

public class ComputeTimeInvocationHandler implements InvocationHandler {
    private Object target;
    public ComputeTimeInvocationHandler(Object o){
        target=o;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DeBug.d(this,"invoke..........."+method.getName());
        long start=System.currentTimeMillis();
        Object result=method.invoke(target,args);
        DeBug.d(this,""+method.getName()+"方法耗时:"+(System.currentTimeMillis()-start)+"ms");
        return result;
    }
}
