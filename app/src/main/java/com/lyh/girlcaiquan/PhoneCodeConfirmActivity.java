package com.lyh.girlcaiquan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yuan.shi.lonng.LongDaGame;
import com.yuan.shi.lonng.LongDaLoginListener;
import com.yuan.shi.lonng.bean.LongDaLoginMethod;

import org.json.JSONObject;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/13
 */
public class PhoneCodeConfirmActivity extends Activity{
    private EditText mEtPhone;
    private EditText mEtCode;
    private MainLoginListener mainLoginListener;
    private boolean mIsShowDialog = true;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        init();
    }

    private void init() {
        mEtPhone = findViewById(R.id.et_user_name);
        mEtCode = findViewById(R.id.et_vercode);

        mainLoginListener = new MainLoginListener();
        findViewById(R.id.user_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procCheckCode();
            }
        });

        findViewById(R.id.send_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送验证码
                procCode();
            }
        });

        findViewById(R.id.phone_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneCodeConfirmActivity.this, PhoneLoginActivity.class));
            }
        });

        findViewById(R.id.phone_regi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneCodeConfirmActivity.this, PhoneRegisterActivity.class));
            }
        });
    }

    private void procCode() {
        String phone = mEtPhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_LONG).show();
            return;
        }

        if (!MyUtils.isMobileNO(phone)) {
            Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_LONG).show();
            return;
        }

        LongDaGame.sendPhoneCode(this, phone, mainLoginListener);
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }

        ((Button)(findViewById(R.id.send_code))).setClickable(false);

        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second = (int)(millisUntilFinished / 1000);
                ((Button)(findViewById(R.id.send_code))).setText(second + "秒");
            }

            @Override
            public void onFinish() {
                ((Button)(findViewById(R.id.send_code))).setText("发送验证码");
                ((Button)(findViewById(R.id.send_code))).setClickable(true);
            }
        }.start();
    }

    /**
     * 处理登录
     */
    private void procCheckCode() {

        String phone = mEtPhone.getText().toString();
        String code = mEtCode.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码！", Toast.LENGTH_LONG).show();
            return;
        }

        if (!MyUtils.isMobileNO(phone)) {
            Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码！", Toast.LENGTH_LONG).show();
            return;
        }

        LongDaGame.checkCode(this, phone, code, mainLoginListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private class MainLoginListener implements LongDaLoginListener {

        @Override
        public void onStart(LongDaLoginMethod loginMethod) {
        }

        @Override
        public void onComplete(LongDaLoginMethod loginMethod, int responseCode, JSONObject data) {

        }

        @Override
        public void onError(LongDaLoginMethod loginMethod, int responseCode, String errorMsg) {
        }

        @Override
        public void onCancel(LongDaLoginMethod loginMethod, int responseCode) {
        }

        @Override
        public void onGetPassword(int responseCode, String errorMsg, JSONObject data) {

        }

        @Override
        public void onCodeConfirm(int responseCode, String errorMsg) {
            String tip = "验证码验证失败";
            if (90000 == responseCode) {
                tip = "验证码验证成功";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneCodeConfirmActivity.this);
            builder.setMessage("验证结果:"
                    + "\n状态=" + tip
                    + "\n原因=" + errorMsg);
            builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

        @Override
        public void onSendCode(int responseCode, String errorMsg) {

        }
    }
}
