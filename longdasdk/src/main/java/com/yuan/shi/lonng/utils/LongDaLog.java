package com.yuan.shi.lonng.utils;

import android.util.Log;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/5/11
 */
public class LongDaLog {
    public static boolean DEBUG = false;
    private static String tag = "LongDa";
    private static boolean flag = false;
    public static boolean isShowDialog = false;

    public LongDaLog() {
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(tag, msg == null ? "null" : msg);
        }

    }

    public static void a(String msg) {
        if (flag) {
            String tags = "zxpp_debug";
            Log.d(tags, msg == null ? "null" : msg);
        }

    }

    public static void a(Exception e) {
        if (flag) {
            if (e != null) {
                e.printStackTrace();
            }
        } else if (DEBUG && e != null && e.getMessage() != null) {
            Log.d(tag, e.getMessage());
        }

    }
}
