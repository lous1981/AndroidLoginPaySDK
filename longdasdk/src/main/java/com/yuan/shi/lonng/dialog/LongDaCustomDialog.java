package com.yuan.shi.lonng.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yuan.shi.lonng.R;
import com.yuan.shi.lonng.utils.LongDaResourceUtils;

import org.w3c.dom.Text;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaCustomDialog extends Dialog{

    private TextView mTvMsg;
    private TextView mTvConfirm;
    private TextView mTvCancel;
    private Context mContext;
    private LongDaDialogOnClickListener longDaDialogOnClickListener;

    public LongDaCustomDialog(Context context) {
        super(context, R.style.longda_custom_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.longda_layout_bg_round_rectangle_dialog);

        mTvMsg = findViewById(R.id.longda_tv_dialog_msg);
        mTvConfirm = findViewById(R.id.longda_tv_dialog_confirm);
        mTvCancel = findViewById(R.id.longda_tv_dialog_cancel);

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != longDaDialogOnClickListener) {
                    longDaDialogOnClickListener.onClick(true);
                }
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
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
     * @param msg 消息正文
     * @param cancelText 取消按钮内容
     * @param confirmText 确认按钮内容
     * @param isShowCancel 是否显示取消按钮
     * @param isShowConfirm 是否显示确定按钮
     * @param onClickListener 回调函数
     */
    public void setInfo(String msg, String cancelText, String confirmText, boolean isShowCancel,
                         boolean isShowConfirm, LongDaDialogOnClickListener onClickListener) {
        if (!TextUtils.isEmpty(msg)) {
            mTvMsg.setText(msg);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            mTvCancel.setText(cancelText);
        }

        if (!TextUtils.isEmpty(confirmText)) {
            mTvConfirm.setText(confirmText);
        }

        if (isShowCancel) {
            mTvCancel.setVisibility(View.VISIBLE);
        } else {
            mTvCancel.setVisibility(View.GONE);
        }

        if (isShowConfirm) {
            mTvConfirm.setVisibility(View.VISIBLE);
        } else {
            mTvConfirm.setVisibility(View.GONE);
        }
        longDaDialogOnClickListener = onClickListener;
    }

    public interface LongDaDialogOnClickListener{
        void onClick(boolean confirm);
    }
}
