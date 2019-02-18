package com.yuan.shi.lonng;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yuan.shi.lonng.activity.LongDaPayAcitivity;
import com.yuan.shi.lonng.bean.LongDaLoginMethod;
import com.yuan.shi.lonng.bean.LongDaPayScreen;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.handler.LongDaPayObject;
import com.yuan.shi.lonng.init.LongDaInit;
import com.yuan.shi.lonng.login.LongDaLogin;
import com.yuan.shi.lonng.login.LongDaOther;
import com.yuan.shi.lonng.utils.LongDaLog;
import com.yuan.shi.lonng.utils.LongDaUtils;


/**
 * Created by @author luyon
 * @version 2.0  2018/5/11
 */
public class LongDaGame {
    /**
     * 初始化处理
     * @param context 上下文
     */
    public static void init(Context context) {
        LongDaInit.getInstance().init(context, null, null);
    }

    /**
     * 初始化处理
     * @param context 上下文
     * @param appId 应用appid
     */
    public static void init(Context context, String appId) {
        LongDaInit.getInstance().init(context, appId, null);
    }

    /**
     * 初始化处理
     * @param context 上下文
     * @param appId 应用appid
     * @param channelId 应用渠道id
     */
    public static void init(Context context, String appId, String channelId) {
        LongDaInit.getInstance().init(context, appId, channelId);
    }

    /**
     * 登录处理
     * @param context 上下文
     * @param loginMethod 登录平台
     * @param loginListener 登录回调
     * @param phone 手机号码，没有为null
     * @param password 密码，没有为null
     */
    public static void login(Context context, LongDaLoginMethod loginMethod, LongDaLoginListener loginListener, String phone, String password, String code) {
        LongDaLogin.getInstance().setLoginInfo(context, loginMethod, loginListener, phone, password, code);
        LongDaLogin.getInstance().startLogin();
    }

    /**
     * 退出登录
     * @param context 上下文
     */
    public static void loginOut(Context context) {
        LongDaUtils.LoginOut(context);
    }

    public static void onActivityReslt(int requestCode, int resultCode, Intent data) {
        LongDaLogin.getInstance().onActivityReslt(requestCode, resultCode, data);
    }

    /**
     * 设置微信登录参数
     * @param context 上下文
     * @param WxappId wx appid
     * @param WxappSecret wx app secret
     */
    public static void setWeiXinLogin(Context context, String WxappId, String WxappSecret) {
        LongDaUtils.setLyLoginWxAppId(context, WxappId);
        LongDaUtils.setLyLoginWxAppSecret(context, WxappSecret);
    }

    /**
     * 设置QQ登录参数
     * @param context 上下文
     * @param QqappId qq appid
     * @param QqappSecret qq app secret
     */
    public static void setQQLogin(Context context, String QqappId, String QqappSecret) {
        LongDaUtils.setLyLoginQqAppId(context, QqappId);
        LongDaUtils.setLyLoginQqAppSecret(context, QqappSecret);
    }

    /**
     * 创建支付订单，调用支付
     * @param activity 要启动的activity
     * @param price 价格，单位分
     * @param good 商品
     * @param goodDesc 商品描述
     * @param orderId 订单号
     * @param longDaPayListener 支付回调函数
     */
    public static void createPayment(Activity activity, int price, String good,
                                     String goodDesc, String orderId, LongDaPayListener longDaPayListener) {
        try {
            // 创建数据
            String data = LongDaUtils.createPayJson(activity, price, good,
                    goodDesc, orderId);

            LongDaLog.d(data);

            // 记录支付回调函数
            LongDaPayObject.getInstance().longDaPayListener = longDaPayListener;
            LongDaPayObject.getInstance().activity = activity;
            LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_UNDEFINED;

            //打开网页
            Intent intent = new Intent(activity, LongDaPayAcitivity.class);
            intent.putExtra(LongDaConstant.EXTRA_CHARGE, data);
            activity.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取密码
     * @param activity 上下文
     * @param phone 手机号码
     * @param code 验证码
     * @param listener 监听回调函数
     */
    public static void getPassword(Activity activity, String phone, String code, LongDaLoginListener listener) {
        try {
            if (TextUtils.isEmpty(phone)) {
                if (null != listener) {
                    listener.onGetPassword(LongDaConstant.LY_RESULT_ERROR, "手机号码为空", null);
                }
                return;
            }

            if (TextUtils.isEmpty(code)) {
                if (null != listener) {
                    listener.onGetPassword(LongDaConstant.LY_RESULT_ERROR, "验证码为空", null);
                }
                return;
            }

            LongDaOther.getInstance().setInfo(activity, phone, code, listener);
            LongDaOther.getInstance().startGetPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证码
     * @param activity 上下文
     * @param phone 手机号码
     * @param listener 监听回调函数
     */
    public static void sendPhoneCode(Activity activity, String phone, LongDaLoginListener listener) {
        try {
            if (TextUtils.isEmpty(phone)) {
                if (null != listener) {
                    listener.onSendCode(LongDaConstant.LY_RESULT_ERROR, "手机号码为空");
                }
                return;
            }
            LongDaOther.getInstance().setInfo(activity, phone, "", listener);
            LongDaOther.getInstance().startSendCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证验证码是否正确
     * @param activity 上下文
     * @param phone 手机号码
     * @param code 验证码
     * @param listener 监听回调函数
     */
    public static void checkCode(Activity activity, String phone, String code, LongDaLoginListener listener) {
        try {
            if (TextUtils.isEmpty(phone)) {
                if (null != listener) {
                    listener.onCodeConfirm(LongDaConstant.LY_RESULT_ERROR, "手机号码为空");
                }
                return;
            }

            if (TextUtils.isEmpty(code)) {
                if (null != listener) {
                    listener.onCodeConfirm(LongDaConstant.LY_RESULT_ERROR, "验证码为空");
                }
                return;
            }

            LongDaOther.getInstance().setInfo(activity, phone, code, listener);
            LongDaOther.getInstance().startCodeConfirm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开日志
     * @param debug 日志开关，true打开
     */
    public static void enableDebugLog(boolean debug) {
        LongDaLog.DEBUG = debug;
    }

    /**
     * 打开加载进度框
     * @param flag 加载进度框开关，true打开
     */
    public static void enableDialog(boolean flag) {
        LongDaLog.isShowDialog = flag;
    }


    /**
     * 设置屏幕方向
     * @param context 上下文
     * @param payScreen 屏幕方向
     */
    public static void setPayScreenOrientation(Context context, LongDaPayScreen payScreen) {
        LongDaUtils.setLyPayScreenOri(context, LongDaPayScreen.getIndex(payScreen.getName()));
    }
}
