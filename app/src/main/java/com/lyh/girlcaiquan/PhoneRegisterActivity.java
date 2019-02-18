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
import android.widget.TextView;
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
public class PhoneRegisterActivity extends Activity{
    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtCode;
    private MainLoginListener mainLoginListener;
    private boolean mIsShowDialog = true;
    private CountDownTimer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        init();
    }

    private void init() {
        mEtPhone = findViewById(R.id.et_user_name);
        mEtPassword = findViewById(R.id.et_password);
        mEtCode = findViewById(R.id.et_vercode);

        mainLoginListener = new MainLoginListener();
        findViewById(R.id.user_reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procLogin();
            }
        });

        findViewById(R.id.phone_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneRegisterActivity.this, PhoneLoginActivity.class));
            }
        });

        findViewById(R.id.phone_forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneRegisterActivity.this, PhoneForgetActivity.class));
            }
        });

        findViewById(R.id.send_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送验证码
                procCode();
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
    private void procLogin() {

        String phone = mEtPhone.getText().toString();
        String passwrod = mEtPassword.getText().toString();
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

        if (TextUtils.isEmpty(passwrod)) {
            Toast.makeText(this, "请输入密码！", Toast.LENGTH_LONG).show();
            return;
        }


        LongDaGame.login(PhoneRegisterActivity.this, LongDaLoginMethod.PHONE, mainLoginListener, phone, passwrod, code);
    }

    private class MainLoginListener implements LongDaLoginListener {

        @Override
        public void onStart(LongDaLoginMethod loginMethod) {
            Toast.makeText(PhoneRegisterActivity.this, "开始"+loginMethod, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete(LongDaLoginMethod loginMethod, int responseCode, JSONObject data) {

            JSONObject json = data;
            int code = json.optInt("code");
            String msg = json.optString("message");

            JSONObject result = json.optJSONObject("data");
            String token = result.optString("token");
            String nickName = result.optString("nickName");
            String userName = result.optString("userName");
            int userId = result.optInt("userId");
            int gender = result.optInt("gender");
            String avatar = result.optString("avatar");

            String sex = "男";
            if (0 == gender) {
                sex = "女";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneRegisterActivity.this);
            builder.setMessage(loginMethod + "登录完成:"
                    + "\n昵称=" + nickName
                    + "\n用户编号=" + userId
                    + "\n性别=" + sex);
            builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

        @Override
        public void onError(LongDaLoginMethod loginMethod, int responseCode, String errorMsg) {
            Toast.makeText(PhoneRegisterActivity.this, "登录错误"+loginMethod + ",responseCode="
                    + responseCode + ", errorMsg=" + errorMsg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(LongDaLoginMethod loginMethod, int responseCode) {
            Toast.makeText(PhoneRegisterActivity.this, "取消登录"+loginMethod, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onGetPassword(int responseCode, String errorMsg, JSONObject data) {

        }

        @Override
        public void onCodeConfirm(int responseCode, String errorMsg) {

        }

        @Override
        public void onSendCode(int responseCode, String errorMsg) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
