package com.lu.library.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lu.library.R;

    public  class TopNoticeDialog {

        private static CountDown mCountDown;
        private static View view;
        private static TextView tip_txt=null;
        private static LinearLayout ll=null;
        private final static int COUNTDOWN_TIME=2000;
        private static WindowManager wm;
        private final static WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        private static Toast mToast;
        private final static Handler mHandler=new Handler(Looper.getMainLooper());
        public TopNoticeDialog() {

        }



        /**
         * 创建一个自定义的Toast
         * 自己控制时长
         * @param context
         * @param text
         * @param type
         */
        @SuppressLint("InflateParams")
        private static void createCustomToast(Context context, CharSequence text, TipType type){
            wm= (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = PixelFormat.TRANSLUCENT;
    //        params.windowAnimations = com.android.internal.R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.gravity= Gravity.CENTER;
            params.setTitle("Toast");
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

            if(view==null){
                LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=inflater.inflate(R.layout.top_notice,null);
                 ll= (LinearLayout) view.findViewById(R.id.ll);
                tip_txt= (TextView) view.findViewById(R.id.text);
            }
            if (view.getParent() != null) {
                 Log.v("TopNoticeDialog", "REMOVE! " + view + " in TopNoticeDialog");
                wm.removeView(view);
            }
            tip_txt.setText(text);
            wm.addView(view, mParams);
            startCountDown();
        }
        public enum TipType {
            SUCCESS_TIP, FAILURE_TIP
        }
        private static void update(CharSequence text,TipType type){
            ((TextView) view.findViewById(R.id.text)).setText(text);
            startCountDown();
        }

        /**
         * 弹出系统的toast
         * 可以避免多次点击弹出多个Toast
         * @param context
         * @param text
         */
        public static void showToast(final Context context,final CharSequence text){
    //          createDialog(context, text, TipType.SUCCESS_TIP);
            if (Thread.currentThread()==Looper.getMainLooper().getThread()){
                realShowToast(context,text);
            }else{
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        realShowToast(context,text);
                    }
                });
            }
        }
        private  static void realShowToast(Context context,CharSequence text){
            if (mToast==null){
                mToast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
            }
            mToast.setText(text);
            mToast.show();
        }
        public static void showToast(Context context,int res){
    //    	createDialog(context, context.getText(res), TipType.SUCCESS_TIP);
            showToast(context,context.getResources().getString(res));
        }
        /**
         * @param context
         * @param text
         * @param type    SUCCESS_TIP, FAILURE_TIP
         */
        public static void showToast(Context context,CharSequence text,TipType type){
            if(view==null){
                createCustomToast(context, text, type);
            }else{
                update(text, type);
            }
        }

        public static void setDialogNull(){
            view=null;
        }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static void closeDialog(boolean setNull) {
            wm.removeView(view);
            stopCountDown();
        }


        static class CountDown extends CountDownTimer {

            public CountDown(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                closeDialog(false);
            }
        }
        /**
         */
        private static void startCountDown() {
            if (mCountDown != null) {
                mCountDown.cancel();
            }
            mCountDown = new CountDown(COUNTDOWN_TIME, 1000);
            mCountDown.start();
        }
        private static void stopCountDown() {
            if (mCountDown != null) {
                mCountDown.cancel();
                mCountDown = null;
            }
        }
    }