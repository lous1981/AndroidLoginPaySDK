package com.yuan.shi.lonng.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yuan.shi.lonng.bean.LongDaLoginMethod;
import com.yuan.shi.lonng.constant.LongDaConstant;

import org.json.JSONObject;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/5/15
 */
public class LongDaUtils {
    private static final String LY_LOGIN_QQ_APP_ID = "ly_login_qq_appid";
    private static final String LY_LOGIN_QQ_APP_SECRET = "ly_login_qq_appsecret";
    private static final String LY_LOGIN_WX_APP_ID = "ly_login_wx_appid";
    private static final String LY_LOGIN_WX_APP_SECRET = "ly_login_wx_appsecret";
    private static final String LY_LY_APP_ID = "ly_ly_appid";
    private static final String LY_LY_APP_CHANNEL = "ly_ly_app_channel";
    private static final String LY_PAY_SCREEN_ORI = "ly_pay_screen";

    /**
     * 设置qq appid
     * @param context 上下文
     * @param QQAppId qq appid
     */
    public static void setLyLoginQqAppId(Context context, String QQAppId) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LOGIN_QQ_APP_ID, QQAppId);
    }

    /**
     * 获取qq appid
     * @param context 上下文
     * @return qq appid
     */
    public static String getLyLoginQqAppId(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LOGIN_QQ_APP_ID, null);
    }

    /**
     * 设置qq app secret
     * @param context 上下文
     * @param QqAppSecret qq appsecret
     */
    public static void setLyLoginQqAppSecret(Context context, String QqAppSecret) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LOGIN_QQ_APP_SECRET, QqAppSecret);
    }

    /**
     * 获取qq app secret
     * @param context 上下文
     * @return qq app secret
     */
    public static String getLyLoginQqAppSecret(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LOGIN_QQ_APP_SECRET, null);
    }

    /**
     * 设置wx appid
     * @param context 上下文
     * @param WxAppId wx appid
     */
    public static void setLyLoginWxAppId(Context context, String WxAppId) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LOGIN_WX_APP_ID, WxAppId);
    }

    /**
     * 获取wx appid
     * @param context 上下文
     * @return wx appid
     */
    public static String getLyLoginWxAppId(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LOGIN_WX_APP_ID, null);
    }

    /**
     * 设置wx app secret
     * @param context 上下文
     * @param WxAppSecret wx app secret
     */
    public static void setLyLoginWxAppSecret(Context context, String WxAppSecret) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LOGIN_WX_APP_SECRET, WxAppSecret);
    }

    /**
     * 获取wx app secret
     * @param context 上下文
     * @return wx app secret
     */
    public static String getLyLoginWxAppSecret(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LOGIN_WX_APP_SECRET, null);
    }

    /**
     * 设置ly app id
     * @param context 上下文
     * @param LyAppId ly appid
     */
    public static void setLyLyAppId(Context context, String LyAppId) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LY_APP_ID, LyAppId);
    }

    /**
     * 获取ly app id
     * @param context 上下文
     * @return ly app id
     */
    public static String getLyLyAppId(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LY_APP_ID, null);
    }

    /**
     * 设置ly app 渠道id
     * @param context 上下文
     * @param channelId ly app 渠道id
     */
    public static void setLyLyAppChannel(Context context, String channelId) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_LY_APP_CHANNEL, channelId);
    }

    /**
     * 获取ly app 渠道id
     * @param context 上下文
     * @return ly app 渠道id
     */
    public static String getLyLyAppChannel(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_LY_APP_CHANNEL, null);
    }

    /**
     * 获取支付屏幕方向
     * @param context 上下文
     * @return 支付屏幕方向
     */
    public static int getLyPayScreenOri(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue(LY_PAY_SCREEN_ORI, -1);
    }


    /**
     * 设置支付屏幕方向
     * @param context 上下文
     * @param value 支付屏幕方向
     */
    public static void setLyPayScreenOri(Context context, int value) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue(LY_PAY_SCREEN_ORI, value);
    }
    /**
     * 获取imei
     * @param context 上下文
     * @return imei
     */
    public static String getSysImei(Context context) {
        String imei = null;
        try {
            /*
             * 判断权限是否拥有
             */
            if (!lacksPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (null != telephonyManager) {
                    imei = telephonyManager.getDeviceId();
                }
            }
        } catch (SecurityException e) {
            LongDaLog.d("READ_PHONE_STATE 权限不存在");
        } finally {
            if (null == imei) {
                imei = UUID.randomUUID().toString();
            }
        }

        return imei;
    }

    /**
     * 获取应用程序名称
     * @param context 上下文
     * @return 应用名
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context 上下文
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context 上下文
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context 上下文
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断是否缺少权限
     * @param mContexts 上下文
     * @param permission 权限
     * @return 是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        if(Build.VERSION.SDK_INT >= 23) {
            return ContextCompat.checkSelfPermission(mContexts, permission) ==
                    PackageManager.PERMISSION_DENIED;
        } else {
            return false;
        }
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取appid 从xml中获取
     * @param context 上下文
     * @return appid
     */
    public static String getApplicationAppIdFromXml(Context context) {
        return getApplicationKey(context, LongDaConstant.LONGY_APPID);
    }

    /**
     * 获取app ChannelId 从xml中获取
     * @param context 上下文
     * @return appid
     */
    public static String getApplicationChannelIDFromXml(Context context) {
        return getApplicationKey(context, LongDaConstant.LONGY_CHANNEL);
    }

    /**
     * 获取Application 的meta数据
     * @param context 上下文
     * @param keyValue key的值
     */
    private static String getApplicationMetaFromXml(Context context, String keyValue) {
        String str = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object object = appInfo.metaData.get(keyValue);
            if (object instanceof String) {
                str = (String)object;
            } else if (object instanceof Integer) {
                str = String.valueOf(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 获取Application value
     * @param context 上下文
     * @param keyValue 渠道key
     * @return Application value
     */
    public static String getApplicationKey(Context context, String keyValue) {
        return getApplicationMetaFromXml(context, keyValue);
    }

    /**
     * 获取记录的相关信息
     * @param context 上下文
     * @param key key值
     * @return value内容
     */
    public static String getKeyInfo(Context context, String key) {
        String result = null;

        if (!TextUtils.isEmpty(key)) {
            LongDaSPUtil spUtil = new LongDaSPUtil(context);
            result = spUtil.getValue(key, null);
        }

        return result;
    }

    /**
     * 保存相关记录
     * @param context 上下文
     * @param key key值
     * @param value value内容
     */
    public static void setkeyInfo(Context context, String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            LongDaSPUtil spUtil = new LongDaSPUtil(context);
            spUtil.setValue(key, value);
        }
    }

    /**
     * 获取userId的相关信息
     * @param context 上下文
     * @return userId值
     */
    public static int getUserId(Context context) {
        int result;

        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        result = spUtil.getValue("longdauserid", -1);

        return result;
    }

    /**
     * 保存userId
     * @param context 上下文
     * @param value userId值
     */
    public static void setUserId(Context context,int value) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue("longdauserid", value);
    }

    /**
     * 设置成功的登录类型
     * @param context 上下文
     * @param value 类型
     */
    public static void setLoginTypeSuccess(Context context, int value) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        spUtil.setValue("longdalogintype", value);
    }

    /**
     * 获取登录类型
     * @param context 上下文
     * @return 登录类型
     */
    public static int getLoginTypeSuccess(Context context) {
        LongDaSPUtil spUtil = new LongDaSPUtil(context);
        return spUtil.getValue("longdalogintype", -1);
    }

    /**
     * 根据类型转换为方法名
     * @param type 类型
     * @return 登录方法名
     */
    public static LongDaLoginMethod loginTypeConvertLoginMethod(int type) {
        if (type == LongDaConstant.LOGIN_WEIXIN) {
            return LongDaLoginMethod.WEIXIN;
        } else if (type == LongDaConstant.LOGIN_QQ) {
            return LongDaLoginMethod.QQ;
        } else if (type == LongDaConstant.LOGIN_PHONE) {
            return LongDaLoginMethod.PHONE;
        } else {
            return LongDaLoginMethod.ONEKEY;
        }
    }

    /**
     * 根据方法名转换为类型
     * @param loginMethod 登录方法名
     * @return 登录类型
     */
    public static int loginMethodConvertLoginType(LongDaLoginMethod loginMethod) {
        if (loginMethod == LongDaLoginMethod.WEIXIN) {
            return LongDaConstant.LOGIN_WEIXIN;
        } else if (loginMethod == LongDaLoginMethod.QQ) {
            return LongDaConstant.LOGIN_QQ;
        } else if (loginMethod == LongDaLoginMethod.PHONE) {
            return LongDaConstant.LOGIN_PHONE;
        } else {
            return LongDaConstant.LOGIN_ONEKEY;
        }
    }

    /**
     * 退出登录
     * @param context 上下文
     */
    public static void LoginOut(Context context) {
        clearToken(context);
    }

    /**
     * 登出
     * @param context 上下文
     */
    private static void clearToken(Context context) {
        //设置usreid为-1
        setUserId(context,  -1);
    }

    /**
     * 创建json
     * @param activity 上下文
     * @param price 价格，单位分
     * @param good 商品
     * @param goodDesc 商品描述
     * @param orderId 订单号
     * @return 字符串
     */
    public static String createPayJson(Activity activity, int price, String good,
                                     String goodDesc, String orderId) {
        String result = null;

        try {
            JSONObject jsonPay = new JSONObject();

            String payType = LongDaConstant.LY_PAY_TYPE_WEIXIN;

            jsonPay.put("zxAppId", getLyLyAppId(activity));
            jsonPay.put("price", price);
            jsonPay.put("goods", good);
            jsonPay.put("goodsDesc", goodDesc);
            jsonPay.put("orderId", orderId);
            jsonPay.put("channelId", getLyLyAppChannel(activity));
            jsonPay.put("payType", payType);

            result = jsonPay.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
