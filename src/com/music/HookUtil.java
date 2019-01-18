package com.music;

import com.lu.library.base.BasePresenter;
import com.lu.library.log.DebugLog;
import com.taobao.android.dexposed.XC_MethodHook;

/**
 * Created by lyw.
 *
 * @author: lyw
 * @package: com.music
 * @description: ${TODO}{ 类注释}
 * @date: 2018/10/16 0016
 */
public class HookUtil {

    static class ThreadMethodHook extends XC_MethodHook {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            BasePresenter t = (BasePresenter) param.thisObject;
            DebugLog.i("thread:" + t + ", started..");
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            BasePresenter t = (BasePresenter) param.thisObject;
            DebugLog.i( "thread:" + t + ", exit..");
        }
    }
public static void hook(){
//    DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
//        @Override
//        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//            super.afterHookedMethod(param);
//            BasePresenter thread = (BasePresenter) param.thisObject;
//            Class<?> clazz = thread.getClass();
//            if (clazz != BasePresenter.class) {
//                DebugLog.d( "found class extend Thread:" + clazz);
//                DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
//            }
//            DebugLog.d( "Thread: " + clazz.getSimpleName() + " class:" + thread.getClass() +  " is created.");
//        }
//    });
//    DexposedBridge.findAndHookMethod(BasePresenter.class, "detachView", new ThreadMethodHook());
}

}
