package com.yuan.shi.lonng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuan.shi.lonng.bean.LongDaLoginMethod;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.login.LongDaLogin;
import com.yuan.shi.lonng.thread.LongDaLoginThread;
import com.yuan.shi.lonng.utils.LongDaLog;
import com.yuan.shi.lonng.utils.LongDaUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by @author luyon
 * @version 2.0  2018/5/11
 */
public class LongDaLoginActivity extends Activity implements IWXAPIEventHandler {

    private Handler mHandler = new ZxHandler(this);
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private String mStrAppId;
    private String mStrAppSecret;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            LongDaLog.a("onCreate");

            mContext = this;
            mStrAppId = LongDaUtils.getLyLoginWxAppId(this);
            mStrAppSecret = LongDaUtils.getLyLoginWxAppSecret(this);
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            api = WXAPIFactory.createWXAPI(this, mStrAppId, false);
            // 将该app注册到微信
            api.registerApp(mStrAppId);
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装了微信
     * @return 微信标识，true安装
     */
    private boolean isHasWx() {
        try {
            Class weixin = Class.forName("com.tencent.mm.sdk.openapi.IWXAPI");
            if (weixin != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LongDaLog.a("onNewIntent isWXPayEntryActivity=");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LongDaLog.a("onDestroy isWXPayEntryActivity=");
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
//                Toast.makeText(this, "正确"+code, Toast.LENGTH_LONG).show();
                //获取用户信息
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
//                Toast.makeText(this, "错误"+baseResp.errCode+"," + baseResp.errStr, Toast.LENGTH_LONG).show();
                LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, baseResp.errCode,
                        baseResp.errStr, null);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_CANCEL, baseResp.errCode,
                        baseResp.errStr, null);
                finish();
                break;
            default:
                LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_CANCEL, baseResp.errCode,
                        baseResp.errStr, null);
                finish();
                break;
        }
    }

    private void onFinish() {
//        Toast.makeText(mContext, "调用关闭", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 获取 access token
     * @param code 授权码
     */
    private void getAccessToken(final String code) {
        int userId = -1;

        if (LongDaLoginMethod.WEIXIN == LongDaUtils.loginTypeConvertLoginMethod(LongDaUtils.getLoginTypeSuccess(mContext))) {
            userId = LongDaUtils.getUserId(mContext);
        }

        LongDaLoginThread longDaLoginThread = new LongDaLoginThread(mContext, mHandler, LongDaConstant.LOGIN_WEIXIN,
                mStrAppId, mStrAppSecret, code, null, null, null, null, null, null, userId);
        longDaLoginThread.startLoginNet();
    }

    /**
     * 保存相关信息
     * @param token token
     * @param userId 用户id
     */
    private void setLoginInfo(String token, int userId) {
        LongDaUtils.setUserId(mContext, userId);
        LongDaUtils.setLoginTypeSuccess(mContext, LongDaUtils.loginMethodConvertLoginType(LongDaLoginMethod.WEIXIN));
    }

    /**
     * 自定义handler
     */
    static class ZxHandler extends Handler {
        WeakReference<LongDaLoginActivity> zxPaymentActivity;

        ZxHandler(LongDaLoginActivity activity) {
            this.zxPaymentActivity = new WeakReference<>(activity);
        }

        /**
         * 处理登录事件
         * @param msg 消息内容
         */
        public void handleMessage(Message msg) {
            LongDaLoginActivity activity = this.zxPaymentActivity.get();
            switch(msg.what) {
                case LongDaConstant.LY_HANDLE_LOGIN_OK: {

                    boolean flag = false;
                    try {
                        String data = (String) msg.obj;

                        if (null != data) {
                            JSONObject json = new JSONObject(data);

                            int code = json.optInt("code");
                            String message = json.optString("message");

                            if (200 == code) {
                                flag = true;
                                JSONObject resultData = json.optJSONObject("data");
                                int userId = resultData.optInt("userId");
                                String token = resultData.optString("token");

                                //记录相关数据
                                activity.setLoginInfo(token, userId);
                                LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_COMPLETE, LongDaConstant.LY_RESULT_SUCCESS, message, data);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "网络错误", null);
                        }
                    }

                    activity.onFinish();
                    break;
                }

                case LongDaConstant.LY_HANDLE_LOGIN_ERROR: {
                    LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_ERROR,
                            LongDaConstant.LY_RESULT_ERROR, ((String)msg.obj), null);
                    activity.onFinish();
                    break;
                }

                default: {
                    LongDaLogin.getInstance().handleResult(LongDaConstant.LY_LOGIN_ON_ERROR,
                            LongDaConstant.LY_RESULT_ERROR, ((String)msg.obj), null);
                    activity.onFinish();
                    break;
                }
            }
        }
    }
}
