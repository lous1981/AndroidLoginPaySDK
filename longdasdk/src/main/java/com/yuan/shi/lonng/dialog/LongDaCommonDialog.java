package com.yuan.shi.lonng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuan.shi.lonng.R;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaCommonDialog extends Dialog{

    private TextView mTvTitle;
    private Button mBtnPay;
    private Button mBtnCancel;
    private Context mContext;
    private LongDaCommonDialogOnClickListener longDaDialogOnClickListener;

    public LongDaCommonDialog(Context context) {
        super(context, R.style.longda_custom_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.longda_layout_common_alert_dialog);

        mTvTitle = findViewById(R.id.longda_common_dialog_title);
        mBtnPay = findViewById(R.id.longda_btn_dialog_confirm_pay);
        mBtnCancel = findViewById(R.id.longda_btn_dialog_repay);

        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != longDaDialogOnClickListener) {
                    longDaDialogOnClickListener.onClick(true);
                }
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != longDaDialogOnClickListener) {
                    longDaDialogOnClickListener.onClick(false);
                }
            }
        });
    }

    /**
     * 设置参数
     * @param title 消息标题
     * @param cancelText 取消按钮内容
     * @param confirmText 确认按钮内容
     * @param isShowCancel 是否显示取消按钮
     * @param isShowConfirm 是否显示确定按钮
     * @param onClickListener 回调函数
     */
    public void setInfo(String title, String cancelText, String confirmText, boolean isShowCancel,
                         boolean isShowConfirm, LongDaCommonDialogOnClickListener onClickListener) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            mBtnCancel.setText(cancelText);
        }

        if (!TextUtils.isEmpty(confirmText)) {
            mBtnPay.setText(confirmText);
        }

        if (isShowCancel) {
            mBtnCancel.setVisibility(View.VISIBLE);
        } else {
            mBtnCancel.setVisibility(View.GONE);
        }

        if (isShowConfirm) {
            mBtnPay.setVisibility(View.VISIBLE);
        } else {
            mBtnPay.setVisibility(View.GONE);
        }
        longDaDialogOnClickListener = onClickListener;
    }

    public interface LongDaCommonDialogOnClickListener{
        void onClick(boolean confirm);
    }
}
