package com.yuan.shi.lonng.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.utils.LongDaHttpUtils;
import com.yuan.shi.lonng.utils.LongDaUtils;

import org.json.JSONObject;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/14
 */
public class LongDaLoginThread {
    private Context mContext;
    private Handler mHandler;
    private String mStrData;

    /**
     * 初始化
     * @param context 上下文
     * @param handler 句柄
     * @param loginType 登录类型
     * @param wxAppId 微信appid
     * @param wxAppSecret 微信appsecret
     * @param wxCode 微信登录code
     * @param qqAppId qqAppId
     * @param qqOpenId qq openid
     * @param accessToken qq access_token
     * @param phone 手机号码
     * @param password 密码
     * @param code 验证码
     * @param userId 登录过的用户的userid字段
     */
    public LongDaLoginThread(Context context, Handler handler, int loginType,
                             String wxAppId, String wxAppSecret, String wxCode,
                             String qqAppId, String qqOpenId, String accessToken,
                             String phone, String password, String code,
                             int userId) {
        mContext = context;
        mHandler = handler;
        createJsonParam(context, loginType, wxAppId, wxAppSecret, wxCode, qqAppId, qqOpenId, accessToken,
                phone, password, code, userId);
    }

    /**
     * 创建json数组
     * @param context 上下文
     * @param loginType 登录类型
     * @param wxAppId 微信appid
     * @param wxAppSecret 微信 appsecret
     * @param wxCode 微信code
     * @param qqAppId qqAppId
     * @param qqOpenId qqopenid
     * @param accessToken qq access_token
     * @param phone 手机号码
     * @param password 密码
     * @param code 验证码
     * @param userId userId
     */
    private void createJsonParam(Context context, int loginType,
                                 String wxAppId, String wxAppSecret, String wxCode,
                                 String qqAppId, String qqOpenId, String accessToken,
                                 String phone, String password, String code,
                                 int userId) {
        try {
            JSONObject json = new JSONObject();
            json.put("loginType", loginType);

            if (null == wxAppId) {
                wxAppId = "";
            }

            if (null == wxAppSecret) {
                wxAppSecret = "";
            }

            if (null == wxCode) {
                wxCode = "";
            }

            if (null == qqOpenId) {
                qqOpenId = "";
            }

            if (null == accessToken) {
                accessToken = "";
            }

            if (null == phone) {
                phone = "";
            }

            if (null == password) {
                password = "";
            }


            /*
             设置登录相关信息，如果已经登录那么就不用再次登录了
             */
            if (LongDaConstant.LOGIN_WEIXIN == loginType) {
                //登录相关信息
                JSONObject json1 = new JSONObject();
                json1.put("appid", wxAppId);
                json1.put("secret", wxAppSecret);
                json1.put("code", wxCode);

                if (-1 != userId) {
                    json1.put("userid", userId);
                }

//                if (!TextUtils.isEmpty(token)) {
//                    json1.put("token", token);
//                }

                json.put("params", json1);
            } else if (LongDaConstant.LOGIN_QQ == loginType) {
                //登录相关信息
                JSONObject json1 = new JSONObject();
                json1.put("appid", qqAppId);
                json1.put("openid", qqOpenId);
                json1.put("access_token", accessToken);

                if (-1 != userId) {
                    json1.put("userid", userId);
                }

//                if (!TextUtils.isEmpty(token)) {
//                    json1.put("token", token);
//                }
                json.put("params", json1);
            } else if (LongDaConstant.LOGIN_PHONE == loginType) {
                //登录相关信息
                JSONObject json1 = new JSONObject();

                if (-1 != userId) {
                    json1.put("userid", userId);
                } else {
                    json1.put("phone", phone);
                    json1.put("passwd", password);

                    if (!TextUtils.isEmpty(code)) {
                        json1.put("smsCode", code);
                    }
                }

//                if (!TextUtils.isEmpty(token)) {
//                    json1.put("token", token);
//                }
                json.put("params", json1);
            } else {
                //登录相关信息
                JSONObject json1 = new JSONObject();
                if (-1 != userId) {
                    json1.put("userid", userId);
                    json.put("params", json1);
                }
//                if (!TextUtils.isEmpty(token)) {
//                    json1.put("token", token);
//                }
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

//            Toast.makeText(mContext, "net=" + mStrData, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始登录联网
     */
    public void startLoginNet() {
        LongDaThreadManager.getPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                boolean mbFlag = false;
                try {
                    String data = LongDaHttpUtils.post(LongDaConstant.LOGIN_URL, mStrData);

                    Message message = mHandler.obtainMessage();
                    message.what = LongDaConstant.LY_HANDLE_LOGIN_OK;
                    message.obj = data;
                    mHandler.sendMessage(message);
                    mbFlag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!mbFlag) {
                        String data = "";
                        Message message = mHandler.obtainMessage();
                        message.what = LongDaConstant.LY_HANDLE_LOGIN_ERROR;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });
    }
}
