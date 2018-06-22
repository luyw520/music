package com.lu.library.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public  class ConnectionUtil {


        private static ConnectivityManager connectivityManager = null;
        private static NetworkInfo niInfo = null;

        /**
         *
         * @param context
         * @return
         */
        public static boolean isNetWorkConnected(Context context) {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            niInfo = connectivityManager.getActiveNetworkInfo();
            return niInfo != null && niInfo.isConnected();
        }

        public static int getNetWorkType(Context context) {

            if (niInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return niInfo.getType();
            } else {
                return isFastMobileNetwork(context);
            }

        }

        private static int isFastMobileNetwork(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            int type = 0;
            switch (telephonyManager.getNetworkType()) {
            // 3g
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                // 3g
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                type = 3;
                break;
            // 2g
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                // 2g
            case TelephonyManager.NETWORK_TYPE_CDMA:

                type = 2;
                break;
            }
            return type;
        }
    }