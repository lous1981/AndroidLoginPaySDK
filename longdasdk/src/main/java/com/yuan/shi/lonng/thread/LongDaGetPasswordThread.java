package com.yuan.shi.lonng.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.utils.LongDaHttpUtils;

import org.json.JSONObject;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/12
 */
public class LongDaGetPasswordThread {
    private Context mContext;
    private String mStrData;
    private Handler mHandler;

    public LongDaGetPasswordThread(Context context, String phone, String code, Handler handler) {
        mContext = context;
        createJson(phone, code);
        mHandler = handler;
    }

    /**
     * 创建json
     * @param phone 手机号码
     * @param code 验证码
     */
    private void createJson(String phone, String code) {
        try {
            JSONObject json = new JSONObject();
            json.put("phone", phone);
            json.put("smsCode", code);

            mStrData = json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始初始化
     */
    public void start() {
        LongDaThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                boolean mbFlag = false;
                try {
                    final String result = LongDaHttpUtils.post(LongDaConstant.FIND_PWD_URL, mStrData);
//                    final String result ="";

                    Message message = mHandler.obtainMessage();
                    message.what = LongDaConstant.LY_HANDLE_PROC_PASSWORD_OK;
                    message.obj = result;
                    mHandler.sendMessage(message);
                    mbFlag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!mbFlag) {
                        String data = "";
                        Message message = mHandler.obtainMessage();
                        message.what = LongDaConstant.LY_HANDLE_PROC_PASSWORD_ERROR;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
    }
}
