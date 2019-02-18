package com.yuan.shi.lonng.thread;

import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.handler.LongDaPayObject;
import com.yuan.shi.lonng.utils.LongDaLog;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/14
 */
public class LongDaPayThread {
    private Handler mHandler;
    private String mStrData;
    private String mlongDaPayType;

    /**
     * 初始化
     * @param handler 句柄
     * @param data 支付信息
     * @param payType 支付类型
     */
    public LongDaPayThread(Handler handler, String data, String payType) {
        mHandler = handler;
        mStrData = data;
        mlongDaPayType = payType;
    }

    /**
     * 开始登录联网
     */
    public void startPayInfoNet() {
        LongDaThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                // 标识是否有返回
                boolean mbFlag = false;
                try {
//                    String data = LongDaHttpUtils.post(LongDaConstant.PAY_INFO_URL, mStrData);
                    Thread.sleep(5000);

                    String data = "";
                    LongDaLog.d(data);
//                    if ((null != data) && (mlongDaPayType.equals(LongDaConstant.LY_PAY_TYPE_ALI))) {
//                        JSONObject json = new JSONObject(data);
//                        String payInfo = json.optString("payInfo");
//
//                        PayTask alipay = new PayTask(LongDaPayObject.getInstance().activity);
//                        Map<String, String> result = alipay.payV2(payInfo, true);
//
//                        Message msg = new Message();
//                        msg.what = LongDaConstant.LY_HANDLE_PAY_INFO_ALI_OK;
//                        msg.obj = result;
//                        mHandler.sendMessage(msg);
//                    } else
                    {
                        Message message = mHandler.obtainMessage();
                        message.what = LongDaConstant.LY_HANDLE_PAY_INFO_OK;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                    mbFlag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!mbFlag) {
                        String data = "";
                        Message message = mHandler.obtainMessage();
                        message.what = LongDaConstant.LY_HANDLE_PAY_INFO_ERROR;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
    }

    /**
     * 开始登录联网
     */
    public void startPayResultNet() {
        LongDaThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                boolean mbFlag = false;
                try {
                    //String data = LongDaHttpUtils.post(LongDaConstant.PAY_RESULT_URL, mStrData);
                    String data = "";
                    Thread.sleep(5000);
                    Message message = mHandler.obtainMessage();
                    message.what = LongDaConstant.LY_HANDLE_PAY_RESULT_OK;
                    message.obj = data;
                    mHandler.sendMessage(message);
                    mbFlag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!mbFlag) {
                        String data = "";
                        Message message = mHandler.obtainMessage();
                        message.what = LongDaConstant.LY_HANDLE_PAY_RESULT_ERROR;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
    }
}
