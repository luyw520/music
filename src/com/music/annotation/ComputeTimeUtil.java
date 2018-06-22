package com.music.annotation;

import com.music.utils.DeBug;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by lyw on 2017/10/3.
 */

public class ComputeTimeUtil {
    public static void inject(Object object){
        Method[] methods=object.getClass().getMethods();
        long start=System.currentTimeMillis();
        //获取所有的方法
        for (Method method:methods){
            //获取方法上面的所有注解
            Annotation annotations[]=method.getAnnotations();
            for (Annotation annotation:annotations){
                //如果此方法是计算时间的。
                if (annotation instanceof ComputeTime){
                    Class<?>[] interfaces =new Class[]{ComputeTime.class};
                   Proxy.newProxyInstance(object.getClass().getClassLoader(),interfaces,new ComputeTimeInvocationHandler(object));
                }
            }
        }
        long end=System.currentTimeMillis();
        DeBug.d("ComputeTimeUtil","..............inject耗时:"+(end-start)+"ms");
    }
}
