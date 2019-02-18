package com.yuan.shi.lonng.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yuan.shi.lonng.LongDaLoginListener;
import com.yuan.shi.lonng.bean.LongDaLoginMethod;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.thread.LongDaLoginThread;
import com.yuan.shi.lonng.utils.LongDaLog;
import com.yuan.shi.lonng.utils.LongDaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/12
 */
public class LongDaLogin {

    private static LongDaLoginListener mlYLoginListener;
    private Context mContext;
    private static LongDaLoginMethod mLoginMethod;
    private LyHandler mLyHandler;
    private BaseUiListener mBaseUiListener;
    private static String mStrPhone;
    private static String mStrPassword;
    private static String mStrCode;
    private static int mIntuserId = -1;
    private Tencent mTencent;
    private static int mIntHandleResult = 2;
    private static ProgressDialog mDialog;

    public static LongDaLogin getInstance() {
        return LYLogin.instance;
    }

    private static class LYLogin {
        private static final LongDaLogin instance = new LongDaLogin();
    }

    public void setLoginInfo(Context context, LongDaLoginMethod loginMethod, LongDaLoginListener loginListener, String phone, String passWord, String code) {
        mContext = context;
        mlYLoginListener = loginListener;
        mLoginMethod = loginMethod;
        mLyHandler = new LyHandler(this);
        mStrPhone = phone;
        mStrPassword = passWord;
        mStrCode = code;
    }

    /**
     * 开始登陆处理
     */
    public void startLogin() {
        // 初始化计数器
        mIntHandleResult = 2;
        mIntuserId = -1;

        // 调用开始事件
        handleResult(LongDaConstant.LY_LOGIN_ON_START, 0, null, null);

        // 获取token值
        getLoginUserId(mContext);

        if (-1 != mIntuserId) {
            if ((mLoginMethod == LongDaLoginMethod.PHONE)
                    || (mLoginMethod == LongDaLoginMethod.ONEKEY)) {
                startHasLogin();
                return;
            }
        }

        // 处理登录
        if (LongDaLoginMethod.QQ == mLoginMethod) {
            mBaseUiListener = null;
            mBaseUiListener = new BaseUiListener();
            startQQLogin();
        } else if (LongDaLoginMethod.WEIXIN  == mLoginMethod) {
            startWeiXinLogin();
        } else if (LongDaLoginMethod.PHONE == mLoginMethod) {
            startPhoneLogin();
        } else if (LongDaLoginMethod.ONEKEY == mLoginMethod) {
            startOneKeyLogin();
        }
    }

    /**
     * 开始qq登陆
     */
    private void startQQLogin() {
        // qq登录实例
        mTencent = Tencent.createInstance(LongDaUtils.getLyLoginQqAppId(mContext), mContext.getApplicationContext());
        mTencent.login((Activity) mContext, "all", mBaseUiListener);
    }

    /**
     * 开始微信登陆
     */
    private void startWeiXinLogin() {
        try {
            String appId = LongDaUtils.getLyLoginWxAppId(mContext);
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            IWXAPI api = WXAPIFactory.createWXAPI(mContext, appId, false);
            // 将该app注册到微信
            api.registerApp(appId);

            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";//
            req.state = "wechat_sdk_微信登录";
            api.sendReq(req);
        } catch (Exception e) {
            handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数异常", null);
        }
    }

    /**
     * 开始用户名和密码登陆
     */
    private void startPhoneLogin() {
        try {
            showDialog("正在登录中......");
            startLoginNet(LongDaConstant.LOGIN_PHONE, null,
                    null, null, mStrPhone, mStrPassword, mStrCode, mIntuserId);
        } catch (Exception e) {
            handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数异常", null);
        }
    }

    /**
     * 开始一键登陆
     */
    private void startOneKeyLogin() {
        try {
            showDialog("正在登录中......");
            startLoginNet(LongDaConstant.LOGIN_ONEKEY, null,
                    null, null, null, null, null, mIntuserId);
        } catch (Exception e) {
            handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数异常", null);
        }
    }

    /**
     * 之前登录过重新登录
     */
    private void startHasLogin() {
        try {
            int loginType;
            if (mLoginMethod == LongDaLoginMethod.WEIXIN) {
                loginType = LongDaConstant.LOGIN_WEIXIN;
            } else if (mLoginMethod == LongDaLoginMethod.QQ) {
                loginType = LongDaConstant.LOGIN_QQ;
            } else if (mLoginMethod == LongDaLoginMethod.PHONE) {
                loginType = LongDaConstant.LOGIN_PHONE;
            } else {
                loginType = LongDaConstant.LOGIN_ONEKEY;
            }

            showDialog("正在登录中......");
            startLoginNet(loginType, LongDaUtils.getLyLoginQqAppId(mContext),
                    null, null, null, null, null, mIntuserId);
        } catch (Exception e) {
            handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数异常", null);
        }
    }

