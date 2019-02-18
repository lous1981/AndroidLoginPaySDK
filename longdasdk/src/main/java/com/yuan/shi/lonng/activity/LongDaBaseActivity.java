package com.yuan.shi.lonng.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.yuan.shi.lonng.LongDaPayListener;
import com.yuan.shi.lonng.R;
import com.yuan.shi.lonng.bean.LongDaPayScreen;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.dialog.LongDaCommonDialog;
import com.yuan.shi.lonng.dialog.LongDaLoadingDialog;
import com.yuan.shi.lonng.handler.LongDaPayObject;
import com.yuan.shi.lonng.thread.LongDaPayThread;
import com.yuan.shi.lonng.utils.LongDaUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaBaseActivity extends Activity{

    // 是否是横竖屏的标识，true 竖屏
    protected boolean mBIsPortrait;

    // 支付宝支付结果标识
    protected static final int ALI_PAY_SUCCESS = 9000;
    protected static final int ALI_PAY_USER_CANCEL = 6001;

    // 回调函数
    protected LongDaPayListener longDaPayListener;
    // 支付参数
    protected String mPayDatas;
    protected LongDaBaseActivity mActivity;
    // 支付主线程处理
    protected LyPayHandler mLyHandler;
    //支付类型
    protected String mPayType = LongDaConstant.LY_PAY_TYPE_WEIXIN;
    // webview 启动支付宝和微信支付
    protected WebView mWebView;
    protected String mStrRegUrl = "http://www.xxx.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LongDaAppManager.getAppManager().addActivity(this);

        judgeScreenOri();

        mLyHandler = new LyPayHandler(this);
        longDaPayListener = LongDaPayObject.getInstance().longDaPayListener;
        LongDaPayObject.getInstance().hasStartPay = false;

        initWebView();
    }

    /**
     * 进行横竖屏的判断
     */
    private void judgeScreenOri() {

        // 取得当前屏幕方向
        int orient = getRequestedOrientation();
        // 若非明确的landscape或portrait时 再透过宽高比例的方法来确认实际显示方向
        // 这会保证orient最终值会是明确的横屏landscape或竖屏portrait
        if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //宽>高为横屏,反正为竖屏
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            orient = width < height ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }

        mBIsPortrait = false;
        int payScreen = LongDaUtils.getLyPayScreenOri(this);

        if (LongDaPayScreen.getName(payScreen).equals(LongDaPayScreen.LANDSCAPE.getName())) {
            mBIsPortrait = false;

            if(orient == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (LongDaPayScreen.getName(payScreen).equals(LongDaPayScreen.PORTRAIT.getName())) {
            mBIsPortrait = true;

            if(orient == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                //竖屏
                mBIsPortrait = true;
            } else {
                //横屏
                mBIsPortrait = false;
            }
        }
    }

    /**
     * 初始化
     */
    private void initWebView() {
        //方式1：直接在在Activity中生成
        mWebView = new WebView(LongDaBaseActivity.this);


        WebSettings webSettings = mWebView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {
                hideDialog();
                LongDaPayObject.getInstance().hasStartPay = true;
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                startActivity(intent1);
                return true;


//                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
//                if (url.startsWith("weixin://wap/pay?") || url.startsWith("weixin")|| url.startsWith("wechat")) {
//                    LongDaPayObject.getInstance().hasStartPay = true;
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//
//                // 支付宝
//                if(url.startsWith("alipays:") || url.startsWith("alipay")) {
//                    try {
//                        LongDaPayObject.getInstance().hasStartPay = true;
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse(url));
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//
//                if (!(url.startsWith("http") || url.startsWith("https"))) {
//                    return true;
//                }
//
//                if (mPayType.equals(LongDaConstant.LY_PAY_TYPE_WEIXIN)) {
//                    Map<String, String> extraHeaders = new HashMap<>();
//                    extraHeaders.put("Referer", mStrRegUrl);
//                    view.loadUrl(url, extraHeaders);
//                    return true;
//                } else if (mPayType.equals(LongDaConstant.LY_PAY_TYPE_ALI)) {
//                    /**
//                     * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
//                     */
//                    final PayTask task = new PayTask(mActivity);
//                    boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
//                        @Override
//                        public void onPayResult(final H5PayResultModel result) {
//                            // 支付结果返回
//                            final String url = result.getReturnUrl();
//                            String resultCode = result.getResultCode();
//
//                            handlerAliResult(resultCode);
//                        }
//                    });
//
//                    /**
//                     * 判断是否成功拦截
//                     * 若成功拦截，则无需继续加载该URL；否则继续加载
//                     */
//                    if (!isIntercepted) {
//                        view.loadUrl(url);
//                    }
//                    return true;
//                }
//                else {
//                    super.shouldOverrideUrlLoading(view, url);
//                    return true;
//                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

    }

    /**
     * 加载网页
     * @param url 网址
     */
    protected void loadWebView(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 隐藏加载框
     */
    protected void hideDialog() {
        if (null != LongDaPayObject.getInstance().longDaLoadingDialog) {
            LongDaPayObject.getInstance().longDaLoadingDialog.cancel();
            LongDaPayObject.getInstance().longDaLoadingDialog = null;
        }
    }

    /**
     * 显示提示内容
     * @param msg 提示内容
     */
    protected void showLoadingDialog(String msg) {
        if (null != LongDaPayObject.getInstance().longDaLoadingDialog) {
            LongDaPayObject.getInstance().longDaLoadingDialog.cancel();
            LongDaPayObject.getInstance().longDaLoadingDialog = null;
        }

        LongDaLoadingDialog dialog = new LongDaLoadingDialog(mActivity);
        dialog.show();
        dialog.setMessage(msg);
        LongDaPayObject.getInstance().longDaLoadingDialog = dialog;
    }

    /**
     * 错误提示
     * @param msg 消息内容
     */
    protected void showTipDialog(String msg) {

    }

    /**
     * 获取支付状态
     */
    protected void startGetPayStatus() {
        LongDaPayThread longDaPayThread = new LongDaPayThread(mLyHandler, mPayDatas, mPayType);
        longDaPayThread.startPayResultNet();

        showLoadingDialog("支付确认中...");
    }

    /**
     * 显示支付完成对话框
     */
    protected void showPayFinishedAlertDialog() {
        if (null != mActivity) {
            final LongDaCommonDialog dialog = new LongDaCommonDialog(mActivity);
            dialog.show();
            dialog.setInfo("", "", "", true, true, new LongDaCommonDialog.LongDaCommonDialogOnClickListener() {

                @Override
                public void onClick(boolean confirm) {
                    dialog.cancel();
                    if (confirm ) {
                        startGetPayStatus();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LongDaPayObject.getInstance().hasStartPay) {
            Toast.makeText(this, "恢复被调用！", Toast.LENGTH_LONG).show();
            LongDaPayObject.getInstance().hasStartPay = false;

            startGetPayStatus();
        }
    }



    /**
     * 支付宝处理结果
     * @param code 标识支付状态，含义如下：9000——订单支付成功 8000——正在处理中 4000——订单支付失败
     *             5000——重复请求 6001——用户中途取消 6002——网络连接出错
     */
    private void handlerAliResult(String code) {
        LongDaPayListener longDaPayListener = LongDaPayObject.getInstance().longDaPayListener;
        if (null != longDaPayListener) {
            LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_ERROR;
            if (code.equals("9000")) {
                LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_SUCCESS;
                LongDaPayObject.getInstance().payFailMsg = "";
            } else if (code.equals("4000")) {
                LongDaPayObject.getInstance().payFailMsg = "订单支付失败";
            } else if (code.equals("5000")) {
                LongDaPayObject.getInstance().payFailMsg = "重复请求";
            } else if (code.equals("6001")) {
                LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_CANCEL;
                LongDaPayObject.getInstance().payFailMsg = "取消支付";
            } else {
                LongDaPayObject.getInstance().payFailMsg = "其他错误";
            }
        }
    }

    /**
     * 处理回调事件
     */
    protected void handleQuitResult(String data) {
        if (null != longDaPayListener) {

            int resultCode = LongDaPayObject.getInstance().payCode;

            // 没有点击过支付，就是用户取消
            if (resultCode == LongDaConstant.LY_RESULT_UNDEFINED) {
                resultCode = LongDaConstant.LY_RESULT_CANCEL;
                LongDaPayObject.getInstance().payFailMsg = "用户取消";
            }

            // 回调调用
            longDaPayListener.onResult(resultCode, data, LongDaPayObject.getInstance().payFailMsg);
        }

        // 初始化实例
        LongDaPayObject.getInstance().activity = null;
        LongDaPayObject.getInstance().longDaPayListener = null;
        LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_UNDEFINED;
        LongDaPayObject.getInstance().payFailMsg = null;
        LongDaPayObject.getInstance().longDaLoadingDialog = null;
        longDaPayListener = null;

        // 关闭所有的Acitivity
        LongDaAppManager.getAppManager().finishAllActivity();
    }

    /**
     * 自定义handler
     */
    static class LyPayHandler extends Handler {
        WeakReference<LongDaBaseActivity> LYPay;

        LyPayHandler(LongDaBaseActivity longDaBaseActivity) {
            this.LYPay = new WeakReference<>(longDaBaseActivity);
        }

        public void handleMessage(Message msg) {
            LongDaBaseActivity lyPay = this.LYPay.get();
            switch(msg.what) {
                // 获取支付信息成功
                case LongDaConstant.LY_HANDLE_PAY_INFO_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";
                    try {
                        String data = (String) msg.obj;

//                        if (null != data) {
//                            JSONObject json = new JSONObject(data);
//
//                            int code = json.optInt("code");
//                            resultMsg = json.optString("message");
//
//                            if (200 == code) {
//
//                                // 支付宝调用的方式
//                        Intent intent = new Intent(lyPay.mActivity, LongDaPayWebActivity.class);
//                        intent.putExtra(LongDaConstant.EXTRA_CHARGE, lyPay.mPayDatas);
//                        intent.putExtra(LongDaConstant.EXTRA_TYPE, lyPay.mPayType);
//                        intent.putExtra(LongDaConstant.EXTRA_PAY_DATA, data);
//                        lyPay.mActivity.startActivityForResult(intent, 100);
                        lyPay.loadWebView("https://dwz.cn/6ToAOyZh");

                        flag = true;
//                            }
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyPay.showTipDialog("网络失败，请稍后重试！");
                        }
                    }

                    break;
                }

                // 获取支付信息失败
                case LongDaConstant.LY_HANDLE_PAY_INFO_ERROR: {
                    lyPay.showTipDialog("网络失败，请稍后重试！");
                    break;
                }

                case LongDaConstant.LY_HANDLE_PAY_INFO_ALI_OK: {
                    Map payResult = (Map)msg.obj;

                    // 支付宝支付结果
                    try {
                        int resultStatus = Integer.valueOf((String)payResult.get("resultStatus"));


                        if (resultStatus == ALI_PAY_SUCCESS) {
                            LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_SUCCESS;
                            lyPay.handleQuitResult("");
                        } else if (resultStatus == ALI_PAY_USER_CANCEL) {
                            lyPay.showTipDialog("用户取消支付！");
                        } else {
                            String meno = (String)payResult.get("meno");
                            lyPay.showTipDialog("网络失败，请稍后重试！");
                        }
                    } catch (Exception e) {
                        lyPay.showTipDialog("网络失败，请稍后重试！");
                    }
                    break;
                }

                // 支付结果成功
                case LongDaConstant.LY_HANDLE_PAY_RESULT_OK: {
                    boolean flag = false;
                    String resultMsg = "网络错误";

                    try {
                        LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_SUCCESS;
                        lyPay.handleQuitResult("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (!flag) {
                            lyPay.showTipDialog("网络失败，请稍后重试！");
                        }
                    }
                    break;
                }

                // 支付结果失败
                case LongDaConstant.LY_HANDLE_PAY_RESULT_ERROR: {
                    lyPay.showTipDialog("网络失败，请稍后重试！");
                    break;
                }

                // 默认情况
                default: {
                    lyPay.showTipDialog("网络失败，请稍后重试！");
                    break;
                }
            }
        }
    }
}
