package com.yuan.shi.lonng.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.yuan.shi.lonng.LongDaLoginListener;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.thread.LongDaCodeConfirmThread;
import com.yuan.shi.lonng.thread.LongDaGetPasswordThread;
import com.yuan.shi.lonng.thread.LongDaSendCodeThread;
import com.yuan.shi.lonng.utils.LongDaLog;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/17
 */
public class LongDaOther {
    private static LongDaLoginListener mlYLoginListener;
    private Context mContext;
    private static String mStrPhone;
    private static String mStrCode;
    private LOtherHandler mLyHandler;
    private static ProgressDialog mDialog;


    public static LongDaOther getInstance() {
        return LongDaOther.LyOther.instance;
    }

    private static class LyOther {
        private static final LongDaOther instance = new LongDaOther();
    }

    public void setInfo(Context context, String phone, String code, LongDaLoginListener listener) {
        mContext = context;
        mStrPhone = phone;
        mStrCode = code;
        mlYLoginListener = listener;
        mLyHandler = new LOtherHandler(this);
    }

    /**
     * 开始获取密码
     */
    public void startGetPassword() {
        LongDaGetPasswordThread passwordThread = new LongDaGetPasswordThread(mContext, mStrPhone, mStrCode, mLyHandler);
        passwordThread.start();
        showDialog("正在处理中......");
    }

    /**
     * 开始校验验证码是否正确
     */
    public void startCodeConfirm() {
        LongDaCodeConfirmThread codeConfirmThread = new LongDaCodeConfirmThread(mContext, mStrPhone, mStrCode, mLyHandler);
        codeConfirmThread.start();
        showDialog("正在处理中......");
    }

    /**
     * 发送验证码
     */
    public void startSendCode() {
        LongDaSendCodeThread sendCodeThread = new LongDaSendCodeThread(mContext, mStrPhone, mStrCode, mLyHandler);
        sendCodeThread.start();
        showDialog("正在处理中......");
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

    /**
     * 结果处理
     * @param errorCode 错误码
     * @param errorMsg 错误内容
     * @param data 数据
     */
    public void handlePasswordResult(int errorCode, String errorMsg, String data) {
        // 处理开始事件
        if (null != mlYLoginListener) {
            try {
                JSONObject json = null;
                if (!TextUtils.isEmpty(data)) {
                    json = new JSONObject(data);
                }
                mlYLoginListener.onGetPassword(errorCode, errorMsg, json);
            } catch (Exception e) {
                e.printStackTrace();
                mlYLoginListener.onGetPassword(errorCode, errorMsg, null);
            }
        }

    }

    /**
     * 结果处理
     * @param errorCode 错误码
     * @param errorMsg 错误内容
     */
    public void handleCodeResult(int errorCode, String errorMsg) {
        // 处理开始事件
        if (null != mlYLoginListener) {
            try {
                mlYLoginListener.onCodeConfirm(errorCode, errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 结果处理
     * @param errorCode 错误码
     * @param errorMsg 错误内容
     */
    public void handleSendCodeResult(int errorCode, String errorMsg) {
        // 处理开始事件
        if (null != mlYLoginListener) {
            try {
                mlYLoginListener.onSendCode(errorCode, errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * 自定义handler
     */
    static class LOtherHandler extends Handler {
        WeakReference<LongDaOther> LYother;

        LOtherHandler(LongDaOther lyLogin) {
            this.LYother = new WeakReference<>(lyLogin);
        }

        public void handleMessage(Message msg) {
            LongDaOther lyOther = this.LYother.get();
            lyOther.hideDialog();
            switch(msg.what) {
                // 获取密码成功
                case LongDaConstant.LY_HANDLE_PROC_PASSWORD_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";
                    try {
                        String data = (String) msg.obj;

                        if (null != data) {
                            JSONObject json = new JSONObject(data);

                            int code = json.optInt("code");
                            resultMsg = json.optString("message");

                            if (200 == code) {
                                flag = true;

                                JSONObject json1 = json.optJSONObject("data");
                                String result = null;
                                if (null != json1) {
                                    result = json1.toString();
                                }

                                lyOther.handlePasswordResult(LongDaConstant.LY_RESULT_SUCCESS, resultMsg, result);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyOther.handlePasswordResult(LongDaConstant.LY_RESULT_ERROR, resultMsg, null);
                        }
                    }

                    break;
                }

                // 获取密码失败
                case LongDaConstant.LY_HANDLE_PROC_PASSWORD_ERROR: {
                    lyOther.handlePasswordResult(LongDaConstant.LY_RESULT_ERROR, ((String) msg.obj), null);
                    break;
                }

                // 验证码成功
                case LongDaConstant.LY_HANDLE_PROC_CODE_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";
                    try {
                        String data = (String) msg.obj;

                        if (null != data) {
                            JSONObject json = new JSONObject(data);

                            int code = json.optInt("code");
                            resultMsg = json.optString("message");

                            if (200 == code) {
                                flag = true;
                                lyOther.handleCodeResult(LongDaConstant.LY_RESULT_SUCCESS, resultMsg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyOther.handleCodeResult(LongDaConstant.LY_RESULT_ERROR, resultMsg);
                        }
                    }

                    break;
                }

                // 验证码失败
                case LongDaConstant.LY_HANDLE_PROC_CODE_ERROR: {
                    lyOther.handleCodeResult(LongDaConstant.LY_RESULT_ERROR, ((String) msg.obj));
                    break;
                }

                // 验证码成功
                case LongDaConstant.LY_HANDLE_PROC_SEND_CODE_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";
                    try {
                        String data = (String) msg.obj;

                        if (null != data) {
                            JSONObject json = new JSONObject(data);

                            int code = json.optInt("code");
                            resultMsg = json.optString("message");

                            if (200 == code) {
                                flag = true;
                                lyOther.handleSendCodeResult(LongDaConstant.LY_RESULT_SUCCESS, resultMsg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyOther.handleSendCodeResult(LongDaConstant.LY_RESULT_ERROR, resultMsg);
                        }
                    }

                    break;
                }

                // 验证码失败
                case LongDaConstant.LY_HANDLE_PROC_SEND_CODE_ERROR: {
                    lyOther.handleSendCodeResult(LongDaConstant.LY_RESULT_ERROR, ((String) msg.obj));
                    break;
                }

                default: {
                    break;
                }
            }

        }
    }
}
