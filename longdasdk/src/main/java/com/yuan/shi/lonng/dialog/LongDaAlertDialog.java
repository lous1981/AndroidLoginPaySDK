package com.yuan.shi.lonng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuan.shi.lonng.R;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaAlertDialog extends Dialog{

    private TextView mTvMsg;
    private ImageView mIvClose;
    private Button mBtnConfirm;
    private Context mContext;
    private LongDaAlertDialogOnClickListener longDaDialogOnClickListener;

    public LongDaAlertDialog(Context context) {
        super(context, R.style.longda_custom_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.longda_layout_alert_dialog);

        mTvMsg = findViewById(R.id.longda_tv_msg);
        mIvClose = findViewById(R.id.longda_iv_close);
        mBtnConfirm = findViewById(R.id.longda_btn_dialog_confirm);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != longDaDialogOnClickListener) {
                    longDaDialogOnClickListener.onClick(true);
                }
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
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
     * @param msg 消息内容
     * @param cancelText 取消按钮内容
     * @param confirmText 确认按钮内容
     * @param isShowCancel 是否显示取消按钮
     * @param isShowConfirm 是否显示确定按钮
     * @param onClickListener 回调函数
     */
    public void setInfo(String msg, String cancelText, String confirmText, boolean isShowCancel,
                         boolean isShowConfirm, LongDaAlertDialogOnClickListener onClickListener) {
        if (!TextUtils.isEmpty(msg)) {
            mTvMsg.setText(msg);
        }

        if (!TextUtils.isEmpty(confirmText)) {
            mBtnConfirm.setText(confirmText);
        }

        if (isShowCancel) {
            mIvClose.setVisibility(View.VISIBLE);
        } else {
            mIvClose.setVisibility(View.GONE);
        }

        if (isShowConfirm) {
            mBtnConfirm.setVisibility(View.VISIBLE);
        } else {
            mBtnConfirm.setVisibility(View.GONE);
        }
        longDaDialogOnClickListener = onClickListener;
    }

    public interface LongDaAlertDialogOnClickListener{
        void onClick(boolean confirm);
    }
}
