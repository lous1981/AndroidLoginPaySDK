package com.yuan.shi.lonng.init;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yuan.shi.lonng.thread.LongDaInitThread;
import com.yuan.shi.lonng.utils.LongDaUtils;


/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/12
 */
public class LongDaInit {
    public static LongDaInit getInstance() {
        return LYInit.instance;
    }

    private static class LYInit {
        private static final LongDaInit instance = new LongDaInit();
    }

    /**
     * 初始化
     * @param context 上下文
     */
    public void init(Context context) {
        init(context, null, null);
    }

    /**
     * 初始化
     * @param context 上下文
     * @param appId appid
     */
    public void init(Context context, String appId) {
        init(context, appId, null);
    }

    /**
     * 初始化
     * @param context 上下文
     * @param appId appid
     * @param channelId 渠道id
     */
    public void init(Context context, String appId, String channelId) {

        // appid为空，从xml获取
        if ((null == appId) || (TextUtils.isEmpty(appId))) {
            appId = LongDaUtils.getApplicationAppIdFromXml(context);
        }

        // 优先从xml获取
        if (!TextUtils.isEmpty(LongDaUtils.getApplicationChannelIDFromXml(context))) {
            channelId = LongDaUtils.getApplicationChannelIDFromXml(context);
        }
        else {
            // channelId为空，从xml获取
            if ((null == channelId) || (TextUtils.isEmpty(channelId))) {
                channelId = "long";
            }
        }

        // 保存相关数据
        LongDaUtils.setLyLyAppId(context, appId);
        LongDaUtils.setLyLyAppChannel(context, channelId);

        LongDaInitThread longDaInitThread = new LongDaInitThread(context);
        longDaInitThread.start();
    }
}
