package com.yuan.shi.lonng.handler;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.yuan.shi.lonng.LongDaPayListener;
import com.yuan.shi.lonng.R;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.dialog.LongDaLoadingDialog;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/5/11
 */
public class LongDaPayObject {
    public Activity activity; // 上下文，接入方传入的
    public LongDaPayListener longDaPayListener; // 支付回调函数
    public LongDaLoadingDialog longDaLoadingDialog; // 加载动画框实例
    public int payCode; // 支付错误码
    public String payFailMsg; // 支付错误消息
    public boolean hasStartPay; // 是否启动了支付标识

    private LongDaPayObject() {
        this.longDaPayListener = null;
        this.activity = null;
        this.longDaLoadingDialog = null;
        this.payCode = LongDaConstant.LY_RESULT_UNDEFINED;
        this.payFailMsg = null;
        this.hasStartPay = false;
    }

    public static LongDaPayObject getInstance() {
        return PpayObject.instance;
    }

    private static class PpayObject {
        private static final LongDaPayObject instance = new LongDaPayObject();
    }
}
