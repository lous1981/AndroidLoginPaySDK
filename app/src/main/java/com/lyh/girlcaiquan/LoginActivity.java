package com.lyh.girlcaiquan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
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
public class LoginActivity extends Activity{
    private MainLoginListener mainLoginListener;
    private boolean mIsShowDialog = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        LongDaGame.setQQLogin(this,"1107847770", "PKhUjpj0AfT6y8HG");
        LongDaGame.setWeiXinLogin(this, "wx953511b1e5f62b01", "97f2aca1bd99c570b5ed9e2b772abc96");
        mainLoginListener = new MainLoginListener();
        findViewById(R.id.qq_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongDaGame.login(LoginActivity.this, LongDaLoginMethod.QQ, mainLoginListener, "", "", "");
            }
        });

        findViewById(R.id.weixin_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongDaGame.login(LoginActivity.this, LongDaLoginMethod.WEIXIN, mainLoginListener, "", "", "");
            }
        });

        findViewById(R.id.user_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));

            }
        });

        findViewById(R.id.onekey_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongDaGame.login(LoginActivity.this, LongDaLoginMethod.ONEKEY, mainLoginListener, null, null, "");
            }
        });

        findViewById(R.id.exit_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LongDaGame.loginOut(LoginActivity.this);
            }
        });

        ((Button)findViewById(R.id.show_dialog)).setText("显示进度框:关");
        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsShowDialog) {
                    ((Button)findViewById(R.id.show_dialog)).setText("显示进度框:开");
                    LongDaGame.enableDialog(mIsShowDialog);
                    mIsShowDialog = false;
                } else {
                    ((TextView)findViewById(R.id.show_dialog)).setText("显示进度框:关");
                    LongDaGame.enableDialog(mIsShowDialog);
                    mIsShowDialog = true;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
    }

    private class MainLoginListener implements LongDaLoginListener {

        @Override
        public void onStart(LongDaLoginMethod loginMethod) {
            Toast.makeText(LoginActivity.this, "开始"+loginMethod, Toast.LENGTH_LONG).show();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            Toast.makeText(LoginActivity.this, "登录错误"+loginMethod + ",responseCode="
                    + responseCode + ", errorMsg=" + errorMsg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(LongDaLoginMethod loginMethod, int responseCode) {
            Toast.makeText(LoginActivity.this, "取消登录"+loginMethod, Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LongDaGame.onActivityReslt(requestCode, resultCode, data);
    }
}