    /**
     * 结果处理
     * @param type 类型
     * @param errorCode 错误码
     * @param errorMsg 错误内容
     * @param data 数据
     */
    public void handleResult(int type, int errorCode, String errorMsg, String data) {
        // 是否处理了回调事件
        if (0 >= mIntHandleResult) {
            return;
        }

        // 调用回调事件减一次
        mIntHandleResult--;

        // 处理开始事件
        if (type == LongDaConstant.LY_LOGIN_ON_START) {
            if (null != mlYLoginListener) {
                mlYLoginListener.onStart(mLoginMethod);
            }
        } else if (type == LongDaConstant.LY_LOGIN_ON_COMPLETE) {
            // 完成事件
            if (null != mlYLoginListener) {
                try {
                    mlYLoginListener.onComplete(mLoginMethod, errorCode, new JSONObject(data));
                } catch (Exception e) {
                    e.printStackTrace();
                    mlYLoginListener.onComplete(mLoginMethod, errorCode, null);
                }
            }
        } else if (type == LongDaConstant.LY_LOGIN_ON_ERROR) {
            // 错误事件
            if (null != mlYLoginListener) {
                mlYLoginListener.onError(mLoginMethod, errorCode, errorMsg);
            }
        } else if (type == LongDaConstant.LY_LOGIN_ON_CANCEL) {
            // 取消事件
            if (null != mlYLoginListener) {
                mlYLoginListener.onCancel(mLoginMethod, errorCode);
            }
        }

    }

    /**
     * qq 回调函数处理
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    public void onActivityReslt(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUiListener);
    }

    /**
     * 自定义handler
     */
    static class LyHandler extends Handler {
        WeakReference<LongDaLogin> LYLogin;

        LyHandler(LongDaLogin lyLogin) {
            this.LYLogin = new WeakReference<>(lyLogin);
        }

        public void handleMessage(Message msg) {
            LongDaLogin lyLogin = this.LYLogin.get();
            lyLogin.hideDialog();
            switch(msg.what) {
                // 登录成功
                case LongDaConstant.LY_HANDLE_LOGIN_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";
                    try {
                        String data = (String) msg.obj;

                        if (null != data) {
                            JSONObject json = new JSONObject(data);

                            int code = json.optInt("code");
                            resultMsg = json.optString("message");

//                            Toast.makeText(lyLogin.mContext, "code="+code+",message="+resultMsg, Toast.LENGTH_LONG).show();

                            if (200 == code) {
                                JSONObject resultData = json.optJSONObject("data");
                                int userId = resultData.optInt("userId");
                                String token = resultData.optString("token");

                                //记录相关数据
                                lyLogin.setLoginInfo(token, userId);

                                flag = true;
                                lyLogin.handleResult(LongDaConstant.LY_LOGIN_ON_COMPLETE, LongDaConstant.LY_RESULT_SUCCESS, resultMsg, data);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyLogin.handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, resultMsg, null);
                        }
                    }

                    break;
                }

                // 登录失败
                case LongDaConstant.LY_HANDLE_LOGIN_ERROR: {
                    lyLogin.handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, ((String) msg.obj), null);
                    break;
                }

