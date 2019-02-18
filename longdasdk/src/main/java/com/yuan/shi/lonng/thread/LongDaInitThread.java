package com.yuan.shi.lonng.thread;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.utils.LongDaHttpUtils;
import com.yuan.shi.lonng.utils.LongDaLog;
import com.yuan.shi.lonng.utils.LongDaUtils;

import org.json.JSONObject;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/12
 */
public class LongDaInitThread {
    private Context mContext;
    private String mStrData;

    public LongDaInitThread(Context context) {
        mContext = context;
        createJson(context);
    }

    /**
     * 创建json
     * @param context 上下文
     */
    private void createJson(Context context) {
        try {
            JSONObject json = new JSONObject();
            int userId = LongDaUtils.getUserId(context);
            if (-1 != userId) {
                json.put("userid", userId);
            }

            // 设备相关信息
            JSONObject json1 = new JSONObject();
            json1.put("deviceType", "android");
            json1.put("version", LongDaUtils.getSystemVersion());
            json1.put("deviceId", LongDaUtils.getSysImei(context));
            json.put("device", json1);

            // 客户端信息
            json1 = new JSONObject();
            json1.put("clientId", LongDaUtils.getLyLyAppId(context));
            json1.put("clientVersion", LongDaUtils.getVersionCode(context) + "");
            json1.put("channelId", LongDaUtils.getLyLyAppChannel(context));
            json.put("client", json1);

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
                try {
                    final String result = LongDaHttpUtils.post(LongDaConstant.INIT_URL, mStrData);
//                    final String result ="";

                    LongDaLog.d(result);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Result=" + result, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
