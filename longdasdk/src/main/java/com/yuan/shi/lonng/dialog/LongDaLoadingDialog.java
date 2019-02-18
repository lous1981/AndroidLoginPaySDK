package com.yuan.shi.lonng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.TextView;

import com.yuan.shi.lonng.R;
import com.yuan.shi.lonng.utils.LongDaResourceUtils;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaLoadingDialog extends Dialog{
    private TextView mTvMsg;

    public LongDaLoadingDialog(Context context) {
        super(context, R.style.longda_custom_dialog);
        setContentView(R.layout.longda_layout_dialog_loading);
        mTvMsg = findViewById(R.id.longda_loading_dialog_msg);
        setCanceledOnTouchOutside(false);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LongDaLoadingDialog setMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            mTvMsg.setText(message);
        }
        return this;
    }
}