                default: {
                    break;
                }
            }

        }
    }

    /**
     * 保存相关信息
     * @param token token值
     * @param userId userId
     */
    private void setLoginInfo(String token, int userId) {
        LongDaUtils.setUserId(mContext, userId);
        LongDaUtils.setLoginTypeSuccess(mContext, LongDaUtils.loginMethodConvertLoginType(mLoginMethod));
    }

    /**
     * 获取userid值
     * @param context 上下文
     */
    private void getLoginUserId(Context context) {
        if (mLoginMethod == LongDaUtils.loginTypeConvertLoginMethod(LongDaUtils.getLoginTypeSuccess(mContext))) {
            mIntuserId = LongDaUtils.getUserId(context);
        } else {
            mIntuserId = -1;
        }
    }

    /**
     * 开始登录
     * @param loginType 登录类型
     * @param qqAppId  qqAppId
     * @param qqOpenId qq openid
     * @param accessToken qq access_token
     * @param phone 手机号码
     * @param password 密码
     * @param code 验证码
     * @param userId 登录过的用户的userid字段
     */
    private void startLoginNet(int loginType, String qqAppId,
                               String qqOpenId, String accessToken,
                               String phone, String password, String code,
                               int userId) {
        LongDaLoginThread longDaLoginThread = new LongDaLoginThread(mContext, mLyHandler, loginType,
                null, null, null, qqAppId, qqOpenId, accessToken, phone, password, code, userId);
        longDaLoginThread.startLoginNet();
    }

    /**
     * QQ登录回调函数
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            /*
             * 下面隐藏的是用户登录成功后 登录用户数据的获取的方法
             * 共分为两种  一种是简单的信息的获取,另一种是通过UserInfo类获取用户较为详细的信息
             *有需要看看
             * */
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                //如果你不知道你能获得什么，看一下下面的LOG
                LongDaLog.d("-------------"+response.toString());
                String openidString = ((JSONObject) response).getString("openid");
                mTencent.setOpenId(openidString);

                mTencent.setAccessToken(((JSONObject) response).getString("access_token"),((JSONObject) response).getString("expires_in"));


                LongDaLog.d("-------------"+openidString);
                String access_token= ((JSONObject) response).getString("access_token");
//                String expires_in = ((JSONObject) response).getString("expires_in");
//                Toast.makeText(mContext, "openidString=" + openidString
//                        + "\naccess_token=" + access_token, Toast.LENGTH_LONG).show();
                startLoginNet(LongDaConstant.LOGIN_QQ, LongDaUtils.getLyLoginQqAppId(mContext),
                        openidString, access_token, null, null, null, mIntuserId);
            } catch (JSONException e) {
                e.printStackTrace();
                handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数错误", null);
            }

            /*
             到此已经获得OpneID以及其他你想获得的内容了
             QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
             sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
             如何得到这个UserInfo类呢？
             */

            /**
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(mContext.getApplicationContext(), qqToken);

            //    info.getUserInfo(new BaseUIListener(this,"get_simple_userinfo"));
            info.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    //用户信息获取到了

                    try {

                        String nickName = ((JSONObject) o).getString("nickname");
                        String gender = ((JSONObject) o).getString("gender");
                        String iconUrl = ((JSONObject) o).getString("figureurl_qq_1");
                        String openId = mTencent.getOpenId();

                        Toast.makeText(mContext, nickName + "\n" + gender + "\n" + iconUrl + "\n"
                        + openId + "\n", Toast.LENGTH_LONG).show();

                        JSONObject myJson = new JSONObject();
                        myJson.put(LongDaConstant.LY_LOGIN_TYPE, LongDaLoginMethod.WEIXIN);
                        myJson.put(LongDaConstant.LY_LOGIN_NICK_NAME, nickName);
                        myJson.put(LongDaConstant.LY_LOGIN_GENDER, gender);
                        myJson.put(LongDaConstant.LY_LOGIN_ICON_URL, iconUrl);
                        myJson.put(LongDaConstant.LY_LOGIN_OPENID, openId);
                        String myData = myJson.toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, LongDaConstant.LY_RESULT_ERROR, "参数错误", null);
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, uiError.errorCode, uiError.errorMessage, null);
                }

                @Override
                public void onCancel() {
                    handleResult(LongDaConstant.LY_LOGIN_ON_CANCEL, LongDaConstant.LY_RESULT_CANCEL, null, null);
                }
            });
             **/
        }

        @Override
        public void onError(UiError uiError) {
            handleResult(LongDaConstant.LY_LOGIN_ON_ERROR, uiError.errorCode, uiError.errorMessage, null);
        }

        @Override
        public void onCancel() {
            handleResult(LongDaConstant.LY_LOGIN_ON_CANCEL, LongDaConstant.LY_RESULT_CANCEL, null, null);
        }
    }

    /**
     * 显示进度框
     * @param msg 显示内容
     */
    private void showDialog(String msg) {
        if (LongDaLog.isShowDialog) {
            if (null == mDialog) {
                if (null != mContext) {
                    mDialog = new ProgressDialog(mContext);
                    mDialog.setMessage(msg);
                    mDialog.show();
                }
            } else if (!mDialog.isShowing()){
                mDialog.setMessage(msg);
                mDialog.show();
            } else {
                mDialog.cancel();
                mDialog.setMessage(msg);
                mDialog.show();
            }

        }
    }

    /**
     * 隐藏进度框
     */
    private void hideDialog() {
        if (LongDaLog.isShowDialog) {
            if ((null != mDialog) && (mDialog.isShowing())) {
                mDialog.cancel();
                mDialog = null;
            }
        }
    }
}
