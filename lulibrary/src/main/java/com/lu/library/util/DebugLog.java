package com.lu.library.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

public class DebugLog {

    private static final int JSON_INDENT = 4;
    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {

    }

    public static boolean isDebuggable() {
        return true;
    }

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        long startTime=System.currentTimeMillis();
        buffer.append("[");
        buffer.append("(");
        buffer.append(className);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append(")");
        buffer.append("#");
        buffer.append(methodName);
        buffer.append("]");
        buffer.append(printIfJson(log));
        long end=System.currentTimeMillis();
        Log.d("DebugLog",(end-startTime)+"ms");
        return buffer.toString();
    }
    public static String printIfJson(String msg) {

        String message;
        boolean isJson=false;
        StringBuffer stringBuffer=new StringBuffer();
        try {
            int index=-1;
            if(msg.contains("{")){
                index=msg.indexOf("{");
            }else if (msg.contains("[")){
                index=msg.indexOf("{");
            }

            if (index!=-1){
                stringBuffer.append(msg.substring(0,index));
                msg=msg.substring(index);
            }

            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
                isJson=true;
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
                isJson=true;
            } else {
                message = msg;
                isJson=false;
            }
        } catch (Exception e) {
            message = msg;
            isJson=false;
        }
        stringBuffer.append(message);
//        message = headString + KLog.LINE_SEPARATOR + message;
//        String[] lines = message.split(KLog.LINE_SEPARATOR);
//        for (String line : lines) {
//            Log.d(tag, "║ " + line);
//        }

//        KLogUtil.printLine(tag, true);
//        message = headString + KLog.LINE_SEPARATOR + message;
//        String[] lines = message.split(KLog.LINE_SEPARATOR);
//        for (String line : lines) {
//            Log.d(tag, "║ " + line);
//        }
//        KLogUtil.printLine(tag, false);
        return stringBuffer.toString();
    }
    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }
    private static void getMethodNames2(StackTraceElement[] sElements) {
        className = sElements[2].getFileName();
        methodName = sElements[2].getMethodName();
        lineNumber = sElements[2].getLineNumber();
    }

    public static void e(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void i(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }
    public static void d(String tag,String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog("tag:"+tag+","+message));
    }
    public static void d(Object message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message==null?"message is null":message.toString()));
    }
    public static void d2(String message) {
        if (!isDebuggable())
            return;

        getMethodNames2(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }
    public static void d2(String tag,String message) {
        if (!isDebuggable())
            return;

        getMethodNames2(new Throwable().getStackTrace());
        Log.i(className, createLog(tag+","+message));
    }
    public static void d2(Object message) {
        if (!isDebuggable())
            return;

        getMethodNames2(new Throwable().getStackTrace());
        Log.i(className, createLog(message==null?"warm!!!message is null":message.toString()));
    }

    public static void v(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}