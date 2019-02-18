package com.yuan.shi.lonng;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/11
 */
public abstract interface LongDaPayListener {
    public abstract void onResult(int responseCode, String payInfo, String errorMsg);
}
