package com.lyh.girlcaiquan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuan.shi.lonng.LongDaGame;
import com.yuan.shi.lonng.LongDaPayListener;
import com.yuan.shi.lonng.bean.LongDaPayScreen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by @author luyon
 *
 * @version 2.0  2018/9/13
 */
public class PayActivity extends Activity{
    private LongPayListener mPayListener;
    private EditText mEtGoodName;
    private EditText mEtGoodDesc;
    private EditText mEtGoodPrice;
    private TextView mTvScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        init();
    }

    private void init() {
        mPayListener = new LongPayListener();

        mEtGoodName = findViewById(R.id.goods_name);
        mEtGoodDesc = findViewById(R.id.goods_desc);
        mEtGoodPrice = findViewById(R.id.goods_price);
        mTvScreen = findViewById(R.id.pay_screen_ori);

        mTvScreen.setText("屏幕方向：自适应");
        LongDaGame.setPayScreenOrientation(PayActivity.this, LongDaPayScreen.SENSOR_LANDSCAPE);

        findViewById(R.id.wx_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procPay();
            }
        });

        findViewById(R.id.init_port).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvScreen.setText("屏幕方向：竖屏");
                LongDaGame.setPayScreenOrientation(PayActivity.this, LongDaPayScreen.PORTRAIT);
            }
        });

        findViewById(R.id.init_hand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvScreen.setText("屏幕方向：横屏");
                LongDaGame.setPayScreenOrientation(PayActivity.this, LongDaPayScreen.LANDSCAPE);
            }
        });

    }

    /**
     * 进行支付
     */
    private void procPay() {
        String orderId = System.currentTimeMillis() + "";
        int price = 100;//单位分
        String good = mEtGoodName.getText().toString();
        String goodDesc = mEtGoodDesc.getText().toString();
        String goodPrice = mEtGoodPrice.getText().toString();

        if (TextUtils.isEmpty(good)) {
            Toast.makeText(this, "请输入商品名！", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(goodDesc)) {
            Toast.makeText(this, "请输入商品描述！", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(goodPrice)) {
            Toast.makeText(this, "请输入商品价格！", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isNumeric(goodPrice)) {
            Toast.makeText(this, "金额必须是数字！", Toast.LENGTH_LONG).show();
            return;
        }

        price = Integer.valueOf(goodPrice);

        LongDaGame.createPayment(PayActivity.this, price, good, goodDesc, orderId, mPayListener);
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private class LongPayListener  implements LongDaPayListener {

        /**
         * @param responseCode 处理返回值 90000支付成功，90001支付失败,90002取消支付
         * @param payInfo 支付信息
         * @param errorMsg 错误消息
         */
        @Override
        public void onResult(int responseCode, String payInfo, String errorMsg) {

            String payResult = "支付成功";

            if (90001 == responseCode) {
                payResult = "支付失败";
            } else if (90002 == responseCode) {
                payResult = "用户取消";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
            builder.setMessage("支付结果:" + payResult);
            builder.setPositiveButton("确认", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
    }
}
