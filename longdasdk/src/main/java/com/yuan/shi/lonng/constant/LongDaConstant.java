package com.yuan.shi.lonng.constant;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/5/11
 */
public class LongDaConstant {

    public static final String INIT_URL = "http://game.ldagame.com/game-data/collectStat";
    public static final String LOGIN_URL = "http://game.ldagame.com/game-data/login";
    public static final String SEND_SMS_URL = "http://game.ldagame.com/game-data/getCode";
    public static final String CHECK_SMS_URL = "http://game.ldagame.com/game-data/checkCode";
    public static final String FIND_PWD_URL = "http://game.ldagame.com/game-data/iforget";

    public static final String PAY_INFO_URL = "http://game.ldagame.com/game-data/login";
    public static final String PAY_RESULT_URL = "http://game.ldagame.com/game-data/login";

    public static final String LONGY_APPID = "LONGY_APPID";
    public static final String LONGY_CHANNEL = "LONGY_CHANNEL";

    // 登录联网处理
    public static final int LY_HANDLE_LOGIN_OK = 8000;
    public static final int LY_HANDLE_LOGIN_ERROR = 8001;
    public static final int LY_HANDLE_PAY_INFO_OK = 8002;
    public static final int LY_HANDLE_PAY_INFO_ALI_OK = 8003;
    public static final int LY_HANDLE_PAY_INFO_ERROR = 8004;
    public static final int LY_HANDLE_PAY_RESULT_OK = 8005;
    public static final int LY_HANDLE_PAY_RESULT_ERROR = 8006;
    public static final int LY_HANDLE_PROC_PASSWORD_ERROR = 8007;
    public static final int LY_HANDLE_PROC_PASSWORD_OK = 8008;
    public static final int LY_HANDLE_PROC_CODE_ERROR = 8009;
    public static final int LY_HANDLE_PROC_CODE_OK = 8010;
    public static final int LY_HANDLE_PROC_SEND_CODE_ERROR = 8011;
    public static final int LY_HANDLE_PROC_SEND_CODE_OK = 8012;

    // 错误码
    public static final int LY_RESULT_UNDEFINED = 90003;
    public static final int LY_RESULT_SUCCESS = 90000;
    public static final int LY_RESULT_ERROR = 90001;
    public static final int LY_RESULT_CANCEL = 90002;

    // 事件处理
    public static final int LY_LOGIN_ON_START = 100000;
    public static final int LY_LOGIN_ON_COMPLETE = 100001;
    public static final int LY_LOGIN_ON_ERROR = 100002;
    public static final int LY_LOGIN_ON_CANCEL = 100003;

    public static final String LY_SP_ONE_KEY = "onekey";
    public static final String LY_SP_USER_SUFFIX = "_id";
    public static final String EXTRA_CHARGE = "com.yuan.shi.app.PaymentActivity.CHARGE";
    public static final String EXTRA_TYPE = "com.yuan.shi.app.type";
    public static final String EXTRA_PAY_DATA = "com.yuan.shi.app.PayData";

    public static final int LOGIN_WEIXIN = 1;//1-wechat 2-qq 3-用户名密码 4-一键登录
    public static final int LOGIN_QQ = 2;
    public static final int LOGIN_PHONE = 5;
    public static final int LOGIN_ONEKEY = 4;

    public static final String LY_PAY_TYPE_WEIXIN = "wxPay";
    public static final String LY_PAY_TYPE_ALI = "aliPay";
}
