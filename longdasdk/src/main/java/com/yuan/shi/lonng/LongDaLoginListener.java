package com.yuan.shi.lonng;

import com.yuan.shi.lonng.bean.LongDaLoginMethod;

import org.json.JSONObject;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/11
 */
public abstract interface LongDaLoginListener {

    public abstract void onStart(LongDaLoginMethod loginMethod);

    public abstract void onComplete(LongDaLoginMethod loginMethod, int responseCode, JSONObject data);

    public abstract void onError(LongDaLoginMethod loginMethod, int responseCode, String errorMsg);

    public abstract void onCancel(LongDaLoginMethod loginMethod, int responseCode);

    public abstract void onGetPassword(int responseCode, String errorMsg, JSONObject data);

    public abstract void onCodeConfirm(int responseCode, String errorMsg);

    public abstract void onSendCode(int responseCode, String errorMsg);
}
