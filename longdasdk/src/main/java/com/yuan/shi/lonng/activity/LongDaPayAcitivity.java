package com.yuan.shi.lonng.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuan.shi.lonng.R;
import com.yuan.shi.lonng.constant.LongDaConstant;
import com.yuan.shi.lonng.dao.LongDaPayItemData;
import com.yuan.shi.lonng.dialog.LongDaCustomDialog;
import com.yuan.shi.lonng.dialog.LongDaLoadingDialog;
import com.yuan.shi.lonng.handler.LongDaPayObject;
import com.yuan.shi.lonng.thread.LongDaPayThread;
import com.yuan.shi.lonng.ui.widget.LongDaMarqueeTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by @author luyon
 *
 * @version 2.0  2018/10/16
 */
public class LongDaPayAcitivity extends LongDaBaseActivity{

    private LinearLayout mLLPayList;
    private List<LongDaPayItemData> mPayItemData;
    private int lastSelectIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        if (mBIsPortrait) {
            setContentView(R.layout.longda_layout_ui_pay_v);
        } else {
            setContentView(R.layout.longda_layout_ui_pay_h);
        }
        init();
        initData();
    }

    /**
     * 初始化
     */
    private void init() {
        //支付列表
        mLLPayList = findViewById(R.id.longda_ll_pay_list);

        /*
         设置支付项数据
        */
        mPayItemData = new ArrayList<>();

        LongDaPayItemData itemData = new LongDaPayItemData();
        itemData.setPayName("微信支付");
        itemData.setPayType(LongDaConstant.LY_PAY_TYPE_WEIXIN);
        itemData.setSelected(true);
        mPayItemData.add(itemData);

        itemData = new LongDaPayItemData();
        itemData.setPayName("支付宝支付");
        itemData.setPayType(LongDaConstant.LY_PAY_TYPE_ALI);
        itemData.setSelected(false);
        mPayItemData.add(itemData);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            /*
             解析数据
             */
            String strData = getIntent().getStringExtra(LongDaConstant.EXTRA_CHARGE);
            mPayDatas = strData;
            JSONObject json = new JSONObject(strData);
            int price = json.optInt("price");
            String goods = json.optString("goods");
            String goodsDesc = json.optString("goodsDesc");
            String orderId = json.optString("orderId");
            String payType = json.optString("payType");

            //提示 ，跑马灯
            ((LongDaMarqueeTextView)(findViewById(R.id.longda_tv_notice_alongda))).setText("24小时客服QQ公众号/电话:400123123123 微信公众号: 龙达龙达龙达龙达龙达龙达龙达");
            double dprice = price / 100.0;
            // 金额
            ((TextView)findViewById(R.id.longda_tv_price_alongda)).setText(format1(dprice));
            // 确认金额
            ((TextView)findViewById(R.id.longda_tv_bottom_submit_btn)).setText("确认支付 ¥" + format1(dprice));
            // 商品名
            ((TextView)findViewById(R.id.longda_tv_wares_name_alongda)).setText(goods);
            // 订单id
            ((TextView)findViewById(R.id.longda_tv_wares_order_id_alongda)).setText(orderId);
            // QQ号码
            ((TextView)findViewById(R.id.longda_tv_pay_hub_qq_bottom)).setText("12345678");
            // 更多支付方式
            findViewById(R.id.longda_ll_more_paytype).setVisibility(View.INVISIBLE);

            // 动态添加布局
            dyNicAddLayout();

            // 确认支付
            findViewById(R.id.longda_tv_bottom_submit_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    procPayInfo();
                }
            });

            // 返回处理
            findViewById(R.id.longda_activity_title_bar_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCancelPayDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化处理，保留两位小数
     * @param value 数值
     * @return 保留两位小数
     */
    private static String format1(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }

    /**
     * 更新支付项
     * @param position 选中的项
     */
    private void updateListItem(int position) {
        for (int i = 0; i < mPayItemData.size(); i++) {
            if (0 == position) {
                mPayType = LongDaConstant.LY_PAY_TYPE_WEIXIN;
            } else {
                mPayType = LongDaConstant.LY_PAY_TYPE_ALI;
            }
        }
    }

    /**
     * 动态添加支付方式
     */
    private void dyNicAddLayout() {
        mPayType = "wxPay";
        lastSelectIndex = 0;

        for (int i = 0; i < mPayItemData.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View convertView = inflater.inflate(R.layout.longda_layout_ui_pay_type_item,
                    null, true);
            LinearLayout linearLayout = convertView.findViewById(R.id.longda_LinearLayout);
            ImageView ivPayIcon = convertView.findViewById(R.id.longda_iv_pay_type_icon);
            TextView tvPayName = convertView.findViewById(R.id.longda_tv_pay_type_name);
            TextView tvPayNotice = convertView.findViewById(R.id.longda_tv_pay_type_discount);
            TextView tvPayMsg = convertView.findViewById(R.id.longda_tv_pay_type_msg);
            final ImageView ivHasSelected = convertView.findViewById(R.id.longda_iv_item_right);

            ivHasSelected.setTag("select" + i);

            LongDaPayItemData data = mPayItemData.get(i);

            if (data.getPayType().trim().equals(LongDaConstant.LY_PAY_TYPE_WEIXIN)) {
                ivPayIcon.setImageResource(R.drawable.longda_icon_paytype_weixin);
                tvPayName.setText(data.getPayName());
                tvPayNotice.setVisibility(View.GONE);
                tvPayMsg.setVisibility(View.GONE);
                if (data.isSelected()) {
                    ivHasSelected.setImageResource(R.drawable.longda_ui_checked);
                } else {
                    ivHasSelected.setImageResource(R.drawable.longda_ui_unchecked);
                }
            } else {
                ivPayIcon.setImageResource(R.drawable.longda_icon_paytype_alipay);
                tvPayName.setText(data.getPayName());
                tvPayNotice.setVisibility(View.GONE);
                tvPayMsg.setVisibility(View.GONE);
                if (data.isSelected()) {
                    ivHasSelected.setImageResource(R.drawable.longda_ui_checked);
                } else {
                    ivHasSelected.setImageResource(R.drawable.longda_ui_unchecked);
                }
            }

            mLLPayList.addView(convertView);

            final int pos = i;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateListItem(pos);
                    itemClickAction(pos);
                }
            });
        }
    }

    /**
     * 处理动态布局每一条linearLayout的点击事件
     * @param pos 点击位置
     */
    private void itemClickAction(int pos) {
        
        ImageView iv = ((LinearLayout) mLLPayList.getChildAt(lastSelectIndex)).findViewWithTag("select" + lastSelectIndex);
        iv.setImageResource(R.drawable.longda_ui_unchecked);
        ImageView iv1 = ((LinearLayout) mLLPayList.getChildAt(pos)).findViewWithTag("select" + pos);
        iv1.setImageResource(R.drawable.longda_ui_checked);
        lastSelectIndex = pos;
    }

    /**
     * 处理支付
     */
    private void procPayInfo() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(mPayDatas);
            jsonObject.put("payType", mPayType);

            mPayDatas = jsonObject.toString();

            // 获取网络信息
            startGetPayInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启获取支付信息
     */
    public void startGetPayInfo() {
        LongDaPayThread longDaPayThread = new LongDaPayThread(mLyHandler, mPayDatas, mPayType);
        longDaPayThread.startPayInfoNet();
        LongDaPayObject.getInstance().payCode = LongDaConstant.LY_RESULT_ERROR;
        LongDaPayObject.getInstance().payFailMsg = "网络错误";

        showLoadingDialog("正在支付中...");
    }

    /**
     * 显示提示框
     * @param msg 提示内容
     */
    @Override
    protected void showTipDialog(String msg) {
//        final LongDaAlertDialog dialog = new LongDaAlertDialog(this);
//        dialog.show();
//        dialog.setInfo(msg, "", "", false, true, new LongDaAlertDialog.LongDaAlertDialogOnClickListener() {
//            @Override
//            public void onClick(boolean confirm) {
//                dialog.cancel();
//            }
//        });
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        hideDialog();
    }

    /**
     * 显示取消支付动画框
     */
    private void showCancelPayDialog() {
        final LongDaCustomDialog dialog = new LongDaCustomDialog(mActivity);
        dialog.show();
        dialog.setInfo("确认放弃购买商品?", "", "", true, true,
                new LongDaCustomDialog.LongDaDialogOnClickListener() {
                    @Override
                    public void onClick(boolean confirm) {
                        if (confirm) {
                            handleQuitResult("");
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        showCancelPayDialog();
    }
}
